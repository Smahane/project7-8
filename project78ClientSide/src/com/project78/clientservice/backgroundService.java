package com.project78.clientservice;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.maps.GeoPoint;



public class backgroundService extends Service {
	

	private LocationManager lm;
	private LocationListener locationListener;
	private GeoPoint gp;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override   
	public void onCreate() {
		System.out.println("SERVICE IS GESTART!!!!!!!");
		Project78ClientSideActivity.tv.setText("Service is gestart");
		
		getGpsLocation();
	 runTcpClient();

	} 

	public void getGpsLocation(){
		
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationListener = new MyLocationListener();
		
		lm.requestSingleUpdate(lm.GPS_PROVIDER,
				locationListener, null);

	}

	
	public class MyLocationListener implements LocationListener {
		public void onLocationChanged(Location loc) {
			try{		
			int lat = (int) (loc.getLatitude() * 1E6);
			int lng = (int) (loc.getLongitude() * 1E6);	
			gp = new GeoPoint(lat, lng);
			// mlocManager.removeUpdates(mlocListener);
			}catch(Exception e){
				Log.i("LocationException", "Kon geen GeoPoint maken");
			}
		}

		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub
			
		}

	}

    private void runTcpClient() {
    	
    	final int socketNumber = 21100;

        try {

            Socket s = new Socket("145.53.174.55", socketNumber);
            
            Project78ClientSideActivity.tv.setText("Connecting");

            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

            //send output msg

            String outMsg = "145.53.174.55%bejaarde%edwin";

            out.write(outMsg);
            Project78ClientSideActivity.tv.setText(outMsg);
            out.flush();

            Log.i("TcpClient", "sent: " + outMsg);
            
            //accept server response

            String inMsg = in.readLine() + System.getProperty("line.separator");

            Log.i("TcpClient", "received: " + inMsg);
            
            //close connection
            s.close();

        } catch (UnknownHostException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        } 

    }

}