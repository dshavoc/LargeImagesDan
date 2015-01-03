package com.DNI.PlanetHop;

import android.graphics.Canvas;

import com.DNI.largeimagesdan.AnimatedObject;
import com.DNI.largeimagesdan.Animation;


public class Rocket extends AnimatedObject{

	static enum RocketState {Home, Airborne, Landed, Crashed};
	RocketState rocketState;
	boolean isFiringThruster;
	boolean isRotatingClockwise;
	boolean isRotatingCounterClockwise;
	public int fuelRemaining;
	private static final float THRUST = .2f;
	public Rocket(float rx, float ry, Animation animation) {
		super(rx, ry, animation);
		rocketState = RocketState.Home;
		isFiringThruster = false;
		this.fuelRemaining = 100;
		// TODO Auto-generated constructor stub
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
	private void stopMoving(){
		speedX = 0;
		speedY = 0;
		accelerationX = 0;
		accelerationY = 0;
	}
	private void adjustAccelerationsFromThrust(){
		if (isFiringThruster){
		speedX += Math.sin(mRotation)*THRUST;
		speedY -= Math.cos(mRotation)*THRUST;
		isRotatingClockwise = false;
		isRotatingCounterClockwise = false;
		}
		if (isRotatingClockwise)
			{
			rotateByRadians(.1f);
			isFiringThruster = false;
			isRotatingCounterClockwise = false;
			}
		if (isRotatingCounterClockwise){
			rotateByRadians(-.1f);
			isFiringThruster = false;
			isRotatingClockwise = false;
		}
	}
	public void update(Canvas canvas){
		handleAnimations();
		adjustAccelerationsFromThrust();
		if (rocketState != RocketState.Home && rocketState != RocketState.Landed)
			movePhysics();
		else{
			stopMoving();			
		}
		drawSelf(canvas, animation);
	}
}
