package com.example.droidstarchart;

import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;

public class DroidStarChartActivity extends Activity {
	private String TAG = "DroidStarChartActivity";
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		Display display = getWindowManager().getDefaultDisplay();
		
		/** Draw something */
		drawView DrawView = new drawView(this,display);
		
		/*
		Random rand = new Random();
	    float ra = rand.nextFloat() * 24;
	    float de = rand.nextFloat() * 180 - 90;*/
	    Star center = new Star((float) 6.7525, 0);
	    DrawView.setCenter(center);
		setContentView(DrawView);
	/*	
		while (true) {			
		    try {
				wait(5 * 1000);
			} catch (InterruptedException e) {
				Log.w(TAG,"User interrupt with Timer.",e);
			}
		    
		    Random rand = new Random();
		    float ra = rand.nextFloat() * 24;
		    float de = rand.nextFloat() * 180 - 90;
		    Star center = new Star(ra, de);
		    DrawView.setCenter(center);
		    setContentView(DrawView);
		    
		    }*/
	}
}