package com.example.largeimagesdan;

import com.example.largeimagesdan.Animation.AnimationLayout;

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
		isShooting = true;
		
		animationFrames = 3;
		animationStart = 10;
	}

	private void walk(){
		animationFrames = 9;
		animationStart = 1;
	}

	public void update(Canvas canvas, Animation animation){
		if (rx == destinationX && ry == destinationY){
			stand();
			//System.out.println("standing");
		}
		else walk();
		move();
		drawSelf(canvas, animation);
		
		
	}


}
