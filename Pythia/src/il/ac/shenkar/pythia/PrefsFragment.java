package il.ac.shenkar.pythia;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;

public class PrefsFragment extends PreferenceFragment implements OnPreferenceChangeListener{
	
	SwitchPreference locPref;
	
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	  // TODO Auto-generated method stub
	  super.onCreate(savedInstanceState);
	  
	  // Load the preferences from an XML resource
	        addPreferencesFromResource(R.xml.prefs);
	        
	        locPref = (SwitchPreference) getPreferenceScreen().findPreference("location_preference");
	 }

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if(preference.equals(locPref)) {
		}
		return false;
	}


}
