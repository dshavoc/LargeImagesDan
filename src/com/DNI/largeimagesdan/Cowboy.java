package com.DNI.largeimagesdan;
import android.graphics.Canvas;

public class Cowboy extends AnimatedObject{
	public boolean isShooting = false;
	public boolean isDead = false;
	public boolean isAtDestination = false;
	public Cowboy(float rx, float ry, Animation animation) {
		super(rx, ry, animation);
		radius = 50;
		// TODO Auto-generated constructor stub
		
	}

	private void stand(){
		isAtDestination = true;
		animationFrames = 1;
		animationStart = 0;
	}
	public void die(){
		isDead = true;
		//an animation or sound effects should accompany this.
	}
	public void shoot(){
	//	System.out.println("shot");
		if(!isShooting) isShooting = true;
		animationFrames = 3;
		animationStart = 10;
		animationCount = 0;
		
	}

	private void walk(){
		animationFrames = 9;
		animationStart = 1;
		move();
		accelerationX = 5;
	}

	public void update(Canvas canvas){
		isAtDestination = false;
		if (isShooting){
			if(animationCount==animationFrames-1) 
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
