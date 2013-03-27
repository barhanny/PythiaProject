package il.ac.shenkar.pythia.Observers;

import il.ac.shenkar.pythia.classes.EventIntent;

public abstract interface ISensorObserver {
	
	public EventIntent buildEventIntent();
	public void report();
	public void stop();
	
}
