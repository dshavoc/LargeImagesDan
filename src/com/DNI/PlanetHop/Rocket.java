package com.DNI.PlanetHop;

import android.graphics.Canvas;

import com.DNI.largeimagesdan.AnimatedObject;
import com.DNI.largeimagesdan.Animation;
import com.DNI.largeimagesdan.SingleAnimation;
import com.DNI.largeimagesdan.SingleAnimationObject;


public class Rocket extends AnimatedObject{

	static enum RocketState {Home, Airborne, Landed, Crashed};
	RocketState rocketState;
	SingleAnimation explosionAnimation;
	SingleAnimationObject explosion;
	boolean isFiringThruster;
	boolean isRotatingClockwise;
	boolean isRotatingCounterClockwise;
	public int fuelRemaining;
	private static final float THRUST = 0.3f;
	
	public Rocket(Planet origin, float radius, Animation animation, SingleAnimation explosionAnimation) {
		super(origin.rx, origin.ry - origin.radius - radius, radius, animation);
		setCenter(0.5f, 0.4f);
		//super(origin.rx, origin.ry, radius, animation);
		rocketState = RocketState.Home;
		isFiringThruster = false;
		this.fuelRemaining = 100;
		this.explosionAnimation = explosionAnimation;
	}
	
	public void fireThrusters() {
		if(fuelRemaining > 0)
			isFiringThruster = true;
		if (rocketState == RocketState.Home)
			rocketState = RocketState.Airborne;
	}
	
	public void stopThrusters() {
		isFiringThruster = false;
		isRotatingClockwise = false;
		isRotatingCounterClockwise = false;
	}
	
	public void rotateClockwise(){
		isRotatingClockwise = true;
	}
	
	public void rotateCounterClockwise(){
		isRotatingCounterClockwise = true;
	}
	
	private void handleAnimations(){
		if (isFiringThruster)
		{
			animationCount = 0;
			animationFrames = 1;
			animationStart = 0;
		}
		else{
			animationCount = 0;
			animationFrames = 1;
			animationStart = 1;
		}
	}
	
	private void stopMoving() {
		speedX = 0;
		speedY = 0;
		accelerationX = 0;
		accelerationY = 0;
	}
	
	private void adjustAccelerationsFromThrust() {
		if (isFiringThruster){
			//mRotation=0 is oriented upward (negative y)
			speedX += Math.sin(mRotation)*THRUST;		
			speedY -= Math.cos(mRotation)*THRUST;
			isRotatingClockwise = false;
			isRotatingCounterClockwise = false;
		}
		if (isRotatingClockwise) {
			rotateByRadians(.1f);
			isFiringThruster = false;
			isRotatingCounterClockwise = false;
		}
		if (isRotatingCounterClockwise) {
			rotateByRadians(-.1f);
			isFiringThruster = false;
			isRotatingClockwise = false;
		}
	}
	
	public void update(Canvas canvas){
		
		//if (rocketState != RocketState.Home && rocketState != RocketState.Landed)
		if (rocketState == RocketState.Airborne) {
			movePhysics();
			adjustAccelerationsFromThrust();
		} else {
			stopMoving();
			if(rocketState == RocketState.Crashed) {
				//crash anim
				if(explosion == null) {
					//Explosion is beginning. Create the explosion here.
					 explosion = new SingleAnimationObject(explosionAnimation, rx, ry);
				}				
				explosion.update(canvas);
			}
		}
		if(rocketState != RocketState.Crashed) {
			handleAnimations();
			drawSelf(canvas, animation);
		}
	}
}
