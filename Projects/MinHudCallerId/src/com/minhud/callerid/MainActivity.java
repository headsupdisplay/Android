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

package com.minhud.callerid;

import com.minhud.callerid.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

/**
 * This is the main Activity that displays the current chat session.
 */
public class MainActivity extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		// Set up the window layout
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.main);

		Button onButton = (Button) findViewById(R.id.buttonOn);
		Button offButton = (Button) findViewById(R.id.buttonOff);

		onButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				startService(new Intent(MainActivity.this, MyPhoneStateListener.class));
				Toast.makeText(MainActivity.this, "Heads Up Incoming Call is ON", Toast.LENGTH_SHORT).show();
				
				SharedPreferences pref = getBaseContext().getSharedPreferences("pref", MODE_PRIVATE);
				Editor editor = pref.edit();
				editor.putString("state", "enabled");
				editor.commit();
			}
		});

		offButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				stopService(new Intent(MainActivity.this, MyPhoneStateListener.class));
				Toast.makeText(MainActivity.this, "Heads Up Incoming Call is OFF", Toast.LENGTH_SHORT).show();
				
				SharedPreferences pref = getBaseContext().getSharedPreferences("pref", MODE_PRIVATE);
				Editor editor = pref.edit();
				editor.putString("state", "disabled");
				editor.commit();
			}
		});

	}
}