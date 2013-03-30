package il.ac.shenkar.pythia.classes;

import il.ac.shenkar.pythia.R;
import android.os.Bundle;
import android.preference.PreferenceFragment;

public class ResultPrefFragment extends PreferenceFragment {
	  
	
	        @Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.status_pref);
	   }

}
