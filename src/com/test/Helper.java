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
        	
        	Toast.makeText(c, "Copying file",Toast.LENGTH_SHORT).show();
            byte[] bytes = new byte[is.available()];
            DataInputStream dis = new DataInputStream(is);
            dis.readFully(bytes);   
/*            FileOutputStream out ;
            File sniff =new File(getSniffBinaryPath(c));
            out = c.openFileOutput("sniff", Context.MODE_PRIVATE);
            byte[] buffersniff= new byte[64];
            while (dis.read(buffersniff) > -1) {
                    out.write(buffersniff);
            }
            out.flush();
            out.close();*/
            FileOutputStream setdbOutStream = new FileOutputStream(path);
            setdbOutStream.write(bytes);
            setdbOutStream.close();

            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("chmod 777 " + path+ "\n");
            os.writeBytes("exit\n");
            os.flush();
        }
        catch(Exception e){
        	Toast.makeText(c, "Error Copying file: " + e.toString(),Toast.LENGTH_SHORT).show();
        	e.printStackTrace();
        }
		
	}
	
	public static String getSniffBinaryPath(Context c) {
        return c.getFilesDir().getAbsolutePath() + File.separator + "sniff";
}

}
