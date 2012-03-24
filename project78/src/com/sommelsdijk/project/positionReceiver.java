package com.sommelsdijk.project;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Binder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class positionReceiver extends Service {

	private final DecimalFormat sevenSigDigits = new DecimalFormat("0.#######");
	private final SimpleDateFormat timestampFormat = new SimpleDateFormat(
			"yyyyMMddHHmmss");

	private LocationManager lm;
	private LocationListener locationListener;

	private static long minTimeMillis = 15 * (60 * 1000);
	private static long minDistanceMeters = 0;
	private static float minAccuracyMeters = 10;

	private int lastStatus = 0;
	private static boolean showingDebugToast = true;

	private static final String tag = "GPSLoggerService";

	// An alarm for rising in special times to fire the pendingIntentPositioning
	private AlarmManager alarmManagerPositioning;
	// A PendingIntent for calling a receiver in special times
	public PendingIntent pendingIntentPositioning;

	/** Called when the activity is first created. */
	private void startLoggerService() {

		// ---use the LocationManager class to obtain GPS locations---
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		locationListener = new MyLocationListener();

		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTimeMillis,
				minDistanceMeters, locationListener);

		Toast.makeText(getBaseContext(),
				"Service gemaakt met interval : \n" + minTimeMillis,
				Toast.LENGTH_LONG).show();

		CountDownTimer timer = new CountDownTimer(minTimeMillis, (60 * 1000)) {

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
				Log.i("time left", " " + millisUntilFinished);

			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub

			}
		}.start();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		
		/*try {
			long interval = 60 * 1000;
			int alarmType = AlarmManager.ELAPSED_REALTIME_WAKEUP;
			long timetoRefresh = SystemClock.elapsedRealtime();
			alarmManagerPositioning.setInexactRepeating(alarmType,
					timetoRefresh, interval, pendingIntentPositioning);
		} catch (NumberFormatException e) {
			Toast.makeText(this, "error running service: " + e.getMessage(),
					Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Toast.makeText(this, "error running service: " + e.getMessage(),
					Toast.LENGTH_SHORT).show();
		}*/
	}

	private void shutdownLoggerService() {
		lm.removeUpdates(locationListener);
	}

	public class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location loc) {
			if (loc != null) {
				if (showingDebugToast)
					Toast.makeText(
							getBaseContext(),
							"Locatie: \nLat: "
									+ sevenSigDigits.format(loc.getLatitude())
									+ " \nLon: "
									+ sevenSigDigits.format(loc.getLongitude())
									+ " \nAlt: "
									+ (loc.hasAltitude() ? loc.getAltitude()
											+ "m" : "?")
									+ " \nAcc: "
									+ (loc.hasAccuracy() ? loc.getAccuracy()
											+ "m" : "?"), Toast.LENGTH_SHORT)
							.show();
			}
		}

		public void onProviderDisabled(String provider) {
			if (showingDebugToast)
				Toast.makeText(getBaseContext(),
						"onProviderDisabled: " + provider, Toast.LENGTH_SHORT)
						.show();

		}

		public void onProviderEnabled(String provider) {
			if (showingDebugToast)
				Toast.makeText(getBaseContext(),
						"onProviderEnabled: " + provider, Toast.LENGTH_SHORT)
						.show();

		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			String showStatus = null;
			if (status == LocationProvider.AVAILABLE)
				showStatus = "Available";
			if (status == LocationProvider.TEMPORARILY_UNAVAILABLE)
				showStatus = "Temporarily Unavailable";
			if (status == LocationProvider.OUT_OF_SERVICE)
				showStatus = "Out of Service";
			if (status != lastStatus && showingDebugToast) {
				Toast.makeText(getBaseContext(), "new status: " + showStatus,
						Toast.LENGTH_SHORT).show();
			}
			lastStatus = status;
		}

	}

	// Below is the service framework methods

	private NotificationManager mNM;

	@Override
	public void onCreate() {
		super.onCreate();
		Toast.makeText(this, "Created", Toast.LENGTH_LONG).show();
		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		alarmManagerPositioning = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		Intent intentToFire = new Intent(this, receiver.class);
		intentToFire.setAction("com.sommelsdijk.project.receiver");
		pendingIntentPositioning = PendingIntent.getBroadcast(this, 0,
				intentToFire, 0);

		startLoggerService();

		// Display a notification about us starting. We put an icon in the
		// status bar.
		showNotification();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		shutdownLoggerService();

		// Cancel the persistent notification.
		mNM.cancel(R.string.local_service_started);

		// Tell the user we stopped.
		Toast.makeText(this, R.string.local_service_stopped, Toast.LENGTH_SHORT)
				.show();
	}

	/**
	 * Show a notification while this service is running.
	 */
	private void showNotification() {
		// In this sample, we'll use the same text for the ticker and the
		// expanded notification
		CharSequence text = getText(R.string.local_service_started);

		// Set the icon, scrolling text and timestamp
		Notification notification = new Notification(R.drawable.service_icon,
				text, System.currentTimeMillis());

		// The PendingIntent to launch our activity if the user selects this
		// notification
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, stopServiceJ.class), 0);

		// Set the info for the views that show in the notification panel.
		notification.setLatestEventInfo(this, getText(R.string.service_name),
				text, contentIntent);

		// Send the notification.
		// We use a layout id because it is a unique number. We use it later to
		// cancel.
		mNM.notify(R.string.local_service_started, notification);
	}

	// This is the object that receives interactions from clients. See
	// RemoteService for a more complete example.
	private final IBinder mBinder = new LocalBinder();

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	public static void setMinTimeMillis(long _minTimeMillis) {
		minTimeMillis = _minTimeMillis;
	}

	public static long getMinTimeMillis() {
		return minTimeMillis;
	}

	public static void setMinDistanceMeters(long _minDistanceMeters) {
		minDistanceMeters = _minDistanceMeters;
	}

	public static long getMinDistanceMeters() {
		return minDistanceMeters;
	}

	public static float getMinAccuracyMeters() {
		return minAccuracyMeters;
	}

	public static void setMinAccuracyMeters(float minAccuracyMeters) {
		positionReceiver.minAccuracyMeters = minAccuracyMeters;
	}

	public static void setShowingDebugToast(boolean showingDebugToast) {
		positionReceiver.showingDebugToast = showingDebugToast;
	}

	public static boolean isShowingDebugToast() {
		return showingDebugToast;
	}

	/**
	 * Class for clients to access. Because we know this service always runs in
	 * the same process as its clients, we don't need to deal with IPC.
	 */
	public class LocalBinder extends Binder {
		positionReceiver getService() {
			return positionReceiver.this;
		}
	}

}
