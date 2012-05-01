package com.test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.content.Context;

public class Helper  {
	
	public static void makeInternalCopy(Context c,String path,int resource){
		InputStream is=c.getResources().openRawResource(resource);
        try {
            byte[] bytes = new byte[is.available()];
            DataInputStream dis = new DataInputStream(is);
            dis.readFully(bytes);   
            FileOutputStream setdbOutStream = new FileOutputStream(path);
            setdbOutStream.write(bytes);
            setdbOutStream.close();

            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("chmod 777 " + path + "\n");
            os.writeBytes("exit\n");
            os.flush();
        }
        catch(Exception e){
        	e.printStackTrace();
        }
		
	}
}
