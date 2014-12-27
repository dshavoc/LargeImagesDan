package com.example.largeimagesdan;

import java.util.Vector;
import java.util.Random;

public class TicTacAI2 {
	char cpuSymbol,playerSymbol;
	boolean isSaying = true;
	int invalidEntry = 99;
	int firstMove = invalidEntry;
	Random rand = new Random();
	public TicTacAI2(char symbol){
		cpuSymbol = symbol;
		if (cpuSymbol == 'x') playerSymbol = 'o';
		else playerSymbol = 'x';
	}

	public int AIMove(Vector<Tile> tiles){
		int ret = invalidEntry;
		ret = takeCPUWin(tiles);
		if (ret == invalidEntry)
			ret = blockOpponentWin(tiles);
		if (ret == invalidEntry)
			ret = respondToFirstMove(tiles);
		if (ret == invalidEntry)
			ret = playTowardWin(tiles);
		if (ret == invalidEntry)
			ret = fillEmptySpace(tiles);
		return ret;
	}

	int takeCPUWin(Vector<Tile> tiles){
		int ret = invalidEntry;
		char symbol = cpuSymbol;
		for (int i = 0; i < 9; i++){
			Tile tile = tiles.elementAt(i);
			if (winMove(tile, tiles, symbol)&&tile.isEmpty()){
				ret = i;
			}
		}
		if (ret!=invalidEntry)say("takeCpuWin returns " + ret);
		return ret;
	}
	int blockOpponentWin(Vector<Tile> tiles){
		int ret = invalidEntry;
		char symbol = playerSymbol;
		for (int i = 0; i < 9; i++){
			Tile tile = tiles.elementAt(i);
			if (winMove(tile, tiles, symbol)&& tile.isEmpty()){
				ret = i;
			}
		}
		if (ret!=invalidEntry)say("blockOpponentWin returns " + ret);
		return ret;
	}

	int respondToFirstMove(Vector<Tile> tiles){
		int ret = invalidEntry;
		if (firstMove != invalidEntry) // player response to opponents first move
		{
			if (firstMove == 4){
				int r = rand.nextInt(4);
				//choose a random corner
				if (r == 0){
					ret = 0;
				}
				else if (r == 1){
					ret = 2;
				}
				else if (r == 2){
					ret = 6;
				}
				else if (r == 3){
					ret = 8;
				}


			}
			if (firstMove == 0 || firstMove == 2 || firstMove == 6 || firstMove == 8){
				ret = 4;
				//more thought may be added here.
			}
			if (firstMove == 1)
				ret = 6;
			else if (firstMove == 3)
				ret = 8;
			else if (firstMove == 5)
				ret = 0;
			else if (firstMove == 7)
				ret = 2;
			
				//more thought may be added here
		}
		if(ret != invalidEntry)
			if (!tiles.elementAt(ret).isEmpty()) ret = invalidEntry;
		if (ret!=invalidEntry)say("respond to first move returns " + ret);
		return ret;
	}

	int playTowardWin(Vector<Tile> tiles){
		int ret = invalidEntry;
		char symbol = cpuSymbol;
		for (int i = 0; i < 9; i++){
			Tile hypotheticalTile = tiles.elementAt(i);
			if( hypotheticalTile.isEmpty()){
				if (checkWin(tiles, symbol)>0)
					ret = i;
				hypotheticalTile.clearTile();
			}
		}
		if (ret!=invalidEntry)say("playTowardWin returns " + ret);
		
		return ret;
	}

	private int fillEmptySpace(Vector<Tile>tiles){
		int ret = invalidEntry;
		
		if(tiles.elementAt(3).isEmpty()) return 3;
		if(tiles.elementAt(5).isEmpty()) return 5;
		if(tiles.elementAt(7).isEmpty()) return 7;
		if(tiles.elementAt(1).isEmpty()) return 1;
		if(tiles.elementAt(2).isEmpty()) return 2;
		if(tiles.elementAt(6).isEmpty()) return 6;
		if(tiles.elementAt(8).isEmpty()) return 8;
		if(tiles.elementAt(1).isEmpty()) return 4;
		if(tiles.elementAt(1).isEmpty()) return 0;
		if (ret!=invalidEntry)say("fillEmptySpace returns " + ret);
		return ret;
	}


	private boolean winMove(Tile tile, Vector<Tile>tiles, char symbol){
		boolean ret = false;
		if (tile.isEmpty())
		{
			tile.placeSymbol(symbol);
			if(checkWin(tiles, symbol)>0)
			{
				ret = true;

				tile.clearTile();
			}
			else tile.clearTile();
		}
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
	private void say(String s){
		if (isSaying)System.out.println(s);
	}
}
