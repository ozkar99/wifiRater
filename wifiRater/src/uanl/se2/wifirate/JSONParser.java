package uanl.se2.wifirate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.CountDownLatch;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Base64;




public class JSONParser {

	private static String result = "";

	
    private final String baseUrl = "http://se2.ozkar.org/";   	
    private final String user = "furby";
   	private final String passwd = "furby";
   		
   	
   	
    // constructor
    public JSONParser() {}
    
    
    /*Update rating of my bssid */
    public void setRating(String bssid, String rating) {
    	   GET(baseUrl + "Ratings/" + bssid + "/" + rating);
    }
    
	/*get the rating based on bssid*/
	public JSONObject getRating(String bssid) {		
		return string2JSON( GET(baseUrl + "Ratings/" + bssid) );
	}

	/*get all the ratings */ 
	public JSONObject getRating() {		 				
		return string2JSON( GET(baseUrl + "Ratings/") );
	}
	 
	/* get hash of user */
	public String getUserHash(String s) {		
		return  GET(baseUrl + "Users/" + s);
	}
	
 	private String GET(final String url){
        
		result = ""; //clean result of the GET
        final CountDownLatch latch = new CountDownLatch(1); //esperar el thread.
        
		new Thread(new Runnable() {
            public void run() {
              
            	try {
            		 
	                    // create HttpClient
	                    HttpClient httpclient = new DefaultHttpClient();

	                    // add Credentials
	                    String authorizationString = "Basic " + Base64.encodeToString( (user + ":" + passwd).getBytes(), Base64.NO_WRAP); 
	                    
	                    HttpGet getsie = new HttpGet(url);
	                    getsie.addHeader("Authorization", authorizationString);
	                    
	                    // make GET request to the given URL
	                    HttpResponse httpResponse = httpclient.execute(getsie);
	         
	                    // receive response as inputStream
	                    InputStream inputStream = httpResponse.getEntity().getContent();
	         
	                    // convert inputstream to string
	                    if(inputStream != null) {
	                        result = convertInputStreamToString(inputStream);	                        	                        
	                    }
	                    
	                	} catch (Exception e) {
                			e.printStackTrace();
                	}
            		
            		latch.countDown();//le decimos a latch que ya termino el thread.
                }
            	
                
              }).start(); //corre el thread my negro.          
        
		
		try {
			latch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        String res = result;                
        return res;
    }
    	
    
    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String res = "";
        while((line = bufferedReader.readLine()) != null)
            res += line;
 
        inputStream.close();
        return res;
 
    }
    
    private static JSONObject string2JSON(String s) {
		JSONObject jo = null;
		
		try {
			jo = new JSONObject(s);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jo;
	}


}
    


   