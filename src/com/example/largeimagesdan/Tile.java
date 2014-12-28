package com.example.largeimagesdan;

import java.util.Vector;

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
	int tileId;
	Bitmap symbolBitmap = null;
	Paint paint = new Paint();
	RectF size = new RectF(); //used for drawing object
	RectF frame = new RectF(); // used for drawing object
	Bitmap bitmapX;
	Bitmap bitmapY;
	int invalidResponse = 99;
	char symbol = ' ';

	public Tile (int row, int col, float b,Bitmap bitmapX, Bitmap bitmapY){
		ry = row*b;
		rx = col*b;
		tileId = row*3+col;
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
	public boolean isForkSetup(Vector<Tile>tiles, char symbol){
		boolean ret = false;

		return ret;
	}
	public boolean isWinMove(Vector<Tile>tiles, char symbol){
		boolean ret = false;

		return ret;
	}
	public boolean isWinSetup(Vector<Tile>tiles, char symbol){
		boolean ret = false;
		int r,c;
		r = tileId/3;	//[0,2] starting from top
		c = tileId%3;
		if (tiles.elementAt((c+1)%3 + 3*r).symbol == symbol || tiles.elementAt((c+1)%3 + 3*r).symbol == symbol);
		return ret;
	}
	public int changeInWinConditionsFor(Vector<Tile>tiles, char symbol){
		int ret = invalidResponse;
			
		return ret;
	}
		
	private int numberOfWinsThatCouldPassThroughMe(Vector<Tile> tiles, int[] imminentWins, char symbol) {
		/* 0 1 2
		 * 3 4 5
		 * 6 7 8
		 */
		placeSymbol(symbol);
		int ret = 0;
		int r, c;
		//Clear imminentWins
		for(int i=0; i<9; i++) imminentWins[i] = 0;
			r = tileId/3;	//[0,2] starting from top
			c = tileId%3;	//[0,2] starting from left
			if(isEmpty()) {
				//Horizontal: opponent has not moved here and I may have.
				if(		(tiles.elementAt((c+1)%3 + 3*r).symbol == symbol || tiles.elementAt((c+1)%3 + 3*r).isEmpty() ) &&
						(tiles.elementAt((c+2)%3 + 3*r).symbol == symbol|| tiles.elementAt((c+2)%3 + 3*r).isEmpty())	)
				{
					ret++;
				}

				//Vertical: opponent has not moved here and I may have.
				if  (	(tiles.elementAt( c + 3*((r+1)%3) ).symbol == symbol || tiles.elementAt( c + 3*((r+1)%3) ).isEmpty() )&&
						(tiles.elementAt( c + 3*((r+2)%3) ).symbol == symbol || tiles.elementAt( c + 3*((r+2)%3) ).isEmpty())	)
				{
					ret++;
				}

				//Left Diagonal: same
				if( tileId == 0 || tileId == 4 || tileId == 8 ) {
					if( (tiles.elementAt(( (tileId+1)%3 )*4).symbol == symbol || tiles.elementAt(( (tileId+1)%3 )*4).isEmpty()) &&
						(tiles.elementAt(( (tileId+2)%3 )*4).symbol == symbol|| tiles.elementAt(( (tileId+2)%3 )*4).isEmpty())	)
					{
						ret++;
					}
				}
				//Right Diagonal: same
				if( tileId == 0 || tileId == 4 || tileId == 8 ) {
					if( 	(tiles.elementAt(( (tileId+1)%3 )*2).symbol == symbol || tiles.elementAt(( (tileId+1)%3 )*2).isEmpty()) &&
							(tiles.elementAt(( (tileId+2)%3 )*2).symbol == symbol)|| tiles.elementAt(( (tileId+2)%3 )*2).isEmpty())
					{
						ret++;
					}
				}
			}
			clearTile();
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
