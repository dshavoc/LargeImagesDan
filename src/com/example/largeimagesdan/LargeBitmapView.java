package com.example.largeimagesdan;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;
import android.graphics.BitmapRegionDecoder;

public class LargeBitmapView extends View{
	int animationCount = 0;
	Bitmap croppedBitmap;
	Animation zombieAnimation;
	Vector<AnimatedObject> animatedObjects = new Vector<AnimatedObject>();
	public LargeBitmapView(Context context) {
		super(context);
		setup();
		System.out.println("set up completed");
		// TODO Auto-generated constructor stub
	}
	
	private void setup(){
		
		InputStream is = getResources().openRawResource(R.drawable.zombie);
		zombieAnimation = new Animation(AnimationModel.zombie,is);
		System.out.println("Setup called.");
		animatedObjects.add(new Zombie(100, 100, zombieAnimation.animLayout));
	}
	
	protected void onDraw(Canvas canvas)
	{	
		
		Zombie z = (Zombie) animatedObjects.elementAt(0);
		z.update(canvas, zombieAnimation);
		try {  
			Thread.sleep(100);   
		} catch (InterruptedException e) { }      
		invalidate();
	}
}