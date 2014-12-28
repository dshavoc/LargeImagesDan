package com.example.largeimagesdan;

import java.util.Calendar;
import java.util.Random;
import java.util.Vector;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


public class FourSquareView extends View {
	boolean finished = false;
	int screenWidth,screenHeight;
	int duplicateColor;
	int correct = 0;
	int attempted = 0;
	int slowOnes = 0;
	User user;
	int startTime = convertTimeNowToMilliSeconds();
	int lastTime = startTime;
	MainActivity main;
	private static int maxTime = 23*60*60*1000 + 59*60*1000 +59*1000 + 999;
	Calendar cal;
	Random rand = new Random();
	Vector<DrawnObject> drawnObjects = new Vector<DrawnObject>();
	enum StateType {WaitForInput};
	StateType state = StateType.WaitForInput;

	enum Quadrant {UPPER_LEFT, UPPER_RIGHT, LOWER_LEFT, LOWER_RIGHT};

	public FourSquareView(Context context) {
		super(context);
		main = (MainActivity)context;
		screenHeight = main.screenHeight;
		screenWidth = main.screenWidth;
		createBoxes();

	}
	private void createBoxes(){
		System.out.println("creating");
		int left,top,halfWidth,halfHeight;
		left = 0;
		top = 0;
		halfWidth = screenWidth/2-1;
		halfHeight = screenHeight/2-1;
		
		DrawnObject upperLeft, upperRight, lowerLeft, lowerRight;
		
		upperLeft = new DrawnObject(left, top, halfWidth, halfHeight, Shape.RECT,Color.RED);
		upperRight = new DrawnObject(halfWidth, top, screenWidth, halfHeight, Shape.RECT,Color.BLACK);
		lowerLeft = new DrawnObject(left, halfHeight, halfWidth, screenHeight, Shape.RECT, Color.GRAY);
		lowerRight = new DrawnObject(halfWidth, halfHeight, screenWidth, screenHeight, Shape.RECT, Color.GREEN);

		drawnObjects.add(upperLeft);
		drawnObjects.add(upperRight);
		drawnObjects.add(lowerLeft);
		drawnObjects.add(lowerRight);

		pickColors();

	}	
	private void pickColors(){
		int[] colors = new int[5];

		colors[0] = Color.RED;
		colors[1] = Color.BLACK;
		colors[2] = Color.GREEN;
		colors[3] = Color.GRAY;
		colors[4] = Color.YELLOW;

		int r = rand.nextInt(5);
		duplicateColor = colors[r];
		int different1,different2;
		while (colors[r] == duplicateColor)
			r = rand.nextInt(5);
		different1 = colors[r];
		while (colors[r] == duplicateColor || colors[r] == different1)
			r = rand.nextInt(5);
		different2 = colors[r];
		
		assignAllColors(duplicateColor, different1, different2);

	}
	private void assignAllColors(int duplicateColor, int different1, int different2){
		int r = rand.nextInt(10)+1;
		System.out.println("think i am assigning");
		switch(r){
		case 1:
			drawnObjects.elementAt(0).color = duplicateColor;
			drawnObjects.elementAt(1).color = duplicateColor;
			drawnObjects.elementAt(2).color = different1;
			drawnObjects.elementAt(3).color = different2;
			
			break;
		case 2:
			drawnObjects.elementAt(3).color = duplicateColor;
			drawnObjects.elementAt(0).color = duplicateColor;
			drawnObjects.elementAt(1).color = different1;
			drawnObjects.elementAt(2).color = different2;
			break;
		case 3:
			drawnObjects.elementAt(2).color = duplicateColor;
			drawnObjects.elementAt(3).color = duplicateColor;
			drawnObjects.elementAt(0).color = different1;
			drawnObjects.elementAt(1).color = different2;
			break;
		case 4:
			drawnObjects.elementAt(1).color = duplicateColor;
			drawnObjects.elementAt(2).color = duplicateColor;
			drawnObjects.elementAt(3).color = different1;
			drawnObjects.elementAt(0).color = different2;
			break;
		case 5:
			drawnObjects.elementAt(1).color = duplicateColor;
			drawnObjects.elementAt(3).color = duplicateColor;
			drawnObjects.elementAt(2).color = different1;
			drawnObjects.elementAt(0).color = different2;
		case 6:
			drawnObjects.elementAt(0).color = duplicateColor;
			drawnObjects.elementAt(1).color = duplicateColor;
			drawnObjects.elementAt(3).color = different1;
			drawnObjects.elementAt(2).color = different2;
			break;
		case 7:
			drawnObjects.elementAt(2).color = duplicateColor;
			drawnObjects.elementAt(0).color = duplicateColor;
			drawnObjects.elementAt(1).color = different1;
			drawnObjects.elementAt(3).color = different2;
			break;
		case 8:
			drawnObjects.elementAt(3).color = duplicateColor;
			drawnObjects.elementAt(2).color = duplicateColor;
			drawnObjects.elementAt(0).color = different1;
			drawnObjects.elementAt(1).color = different2;
			break;
		case 9:
			drawnObjects.elementAt(3).color = duplicateColor;
			drawnObjects.elementAt(0).color = duplicateColor;
			drawnObjects.elementAt(1).color = different1;
			drawnObjects.elementAt(2).color = different2;
			break;
		case 10:
			drawnObjects.elementAt(1).color = duplicateColor;
			drawnObjects.elementAt(3).color = duplicateColor;
			drawnObjects.elementAt(2).color = different1;
			drawnObjects.elementAt(0).color = different2;
		
			break;
		default:
			System.out.println("r was " + r);
				
		}
	}
	public boolean onTouchEvent(MotionEvent event) {
		int eventaction = event.getAction();
		switch (eventaction) {
		case MotionEvent.ACTION_DOWN: 
			// finger touches the screen
			if (!finished){
				checkForCorrectBox(event.getX(), event.getY());
				pickColors();
			}
			break;

		case MotionEvent.ACTION_MOVE:
			// finger moves on the screen
			break;

		case MotionEvent.ACTION_UP:   
			// finger leaves the screen
			break;
		}

		// tell the system that we handled the event and no further processing is required
		return true; 
	}
	private int convertTimeNowToMilliSeconds(){
		cal = Calendar.getInstance();
		int convertedTime = 0;		
		int currentMilliSeconds = cal.get(Calendar.MILLISECOND);
		int currentSeconds = cal.get(Calendar.SECOND); 
		int currentMinutes = cal.get(Calendar.MINUTE);
		int currentHour = cal.get(Calendar.HOUR_OF_DAY);
		convertedTime = currentMilliSeconds+currentSeconds*1000+currentMinutes*60*1000+currentHour*60*60*1000;
		return convertedTime;
	}
	private int calculateElapsedTime (int startingTime){
		int elapsedTime = 0;
		int currentTime = convertTimeNowToMilliSeconds();
		if (currentTime>startingTime)
			elapsedTime = currentTime - startingTime;
		else elapsedTime = maxTime - startingTime + currentTime; // midnight scenario
		return elapsedTime; 
	}
	private void checkForCorrectBox(float x, float y){
		if (calculateElapsedTime(lastTime)>1000)
			slowOnes++;
		lastTime = convertTimeNowToMilliSeconds();
		attempted++;
		for (int i = 0; i < 4; i ++){
			if (drawnObjects.elementAt(i).color == duplicateColor 
					&& drawnObjects.elementAt(i).isClicked(x, y))
				correct++;
		}
		System.out.println(correct + " of " + attempted);
		if (correct>20){
			int elapsedTime = calculateElapsedTime(startTime);
			int tpc = elapsedTime/attempted;
			String message = "Average time per click = " + tpc + " milliseconds. You correctly answered " + correct + " of " + attempted + " attempted and were slow on " + slowOnes;
			Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
			finished = true;
			System.out.println("here");
			main.dbm.updateUser(main.u, tpc, "DOORSTIME");
			main.dbm.sayValue("DOORSTIME", main.u.initials);
		}
	}
		public void onDraw(Canvas canvas) {
		//draw objects
		int numberOfObjects = drawnObjects.size();
		for ( int i = 0; i < numberOfObjects; i++)
			drawnObjects.elementAt(i).draw(canvas);
	try {  
		Thread.sleep(100);  
	} catch (InterruptedException e) { }      
	invalidate();  // Force a re-draw
	}
}
