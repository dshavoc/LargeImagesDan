package com.example.largeimagesdan;

import java.util.Random;

import com.example.largeimagesdan.Animation.AnimationLayout;

import android.graphics.Canvas;

public class Zombie extends AnimatedObject{
	Random rand = new Random();
	public Zombie(float rx, float ry, AnimationLayout animationLayout) {
		super(rx, ry, animationLayout);
		//this.a = new Animation(AnimationModel.zombie, AnimationType.directionalLR);
		this.radius=100;
		setDestination(100,100);

		// TODO Auto-generated constructor stub
	}
	private void stand(){
	
	}


	private void walk(){
		animationFrames = animationLayout.roamLen;
		animationStart = animationLayout.roamCol;
	}
	public void update(Canvas canvas, Animation animation) {
		walk();
		move();
		drawSelf(canvas, animation);
		int r = rand.nextInt(10);
		if (r == 9) setDestination(rand.nextInt(300)+100, rand.nextInt(700)+100);
	}

}
