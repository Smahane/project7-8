package com.schriek.servlet;

import java.io.IOException;
import java.sql.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Servlet
 */
@WebServlet("/Servlet")
public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection conn;
	private Statement s;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Servlet() {
		super();

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			conn = DriverManager.getConnection(
					"jdbc:mysql://192.168.2.5:3306/project78", "project78",
					"sommelsdijk");
			s = conn.createStatement();
			DatabaseMetaData dbm = conn.getMetaData();

			String[] types = { "TABLE" };
			ResultSet res = dbm.getTables(null, null, "%", types);

			while (res.next()) {
				if (!res.getString("TABLE_NAME").equalsIgnoreCase("home")
						&& !res.getString("TABLE_NAME").equalsIgnoreCase(
								"TrustedLocations")
						&& !res.getString("TABLE_NAME").equalsIgnoreCase(
								"Vision")) {
					System.out.println(res.getString("TABLE_NAME"));

					ResultSet results = s.executeQuery("SELECT * FROM "
							+ res.getString("TABLE_NAME"));

					int res_size = 0;
					while (results.next()) {
						res_size++;
					}

					System.out.println(res_size);
					float[] rset_lng = new float[res_size];
					float[] rset_lat = new float[res_size];

					results.first();
					int index = 0;
					while (results.next()) {
						rset_lng[index] = results.getFloat(2);
						rset_lat[index] = results.getFloat(3);
						index++;
					}

					System.out.println(rset_lng.length + " " + rset_lat.length);

					List<String> locations = new LinkedList<String>();
					List<String> temp = new LinkedList<String>();
					locations.add("" + rset_lng[1] + " " + rset_lat[1]);
					
					for (int i = 1; i < res_size; i++) {

						System.out.println(i);
						int ok = 0;
						for (ListIterator<String> it = locations.listIterator(); it
								.hasNext();) {
							String e = it.next();

							String[] split = e.split(" ");
							float longtitude = Float.valueOf(split[0].trim())
									.floatValue();
							float latitude = Float.valueOf(split[1].trim())
									.floatValue();

							double dist = Utils.distFrom((double) latitude,
									(double) longtitude, (double) rset_lat[i],
									(double) rset_lng[i]);

							if (dist > 1.0) {
								System.out.println("" + rset_lng[i] + " " + rset_lat[i]);
							}
						}
					}

					for (String e : temp) {
						System.out.println(e);
					}
				}
			}

			System.out.println("Connected");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
