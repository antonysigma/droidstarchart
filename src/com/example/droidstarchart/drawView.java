package com.example.droidstarchart;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.Display;
import android.view.View;

public class drawView extends View {
	Paint paint = new Paint();
	/** Copy database to device */
	DataBaseHelper myDbHelper = null;
	
	private static final String TAG = "drawView";

	private float screenWidth;
	private float screenHeight;

	
	public drawView(Context context,Display display) {
		super(context);
		paint.setColor(Color.BLACK);
		setBackgroundColor(Color.WHITE);
		
		/** Setup screen variables */
		
		screenWidth = display.getWidth();
		screenHeight = display.getHeight();;

		
		/** Setup database */
		myDbHelper = new DataBaseHelper(context);
		myDbHelper.createDataBase();
		myDbHelper.openDataBase();
	}

	@Override
	public void onDraw(Canvas canvas) {
		
		/** Try to read something */
		Cursor result = myDbHelper.getStars();

		float ra,de,mag;
		result.moveToFirst();
		 while (!result.isAfterLast()) {
		ra = result.getFloat(0);
		de = result.getFloat(1);
		mag = result.getFloat(2);
		 result.moveToNext();
		 canvas.drawCircle((de+90)*screenWidth/180, ra*screenHeight/24, 7-mag, paint);
		 //Log.i(TAG,de+"");
		 }
		result.close();
	}

}