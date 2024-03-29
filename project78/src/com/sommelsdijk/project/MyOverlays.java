package com.sommelsdijk.project;

import java.util.concurrent.ExecutionException;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.os.SystemClock;
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
	protected Address currentAddress = null;
	private String devNaam;
	private boolean internal;

	public MyOverlays(Context context, Drawable defaultMarker, boolean internal) {
		super(boundCenterBottom(defaultMarker));
		this.context = context;
		this.internal = internal;
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

	public void Builder() {
		devNaam = android.os.Build.MODEL;
		devNaam = devNaam.replaceAll(" ", "");
		Builder builder = new AlertDialog.Builder(context);
		if (currentAddress != null) {
			builder.setMessage("Is " + currentAddress.getAddressLine(0)
					+ " uw huisadres?");
		} else {
			builder.setMessage("GEEN ADRES BESCHIKBAAR");
		}
		builder.setCancelable(true);
		builder.setPositiveButton("Ja", new JaOnClickListener());
		builder.setNegativeButton("Cancel", new CancelOnClickListener());
		builder.setNeutralButton("TrustedLocation",
				new TrustedLocationListener());
		AlertDialog dialog = builder.create();
		dialog.show();

	}

	private final class CancelOnClickListener implements
			DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
			Toast.makeText(context, "cancelled", Toast.LENGTH_LONG).show();
		}
	}

	private final class JaOnClickListener implements
			DialogInterface.OnClickListener {

		public void onClick(DialogInterface dialog, int which) {

			Toast.makeText(context, "Uw nieuwe huisadres wordt opgeslagen",
					Toast.LENGTH_LONG).show();

			try {
				String res = new dbLees("project78", "sommelsdijk", internal)
						.execute("leeshome", devNaam).get();
				if (res == null) {
					new dbSchrijf("project78", "sommelsdijk", internal)
							.execute("home", devNaam,
									"" + currentAddress.getLatitude(), ""
											+ currentAddress.getLongitude(), ""
											+ SystemClock.uptimeMillis());
				} else {
					new dbSchrijf("project78", "sommelsdijk", internal)
							.execute("updateHome", devNaam,
									"" + currentAddress.getLatitude(), ""
											+ currentAddress.getLongitude());
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private final class TrustedLocationListener implements
			DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
			PatientLocationTrackerActivity.createTrustedLocation();

			Toast.makeText(context, "Vertrouwde locatie wordt opgeslagen",
					Toast.LENGTH_LONG).show();

			// dbSchrijf schrijf = new dbSchrijf("project78", "sommelsdijk");
			// schrijf.setInternal(false);

			// schrijf.execute("trustedlocation", devNaam,
			// "" + currentAddress.getLatitude(), "" +
			// currentAddress.getLongitude(), ""
			// + System.currentTimeMillis());

			new dbSchrijf("project78", "sommelsdijk", internal).execute(
					"TrustedLocations", devNaam,
					"" + currentAddress.getLatitude(),
					"" + currentAddress.getLongitude());

		}

	}
}
