package com.example.largeimagesdan;

import java.util.Vector;


public class TicTacAI2 {
	char cpuSymbol,playerSymbol;
	int invalidEntry = 99;
	public TicTacAI2(char symbol){
		cpuSymbol = symbol;
		if (cpuSymbol == 'x') playerSymbol = 'o';
		else playerSymbol = 'x';
	}

	public int AIMove(Vector<Tile> tiles){
		int ret = invalidEntry;
		//if move will win move there
		//if move will block opponent move go there


		return ret;
	}

	int takePlayerWin(Vector<Tile> tiles){
		int ret = invalidEntry;

		return ret;
	}
	int blockOpponentWin(Vector<Tile> tiles){
		int ret = invalidEntry;

		return ret;
	}
	
	int respondToFirstMove(int move){
		int ret = invalidEntry;

		return ret;
	}
	
	//Return a place for the AI to move that creates a fork for itself
	int lookForFork(Vector<Tile> tiles){
		int ret = invalidEntry;

		//Method A: Count imminent wins before and after moving in the i-th space
		//Method B: Return whether this space creates more than 1 imminent win
		int[] imminentWins = {0, 0, 0, 0, 0, 0, 0, 0, 0};
		for(int i = 0; (i < 9) && (ret == invalidEntry); i++) {
			if(tiles.elementAt(i).isEmpty()) {
				//Play here and count resulting imminent wins
				tiles.elementAt(i).placeSymbol(cpuSymbol);
				
				findImminentWins(tiles, imminentWins);
				
				//Look for 2+ imminent wins as a result of playing in this spot
				for(int j=0; (j<9) && (ret == invalidEntry); j++) {
					if(imminentWins[j] > 1) {
						ret = j;
					}
				}
				
				//Unplay here
				tiles.elementAt(i).clearTile();
			}
		}
		return ret;
	}
	
	void findImminentWins(Vector<Tile> tiles, int[] imminentWins) {
		/* 0 1 2
		 * 3 4 5
		 * 6 7 8
		 */
		int r, c;
		
		//Clear imminentWins
		for(int i=0; i<9; i++) imminentWins[i] = 0;
		
		for(int i = 0; i<9; i++) {
			r = i/3;	//[0,2] starting from top
			c = i%3;	//[0,2] starting from left
			if(tiles.isEmpty()) {
				//Horizontal win: If the other two in this row are mine, then increment this tile's imminent counter
				if(		tiles.elementAt((c+1)%3 + 3*r).symbol == cpuSymbol &&
						tiles.elementAt((c+2)%3 + 3*r).symbol == cpuSymbol)
				{
					imminentWins[i]++;
				}
				
				//Vertical win: If the other two tiles in this column are mine, then increment this tile's imminent counter
				if(		tiles.elementAt( c + 3*((r+1)%3) ).symbol == cpuSymbol &&
						tiles.elementAt( c + 3*((r+1)%3) ).symbol == cpuSymbol)
				{
					imminentWins[i]++;
				}
				
				//Left Diagonal:
				if( i == 0 || i == 4 || i == 8 ) {
					if( 	tiles.elementAt(( (i+1)%3 )*4).symbol == cpuSymbol &&
							tiles.elementAt(( (i+2)%3 )*4).symbol == cpuSymbol)
					{
						imminentWins[i]++;
					}
				}
				//Right Diagonal:
				if( i == 2 || i == 4 || i == 6 ) {
					if( 	tiles.elementAt(( (i+1)%3 )*2 + 2).symbol == cpuSymbol &&
							tiles.elementAt(( (i+2)%3 )*2 + 2).symbol == cpuSymbol)
					{
						imminentWins[i]++;
					}
				}
			}
			else {
				imminentWins[i] = 0;
			}
		}
	}
	
	int playTowardWin(Vector<Tile> tiles){
		int ret = invalidEntry;

		return ret;
	}
	
	
	
	int checkWin(Vector<Tile> tiles,char symbol){ //confirmed functional as one player
		int ret = 0;
		for (int row = 0; row < 3; row++)
			if (tiles.elementAt(3*row).symbol == symbol && tiles.elementAt(3*row+1).symbol == symbol&&tiles.elementAt(3*row+2).symbol == symbol)
			{
				ret++; //horizontal win
			}
		for (int col = 0; col < 3; col++)
			if (tiles.elementAt(col).symbol == symbol && tiles.elementAt(col+3).symbol == symbol &&tiles.elementAt(col+6).symbol == symbol)
			{
				ret++; //vertical win
			}

		if (tiles.elementAt(0).symbol == symbol && tiles.elementAt(4).symbol == symbol && tiles.elementAt(8).symbol == symbol)
		{
			ret++;
		}
		if (tiles.elementAt(2).symbol == symbol && tiles.elementAt(4).symbol == symbol && tiles.elementAt(6).symbol == symbol)
		{
			ret ++;
		}

		return ret;
	}
}
