package il.ac.shenkar.pythia.classes;

import android.content.Intent;

public class OnGoingNotification {
	private static final int unigueID = 10248534;
	private static OnGoingNotification instance = null;
	
	private OnGoingNotification(Intent intent) {

	}
	
	public static OnGoingNotification getInstance(Intent intent) {
		if(instance == null) {
			return instance = new OnGoingNotification(intent);
		}
		return instance;
		
	}
	
}
