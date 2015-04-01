package com.DNI.multitask;

import java.util.Random;
import java.util.Vector;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

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
	RectF bulletBounds1;
	RectF bulletBounds2;
	float bulletWidth;
	
	boolean isEnemy;
	Random rand = new Random();
	private Vector<Bitmap> zombieBitmaps;
	private Vector<Bitmap> humanBitmaps;
	Bitmap shotgunShell;
	
	public FirePane(Rect bounds, Vector<Bitmap> zombieBitmaps, Vector<Bitmap> humanBitmaps, Bitmap shotgunShell, int difficultyNumber, Bitmap background){
		this.bounds = bounds;
		this.zombieBitmaps = zombieBitmaps;
		this.humanBitmaps = humanBitmaps;
		this.difficultyNumber = difficultyNumber;
		this.shotgunShell = Bitmap.createScaledBitmap(shotgunShell, (int)(bounds.width()*.05), (int)(bounds.height()*.25), false);
		this.background = background;
		bulletWidth = bounds.width()*.05f;
		
		bulletBounds1 = new RectF(
				bounds.centerX()-bulletWidth*.5f,
				bounds.centerY()+bounds.height()*.25f,
				bounds.centerX()+bulletWidth*.5f,
				bounds.centerY()+bounds.height()*.25f + bulletWidth*1.5f);
		bulletBounds2 = new RectF(
				bounds.centerX()+bulletWidth*.5f,
				bounds.centerY()+bounds.height()*.25f,
				bounds.centerX()+bulletWidth*1.5f,
				bounds.centerY()+bounds.height()*.25f + bulletWidth*1.5f);
		reloadShotgun();
	}

	private void createNewTarget(){
		timeLastCreation = System.currentTimeMillis();
		targetX =   rand.nextInt((int)(bounds.width()*.5))+(int)(bounds.width()*.1);
		targetY =   (int) (bounds.height()*.1);
		int left = targetX;
		int top = (int) (bounds.top+bounds.height()*.25);
		int right = (int) (targetX + bounds.width()*.2);
		int bottom = (int) (top + bounds.height()*.7);

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
		canvas.drawBitmap(background, null, bounds, null);
		if (numberOfShells >0){
			canvas.drawBitmap(shotgunShell, null, bulletBounds1, null);
		}
		if (numberOfShells == 2){
			canvas.drawBitmap(shotgunShell, null, bulletBounds2, null);
		}
		
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
