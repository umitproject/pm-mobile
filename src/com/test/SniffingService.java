package com.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.IntentService;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import com.test.MainActivity.BroadcastPackets;

public class SniffingService extends IntentService{
	Process root;
	public static String packet= "packet";
	public SniffingService() {
		super("SniffingService");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
		// TODO Auto-generated method stub
		System.out.println("SniffingService called");
		 Toast.makeText(getApplicationContext(), "SniffingService called",Toast.LENGTH_SHORT).show();
		try {		
			root=Runtime.getRuntime().exec("su -c \"/data/local/sniff\"");
		
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(root.getOutputStream()));
        BufferedReader in = new BufferedReader(new InputStreamReader(root.getInputStream()));
        int i=1;
        String line=null;
        
        
        while ((line = in.readLine()) != null) {
        	System.out.println("line read: "+ line );
        	Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(BroadcastPackets.Packet);
            broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
            
            broadcastIntent.putExtra("packet", line+"\n");
            System.out.println("Sending Broadcast..");
        	sendBroadcast(broadcastIntent);	
        	System.out.println("Broadcast Send..");
        }
        
        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("SniffingService not running because: " + e.toString());
		}
	}
	public void onDestroy(){
		
		root.destroy();
	
	
	}

}
