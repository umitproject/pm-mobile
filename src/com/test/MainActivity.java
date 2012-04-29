package com.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity implements OnClickListener 
{
	static TextView tv;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button start =(Button)findViewById(R.id.start);
        start.setOnClickListener(this);
        Button stop =(Button)findViewById(R.id.stop);
        stop.setOnClickListener(this);
        tv=(TextView)findViewById(R.id.textview);
        
    }

	
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Background a=new Background();
		switch(arg0.getId()){
		case R.id.start:
			print("Starting Sniffing\n");
			String command="su -c \"/data/local/sniff\"" ;
			new Background().execute();
			
			break;
		case R.id.stop:
			a.cancel(true);
			break;
		}
		
	}
	public static void print(String s){
		tv.append(s);
	}
	
	
}