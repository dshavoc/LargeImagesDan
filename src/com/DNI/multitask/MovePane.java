package com.DNI.multitask;

import java.util.Random;
import java.util.Vector;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
enum PanelPosition{LEFT, CENTER, RIGHT};
enum PanelType{TARGET,COLOR,TEST};

public class MovePane {
	Rect bounds;
	public int currentNumber;
	private int currentSelectedNumbersSum;
	int targetColor = Color.BLACK; // the color of the targeting indicator
	Bitmap targetBitmap;
	Bitmap background;
	int backGroundOfTest; 
	int defaultNumber=9;
	int difficultyNumber;
	long timeLastShuffle;
	int timeForNextShuffle;
	Vector<PanelPosition> allPanelPositions = new Vector<PanelPosition>();
	Vector<PanelPosition> currentPanelPositions = new Vector<PanelPosition>();
	Vector<Bitmap> zombieBitmaps = new Vector<Bitmap>();
	Random rand = new Random();
	MovePanel targetPanel, colorPanel, testPanel;
	
	public MovePane(Rect bounds, Vector<Bitmap> zombieBitmaps, int difficultyNumber, Bitmap background){
		this.bounds = bounds;
		this.zombieBitmaps = zombieBitmaps;
		this.difficultyNumber = difficultyNumber;
		this.background = background;
		currentNumber = defaultNumber;
		shufflePanels();
		
	}
	private void setRandomPanelOrder(){
		resetAllPanelPositions();
		int originalSize = allPanelPositions.size();
		int selectedPanelNumber = 0;
		for (int i = 0; i < originalSize; i++){
			selectedPanelNumber = rand.nextInt(allPanelPositions.size());
			currentPanelPositions.add(allPanelPositions.elementAt(selectedPanelNumber));
			allPanelPositions.remove(allPanelPositions.elementAt(selectedPanelNumber));
		}
	}
	
	public boolean adjustReloadNumber(int num) {
		//TODO: Add the number passed in to currentSelectedNumbersSum in a valid manner.
		boolean ret = false;
		currentSelectedNumbersSum += num;
		if (currentNumber == currentSelectedNumbersSum){
			ret = true;
			currentSelectedNumbersSum = 0;
			currentNumber = defaultNumber;
			
		}
		if (currentSelectedNumbersSum>currentNumber){
			//reset current Number and bad stuff
			currentSelectedNumbersSum = 0;
			currentNumber = rand.nextInt(4)+5;
		}
		return ret;
	}
	
	private void resetAllPanelPositions(){
		allPanelPositions.clear();
		currentPanelPositions.clear();
		allPanelPositions.add(PanelPosition.LEFT);
		allPanelPositions.add(PanelPosition.CENTER);
		allPanelPositions.add(PanelPosition.RIGHT);
	}
	
	public void shufflePanels(){
		setRandomPanelOrder();
		timeLastShuffle = System.currentTimeMillis();
		timeForNextShuffle = rand.nextInt(500)+650;
		targetPanel = new MovePanel(PanelType.TARGET, currentPanelPositions.elementAt(0));
		colorPanel = new MovePanel(PanelType.COLOR, currentPanelPositions.elementAt(1));
		testPanel = new MovePanel(PanelType.TEST, currentPanelPositions.elementAt(2));			
	}
	
	public void updatePane(Canvas canvas){
		targetPanel.update(canvas);
		colorPanel.update(canvas);
		testPanel.update(canvas);
		if (timeLastShuffle+timeForNextShuffle<System.currentTimeMillis())
			shufflePanels();
	}
	
	public boolean processClick(int clickX, int clickY){
		boolean ret = false;
		ret = testPanel.processClick(clickX, clickY);
		return ret;
	}


	class MovePanel{
		PanelPosition panelPosition;
		PanelType panelType;
		Bitmap bitmap;
		Rect panelBounds = new Rect();
		boolean isTarget;
		Paint paint;
		private MovePanel(PanelType panelType, PanelPosition panelPosition){
			paint = new Paint();
			this.panelType = panelType;
			int third = (bounds.right-bounds.left)/3;
			switch (panelPosition){
			case CENTER:
				panelBounds.set(third, (int) (bounds.top*1.15), 2*third, bounds.bottom);
				break;
			case LEFT:
				panelBounds.set(bounds.left, (int) (bounds.top*1.15), third, bounds.bottom);
				break;
			case RIGHT:
				panelBounds.set(2*third, (int) (bounds.top*1.15), bounds.right, bounds.bottom);
				break;
			default:
				break;
			
			}
			switch(panelType){
			case COLOR:
				targetColor = chooseRandomColor();
				//currentNumber = rand.nextInt(10)+4;
				isTarget = false;
				break;
			case TEST:
				backGroundOfTest = targetColor;
				
				if (rand.nextInt(difficultyNumber*2+1)==1){ //is a good target
					 isTarget = true;
					 bitmap = targetBitmap;
				}
				else 
					while(backGroundOfTest == targetColor){
						backGroundOfTest = chooseRandomColor();
						bitmap = zombieBitmaps.elementAt(rand.nextInt(zombieBitmaps.size()));
					}
				
				break;
			case TARGET:
				targetBitmap = zombieBitmaps.elementAt(rand.nextInt(zombieBitmaps.size()));
				break;
			default:
				break;
			
			}
		}
		private int chooseRandomColor(){
			int ret = Color.BLUE; 
			int r = rand.nextInt(5);
			if (r == 0) ret = Color.BLUE;
			if (r == 1) ret = Color.RED;
			if (r == 2) ret = Color.GREEN;
			if (r == 3) ret = Color.YELLOW;
			if (r == 4) ret = Color.CYAN;
			return ret;
		}
		public void update(Canvas canvas){
			switch(panelType){
			case COLOR:
				paint.setColor(targetColor);
				paint.setTextSize((float) (bounds.height()*.5));
				paint.setTextAlign(Paint.Align.CENTER);
				//resourceController should do this...
				canvas.drawText(currentSelectedNumbersSum+"", panelBounds.centerX(), panelBounds.centerY(), paint);
				
				canvas.drawText(currentNumber+"", panelBounds.centerX(), panelBounds.centerY()+panelBounds.height()*.4f, paint);
				// a nominal new years submit.
				break;
			case TARGET:
				canvas.drawBitmap(targetBitmap, null, panelBounds, paint);
				break;
			case TEST:
				paint.setColor(backGroundOfTest);
				canvas.drawRect(panelBounds, paint);
				canvas.drawBitmap(bitmap, null, panelBounds, paint);
				break;
			default:
				break;
			
			}
			canvas.drawBitmap(background, null, bounds, null);
			
		}
		public boolean processClick(int clickX, int clickY){
			boolean ret = false;
			if (panelBounds.contains(clickX, clickY)&&isTarget)
				ret = true;
			return ret;
		}
	}
}
