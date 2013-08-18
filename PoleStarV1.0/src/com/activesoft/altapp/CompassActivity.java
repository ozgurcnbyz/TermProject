package com.activesoft.altapp;


import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

@SuppressWarnings("deprecation")
public class CompassActivity extends Activity implements SensorEventListener
{
	SensorManager sensorManager;
	  static final int sensor = SensorManager.SENSOR_ORIENTATION;
	  ImgCompass ImageCompass;
	  private Sensor myCompassSensor;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		        WindowManager.LayoutParams.FLAG_FULLSCREEN);
		    requestWindowFeature(Window.FEATURE_NO_TITLE);
		    ImageCompass=new ImgCompass(this);
		    setContentView(ImageCompass);
		 // get sensor manager
		    sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		    // get compass sensor (ie magnetic field)
		    myCompassSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
	}

	 // register to listen to sensors
	  @Override
	  public void onResume() {
	    super.onResume();
	    sensorManager.registerListener(this, myCompassSensor, SensorManager.SENSOR_DELAY_NORMAL);
	  }
	// unregister
	  @Override
	  public void onPause() {
	    super.onPause();
	    sensorManager.unregisterListener(this);
	  }

	

	
	  // Ignore for now
	  public void onAccuracyChanged(Sensor sensor, int accuracy) {
	      // TODO Auto-generated method stub
	  }

	
	  public void onSensorChanged(SensorEvent event) {
	      // this check is unnecessary with only one registered sensor
	      // but it's useful to know in case you need to add more sensors
	      if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
	          int orientation = (int) event.values[0];
	          ImageCompass.setDirection(orientation);
	      }
	  }
}
