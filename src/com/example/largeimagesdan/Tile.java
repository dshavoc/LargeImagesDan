package com.example.largeimagesdan;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.graphics.RectF;

enum HorizontalAlignment{left,middle,right}
enum VerticalAlignment{top,middle,bottom}
public class Tile {
	HorizontalAlignment horizontalAlignment;
	VerticalAlignment verticalAlignment;
	float rx,ry,boxSize,frameWidth = 5;
	Bitmap symbolBitmap = null;
	Paint paint = new Paint();
	RectF size = new RectF(); //used for drawing object
	RectF frame = new RectF(); // used for drawing object
	Bitmap bitmapX;
	Bitmap bitmapY;
	char symbol = ' ';

	public Tile (int row, int col, float b,Bitmap bitmapX, Bitmap bitmapY){
		ry = row*b;
		rx = col*b;
		
		this.bitmapX = bitmapX;
		this.bitmapY = bitmapY;
		
		if (col == 0) horizontalAlignment = HorizontalAlignment.left;
		if (col == 1) horizontalAlignment = HorizontalAlignment.middle;
		if (col == 2) horizontalAlignment = HorizontalAlignment.right;

		if (row == 0) verticalAlignment = VerticalAlignment.top;
		if (row == 1) verticalAlignment = VerticalAlignment.middle;
		if (row == 2) verticalAlignment = VerticalAlignment.bottom;

		boxSize = b;
		frame.set(rx, ry, rx+boxSize, ry+boxSize);
		size.set(rx+frameWidth, ry+frameWidth, rx+boxSize-frameWidth, ry+boxSize-frameWidth);


	}
	public boolean isClicked(HorizontalAlignment h, VerticalAlignment v){
		boolean ret = false;
		if (h == horizontalAlignment && v == verticalAlignment )
			ret = true;
		return ret;
	}

	public boolean isClicked(float clickedX, float clickedY){
		boolean ret = false;
		if (clickedX > rx && clickedX < rx+boxSize && clickedY > ry && clickedY < ry + boxSize )
			ret = true;
		return ret;
	}

	public boolean placeSymbol(char xo){
		boolean ret = false;
		Bitmap b = null;
		if (symbolBitmap == null)
		{
			symbol = xo;
			if (symbol == 'x') 
				b = bitmapX;
			if (symbol == 'o') 
				b = bitmapY;
			
			symbolBitmap = Bitmap.createScaledBitmap(b, (int)boxSize, (int)boxSize, true);
			
			ret = true;
		}
		
		return ret;
	}
	public boolean isEmpty(){
		boolean ret = true;
			if (symbol !=' ') ret = false;
		return ret;
	}
	public void clearTile(){
		symbolBitmap = null;
		symbol = ' ';
	}

	public void drawSelf(Canvas canvas, int i){
		paint.setColor(Color.WHITE);
		canvas.drawRect(frame, paint);
		paint.setColor(Color.BLACK);
		canvas.drawRect(size, paint);
		paint.setColor(Color.YELLOW);
		paint.setTextSize(30);
		canvas.drawText(Integer.toString(i), rx+boxSize*.5f, ry+boxSize*.5f, paint);
		if (symbolBitmap != null)
		{
			canvas.drawBitmap(symbolBitmap, rx, ry, null);
		}
	}

}
