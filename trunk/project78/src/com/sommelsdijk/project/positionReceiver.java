package com.sommelsdijk.project;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class positionReceiver extends BroadcastReceiver {

	private static final int MIN_TIME_REQUEST = 5 * 1000;
	private static Context _context;
	private String provider = LocationManager.GPS_PROVIDER;
	private Intent _intent;
	private static LocationManager locationManager;
	private static LocationListener locationListener = new LocationListener() {

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			Log.i("banaan", "baanaaaan");
		}

		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			Log.i("banaan", "baanaaaan");
		}

		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			Log.i("banaan", "baanaaaan");
		}

		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			Log.i("banaan", "baanaaaan");
		}
	};

	@Override
	public void onReceive(final Context context, Intent intent) {
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
		}
	}

	private static void gotLocation(Location location) {
		int lat = (int) (location.getLatitude() * 1E6);
		int lng = (int) (location.getLongitude() * 1E6);

		Log.i("loc", "" + lat + " " + lng);
	}

	public static void stopLocationListener() {
		locationManager.removeUpdates(locationListener);
		Toast.makeText(_context, "provider stoped", Toast.LENGTH_SHORT).show();
	}

}
