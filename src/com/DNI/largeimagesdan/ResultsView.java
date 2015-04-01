package com.DNI.largeimagesdan;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;

public class ResultsView extends View{
	MainActivity main;
	
	public ResultsView(Context context) {
		super(context);
		main = (MainActivity) context;
		
		// TODO Auto-generated constructor stub
	}
	
	
	//tested items
		//decision making
			//multitask
			//tictac
		//reaction time
			//lander
			//planethop
			//multitask
		//memory
			//pin (new view)
		//perception
			//signin
			//multitask
			
	protected void onDraw(Canvas canvas) {
		
			try {  
				Thread.sleep(100);	//Max fps = 100, throttled down to targetFps   
			} catch (InterruptedException e) {
				System.err.println("ResultsView on Draw error");
			}
		//Draw here
		main.resourceController.printCenteredOnCanvas("Results", canvas, main.screenWidth*.5f, main.screenHeight*.2f, 30, Color.BLACK);
		
		invalidate();   

	}
}
