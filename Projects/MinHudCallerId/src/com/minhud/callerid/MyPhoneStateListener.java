package com.minhud.callerid;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class MyPhoneStateListener extends Service
{
	private final String TAG = "MyPhoneStateListener";

	// Name of the connected device
	private String mConnectedDeviceName = null;
	// String buffer for outgoing messages
	private StringBuffer mOutStringBuffer;
	// Local Bluetooth adapter
	private BluetoothAdapter mBluetoothAdapter = null;
	// Member object for the chat services
	private BluetoothChatService mChatService = null;

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	public int onStartCommand(Intent intent, int flags, int startId)
	{
		return 1;
	}

	@Override
	public void onCreate()
	{
		Log.v(TAG, "onCreate");
		super.onCreate();
		StateListener phoneStateListener = new StateListener();
		TelephonyManager telephonymanager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		telephonymanager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

		// Get local Bluetooth adapter
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (!mBluetoothAdapter.isEnabled())
		{
			// Intent enableIntent = new
			// Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			// startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
			// Otherwise, setup the chat session
		}
		else
		{
			if (mChatService == null) setupChat();
		}
	}

	private void setupChat()
	{
		Log.d(TAG, "setupChat()");

		// Initialize the BluetoothChatService to perform bluetooth connections
		mChatService = new BluetoothChatService(this, null);

		// Initialize the buffer for outgoing messages
		mOutStringBuffer = new StringBuffer("");

		Log.v(TAG, "ADD: " + "00:06:66:46:C2:46");
		// Get the BLuetoothDevice object
		BluetoothDevice device = mBluetoothAdapter.getRemoteDevice("00:06:66:46:C2:46");
		// Attempt to connect to the device
		mChatService.connect(device);
	}

	private void sendMessage(byte[] message)
	{
		// Check that we're actually connected before trying anything
		if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED)
		{
			Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
			return;
		}

		// Check that there's actually something to send
		if (message.length > 0)
		{
			// Get the message bytes and tell the BluetoothChatService to write
			mChatService.write(message);

			// Reset out string buffer to zero
			mOutStringBuffer.setLength(0);
		}
	}

	class StateListener extends PhoneStateListener
	{
		@Override
		public void onCallStateChanged(int state, String incomingNumber)
		{
			super.onCallStateChanged(state, incomingNumber);

			SharedPreferences pref = getBaseContext().getSharedPreferences("pref", MODE_PRIVATE);
			String currentSetting = pref.getString("state", "disabled");
			if (currentSetting.equals("disabled")) return;

			switch (state)
			{
				case TelephonyManager.CALL_STATE_RINGING:
					Log.v(TAG, "incoming call");
					sendMessage(Operations.turnPeripheralOn((byte) 2));
					break;
				case TelephonyManager.CALL_STATE_OFFHOOK:
					sendMessage(Operations.turnPeripheralOff((byte) 2));
					break;
				case TelephonyManager.CALL_STATE_IDLE:
					sendMessage(Operations.turnPeripheralOff((byte) 2));
					break;
			}
		}
	}
}