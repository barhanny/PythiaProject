package il.ac.shenkar.pythia.classes;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;


public class EventIntent implements Parcelable {
	private int id;
	private String json;

	public EventIntent() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public EventIntent(JSONObject json) {
		super();
		this.json = json.toString();
	}

	public EventIntent(int id, JSONObject json) {
		super();
		this.id = id;
		this.json = json.toString();
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getJson() {
		return json;
	}
	
	public void setJson(JSONObject json) {
		this.json = json.toString();
	}
	
    private EventIntent(Parcel in) {
    	id = in.readInt();
        json = in.readString();
    }
    
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
    	out.writeInt(id);
        out.writeString(json);
    }
    
    public void readFromParcel(Parcel in) {
    	id = in.readInt();
    	json = in.readString();
    }

    public static final Parcelable.Creator<EventIntent> CREATOR = new Parcelable.Creator<EventIntent>() {
    	
        public EventIntent createFromParcel(Parcel in) {
            return new EventIntent(in);
        }

        public EventIntent[] newArray(int size) {
            return new EventIntent[size];
        }
    };
	
}
