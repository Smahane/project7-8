package com.sommelsdijk.project;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.OverlayItem;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Criteria;

public class PatientLocationTrackerActivity extends MapActivity {

	static MapView mapView;
	private final String tag = "MainActivity";
	private MapController mapController;
	private LocationManager mlocManager;
	private LocationListener mlocListener;
	private MyOverlays itemizedoverlay;
	private MyOverlays homeOverlay;
	private MyLocationOverlay myLocationOverlay;
	List<Address> addresses = null;
	private Address sameLocation = null;
	private TextView locationTV;
	private String devNaam;
	private float homeLatitude;
	private float homeLongitude;
	private boolean homeIsSet;
	private boolean internal;
	private static KnownOverlays TrustedLocations; // instantie van en Overlay
													// klasse,
	// kunnen 5 verschillende overlays
	// zijn, aanpasbaar
	private static GeoPoint gpForTrustedLocations[];
	private static int gpForTrustedLocationsCounter;
	private float longtitude;
	private float latitude;
	private boolean showingTrustedLocations;
	private SeekBar seekBar;
	private float radius;
	private boolean ShowSeekBar;
	private int CircleLocationInMapView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		devNaam = android.os.Build.MODEL;
		Log.d("devNaam", "" + devNaam);

		// Gets home location from the database;
		internal = false;
		getHomeLocation();

		this.startService(new Intent(PatientLocationTrackerActivity.this,
				positionReceiver.class));
		positionReceiver.setMinTimeMillis((2 * 60 * 1000));
		positionReceiver.setExtern(internal);

		// Creates a fake location in the DB;
		// new dbSchrijf("project78", "sommelsdijk", true).execute(
		// "TrustedLocations", devNaam, "" + 50.f, "" + 50f);

		// leesDb(false, "leeshome");

		Initialize();
		createHomeOverlay();

		/*
		 * myLocationOverlay.runOnFirstFix(new Runnable() { public void run() {
		 * mapView.getController().animateTo(
		 * myLocationOverlay.getMyLocation()); } });
		 */

		mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mlocListener = new MyLocationListener();
		mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				1000000, 0, mlocListener);

		/*
		 * De SeekBar listener 
		 * Progress is hoever de schuiver staat. 
		 * Radius is de radius van de cirkel die we willen tekenen 
		 * Eerst deleten we alle bestaande overlays, om ze vervolgens opnieuw te tekenen 
		 * Inclusief de nieuwe HomeCirkel
		 */
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			int progress = 0;

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				this.progress = progress;
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			public void onStopTrackingTouch(SeekBar seekBar) {
				System.out.println(" SEEKBAR VALUE =  " + progress);
				radius = progress * 2;
				try {
					mapView.getOverlays().clear();
					homeIsSet = false;
					mlocManager.requestSingleUpdate(mlocManager.GPS_PROVIDER,
							mlocListener, null);
				} catch (ArrayIndexOutOfBoundsException e) {
					Log.i(tag, "Foutje met de Slider!");
				}
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.ShowTrustedLocs:
			if (!showingTrustedLocations) {
				getTrustedLocation();
				mlocManager.requestSingleUpdate(mlocManager.GPS_PROVIDER,
						mlocListener, null);
			} else {
				mapView.getOverlays().clear();
				showingTrustedLocations = false;
				homeIsSet = false;
				mlocManager.requestSingleUpdate(mlocManager.GPS_PROVIDER,
						mlocListener, null);
			}
			Toast.makeText(this, "Redrawing map", Toast.LENGTH_SHORT);
			break;
		case R.id.ChangeSafeZone:
			if (!ShowSeekBar) {
				seekBar.setVisibility(1);
				ShowSeekBar = true;
			} else {
				seekBar.setVisibility(View.GONE);
				ShowSeekBar = false;
			}
			break;
		case R.id.ForceUpdate:
			mlocManager.requestSingleUpdate(mlocManager.GPS_PROVIDER,
					mlocListener, null);
			break;
		}
		return true;
	}

	private void getHomeLocation() {
		try {
			try {
				String getHomeResultSet = new dbLees("project78",
						"sommelsdijk", internal).execute("leeshome", devNaam)
						.get();
				try {
					String[] tmp = getHomeResultSet.split(" ");
					homeLatitude = Float.parseFloat(tmp[2]);
					homeLongitude = Float.parseFloat(tmp[3]);

				} catch (Exception e) {
					Toast.makeText(this, "Thuis adres niet gevonden!",
							Toast.LENGTH_LONG).show();
					Log.i(getHomeResultSet, "ResultSet is NULL");
				}

			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void createHomeOverlay() {
		int lat = (int) (homeLatitude * 1E6);
		int lng = (int) (homeLongitude * 1E6);
		if (lat != 0 && lng != 0) {
			GeoPoint gp = new GeoPoint(lat, lng);
			System.out.println(lat + " " + lng);
			OverlayItem overlayitem = new OverlayItem(gp, "", "");
			homeOverlay.addOverlay(overlayitem);
		}

		if (homeOverlay.size() > 0) {
			mapView.getOverlays().add(homeOverlay);
			mapView.getOverlays().add(
					new CircleOverlay(this, homeLatitude, homeLongitude,
							radius, CircleOverlay.home));
			CircleLocationInMapView = mapView.getOverlays().size();
			homeIsSet = true;
		} else {
			homeIsSet = false;
		}
	}

	protected void getTrustedLocation() {
		try {
			try {
				String getTrustedResultSet = new dbLees("project78",
						"sommelsdijk", internal)
						.execute("leestrusted", devNaam).get();
				String[] tmp = getTrustedResultSet.split("/");

				for (String s : tmp) {
					String[] split = s.split(" ");

					String id = split[0];
					latitude = Float.parseFloat(split[1]);
					longtitude = Float.parseFloat(split[2]);

					mapView.getOverlays().add(
							new CircleOverlay(this, latitude, longtitude, 100,
									CircleOverlay.trusted));

				}

			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		showingTrustedLocations = true;
	}

	private void Initialize() {
		locationTV = (TextView) findViewById(R.id.tvLocation);
		mapView = (MapView) findViewById(R.id.mapView);
		mapView.setBuiltInZoomControls(true);
		mapView.setSatellite(true);
		mapController = mapView.getController();
		// zoom van 1 tot 21, 1 is wereldmap.
		mapController.setZoom(18);
		// Geven de overlay de context en onze mapview mee
		myLocationOverlay = new MyLocationOverlay(this, mapView);

		mapView.getOverlays().add(myLocationOverlay);
		Drawable drawable = this.getResources().getDrawable(R.drawable.pltoma);
		itemizedoverlay = new MyOverlays(this, drawable, internal);
		Drawable drawable2 = this.getResources().getDrawable(
				R.drawable.service_icon);
		TrustedLocations = new KnownOverlays(this, drawable2, internal);

		// Array van GEOPOINTS, kunnen de GPs in de database opslaan, door deze
		// weer op te vragen kunnen we locaties op de map tekenen.
		gpForTrustedLocations = new GeoPoint[10];
		// Counter is nodig om de array op te schuiven.
		gpForTrustedLocationsCounter = 0;

		Drawable homeDrawable = this.getResources()
				.getDrawable(R.drawable.home);
		homeOverlay = new MyOverlays(this, homeDrawable, internal);
		seekBar = (SeekBar) findViewById(R.id.seekbar);
		seekBar.setVisibility(View.GONE);
		radius = 100;

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

	protected static void createTrustedLocation() {
		OverlayItem overlayitem = new OverlayItem(
				gpForTrustedLocations[gpForTrustedLocationsCounter], "", "");
		TrustedLocations.addOverlay(overlayitem);

		if (TrustedLocations.size() > 0) {
			mapView.getOverlays().add(TrustedLocations);
		}
		gpForTrustedLocationsCounter++;
	}

	public class MyLocationListener implements LocationListener {
		public void onLocationChanged(Location loc) {
			try {
				getAddressForLocation(getApplicationContext(), loc);
				// mlocManager.removeUpdates(mlocListener);
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

			// Geven de Huidige GP mee in de array, later moet dit niet gezelfde
			// GP zijn als bijv het huisadres.
			if (gpForTrustedLocationsCounter < 10) {
				gpForTrustedLocations[gpForTrustedLocationsCounter] = gp;
			}
			if (!homeIsSet) {

				getHomeLocation();
				createHomeOverlay();
			}

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
				itemizedoverlay.currentAddress = addresses.get(0);

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

// 0H6cbPmYFwiQTNV-yM5b8tX-Uz-yOtKSlMShg9Q
// schriek

// B6:CD:2F:86:1E:EB:21:9B:6F:C6:1C:EE:AF:85:E2:3E
/*
 * C:\Users\Danny\.android
 * 
 * $ keytool -list -alias androiddebugkey \ -keystore
 * C:\Users\Danny\.android\debug.keystore -storepass android -keypass android
 * 
 * apikey 0oBic7LCnLL8CDepLyfzYgiA_5aMNECZ5CafYCA // DANNY apikey
 * 0H6cbPmYFwiQTNV-yM5b8tX-Uz-yOtKSlMShg9Q // SCHRIEK
 * 
 * <com.google.android.maps.MapView android:layout_width="fill_parent"
 * android:layout_height="fill_parent"
 * android:apiKey="0oBic7LCnLL_qXSw0UDOnWqz2mdnAfNO0a7J3QA" />
 */
