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
	
	Bitmap zombieBitmap,cowboyBitmap, zombieStandBitmap;
	Animation zombieAnimation;
	Animation cowboyAnimation;
	Animation zombieStandAnimation;
	//Cowboy player;
	long lastClickTime = System.currentTimeMillis()-300;

	public ZombieView(Context context) {
		super(context);
		setup();
		// TODO Auto-generated constructor stub
	}
	public boolean onTouchEvent(MotionEvent event) {

		int eventAction = event.getAction();   

		switch (eventAction) {
		case MotionEvent.ACTION_DOWN:         
			if(isDoubleClick())
			{
				//player.shoot();
				//DrawnObject bullet = new DrawnObject(10,10,)
			}
			else {
				//player.setDestination((int)event.getX(), (int)event.getY());
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
	private void setup(){
		BitmapFactory.Options o=new BitmapFactory.Options();
		o.inSampleSize = 4;
		InputStream is = getResources().openRawResource(R.drawable.zombie);
		zombieBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.zombie);
		//cowboyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cowboy);
		//cowboyAnimation = new Animation(AnimationModel.cowboy, is);
		zombieAnimation = new Animation(AnimationModel.zombie,is);
		for (int i = 0; i < 3; i ++){
			zombies.add(new Zombie(200,200));
		}
		//player = new Cowboy(100,100);
		decodeResource();
	}

	private void updateZombies(Canvas canvas){
		int n = zombies.size();
		for (int i = 0; i < n; i ++){
			zombies.elementAt(i).update(canvas, zombieAnimation);
		}
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
	protected void onDraw(Canvas canvas)
	{	
		canvas.drawBitmap(zombieAnimation.animationFrames.elementAt(3),20,20,null);
		
		//player.update(canvas,cowboyAnimation);
		updateZombies(canvas);
		//drawGuys(canvas);
		try {  
			Thread.sleep(100);   
		} catch (InterruptedException e) { }      
		invalidate();
	}

}
