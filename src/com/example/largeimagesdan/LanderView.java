package com.example.largeimagesdan;

import java.io.InputStream;

import com.example.largeimagesdan.LunarLander.LanderState;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

public class LanderView extends View {

	Bitmap landerBmp, backgroundBmp;
	LunarLander lander;
	Animation landerAnimation;
	SingleAnimation explosionAnimation;
	boolean explosionCreated = false;
	MainActivity main;
	RectF backGroundFrame;
	public LanderView(Context context) {
		super(context);
		main = (MainActivity) context;
		int startY = (int) (main.screenHeight*.1);
		int startX = (int) (main.screenWidth*.5);
		int finishY = (int) (main.screenHeight*.6);
		loadBitmaps();
		backGroundFrame = new RectF(0,0,main.screenWidth,main.screenHeight);
		
		lander = new LunarLander( startX,  startY, finishY);
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
	
	public boolean onTouchEvent(MotionEvent event) {
		int eventaction = event.getAction();
		switch (eventaction) {
		case MotionEvent.ACTION_DOWN: 			// finger touches the screen
			//TODO: Detect whether the Thrust button has been pressed
			lander.fireThrusters();
			break;
		case MotionEvent.ACTION_UP:
			//TODO: Detect whether the Thrust button has been released
			lander.stopThrusters();
			break;
		}
		
		// tell the system that we handled the event and no further processing is required
		return true; 
	}
	
	protected void onDraw(Canvas canvas) {
		
		canvas.drawBitmap(backgroundBmp,0,0,null);
		lander.update(canvas, landerAnimation, explosionAnimation);
		
		try {  
			Thread.sleep(30);   
		} catch (InterruptedException e) { }      
		invalidate();
	}
}
