package il.ac.shenkar.pythia;

import il.ac.shenkar.pythia.classes.EventIntent;

import org.json.JSONObject;

import android.os.Parcelable;

public class LocationEvent extends EventIntent implements Parcelable{

	public LocationEvent() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LocationEvent(JSONObject json) {
		super(json);
		// TODO Auto-generated constructor stub
	}

	public LocationEvent(int id, JSONObject json) {
		super(id, json);
		// TODO Auto-generated constructor stub
	}
	
}
