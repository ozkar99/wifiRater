package uanl.se2.wifirate;

import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.net.wifi.ScanResult;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;



public class WifiAdapter extends ArrayAdapter<ScanResult> {
	
	  		  	  	  	  
	  private static final Locale Locale = java.util.Locale.ENGLISH;
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


	@SuppressLint("DefaultLocale")
	@Override
	  public View getView(final int position, View convertView, ViewGroup parent) {
	   
				
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    
	    View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
	   	       	    	    	    	    	  
	    TextView txtEssid = (TextView) rowView.findViewById(R.id.lbEssid);
	    RatingBar rbWifi = (RatingBar) rowView.findViewById(R.id.rbWifi);
	    
	   	    
	    txtEssid.setText(values.get(position).SSID);
	    	    	    
	    
	    /*Set the rating stars*/
	    String bssid = values.get(position).BSSID.toUpperCase(Locale);	  
	    
	    Integer rating=0;
	    
	    /*Get values from internet service my nigga*/
	    JSONParser jp = new JSONParser();
	    JSONObject jo = jp.getRating(bssid);
	    
	    String joRating = null;
	    String joBssid = null;
	    
	    if(jo != null) {
	    
		    try {	    	
		      joRating = jo.getString("rating");
		      joBssid = jo.getString("bssid").toUpperCase();
		    } catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    
	    }
	    /*If everything went well, joRating and joBssid have our Internet values */
	    if ( (joRating != null) && (joBssid != null) ) {
	   
	    	
	    	if (db != null) {
		    	 Cursor c = db.rawQuery("SELECT rating FROM Ratings WHERE bssid='" + bssid + "'", null);		    	 
		    	
		    	 if (c.moveToFirst()) {
		    		 /*update the record*/
		    		 db.execSQL("UPDATE Ratings SET rating=" + joRating + " WHERE bssid='" + joBssid +"'");
		    	 } else {
		    		 /*Add the record*/
		    		 db.execSQL("INSERT INTO Ratings (rating, bssid) VALUES (" + joRating + " , '" + joBssid + "')");
		    	 }
	    	}
	    
	    }
	   
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
	    
	    /* Dem Starz changes*/	    
	    /*Position being 'final' on getView is the key for this to actually work correctly, else position would get overwritten by the last item*/
	    rbWifi.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {		 					
					JSONParser jp = new JSONParser();
					String myBssid = values.get(position).BSSID.toUpperCase();
					String strRating = Float.toString(rating);
					jp.setRating(myBssid, strRating);
				}
			});
	    
	    return rowView;
	  }
		
}
