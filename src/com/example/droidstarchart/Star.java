package com.example.droidstarchart;

public class Star {

	public float ra,de;
	
	Star(float _ra, float _de){
	ra = _ra;
	de = _de;
}
	
	public Point toRect(Star center, float zoom, Point screenSize){
		float screen_x = (ra - center.ra) * zoom / 24 + (float) screenSize.x/2;
		float screen_y = (de - center.de) * zoom / 180 + (float) screenSize.y/2;
		
	return new Point(screen_x,screen_y);
	}
}
