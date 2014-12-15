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
	Bitmap largeBitmap;
	Bitmap croppedBitmap;
	Vector<Bitmap> animationFrames = new Vector<Bitmap>();
	public LargeBitmapView(Context context) {
		super(context);
		setup();
		// TODO Auto-generated constructor stub
	}
	
	@SuppressLint("NewApi") 
	private void setup(){
		
		//largeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.huge,o);
		
		try {
			InputStream is = getResources().openRawResource(R.drawable.huge);
			BitmapRegionDecoder regionDecoder = BitmapRegionDecoder.newInstance(is, false);
			croppedBitmap = regionDecoder.decodeRegion(new Rect(100,100,200,200), null);
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  	
	}
	
	protected void onDraw(Canvas canvas)
	{	

		canvas.drawBitmap(croppedBitmap,20,20,null); //20 20 works
		
		try {  
			Thread.sleep(100);   
		} catch (InterruptedException e) { }      
		invalidate();
	}
}