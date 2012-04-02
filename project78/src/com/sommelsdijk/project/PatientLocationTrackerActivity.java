package com.sommelsdijk.project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.ItemizedOverlay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Criteria;

public class PatientLocationTrackerActivity extends MapActivity {

	private MapView mapView;
	private final String tag = "MainActivity";
	private MapController mapController;
	private LocationManager mlocManager;
	private LocationListener mlocListener;
	private MyOverlays itemizedoverlay;
	private MyLocationOverlay myLocationOverlay;
	List<Address> addresses = null;
	private Address sameLocation = null;
	private TextView locationTV;
	private String devNaam;
	private MenuItem stop;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		devNaam = android.os.Build.MODEL;
		Log.d("devNaam", "" + devNaam);

		//this.startService(new Intent(PatientLocationTrackerActivity.this, positionReceiver.class));
		//positionReceiver.setMinTimeMillis((10 * 60 * 1000));
		//positionReceiver.setExtern(false);
		
		dbSchrijf schrijf = new dbSchrijf("project78", "sommelsdijk");
		schrijf.setInternal(false);
		schrijf.execute("create", devNaam,
				"" + "0.023939", "" + "0.040404", ""
						+ System.currentTimeMillis());
		Initialize();

		myLocationOverlay.runOnFirstFix(new Runnable() {
			public void run() {
				mapView.getController().animateTo(
						myLocationOverlay.getMyLocation());

			}
		});

		mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mlocListener = new MyLocationListener();
		mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000,
				0, mlocListener);

	} // 5secs update // 0 = locatieverandering triggert geen update


	/*
	 * Lees database op devNaam
	 */
	private String leesDb(boolean isInternal, String devNaam) {
		dbLees lees = new dbLees(this, "project78", "sommelsdijk");
		lees.setInternal(isInternal);
		try {
			String result = lees.execute(devNaam).get();
			if (result != null) {
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Foutje";
	}

	/*
	 * Schrijf longtitude en latitude weg naar behorende apparaatnaam
	 */
	private void schrijfDb(boolean isInternal, String devNaam, float latitude,
			float longtitude) {

		dbSchrijf schrijf = new dbSchrijf("project78", "sommelsdijk");
		schrijf.setInternal(isInternal);
		schrijf.execute(devNaam, "" + latitude, "" + longtitude);
	}

	/*
	 * Check database of apparaatnaam al bestaat, zo ja; doe niks, zo nee; maak
	 * een nieuwe rij met apparaatnaam
	 */
	private void checkDb(String devNaam, boolean isInternal) {
		dbLees lees = new dbLees(this, "project78", "sommelsdijk");
		lees.setInternal(isInternal);
		try {
			String test = lees.execute(devNaam).get();
			if (test != null) {
				Log.i(tag, "Rij bestaat in db");
			} else {
				dbSchrijf schrijf = new dbSchrijf("project78",
						"sommelsdijk");
				schrijf.setInternal(isInternal);
				schrijf.execute("create", devNaam);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void Initialize() {

		locationTV = (TextView) findViewById(R.id.tvLocation);
		mapView = (MapView) findViewById(R.id.mapView);
		mapView.setBuiltInZoomControls(true);
		mapView.setSatellite(true);
		mapController = mapView.getController();
		// zoom van 1 tot 21, 1 is wereldmap.
		mapController.setZoom(20);
		// Geven de overlay de context en onze mapview mee
		myLocationOverlay = new MyLocationOverlay(this, mapView);

		mapView.getOverlays().add(myLocationOverlay);
		Drawable drawable = this.getResources().getDrawable(R.drawable.pltoma);
		itemizedoverlay = new MyOverlays(this, drawable);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Criteria criteria = new Criteria();
		mlocManager.requestLocationUpdates(5000, 100, criteria, mlocListener,
				null);
	}

	/* Remove the locationlistener updates when Activity is paused */
	@Override
	protected void onPause() {
		super.onPause();
		mlocManager.removeUpdates(mlocListener);
	}

	private void createMarker(GeoPoint gp) {
		OverlayItem overlayitem = new OverlayItem(gp, "", "");
		itemizedoverlay.addOverlay(overlayitem);

		if (itemizedoverlay.size() > 0) {
			mapView.getOverlays().add(itemizedoverlay);
		}
	}

	public class MyLocationListener implements LocationListener {
		public void onLocationChanged(Location loc) {
			try {
				getAddressForLocation(getApplicationContext(), loc);
				//mlocManager.removeUpdates(mlocListener);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		public void onProviderDisabled(String provider) {
			Toast.makeText(getApplicationContext(), "Gps Disabled",
					Toast.LENGTH_LONG).show();
		}

		public void onProviderEnabled(String provider) {
			Toast.makeText(getApplicationContext(), "Gps Enabled",
					Toast.LENGTH_SHORT).show();
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		public Address getAddressForLocation(Context context, Location location)
				throws IOException {

			if (location == null) {
				return null;
			}
			int maxResults = 1;
			System.out.println(Locale.getDefault());
			int lat = (int) (location.getLatitude() * 1E6);
			int lng = (int) (location.getLongitude() * 1E6);
			GeoPoint gp = new GeoPoint(lat, lng);
			createMarker(gp);
			mapController.animateTo(gp);

			Geocoder gc = new Geocoder(getApplicationContext(),
					Locale.getDefault());

			List<Address> addresses = gc
					.getFromLocation(location.getLatitude(),
							location.getLongitude(), maxResults);
			System.out.println(addresses.toString());

			if (!addresses.get(0).getAddressLine(0).isEmpty()
					&& sameLocation != addresses.get(0)) {
				locationTV.setText("De patient bevind zich op het adres "
						+ addresses.get(0).getAddressLine(0));
				itemizedoverlay.locatie = addresses.get(0).getAddressLine(0)
						+ " \n" + location.getTime();

				sameLocation = addresses.get(0);
			}

			if (addresses.size() == 1) {
				return addresses.get(0);
			} else {
				return null;
			}
		}

	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}

//0H6cbPmYFwiQTNV-yM5b8tX-Uz-yOtKSlMShg9Q
//schriek

// B6:CD:2F:86:1E:EB:21:9B:6F:C6:1C:EE:AF:85:E2:3E
/*
 * C:\Users\Danny\.android
 * 
 * $ keytool -list -alias androiddebugkey \ -keystore
 * C:\Users\Danny\.android\debug.keystore -storepass android -keypass android
 * 
 * apikey 0oBic7LCnLL8CDepLyfzYgiA_5aMNECZ5CafYCA
 * 
 * <com.google.android.maps.MapView android:layout_width="fill_parent"
 * android:layout_height="fill_parent"
 * android:apiKey="0oBic7LCnLL_qXSw0UDOnWqz2mdnAfNO0a7J3QA" />
 */
