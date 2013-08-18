package com.example.Ozgur_Canibeyaz_HW;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class MainActivity extends Activity {
	ArrayList<Struct_item> ls_SavedItems = new ArrayList<Struct_item>();
	SharedPreferences pref;
	JSONArray jsonArray;
	LinearLayout ll_list;
	LayoutInflater inflator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		pref = getSharedPreferences(Constants.PREF_NAME, 0);
		btn_new();
		onClearList();
		showList();
	}
	
	

	private void onClearList()
	{
		getListFromPref();
		ls_SavedItems.clear();
		try
		{
			SharedPreferences.Editor editor = pref.edit();
			editor.putString(Constants.LIST_ITEMS, new Gson().toJson(ls_SavedItems).toString());
			editor.commit();
		

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	


	@Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) {     

	        if(keyCode == KeyEvent.KEYCODE_HOME || keyCode==KeyEvent.KEYCODE_BACK )
	        {
	           //The Code Want to Perform.
	        	onClearList();
	        	finish();
	        	return true;
	        }
			return false;
	    }
	
	 
	
	private void btn_new()
	{
		Button bt_new=(Button)findViewById(R.id.bt_addnew);
		bt_new.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			//Intent intent_new=new Intent(MainActivity.this,New_Activity.class);
			//startActivity(intent_new);
				onNewDialog();
			}
		});
	}
	@Override
	protected void onResume()
	{
		super.onResume();
		showList();

	}
	private void showList()
	{
		getListFromPref();
		ll_list = (LinearLayout) findViewById(R.id.ll_List);
		ll_list.removeAllViews();

		inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ll_list = (LinearLayout) findViewById(R.id.ll_List);

		for(int i = ls_SavedItems.size()-1; i>=0; i--)
		{
			final View v = inflator.inflate(R.layout.row_main, null);
			final TextView title = (TextView) v.findViewById(R.id.tv_title);
			final TextView date = (TextView) v.findViewById(R.id.tv_date);
			
			final int a = i;
			runOnUiThread(new Runnable()
			{

				@Override
				public void run()
				{
					date.setText(ls_SavedItems.get(a).date);
					title.setText(ls_SavedItems.get(a).title);
					

					ll_list.addView(v);
				}
			});
			RowClickListener(v, ls_SavedItems.get(i));
			Longclick(v, ls_SavedItems.get(i),String.valueOf(i));
		}
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

				for(int i =0; i <jsonArray.length(); i++)
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
								struct.note=row.getString("note");
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
	private void RowClickListener(final View aView, final Struct_item aObj)
	{
		aView.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{

				Intent i = new Intent(getApplicationContext(), ViewActivity.class);
				i.putExtra(Constants.NOTE, aObj.note);
				i.putExtra(Constants.DATE, aObj.date);
				i.putExtra(Constants.TITLE, aObj.title);
				startActivity(i);
			}
		});
	}
	
	private void onNewDialog()
	{
		 AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		    // Get the layout inflater
		    LayoutInflater inflater = MainActivity.this.getLayoutInflater();
		    final View v=inflater.inflate(R.layout.dialog_view, null);
		    // Inflate and set the layout for the dialog
		    // Pass null as the parent view because its going in the dialog layout
		    new AlertDialog.Builder(MainActivity.this).setView(v).setPositiveButton("Save", new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	                   EditText et_title=(EditText)v.findViewById(R.id.et_title);
	                   EditText et_note=(EditText)v.findViewById(R.id.et_note);
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
	   						showList();

	   					}
	   					catch (Exception e)
	   					{
	   						e.printStackTrace();
	   					}
	   				}
	               }
	           }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                   
	               }
	           }).show();
	}
	
	private void Longclick(View aView, final Struct_item aObj,final String id)
	{
		aView.setOnLongClickListener(new OnLongClickListener()
		{

			@Override
			public boolean onLongClick(View v)
			{
				

				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				new AlertDialog.Builder(MainActivity.this).setTitle("Please confirm").setMessage("Delete Note ").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						
						try
						{
							ls_SavedItems.remove(aObj);
							SharedPreferences.Editor editor = pref.edit();
							editor.putString(Constants.LIST_ITEMS, new Gson().toJson(ls_SavedItems).toString());
							editor.commit();
							

						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
						dialog.cancel();

						runOnUiThread(new Runnable()
						{
							@Override
							public void run()
							{
								showList();
								Toast.makeText(getApplicationContext(), "Note is deleted", 0).show();
							}
						});
					}
				}).setNegativeButton("No", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.cancel();
					}
				}).show();

				return true;
			}
		});
	}
	
}
