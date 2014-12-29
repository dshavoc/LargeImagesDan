package com.example.largeimagesdan;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
enum SingleAnimationModel {explosion};
public class SingleAnimation {
	int columns, rows;
	int spriteSheetWidth, spriteSheetHeight; 
	SingleAnimationModel singleAnimationModel;
	int frames;
	int startFrame;
	boolean isLooping;
	Vector<Bitmap> animationFrames = new Vector<Bitmap>();

	public SingleAnimation (SingleAnimationModel singleAnimationModel, InputStream is){
		
		defineAnimationAttributes(singleAnimationModel);
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

		int frameWidth = spriteSheetWidth/columns;
		int frameHeight = spriteSheetHeight/rows;
		for (int row =0; row < rows; row++) {
			for (int col = 0; col < columns; col++)
			{	
				//used = original;
				left = col * frameWidth + 1;
				top = row * frameHeight + 1;
				right = left + frameWidth;
				bottom = top + frameHeight;

				rect = new Rect(left,top,right,bottom);

				croppedBitmap = regionDecoder.decodeRegion(rect, null);
				animationFrames.add(croppedBitmap);
			}
		}
	}
	private void defineAnimationAttributes(SingleAnimationModel singleAnimationModel){
		switch(singleAnimationModel){
		case explosion:
			spriteSheetWidth = 320;
			spriteSheetHeight = 320;
			columns = 5;
			rows = 5;
			isLooping = false;
			
			break;
		default:
			break;
		
		}
	}
	
	
	
	
	
	
	
}
