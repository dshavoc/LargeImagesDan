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
	private float radius = 15;
	public Planet(float rx, float ry) {
		this.rx = rx;
		this.ry = ry;
		
		massByG = 100; // to be modified and considered at a laterDate;
		// TODO Auto-generated constructor stub
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
	private void detectCollision(Rocket rocket){
		if (rocket.rx+rocket.radius>rx-radius && rocket.rx-rocket.radius <rx+radius && rocket.ry+rocket.radius>ry-radius && rocket.ry-rocket.radius <ry+radius&& rocket.rocketState != RocketState.Landed)
			rocket.rocketState = RocketState.Crashed;
	}
	
	private void drawSelf(Canvas canvas){
		Paint paint = new Paint();
		paint.setColor(Color.BLUE);
		canvas.drawCircle(rx, ry, 2*radius, paint);
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
