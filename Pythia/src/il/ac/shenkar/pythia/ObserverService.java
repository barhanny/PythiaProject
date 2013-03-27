package il.ac.shenkar.pythia;

import il.ac.shenkar.pythia.Observers.LocationObserver;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;


public class ObserverService extends Service{
	
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
		/*if(newValue != null && preference != null && preference.getKey().equals("location_preference") && newValue.equals(true)) {
			
		}
		if(newValue != null && preference != null && preference.getKey().equals("location_preference") && newValue.equals(false)) {
			locationOb.stop();
		}*/
		
		
		return START_STICKY;
	}
	
	OnSharedPreferenceChangeListener listener  = new OnSharedPreferenceChangeListener() {

		@Override
		public void onSharedPreferenceChanged (SharedPreferences pref, String key) {
			if(pref != null && pref.getBoolean("location_preference", false)) {
				locationOb = new LocationObserver(getApplicationContext());	
			} 
			else if (pref != null && !pref.getBoolean("location_preference", false)) {
				Log.i(TAG, "STOPPP");
				locationOb.stop();
			}
			
		}
		
	};
}
