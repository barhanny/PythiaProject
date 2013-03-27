package il.ac.shenkar.pythia.classes;

import org.json.JSONArray;

import android.content.Intent;

public class SyncIntent extends Intent{
	
	private String device_id;
	private JSONArray events;

	public SyncIntent(JSONArray jsonArr) {
		super();
		this.events = jsonArr;
	}
	
	public SyncIntent() {
		super();
		events = null;
	}

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public JSONArray getEvents() {
		return events;
	}

	public void setEvents(JSONArray events) {
		this.events = events;
	}

	
}
