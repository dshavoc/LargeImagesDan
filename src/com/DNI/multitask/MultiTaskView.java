package com.DNI.multitask;

import java.util.Vector;

import com.DNI.largeimagesdan.Animation;
import com.DNI.largeimagesdan.MainActivity;
import com.DNI.largeimagesdan.R;
import com.DNI.largeimagesdan.ViewType;

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
	long startTime = 0;
	public int testTime;
	Bitmap shotgunShell, movePaneBG, reloadPaneBG, firePaneBG, cinematicPaneBG;
	public int failures=0;
	int clicks = 0;
	int successes = 0;
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
		shotgunShell = BitmapFactory.decodeResource(getResources(), R.drawable.bullet);
		movePaneBG = BitmapFactory.decodeResource(getResources(), R.drawable.move_pane_bg);
		cinematicPaneBG = BitmapFactory.decodeResource(getResources(), R.drawable.cinematicbg);
		firePaneBG = BitmapFactory.decodeResource(getResources(), R.drawable.shooting_gallery);
		reloadPaneBG = BitmapFactory.decodeResource(getResources(), R.drawable.reloadbg);
		
	}
	public void setup(Animation cowboyAnimation, Animation zombieAnimation){
		movePane = new MovePane(new Rect(0,(int)(main.screenHeight*.2),main.screenWidth,(int)(main.screenHeight*.4)),zombieBitmaps,difficultyLevel,movePaneBG);
		firePane = new FirePane(new Rect(0,(int)(main.screenHeight*.4),main.screenWidth,(int)(main.screenHeight*.6)),zombieBitmaps,humanBitmaps,shotgunShell,difficultyLevel,firePaneBG);
		reloadPane = new ReloadPane(new Rect(0,(int)(main.screenHeight*.6),main.screenWidth,(int)(main.screenHeight*.8)), difficultyLevel,movePane,reloadPaneBG);
		cinematicPane = new CinematicPane(new Rect(0,0,main.screenWidth,(int)(main.screenHeight*.2)), cowboyAnimation, zombieAnimation, difficultyLevel, cinematicPaneBG);
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		int eventaction = event.getAction();
		performClick();
		clicks++;
		switch (eventaction) {
		case MotionEvent.ACTION_DOWN: 		
			if (startTime == 0) startTime = System.currentTimeMillis();
			int clickX = (int) event.getX();
			int clickY = (int) event.getY();
			if (movePane.processClick(clickX, clickY)) {
				movePane.shufflePanels();
				cinematicPane.run();
				successes++;
			}
			
			if (firePane.processClick(clickX, clickY)) 
				{
				cinematicPane.shootZombie();
				successes++;
				}
			
			int reloadNumberClicked = reloadPane.processClick(clickX,clickY);
			if(reloadNumberClicked > 0) {
				if(movePane.adjustReloadNumber(reloadNumberClicked))
					{
					successes++;
					firePane.reloadShotgun();
					}
			}
			
			break;
		}
		
		// tell the system that we handled the event and no further processing is required
		return true; 
	}
	private void exit(){
		failures = clicks-successes;
		//failures+=(int)(cinematicPane.failures/10);
		testTime = (int) (System.currentTimeMillis()-startTime);
		main.resourceController.processEndOfView(ViewType.multitask);
	}
	float targetFps = 100;
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

	movePane.updatePane(canvas);
	firePane.update(canvas);
	reloadPane.update(canvas);
	if(cinematicPane.update(canvas))exit();
	      
	invalidate();
}

}
