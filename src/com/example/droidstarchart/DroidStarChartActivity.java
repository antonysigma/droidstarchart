package com.example.droidstarchart;

import java.io.IOException;

import android.app.Activity;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;

public class DroidStarChartActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/** Copy database to device */
		DatabaseHelper myDbHelper = new DatabaseHelper(this);

		try {
			myDbHelper.createDataBase();
		} catch (IOException ioe) {
			throw new Error("Unable to create database");
		}

		try {
			myDbHelper.openDataBase();
		} catch (SQLException sqle) {
			throw sqle;
		}
		
		/** Try to read something */
		Cursor result = myDbHelper.getNumberOfStars(4.5); 
		
		int number;
		result.moveToFirst(); 
	    //while (!result.isAfterLast()) { 
	        number = result.getInt(0); 
	        // do something useful with these 
	       // result.moveToNext(); 
	      //} 
	      //result.close();

		/** Draw something */
		drawView DrawView = new drawView(this);
		DrawView.setBackgroundColor(Color.WHITE);
		setContentView(DrawView);
	}
}