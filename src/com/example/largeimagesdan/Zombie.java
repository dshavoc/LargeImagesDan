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
	boolean attack(){
		boolean ret = false;
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
		
	}
	public void update(Canvas canvas, Animation animation) {
		walk();
		drawSelf(canvas, animation);
		int r = rand.nextInt(10);
		if (r == 9 && !isAttacking) setDestination(rand.nextInt(300)+100, rand.nextInt(700)+100);
	}

}
