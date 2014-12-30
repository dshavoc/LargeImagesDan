package com.DNI.multitask;

import java.util.Random;
import java.util.Vector;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
enum PanelPosition{LEFT, CENTER, RIGHT};
enum PanelType{TARGET,COLOR,TEST};
public class MovePane {
	Rect bounds;
	int currentNumber;
	int targetColor = Color.BLACK; // the color of the targeting indicator
	Bitmap targetBitmap;
	int backGroundOfTest; 
	int difficultyNumber;
	Vector<PanelPosition> allPanelPositions = new Vector<PanelPosition>();
	Vector<PanelPosition> currentPanelPositions = new Vector<PanelPosition>();
	Vector<Bitmap> zombieBitmaps = new Vector<Bitmap>();
	Random rand = new Random();
	MovePanel targetPanel, colorPanel, testPanel;
	
	public MovePane(Rect bounds, Vector<Bitmap> zombieBitmaps, int difficultyNumber){
		this.bounds = bounds;
		this.zombieBitmaps = zombieBitmaps;
		this.difficultyNumber = difficultyNumber;
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
	
	private void resetAllPanelPositions(){
		allPanelPositions.clear();
		currentPanelPositions.clear();
		allPanelPositions.add(PanelPosition.LEFT);
		allPanelPositions.add(PanelPosition.CENTER);
		allPanelPositions.add(PanelPosition.RIGHT);
	}
	
	public void shufflePanels(){
		setRandomPanelOrder();
		targetPanel = new MovePanel(PanelType.TARGET, currentPanelPositions.elementAt(0));
		colorPanel = new MovePanel(PanelType.COLOR, currentPanelPositions.elementAt(1));
		testPanel = new MovePanel(PanelType.TEST, currentPanelPositions.elementAt(2));			
	}
	
	public void updatePane(Canvas canvas){
		targetPanel.update(canvas);
		colorPanel.update(canvas);
		testPanel.update(canvas);
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
				panelBounds.set(third, bounds.top, 2*third, bounds.bottom);
				break;
			case LEFT:
				panelBounds.set(bounds.left, bounds.top, third, bounds.bottom);
				break;
			case RIGHT:
				panelBounds.set(2*third, bounds.top, bounds.right, bounds.bottom);
				break;
			default:
				break;
			
			}
			switch(panelType){
			case COLOR:
				targetColor = chooseRandomColor();
				currentNumber = rand.nextInt(10)+4;
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
				canvas.drawText(currentNumber+" ", panelBounds.centerX()-bounds.height()*.2f, panelBounds.centerY(), paint);
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
		}
		public boolean processClick(int clickX, int clickY){
			boolean ret = false;
			if (panelBounds.contains(clickX, clickY)&&isTarget);
				ret = true;
			return ret;
		}
	}
}
