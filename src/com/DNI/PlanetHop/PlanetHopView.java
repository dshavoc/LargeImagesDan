package com.DNI.PlanetHop;

import com.DNI.PlanetHop.Rocket.RocketState;
import com.DNI.largeimagesdan.Animation;
import com.DNI.largeimagesdan.MainActivity;
import com.DNI.largeimagesdan.SingleAnimation;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

public class PlanetHopView extends View {
	Rocket rocket;
	Planet home, destination;
	Animation rocketAnimation;
	SingleAnimation explosionAnimation;
	MainActivity main;
	public PlanetHopView(Context context, Animation rocketAnimation, SingleAnimation explosionAnimation) {
		super(context);
		main = (MainActivity) context;
		this.rocketAnimation = rocketAnimation;
		this.explosionAnimation = explosionAnimation;
		createPlanets();
		createRocket();
		// TODO Auto-generated constructor stub
	}
	public boolean onTouchEvent(MotionEvent event) {
		int eventaction = event.getAction();
		float third = main.screenWidth/3;
		switch (eventaction) {
		case MotionEvent.ACTION_DOWN: 			// finger touches the screen
			if (event.getX()<third)// first third
				rocket.rotateCounterClockwise();
			if (event.getX()>=third && event.getX()< 2*third)// first third
				rocket.fireThrusters();
			if (event.getX()> 2*third)
				rocket.rotateClockwise();
		
			break;
		case MotionEvent.ACTION_UP:
			rocket.stopThrusters();
			break;
		}
		
		// tell the system that we handled the event and no further processing is required
		return true; 
	}
	private void createPlanets(){
		home = new Planet(main.screenWidth*.2f,main.screenHeight*.23f);
		destination = new Planet(main.screenWidth*.8f,main.screenHeight*.75f);
	}
	
	private void createRocket(){
		rocket = new Rocket(main.screenWidth*.2f,main.screenHeight*.20f,rocketAnimation);
		rocket.setRadius(main.screenWidth*.05f);
	}
	
	private void updateWorldPhysics(Canvas canvas){
		home.update(canvas, rocket);
		destination.update(canvas, rocket);
		rocket.update(canvas);
	}
	protected void onDraw(Canvas canvas) {
		updateWorldPhysics(canvas);
		try {  
			Thread.sleep(30);   
		} catch (InterruptedException e) { }      
		invalidate();
	}

}
