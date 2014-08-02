package com.example.beacons;



import java.util.Collection;

import com.radiusnetworks.ibeacon.IBeacon;
import com.radiusnetworks.ibeacon.IBeaconConsumer;
import com.radiusnetworks.ibeacon.IBeaconManager;
import com.radiusnetworks.ibeacon.Region;
import com.radiusnetworks.ibeacon.RangeNotifier;
import com.example.beacons.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.widget.TextView;

public class MainActivity extends Activity implements IBeaconConsumer{

	
	private IBeaconManager iBeaconManager = IBeaconManager.getInstanceForApplication(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		iBeaconManager.bind(this);
	}

	 @Override 
	    protected void onDestroy() {
	        super.onDestroy();
	        iBeaconManager.unBind(this);
	    }
	
	@Override 
    protected void onPause() {
    	super.onPause();
    	if (iBeaconManager.isBound(this)) iBeaconManager.setBackgroundMode(this, true);    		
    }
    @Override 
    protected void onResume() {
    	super.onResume();
    	if (iBeaconManager.isBound(this)) iBeaconManager.setBackgroundMode(this, false);    		
    }
	
	
	@Override
    public void onIBeaconServiceConnect() {
        iBeaconManager.setRangeNotifier(new RangeNotifier() {
        @Override 
        public void didRangeBeaconsInRegion(Collection<IBeacon> iBeacons, Region region) {
        	
            if (iBeacons.size() > 0) {
            	
            	logToDisplay("" + iBeacons.iterator().next().getProximityUuid(),
            			"" + iBeacons.iterator().next().getMajor(),
            			"" + iBeacons.iterator().next().getMinor(),
            			"" + iBeacons.iterator().next().getAccuracy());
            	
            } else {
            	logToDisplay("finding..", "finding..", "finding..", "finding..");
            }
        }

        });

        try {
            iBeaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {   }
    }
    
	private void logToDisplay(final String data_1, final String data_2, final String data_3, final String data_4) {
    	runOnUiThread(new Runnable() {
    	    public void run() {
    	    	TextView uuid = (TextView)MainActivity.this.findViewById(R.id.uuid);
    	    	TextView major = (TextView)MainActivity.this.findViewById(R.id.major);
    	    	TextView minor = (TextView)MainActivity.this.findViewById(R.id.minor);
    	    	TextView dist = (TextView)MainActivity.this.findViewById(R.id.dist);
    	    	
    	    //	uuid.setText(data_1);
    	    	major.setText(data_2);
    	    	minor.setText(data_3);
    	    	dist.setText(data_4);
    	    	

    			try {
    				
    				DbHandler entry = new DbHandler(MainActivity.this);
    				
    				entry.open();
    				String info = entry.getInfo("000" + data_3);
    				uuid.setText(info);
    				entry.close();
    				if (info == null){
    					uuid.setText("No Familiar Beacon Found");
    				}
    			} catch (Exception e) {
    				
    			} finally {
    				
    			}
    	    	
    	    	
    	    }
    	});
    }
	
}
