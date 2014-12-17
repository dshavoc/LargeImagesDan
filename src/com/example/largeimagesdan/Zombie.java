package com.example.largeimagesdan;

import java.util.Random;
import com.example.largeimagesdan.Animation.AnimationLayout;
import android.graphics.Canvas;

public class Zombie extends AnimatedObject{
	Random rand = new Random();
	boolean isAttacking = false;
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
		if (animationCount == animationFrames-1)
			ret = true;
		return ret;
	}


	private void walk(){
		animationFrames = 8;
		animationStart = 4;
		isAttacking = true;
		move();
	}
	private void stand(){
		animationFrames = animationLayout.roamLen;
		animationStart = animationLayout.roamCol;
	}
	public void update(Canvas canvas, Animation animation) {
		boolean isAtDestination = false;
		if (rx == destinationX && ry == destinationY) isAtDestination = true;

		if (isAtDestination && !isAttacking)	
			{
			isAttacking = true;
			animationCount = 0;
			stand();
			}
		if (isAtDestination && isAttacking)
			if (attack())
				{
				setDestination(rand.nextInt(800),rand.nextInt(800));
				move();
				isAttacking = false;
				}
		if (!isAtDestination)
			walk();

		drawSelf(canvas, animation);
		int r = rand.nextInt(10);
		if (r == 9 && !isAttacking) setDestination(rand.nextInt(300)+100, rand.nextInt(700)+100);
	}

}
