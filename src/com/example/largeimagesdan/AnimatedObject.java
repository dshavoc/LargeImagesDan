package com.example.largeimagesdan;
import com.example.largeimagesdan.Animation.AnimationLayout;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
enum MoveDirection{north, northEast, east, southEast, south, southWest, west, northWest};

public class AnimatedObject {

	MoveDirection direction = MoveDirection.north;
	AnimationLayout animationLayout;
	boolean isAnimated = true;


	float radius=20; // radius of object
	float rx=50,ry=50; //position of object on x and y axis
	RectF size = new RectF(); //used for drawing object
	Rect frame = new Rect(); // used for drawing object
	int animationFrame =0;

	int speed = 8; // speed of object not speed of animation
	int destinationX, destinationY;
	
	float speedX;
	float speedY;
	
	int animationCount = 0;		//Which sprite in the animation is currently being rendered
	int animationStart = 0;		//Where in the current row (accounted for in drawSelf with animLaout) the current animation starts
	int animationFrames = 0;	//Number of frames of the current animation within the sprite sheet
	
	
	//Constructor takes origin
	public AnimatedObject(float rx, float ry){
		this.rx = rx;
		this.ry = ry;
		destinationX = (int)rx;
		destinationY = (int)ry;
		size.set(rx-radius, ry-radius, rx+radius, ry+radius);
	}
	
	public void setDestination(int newDestinationX, int newDestinationY){
		destinationX = newDestinationX;
		destinationY = newDestinationY;
		int dx = (int) (newDestinationX-rx);
		int dy = (int) (newDestinationY-ry);
		double d = Math.pow(dx*dx+dy*dy, .5);
		//double e = Math.hypot(dx,dy);
		int ax;
		ax = Math.abs(dx);
		double px = ax/(d);
		if (dx>0 && dy >0)
		{
			direction = MoveDirection.southEast;
			if (px>.7)direction = MoveDirection.east;
			if (px<.3)direction = MoveDirection.south;
		}
		if (dx<0 && dy <0){
			direction = MoveDirection.northWest;
			if (px>.7)direction = MoveDirection.west;
			if (px<.3)direction = MoveDirection.north;
		}

		if (dx>0 && dy <0){
			direction = MoveDirection.northEast;
			if (px>.7)direction = MoveDirection.east;
			if (px<.3)direction = MoveDirection.north;
		}
		if(dx <0 && dy >0){
			direction = MoveDirection.southWest;
			if (px>.7)direction = MoveDirection.west;
			if (px<.3)direction = MoveDirection.south;
		}
	}
	public boolean isClicked(float clickX, float clickY){
		boolean ret = false;
			if (clickX>rx-radius  && clickX<rx+radius && clickY>ry-radius && clickY < ry+ radius)
				ret = true;
		return ret;
	}
	public int distanceTo(AnimatedObject target){
		int ret = 0;
		double dx = (double) (target.rx - rx);
		double dy = (double) (target.ry - ry);
		ret = (int) Math.hypot(dx,dy);
		return ret;
	}
	public void move(){
		int dx=0,dy=0,d=0;
		speedX=0; speedY=0;
		dx = (int) (destinationX-rx);
		dy = (int) (destinationY-ry);
		d  = (int) Math.pow(dx*dx+dy*dy, .5);

		if(d<speed) {
			rx = destinationX;
			ry = destinationY;
			speedX = 0;
			speedY = 0;
			//System.out.println("at destination");
		}
		else{
			if (d>0)
			{
				speedX = speed*dx/d;
				speedY = speed*dy/d;
				//System.out.println ("moving");
			}
		}
		rx+=speedX;
		ry+=speedY;
		size.set(rx-radius, ry-radius, rx+radius, ry+radius);
		//System.out.println(rx + " " + ry);
	}

	public void drawSelf(Canvas canvas, Animation animation){
		if (isAnimated){
			
			switch(direction){
			case east: animationFrame = animation.animLayout.eastRow*animation.columns + animationCount + animationStart;
			break;
			case north: animationFrame =  animation.animLayout.northRow*animation.columns + animationCount + animationStart;
			break;
			case northEast:animationFrame = animation.animLayout.northEastRow*animation.columns + animationCount + animationStart;
			break;
			case northWest:animationFrame = animation.animLayout.northWestRow*animation.columns + animationCount + animationStart;
			break;
			case south:animationFrame =  animation.animLayout.southRow*animation.columns + animationCount + animationStart;
			break;
			case southEast:animationFrame =  animation.animLayout.southEastRow*animation.columns + animationCount + animationStart;
			break;
			case southWest:animationFrame =  animation.animLayout.southWestRow*animation.columns + animationCount + animationStart;
			break;
			case west:animationFrame =  animation.animLayout.westRow*animation.columns + animationCount + animationStart;
			break;
			default:
				break;

			}
			animationCount++;
			if(animationCount>=animationFrames && isAnimated)
				animationCount = 0;

		}
		Bitmap b;
		if (animationFrame < animation.animationFrames.size())
			{
			b = animation.animationFrames.elementAt(animationFrame);
			canvas.drawBitmap(b, rx-radius, ry-radius, null);
			}
		else {
			System.out.println("draw error: called a frame outside the size of animationFrames. Animation direction = " + direction + " animation count = " + animationCount);
			animationFrame = 0;
		}
	
	}

}
