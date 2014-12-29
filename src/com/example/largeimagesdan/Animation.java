package com.example.largeimagesdan;



import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Bitmap;
import android.graphics.Rect;

enum AnimationModel{zombie,cowboy};//add names to this in or

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
	
	
	//This class is just an organized data blob
	class AnimationLayout {
		int frameWidth, frameHeight;
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
			
			animLayout.frameWidth = spriteSheetWidth/columns;
			animLayout.frameHeight = spriteSheetHeight/rows;
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
			
			animLayout.frameWidth = spriteSheetWidth/columns;
			animLayout.frameHeight = spriteSheetHeight/rows;
			
			break;
		default:
			break;
		}
	}
}