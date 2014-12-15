package com.example.largeimagesdan;



import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import android.graphics.BitmapRegionDecoder;
import android.R.anim;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.RectF;

enum AnimationModel{zombie};//add names to this in or

public class Animation {
	int columns, rows;

	int spriteSheetWidth, spriteSheetHeight; 
	int frameWidth, frameHeight;

	int animationCount=1; // number of frames in a single animation, if a NSEW 3x4 animation count = 3 because three images make the animation in each of the 4 directions. 
	int north,northeast,east,southeast, south, southwest, west, northwest;


	int frames;
	int startFrame;
	Vector<Bitmap> animationFrames = new Vector<Bitmap>();

	public Animation (AnimationModel animationModel, InputStream is){
		defineAnimationAttributes(animationModel);
		createFrames(is);

	}
	private void createFrames(InputStream is){
		BitmapRegionDecoder regionDecoder = null;
		Bitmap croppedBitmap;
		Rect rect;
		int left, right, top, bottom;
		InputStream original = is;
		InputStream used;
		for (int row =0; row < rows; row++)
			for (int col = 0; col < columns; col++)
			{	
				used = original;
				left = col*frameWidth+1;
				top = row*frameHeight+1;
				right = left + frameWidth-1;
				bottom = top + frameHeight-1;
				
				rect = new Rect(left,top,right,bottom);

				try {
					regionDecoder = BitmapRegionDecoder.newInstance(used, false);
					croppedBitmap = regionDecoder.decodeRegion(rect, null);
					animationFrames.add(croppedBitmap);

				} 
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	}
	private void defineAnimationAttributes(AnimationModel animationModel){
		switch(animationModel){
		case zombie:
			spriteSheetHeight = 1024;
			spriteSheetWidth = 4068;
			frames = 9; //9
			columns = 36; //36
			rows = 8; //1
			startFrame = 1;
			west = 0;
			northwest = 1;
			north = 2;
			northeast = 3;
			east = 4;
			southeast = 5;
			south = 6;
			southwest = 7;
			
			frameWidth = spriteSheetWidth/columns;
			frameHeight = spriteSheetHeight/rows;
			break;
		}
	}
}