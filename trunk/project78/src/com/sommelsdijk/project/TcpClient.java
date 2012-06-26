package com.sommelsdijk.project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.google.android.maps.GeoPoint;

import android.os.AsyncTask;
import android.util.Log;

public class TcpClient extends AsyncTask<String, String, String> {

	private static final int TCP_SERVER_PORT = 21100;
	private Socket s;
	private PatientLocationTrackerActivity pat;

	public TcpClient(PatientLocationTrackerActivity pat) {
		this.pat = pat;
	}

	@Override
	protected String doInBackground(String... params) {
		String inMsg = null;
		try {
			String fromServer = null;
			s = new Socket("192.168.2.5", TCP_SERVER_PORT);

			DataOutputStream st = new DataOutputStream(s.getOutputStream());
			DataInputStream ins = new DataInputStream(s.getInputStream());
			PrintWriter print = new PrintWriter(s.getOutputStream(), true);
			// send output msg
			String outMsg = "127.0.0.1%pingBejaarde%" + params[0] + "%banaan";
			// String outMsg = "q";
			st.writeUTF(outMsg);
			// System.out.println("TcpClient sent: " + outMsg);
			// accept server response

			while ((fromServer = ins.readUTF()) != null) {
				System.out.println("Server : " + fromServer);
				inMsg = fromServer;

				String[] split = fromServer.split("%");
				System.out.println(split[0] + split[1]);
				GeoPoint gp = new GeoPoint(Integer.parseInt(split[0]),
						Integer.parseInt(split[1]));

				pat.createMarker(gp);
				pat.mapController.animateTo(gp);

				s.close();
				this.cancel(true);
			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (inMsg != null) {
			return inMsg;
		}

		return null;
	}

}
