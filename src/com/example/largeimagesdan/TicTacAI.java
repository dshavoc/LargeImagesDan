package com.example.largeimagesdan;

import java.util.Vector;

public class TicTacAI {
	int noValidMove = 9;
	int [] playerWinsMadePossible = new int[9]; 
	int [] cpuWinsMadePossible = new int[9];
	int [] playerPossibleWinsThatPassThrough = new int[9];
	int [] cpuPossibleWinsThatPassThrough = new int[9];
	int [] combinedPossibleWinsThatPassThrough = new int[9];
	
	char cpuSymbol;
	char playerSymbol;
	int firstMove=10;
	int playerFirstMove = 10;
	public TicTacAI(char symbol){
		cpuSymbol = symbol;
		if (cpuSymbol == 'x') playerSymbol = 'o';
		else playerSymbol = 'x';
	}

	public int AIMove(Vector<Tile> tiles){
		boolean result = false;
		int ret = fillEmptySpace(tiles);
		int winMoveResult = winMove(tiles);
		int claimLandResult = claimLand(tiles);
		System.out.println("fillEmpty = " + ret);
		if (winMoveResult !=10) {
			ret = winMoveResult;
			System.out.println (" win result = " + ret);

			result = true;
		}
		if (!result && claimLandResult !=10){
			ret = claimLandResult;
			System.out.println("claim land result " + ret);
			result = true;
		}

		return ret;
	}
	private int winMove(Vector<Tile> tiles){
		int ret = 10;
		for (int i = 0; i < 9; i++)
		{
			Tile tile = tiles.elementAt(i);

			if (tile.isEmpty())
			{
				if(winMove(tile, tiles, cpuSymbol)) 
				{	
					System.out.println("cpu could win at " + i);
					ret = i;

				}

				if(winMove(tile, tiles, playerSymbol)) 
				{
					System.out.println("player could win at " + i);
					if (ret == 10) ret = i;
				}
				if (ret == 10){
					playerWinsMadePossible[i] =  calculateNumberOfPossibleWinsAfterMove(tile, tiles, playerSymbol);
					cpuWinsMadePossible [i] =  calculateNumberOfPossibleWinsAfterMove(tile, tiles, playerSymbol);
				}
			}
		}
		return ret;
	}


	private int claimLand(Vector<Tile>tiles){// this code is problematic...
		int ret = 10;
		int currentMax = 0;
		ret = 0;
		
		fillPotentials(tiles);
		for (int i = 0; i < 9; i++){
			if (combinedPossibleWinsThatPassThrough[i]>currentMax || (i == 4 && combinedPossibleWinsThatPassThrough[i]>currentMax) && tiles.elementAt(i).isEmpty()) ret = i;
		}
		return ret;

	}
	private int respondToFirstMove(Vector<Tile>tiles){
		int ret = 10;
		if (firstMove != 10) // player response to opponents first move
		{
			if (firstMove == 4){
				//choose a random corner
			}
			if (firstMove == 0 || firstMove == 2 || firstMove == 6 || firstMove == 8){
				
			}
		}
		return ret;
	}

	private int fillEmptySpace(Vector<Tile>tiles){
		int ret = 10;
		if(tiles.elementAt(2).isEmpty()) return 2;
		if(tiles.elementAt(6).isEmpty()) return 6;
		if(tiles.elementAt(8).isEmpty()) return 8;
		if(tiles.elementAt(3).isEmpty()) return 3;
		if(tiles.elementAt(5).isEmpty()) return 5;
		if(tiles.elementAt(7).isEmpty()) return 7;
		if(tiles.elementAt(1).isEmpty()) return 1;
		if(tiles.elementAt(1).isEmpty()) return 4;
		if(tiles.elementAt(1).isEmpty()) return 0;
		return ret;
	}

