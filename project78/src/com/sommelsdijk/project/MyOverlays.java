package com.sommelsdijk.project;


import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class MyOverlays extends ItemizedOverlay<OverlayItem> {

	private static int maxNum = 1;
	private OverlayItem overlays[] = new OverlayItem[maxNum];
	private int index = 0;
	private boolean full = false;
	private static Context context;
	private OverlayItem previousoverlay;
	protected Address huisAdres = null;
	private String devNaam;

	public MyOverlays(Context context, Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
		this.context = context;
	}

	@Override
	protected OverlayItem createItem(int i) {
		return overlays[i];
	}
	
	@Override
	public int size() {
		if (full) {
			return overlays.length;
		} else {
			return index;
		}

	}


	public void addOverlay(OverlayItem overlay) {
		if (previousoverlay != null) {
			if (index < maxNum) {
				overlays[index] = previousoverlay;
			} else {
				index = 0;
				full = true;
				overlays[index] = previousoverlay;
			}
			index++;
			populate();
		}
		this.previousoverlay = overlay;
	}

	protected boolean onTap(int index) {
		OverlayItem overlayItem = overlays[index];
		
		Builder();
		
		return true;
	};
	
	public void Builder(){
		devNaam = android.os.Build.MODEL;
		Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("Is " + huisAdres.getAddressLine(0) + " uw huisadres?");
		builder.setCancelable(true);
		builder.setPositiveButton("Ja", new JaOnClickListener());
		builder.setNegativeButton("Cancel", new CancelOnClickListener());
		AlertDialog dialog = builder.create();
		dialog.show();
	
		
	}
	

	private final class CancelOnClickListener implements
			DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
			Toast.makeText(context, "cancelled", Toast.LENGTH_LONG)
					.show();			
		}
	}
	
	private final class JaOnClickListener implements
	DialogInterface.OnClickListener {
		
		MyOverlays HomeOverlay;
			public void onClick(DialogInterface dialog, int which) {
	Toast.makeText(context, "Uw nieuwe huisadres wordt opgeslagen", Toast.LENGTH_LONG)
			.show();

	/*dbSchrijf schrijf = new dbSchrijf("project78", "sommelsdijk");
	schrijf.setInternal(false);
	
	schrijf.execute("create", devNaam,
			"" + "longtitude", "" + "latitude", ""
					+ System.currentTimeMillis());
	*/
	Drawable drawableHome = MyOverlays.context.getResources().getDrawable(R.drawable.home);
	HomeOverlay = new MyOverlays(MyOverlays.context, drawableHome);
	GeoPoint gp = new GeoPoint((int) huisAdres.getLatitude(), (int) huisAdres.getLongitude());
	
	createMarker(gp);
	
			}
			private void createMarker(GeoPoint gp) {
				OverlayItem overlayitem = new OverlayItem(gp, "", "");
				HomeOverlay.addOverlay(overlayitem);
				

				if (HomeOverlay.size() > 0) {
					PatientLocationTrackerActivity.mapView.getOverlays().add(HomeOverlay);
				
				}
			}
	
}
}


	