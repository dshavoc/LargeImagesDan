package com.DNI.largeimagesdan;

import java.io.InputStream;

import com.DNI.largeimagesdan.LunarLander.LanderState;
import com.DNI.largeimagesdan.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
enum gameState{LAUNCHING, LANDING}

public class LanderView extends View {

	Bitmap landerBmp, backgroundBmp;
	LunarLander lander;
	Animation landerAnimation;
	SingleAnimation explosionAnimation;
	boolean explosionCreated = false;
	MainActivity main;
	RectF backGroundFrame;
	RectF landingPad;
	long startTime = 0;
	public int testTime;
	public int failures;
	int startX, startY;
	Paint paint;
	int finishY;
	public LanderView(Context context) {
		super(context);
		paint = new Paint();
		main = (MainActivity) context;
		startY = (int) (main.screenHeight*.1);
		startX = (int) (main.screenWidth*.5);
		finishY = (int) (main.screenHeight*.8);
		loadBitmaps();
		backGroundFrame = new RectF(0,0,main.screenWidth,main.screenHeight);
		reset();
	}
	
	private void loadBitmaps(){
		landerBmp = BitmapFactory.decodeResource(getResources(), R.drawable.lander);
		backgroundBmp = BitmapFactory.decodeResource(getResources(), R.drawable.landerbackground);
		backgroundBmp = Bitmap.createScaledBitmap(backgroundBmp, main.screenWidth, main.screenHeight, false);
		InputStream is = getResources().openRawResource(R.drawable.lander);
		landerAnimation = new Animation(AnimationModel.lander,is);
		InputStream is2 = getResources().openRawResource(R.drawable.explosion);
		explosionAnimation = new SingleAnimation(SingleAnimationModel.explosion,is2);
	}
	private void reset(){
		lander = new LunarLander( startX,  startY, finishY, landerAnimation);
		landingPad = new RectF(lander.rx-main.screenWidth*.12f,finishY+main.screenHeight*.01f, lander.rx+main.screenWidth*.12f,finishY+.03f*main.screenHeight);
		lander.radius = main.screenHeight*.08f;
	}
	public boolean onTouchEvent(MotionEvent event) {
		int eventaction = event.getAction();
		if (startTime == 0) startTime = System.currentTimeMillis();
		switch (eventaction) {
		case MotionEvent.ACTION_DOWN: 			// finger touches the screen
			lander.fireThrusters();
			break;
		case MotionEvent.ACTION_UP:
			lander.stopThrusters();
			break;
		}
		// tell the system that we handled the event and no further processing is required
		return true; 
	}
	
	private boolean checkExitCondition() {
		boolean rtn = false;
		if( lander.landerState == LanderState.Crashed ) {
			rtn = true;
		}
		return rtn;
	}
	private void exit(){
			testTime = (int) (System.currentTimeMillis()-startTime);
			main.resourceController.processEndOfView(ViewType.lander);
	}

	
	float targetFps = 30;
	float targetMsPerFrame = 1000/targetFps;
	long timeLastUpdate = 0, timeNow = 0;
	
	protected void onDraw(Canvas canvas) {
		
		timeNow = System.currentTimeMillis();
		//Wait until the proper time has passed to render the next frame
		while( (timeNow - timeLastUpdate) < targetMsPerFrame ) {
			System.out.println("timeNow - timeLastUpdate = " + (timeNow - timeLastUpdate));
			try {  
				Thread.sleep(1);	//Max fps = 100, throttled down to targetFps   
			} catch (InterruptedException e) {
				System.err.println("PlanetHopView.onDraw error");
			}
			timeNow = System.currentTimeMillis();
		}
		timeLastUpdate = timeNow;
		
		//Draw here
		paint.setTextSize((float) (main.screenHeight*.05));
		paint.setColor(Color.WHITE);
		canvas.drawBitmap(backgroundBmp,0,0,null);
		paint.setColor(Color.DKGRAY);
		canvas.drawOval(landingPad, paint);
		if( checkExitCondition() )
			exit();
		invalidate();   
		lander.update(canvas, landerAnimation, explosionAnimation);
		if (lander.landerState == LanderState.Crashed || lander.landerState == LanderState.Landed)
		{
			if (lander.landerState == LanderState.Crashed) failures++;
				reset();
		}
		paint.setColor(Color.WHITE);
		canvas.drawText("Fuel:" + lander.fuelRemaining, main.screenWidth*.1f,main.screenHeight*.1f,paint);     
		invalidate();
	}
}
