package com.sommelsdijk.project;

import java.util.concurrent.ExecutionException;

public class TrustedLocations {

	private float homeLongitude;
	private float homeLatitude;
	private String devNaam;
	private String trustedLocations[];

	public TrustedLocations(String devNaam) {
		this.devNaam = devNaam;

	}

	protected void getHomeLocation() {
		try {
			try {
				String getTrustedResultSet = new dbLees("project78",
						"sommelsdijk", false).execute("leeshome", devNaam)
						.get();
				String[] tmp = getTrustedResultSet.split("/");
				
				for(String temp : tmp) {
					System.out.println(temp);
				}
				
				homeLatitude = Float.parseFloat(tmp[2]);
				homeLongitude = Float.parseFloat(tmp[3]);
				
				System.out.println(homeLatitude + " " + homeLongitude);

			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			} catch (InterruptedException e) {
				e.printStackTrace();
		}

	}
}
