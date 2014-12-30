package com.DNI.multitask;

import java.util.Vector;

import com.DNI.largeimagesdan.MainActivity;

import com.example.largeimagesdan.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

public class MultiTaskView extends View{
	MainActivity main;
	MovePane movePane;
	Vector<Bitmap> zombieBitmaps = new Vector<Bitmap>();
	public MultiTaskView(Context context) {
		super(context);
		main = (MainActivity) context;
		loadBitmaps();
		// will create 4 pane
		movePane = new MovePane(new Rect(0,(int)(main.screenHeight*.25),main.screenWidth,(int)(main.screenHeight*.5)),zombieBitmaps,1);
		// TODO Auto-generated constructor stub
	}
	
	private void loadBitmaps(){
		zombieBitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.zombie1));
		zombieBitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.zombie2));
		zombieBitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.zombie3));
		zombieBitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.zombie4));
		zombieBitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.zombie5));
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		int eventaction = event.getAction();
		switch (eventaction) {
		case MotionEvent.ACTION_DOWN: 			// finger touches the screen
			//TODO: check if touch mattered
			int clickX = (int) event.getX();
			int clickY = (int) event.getY();
			if (movePane.processClick(clickX, clickY)) movePane.shufflePanels();
			else movePane.shufflePanels();
			break;
		}
		
		// tell the system that we handled the event and no further processing is required
		return true; 
	}
	protected void onDraw(Canvas canvas) {
		movePane.updatePane(canvas);
		try {  
			Thread.sleep(30);   
		} catch (InterruptedException e) { }      
		invalidate();
	}

}
