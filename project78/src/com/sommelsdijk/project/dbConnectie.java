package com.sommelsdijk.project;

import java.sql.*;

import android.os.AsyncTask;
import android.util.Log;

public class dbConnectie extends AsyncTask<Void, Void, Void>{

	private final static String tag = "dbConnectie";
	private String login;
	private String password;
	private boolean isInternal;

	private String url;

	public dbConnectie(String login, String password) {
		this.login = login;
		this.password = password;
	}
	
	public void setInternal(boolean isInternal) {
		this.isInternal = isInternal;
	}

	@Override
	protected Void doInBackground(Void... params) {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			if(isInternal) {
				url = "jdbc:mysql://192.168.2.5:3306/project78";
			} else {
				url = "jdbc:mysql://schriek.dscloud.me:3306/project78";
			}
			
			Connection conn = DriverManager.getConnection(url, login, password);
			
			Log.i(tag, "Connected");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
