package com.example.droidstarchart;

import android.app.Activity;
import android.os.Bundle;
import android.view.Display;

public class DroidStarChartActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		Display display = getWindowManager().getDefaultDisplay();
		
		/** Draw something */
		drawView DrawView = new drawView(this,display);
		setContentView(DrawView);
	}
}