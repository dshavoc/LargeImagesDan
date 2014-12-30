package com.DNI.largeimagesdan;

import java.util.Vector;
import java.util.Random;

public class TicTacAI2 {
	char cpuSymbol,playerSymbol;
	boolean isSaying = true;
	int invalidEntry = 99;
	int aiStrategy = invalidEntry;
	int firstMove = invalidEntry;
	int [][] strategy = new int [10][2];
	Random rand = new Random();
	public TicTacAI2(char symbol){
		cpuSymbol = symbol;
		if (cpuSymbol == 'x') playerSymbol = 'o';
		else playerSymbol = 'x';
		fillStrategy();
		aiStrategy = rand.nextInt(10);
	}

	public int AIMove(Vector<Tile> tiles){
		//sayBoard(tiles);
		int ret = invalidEntry;
		ret = takeCPUWin(tiles);
		if (ret == invalidEntry)
			ret = blockOpponentWin(tiles);
		if (ret == invalidEntry){
			lookForFork(tiles);
		}
		if (ret == invalidEntry)
			ret = respondToFirstMove(tiles);
		if (ret == invalidEntry)
			ret = followStrategy(tiles);
		if (ret == invalidEntry)
			ret = playTowardWin(tiles);
		if (ret == invalidEntry)
			ret = fillEmptySpace(tiles);
		return ret;
	}
	private void fillStrategy(){
		strategy[0][0] = 3;
		strategy[0][1] = 2;
		
		strategy[1][0] = 3;
		strategy[1][1] = 8;
		
		strategy[2][0] = 6;
		strategy[2][1] = 2;
		
		strategy[3][0] = 3;
		strategy[3][1] = 8;
		
		strategy[4][0] = 3;
		strategy[4][1] = 7;
		
		strategy[5][0] = 1;
		strategy[5][1] = 5;
		
		strategy[6][0] = 7;
		strategy[6][1] = 5;
		
		strategy[7][0] = 0;
		strategy[7][1] = 8;
		
		strategy[8][0] = 0;
		strategy[8][1] = 5;
		
		strategy[9][0] = 4;
		strategy[9][1] = 7;
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
				ret = 2;
			else if (firstMove == 3)
				ret = 0;
			else if (firstMove == 5)
				ret = 8;
			else if (firstMove == 7)
				ret = 6;

			//more thought may be added here
		}
		if(ret != invalidEntry)
			if (!tiles.elementAt(ret).isEmpty()) ret = invalidEntry;
		if (ret!=invalidEntry)say("respond to first move returns " + ret);
		return ret;
	}
	int followStrategy(Vector<Tile> tiles){
		int ret = invalidEntry;
			if(tiles.elementAt(strategy[aiStrategy][0]).isEmpty())
				ret = strategy[aiStrategy][0];
			if(tiles.elementAt(strategy[aiStrategy][1]).isEmpty()&& tiles.elementAt(strategy[aiStrategy][0]).symbol==cpuSymbol)
				ret = strategy[aiStrategy][1];
		return ret;
	}
	//Return a place for the AI to move that creates a fork for itself
	int lookForFork(Vector<Tile> tiles){
		int ret = invalidEntry;

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
		if (ret!=invalidEntry)say("look for fork returns " + ret);
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
			if(tiles.elementAt(i).isEmpty()) {
				//Horizontal win: If the other two in this row are mine, then increment this tile's imminent counter
				if(		tiles.elementAt((c+1)%3 + 3*r).symbol == cpuSymbol &&
						tiles.elementAt((c+2)%3 + 3*r).symbol == cpuSymbol)
				{
					imminentWins[i]++;
				}

				//Vertical win: If the other two tiles in this column are mine, then increment this tile's imminent counter
				if(		tiles.elementAt( c + 3*((r+1)%3) ).symbol == cpuSymbol &&
						tiles.elementAt( c + 3*((r+2)%3) ).symbol == cpuSymbol)
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
	
//Used in setUpFork to index the possible win directions
static int[][] directionsTable = {
		{0, 1, 2},	//top row
		{3, 4, 5},	//mid row
		{6, 7, 8},	//bot row
		{0, 3, 6},	//lt  col
		{1, 4, 7},	//mid col
		{2, 5, 8},	//rt  col
		{0, 4, 8},	//lt  diag
		{2, 4, 6}	//rt  diag
};
static int NUM_DIRECTIONS = 8;

//Used in setUpFork to index all possible forks (pairs of directions, that are indices of directionsTable)
static int[][] forksTable = {
		//Intersections with top row
		{0, 3},
		{0, 4},
		{0, 5},
		{0, 6},
		{0, 7},
		//Intersections with middle row
		{1, 3},
		{1, 4},
		{1, 5},
		{1, 6},
		{1, 7},
		//Intersections with bottom row
		{2, 3},
		{2, 4},
		{2, 5},
		{2, 6},
		{2, 7},
		//Intersections of left column with the diagonals
		{3, 6},
		{3, 7},
		//Intersections of middle column with the diagonals
		{4, 6},
		{4, 7},
		//Intersections of right column with the diagonals
		{5, 6},
		{5, 7}
};
static int NUM_FORKS = 21;

	//Returns a move position that will allow the next turn to create a fork
	int setUpFork(Vector<Tile> tiles, char symbol) {
		char opponentSymbol;
		if(symbol == 'o') {
			opponentSymbol = 'x';
		}
		else {
			opponentSymbol = 'o';
		}
		/*	
		Set up fork:
		1. Identify the possible forks that have at least one of my pieces and no enemy pieces
		1.1.	Identify all directions that contain exactly one of my pieces
		1.2.	For each fork buddy of the directions (1.1), identify those that contains no enemies
			e.g. 	my piece is on 6, opponent on 7. My intersecting directions are 7 and 3.
				Fork buddies for 7 are: 0, 1, 2, 3, 4, 5
				Fork buddies for 3 are: 0, 1, 2, 6, 7
				Enemy on square 7 eliminates directions 2 and 4, leaving: 0, 1, 3, 5, 6, 7

	 	*/
		
		int i, j;	//generic counters
		
		/*
		 * Find primary direction (direction where I am invested and enemy is not)
		 */
		
		//Assume for now that this is the second move of mine, and consider only the first piece found
		int myTile = findOneOfMine(tiles, symbol);
		if(myTile == invalidEntry) {
			return invalidEntry;
		}
		
		//Find directions that intersect my tile and do not intersect enemy tiles.
		//1. Search the directionsTable for indices containing myTile
		//2. Search those directions looking for 2 empty.
		int numOwnedByMe, numOwnedByOther, numUnclaimed;
		for(i = 0; i < NUM_DIRECTIONS; i++) {
			//numOwnedByMe = 0;
			//numOwnedByOther = 0;
			numUnclaimed = 0;
			if(directionsTable[i][0] == myTile || directionsTable[i][1] == myTile || directionsTable[i][2] == myTile) {
				//Found a direction (i) my tile is in. Search this direction for two empty
				numUnclaimed = numUnclaimedInDirection(tiles, i);
				
			}

		}

		
		return 0;
	}
	
	//Retuns the position of one of the Tiles belonging to symbol. Used in setUpFork()
	int findOneOfMine(Vector<Tile> tiles, char symbol) {
		for(int i = 0; i < 9; i++) {
			if(tiles.elementAt(i).symbol == symbol) {
				return i;
			}
		}
		return invalidEntry;	//shouldn't get here
	}
	
	//Checks the directionsTable for number of tiles owned by ownerSymbol in the given dir (index of directionsTable)
	int numOwnedInDirection(Vector<Tile> tiles, char ownerSymbol, int dir) {
		int count = 0;
		for(int i = 0; i < 3; i++) {
			if(tiles.elementAt(directionsTable[dir][i]).symbol == ownerSymbol ) {
				count++;
			}
		}
		return count;
	}

	//Checks the directionsTable for number of tiles that are empty in the given dir (index of directionsTable)
	int numUnclaimedInDirection(Vector<Tile> tiles, int dir) {
		int count = 0;
		for(int i = 0; i < 3; i++) {
			if(tiles.elementAt(directionsTable[dir][i]).isEmpty() ) {
				count++;
			}
		}
		return count;
	}
	
	//This may be superceded by lookForFork
	int playTowardWin(Vector<Tile> tiles){
		int ret = invalidEntry;
		char symbol = cpuSymbol;
		int currentMax = 0;
		int winResult;
		for (int i = 0; i < 9; i++){
			{
				Tile hypotheticalTileA = tiles.elementAt(i);
				if (hypotheticalTileA.isEmpty()){
					for (int j = 0; j < 9; j++){
						Tile hypotheticalTileB = tiles.elementAt(j);
						if(hypotheticalTileB.isEmpty()){
							hypotheticalTileA.placeSymbol(symbol);
							hypotheticalTileB.placeSymbol(symbol);
							winResult = checkWin(tiles, symbol);
							if ((i == 1 || i == 3 || i == 5 || i == 7) && winResult >0) 
								winResult +=1;
							if (winResult>currentMax)
							{
								say("returns: i = " + i +  " j = " + j + " winResult = " + winResult);
								ret = i;
								currentMax = winResult;
							}
							hypotheticalTileA.clearTile();
							hypotheticalTileB.clearTile();
						}
					}
				}
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
	private void sayBoard(Vector<Tile> tiles){
		for (int i = 0; i < 3; i++)
		{
			say ("returns" + tiles.elementAt((i*3)).symbol+tiles.elementAt((i*3)+1).symbol+tiles.elementAt((i*3)+2).symbol+" ");
		}
	}
	private void say(String s){
		if (isSaying)System.out.println(s);
	}
}
