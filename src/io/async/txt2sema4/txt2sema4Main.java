/*
// Copyright (c) 2010 Jonathan M. Altman
// 
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
// 
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
// 
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.
*/
package io.async.txt2sema4;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class txt2sema4Main extends Activity implements OnInitListener {
	private static final String TAG = "txt2sema4";
	private int ttsDataCheck = 42; // It's always 42, isn't it?
	protected boolean hasTTS = false;
	protected ConnectivityManager connectivity;
	protected TextView pronounce_output;
	protected EditText pronounce_input;
	protected TextToSpeech mTts = null;
	protected Spinner speedSpinner = null;
	protected CheckBox shouldSay = null;
	public String lastSpoken = null;
	private final static HashMap<Character, String> letterDecodes = new HashMap<Character, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			 put('a', "alfa");
			 put('b', "bravo");
			 put('c', "charlie");
			 put('d', "delta");
			 put('e', "echo");
			 put('f', "foxtrot");
			 put('g', "golf");
			 put('h', "hotel");
			 put('i', "india");
			 put('j', "juliett");
			 put('k', "kilo");
			 put('l', "lima");
			 put('m', "mike");
			 put('n', "november");
			 put('o', "oscar");
			 put('p', "papa");
			 put('q', "quebec");
			 put('r', "romeo");
			 put('s', "sierra");
			 put('t', "tango");
			 put('u', "uniform");
			 put('v', "victor");
			 put('w', "whiskey");
			 put('x', "xray");
			 put('y', "yankee");
			 put('z', "zulu");
			 put('_', "underscore");
		}
	};

	
	public enum SpeechSpeeds {
		SLOW, NORMAL, FAST;
	}
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		
		speedSpinner = (Spinner) findViewById(R.id.Pronounce_speed_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.speech_speeds_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		speedSpinner.setAdapter(adapter);
		// @TODO: this is tightly tied to the string list
		speedSpinner.setSelection(1);
		
		shouldSay = (CheckBox)findViewById(R.id.ShouldSpeak);

		View pronounce_button = findViewById(R.id.Pronounce_button);
		pronounce_button.setOnClickListener(mPronounceText);
		
		pronounce_output = (TextView)findViewById(R.id.Pronounce_output);
		pronounce_input = (EditText)findViewById(R.id.Pronounce_value);
		
		Intent intent = new Intent(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(intent, ttsDataCheck);
	}
	
	@Override
	public void onDestroy() {
			mTts.shutdown();
			super.onDestroy();
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
			if (requestCode == ttsDataCheck)
			{
				if (resultCode ==TextToSpeech.Engine.CHECK_VOICE_DATA_PASS)
				{
					Log.d(TAG, "TTS engine is installed");
					mTts = new TextToSpeech(this, this);
				}
				else
				{
					Log.d(TAG, "TTS engine not present");
		   		Toast toast = Toast.makeText(getApplicationContext(), R.string.ErrMsg_NoTTS, Toast.LENGTH_SHORT);
					toast.show();
					shouldSay.setClickable(false);
   			}
			}
	}
	public String GetPronunciation(String source_text)
	{
		StringReader bar = new StringReader  (source_text.toLowerCase());
		StringBuffer pronounce_output = new StringBuffer();
		int curChar;
		try {
			while((curChar = bar.read()) != -1) {
				if (pronounce_output.length() > 0) { pronounce_output.append(' '); }
				String charDecode = txt2sema4Main.letterDecodes.get((char)curChar);
				pronounce_output.append(charDecode != null ? charDecode : (char)curChar);
			}			
		}
		catch (IOException e) {e.printStackTrace(); }
		
		return pronounce_output.toString();
		
	 }
	
	public final Button.OnClickListener mPronounceText = new Button.OnClickListener() {
		public void onClick(View v) {
				
				String pronounce_response = "";
				String newSpoken = pronounce_input.getText().toString();
				
				pronounce_response = txt2sema4Main.this.GetPronunciation(newSpoken);
				txt2sema4Main.this.lastSpoken = newSpoken;
				pronounce_output.setText(pronounce_response.replace(" ", "\r\n"));
				
				if (shouldSay.isChecked())
				{
					String speech_speed_value = (String)speedSpinner.getSelectedItem();
					SpeechSpeeds speed_value = SpeechSpeeds.valueOf(speech_speed_value.toUpperCase());
					float tts_speed_value = (float) .75;
					switch(speed_value)
					{
						case SLOW:
							tts_speed_value = (float).5;
							break;
						case NORMAL:
							tts_speed_value = (float).75;
							break;
						case FAST:
							tts_speed_value = (float)1;
							break;
					}
					mTts.setSpeechRate(tts_speed_value);
					mTts.speak(pronounce_response, TextToSpeech.QUEUE_FLUSH, null);
				}
			}
		}
	  ;
	@Override
	public void onInit(int status)
	{
		mTts.setLanguage(Locale.US);		
	}
	
	}
