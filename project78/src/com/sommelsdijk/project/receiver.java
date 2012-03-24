package com.sommelsdijk.project;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class receiver extends BroadcastReceiver {

	public static final String COMMAND = "SENDER";
	public static final int SENDER_ACT_DOCUMENT = 0;
	public static final int SENDER_SRV_POSITIONING = 1;
	public static final int MIN_TIME_REQUEST = 5 * 1000;

	public static final String ACTION_REFRESH_SCHEDULE_ALARM = "org.mabna.order.ACTION_REFRESH_SCHEDULE_ALARM";

	private static Location currentLocation;
	private static Location prevLocation;
	private static Context _context;
	private String provider = LocationManager.GPS_PROVIDER;
	private static Intent _intent;
	private static LocationManager locationManager;
	private static LocationListener locationListener = new LocationListener() {

		public void onStatusChanged(String provider, int status, Bundle extras) {
			try {
				String strStatus = "";
				switch (status) {
				case GpsStatus.GPS_EVENT_FIRST_FIX:
					strStatus = "GPS_EVENT_FIRST_FIX";
					break;
				case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
					strStatus = "GPS_EVENT_SATELLITE_STATUS";
					break;
				case GpsStatus.GPS_EVENT_STARTED:
					strStatus = "GPS_EVENT_STARTED";
					break;
				case GpsStatus.GPS_EVENT_STOPPED:
					strStatus = "GPS_EVENT_STOPPED";
					break;

				default:
					strStatus = String.valueOf(status);
					break;
				}
				Toast.makeText(_context, "Status: " + strStatus,
						Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void onProviderEnabled(String provider) {

		}

		public void onProviderDisabled(String provider) {

		}

		public void onLocationChanged(Location location) {
			try {
				Toast.makeText(_context, "***new location***",
						Toast.LENGTH_SHORT).show();
				gotLocation(location);
			} catch (Exception e) {

			}
		}
	};

	@Override
	public void onReceive(final Context context, Intent intent) {

		/*
		Toast.makeText(context, "new request received by receiver",
				Toast.LENGTH_SHORT).show();
		_context = context;
		_intent = intent;

		locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

		if (locationManager.isProviderEnabled(provider)) {
			locationManager.requestLocationUpdates(provider, MIN_TIME_REQUEST,
					5, locationListener);

			Location gotLoc = locationManager.getLastKnownLocation(provider);
			gotLocation(gotLoc);
		} else {
			Toast t = Toast.makeText(context, "please turn on GPS",
					Toast.LENGTH_LONG);
			t.setGravity(Gravity.CENTER, 0, 0);
			t.show();

			Intent settinsIntent = new Intent(
					android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			settinsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			_context.startActivity(settinsIntent);
		}*/
	}

	private static void gotLocation(Location location) {
		prevLocation = currentLocation == null ? null : new Location(
				currentLocation);
		currentLocation = location;

		if (isLocationNew()) {
			// OnNewLocationReceived(location);

			Toast.makeText(
					_context,
					"new location saved \n" + location.getLatitude() + "\n "
							+ location.getLongitude(), Toast.LENGTH_SHORT)
					.show();
			stopLocationListener();
		}
	}

	private static boolean isLocationNew() {
		if (currentLocation == null) {
			return false;
		} else if (prevLocation == null) {
			return true;
		} else if (currentLocation.getTime() == prevLocation.getTime()) {
			return false;
		} else {
			return true;
		}
	}

	public static void stopLocationListener() {
		locationManager.removeUpdates(locationListener);
		Toast.makeText(_context, "provider stoped", Toast.LENGTH_SHORT).show();
	}

}
