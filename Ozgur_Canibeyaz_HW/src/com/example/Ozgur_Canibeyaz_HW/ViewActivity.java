package com.example.Ozgur_Canibeyaz_HW;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class ViewActivity extends Activity {
	TextView et_title;
	TextView et_note;
	TextView et_date;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.view);
		et_title=(TextView)findViewById(R.id.et_title);
		et_note=(TextView)findViewById(R.id.et_note);
		et_date=(TextView)findViewById(R.id.et_date);
		
		et_title.setText(getIntent().getExtras().getString(Constants.TITLE));
		et_note.setText(getIntent().getExtras().getString(Constants.NOTE));
		et_date.setText(getIntent().getExtras().getString(Constants.DATE));
		onDeleteItem();

	}
	
	private void onDeleteItem()
	{
		
		
	}

}
