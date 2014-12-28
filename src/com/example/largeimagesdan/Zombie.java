package com.example.largeimagesdan;

import java.util.Random;
import com.example.largeimagesdan.Animation.AnimationLayout;
import android.graphics.Canvas;

public class Zombie extends AnimatedObject{
	Random rand = new Random();
	boolean isAttacking = false;
	public Zombie(float rx, float ry) {
		super(rx, ry);
		//this.a = new Animation(AnimationModel.zombie, AnimationType.directionalLR);
		this.radius=50;
		setDestination(rand.nextInt(600),rand.nextInt(800));

		// TODO Auto-generated constructor stub
	}
	private void attack(){
		animationFrames = 10;
		animationStart = 12;
		
		if (animationCount == animationFrames-1)
			isAttacking = false;
	}


	private void walk(){
		animationFrames = 8;
		animationStart = 4;
		isAttacking = true;
		move();
	}
	private void stand(){//broken
		if (rand.nextInt(10)==9&&!isAttacking)
			isAttacking = true;
		if (rand.nextInt(10)==9&&!isAttacking)
			setDestination(rand.nextInt(500), rand.nextInt(500));
		animationFrames = 4;
		animationStart = 0;
	}
	public void update(Canvas canvas, Animation animation) {
		if (isAttacking)attack();
		{
			if (rx == destinationX && ry == destinationY)
				stand();
			else
				walk();
			drawSelf(canvas, animation);

			int r = rand.nextInt(10);

			if (r == 9 && !isAttacking) setDestination(rand.nextInt(300)+100, rand.nextInt(700)+100);
		}
	}
}
