package com.DNI.largeimagesdan;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

enum Shape {RECT,OVAL}
public class DrawnObject {
	//This is a comment to test that DGK can commit a change to RCK's github project.
	//confirmed change
	//move
	//changeColor
	//changeSize
	//shape
	//velocityX
	//velocityY
	//detect collision (considers Shape)
	//name
	//posX
	//posY by center
	//update()
	int x,y;
	int width,height;
	Shape shape;
	String name = "";
	public RectF bounds;   // Needed for Canvas.drawOval
	private Paint paint;    // The paint style, color used for drawing
	int color = Color.RED;
	
	public DrawnObject(int x, int y, int width, int height, Shape shape)
	{
		this.x = x;
		this.y = y;
		
		this.width = width;
		this.height = height;
		bounds = new RectF();
		paint  = new Paint();
		paint.setColor(color);
		this.shape = shape;
	}
	public DrawnObject(int x, int y, int width, int height, Shape shape,int color)
	{
		this.x = x;
		this.y = y;
		this.color = color;
		this.width = width;
		this.height = height;
		bounds = new RectF();
		paint  = new Paint();
		paint.setColor(color);
		this.shape = shape;
	}
	public DrawnObject(int x, int y, int width, int height, Shape shape, String name)
	{
		this.x = x;
		this.y = y;
		
		this.width = width;
		this.height = height;
		bounds = new RectF();
		paint  = new Paint();
		paint.setColor(color);
		this.shape = shape;
		this.name = name;
	}

	public boolean isClicked(float cx, float cy){
		boolean returnValue = false;
		if (cx < x+width && cx > x-width && cy <y+height && cy>y) returnValue = true;
		return returnValue;
	}

	public void changeColor(int c){
		color = c;
	}
	public void reportStatus(){
		System.out.println(color);
	}
	public void draw(Canvas canvas) {
		paint.setColor(color);
		bounds.set(x, y, x+width, y+height);
		paint.setTextSize((float) (height*.25));
		//canvas.drawRect(bounds, paint);
		
		switch(shape){
		case OVAL:
			canvas.drawOval(bounds, paint);	
			break;
		case RECT:
			canvas.drawRect(bounds, paint);
			break;
		default:
			break;
		}
		paint.setColor(Color.WHITE);
		canvas.drawText(name, x+width*.3f, y+height*.5f, paint);
		
	}
}
