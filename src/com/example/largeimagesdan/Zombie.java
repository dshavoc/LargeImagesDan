package com.example.largeimagesdan;

import java.util.Random;
import com.example.largeimagesdan.Animation.AnimationLayout;
import android.graphics.Canvas;

public class Zombie extends AnimatedObject{
	Random rand = new Random();
	boolean attacking = false;
	public Zombie(float rx, float ry, AnimationLayout animationLayout) {
		super(rx, ry, animationLayout);
		//this.a = new Animation(AnimationModel.zombie, AnimationType.directionalLR);
		this.radius=100;
		setDestination(rand.nextInt(600),rand.nextInt(800));

		// TODO Auto-generated constructor stub
	}
	boolean attack(){
		boolean ret = false;
		animationFrames = animationLayout.attackLen;
		animationStart = animationLayout.attackCol;
		if (animationCount == animationFrames)
			ret = true;
		return ret;
	}


	private void walk(){
		animationFrames = 8;
		animationStart = 4;
		animationFrames = animationLayout.pursueLen;
		animationStart = animationLayout.pursueCol;
		move();
	}
	private void stand(){
		animationFrames = animationLayout.roamLen;
		animationStart = animationLayout.roamCol;
	}
	public void update(Canvas canvas, Animation animation) {
		if (rx == destinationX && ry == destinationY)
			attack();
		else {
			walk();
		}
		
		
		drawSelf(canvas, animation);
		int r = rand.nextInt(10);
		if (r == 9) setDestination(rand.nextInt(300)+100, rand.nextInt(700)+100);
	}

}
