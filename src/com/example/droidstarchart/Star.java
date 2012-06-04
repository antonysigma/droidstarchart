package com.example.droidstarchart;

public class Star {

	public float ra,de;
	
	Star(float _ra, float _de){
	ra = _ra;
	de = _de;
}
	
	public Point toRect(Star center, float zoom, Point screenSize){
		float screen_x = (ra - center.ra) * zoom / 12 + (float) screenSize.x/2;
		float screen_y = (de - center.de) * zoom / 180 + (float) screenSize.y/2;
		
		if(screen_x >=0 && screen_x <= screenSize.x)
			return new Point(screen_x,screen_y);

		// Right unwrap RA coordinate
		float screen_x1 = (ra - center.ra + 24) * zoom / 12 + (float) screenSize.x/2;
		if(screen_x1 >=0 && screen_x1 <= screenSize.x)
			return new Point(screen_x1,screen_y);

		// Left unwrap RA coordinate
		screen_x1 = (ra - center.ra - 24) * zoom / 12 + (float) screenSize.x/2;
		if(screen_x1 >=0 && screen_x1 <= screenSize.x)
			return new Point(screen_x1,screen_y);
		
		// Else output default
		return new Point(screen_x,screen_y);
	}
}
