package com.test;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivity extends ListActivity implements OnClickListener 
{
	public static String path;
	static TextView tv;
	ArrayList<Packet> Packets= new ArrayList<Packet>();
	ArrayList<String> headers = new ArrayList<String>();
	String[] Packets_string = new String[] { "Item 1 ","Item 2" };
	ArrayAdapter adapter ;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        path=Helper.getSniffBinaryPath(getApplicationContext());
        Helper.makeInternalCopy(getApplicationContext(),path, R.raw.sniff);
        
        Button button=(Button)findViewById(R.id.button);
        button.setOnClickListener(this);
        
        adapter=new ArrayAdapter<Packet>(this, R.layout.list_item, Packets);
        
        setListAdapter(adapter);
        ListView lv = getListView();
        
        
        lv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) {
              // When clicked, show a toast with the TextView text
            	
            	String Body="Protocol: "+Packets.get(position).protocol;
            	if(Packets.get(position).payload!=null){
            		Body=Body+"\n"+"Payload:" +Packets.get(position).payload+"\n";
            	}
            	  
            	 AlertDialog.Builder asd= new AlertDialog.Builder(MainActivity.this);  
                 asd.setMessage(Body)  
                .setTitle("Details")  
                 .setCancelable(true)  
                 .show();    
                }
            });
          
        
        IntentFilter filter = new IntentFilter(BroadcastPackets.Packet);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        BroadcastPackets BP = new BroadcastPackets();
        registerReceiver(BP, filter);
        
    }

	
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		case R.id.button:
			Button button= (Button)findViewById(R.id.button);
			if(button.getText().equals("Start")){
				button.setText("Stop");
				System.out.println("Starting Sniffing\n");
//			String command="su -c \"/data/local/sniff\"" ;
//			new Background().execute();
				System.out.println("Start Button Clicked");
				Intent SniffingIntent = new Intent(this, SniffingService.class);
				SniffingIntent.putExtra("variable", "value");
				startService(SniffingIntent);
			}
			else if(button.getText().equals("Stop")){
				button.setText("Start");
				Intent SniffingIntent = new Intent(this, SniffingService.class);
				System.out.println("Stoping Service");
				stopService(SniffingIntent);
				System.out.println("Service Stopped!");
			}
			break;
		}
		
	}
	
	public class BroadcastPackets extends BroadcastReceiver{
		
		public static final String Packet="com.test.intent.action.MESSAGE_PROCESSED";
		
		@Override
		public void onReceive(Context context, Intent intent) {
			System.out.println("Broadcast Received");
			// TODO Auto-generated method stub
			Packet packet=new Packet();
			if(packet!=null){
			System.out.println("Received: "+ packet);
			//tv=(TextView)findViewById(R.id.textview);
			//tv.append(text);
			packet.header = intent.getStringExtra("header");
			packet.protocol = intent.getStringExtra("protocol");
			packet.payload = intent.getStringExtra("payload");
	//		packet.header=intent.getStringExtra("line");
			Packets.add(packet);
			adapter.notifyDataSetChanged();
			}
			else{
				System.out.println("Null Received!! Dropping..");
			}
			
		}
		
	}
	public void onDestroy() {
        super.onDestroy();

        /*
         * Kill application when the root activity is killed.
         */
        this.finish();
    }
	
	

}