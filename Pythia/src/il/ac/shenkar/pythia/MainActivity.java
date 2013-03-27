package il.ac.shenkar.pythia;

import java.util.Calendar;

import il.ac.shenkar.pythia.Observers.LocationObserver;
import il.ac.shenkar.pythia.classes.SyncIntent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.provider.Settings.Secure;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.Window;

public class MainActivity extends PreferenceActivity {

	private static final String TAG = "MainActivity";
	
	private SharedPreferences sharedPreferences;
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.onCreate(savedInstanceState);
		 getFragmentManager().beginTransaction().replace(android.R.id.content,
	                new PrefsFragment()).commit();
		 
		 
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		if(sharedPreferences.getBoolean("firstTime", true)) {
			Log.i(TAG, "FIRST TIME!!!");
			sharedPreferences.edit().putBoolean("firstTime", false).commit();
			initReciever();
		}	
				
		startService(new Intent(this,ObserverService.class));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void initReciever() {
		
		sharedPreferences.edit().putBoolean("location_preference", false).commit();
		Intent intent = new Intent(getApplicationContext(),ReporterIntentService.class);
		String android_id = Secure.getString(getBaseContext().getContentResolver(), Secure.ANDROID_ID); 
		intent.putExtra("android_id", android_id);
		intent.putExtra("sync", true);
		PendingIntent pi = PendingIntent.getService(getApplicationContext(), 0, intent, 0);
		//intervalSync.SetAutomaticRecording(getApplicationContext(), pi, 3000);
		
		Calendar calendar = Calendar.getInstance();
		AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE); // Getting the alarm manager

		am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 10000, pi); 
	}
}
