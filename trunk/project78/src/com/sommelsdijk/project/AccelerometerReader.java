package com.sommelsdijk.project;

import android.app.Activity;
import android.hardware.*;

public class AccelerometerReader extends Activity implements SensorEventListener {

	     private final SensorManager mSensorManager;
	     private final Sensor mAccelerometer;

	     public AccelerometerReader() {
	         mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
	         mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	     }

	     protected void onResume() {
	         super.onResume();
	         mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	     }

	     protected void onPause() {
	         super.onPause();
	         mSensorManager.unregisterListener(this);
	     }

	     public void onAccuracyChanged(Sensor sensor, int accuracy) {
	     }

	     public void onSensorChanged(SensorEvent event) {
	     }
	 }
