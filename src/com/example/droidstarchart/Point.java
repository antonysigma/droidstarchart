package com.example.droidstarchart;

public class Point {
	public float x, y;

	Point(float _x, float _y) {
		x = _x;
		y = _y;
	}
	
	public Boolean inScreen(Point screenSize){
		return (x>=0 && x<=screenSize.x) && (y>=0 && y<=screenSize.y);
	}
}
