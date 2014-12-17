package com.example.largeimagesdan;



import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Bitmap;
import android.graphics.Rect;

enum AnimationModel{zombie};//add names to this in or

public class Animation {
	/* ASSUMPTIONS about spritesheet:
	 * Rows contain all animation frames for a single orientation
	 * Each orientation contains frames for the following five sequences: walk, pursue, attack, die(normal), die(head splat)
	 * Each sequence can have configurable number of frames that are same for all orientations/rows.
	 */
	int columns, rows;

	int spriteSheetWidth, spriteSheetHeight; 

	int frames;
	int startFrame;
	Vector<Bitmap> animationFrames = new Vector<Bitmap>();
	
	//What is this, Ian?? Does this assume each sequence is of length 3? Not the case in Zombie spritesheet. (DGK)
	int animationCount=1; // number of frames in a single animation, if a NSEW 3x4 animation count = 3 because three images make the animation in each of the 4 directions.
	
	//This class is just an organized data blob
	class AnimationLayout {
		int frameWidth, frameHeight;
		public int northRow, northEastRow, eastRow, southEastRow, southRow, southWestRow, westRow, northWestRow;	//The row (in sprite-heights, zero-based) containing all animations pertaining to given direction
		public int roamCol, pursueCol, attackCol, deathCol, headshotCol;		//The column (in sprite-widths, zero-based) where the given animation begins
		public int roamLen, pursueLen, attackLen, deathLen, headshotLen;		//The number of columns in the given sequence
	};
	AnimationLayout animLayout = new AnimationLayout();
	
	//Eventually we'll want an enum naming the 8 orientations, then we'll want to have a function that will accept
	//  a direction/orientation, an anim sequence (roam,pursue,etc), and a speed, and return the sprite to render.
	//  More detailed idea that will be simple and intuitive: create two functions:
	//  1. function startAnim(orientation, sequence, speed)
	//  2. function getAnimFrame(currentTimeMillis)

	public Animation (AnimationModel animationModel, InputStream is){
		defineAnimationAttributes(animationModel);
		createFrames(is);

	}
	private void createFrames(InputStream is){
		BitmapRegionDecoder regionDecoder = null;
		Bitmap croppedBitmap;
		Rect rect;
		//InputStream is;
		int left, right, top, bottom;

		try {
			regionDecoder = BitmapRegionDecoder.newInstance(is, false);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.out.println("Animation.createFrames(): BitmapRegionDecoder could not read the input stream!");
			e1.printStackTrace();
		}
		for (int row =0; row < rows; row++) {
			for (int col = 0; col < columns; col++)
			{	
				//used = original;
				left = col * animLayout.frameWidth + 1;
				top = row * animLayout.frameHeight + 1;
				right = left + animLayout.frameWidth;
				bottom = top + animLayout.frameHeight;
				
				rect = new Rect(left,top,right,bottom);

				croppedBitmap = regionDecoder.decodeRegion(rect, null);
				animationFrames.add(croppedBitmap);
			}
		}
	}
	private void defineAnimationAttributes(AnimationModel animationModel){
		switch(animationModel){
		case zombie:
			spriteSheetHeight = 1024;
			spriteSheetWidth = 4608;	//This was originally a typo: 4068, resulting in sprite drifting
			frames = 9; //9
			columns = 36; //36
			rows = 8; //12
			startFrame = 1;
			
			//Map rows to sprite orientation
			animLayout.westRow = 0;
			animLayout.northWestRow = 1;
			animLayout.northRow = 2;
			animLayout.northEastRow = 3;
			animLayout.eastRow = 4;
			animLayout.southEastRow = 5;
			animLayout.southRow = 6;
			animLayout.southWestRow = 7;
			
			//Map columns to sequence boundaries
			
			//mostly works... check these points one is wrong... I am out of time. 
			animLayout.roamCol = 0;			animLayout.roamLen = 4;
			animLayout.pursueCol = 3;		animLayout.pursueLen = 8;
			animLayout.attackCol = 11;		animLayout.attackLen = 10;
			animLayout.deathCol = 23;		animLayout.deathLen = 6;
			animLayout.headshotCol = 27;	animLayout.headshotLen = 8;
			
			animLayout.frameWidth = spriteSheetWidth/columns;
			animLayout.frameHeight = spriteSheetHeight/rows;
			break;
		}
	}
}