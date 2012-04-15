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
	public dbSchrijf(String login, String password) {
		this.login = login;
		this.password = password;
		// this.context = context;
	}
	
	public dbSchrijf(String login, String password, boolean extern) {
		this.login = login;
		this.password = password;
		this.isInternal = extern;
		// this.context = context;
	}

	/*
	 * Intern of externe ip-address
	 */
	public void setInternal(boolean isInternal) {
		this.isInternal = isInternal;
	}

	/*
	 * (non-Javadoc) Schrijven naar rij met devNaam of nieuwe rij aanmaken met
	 * devNaam
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

			if (params[0] == "table") {
				String[] tmp = params[1].split(" ");
				recordsUpdated = s
						.executeUpdate("CREATE TABLE IF NOT EXISTS "
								+ tmp[1]
								+ " (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, longtitude FLOAT, latitude FLOAT, timestamp FLOAT);");
			}
			
			if (params[0] == "create") {
				String[] tmp = params[1].split(" ");
				System.out.println(tmp[1]);
				recordsUpdated = s
						.executeUpdate("INSERT INTO " + tmp[1] + " VALUES (NULL,'"
								+ params[2] + "','" + params[3] + "','"
								+ params[4] + "')");

				Log.i(tag, recordsUpdated + " Rijen aangemaakt met id = "
						+ params[1]);
			}
			
			if (params[0] == "home") {
				recordsUpdated = s
						.executeUpdate("INSERT INTO home VALUES (NULL,'" 
								+ params[1] + "','" + params[2] + "','"
								+ params[3] + "','" + params[4] + "')");

				Log.i(tag, recordsUpdated + " Rijen aangemaakt met id = "
						+ params[1]);
			}
			
			if(params[0] == "updateHome") {
				recordsUpdated = s.executeUpdate("UPDATE home SET latitude = '"
								+ params[2] + "', longtitude = '" + params[3]
								+ "' WHERE devNaam = '" + params[1] + "'");
			}
			
			if (params[0] == "update") {
				recordsUpdated = s
						.executeUpdate("UPDATE gegevens SET latitude = '"
								+ params[2] + "', longtitude = '" + params[3]
								+ "' WHERE id = '" + params[1] + "'");

				Log.i(tag, recordsUpdated + " Records updated");
			}

			Log.i(tag, "Verbonden met db");
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

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}
}
