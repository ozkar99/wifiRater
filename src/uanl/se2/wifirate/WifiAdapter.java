package uanl.se2.wifirate;

import java.util.List;
import android.net.wifi.ScanResult;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;



public class WifiAdapter extends ArrayAdapter<ScanResult> {
	
	  
	
	  private final SQLiteDatabase db;	
	  private final Context context;
	  private final List<ScanResult> values;

	  public WifiAdapter(Context context, List<ScanResult> essids) {
		super(context, R.layout.rowlayout, essids);
	    this.context = context;
	    this.values = essids;
	    
	    /*Conexion con sqlite*/
	    SQLite dbR = new SQLite (context, "DBRatings", null, 1);
	    this.db = dbR.getWritableDatabase();
	  }


	@Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	   
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    
	    View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
	   	       	    	    	    	    	  
	    TextView txtEssid = (TextView) rowView.findViewById(R.id.lbEssid);
	    RatingBar rbWifi = (RatingBar) rowView.findViewById(R.id.rbWifi);
	    
	   
	    
	    txtEssid.setText(values.get(position).SSID);
	    
	    
	    /*Set the rating stars*/
	    String bssid = values.get(position).BSSID;
	    Integer rating=0;
	    
	    /*Make sure db and cursor are not null*/
	    if(db != null) {
	    	 Cursor c = db.rawQuery("SELECT rating FROM Ratings WHERE bssid='" + bssid + "'", null);
	    	 /*moveToFirst returns false if cursor has no results*/
	    	 if ( c.moveToFirst() ){	    		 	    	
	    		 rating = Integer.parseInt(c.getString(0));
	    	 } else {
	    		 rating = 0;
	    	 }	    	 	    	
	    }
	    rbWifi.setRating(rating);    	    
	    return rowView;
	  }
}
