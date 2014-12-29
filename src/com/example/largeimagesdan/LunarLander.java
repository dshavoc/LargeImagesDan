package com.example.largeimagesdan;

import android.graphics.Canvas;

public class LunarLander extends AnimatedObject {
	
	private static final double PX_PER_METER = 10;
	private static final double ACCEL_GRAVITY = 9.8 * PX_PER_METER;		// px/s^2
	private static final double ACCEL_THRUST  = 14 * PX_PER_METER;		// px/s^2
	private static final double MAX_SAFE_LAND_SPEED = 1 * PX_PER_METER;	// px/s
	private static final int STARTING_FUEL = 200;
	
	//height and acceleration will be calculated in double for smoother physics and finer acceleration control
	//POSITIVE IS DOWN
	private double startY;
	private double posY;
	private double accel;
	private double landerSpeed;
	private double groundLevelY;
	private int fuelRemaining;
	private boolean isFiringThruster;

	static enum LanderState {Airborne, Landed, Crashed};
	private LanderState landerState;

	private long timeLastUpdate;
	
	
	public LunarLander(int startX, int startY, int finishY) {
		super(startX, startY);
		this.startY = (double)startY;
		groundLevelY = (double)finishY;
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
		isFiringThruster = true;
	}
	
	public void stopThrusters() {
		isFiringThruster = false;
	}

	public void update(Canvas canvas, Animation animation) {
		updatePhysics();
		checkWinCondition();
		drawSelf(canvas, animation);
	}
	
	private void updatePhysics() {
		if(landerState == LanderState.Airborne) {
			//Find time since last physics update
			long timeNow = System.currentTimeMillis();							//milliseconds
			double deltaTime = (double)(timeNow - timeLastUpdate) * 1000;		//seconds
			timeLastUpdate = timeNow;
			
			//Calculate acceleration
			accel = ACCEL_GRAVITY;
			if(isFiringThruster)	accel -= ACCEL_THRUST;
			
			//Update position
			posY += landerSpeed * deltaTime + 0.5 * accel * deltaTime * deltaTime;
			
			//update velocity
			landerSpeed += accel * deltaTime;
			
			//Copy the lander position into AnimatedObject space
			ry = (float) posY;
			
		}
	}
	
	private void checkWinCondition() {
		if(posY >= groundLevelY) {
			if(landerSpeed <= MAX_SAFE_LAND_SPEED) {
				landerState = LanderState.Landed;
			}
			else {
				landerState = LanderState.Crashed;
			}
		}
	}

}
