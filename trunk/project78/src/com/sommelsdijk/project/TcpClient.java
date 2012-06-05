package com.sommelsdijk.project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;
import android.util.Log;

public class TcpClient extends AsyncTask<String, String, String> {

	private static final int TCP_SERVER_PORT = 21100;

	@Override
	protected String doInBackground(String... params) {
		String inMsg = null;
		try {
			Socket s = new Socket("schriek.dscloud.me", TCP_SERVER_PORT);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					s.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					s.getOutputStream()));
			// send output msg
			String outMsg = "127.0.0.1%pingBejaarde%" + params[0] 
					+ System.getProperty("line.separator");
			out.write(outMsg);
			out.flush();
			Log.i("TcpClient", "sent: " + outMsg);
			// accept server response
			inMsg = in.readLine() + System.getProperty("line.separator");
			Log.i("TcpClient", "received: " + inMsg);
			// close connection
			s.close();
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
