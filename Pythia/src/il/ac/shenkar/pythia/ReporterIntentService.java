package il.ac.shenkar.pythia;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import il.ac.shenkar.pythia.classes.EventIntent;
import il.ac.shenkar.pythia.classes.SyncIntent;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class ReporterIntentService extends IntentService {
	private static final String TAG ="ReporterIntentService";
	private DataBaseHandler db;
	
	private SyncIntent syncIntent;
	
	public ReporterIntentService(){
		super("ReporterIntentService");
	}
	
	public ReporterIntentService(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		db = new DataBaseHandler(this);
		
		if(intent.hasExtra("sync")) {
			ConnectToServer(buildSyncIntent(intent));
			
		}
		if(intent.hasExtra("event")) {
			Log.i(TAG, "ReporterIntentService.. onHandleIntent was calles with intent : "+ intent.toString());
	        

	        // Inserting Event
	        Log.d("Insert: ", "Inserting ..");
	        Bundle bn = intent.getExtras().getParcelable("bundle");
	        EventIntent evTest = (EventIntent) bn.get("eventIntent");
	      
	        db.addEvent(evTest);
		}

	
	}

	private SyncIntent buildSyncIntent(Intent intent) {
		syncIntent = new SyncIntent();
		syncIntent.setDevice_id(intent.getStringExtra("android_id"));
		
		JSONArray arr = new JSONArray();
		int i = 0;
		List<EventIntent> events = db.getAllEvents();
        for (EventIntent ev : events) {
            String log =  ev.getJson().toString() ;
            try {
				arr.put(i,ev.getJson());
				++i;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            // Writing Events log
            Log.d(TAG, log);  
            syncIntent.setEvents(arr);
        }
        return syncIntent;
	}

	private void ConnectToServer(SyncIntent syncIntent) {
		HttpPost request;
		JSONObject json = new JSONObject();
		try {
			json.put("device_id", syncIntent.getDevice_id());
			json.put("events", syncIntent.getEvents());
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

		try {
			String url = new String("http://pythia-dev.herokuapp.com/1/event");
			request = new HttpPost(url);
			
			StringEntity entity = new StringEntity(json.toString());
			entity.setContentType("application/json");
			request.setEntity(entity);
			
			// Send request to WCF service
			HttpClient httpClient = new DefaultHttpClient();
			
			HttpResponse response = httpClient.execute(request);
			Log.i(TAG, response.toString());
			
			HttpEntity resEntity = response.getEntity();
			if(resEntity!= null) {
				Log.i(TAG,resEntity.toString());
			}
		
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