	private void fillPotentials(Vector<Tile>tiles){
		clearPotentials();

		checkDiagonals(tiles, playerSymbol);
		checkLR(tiles, playerSymbol);
		checkUD(tiles, playerSymbol);

		checkDiagonals(tiles, cpuSymbol);
		checkLR(tiles, cpuSymbol);
		checkUD(tiles, cpuSymbol);

		for (int i = 0; i < 9; i ++){
			combinedPossibleWinsThatPassThrough[i] = playerPossibleWinsThatPassThrough[i]+cpuPossibleWinsThatPassThrough[i];
			System.out.println("combined at i = " + i + " = " + combinedPossibleWinsThatPassThrough[i] );
		}
	}
	public void clearPotentials(){
		for (int i = 0; i < 9; i++){
			playerPossibleWinsThatPassThrough[i]=0;
			cpuPossibleWinsThatPassThrough[i]=0;
			combinedPossibleWinsThatPassThrough[i]=0;
		}
	}
	private void checkDiagonals(Vector<Tile>tiles, char symbol){
		char opponentSymbol;
		if (symbol == 'x') opponentSymbol = 'o'; else opponentSymbol = 'x';
		if (tiles.elementAt(0).symbol == symbol || tiles.elementAt(4).symbol == symbol || tiles.elementAt(8).symbol == symbol)
			if (tiles.elementAt(0).symbol != opponentSymbol && tiles.elementAt(4).symbol != opponentSymbol && tiles.elementAt(8).symbol != opponentSymbol)
			{
				if (tiles.elementAt(0).isEmpty()){
					if(playerSymbol == symbol) playerPossibleWinsThatPassThrough[0]++;  	
					if(cpuSymbol == symbol) cpuPossibleWinsThatPassThrough[0]++;
				}

				if (tiles.elementAt(4).isEmpty()){
					if(playerSymbol == symbol) playerPossibleWinsThatPassThrough[4]++;  	
					if(cpuSymbol == symbol) cpuPossibleWinsThatPassThrough[4]++;
				}

				if (tiles.elementAt(8).isEmpty()){
					if(playerSymbol == symbol) playerPossibleWinsThatPassThrough[8]++;  	
					if(cpuSymbol == symbol) cpuPossibleWinsThatPassThrough[8]++;
				}

			}


		if (tiles.elementAt(2).symbol == symbol || tiles.elementAt(4).symbol == symbol || tiles.elementAt(6).symbol == symbol)
			if (tiles.elementAt(2).symbol != opponentSymbol && tiles.elementAt(4).symbol != opponentSymbol && tiles.elementAt(6).symbol != opponentSymbol)
			{
				if (tiles.elementAt(2).isEmpty()){
					if(playerSymbol == symbol) playerPossibleWinsThatPassThrough[2]++;  	
					if(cpuSymbol == symbol) cpuPossibleWinsThatPassThrough[2]++;
				}

				if (tiles.elementAt(4).isEmpty()){
					if(playerSymbol == symbol) playerPossibleWinsThatPassThrough[4]++;  	
					if(cpuSymbol == symbol) cpuPossibleWinsThatPassThrough[4]++;
				}

				if (tiles.elementAt(6).isEmpty()){
					if(playerSymbol == symbol) playerPossibleWinsThatPassThrough[6]++;  	
					if(cpuSymbol == symbol) cpuPossibleWinsThatPassThrough[6]++;
				}

			}

	}
	private void checkUD(Vector<Tile>tiles, char symbol){
		char opponentSymbol;
		if (symbol == 'x') opponentSymbol = 'o'; else opponentSymbol = 'x';
		for (int i = 0; i < 9; i++)
		{
			if (tiles.elementAt(i).isEmpty()){
				for (int col = 0; col < 3; col++)
					if (tiles.elementAt(col).symbol == symbol || tiles.elementAt(col+3).symbol == symbol || tiles.elementAt(col+6).symbol == symbol)
						if (tiles.elementAt(col).symbol != opponentSymbol && tiles.elementAt(col+3).symbol != opponentSymbol && tiles.elementAt(col+6).symbol != opponentSymbol)				
						{
							if(playerSymbol == symbol) playerPossibleWinsThatPassThrough[i]++;  	
							if(cpuSymbol == symbol) cpuPossibleWinsThatPassThrough[i]++;  		
						}
			}
		}
	}
	private void checkLR(Vector<Tile>tiles, char symbol){
		char opponentSymbol;
		if (symbol == 'x') opponentSymbol = 'o'; else opponentSymbol = 'x';

		for (int i = 0; i < 9; i++)
			if (tiles.elementAt(i).isEmpty()){

				{
					for (int row = 0; row < 3; row++)
						if (tiles.elementAt(3*row).symbol == symbol || tiles.elementAt(3*row+1).symbol == symbol || tiles.elementAt(3*row+2).symbol == symbol)//invested horizontally
							if (tiles.elementAt(3*row).symbol !=opponentSymbol && tiles.elementAt(3*row+1).symbol !=opponentSymbol && tiles.elementAt(3*row+2).symbol !=opponentSymbol)//invested horizontally
							{
								if(playerSymbol == symbol) playerPossibleWinsThatPassThrough[i]++;  	
								if(cpuSymbol == symbol) cpuPossibleWinsThatPassThrough[i]++;  	
							}
				}
			}
	}
	private int calculateNumberOfPossibleWinsAfterMove(Tile tile, Vector<Tile>tiles, char symbol){
		int ret = 0;

		//fed tile is a hypothetical move
		if(tile.isEmpty())
		{
			tile.placeSymbol(symbol);

			// with the hypothetical placement of tile how many moves can now be made that lead to a win?
			Tile secondTile;
			for (int i = 0; i < 9; i ++){
				secondTile = tiles.elementAt(i);
				if (secondTile.isEmpty())
					if(winMove(secondTile, tiles, symbol))
						ret++;
			}


			tile.clearTile();
		}

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
}
