package com.test;

import java.util.ArrayList;

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
import android.widget.Toast;


public class MainActivity extends ListActivity implements OnClickListener 
{
	public static String path="/data/local/sniff";
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
              Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
                  Toast.LENGTH_SHORT).show();
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
				Intent SniffingIntent1 = new Intent(this, SniffingService.class);
				System.out.println("Stoping Service");
				stopService(SniffingIntent1);
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
			packet.header = intent.getStringExtra(SniffingService.packet);
			packet.payload="payload";
			Packets.add(packet);
			adapter.notifyDataSetChanged();
			}
			else{
				System.out.println("Null Received!! Dropping..");
			}
			
		}
		
	}
	
	

}