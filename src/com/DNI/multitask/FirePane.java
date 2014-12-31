package com.DNI.multitask;

import java.util.Random;
import java.util.Vector;

import com.DNI.multitask.MovePane.MovePanel;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import android.graphics.Rect;



public class FirePane {
	Rect bounds;
	Rect targetBounds;
	Bitmap background;
	Bitmap target;
	long timeLastCreation;
	int difficultyNumber;
	int targetX, targetY;
	int numberOfShells;

	boolean isEnemy;
	Random rand = new Random();
	private Vector<Bitmap> zombieBitmaps;
	private Vector<Bitmap> humanBitmaps;
	Bitmap shotgunShell;

	public FirePane(Rect bounds, Vector<Bitmap> zombieBitmaps, Vector<Bitmap> humanBitmaps, Bitmap shotgunShell, int difficultyNumber){
		this.bounds = bounds;
		this.zombieBitmaps = zombieBitmaps;
		this.humanBitmaps = humanBitmaps;
		this.difficultyNumber = difficultyNumber;
		this.shotgunShell = Bitmap.createScaledBitmap(shotgunShell, (int)(bounds.width()*.05), (int)(bounds.height()*.25), false);
		reloadShotgun();
	}

	private void createNewTarget(){
		timeLastCreation = System.currentTimeMillis();
		targetX =   rand.nextInt((int)(bounds.width()*.5))+(int)(bounds.width()*.1);
		targetY =   (int) (bounds.height()*.1);
		int left = targetX;
		int top = (int) (bounds.top+bounds.height()*.1);
		int right = (int) (targetX + bounds.width()*.2);
		int bottom = (int) (top + bounds.height()*.8);

		targetBounds = new Rect(left,top,right,bottom);
		if (rand.nextBoolean())
		{
			isEnemy = true;
			target = zombieBitmaps.elementAt(rand.nextInt(zombieBitmaps.size()));
		}
		else{
			isEnemy = false;
			target = humanBitmaps.elementAt(rand.nextInt(humanBitmaps.size()));
		}
	}
	public void reloadShotgun(){
		numberOfShells = 2;
	}
	public void update(Canvas canvas){
		if (System.currentTimeMillis()-timeLastCreation>(10-difficultyNumber)*100)
			createNewTarget();
		canvas.drawBitmap(target, null, targetBounds, null);
		for (int i = 0; i <numberOfShells; i++)
			canvas.drawBitmap(shotgunShell, bounds.right*(.85f+i*.05f), bounds.top+ bounds.height()*.1f, null);

	}
	public boolean processClick(int clickX, int clickY){
		boolean ret = false;
		if (bounds.contains(clickX, clickY))
			if (numberOfShells>0){
				numberOfShells--;
				if (targetBounds.contains(clickX, clickY)&&isEnemy){
					ret = true;
					System.out.println("correct click");
				}
			}
		return ret;
	}

}
