package il.ac.shenkar.pythia;

import il.ac.shenkar.pythia.classes.EventIntent;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHandler extends SQLiteOpenHelper{
	private static final String TAG = "DataBaseHandler";
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;
 
    // Database Name
    private static final String DATABASE_NAME = "eventsManager";
 
    // Events table name
    private static final String TABLE_EVENTS = "events";
 
    // Events Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_DATA = "event";
 
    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EVENTS_TABLE = "CREATE TABLE " + TABLE_EVENTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DATA + " TEXT"+")";
        db.execSQL(CREATE_EVENTS_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
 
        // Create tables again
        onCreate(db);
    }
    
    
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
 
    // Adding new event
    void addEvent(EventIntent event) {
    	Log.i(TAG, event.toString());
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_DATA, event.getJson().toString()); // Event data
 
        // Inserting Row
        db.insert(TABLE_EVENTS, null, values);
        db.close(); // Closing database connection
    }
 
    // Getting single event
    EventIntent getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_EVENTS, new String[] { KEY_ID,
                KEY_DATA }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
 
        EventIntent event = null;
		try {
			event = new EventIntent(Integer.parseInt(cursor.getString(0)),
			        new JSONObject(cursor.getString(1)));
			
			return event;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // return event, could e null.
		return event;
        
    }
 
    // Getting All Events
    public List<EventIntent> getAllEvents() {
        List<EventIntent> eventsList = new ArrayList<EventIntent>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_EVENTS;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
        	try {	
        		do {
        			EventIntent event = new EventIntent();
        			event.setId(Integer.parseInt(cursor.getString(0)));
                
					event.setJson(new JSONObject(cursor.getString(1)));

					// Adding event to list
					eventsList.add(event);
        		} while (cursor.moveToNext());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
 
        // return event list
        return eventsList;
    }
 
    // Updating single event
    public int updateEvent(EventIntent event) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_DATA, event.getJson().toString());
 
        // updating row
        return db.update(TABLE_EVENTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(event.getId()) });
    }
 
    // Deleting single event
    public void deleteEvent(EventIntent event) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EVENTS, KEY_ID + " = ?",
                new String[] { String.valueOf(event.getId()) });
        db.close();
    }
 
    // Getting events Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_EVENTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
 
        // return count
        return cursor.getCount();
    }
 

}
