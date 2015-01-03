package com.DNI.multitask;

import java.util.Vector;

import com.DNI.largeimagesdan.Animation;
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
	FirePane firePane;
	public boolean isVisible=false;
	ReloadPane reloadPane;
	CinematicPane cinematicPane;
	Vector<Bitmap> zombieBitmaps = new Vector<Bitmap>();
	Vector<Bitmap> humanBitmaps = new Vector<Bitmap>();
	Bitmap shotgunShell;
	int difficultyLevel =1;
	public MultiTaskView(Context context) {
		super(context);
		main = (MainActivity) context;
		loadBitmaps();
		// will create 4 pane
		
		// TODO Auto-generated constructor stub
	}
	
	private void loadBitmaps(){
		zombieBitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.zombie1));
		zombieBitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.zombie2));
		zombieBitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.zombie3));
		zombieBitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.zombie4));
		zombieBitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.zombie5));
		humanBitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.human1));
		humanBitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.human2));
		shotgunShell = BitmapFactory.decodeResource(getResources(), R.drawable.shotgunshell);
	}
	public void setup(Animation cowboyAnimation, Animation zombieAnimation){
		movePane = new MovePane(new Rect(0,(int)(main.screenHeight*.2),main.screenWidth,(int)(main.screenHeight*.4)),zombieBitmaps,difficultyLevel);
		firePane = new FirePane(new Rect(0,(int)(main.screenHeight*.4),main.screenWidth,(int)(main.screenHeight*.6)),zombieBitmaps,humanBitmaps,shotgunShell,difficultyLevel);
		reloadPane = new ReloadPane(new Rect(0,(int)(main.screenHeight*.6),main.screenWidth,(int)(main.screenHeight*.8)), difficultyLevel);
		cinematicPane = new CinematicPane(new Rect(0,0,main.screenWidth,(int)(main.screenHeight*.2)), cowboyAnimation, zombieAnimation, difficultyLevel);
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		int eventaction = event.getAction();
		performClick();
		switch (eventaction) {
		case MotionEvent.ACTION_DOWN: 		
			
			int clickX = (int) event.getX();
			int clickY = (int) event.getY();
			if (movePane.processClick(clickX, clickY)) {
				movePane.shufflePanels();
				firePane.reloadShotgun();
				cinematicPane.run();
			}
			if (firePane.processClick(clickX, clickY)) 
				cinematicPane.shootZombie();
			int reloadNumberClicked = reloadPane.processClick(clickX,clickY);
			if(reloadNumberClicked > 0) {
				if(movePane.adjustReloadNumber(reloadNumberClicked))
					firePane.reloadShotgun();
			}
			break;
		}
		
		// tell the system that we handled the event and no further processing is required
		return true; 
	}
	protected void onDraw(Canvas canvas) {
		
		movePane.updatePane(canvas);
		firePane.update(canvas);
		cinematicPane.update(canvas);
		reloadPane.update(canvas);
		try {  
			Thread.sleep(30);   
		} catch (InterruptedException e) { }      
		invalidate();
	}

}
