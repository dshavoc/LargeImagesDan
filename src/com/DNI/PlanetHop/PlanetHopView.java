package com.DNI.PlanetHop;

import com.DNI.PlanetHop.Rocket.RocketState;
import com.DNI.largeimagesdan.Animation;
import com.DNI.largeimagesdan.MainActivity;
import com.DNI.largeimagesdan.R;
import com.DNI.largeimagesdan.SingleAnimation;
import com.DNI.largeimagesdan.ViewType;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

public class PlanetHopView extends View {
	Rocket rocket;
	Planet home, destination;
	Animation rocketAnimation;
	SingleAnimation explosionAnimation;
	Bitmap background;
	Bitmap planet;
	MainActivity main;
	long startTime = 0;
	public int testTime;
	public int failures;
	RectF bounds;
	
	
	public PlanetHopView(Context context, Animation rocketAnimation, SingleAnimation explosionAnimation) {
		super(context);
		main = (MainActivity) context;
		bounds = new RectF(0,0,main.screenWidth,main.screenHeight);
		this.rocketAnimation = rocketAnimation;
		this.explosionAnimation = explosionAnimation;
		loadBitmaps();
		createPlanets();	//create planets before rocket
		reset();
	}
	private void loadBitmaps(){
		background = BitmapFactory.decodeResource(getResources(), R.drawable.spacebg);
		planet = BitmapFactory.decodeResource(getResources(), R.drawable.earth);
	}
	public boolean onTouchEvent(MotionEvent event) {
		if (startTime == 0) startTime = System.currentTimeMillis();
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
	private void reset(){
		startTime=0;
		createRocket();
	}
	
	private void createPlanets(){
		home = new Planet(main.screenWidth*.2f, main.screenHeight*.23f, main.screenWidth*.06f,planet);
		destination = new Planet(main.screenWidth*.8f, main.screenHeight*.75f, main.screenWidth*.06f,planet);
	}
	
	private void createRocket(){
		rocket = new Rocket(home, main.screenWidth*0.04f, rocketAnimation, explosionAnimation);
	}
	
	private void updateWorldPhysics(Canvas canvas){
		home.update(canvas, rocket);
		destination.update(canvas, rocket);
		rocket.update(canvas);
	}
	

	float targetFps = 30;
	float targetMsPerFrame = 1000/targetFps;
	long timeLastUpdate = 0, timeNow = 0;
	protected void onDraw(Canvas canvas) {
	
		timeNow = System.currentTimeMillis();
		//Wait until the proper time has passed to render the next frame
		while( (timeNow - timeLastUpdate) < targetMsPerFrame ) {
			try {  
				Thread.sleep(1);	//Max fps = 100, throttled down to targetFps   
			} catch (InterruptedException e) {
				System.err.println("PlanetHopView.onDraw error");
			}
			timeNow = System.currentTimeMillis();
		}
		timeLastUpdate = timeNow;
		
		//Draw here
		canvas.drawBitmap(background, null, bounds, null);
		updateWorldPhysics(canvas);
		if( checkExitCondition() )
			exit();
		invalidate();   

	}
	
	private boolean checkExitCondition() {
		boolean rtn = false;
		if( rocket.rocketState == RocketState.Crashed ) {
			if( rocket.explosion != null ) {
				if( rocket.explosion.isFinished ) {
					reset();
					failures++;
					if (failures == 3)
						{
						rtn = true;
						testTime = -1;
						}
					
				}
			}
		} else if( rocket.rocketState == RocketState.Landed ) {
			testTime = (int) (System.currentTimeMillis()-startTime);
			rtn = true;
		}
		return rtn;
	}
	
	private void exit() {
		main.resourceController.processEndOfView(ViewType.planetHop);
	}

}
