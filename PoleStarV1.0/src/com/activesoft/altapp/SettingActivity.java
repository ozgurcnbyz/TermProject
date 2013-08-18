package com.activesoft.altapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;

public class SettingActivity extends Activity
{
	private RadioButton radioFarenhiet;
	private RadioButton radioCelsius;
	private RadioButton radioKmp;
	private RadioButton radioMph;
	private CommonSharedPref shared;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		radioFarenhiet=(RadioButton)findViewById(R.id.radio_Farenheit);
		radioCelsius=(RadioButton)findViewById(R.id.radio_Celsius);
		radioKmp=(RadioButton)findViewById(R.id.radioKph);
		radioMph=(RadioButton)findViewById(R.id.radioMph);
		btnDone();
		shared=new CommonSharedPref(this);
		setRadio();
		
	}
	private void setRadio()
	{
		if(shared.getDataFromSharedPref("Temp").equals("C"))
			radioCelsius.setChecked(true);
		else
			radioFarenhiet.setChecked(true);
		
		if(shared.getDataFromSharedPref("Wind").equals("M"))
			radioMph.setChecked(true);
		else
			radioKmp.setChecked(true);
	}
	private void btnDone()
	{
		Button btnDone=(Button)findViewById(R.id.btn_Done);
		btnDone.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				 finish();          
		         moveTaskToBack(true);
			}
		});
	}
	public void onRadioButtonClicked(View view) {
	    // Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();
	    
	    // Check which radio button was clicked
	    switch(view.getId()) {
	        case R.id.radio_Celsius:	           
	               shared.saveDataInSharedPref("Temp","C" );	      
	            break;
	        case R.id.radio_Farenheit:
	                shared.saveDataInSharedPref("Temp", "F");          
	            break;
	        case R.id.radioKph:        	
	        		shared.saveDataInSharedPref("Wind", "K");
	        
	        	break;
	        case R.id.radioMph:
	        		shared.saveDataInSharedPref("Wind", "M");
	        	break;
	    }
	}
}
