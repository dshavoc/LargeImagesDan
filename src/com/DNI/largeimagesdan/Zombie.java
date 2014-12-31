package com.DNI.largeimagesdan;

import java.util.Random;

import com.DNI.largeimagesdan.Animation.AnimationLayout;

import android.graphics.Canvas;
enum ZombieAction{dying,headShot,stand,attack,shuffle}

public class Zombie extends AnimatedObject{
	Random rand = new Random();
	ZombieAction currentZombieAction;
	AnimatedObject player;
	public boolean isDying;
	
	//is animated is stored in animated object and decides whether or not an animated object is still being animated.

	public Zombie(float rx, float ry, Cowboy player, Animation animation) {
		super(rx, ry, animation);
		//this.a = new Animation(AnimationModel.zombie, AnimationType.directionalLR);
		radius = 50;
		speed = 7;
		currentZombieAction = ZombieAction.stand;
		this.player = player;
		// TODO Auto-generated constructor stub
	}
	private void attack(){
		currentZombieAction = ZombieAction.attack;
		animationCount = 0;
		animationFrames = 10;
		animationStart = 12;
		player.speed = 0;
	}
	private void shuffle(){
		currentZombieAction = ZombieAction.shuffle;
		animationFrames = 8;
		animationStart = 4;
		checkForAttackOpportunity();
	}
	public void killed(){
		currentZombieAction = ZombieAction.dying;
		animationCount = 0;
		animationFrames = 6;
		animationStart = 22;
		isDying = true;
	}
	public void headShotted(){
		currentZombieAction = ZombieAction.headShot;
		animationCount = 0;
		animationFrames = 6;
		animationStart = 28;
		isDying = true;
	}
	private void stand(){
		currentZombieAction = ZombieAction.stand;
		animationFrames = 4;
		animationStart = 0;
		intercept(player);
		currentZombieAction = ZombieAction.shuffle;
		//look
	}
	private void checkForAttackOpportunity(){
		int distance = distanceTo(player);
		int spacer = (int) (radius*.3 + player.radius*.3);
		if (distance < spacer)
				attack();
	}
	private void manageAnimations(){
		switch(currentZombieAction){
		case attack:
			if (animationCount == animationFrames-1)
				stand();
			break;
		case dying:
			if (animationCount == animationFrames-1)
				{
				isAnimated = false;
				}
			break;
		case headShot:
			if (animationCount == animationFrames-2)
				isAnimated = false;
			break;
		case shuffle:
			shuffle();
			if (rx == destinationX && ry == destinationY)
				stand();
			else {
				move();
				intercept(player);
			}
			break;
		case stand:
			intercept(player);
			shuffle();
			//look 
			break;
		default:
			break;
		
		}
	}
	private void intercept(AnimatedObject target){
		float a,c,d,e,g,h,i,j,t=-1,t1,t2;
		
		float A,B,C; //quadratic
		//givens
		a = rx;
		c = target.rx;
		d = target.speedX;
		e = ry;
		g = target.ry;
		h = target.speedY;
		//calculated
		i = c-a;
		j = g-e;
		
		A = d*d+h*h-speed*speed;
		B = 2*i*d+2*j*h;
		C = i*i + j*j;
		
		float u = B*B-4*A*C;
		if (u < 0)
			setDestination((int)player.rx, (int)player.ry);
			 // no intercept move toward;
		else{
			u = (float) Math.pow(u, .5);
			t1 = (-B+u)/(2*A);
			t2 = (-B-u)/(2*A);
			if (t1>0 && t2>0)//2 valid answers 
				{
				if (t1<t2)t = t1; else t = t2;
				}
			if (t1<0 && t2 >0)
				t = t2;
			if (t1>0 && t2<0)
				t = t1;
			
			if (t == t1 || t == t2){
				speedX = (i+d*t)/t;
				speedY = (j + h*t)/t;
				int newDestinationX = (int) (speedX*t+rx);
				int newDestinationY = (int) (speedY*t+ry);
				setDestination(newDestinationX, newDestinationY);
			}
			else setDestination((int)player.rx, (int)player.ry);
		}
	}
	
	public void update(Canvas canvas) {
		if (isAnimated) manageAnimations();
		drawSelf(canvas, animation);	
	}
}
