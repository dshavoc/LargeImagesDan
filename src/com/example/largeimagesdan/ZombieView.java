package com.example.largeimagesdan;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;


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
	MainActivity main;
	long lastClickTime = System.currentTimeMillis()-300;

	public ZombieView(Context context) {
		super(context);
		main = (MainActivity) context;
		setup();
		reset();
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
	private void reset(){
		zombies.clear();

		int unitRadius = (int) (main.screenHeight*.07);	

		player = new Cowboy(main.screenWidth*.5f,main.screenHeight*.9f);
		player.radius = unitRadius*.8f;

		for (int i = 0; i < 8; i ++){
			zombies.add(new Zombie(i*main.screenWidth*.1f,main.screenHeight*.2f));
			zombies.elementAt(i).player = player;
			zombies.elementAt(i).radius = unitRadius*1.5f;
		}
	}
	private void setup(){
		InputStream is = getResources().openRawResource(R.drawable.zombie);
		zombieAnimation = new Animation(AnimationModel.zombie,is);

		InputStream is2= getResources().openRawResource(R.drawable.cowboy);
		cowboyAnimation = new Animation(AnimationModel.cowboy, is2);
		
		backGroundBmp = BitmapFactory.decodeResource(getResources(), R.drawable.zombiebackground);
		backGroundBmp = Bitmap.createScaledBitmap(backGroundBmp, main.screenWidth, main.screenHeight, false);
	}

	
	private void decodeResource(){
		//		BitmapRegionDecoder decoder = null;
		//		try{
		//			decoder = BitmapRegionDecoder.newInstance("res/drawable/zombie.png", false);
		//			aZombieBitmap = decoder.decodeRegion(new Rect(30,30, (20),(20)), null);
		//		} 
		//		catch (IOException e) {
		//			System.out.println("fucked");
		//			e.printStackTrace();
		//		}
		try {
			FileInputStream in = new FileInputStream("res/drawable/zombie.png");
			BufferedInputStream buf = new BufferedInputStream(in);
			byte[] bMapArray= new byte[buf.available()];
			buf.read(bMapArray);
			//        aZombieBitmap = BitmapFactory.decodeByteArray(bMapArray, 0, 40);

			if (in != null) {
				in.close();
			}
			if (buf != null) {
				buf.close();
			}
		} catch (Exception e) {
			Log.e("Error reading file", e.toString());
		}


	}
	private void detectColisions(){
		Zombie zombie, otherZombie;
		for (int zombieI = 0; zombieI < zombies.size(); zombieI++){
			{
				zombie = zombies.elementAt(zombieI);
				for (int otherZombieI = zombieI; otherZombieI < zombies.size(); otherZombieI++){
					otherZombie = zombies.elementAt(otherZombieI);
					zombie.detectCollision(otherZombie);
				}
			}
		}
	}
	
	private void updateZombiesAbovePlayer(Canvas canvas, Animation animation){
		Zombie zombie;
		for (int i = 0; i < zombies.size(); i++){
			zombie = zombies.elementAt(i);
			if (zombie.ry<=player.ry)
				zombie.update(canvas, animation);
		}
	}
	private void updateZombiesBelowPlayer(Canvas canvas, Animation animation){
		Zombie zombie;
		for (int i = 0; i < zombies.size(); i++){
			zombie = zombies.elementAt(i);
			if (zombie.ry>player.ry)
				zombie.update(canvas, animation);
		}
	}
	private void updateDeadZombies(Canvas canvas, Animation animation){
		Zombie zombie;
		for (int i = 0; i < zombies.size(); i++){
			zombie = zombies.elementAt(i);
			if (!zombie.isAnimated)
				zombie.update(canvas, animation);
		}
	}
	protected void onDraw(Canvas canvas)
	{	
		canvas.drawBitmap(backGroundBmp,0,0,null);
		detectColisions();
		updateDeadZombies(canvas, zombieAnimation);
		updateZombiesAbovePlayer(canvas, zombieAnimation);
		player.update(canvas,cowboyAnimation);
		updateZombiesBelowPlayer(canvas, zombieAnimation);
		detectColisions();
		//drawGuys(canvas);
		try {  
			Thread.sleep(100);   
		} catch (InterruptedException e) { }      
		invalidate();
	}

}
