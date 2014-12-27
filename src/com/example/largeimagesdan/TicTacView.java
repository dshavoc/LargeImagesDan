package com.example.largeimagesdan;
import java.util.Vector;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

public class TicTacView extends View{
	Vector<Tile> tiles = new Vector<Tile>();
	Bitmap x;
	Bitmap o;
	Bitmap catsGame, zombieWin;
	Bitmap winDisplayBitmap;
	TicTacAI ai;
	boolean isWinDisplay=false;
	int numberOfMoves;
	
	public TicTacView(Context context) {
		super(context);
		loadBitmaps();
		ai = new TicTacAI('o');
		createNewGame();
	
	}
	private void loadBitmaps(){
		x = BitmapFactory.decodeResource(getResources(), R.drawable.xsymbol);
		o = BitmapFactory.decodeResource(getResources(), R.drawable.osymbol);
		catsGame = BitmapFactory.decodeResource(getResources(), R.drawable.cody);
		zombieWin = BitmapFactory.decodeResource(getResources(), R.drawable.zombiewin);
	}

	private void createNewGame(){
		ai.firstMove=10;
		ai.playerFirstMove = 10;
		ai.clearPotentials();
		tiles.clear();
		numberOfMoves = 0;
		for (int i = 0; i <3; i++)
			for (int j = 0; j < 3; j++)
				tiles.add(new Tile(i,j,150f,x,o));
	}

	public boolean onTouchEvent(MotionEvent event) {
		int eventaction = event.getAction();
		int aiDecision = 10;
		switch (eventaction) {
		case MotionEvent.ACTION_DOWN: 
			// finger touches the screen
			for (int i = 0; i < 9; i ++){
				Tile tile = tiles.elementAt(i);
				
				if (tile.isClicked(event.getX(), event.getY()))
				{
				
					if(tile.placeSymbol(ai.playerSymbol))
					{
						if (ai.playerFirstMove == 10) ai.playerFirstMove=i;
						numberOfMoves++;
						if(ai.checkWin(tiles,ai.playerSymbol)>0) 
							{
							if (ai.playerSymbol == 'x') 
								winDisplayBitmap=x;
							else 
								winDisplayBitmap = o;
							
							isWinDisplay = true;
							createNewGame();
							
							}
						aiDecision = ai.AIMove(tiles);
						tiles.elementAt(aiDecision).placeSymbol(ai.cpuSymbol);
						numberOfMoves++;
						if(ai.checkWin(tiles,ai.cpuSymbol)>0)
							{
							winDisplayBitmap=zombieWin;
							isWinDisplay = true;
							createNewGame();
							}
						if (numberOfMoves>=9){
							winDisplayBitmap=catsGame;
							isWinDisplay = true;
							createNewGame();
						}
					}
				}
				if (tile.symbol!=' ') System.out.println("Tile i = " + i + "is " + tile.symbol);
			}
			break;

		}

		// tell the system that we handled the event and no further processing is required
		return true; 
	}
	protected void onDraw(Canvas canvas)
	{
		if (isWinDisplay){
			canvas.drawBitmap(winDisplayBitmap, 50, 50, null);
			isWinDisplay = false;
		try {  
			Thread.sleep(700);   
		} catch (InterruptedException e) { }      
		invalidate();
		}
		else{
		for (int i = 0; i < tiles.size(); i++){
			tiles.elementAt(i).drawSelf(canvas,i);
		}
		try {  
			Thread.sleep(50);   
		} catch (InterruptedException e) { }      
		invalidate();
		}
		
	}
}

