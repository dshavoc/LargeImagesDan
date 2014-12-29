package com.example.largeimagesdan;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

public class SingleAnimationObject {
	int animationCount;
	boolean isFinished;
	float xPos, yPos;
	int width = 300;
	int height = 300;
	SingleAnimation singleAnimation;
	
	
	public SingleAnimationObject(SingleAnimation singleAnimation,float rx, float ry) {
		// TODO 
		this.singleAnimation = singleAnimation;
		xPos = rx;
		yPos = ry;
	}
	private void drawSelf(Canvas canvas){
		Bitmap b = singleAnimation.animationFrames.elementAt(animationCount);
		b = Bitmap.createScaledBitmap(b,width, height, false);
		canvas.drawBitmap(b, xPos-.5f*width, yPos-.5f*height, null);
	}
	public void update(Canvas canvas){
		if (animationCount < singleAnimation.animationFrames.size())
			animationCount++;
		else isFinished = true;
		drawSelf(canvas);
	}
}
