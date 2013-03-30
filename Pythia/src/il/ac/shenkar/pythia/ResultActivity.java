package il.ac.shenkar.pythia;

import il.ac.shenkar.pythia.classes.ResultPrefFragment;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.Window;

public class ResultActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.onCreate(savedInstanceState);
		 getFragmentManager().beginTransaction().replace(android.R.id.content,
	                new ResultPrefFragment()).commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.result, menu);
		return true;
	}

}
