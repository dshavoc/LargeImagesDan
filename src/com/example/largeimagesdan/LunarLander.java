package com.example.largeimagesdan;

import android.graphics.Canvas;

public class LunarLander extends AnimatedObject {
	
	private static final double PX_PER_METER = .01;
	private static final double ACCEL_GRAVITY = 9.8 * PX_PER_METER;		// px/s^2
	private static final double ACCEL_THRUST  = -24 * PX_PER_METER;		// px/s^2
	private static final double MAX_SAFE_LAND_SPEED = 300 * PX_PER_METER;	// px/s
	private static final int STARTING_FUEL = 50;
	
	//height and acceleration will be calculated in double for smoother physics and finer acceleration control
	//POSITIVE IS DOWN
	private double startY;
	private double posY;
	private double accel;
	private double landerSpeed;
	private double groundLevelY;
	private int fuelRemaining;
	private boolean isFiringThruster;
	SingleAnimationObject explosion;

	static enum LanderState {Airborne, Landed, Crashed};
	public LanderState landerState;

	private long timeLastUpdate;
	
	
	public LunarLander(int startX, int startY, int finishY) {
		super(startX, startY);
		landerState = LanderState.Airborne;
		this.startY = (double)startY;
		groundLevelY = (double)finishY;
		reset();
	}
	
	public void reset() {
		fuelRemaining = STARTING_FUEL;
		posY = startY;
		accel = 0;
		landerSpeed = 0;
		isFiringThruster = false;
		landerState = LanderState.Airborne;		
	}
	
	public int getFuelRemaining() {
		return fuelRemaining;
	}
	
	public void fireThrusters() {
		if(fuelRemaining > 0)
			isFiringThruster = true;
	}
	
	public void stopThrusters() {
		isFiringThruster = false;
	}

	public void update(Canvas canvas, Animation flightAnimation, SingleAnimation explosionAnimation) {
		updatePhysicsIan();
		handleAnimations();
		checkWinCondition();
		if (isFiringThruster) fuelRemaining --;
		if (landerState != LanderState.Crashed) drawSelf(canvas, flightAnimation);
		else if (explosion == null){
			explosion = new SingleAnimationObject(explosionAnimation, rx, ry);
			explosion.update(canvas);
		}
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
	private void updatePhysics() {
		if(landerState == LanderState.Airborne) {
			//Find time since last physics update
			long timeNow = System.currentTimeMillis();							//milliseconds
			double deltaTime = (double)(timeNow - timeLastUpdate) / 1000;		//seconds
			timeLastUpdate = timeNow;
			
			//Calculate acceleration
			accel = ACCEL_GRAVITY;
			if(fuelRemaining>0)
				{
				if(isFiringThruster)	accel -= ACCEL_THRUST;
				}
			else 
				isFiringThruster = false;
			
			//Update position
			posY += landerSpeed * deltaTime + 0.5 * accel * deltaTime * deltaTime;
			
			//update velocity
			landerSpeed += accel * deltaTime;
			
			//Copy the lander position into AnimatedObject space
			ry = (float) posY;
			
		}
	}
	private void updatePhysicsIan () {
		if(landerState == LanderState.Airborne) {
			//Calculate acceleration
			if(fuelRemaining<0&&isFiringThruster) 
				isFiringThruster = false;
			if(isFiringThruster) speedY+=ACCEL_THRUST;
			speedY+=ACCEL_GRAVITY;			
			movePhysics();
		}
		
	}
	
	private void checkWinCondition() {
	if (landerState == LanderState.Airborne)
		if (ry >= groundLevelY) //encountered ground
			if (speedY > MAX_SAFE_LAND_SPEED)
				landerState = LanderState.Crashed;
			else landerState = LanderState.Landed;
			
	}
}
