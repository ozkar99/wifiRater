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
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
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
			Toast.makeText(this, getString(R.string.wifi_on) , Toast.LENGTH_SHORT).show();
			wifiManager.setWifiEnabled(true);
		
			try {
				wait(100000); //wait 10 seconds for wifi to become stable.
				Toast.makeText(this, getString(R.string.scanning) , Toast.LENGTH_SHORT).show();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
						
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

		
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
		/*Fill the list with the scan results*/
		scanResults = getScanResults();
		
		/*If scan results are good procede*/
		if (scanResults != null) {
			fillWifiList(scanResults);
		}
		
	}

}
