package com.example.droidstarchart;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.MotionEvent;

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
		constellation_paint.setStrokeWidth((float) 2);
		boundary_paint.setColor(Color.GREEN);
		setBackgroundColor(Color.WHITE);

		/** Setup screen variables */

		screenSize = new Point( display.getWidth(), display.getHeight());
		Log.i(TAG, "Screen dimension: " + screenSize.x + ", " + screenSize.y);

		/** Setup default center and zoom level */
		center = new Star(6.752477f,-16.7161f); //***Testing: position of Sirius
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
			canvas.drawText(ra+"h",start.x,screenSize.y-50,grid_paint);
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
			canvas.drawText(de+"deg",5,start.y,grid_paint);
		}
	}

	private boolean checkOutofboundary(Point start, Point stop, Point screenSize){
		float left = Math.min(start.x, stop.x);
		float right = Math.max(start.x, stop.x);
		if (left < 0 && right > screenSize.x)
			return true;

		float top = Math.min(start.y, stop.y);
		float bottom = Math.max(start.y, stop.y);
		if (top < 0 && bottom > screenSize.x)
			return true;
		return false;
	}

	private void drawBoundary(Canvas canvas){
		Cursor result = myDbHelper.getConstellationBoundary();
		
		for (result.moveToFirst();!result.isAfterLast();result.moveToNext()) {
			Star start = new Star(result.getFloat(1), result.getFloat(2));
			Star stop = new Star(result.getFloat(3), result.getFloat(4));
			
			Point start_screen = start.toRect(center, zoom, screenSize);
			Point stop_screen = stop.toRect(center, zoom, screenSize);

			if (checkOutofboundary(start_screen, stop_screen, screenSize))
				continue;

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

			if (checkOutofboundary(screen1, screen2, screenSize))
				continue;

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
		drawGrid(canvas);
		drawConstellation(canvas);
		drawStar(canvas);
		drawConstellationLabel(canvas);
	}
	
	public void setCenter(Star _center){
        center = _center;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
        float zone_x = event.getX() / screenSize.x;
        float zone_y = event.getY() / screenSize.y;

        // Left
        if (zone_x > 0.67 &&
                zone_y>=0.33 && zone_y<=0.67)
            center.ra += 0.5f;
        // Right
        else if (zone_x < 0.33 &&
                zone_y>=0.33 && zone_y<=0.67)
            center.ra -= 0.5f;
        // Top
        else if (zone_y < 0.33 &&
                zone_x>=0.33 && zone_x<=0.67)
            center.de += 5.f;
        // Bottom
        else if (zone_y > 0.67 &&
                zone_x>=0.33 && zone_x<=0.67)
            center.de -= 5.f;
        // Bottom left
        else if (zone_x < 0.33 && zone_y > 0.67)
            zoom /= 2;
        // Bottom right
        else if (zone_x > 0.67 && zone_y > 0.67)
            zoom *= 2;

        else
            super.onTouchEvent(event);

        invalidate();
        return super.onTouchEvent(event);
	}
	

}