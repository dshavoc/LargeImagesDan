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
	int lookForFork(Vector<Tile> tiles){
		int ret = invalidEntry;

		return ret;
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
