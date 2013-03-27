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
import org.json.JSONObject;

import il.ac.shenkar.pythia.classes.EventIntent;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class ReporterIntentService extends IntentService {
	private static final String TAG ="ReporterIntentService";
	
	public ReporterIntentService(){
		super("ReporterIntentService");
	}
	
	public ReporterIntentService(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		if(intent.hasExtra("sync")) {
			ConnectToServer(intent);
		}
		if(intent.hasExtra("event")) {
			Log.i(TAG, "ReporterIntentService.. onHandleIntent was calles with intent : "+ intent.toString());
	        DataBaseHandler db = new DataBaseHandler(this);

	        // Inserting Event
	        Log.d("Insert: ", "Inserting ..");
	        Bundle bn = intent.getExtras().getParcelable("bundle");
	        EventIntent evTest = (EventIntent) bn.get("eventIntent");
	      
	        db.addEvent(evTest);
	 /*
	        // Reading all events
	        Log.d("Reading: ", "Reading all events..");
	        List<EventIntent> events = db.getAllEvents();       
	 
	        for (EventIntent ev : events) {
	            String log = "Id: "+ev.getId()+" ,Data: " + ev.getJson().toString() ;
	            // Writing Events log
	            Log.d(TAG, log);
	            Toast.makeText(getApplicationContext(), "onHandleIntent "+ log, Toast.LENGTH_LONG).show();
	        }
	        */
		}

	
	}

	private void ConnectToServer(Intent intent) {
		HttpPost request;
		JSONObject json = new JSONObject();

		try {
			String url = new String("http://pythia-dev.herokuapp.com/1/event");
			request = new HttpPost(url);
			
			StringEntity entity = new StringEntity(json.toString());
			entity.setContentType("application/json;charset=utf-8");
			request.setEntity(entity);
			
			// Send request to WCF service
			HttpClient httpClient = new DefaultHttpClient();
			
			HttpResponse response = httpClient.execute(request);
			System.out.println(response.toString());
			
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
