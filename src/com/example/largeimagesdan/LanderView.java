package com.example.largeimagesdan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

public class LanderView extends View {

	Bitmap landerBmp, backgroundBmp;
	LunarLander lander;
	
	public LanderView(Context context) {
		super(context);
		
		loadBitmaps();
		context.
		lander = new LunarLander(int startX, int startY, int finishY);
	}
	
	private void loadBitmaps(){
		landerBmp = BitmapFactory.decodeResource(getResources(), R.drawable.lander);
		backgroundBmp = BitmapFactory.decodeResource(getResources(), R.drawable.landerbackground);
	}

	public boolean onTouchEvent(MotionEvent event) {
		int eventaction = event.getAction();
		switch (eventaction) {
		case MotionEvent.ACTION_DOWN: 			// finger touches the screen
			//TODO: Detect whether the Thrust button has been pressed
			
			break;
		case MotionEvent.ACTION_UP:
			//TODO: Detect whether the Thrust button has been released
			
			break;
		}
		
		// tell the system that we handled the event and no further processing is required
		return true; 
	}
	
	protected void onDraw(Canvas canvas) {
		
	}
}
