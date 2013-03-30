package il.ac.shenkar.pythia;

import java.util.Calendar;

import il.ac.shenkar.pythia.Observers.LocationObserver;
import il.ac.shenkar.pythia.classes.SyncIntent;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.provider.Settings.Secure;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.widget.RemoteViews;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class MainActivity extends Activity {

	private static final String TAG = "MainActvity";
	private static final int onGoingUnigueID = 12345;
	private static final int notificationUnigueID = 13245;
	
	private SharedPreferences sharedPreferences;
	private NotificationManager nm;
	private AlarmManager am;
	private String syncInterval = "5000";
	Calendar calendar; 
	PendingIntent pi;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.onCreate(savedInstanceState);
		 getFragmentManager().beginTransaction().replace(android.R.id.content,
	                new PrefsFragment()).commit();
		
		onGoingNotification();
		
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		sharedPreferences.registerOnSharedPreferenceChangeListener(new OnSharedPreferenceChangeListener() {
			
			@Override
			public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
					String key) {
				if(key.contentEquals("interval_pref")) {
					syncInterval = sharedPreferences.getString(key,"5000");
					Log.i(TAG, syncInterval);
					am.cancel(pi);
					am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), Integer.parseInt(syncInterval), pi); 
				}
				
			}
		});
		if(sharedPreferences.getBoolean("firstTime", true)) {
			Log.i(TAG, "FIRST TIME!!!");
			sharedPreferences.edit().putBoolean("firstTime", false).commit();
			initialize();
			onGoingNotification();
		}	
				
		startService(new Intent(this,ObserverService.class));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}
	
	private void initialize() {
		
		
		Intent intent = new Intent(getBaseContext(),ReporterIntentService.class);
		String android_id = Secure.getString(getBaseContext().getContentResolver(), Secure.ANDROID_ID); 
		intent.putExtra("android_id", android_id);
		intent.putExtra("sync", true);
		pi = PendingIntent.getService(getBaseContext(), 0, intent, 0);
		
		calendar = Calendar.getInstance();
		am = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE); // Getting the alarm manager

		am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), Integer.parseInt(syncInterval), pi); 
		
	}
	
	@SuppressLint("NewApi")
	private void onGoingNotification() {
		
		
		Intent intent = new Intent(this,MainActivity.class);
		//PendingIntent pi = PendingIntent.getService(getBaseContext(), 0, intent, 0);
		String body = "This is the body of notificstion";
		String title = "Updating Status ";
		
		NotificationCompat.Builder n =  new NotificationCompat.Builder(getBaseContext());
		n.setSmallIcon(R.drawable.status_icon);
		n.setContentText(body);
		n.setContentTitle(title);
		
		n.setOngoing(isChangingConfigurations());
		
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(this, ResultActivity.class);

		// The stack builder object will contain an artificial back stack for the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(ResultActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent( 0, PendingIntent.FLAG_UPDATE_CURRENT );
		n.setContentIntent(resultPendingIntent);
		
		nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		
		Notification noti = n.build();
		noti.flags = Notification.FLAG_ONGOING_EVENT;
		nm.notify(onGoingUnigueID, noti);
		
		//Notification n = new Notification(R.drawable.status_icon, body, System.currentTimeMillis());
	//n.setLatestEventInfo(this, title, body, pi);
	//	n.flags = Notification.FLAG_ONGOING_EVENT;
		
		//n.defaults = Notification.DEFAULT_VIBRATE;
		
	}
}
