package com.sommelsdijk.project;

import java.sql.*;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class dbLees extends AsyncTask<String, Void, String> {

	private final static String tag = "dbLees";
	private String login;
	private String password;
	private boolean isInternal;
	private String url;
	private Context context;
	private Connection conn;
	private String devNaam;

	/*
	 * Database connectie leggen met naam en wachtwoord
	 */
	public dbLees(Context context, String login, String password) {
		this.login = login;
		this.password = password;
		this.context = context;
	}

	/*
	 * Intern of externe ip-address
	 */
	public void setInternal(boolean isInternal) {
		this.isInternal = isInternal;
	}

	/*
	 * (non-Javadoc) De uiteindelijk connectie leggen
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected String doInBackground(String... params) {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			if (isInternal) {
				url = "jdbc:mysql://192.168.2.5:3306/project78";
			} else {
				url = "jdbc:mysql://schriek.dscloud.me:3306/project78";
			}

			conn = DriverManager.getConnection(url, login, password);
			Statement s = conn.createStatement();

			if (params[0] == "leeshome") {
				ResultSet rs = null;

				rs = s.executeQuery("SELECT * FROM home WHERE devNaam = '"
						+ params[1] + "'");
				while (rs.next()) {
					String devnom = rs.getString(1);
					float latitude = rs.getFloat(2);
					float longtitude = rs.getFloat(3);
					
					return devnom + " " + latitude + " " + longtitude;
				}
			}

			Log.i(tag, "Connected");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
}
