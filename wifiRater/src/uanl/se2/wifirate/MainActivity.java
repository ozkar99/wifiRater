package uanl.se2.wifirate;


/*Wifi Imports*/
import android.net.wifi.WifiManager;
import android.net.wifi.ScanResult;  

/*Toast Import*/
import android.widget.Toast;


/*Widgets Import*/
import android.widget.ListView;

/*Regular Imports*/
import android.os.Bundle;
import android.os.SystemClock;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

 


public class MainActivity extends Activity {

	/*Global Variables*/
	WifiManager wifiManager;
	ListView lvWifis;	
	List<ScanResult> scanResults;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	
		
		/*Get the shit we need into usable variables*/
		wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);				             
        lvWifis = (ListView)findViewById(R.id.lvWifis);  
        
	}
		
	@Override
	protected void onResume() {
		
		/*Enable Wifi if its not ON*/				
		if(!wifiManager.isWifiEnabled()) {					
			Toast.makeText(this, getString(R.string.wifi_on) , Toast.LENGTH_LONG).show();
			wifiManager.setWifiEnabled(true);
			SystemClock.sleep(5000); //wait 5 seconds for wifi to become stable.			
	    } 
						
		reScan();											
		super.onResume();
	}
		
	@Override
 	protected void onPause() {

		// TODO Auto-generated method stub
		super.onPause();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.action_settings:
	    	reScan();
	        return true;	    
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}

	
	/*Custom Functions*/
	protected List<ScanResult> getScanResults() {		
			wifiManager.startScan();	
			return wifiManager.getScanResults();
	}

	protected void fillWifiList(List<ScanResult> list) {
				
		final WifiAdapter adapter = new WifiAdapter(this, list);		
		lvWifis.setAdapter(adapter);
	}
	
	protected void reScan(){
		Toast.makeText(this, getString(R.string.scanning_start) , Toast.LENGTH_SHORT).show();
		/*Fill the list with the scan results*/
		scanResults = getScanResults();
		
		/*If scan results are good proceed*/
		if (scanResults != null) {
			fillWifiList(scanResults);
		}
		Toast.makeText(this, getString(R.string.scanning_finished) , Toast.LENGTH_SHORT).show();
	}

	
}
