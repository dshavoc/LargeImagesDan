package com.DNI.largeimagesdan;
import java.util.Vector;

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

public class TicTacView extends View{
	Vector<Tile> tiles = new Vector<Tile>();
	boolean inTutorial = true;
	Bitmap x;
	Bitmap o;
	Tile exitTutorialTile;
	Bitmap background;
	Bitmap redBar;
	Bitmap greenBar;
	Bitmap invalid;
	Bitmap intro;
	RectF backgroundBounds;
	RectF redBarBounds;
	RectF greenBarBounds;
	Paint paint;
	TicTacAI2 ai;
	int numberOfMoves;
	int screenWidth,screenHeight;
	MainActivity main;
	int losses;
	int gamesCompleted;
	long startTime = 0;
	long lossPenaltyEnds;
	boolean penaltyTime = false;
	boolean testFailed = false;
	public int testTime;
	public TicTacView(Context context) {
		super(context);
		float left, top, right, bottom;
		main = (MainActivity)context;
		loadBitmaps();
		paint = new Paint();
		losses = 0;
		gamesCompleted = 0;
		backgroundBounds = new RectF(0,0,main.screenWidth,main.screenHeight*.9f);
		left = 0;
		right = main.screenWidth;
		top = main.screenWidth;
		bottom = top+(main.screenHeight*.9f-main.screenWidth)/2f;
		redBarBounds   = new RectF(left,top,right,bottom);
		
		left = 0;
		right = main.screenWidth;
		top = bottom;
		bottom = main.screenHeight*.9f;
		greenBarBounds = new RectF(left,top,right,bottom);
		ai = new TicTacAI2('o');
		createNewGame();
		
		screenHeight = main.screenHeight;
		screenWidth = main.screenWidth;
	}
	private void loadBitmaps(){
		x = BitmapFactory.decodeResource(getResources(), R.drawable.xsymbol);
		o = BitmapFactory.decodeResource(getResources(), R.drawable.osymbol);
		background = BitmapFactory.decodeResource(getResources(), R.drawable.metal);
		redBar = BitmapFactory.decodeResource(getResources(), R.drawable.red);
		greenBar = BitmapFactory.decodeResource(getResources(), R.drawable.green);
		invalid = BitmapFactory.decodeResource(getResources(), R.drawable.invalid);
		intro = BitmapFactory.decodeResource(getResources(), R.drawable.introtic);
	}

	private void createNewGame(){
		ai.firstMove=10;
		gamesCompleted++;
		if (gamesCompleted>9)
			wrapUpTest();
		ai.firstMove = 10;
		exitTutorialTile = new Tile(2,2,main.screenWidth,x,o);
		
		tiles.clear();
		numberOfMoves = 0;
		for (int i = 0; i <3; i++)
			for (int j = 0; j < 3; j++)
				tiles.add(new Tile(i,j,main.screenWidth/3,x,o));
		
	}
	private void wrapUpTest(){
		long testTime = System.currentTimeMillis()-startTime;
		
		System.out.println("tic test results: time = " + testTime + " fails = " + losses);
	}
	public boolean onTouchEvent(MotionEvent event) {
		if (startTime ==0 && !inTutorial) 
			startTime = System.currentTimeMillis();
		int eventaction = event.getAction();
		int aiDecision = 10;
		if (!penaltyTime){
		switch (eventaction) {
		case MotionEvent.ACTION_DOWN: 
			// finger touches the screen
			if (!inTutorial){
				if(startTime == 0)
					startTime = System.currentTimeMillis();
				for (int i = 0; i < 9; i ++){
					Tile tile = tiles.elementAt(i);
					
					if (tile.isClicked(event.getX(), event.getY()))
					{
						if(tile.placeSymbol(ai.playerSymbol))
						{
							if (ai.firstMove == 10) ai.firstMove=i;
							numberOfMoves++;
							if(ai.checkWin(tiles,ai.playerSymbol)>0) 
								{
								createNewGame();
								}
							aiDecision = ai.AIMove(tiles);
							if (aiDecision!=99)
								tiles.elementAt(aiDecision).placeSymbol(ai.cpuSymbol);
							
							numberOfMoves++;
							if(ai.checkWin(tiles,ai.cpuSymbol)>0)
								{
								penaltyTime = true;
								lossPenaltyEnds = System.currentTimeMillis()+3000;
								losses++;
								
								createNewGame();
								}
							if (numberOfMoves>=9){
								
								createNewGame();
							}
						}
					}
				
				if (tile.symbol!=' ') System.out.println("Tile i = " + i + "is " + tile.symbol);
				}
			}
			else /// in tutorial
			{
				inTutorial = false;
			}
		
			break;
		}
		}

		// tell the system that we handled the event and no further processing is required
		return true; 
	}
	private void exit(){
		testTime = (int) (System.currentTimeMillis()-startTime);
		main.resourceController.processEndOfView(ViewType.ticTacToe);
	}
	private void drawProgressBars(Canvas canvas){
		float left, top, right, bottom;
		float redBarFill, greenBarFill;
		long now = System.currentTimeMillis();
		if (startTime != 0)
		{
			redBarFill = (now-startTime) /30000f;
			left = redBarBounds.left+redBarBounds.width()*.1f;// left
			top = redBarBounds.bottom-redBarBounds.height()*.3f; 
			right = redBarBounds.width()*.85f*redBarFill; 
			bottom = redBarBounds.bottom-redBarBounds.height()*.15f; 
			paint.setColor(Color.RED);
			canvas.drawRect(left,top,right,bottom,paint);	
			
			greenBarFill = (gamesCompleted-1-losses)/5f;
			if (greenBarFill < 0) greenBarFill = 0;
			
			left = greenBarBounds.left+greenBarBounds.width()*.1f;// left
			top = greenBarBounds.bottom-greenBarBounds.height()*.3f; 
			right = greenBarBounds.width()*.85f*greenBarFill; 
			bottom = greenBarBounds.bottom-greenBarBounds.height()*.15f; 
			paint.setColor(Color.GREEN);
			canvas.drawRect(left,top,right,bottom,paint);	
			if (redBarFill >1) 
				{
				testFailed = true;
				exit();
				}
		else if (greenBarFill >1)
			exit();
		}
	}
	
	protected void onDraw(Canvas canvas)
	{
			
			canvas.drawBitmap(background,null,backgroundBounds,null);
			canvas.drawBitmap(redBar, null, redBarBounds, null);
			canvas.drawBitmap(greenBar, null, greenBarBounds, null);
			drawProgressBars(canvas);
			if (inTutorial){
				canvas.drawBitmap(intro, null, redBarBounds, null);
			}
			if (penaltyTime){
				canvas.drawBitmap(invalid, null, redBarBounds, null);
				if (lossPenaltyEnds < System.currentTimeMillis()){
					penaltyTime = false;
				}
			}
			
			for (int i = 0; i < tiles.size(); i++){
			tiles.elementAt(i).drawSelf(canvas,i);
		}
		try {  
			Thread.sleep(50);   
		} catch (InterruptedException e) { }      
		invalidate();
		}
		
	
}

