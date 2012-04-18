package com.sommelsdijk.project;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class CircleOverlay extends Overlay {

	Context context;
	double mLat;
	double mLon;
	float radius;

	public CircleOverlay(Context _context, double _lat, double _lon, float radius) {
		context = _context;
		mLat = _lat;
		mLon = _lon;
		this.radius = radius;
	}

	public void draw(Canvas canvas, MapView mapView, boolean shadow) {

		super.draw(canvas, mapView, shadow);

		Projection projection = mapView.getProjection();

		Point pt = new Point();

		GeoPoint geo = new GeoPoint((int) (mLat * 1e6), (int) (mLon * 1e6));

		projection.toPixels(geo, pt);

		float circleRadius = this.radius;

		Paint innerCirclePaint;

		innerCirclePaint = new Paint();
		innerCirclePaint.setARGB(255, 255, 255, 255);
		innerCirclePaint.setAntiAlias(true);

		innerCirclePaint.setStyle(Paint.Style.STROKE);

		canvas.drawCircle((float) pt.x, (float) pt.y, circleRadius,
				innerCirclePaint);
	}
}