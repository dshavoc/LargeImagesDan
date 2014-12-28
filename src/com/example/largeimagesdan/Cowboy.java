package com.example.largeimagesdan;
import android.graphics.Canvas;

public class Cowboy extends AnimatedObject{
	boolean isShooting = false;
	public Cowboy(float rx, float ry) {
		super(rx, ry);
		// TODO Auto-generated constructor stub
		
	}

	private void stand(){
		animationFrames = 1;
		animationStart = 0;
	}

	public void shoot(){
	//	System.out.println("shot");
		animationFrames = 3;
		animationStart = 10;
		
	}

	private void walk(){
		animationFrames = 9;
		animationStart = 1;
		move();
	}

	public void update(Canvas canvas, Animation animation){
		if (isShooting){
			shoot();
			if (animation.animationCount==animationFrames)
				isShooting = false;
		}
		else{
		if (rx == destinationX && ry == destinationY){
			stand();
			//System.out.println("standing");
		}
		else walk();
		}
		drawSelf(canvas, animation);	
	}


}
