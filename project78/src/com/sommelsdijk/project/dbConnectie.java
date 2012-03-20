package com.sommelsdijk.project;

import java.sql.*;

import android.util.Log;

public class dbConnectie {

	private final static String tag = "hoi";

	public dbConnectie(String login, String password) {
		try {
			Class.forName("com.imaginary.sql.msql.MsqlDriver");

			String url = "jdbc:msql://www.schriek.dscloud.me......";
			Connection conn = DriverManager.getConnection(url, login, password);
		} catch (Exception e) {
			Log.wtf(tag, e.getMessage());
		}
	}
}
