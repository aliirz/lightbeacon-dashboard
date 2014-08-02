package com.example.beacons;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver{


	
	
	TextView myPosition;
	Button findButton;
	EditText BeaconMinor;
	String inputMinor;
	String url = "http://api.net46.net/";
	String data;
	String jsonStr;
	
	
	private static final String TAG_ID = "id";
    private static final String TAG_MINOR = "minor";
    private static final String TAG_INFO = "info";
 
	// contacts JSONArray
    JSONArray beacons = null;
	
	@Override
	public void onReceive(Context context, Intent arg1) {
		// TODO Auto-generated method stub
		
		if(isConnected(context)){
			Toast.makeText(context, "Connected.", Toast.LENGTH_LONG).show();
			
			//Here i will sync it
			
			Log.i("PML", "before sending  http");
			ServiceHandler sh = new ServiceHandler();
			// Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
//            params.add(new BasicNameValuePair("minor", inputMinor));
			// Making a request to url and getting response
            Log.i("PML", "before call");
			jsonStr = sh.makeServiceCall(url, ServiceHandler.POST, params);
			Log.i("PML", "after call");
//			myPosition.setText(jsonStr);
			Log.d("Response: ", "> " + jsonStr);

			if (jsonStr != null) {
				try {
					
					JSONObject jsonObj = new JSONObject(jsonStr);
                     
                    // Getting JSON Array node
                    beacons = jsonObj.getJSONArray("beacons");
                    DbHandler entry = new DbHandler(context);
                    Log.i("PML", "before open");
        			entry.open();
        			Log.i("PML", "after open");
        			for (int i = 0; i < 3; i++){
        				JSONObject c = beacons.getJSONObject(i);
                        
                        String id = c.getString(TAG_ID);
                        String minor = c.getString(TAG_MINOR);
                        String info = c.getString(TAG_INFO);
                        
        				entry.createEntry(minor, info);
        				Log.i("PML", "after entry");
        				data += "" + id + ") " + "Minor: " + minor + " & info: " + info + "\n";
                        
        			}
        			entry.close();
        			 Log.i("PML", "after close");
            		
                        
                        
				} catch (JSONException e) {
					e.printStackTrace();
					data = e.toString();
					Toast.makeText(context, "Lost connect.", Toast.LENGTH_LONG).show();
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}
			
		} else {
			Toast.makeText(context, "Lost connect.", Toast.LENGTH_LONG).show();
		}
		
	}
	
	public boolean isConnected(Context context){
		boolean isInternetAvail = false;
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		isInternetAvail = activeNetwork != null && activeNetwork.isConnected();
		return isInternetAvail;
	}
	

}
