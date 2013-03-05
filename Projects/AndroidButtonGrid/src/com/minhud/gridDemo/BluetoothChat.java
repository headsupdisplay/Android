/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.minhud.gridDemo;

import java.util.ArrayList;
import java.util.List;

import com.minhud.gridDemo.R;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * This is the main Activity that displays the current chat session.
 */
public class BluetoothChat extends Activity
{
	// Debugging
	private static final String TAG = "BluetoothChat";
	private static final boolean D = true;

	// Message types sent from the BluetoothChatService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;

	// Key names received from the BluetoothChatService Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";

	// Intent request codes
	private static final int REQUEST_CONNECT_DEVICE = 1;
	private static final int REQUEST_ENABLE_BT = 2;

	// Layout Views
	private List<Button> buttons = new ArrayList<Button>();

	// Name of the connected device
	private String mConnectedDeviceName = null;
	// String buffer for outgoing messages
	private StringBuffer mOutStringBuffer;
	// Local Bluetooth adapter
	private BluetoothAdapter mBluetoothAdapter = null;
	// Member object for the chat services
	private BluetoothChatService mChatService = null;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if (D) Log.e(TAG, "+++ ON CREATE +++");

		// Set up the window layout
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);

		for (int i = 1; i <= 16; i++)
		{
			String buttonId = "button" + i;
			int resId = getResources().getIdentifier(buttonId, "id", "com.minhud.gridDemo");
			buttons.add((Button) findViewById(resId));
		}

		//Orange
		buttons.get(0).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				sendMessage(Operations.setPeripheralColor((byte) 3, (byte) 255, (byte) 102, (byte) 0));
				sendMessage(Operations.turnPeripheralOn((byte)3));
			}
		});

		//Purple
		buttons.get(1).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				sendMessage(Operations.setPeripheralColor((byte) 3, (byte) 255, (byte) 0, (byte) 255));
				sendMessage(Operations.turnPeripheralOn((byte)3));
			}
		});

		//Blue
		buttons.get(2).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				sendMessage(Operations.setPeripheralColor((byte) 3, (byte) 0, (byte) 0, (byte) 255));
				sendMessage(Operations.turnPeripheralOn((byte)3));
			}
		});

		//White
		buttons.get(3).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				sendMessage(Operations.setPeripheralColor((byte) 3, (byte) 255, (byte) 255, (byte) 255));
				sendMessage(Operations.turnPeripheralOn((byte)3));
			}
		});

		//Green
		buttons.get(4).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				sendMessage(Operations.turnPeripheralOn((byte) 0));
			}
		});

		//Yellow
		buttons.get(5).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				sendMessage(Operations.turnPeripheralOn((byte) 1));
			}
		});

		//Red
		buttons.get(6).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				sendMessage(Operations.turnPeripheralOn((byte) 2));
			}
		});

		//Xmas
		buttons.get(7).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				sendMessage(Operations.setPeripheralBlinkColor((byte) 3, (byte) 255, (byte) 0, (byte) 0));
				sendMessage(Operations.setPeripheralColor((byte) 3, (byte) 0, (byte) 255, (byte) 0));
				sendMessage(Operations.setPeripheralBlink((byte)3, (short)2000, (short)2000));
			}
		});

		//Green Off
		buttons.get(8).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				sendMessage(Operations.turnPeripheralOff((byte) 0));
			}
		});

		//Yellow off
		buttons.get(9).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				sendMessage(Operations.turnPeripheralOff((byte) 1));
			}
		});

		//Red off
		buttons.get(10).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				sendMessage(Operations.turnPeripheralOff((byte) 2));
			}
		});

		//5 Minute Timer Demo
		buttons.get(11).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				sendMessage(Operations.fiveMinuteTimerDemo((byte) 3));
			}
		});

		//Green blink
		buttons.get(12).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				sendMessage(Operations.setPeripheralBlink((byte) 0, (short)50, (short)50));
			}
		});

		//Yellow blink
		buttons.get(13).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				sendMessage(Operations.setPeripheralBlink((byte) 1, (short)500, (short)500));
			}
		});

		//Red blink
		buttons.get(14).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				sendMessage(Operations.setPeripheralBlink((byte) 2, (short)100, (short)1000));
			}
		});

		//All off
		buttons.get(15).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				sendMessage(Operations.turnPeripheralOff((byte)0));
				sendMessage(Operations.turnPeripheralOff((byte)1));
				sendMessage(Operations.turnPeripheralOff((byte)2));
				sendMessage(Operations.turnPeripheralOff((byte)3));
			}
		});

		// Get local Bluetooth adapter
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		// If the adapter is null, then Bluetooth is not supported
		if (mBluetoothAdapter == null)
		{
			Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
			finish();
			return;
		}
	}

	@Override
	public void onStart()
	{
		super.onStart();
		if (D) Log.e(TAG, "++ ON START ++");

		// If BT is not on, request that it be enabled.
		// setupChat() will then be called during onActivityResult
		if (!mBluetoothAdapter.isEnabled())
		{
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
			// Otherwise, setup the chat session
		}
		else
		{
			if (mChatService == null) setupChat();
		}
	}

	@Override
	public synchronized void onResume()
	{
		super.onResume();
		if (D) Log.e(TAG, "+ ON RESUME +");

		// Performing this check in onResume() covers the case in which BT was
		// not enabled during onStart(), so we were paused to enable it...
		// onResume() will be called when ACTION_REQUEST_ENABLE activity
		// returns.
		if (mChatService != null)
		{
			// Only if the state is STATE_NONE, do we know that we haven't
			// started already
			if (mChatService.getState() == BluetoothChatService.STATE_NONE)
			{
				// Start the Bluetooth chat services
				mChatService.start();
			}
		}
	}

	private void setupChat()
	{
		Log.d(TAG, "setupChat()");

		// Initialize the BluetoothChatService to perform bluetooth connections
		mChatService = new BluetoothChatService(this, mHandler);

		// Initialize the buffer for outgoing messages
		mOutStringBuffer = new StringBuffer("");
	}

	@Override
	public synchronized void onPause()
	{
		super.onPause();
		if (D) Log.e(TAG, "- ON PAUSE -");
	}

	@Override
	public void onStop()
	{
		super.onStop();
		if (D) Log.e(TAG, "-- ON STOP --");
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		// Stop the Bluetooth chat services
		if (mChatService != null) mChatService.stop();
		if (D) Log.e(TAG, "--- ON DESTROY ---");
	}

	private void ensureDiscoverable()
	{
		if (D) Log.d(TAG, "ensure discoverable");
		if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE)
		{
			Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			startActivity(discoverableIntent);
		}
	}

	/**
	 * Sends a message.
	 * 
	 * @param message
	 *            A string of text to send.
	 */
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

	// The Handler that gets information back from the BluetoothChatService
	private final Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case MESSAGE_STATE_CHANGE:
					if (D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
					switch (msg.arg1)
					{
						case BluetoothChatService.STATE_CONNECTED:
							break;
						case BluetoothChatService.STATE_CONNECTING:
							break;
						case BluetoothChatService.STATE_LISTEN:
							break;
						case BluetoothChatService.STATE_NONE:
							break;
					}
					break;
				case MESSAGE_WRITE:
					byte[] writeBuf = (byte[]) msg.obj;
					// construct a string from the buffer
					String writeMessage = new String(writeBuf);
					break;
				case MESSAGE_READ:
					byte[] readBuf = (byte[]) msg.obj;
					// construct a string from the valid bytes in the buffer
					String readMessage = new String(readBuf, 0, msg.arg1);
					break;
				case MESSAGE_DEVICE_NAME:
					// save the connected device's name
					mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
					Toast.makeText(getApplicationContext(), "Connected to " + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
					break;
				case MESSAGE_TOAST:
					Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST), Toast.LENGTH_SHORT).show();
					break;
			}
		}
	};

	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (D) Log.d(TAG, "onActivityResult " + resultCode);
		switch (requestCode)
		{
			case REQUEST_CONNECT_DEVICE:
				// When DeviceListActivity returns with a device to connect
				if (resultCode == Activity.RESULT_OK)
				{
					// Get the device MAC address
					String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
					Log.v(TAG, "ADD: " + address);
					// Get the BLuetoothDevice object
					BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
					// Attempt to connect to the device
					mChatService.connect(device);
				}
				break;
			case REQUEST_ENABLE_BT:
				// When the request to enable Bluetooth returns
				if (resultCode == Activity.RESULT_OK)
				{
					// Bluetooth is now enabled, so set up a chat session
					setupChat();
				}
				else
				{
					// User did not enable Bluetooth or an error occured
					Log.d(TAG, "BT not enabled");
					Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
					finish();
				}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.option_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.scan:
				// Launch the DeviceListActivity to see devices and do scan
				Intent serverIntent = new Intent(this, DeviceListActivity.class);
				startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
				return true;
			case R.id.discoverable:
				// Ensure this device is discoverable by others
				ensureDiscoverable();
				return true;
		}
		return false;
	}

}