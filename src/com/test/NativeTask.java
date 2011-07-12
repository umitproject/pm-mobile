package com.test;

import android.util.Log;

public class NativeTask{
	
	static{
		
		try
		{
			System.loadLibrary("nativetask");
		}
		catch(Exception e)
		{
			Log.e("Native Task","Unable to load Library due to: "+e.toString());
		}
	}
	
	public static native int runCommand(String command);
}