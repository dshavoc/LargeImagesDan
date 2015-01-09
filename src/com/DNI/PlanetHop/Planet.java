package com.DNI.PlanetHop;

import java.util.Random;

import com.DNI.PlanetHop.Rocket.RocketState;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Planet {
	Random rand = new Random();
	int massByG;
	public float rx;
	public float ry;
	public float radius;
	
	public float SAFE_ENTRY_SPEED = 2;		//TODO: Futz with this 
	
	public Planet(float rx, float ry, float radius) {
		this.rx = rx;
		this.ry = ry;
		this.radius = radius;
		massByG = (int) (radius*75); // to be modified and considered at a laterDate;
		//cMBG/cR^2 = mbg/r^2
	}
	private void setMassByG(){
		int massByGravityConstant = 75;
		//let CR^2 = 1;
		
		massByG = (int) (radius*radius*massByGravityConstant);
	}
	private void enactGravity(Rocket rocket){
		float dx = rx-rocket.rx;
		float dy = ry-rocket.ry;
		float distance = (float) Math.hypot(dx,dy);
		
		float acceleration = massByG/(distance*distance);
		float accelerationX = acceleration*dx/distance;
		float accelerationY = acceleration*dy/distance;
		rocket.speedX+=accelerationX;
		rocket.speedY+=accelerationY;
	}
	private void detectCollision(Rocket rocket) {
		if( /*rocket.rx+rocket.radius>rx-radius &&
			rocket.rx-rocket.radius <rx+radius &&
			rocket.ry+rocket.radius>ry-radius &&
			rocket.ry-rocket.radius <ry+radius &&
			rocket.rocketState*/
			Math.hypot((rocket.rx - rx), (rocket.ry - ry)) < (radius)	&&
			rocket.rocketState == RocketState.Airborne)
		{
			if(Math.hypot(rocket.speedX, rocket.speedY) <= SAFE_ENTRY_SPEED) {
				rocket.rocketState = RocketState.Landed;
			} else {
				rocket.rocketState = RocketState.Crashed;
			}
		}
	}
	
	private void drawSelf(Canvas canvas){
		Paint paint = new Paint();
		paint.setColor(Color.BLUE);
		canvas.drawCircle(rx, ry, radius, paint);
	}
	
	public void update(Canvas canvas, Rocket rocket){
		if (rocket.rocketState != RocketState.Home)
		{
			enactGravity(rocket);
			detectCollision(rocket);
		}
		drawSelf(canvas);
	}
}
