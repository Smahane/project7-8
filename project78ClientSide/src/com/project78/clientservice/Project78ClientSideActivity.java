package com.project78.clientservice;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;



public class Project78ClientSideActivity extends Activity {
	
	static TextView tv;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        tv = (TextView)findViewById(R.id.tv1);
        
        startService(new Intent(Project78ClientSideActivity.this, backgroundService.class));

        
    }
    

    
}