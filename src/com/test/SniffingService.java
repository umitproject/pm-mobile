package com.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

import com.test.MainActivity.BroadcastPackets;

public class SniffingService extends IntentService{
	Process root;
	public static String packet= "packet";
	Parser parser;
	public SniffingService() {
		super("SniffingService");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
		// TODO Auto-generated method stub
		System.out.println("SniffingService called");
	//	 Toast.makeText(getApplicationContext(), "SniffingService called",Toast.LENGTH_SHORT).show();
		try {		
			root=Runtime.getRuntime().exec("su -c \""+MainActivity.path+"\"");
		
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(root.getOutputStream()));
        BufferedReader in = new BufferedReader(new InputStreamReader(root.getInputStream()));
        int i=1;
        String line=null;
        int formed=0;
        
        while ((line = in.readLine()) != null) {
        	System.out.println("line read: "+ line );
        	
        	parser=new Parser(line);
        	if (parser.parsed_packet==null)
        		continue;
        	Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(BroadcastPackets.Packet);
            broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        	broadcastIntent.putExtra("header", parser.header());
        	broadcastIntent.putExtra("protocol", parser.protocol());
            broadcastIntent.putExtra("payload", parser.payload());
    //    	broadcastIntent.putExtra("line", line);
        	
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
		System.out.println("Destoying Process");
		root.destroy();
		System.out.println("Process Destroyed");
	}

}
