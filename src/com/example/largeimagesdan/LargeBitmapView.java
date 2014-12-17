package com.example.largeimagesdan;

import java.io.InputStream;
import java.util.Vector;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

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
		for (int i = 0; i <5; i ++)
			animatedObjects.add(new Zombie(i*30, 100, zombieAnimation.animLayout));
	}
	private void drawZombies(Canvas canvas){
		for (int i = 0; i < animatedObjects.size(); i++)
		{
			Zombie z = (Zombie) animatedObjects.elementAt(i);
			z.update(canvas, zombieAnimation);
			
		}
	}
	protected void onDraw(Canvas canvas)
	{	
		drawZombies(canvas);
		try {  
			Thread.sleep(100);   
		} catch (InterruptedException e) { }      
		invalidate();
	}
}