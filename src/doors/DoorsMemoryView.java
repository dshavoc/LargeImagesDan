package doors;

import java.io.SequenceInputStream;
import java.util.Random;
import java.util.Vector;

import com.DNI.largeimagesdan.MainActivity;
import com.DNI.largeimagesdan.R;
import com.DNI.largeimagesdan.ViewType;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.test.suitebuilder.TestSuiteBuilder.FailedToCreateTests;
import android.view.MotionEvent;
import android.view.View;

public class DoorsMemoryView extends View{
	enum ViewState{intro1, intro2, fail, test, learn};
	boolean testing = false;
	boolean inFail = false;
	
	long endIntro;
	long endFail;
	ViewState viewState = ViewState.intro1;
	String message = "Tap to begin.";
	int pointInSequence = 0;
	int zombieBitmap = 0;
	Random rand = new Random();
	Bitmap background;
	RectF panel0, panel1, panel2, panel3, backgroundR;
	long startTime;
	int failPanel;
	public int testTime;
	int correctDoor;
	boolean resetFlag;
	int totalDoors =0 ;
	public int wrongDoors;
	MainActivity main;
	Vector<RectF>  panes = new Vector<RectF>();
	Vector<Bitmap> zombieBitmaps = new Vector<Bitmap>();
	Vector<SignalBall> balls = new Vector<SignalBall>();
	public DoorsMemoryView(Context context) {
		super(context);
		main = (MainActivity) context;
		panel0 = new RectF();
		panel1 = new RectF();
		panel2 = new RectF();
		panel3 = new RectF();
		backgroundR = new RectF(0,0,main.screenWidth,main.screenHeight*.9f);

		panel0.set(
				0,
				0, 
				main.screenWidth*.5f,
				main.screenHeight*.3f);
		panel1.set(
				main.screenWidth*.5f,
				0,
				main.screenWidth,
				main.screenHeight*.3f);
		panel2.set(
				0,
				main.screenHeight*.3f,
				main.screenWidth*.5f,
				main.screenHeight*.7f);
		panel3.set(
				main.screenWidth*.5f,
				main.screenHeight*.4f,
				main.screenWidth,
				main.screenHeight*.7f);
		panes.add(panel0);
		panes.add(panel1);
		panes.add(panel2);
		panes.add(panel3);
		createMemoryTest(5);
		loadBitmaps();
		// TODO Auto-generated constructor stub
	}
	private void createMemoryTest(int n){
		int r, lastR=4;
		for (int i = 0; i< n; i++){
			r = rand.nextInt(4);
			if (i > 0)
				while (r == lastR){
					r = rand.nextInt(4);
				}
			balls.add(new SignalBall(r,i, (int) (main.screenWidth*.05f), main.screenHeight*.83f));
			lastR = r;
		}
	}
	private void startTest(){
		viewState = ViewState.test;
		message = "started test";
		testing = true;
		pointInSequence = 0;
		int l = balls.size();
		for (int i = 0; i< l; i++){
			balls.elementAt(i).met = false;
		}
	}
	public boolean onTouchEvent(MotionEvent event) {

		int eventAction = event.getAction();   
		float clickX = event.getX();
		float clickY = event.getY();
		switch (eventAction) {
		case MotionEvent.ACTION_DOWN:         
				int panel = paneClicked(clickX,clickY);
				
				if (paneClicked(clickX,clickY)<balls.size())
					if (paneClicked(clickX,clickY)==balls.elementAt(pointInSequence).panel){
					balls.elementAt(pointInSequence).isClicked();
					pointInSequence++;
					if(pointInSequence==balls.size()&&!testing)
					{
						message = "Follow Sequence";
						startTest();
					}
				}
				else {
					inFail = true;
					viewState = ViewState.fail;
					zombieBitmap = rand.nextInt(5);
					endFail = System.currentTimeMillis()+2000;
					failPanel = paneClicked(clickX,clickY);
					message = "OUCH!!!";
				}
				if (totalDoors > 10 && main.resourceController.slowLoad)
					exit();

			}
		return true;
	}
	private void loadBitmaps(){
		zombieBitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.zombie1));
		zombieBitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.zombie2));
		zombieBitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.zombie3));
		zombieBitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.zombie4));
		zombieBitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.zombie5));
		background = BitmapFactory.decodeResource(getResources(), R.drawable.memory_doors_bg);
	}
	private void consoleOutput(Canvas canvas){
		main.resourceController.printCenteredOnCanvas(message, canvas,main.screenWidth*.5f, main.screenHeight*.765f, 30, Color.RED);
	}
	private int paneClicked(float clickX, float clickY){
		int ret = 4;
		if (panel1.contains(clickX, clickY))
			ret = 1;
		if (panel2.contains(clickX, clickY))
			ret = 2;
		if (panel3.contains(clickX, clickY))
			ret = 3;
		if (panel0.contains(clickX, clickY))
			ret = 0;
		return ret;
	}
	private void exit(){
		main.resourceController.processEndOfView(ViewType.doors);
	}
	private void resetBalls(){
		int l = balls.size();
		for (int i = 0; i < l; i++){
			balls.elementAt(i).reset();
		}
	}
	private void drawBalls(Canvas canvas){
		int l = balls.size();
		for (int i = 0; i < l; i++){
			balls.elementAt(i).drawSelf(canvas);
		}
	}
	private void drawCompletedTestBalls(Canvas canvas){
		int l = balls.size();
		for (int i = 0; i < l; i++){
			if (balls.elementAt(i).met)
				balls.elementAt(i).drawSelf(canvas);
		}
	}
	private void appendBall(){
		int r = rand.nextInt(4);
		int lastR = balls.elementAt(balls.size()-1).panel;
		while (r == lastR)
			r = rand.nextInt(4);
		balls.add(new SignalBall(r, balls.size(), (int) (main.screenWidth*.05f), main.screenHeight*.83f));
	}
	private void drawTest(Canvas canvas){
		drawCompletedTestBalls(canvas);
		if(pointInSequence == balls.size())
		{
			message = "completedTest";
			appendBall();
			appendBall();
			resetBalls();
			testing=false;
			viewState = ViewState.learn;
			pointInSequence = 0;
		}

	}
	private void drawFail(Canvas canvas){
		if (failPanel == 0)
			canvas.drawBitmap(zombieBitmaps.elementAt(zombieBitmap), null, panel0, null);
		if (failPanel == 1)
			canvas.drawBitmap(zombieBitmaps.elementAt(zombieBitmap), null, panel1, null);
		if (failPanel == 2)
			canvas.drawBitmap(zombieBitmaps.elementAt(zombieBitmap), null, panel2, null);
		if (failPanel == 3)
			canvas.drawBitmap(zombieBitmaps.elementAt(zombieBitmap), null, panel3, null);
		if (System.currentTimeMillis() > endFail)
			inFail = false;
		message = "Ouch!";
		if (System.currentTimeMillis()>endFail)
		{
			if(testing)
				viewState = ViewState.test;
			else 
				viewState = ViewState.learn;
		}
	}
	
	private void exitView(){
		main.resourceController.processEndOfView(ViewType.doorsMemory);
	}
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(background, null, backgroundR, null);
		switch (viewState){

		case fail:
			drawFail(canvas);
			break;
		case intro1:
			message = "Follow the pattern";
			if(System.currentTimeMillis()>endIntro-2000)
				viewState = ViewState.intro2;
			break;
		case intro2:
			message = "by tapping doors";
			if(System.currentTimeMillis()>endIntro)
				viewState = ViewState.learn;
			break;
		case learn:
			message = "Follow the pattern";
			drawBalls(canvas);
			if (pointInSequence == balls.size())
				startTest();			
			break;
		case test:
			message = "Remember the pattern?";
			drawTest(canvas);
			break;
		default:
			break;
		}
		consoleOutput(canvas);
		try {  
			Thread.sleep(100);   
		} catch (InterruptedException e) { }      
		invalidate();
	}
}