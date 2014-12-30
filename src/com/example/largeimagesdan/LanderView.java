package com.example.largeimagesdan;

import java.io.InputStream;

import com.example.largeimagesdan.LunarLander.LanderState;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
	RectF landingPad;
	
	Paint paint;
	int finishY;
	public LanderView(Context context) {
		super(context);
		paint = new Paint();
		main = (MainActivity) context;
		int startY = (int) (main.screenHeight*.1);
		int startX = (int) (main.screenWidth*.5);
		finishY = (int) (main.screenHeight*.8);
		loadBitmaps();
		backGroundFrame = new RectF(0,0,main.screenWidth,main.screenHeight);
		lander = new LunarLander( startX,  startY, finishY);
		landingPad = new RectF(lander.rx-main.screenWidth*.12f,finishY+main.screenHeight*.01f, lander.rx+main.screenWidth*.12f,finishY+.03f*main.screenHeight);
		lander.radius = main.screenHeight*.08f;
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
		paint.setTextSize((float) (main.screenHeight*.05));
		paint.setColor(Color.WHITE);
		canvas.drawBitmap(backgroundBmp,0,0,null);
		paint.setColor(Color.DKGRAY);
		canvas.drawOval(landingPad, paint);
		lander.update(canvas, landerAnimation, explosionAnimation);
		
		canvas.drawText("Fuel:" + lander.fuelRemaining, main.screenWidth*.1f,main.screenHeight*.1f,paint);
		try {  
			Thread.sleep(30);   
		} catch (InterruptedException e) { }      
		invalidate();
	}
}
