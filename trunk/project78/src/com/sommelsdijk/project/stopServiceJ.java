package com.sommelsdijk.project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class stopServiceJ extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		stopService(new Intent(this, positionReceiver.class));

		finish();

	}
}
