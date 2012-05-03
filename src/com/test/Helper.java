package com.test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.content.Context;
import android.widget.Toast;

public class Helper  {
	
	public static void makeInternalCopy(Context c,String path,int resource){
		InputStream is=c.getResources().openRawResource(resource);
        try {
        	
        	Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            File sniff=new File(path);
            if(sniff.exists())
            {
            	os.writeBytes("rm " + path+ "\n");
            }
            byte[] bytes = new byte[is.available()];
            
            
            FileOutputStream setdbOutStream = new FileOutputStream(path);
            DataInputStream dis = new DataInputStream(is);
            dis.readFully(bytes);   

            
            setdbOutStream.write(bytes);
            setdbOutStream.close();

            
            os.writeBytes("chmod 777 " + path+ "\n");
            os.writeBytes("exit\n");
            os.flush();
        }
        catch(Exception e){
//        	Toast.makeText(c, "Error Copying file: " + e.toString(),Toast.LENGTH_SHORT).show();
        	e.printStackTrace();
        }
		
	}
	
	public static String getSniffBinaryPath(Context c) {
        return c.getFilesDir().getAbsolutePath() + File.separator + "sniff";
}

}
