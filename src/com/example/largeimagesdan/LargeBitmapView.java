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
	Bitmap croppedBitmap;
	Animation zombieAnimation;
	public LargeBitmapView(Context context) {
		super(context);
		setup();
		// TODO Auto-generated constructor stub
	}
	
	private void setup(){
		
			InputStream is = getResources().openRawResource(R.drawable.zombie);
			zombieAnimation = new Animation(AnimationModel.zombie,is);
			System.out.println("query");
	}
	
	protected void onDraw(Canvas canvas)
	{	
		croppedBitmap = zombieAnimation.animationFrames.elementAt(0);
		canvas.drawBitmap(croppedBitmap,20,20,null); //20 20 works
		
		try {  
			Thread.sleep(100);   
		} catch (InterruptedException e) { }      
		invalidate();
	}
}