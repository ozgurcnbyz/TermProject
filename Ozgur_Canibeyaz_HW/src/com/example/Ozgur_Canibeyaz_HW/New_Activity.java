package com.example.Ozgur_Canibeyaz_HW;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

public class New_Activity extends Activity {
	SharedPreferences pref;
	JSONArray jsonArray;
	ArrayList<Struct_item> ls_SavedItems = new ArrayList<Struct_item>();
	private Button bt_add;
	EditText et_title;
	EditText et_note;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.new_item);
		et_title=(EditText)findViewById(R.id.et_title);
		et_note=(EditText)findViewById(R.id.et_note);
		pref = getSharedPreferences(Constants.PREF_NAME, 0);
		bt_add();
		onCancel();

	}
	private void onCancel()
	{
		Button bt_cancel=(Button)findViewById(R.id.bt_cancel);
		bt_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			finish();	
			}
		});
	}
	private void getListFromPref()
	{
		ls_SavedItems = new ArrayList<Struct_item>();
		if(pref.getString(Constants.LIST_ITEMS, "").isEmpty())
		{

		}
		else
		{
			try
			{
				jsonArray = new JSONArray(pref.getString(Constants.LIST_ITEMS, ""));

				for(int i = 0; i < jsonArray.length(); i++)
				{

					final int a = i;

					runOnUiThread(new Runnable()
					{

						@Override
						public void run()
						{
							try
							{
								JSONObject row = jsonArray.getJSONObject(a);

								Struct_item struct = new Struct_item();
								struct.date = row.getString("date");
								struct.title = row.getString("title");
							
								ls_SavedItems.add(struct);

							}
							catch (Exception e)
							{
								e.printStackTrace();

							}

						}
					});
				}
				
			}
			catch (Exception ex)
			{
				ex.printStackTrace();

			}
		}

	}

	private void bt_add()
	{
		bt_add = (Button) findViewById(R.id.bt_add);
		bt_add.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				if(et_title.getText().toString().trim().equals("") || et_note.getText().toString().trim().equals(""))
				{
					Toast.makeText(getApplicationContext(), "Please enter data", 0).show();
				}
				else
				{
					getListFromPref();
					Struct_item item = new Struct_item();
					item.title=et_title.getText().toString().trim();
					item.note=et_note.getText().toString();
					final Calendar c = Calendar.getInstance();
					String date=(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.DATE)+"/"+c.get(Calendar.YEAR);
					item.date=date;
					

					ls_SavedItems.add(item);
					try
					{
						SharedPreferences.Editor editor = pref.edit();
						editor.putString(Constants.LIST_ITEMS, new Gson().toJson(ls_SavedItems).toString());
						editor.commit();
						finish();

					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		});
	}


}
