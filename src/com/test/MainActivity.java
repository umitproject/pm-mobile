
package com.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements OnClickListener 
{
	Process root;
	
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
    }

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()){
		
		case R.id.start:
	        try {
	        	TextView output=(TextView)findViewById(R.id.textview);
	        	String command="su -c \"/data/local/sniff\"" ;
				root=Runtime.getRuntime().exec("su -c \"/data/local/sniff\"");
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(root.getOutputStream()));
		        BufferedReader in = new BufferedReader(new InputStreamReader(root.getInputStream()));
		        output.append("Command Running : " +command+"\n");
		        String line = null;
		        while ((line = in.readLine()) != null) {  
		            output.append(line+"\n");
		            System.out.println(line+"\n");
		        }
		        
//		        int a=NativeTask.runCommand("su -c \"/data/local/native\"");
		//        output.append(Integer.toString(a));
			    //root.waitFor();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Toast.makeText(this, "Unable to run command \"su\" : "+e.toString() , Toast.LENGTH_SHORT).show();
			}
		case R.id.stop:
			root.destroy();
		}
	}
}
