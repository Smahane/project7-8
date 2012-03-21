package com.sommelsdijk.project;

import java.sql.*;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class dbSchrijf extends AsyncTask<String, Void, String> {

	private final static String tag = "dbSchrijf";
	private String login;
	private String password;
	private boolean isInternal;
	private String url;
	private Context context;
	private Connection conn;

	/*
	 * Database connectie leggen met naam en wachtwoord
	 */
	public dbSchrijf(Context context, String login, String password) {
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

			int recordsUpdated;
			recordsUpdated = s.executeUpdate("INSERT INTO gegevens VALUES ('" + params[0] + "','" + params[1] + "','" + params[2] + "')");
			
			Log.i(tag, recordsUpdated + " Records updated");


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
