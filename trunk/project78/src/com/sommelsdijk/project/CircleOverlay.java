package com.sommelsdijk.project;

import android.content.Context;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class CircleOverlay extends Overlay {

	private Context context;
	private double mLat;
	private double mLon;
	private float radius;
	private int type;
	public final static int home = Color.BLACK;
	public final static int trusted = Color.BLUE;

	public CircleOverlay(Context _context, double _lat, double _lon,
			float radius, int type) {
		this.context = _context;
		this.mLat = _lat;
		this.mLon = _lon;
		this.radius = radius;
		this.type = type;
	}

	public static int metersToRadius(float meters, MapView map, double latitude) {
		return (int) (map.getProjection().metersToEquatorPixels(meters) * (1 / Math
				.cos(Math.toRadians(latitude))));
	}

	public void draw(Canvas canvas, MapView mapView, boolean shadow) {

		super.draw(canvas, mapView, shadow);

		Projection projection = mapView.getProjection();

		Point pt = new Point();

		GeoPoint geo = new GeoPoint((int) (mLat * 1e6), (int) (mLon * 1e6));

		projection.toPixels(geo, pt);

		float circleRadius = metersToRadius(radius, mapView, mLat);

		Paint innerCirclePaint;
		Paint outerCicrclePaint;

		innerCirclePaint = new Paint();
		innerCirclePaint.setARGB(96, 255, 255, 255);
		innerCirclePaint.setAntiAlias(true);

		outerCicrclePaint = new Paint();
		outerCicrclePaint.setARGB(200, 0, 0, 255);
		outerCicrclePaint.setAntiAlias(true);
		outerCicrclePaint.setShadowLayer((circleRadius + 5), 20f, 20f, type);

		innerCirclePaint.setStyle(Paint.Style.FILL);
		outerCicrclePaint.setStyle(Paint.Style.STROKE);

		canvas.drawCircle((float) pt.x, (float) pt.y, circleRadius,
				innerCirclePaint);

		canvas.drawCircle((float) pt.x, (float) pt.y, circleRadius,
				outerCicrclePaint);
	}
}