package com.activesoft.altapp;

import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener
{
	private Button bt_weather;
	private LinearLayout ll_compass;
	private Button bt_sun;
	private Button bt_gps;
	private CommonSharedPref pref; // Class to Use SharedPreference
	private LocationManager locationManager; // Location Manager class to get
											 // location info
	ProgressBar bar;  // Use to show Progress bar when GPS getting location info
	Location loc;     // Location object
	String Sunrise;   // String for Sun Rise time
	String Sunset;    // String for Sun set time
	String location_address; // Name of location
	ImgCompass ImageCompass; // ImageView class to show compass image
	private TextView tv_locationName;
	private Double altitude; // String for altitude value

	SensorManager sensorManager;  // Use for Compass directions
	//static final int sensor = SensorManager.SENSOR_ORIENTATION;  // Get Sensor
																// Orientations
	private Sensor myCompassSensor;
	String responce;
	private static final int BUFFER_SIZE = 2000;
	private final String TEMP_C = "temp_C";
	private final String TEMP_F = "temp_F";
	private final String ICON = "weatherIconUrl";
	private final String DESC = "weatherDesc";
	private final String WIND_MILES = "windspeedMiles";
	private final String WIND_KM = "windspeedKmph";
	private String temp_C;
	private String temp_F;
	private String icon;
	private String desc;
	private String wind_miles;
	private String wind_km;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		bt_gps = (Button) findViewById(R.id.bt_Gps);
		bt_sun = (Button) findViewById(R.id.bt_Sun);
		tv_locationName = (TextView) findViewById(R.id.tv_location);
		ll_compass = (LinearLayout) findViewById(R.id.ll_Compass);
		bt_weather = (Button) findViewById(R.id.bt_Weather);
		bar = (ProgressBar) findViewById(R.id.progressBar1);
		bar.setVisibility(View.VISIBLE);
		// Initialize CompassView object
		ImageCompass = new ImgCompass(this);
		// Image compass
		ImageCompass.setImageResource(R.drawable.btn_bg);
		// get sensor manager
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		// get compass sensor (ie magnetic field)
		myCompassSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		// Add Compass image to layout view
		ll_compass.addView(ImageCompass);
		// Initialize SharedPreference Object
		pref = new CommonSharedPref(getApplicationContext());
		// Set Latitude Longitude to null
		pref.saveDataInSharedPref("Lat", "");
		pref.saveDataInSharedPref("Long", "");

		

	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		// Hide location name
		tv_locationName.setVisibility(View.GONE);
		// Register sensor manager to listen sensor information for compass
		sensorManager.registerListener(this, myCompassSensor, SensorManager.SENSOR_DELAY_NORMAL);
		// Get Location Service
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000L, 0, locationListener); // It
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000L, 0, locationListener);																								   // update
																										   // location
																										   // after
																										   // 30
																										   // second(30000
																										   // m/s)
		//Weather Info
		String show_temp;
		String show_wind;

		
		if(pref.getDataFromSharedPref("Temp").equals("C"))
			show_temp = temp_C + " \u00B0C\n";

		else
			show_temp = temp_F + " \u00B0F\n";
		if(pref.getDataFromSharedPref("Wind").equals("K"))
			show_wind = wind_km + " kph\n";
		else
			show_wind = wind_miles + " mph\n";
		if(show_temp.equals("null °F")==true && show_wind.equals("null °F")==true)
		bt_weather.setText(show_temp + show_wind + desc);
	}

	@Override
	protected void onPause()
	{
		// TODO Auto-generated method stub
		// Disable GPS when you go to other screen
		locationManager.removeUpdates(locationListener);
		// UnRegister Sensor when you go to other screen
		sensorManager.unregisterListener(this);
		super.onPause();
	}

	// This is used to call Service in background thread and get xml data
	// It has three methods
	// 1)OnPreExecute : call before starting background work
	// 2)doInBackground : call background thread
	// 3)onPostExecute : call to update views
	private class AsynchSun extends AsyncTask<Void, Void, Boolean>
	{

		@Override
		protected void onPreExecute()
		{

		}

		protected void onPostExecute(Boolean result)
		{
			try
			{
				if(result == true)
				{

					bt_gps.setText("Lat:" + pref.getDataFromSharedPref("Lat") + "\nLong:" + pref.getDataFromSharedPref("Long"));
					bt_sun.setText("Rise:" + ConvertTime24Format(Sunrise) + "\nSet:" + ConvertTime24Format(Sunset));
					tv_locationName.setText("Location:"+location_address + "\n" + "Altitude:" +String.format("%.2f",altitude));
					tv_locationName.setVisibility(View.VISIBLE);
					
					//Weather Info
					String show_temp;
					String show_wind;

					
					if(pref.getDataFromSharedPref("Temp").equals("C"))
						show_temp = temp_C + " \u00B0C\n";

					else
						show_temp = temp_F + " \u00B0F\n";
					if(pref.getDataFromSharedPref("Wind").equals("K"))
						show_wind = wind_km + " kph\n";
					else
						show_wind = wind_miles + " mph\n";

					bt_weather.setText(show_temp + show_wind + desc);
				}

			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}

		@Override
		protected Boolean doInBackground(Void... arg0)
		{
			Document doc;
			org.w3c.dom.Element element;
			// These Line of code use to get Current Time
			Calendar c = Calendar.getInstance();
			System.out.println("Current time => " + c.getTime());
			SimpleDateFormat df = new SimpleDateFormat("dd/MM");
			String formattedDate = df.format(c.getTime());

			try
			{

				// This is the URL for service to get Location SunRise and
				String finalURL = "http://www.earthtools.org/sun/" + String.valueOf(loc.getLatitude()) + "/" + String.valueOf(loc.getLongitude()) + "/" + formattedDate + "/99/0";
				//This is the URL for service to get altitude
				String altitudeUrl="http://www.earthtools.org/height/"+String.valueOf(loc.getLatitude())+"/"+String.valueOf(loc.getLongitude());
				// SunSet time
				String KEY_RISE = "sunrise";   // Sunrise time
				String KEY_SET = "sunset";    // Sunset time
				XMLParser parser = new XMLParser();  // this object use to parse
													// XML resource

				
				
				//Altitude Service
				String a_xml=parser.getXmlFromUrl(altitudeUrl);
				doc=parser.getDomElement(a_xml);
				element=doc.getDocumentElement();
				altitude=Double.valueOf(parser.getValue(element, "meters"));
				
				// Sun Service
				String xml = parser.getXmlFromUrl(finalURL); // getting XML
			    doc = parser.getDomElement(xml); // getting DOM element
			    element = doc.getDocumentElement();
				Sunrise = parser.getValue(element, KEY_RISE);
				Sunset = parser.getValue(element, KEY_SET);
				
				//Weather Service
				//This is Service URL
				String final_wURL = "http://api.worldweatheronline.com/free/v1/weather.ashx?q=" + pref.getDataFromSharedPref("Lat") + "%2C" + pref.getDataFromSharedPref("Long") + "&format=json&num_of_days=1&key=xxhkezja45br7eh266ecuzpn";
				responce = HttpRequest(final_wURL); //Get Responce String
				ParseResponse(responce); // Parse JSON data

			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				return false;
			}
			return true;

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
	// Convert time into required format
	private String ConvertTime24Format(String dateString)
	{
		java.text.DateFormat format = DateFormat.getTimeFormat(MainActivity.this);
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.US);// ("EEE, dd MMM yyyyy HH:mm:ss z");
		Date convertedDate;
		try
		{

			convertedDate = dateFormat.parse(dateString);

			// strDateTime=convertedDate.h
			// +":"+convertedDate.getMinutes()+":"+convertedDate.getSeconds();

		}
		catch (ParseException e)
		{
			e.printStackTrace();
			return null;
		}

		return format.format(convertedDate);
	}

	// This is location Listener
	private final LocationListener locationListener = new LocationListener()
	{
		// This method execute on the location change and get new location
		// information
		public void onLocationChanged(Location location)
		{
			if(location.getProvider().equals(LocationManager.GPS_PROVIDER))
			{
				Log.d("GPS", "GPS Provider");
			}
			else if(location.getProvider().equals(LocationManager.NETWORK_PROVIDER))
			{
				Log.d("Network", "Network Provider");
			}

			loc = location;
			bar.setVisibility(View.GONE);
			try
			{
				altitude = location.getAltitude();

				if(location != null)
				{

					Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());

					List<Address> addresses = geoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
					location_address = addresses.get(0).getAddressLine(1);

					pref.saveDataInSharedPref("Lat", String.format("%.2f", location.getLatitude()));
					pref.saveDataInSharedPref("Long", String.format("%.2f", location.getLongitude()));

					// bt_gps.setText(location_address);
					AsynchSun sun = new AsynchSun();
					sun.execute();
				}
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		public void onProviderDisabled(String provider)
		{
			Log.d("Network", "Disable location manager");
		}

		public void onProviderEnabled(String provider)
		{
			Log.d("Network", "Enable location manager");
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras)
		{
			// TODO Auto-generated method stub
			Log.d("Network", "Status change");
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// TODO Auto-generated method stub
		if(item.getItemId() == R.id.menu_settings)
		{
			Intent intentSetting = new Intent(MainActivity.this, SettingActivity.class);
			startActivity(intentSetting);
		}
		return super.onOptionsItemSelected(item);
	}

	// it is SensorEventListener method. Ignore for now
	public void onAccuracyChanged(Sensor sensor, int accuracy)
	{
		// TODO Auto-generated method stub
	}

	// it is SensorEventListener method use to update compass information
	public void onSensorChanged(SensorEvent event)
	{
		// this check is unnecessary with only one registered sensor
		// but it's useful to know in case you need to add more sensors
		
			int orientation = (int) event.values[0];
			ImageCompass.setDirection(orientation);
		
	}

}
