package il.ac.shenkar.pythia.Observers;

import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import il.ac.shenkar.pythia.LocationEvent;
import il.ac.shenkar.pythia.ReporterIntentService;
import il.ac.shenkar.pythia.classes.EventIntent;

public class LocationObserver implements ISensorObserver{
	private static final String TAG ="LocationObserver";
	
	private LocationEvent event;
	private LocationManager locationManager;
	private Context context;
	private Location loc;

	public LocationObserver(Context context) {
		this.context = context;
		
		Log.i(TAG, "LocationObserver.. C'tor");
		
		locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		
		// Register the listener with the Location Manager to receive location updates
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
	}
	
	@Override
	public void report() {
		Log.i(TAG, "LocationObserver.. report was called and reporting to reprter service");
		Intent intent = new Intent(context, ReporterIntentService.class);
		Bundle bundle = new Bundle();
		bundle.putParcelable("eventIntent", event);

		intent.putExtra("event", true);
		intent.putExtra("bundle", bundle);
		context.startService(intent);
	}

	@Override
	public EventIntent buildEventIntent() {
		Log.i("Status", "BuildEventIntent was called and building the json object");
		
		JSONObject json = new JSONObject();
		try {
			json.put("timestamp", loc.getTime());
			json.put("type", "LOCATION");
			json.put("lat", loc.getLatitude());
			json.put("long", loc.getLongitude());
			json.put("accuracy", loc.getAccuracy());
			json.put("provider", loc.getProvider());
			json.put("speed", loc.getSpeed());
			
			Toast.makeText(context, json.toString(), Toast.LENGTH_LONG).show();
			event = new LocationEvent();
			event.setJson(json);
			report();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return event;
	}	

	// Define a listener that responds to location updatess
	LocationListener locationListener = new LocationListener() {
		
	    public void onLocationChanged(Location location) {
	    	
	    	Log.i(TAG, "onLocationChanged..");
	    	loc = null;
	    	loc = new Location(location);
	    	//Toast.makeText(context, "**************"+ location.getLatitude() +" "+ location.getLongitude(), Toast.LENGTH_SHORT).show();
	    	Toast.makeText(context, loc.getLatitude() +" "+ loc.getLongitude(), Toast.LENGTH_SHORT).show();
	    	buildEventIntent();
	    }

	    public void onStatusChanged(String provider, int status, Bundle extras) {}

	    public void onProviderEnabled(String provider) {}

	    public void onProviderDisabled(String provider) {}
	  };

	@Override
	public void stop() {
		locationManager.removeUpdates(locationListener);
		
	}


}
