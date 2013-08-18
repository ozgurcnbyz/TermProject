package com.activesoft.altapp;

import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WeatherActivity extends Activity
{

	private ProgressDialog pd;
	private CommonSharedPref pref;
	private final String TEMP_C = "temp_C";
	private final String TEMP_F = "temp_F";
	private final String ICON = "weatherIconUrl";
	private final String DESC = "weatherDesc";
	private final String WIND_MILES = "windspeedMiles";
	private final String WIND_KM = "windspeedKmph";
	private RelativeLayout imgWeahter;
	private TextView tv_result;

	private String temp_C;
	private String temp_F;
	private String icon;
	private String desc;
	private String wind_miles;
	private String wind_km;

	private final String partly = "Partly Cloudy";
	private final String sunny = "Sunny";

	private static final int BUFFER_SIZE = 2000;
	String response;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather);// xml layout file
		pref = new CommonSharedPref(getApplicationContext());// Initialize
															 // SharedPref
															 // object
		imgWeahter = (RelativeLayout) findViewById(R.id.rl_main);
		tv_result = (TextView) findViewById(R.id.tv_result); // Show information
		AsynchWeather weahter = new AsynchWeather();  // Asynch Object to do
													 // background thread
		weahter.execute(); // Call thread

	}

	// This is used to call Service in background thread and get JSON data
	// It has three methods
	// 1)OnPreExecute : call before starting background work
	// 2)doInBackground : call background thread
	// 3)onPostExecute : call to update views
	private class AsynchWeather extends AsyncTask<Void, Void, Boolean>
	{

		@Override
		protected void onPreExecute()
		{
			// Start Progress Dialog
			pd = ProgressDialog.show(WeatherActivity.this, "", "Please Wait...");
		}

		protected void onPostExecute(Boolean result)
		{
			try
			{
				String show_temp;
				String show_wind;

				// imgWeahter.setBackground();
				if(desc.equals(partly))
				{
					imgWeahter.setBackgroundResource(R.drawable.cloud);
				}
				else if(desc.equals(sunny))
				{
					imgWeahter.setBackgroundResource(R.drawable.sunny);
				}
				else
					imgWeahter.setBackgroundResource(R.drawable.rain);

				if(pref.getDataFromSharedPref("Temp").equals("C"))
					show_temp = temp_C + " \u00B0C\n";

				else
					show_temp = temp_F + " \u00B0F\n";
				if(pref.getDataFromSharedPref("Wind").equals("K"))
					show_wind = wind_km + " kph\n";
				else
					show_wind = wind_miles + " mph\n";

				tv_result.setText(show_temp + show_wind + desc);
				pd.dismiss();
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				pd.dismiss();
			}
		}

		@Override
		protected Boolean doInBackground(Void... arg0)
		{
			try
			{
				//This is Service URL
				String finalURL = "http://api.worldweatheronline.com/free/v1/weather.ashx?q=" + pref.getDataFromSharedPref("Lat") + "%2C" + pref.getDataFromSharedPref("Long") + "&format=json&num_of_days=1&key=xxhkezja45br7eh266ecuzpn";
				response = HttpRequest(finalURL); //Get Responce String
				ParseResponse(response); // Parse JSON data
				return true;
			}
			catch (Exception ex)
			{
				pd.dismiss();
				return false;
			}

		}
	}

	private void ParseResponse(String aResposne)
	{
		try
		{
			JSONObject obj = new JSONObject(aResposne);
			JSONObject data = obj.getJSONObject("data");
			JSONArray current = data.getJSONArray("current_condition");
			JSONObject objInfo = current.getJSONObject(0);

			// Get Weather Description from JSON
			JSONArray arrDesc = objInfo.getJSONArray(DESC);
			JSONObject objValue = arrDesc.getJSONObject(0);
			desc = objValue.getString("value");
			// Get Temperature and wind Speed from JSON
			temp_C = objInfo.getString(TEMP_C);
			temp_F = objInfo.getString(TEMP_F);
			wind_km = objInfo.getString(WIND_KM);
			wind_miles = objInfo.getString(WIND_MILES);
			// Get Weather Icon Image from JSON
			JSONArray arrIcon = objInfo.getJSONArray(ICON);
			JSONObject objIcon = arrIcon.getJSONObject(0);
			icon = objIcon.getString("value");

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	// Use to call HTTP request
	public String HttpRequest(String aURL)
	{
		String api_response = "";
		aURL = aURL.replaceAll(" ", "%20");

		HttpClient client = new DefaultHttpClient();
		HttpEntity Httpentity;

		try
		{
			HttpGet get = new HttpGet(aURL);
			HttpResponse response = client.execute(get);
			Httpentity = response.getEntity();
			InputStreamReader isr = new InputStreamReader(Httpentity.getContent());

			int charRead;
			char[] inputBuffer = new char[BUFFER_SIZE];

			while ((charRead = isr.read(inputBuffer)) > 0)
			{
				// ---convert the chars to a String---
				String readString = String.copyValueOf(inputBuffer, 0, charRead);
				api_response += readString;
				inputBuffer = new char[BUFFER_SIZE];
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return api_response;
	}
}
