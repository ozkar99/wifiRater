package uanl.se2.wifirate;

import org.json.JSONObject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLite extends SQLiteOpenHelper {

	SQLiteDatabase db;
	
	public SQLite(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
			
	@Override
    public void onCreate(SQLiteDatabase db) {    
        /*Create the tables*/
		db.execSQL("CREATE TABLE Ratings (rating INTEGER, bssid TEXT)");       
        
		JSONParser jp = new JSONParser ();
        JSONObject jObj = jp.getRating();
		
        /*TODO: parse el jObj y por cada chingadera del array empezar rellenar nuestro SQLite local.
         * db.execSQL("INSERT INTO Ratings (rating, bssid) VALUES (5, '00:23:69:91:fb:35')");        
         * 
         */
       
    }
		
	@Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {       
        /*Re write the table, dem extra points*/
		db.execSQL("DROP TABLE IF EXISTS Ratings");		
    }
	
	
}
