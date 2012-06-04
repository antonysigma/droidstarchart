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
	private Paint star_paint = new Paint();
	private Paint grid_paint = new Paint();
	private Paint constellation_paint = new Paint();
	private Paint boundary_paint = new Paint();
	
	/** Copy database to device */
	private DataBaseHelper myDbHelper = null;

	/** Debug information */
	private static final String TAG = "drawView";

	/** Screen dimenstion */
	private Point screenSize;

	/** Pan and zoom */
	private Star center;
	private float zoom;

	public drawView(Context context, Display display) {
		super(context);

		/** Setup default color */
		star_paint.setColor(Color.BLACK);
		grid_paint.setColor(Color.GRAY);
		constellation_paint.setColor(Color.BLUE);
		//constellation_paint.setStrokeWidth((float) 2);
		boundary_paint.setColor(Color.GRAY);
		setBackgroundColor(Color.WHITE);

		/** Setup screen variables */

		screenSize = new Point( display.getWidth(), display.getHeight());
		Log.i(TAG, "Screen dimension: " + screenSize.x + ", " + screenSize.y);

		/** Setup default center and zoom level */
		center = new Star((float) 6.752477,(float) -16.7161); //***Testing: position of Sirius
		zoom = screenSize.x * 2;

		/** Setup database */
		myDbHelper = new DataBaseHelper(context);
		myDbHelper.createDataBase();
		myDbHelper.openDataBase();
	}
	
	private void drawGrid(Canvas canvas) {
		
		/** Draw RA grids */
		for(float ra = 0; ra<24; ra++)
		{
			Star start_ra = new Star(ra,-90);
			Star stop_ra = new Star(ra,90);
			
			Point start = start_ra.toRect(center,zoom,screenSize);
			Point stop = stop_ra.toRect(center,zoom,screenSize);
			
			// Draw vertical grid
			canvas.drawLine(start.x,start.y,stop.x,stop.y,grid_paint);
			//Draw RA label
			canvas.drawText(ra+"h",start.x,screenSize.y-100,grid_paint);
		}		
		
		/** Draw DE grids */
		for(float de=-90;de<=90;de+=10)
		{
			Star start_de = new Star(0,de);
			Star stop_de = new Star(24,de);
			
			Point start = start_de.toRect(center,zoom,screenSize);
			Point stop = stop_de.toRect(center,zoom,screenSize);
			
			// Draw horizontal grid
			canvas.drawLine(0,start.y,screenSize.x,stop.y,grid_paint);
			//Draw RA label
			canvas.drawText(de+"deg",0,start.y,grid_paint);
		}
	}

	private void drawBoundary(Canvas canvas){
		Cursor result = myDbHelper.getConstellationBoundary();
		
		for (result.moveToFirst();!result.isAfterLast();result.moveToNext()) {
			Star start = new Star(result.getFloat(1), result.getFloat(2));
			Star stop = new Star(result.getFloat(3), result.getFloat(4));
			
			Point start_screen = start.toRect(center, zoom, screenSize);
			Point stop_screen = stop.toRect(center, zoom, screenSize);

				canvas.drawLine(start_screen.x,start_screen.y,stop_screen.x,stop_screen.y,boundary_paint);
		}
	}
	
	private void drawStar(Canvas canvas) {
		/** Get all stars */
		float mag_limit = 6;
		Cursor result = myDbHelper.getStars(mag_limit);

		for (result.moveToFirst();!result.isAfterLast();result.moveToNext()) {

			Star star = new Star(result.getFloat(0), result.getFloat(1));
			float mag = result.getFloat(2);
			
			Point screen = star.toRect(center, zoom, screenSize);
			
			if(screen.inScreen(screenSize))
				canvas.drawCircle(screen.x, screen.y, (7 - mag), star_paint);

			// Log.i(TAG,de+"");
		}
		result.close();

	}

	private void drawConstellation(Canvas canvas) {
		/** Get all stars */
		float mag_limit = 6;
		Cursor result = myDbHelper.getConstellationLine(mag_limit);

		for (result.moveToFirst();!result.isAfterLast();result.moveToNext()) {

			Star star1 = new Star(result.getFloat(0), result.getFloat(1));
			Star star2 = new Star(result.getFloat(2), result.getFloat(3));
			
			Point screen1 = star1.toRect(center, zoom, screenSize);
			Point screen2 = star2.toRect(center, zoom, screenSize);
			
			if(screen1.inScreen(screenSize) || screen2.inScreen(screenSize))
				canvas.drawLine(screen1.x, screen1.y, screen2.x, screen2.y, constellation_paint);

			// Log.i(TAG,de+"");
		}
		result.close();
	}
	
	private void drawConstellationLabel(Canvas canvas){
		Cursor result = myDbHelper.getConstellationLabel();
		
		for (result.moveToFirst();!result.isAfterLast();result.moveToNext()) {
			String abbr = result.getString(0);
			Star position = new Star(result.getFloat(1),result.getFloat(2));
			
			Point screen = position.toRect(center, zoom, screenSize);
			
			if(screen.inScreen(screenSize))
			canvas.drawText(abbr, screen.x, screen.y, constellation_paint);
		}
	}

	@Override
	public void onDraw(Canvas canvas) {
		drawBoundary(canvas);
		drawConstellation(canvas);
		drawStar(canvas);
		drawConstellationLabel(canvas);
	}
	
	public void setCenter(Star _center){
		center = _center;
	}
	

}