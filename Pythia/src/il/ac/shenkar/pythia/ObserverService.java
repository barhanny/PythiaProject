package il.ac.shenkar.pythia;

import il.ac.shenkar.pythia.Observers.LocationObserver;
import android.R.bool;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.IBinder;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.util.Log;


public class ObserverService extends Service {
	
	private static final String TAG ="ObserverService";
	
	LocationObserver locationOb;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//switch case Control which observers are functional..
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(listener);

		if(prefs.getBoolean("location_preference", false)) {
			locationOb = new LocationObserver(getApplicationContext());
		}
		
		
		return START_STICKY;
	}
	
	OnSharedPreferenceChangeListener listener  = new OnSharedPreferenceChangeListener() {

		@Override
		public void onSharedPreferenceChanged (SharedPreferences pref, String key) {
			if(key.contentEquals("location_preference")) {
				Boolean locOn = pref.getBoolean("location_preference", false);
				if(locOn) {
					locationOb = new LocationObserver(getApplicationContext());	
				} 
				if(!locOn) {
					Log.i(TAG, "STOPPP");
					locationOb.stop();
				}
			}
			if(key.contentEquals("interval_pref")) {
				Log.i(TAG, "THIS IS INTERVALL");
			}
			
		}
		
	};
}
