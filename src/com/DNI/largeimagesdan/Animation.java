package com.DNI.largeimagesdan;



import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;

enum AnimationModel{zombie,cowboy,lander};//add names to this in or



public class Animation {
	/* ASSUMPTIONS about spritesheet:
	 * Rows contain all animation frames for a single orientation
	 * Each orientation contains frames for the following five sequences: walk, pursue, attack, die(normal), die(head splat)
	 * Each sequence can have configurable number of frames that are same for all orientations/rows.
	 */
	int columns, rows;
	int spriteSheetWidth, spriteSheetHeight;
	
	//This value determines the region size to separate large images into before segregating them into sprites. May be device-sensitive?
	//	Works fine on Nexus 5
	final int MAX_IMAGE_REGION_PX = 1024;

	int frames;
	int startFrame;
	Vector<Bitmap> animationFrames = new Vector<Bitmap>();
	
	
	//This class is just an organized data blob
	class AnimationLayout {
		int spriteWidth, spriteHeight;
		public int northRow, northEastRow, eastRow, southEastRow, southRow, southWestRow, westRow, northWestRow;	//The row (in sprite-heights, zero-based) containing all animations pertaining to given direction
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
		Bitmap spriteBitmap, regionBitmap;
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
		
		int numPxRemainingR;
		int numPxRemainingC, numSpritesInThisRegionC;
		int numSpritesInAllPrevRegionsR, numSpritesInAllPrevRegionsC;
		int spriteR, spriteC;
		int totalCols;
		
		numPxRemainingR = spriteSheetHeight;	//Initial count. reduce as regions are processed
		int numSpritesInThisRegionR = Math.min(numPxRemainingR, MAX_IMAGE_REGION_PX) / animLayout.spriteHeight;
		numSpritesInAllPrevRegionsR = 0;
				
		//Loop over region rows while there are more
		while(numSpritesInThisRegionR > 0) {
			numPxRemainingC = spriteSheetWidth;	
			numSpritesInThisRegionC = Math.min(numPxRemainingC, MAX_IMAGE_REGION_PX) / animLayout.spriteWidth;
			numSpritesInAllPrevRegionsC = 0; 
			
			//Loop over region columns while there are more
			while(numSpritesInThisRegionC > 0) {
				//Decode this region into a bitmap
				left = numSpritesInAllPrevRegionsC * animLayout.spriteWidth;
				top = numSpritesInAllPrevRegionsR * animLayout.spriteHeight;
				right = left + numSpritesInThisRegionC * animLayout.spriteWidth - 1;
				bottom = top + numSpritesInThisRegionR * animLayout.spriteHeight - 1;
				rect = new Rect(left,top,right,bottom);
				
				regionBitmap = regionDecoder.decodeRegion(rect, null);
				
				//Extract sprites and place them properly into the vector
				for(spriteR=0; spriteR<numSpritesInThisRegionR; spriteR++) {
					for(spriteC=0; spriteC<numSpritesInThisRegionC; spriteC++) {
						//Extract sprite
						spriteBitmap = Bitmap.createBitmap(
							regionBitmap,										//src
							animLayout.spriteWidth * spriteC,					//x
							animLayout.spriteHeight * spriteR,					//y
							animLayout.spriteWidth - 1, animLayout.spriteHeight - 1		//w, h
						);
						
						//if this is the first row, then use (prev cols + this cols) as total cols
						//to calculate final target index
						if(numSpritesInAllPrevRegionsR == 0)
							totalCols = numSpritesInAllPrevRegionsC + numSpritesInThisRegionC;
						else
							totalCols = columns;
						//If this is the first column, add it to the end of the array
						if(numSpritesInAllPrevRegionsC == 0) {
							animationFrames.add(spriteBitmap);
						} else {
							//Insert image at correct location in index
							animationFrames.insertElementAt(spriteBitmap,		//Bug in this math that results in sprites out of order
								numSpritesInAllPrevRegionsC + spriteC
								+ numSpritesInAllPrevRegionsR * totalCols		//for Region 1+
								+ spriteR * numSpritesInThisRegionC		
							);
						}
					}
				}
				
				numSpritesInAllPrevRegionsC += numSpritesInThisRegionC;
				numPxRemainingC -= numSpritesInThisRegionC * animLayout.spriteWidth;
				//Load next value
				numSpritesInThisRegionC = Math.min(numPxRemainingC, MAX_IMAGE_REGION_PX) / animLayout.spriteWidth;
	 
			}
			numSpritesInAllPrevRegionsR += numSpritesInThisRegionR;
			numPxRemainingR -= numSpritesInThisRegionR * animLayout.spriteHeight;
			//Load next value
			numSpritesInThisRegionR = Math.min(numPxRemainingR, MAX_IMAGE_REGION_PX) / animLayout.spriteHeight;
 
		}
		

//		for (int row =0; row < rows; row++) {
//			for (int col = 0; col < columns; col++)
//			{	
//				//used = original;
//				left = col * animLayout.spriteWidth + 1;
//				top = row * animLayout.spriteHeight + 1;
//				right = left + animLayout.spriteWidth;
//				bottom = top + animLayout.spriteHeight;
//				
//				rect = new Rect(left,top,right,bottom);
//
//				spriteBitmap = regionDecoder.decodeRegion(rect, null);
//				animationFrames.add(spriteBitmap);
//			}
//		}
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
			
			animLayout.spriteWidth = spriteSheetWidth/columns;
			animLayout.spriteHeight = spriteSheetHeight/rows;
			break;
		case cowboy:
			spriteSheetHeight = 1017;
			spriteSheetWidth = 1792;	
			frames = 9; 
			columns = 14; 
			rows = 8;
			startFrame = 1;
			
			
			animLayout.westRow = 5;
			animLayout.northWestRow = 4;
			animLayout.northRow = 3;
			animLayout.northEastRow = 2;
			animLayout.eastRow = 1;
			animLayout.southEastRow = 0;
			animLayout.southRow = 7;
			animLayout.southWestRow = 6;
			
			animLayout.spriteWidth = spriteSheetWidth/columns;
			animLayout.spriteHeight = spriteSheetHeight/rows;
			
			break;
		case lander:
			spriteSheetHeight = 256;
			spriteSheetWidth = 164;	
			frames = 1; 
			columns = 2; 
			rows = 1;
			startFrame = 0;
			
			
			animLayout.westRow = 0;
			animLayout.northWestRow = 0;
			animLayout.northRow = 0;
			animLayout.northEastRow = 0;
			animLayout.eastRow = 0;
			animLayout.southEastRow = 0;
			animLayout.southRow = 0;
			animLayout.southWestRow = 0;
			
			animLayout.spriteWidth = spriteSheetWidth/columns;
			animLayout.spriteHeight = spriteSheetHeight/rows;
		default:
			break;
		}
	}
}