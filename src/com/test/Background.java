package com.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.content.Intent;
import android.os.AsyncTask;

public class Background extends AsyncTask<String, String, Long> {
	Process root;
	boolean run=false;
	protected Long doInBackground(String... command) {
//		MainActivity.print("Background Process Started\n");
		 try {
			 run=true;
//			 MainActivity.print("Trying to run command\n");
	        	
				root=Runtime.getRuntime().exec("su -c \"/data/local/sniff\"");
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(root.getOutputStream()));
		        BufferedReader in = new BufferedReader(new InputStreamReader(root.getInputStream()));
	//	        output.append("Command Running : " +command+"\n");
		        
		        int i=1;
		        String line=null;
		        Intent broadcastIntent = new Intent();
		        while ((line = in.readLine()) != null) {  
		        	 publishProgress(line);
//		        	 System.out.println(line+"\n");
		        }
		        
		        
//		        int a=NativeTask.runCommand("su -c \"/data/local/native\"");
		//        output.append(Integer.toString(a));
			    //root.waitFor();
	        
		    
			} catch (Exception e) {
				// TODO Auto-generated catch block
//				MainActivity.print("Unable to run the exec because : "+e.toString()+"\n");
				e.printStackTrace();
//				System.out.println(e.toString());
			}
	return null;
    }

    protected void onProgressUpdate(String ... progress) {
//       MainActivity.print(progress[0]+"\n");
    }

    protected void onPostExecute(Long result) {

//	       MainActivity.print("scanned\n");
    }
    
    protected void onCancelled(){
    	if(run==false)
    		return;	
    	run=false;
    	root.destroy();	
    	
    }

}
