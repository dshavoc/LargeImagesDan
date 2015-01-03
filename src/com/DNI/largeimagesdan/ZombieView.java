package com.DNI.largeimagesdan;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import com.example.largeimagesdan.R;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class ZombieView extends View{
	Vector<Zombie> zombies = new Vector<Zombie>();

	Bitmap zombieBitmap,cowboyBitmap,backGroundBmp;
	Animation zombieAnimation;
	Animation cowboyAnimation;
	Animation zombieStandAnimation;
	
	Cowboy player;
	int unitRadius;
	MainActivity main;
	long lastClickTime = System.currentTimeMillis()-300;

	public ZombieView(Context context) {
		super(context);
		main = (MainActivity) context;
		unitRadius = (int) (main.screenHeight*.07);
		//setup();
		
		// TODO Auto-generated constructor stub
	}
	public boolean onTouchEvent(MotionEvent event) {

		int eventAction = event.getAction();   

		switch (eventAction) {
		case MotionEvent.ACTION_DOWN:         
			Zombie zombie;
			if(isDoubleClick())
			{
				if(player.isClicked(event.getX(), event.getY())) reset();
				if (player.speed == 0) reset();

				if(!player.isShooting){
					player.shoot();
					for (int i = 0; i < zombies.size(); i++){
						zombie = zombies.elementAt(i);
						if (zombie.isClicked(event.getX(), event.getY()))
							zombie.killed();	
					}	
				}
			}
			else {
				player.setDestination((int)event.getX(), (int)event.getY());
				lastClickTime = System.currentTimeMillis();
			}
			//if(newDestination)
		}
		return true;
	}
	private boolean isDoubleClick(){
		boolean ret = false;
		//System.out.println("result = " +(System.currentTimeMillis()-lastClickTime));
		if(System.currentTimeMillis()-lastClickTime < 400)
			ret = true;
		if (ret) System.out.println("double detected");
		return ret;
	}
	private void createZombieWave(float percentageFromLeft, float percentageFromTop, int n){
		int spacer = (int) (main.screenWidth*.05);
		int verticalAdjust;
		for (int i = 0; i < n; i ++){
			verticalAdjust = (int)(i/7)*unitRadius;
			zombies.add(new Zombie(percentageFromLeft*main.screenWidth+spacer*(i%7),main.screenHeight*percentageFromTop + verticalAdjust,player,zombieAnimation));
		}
	}
	private void reset(){
		zombies.clear();
		
		if(main.zombiesLoaded){
			player = new Cowboy(main.screenWidth*.5f,main.screenHeight*.9f,cowboyAnimation);
			createZombieWave(.2f, .2f, 7);
		}
		player.radius = unitRadius*.8f;

		for (int i = 0; i < zombies.size(); i ++){
			zombies.elementAt(i).radius = unitRadius*1.5f;
		}
	}
	public void setup(){
		InputStream is = getResources().openRawResource(R.drawable.zombie);
		zombieAnimation = new Animation(AnimationModel.zombie,is);

		InputStream is2= getResources().openRawResource(R.drawable.cowboy);
		cowboyAnimation = new Animation(AnimationModel.cowboy, is2);
		
		backGroundBmp = BitmapFactory.decodeResource(getResources(), R.drawable.zombiebackground);
		backGroundBmp = Bitmap.createScaledBitmap(backGroundBmp, main.screenWidth, main.screenHeight, false);
		
		createZombieWave(main.screenWidth*.3f, main.screenHeight*.2f, 10);
	}
	private void bounceOffWalls(AnimatedObject bouncable){
		//specific data from source image
		
		
		float upperLeftX = 60;
		float lowerLeftX = 120;
		float lowerRightX = 406;
		
		float originWidth = 463;
		float originHeight = 606;
		//have to scale from image to drawn (drawn is stretched)
		float originXPos = bouncable.rx*originWidth/main.screenWidth;
		float originYPos = bouncable.ry*originHeight/main.screenHeight;
		
		
		
		//find slope for X given x = my
		double slope = (upperLeftX-lowerLeftX)/(originHeight);
		
				
		//adjustment based on y
		float adjustment = (float) (originYPos*slope);
		
		float distanceFromRightEdge = originWidth-originXPos; 
		if (originXPos<lowerLeftX+adjustment || distanceFromRightEdge<lowerLeftX+adjustment*1.1)
			bouncable.setDestination((int)(main.screenWidth*.5), (int)bouncable.ry);
		
	}
	private void detectColisions(){
		Zombie zombie, otherZombie;
		for (int zombieI = 0; zombieI < zombies.size(); zombieI++){
			{
				zombie = zombies.elementAt(zombieI);
				for (int otherZombieI = zombieI; otherZombieI < zombies.size(); otherZombieI++){
					otherZombie = zombies.elementAt(otherZombieI);
					zombie.detectCollision(otherZombie);
					bounceOffWalls(zombie);
				}
			}
		}
	}
	
	private void updateZombiesAbovePlayer(Canvas canvas){
		Zombie zombie;
		for (int i = 0; i < zombies.size(); i++){
			zombie = zombies.elementAt(i);
			bounceOffWalls(zombie);
			if (zombie.ry<=player.ry)
				zombie.update(canvas);

		}
	}
	
	private void updateZombiesBelowPlayer(Canvas canvas){
		Zombie zombie;
		for (int i = 0; i < zombies.size(); i++){
			zombie = zombies.elementAt(i);
			bounceOffWalls(zombie);
			if (zombie.ry>player.ry)
				zombie.update(canvas);
		}
	}
	private void updateDeadZombies(Canvas canvas){
		Zombie zombie;
		for (int i = 0; i < zombies.size(); i++){
			zombie = zombies.elementAt(i);
			if (!zombie.isAnimated)
				zombie.update(canvas);
		}
	}
	protected void onDraw(Canvas canvas)
	{	
		if (main.zombiesLoaded){
		canvas.drawBitmap(backGroundBmp,0,0,null);
		detectColisions();
		updateDeadZombies(canvas);
		updateZombiesAbovePlayer(canvas);
		bounceOffWalls(player);
		player.update(canvas);
		
		updateZombiesBelowPlayer(canvas);
		detectColisions();
		}
		//drawGuys(canvas);
		try {  
			Thread.sleep(100);   
		} catch (InterruptedException e) { }      
		invalidate();
	}
	

}
