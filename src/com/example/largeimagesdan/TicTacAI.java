package com.example.largeimagesdan;

import java.util.Vector;

public class TicTacAI {
	int noValidMove = 9;
	char cpuSymbol;
	char playerSymbol;
	public TicTacAI(char symbol){
		cpuSymbol = symbol;
		if (cpuSymbol == 'x') playerSymbol = 'o';
		else playerSymbol = 'x';
	}
	
	public int AIMove(Vector<Tile> tiles){
		//check for immediate win
		//check for immediate loss
		
		for (int i = 0; i < 9; i++){
			Tile tile = tiles.elementAt(i);
			if (tile.isEmpty())
			{
				tile.placeSymbol(cpuSymbol);
				if(checkWin(tiles, cpuSymbol))
				{
					System.out.println("cpu believes it won");
					tile.clearTile();
					return i;
				}
				else tile.clearTile();
			}
			if (tile.isEmpty())
			{
				tile.placeSymbol(playerSymbol);
				if(checkWin(tiles, playerSymbol))
				{
					System.out.println("cpu believes player won");
					tile.clearTile();
					
					return i;
				}
				else tile.clearTile();
			}
		}
		
		
		//set up for nextTurnWin
		
		//claim land
		if(tiles.elementAt(4).isEmpty()) return 4;
		if(tiles.elementAt(0).isEmpty()) return 0;
		if(tiles.elementAt(2).isEmpty()) return 2;
		if(tiles.elementAt(6).isEmpty()) return 6;
		if(tiles.elementAt(8).isEmpty()) return 8;
		if(tiles.elementAt(1).isEmpty()) return 1;
		if(tiles.elementAt(3).isEmpty()) return 3;
		if(tiles.elementAt(5).isEmpty()) return 5;
		if(tiles.elementAt(7).isEmpty()) return 7;
		
		return 0;
	}
	boolean checkWin(Vector<Tile> tiles,char symbol){
		boolean ret = false;
		for (int row = 0; row < 3; row++)
			if (tiles.elementAt(3*row).symbol == symbol && tiles.elementAt(3*row+1).symbol == symbol&&tiles.elementAt(3*row+2).symbol == symbol)
				{
				ret = true; //horizontal win
				}
		for (int col = 0; col < 3; col++)
			if (tiles.elementAt(col).symbol == symbol && tiles.elementAt(col+3).symbol == symbol &&tiles.elementAt(col+6).symbol == symbol)
				{
				ret = true; //vertical win
				}
		
		if (tiles.elementAt(0).symbol == symbol && tiles.elementAt(4).symbol == symbol && tiles.elementAt(8).symbol == symbol)
			{
			ret = true;
			}
		if (tiles.elementAt(2).symbol == symbol && tiles.elementAt(4).symbol == symbol && tiles.elementAt(6).symbol == symbol)
			{
			ret = true;
			}
		
		
		return ret;
	}
}
