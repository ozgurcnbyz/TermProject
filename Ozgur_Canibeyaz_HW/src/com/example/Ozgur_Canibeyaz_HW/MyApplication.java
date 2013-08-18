package com.example.Ozgur_Canibeyaz_HW;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

import android.app.Application;
import android.content.SharedPreferences;

public class MyApplication extends Application {

	SharedPreferences pref;
	JSONArray jsonArray;
	ArrayList<Struct_item> ls_SavedItems = new ArrayList<Struct_item>();
private static MyApplication singleton;
	
	public MyApplication getInstance(){
		return singleton;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		pref = getSharedPreferences(Constants.PREF_NAME, 0);
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
				
			}
			catch (Exception ex)
			{
				ex.printStackTrace();

			}
		}

	}
}
