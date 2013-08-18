package com.activesoft.altapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class CommonSharedPref
{
	private SharedPreferences preferences; //Shared Preference to save data
	SharedPreferences.Editor editor; //To enter data in shared preference
	public CommonSharedPref(Context context)
	{
		preferences = context.getSharedPreferences("Pref", 0);
		editor = preferences.edit();
		
	}
	//Get Data by Key
	public String getDataFromSharedPref(String pReqKey) 
	{
		String Result = "";
		try 
		{
			Result = preferences.getString(pReqKey, "");
		} 
		catch (Exception exp) 
		{
			Log.d("Exception in SharedPreferencs", exp.getMessage());
		}
		return Result;
	}
	//Save data 
	public void saveDataInSharedPref(String key, String Value) {
		
		editor.putString(key, Value);
		editor.commit();
	}
}
