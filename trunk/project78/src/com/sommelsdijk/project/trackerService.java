package com.sommelsdijk.project;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

public class trackerService extends Service {

	public PendingIntent pendingIntent;
	private AlarmManager alarmManagerPositioning;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Toast.makeText(getBaseContext(), "trackService started!", Toast.LENGTH_LONG).show();
		
		alarmManagerPositioning = (AlarmManager) getSystemService
	            (Context.ALARM_SERVICE);
		
		Intent intentToFire = new Intent(this, positionReceiver.class);
		pendingIntent= PendingIntent.getBroadcast(
	            this, 0, intentToFire, 0);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		positionReceiver.stopLocationListener();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		try {
	        long interval = 60 * 1000;
	        int alarmType = AlarmManager.ELAPSED_REALTIME_WAKEUP;
	        long timetoRefresh = SystemClock.elapsedRealtime();
	        alarmManagerPositioning.setInexactRepeating(alarmType,
	                timetoRefresh, interval, pendingIntent);
	    } catch (NumberFormatException e) {
	        Toast.makeText(this, "error running service: " + e.getMessage(),
	                Toast.LENGTH_SHORT).show();
	    } catch (Exception e) {
	        Toast.makeText(this, "error running service: " + e.getMessage(),
	                Toast.LENGTH_SHORT).show();
	    }
	}

	
}
