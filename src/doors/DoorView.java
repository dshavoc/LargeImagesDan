package doors;

import java.util.Random;
import java.util.Vector;

import com.DNI.largeimagesdan.MainActivity;
import com.DNI.largeimagesdan.R;
import com.DNI.largeimagesdan.ViewType;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

public class DoorView extends View{
	Random rand = new Random();
	Bitmap background;
	RectF panel0, panel1, panel2, panel3, backgroundR;
	long startTime;
	public int testTime;
	int correctDoor;
	boolean resetFlag;
	int totalDoors =0 ;
	public int wrongDoors;
	MainActivity main;
	Vector<RectF>  panes = new Vector<RectF>();
	Vector<Bitmap> zombieBitmaps = new Vector<Bitmap>();
	public DoorView(Context context) {
		super(context);
		//ca
		main = (MainActivity) context;
		panel0 = new RectF();
		panel1 = new RectF();
		panel2 = new RectF();
		panel3 = new RectF();
		backgroundR = new RectF(0,0,main.screenWidth,main.screenHeight*.9f);
		
		panel0.set(0, 0, main.screenWidth*.5f,main.screenHeight*.45f);
		panel1.set(main.screenWidth*.5f,0,main.screenWidth,main.screenHeight*.45f);
		panel2.set(0,main.screenHeight*.45f,main.screenWidth*.5f,main.screenHeight*.9f);
		panel3.set(main.screenWidth*.5f,main.screenHeight*.45f,main.screenWidth,main.screenHeight*.9f);
		panes.add(panel0);
		panes.add(panel1);
		panes.add(panel2);
		panes.add(panel3);
		
		loadBitmaps();
		// TODO Auto-generated constructor stub
	}
	public boolean onTouchEvent(MotionEvent event) {

		int eventAction = event.getAction();   

		switch (eventAction) {
		case MotionEvent.ACTION_DOWN:         
			//process click
			if (totalDoors == 0) startTime = System.currentTimeMillis();
			totalDoors++;
			if (paneClicked(event.getX(),event.getY())!=correctDoor)
				wrongDoors++;
				resetZombies();
			//call redraw
		if (totalDoors > 10)	
			{
			testTime = (int) (System.currentTimeMillis()-startTime);
			}
		if (totalDoors > 10 && main.resourceController.slowLoad)
			exit();
		
		}
		
		
		
		return true;
	}
	private void loadBitmaps(){
		zombieBitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.zombie1));
		zombieBitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.zombie2));
		zombieBitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.zombie3));
		zombieBitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.zombie4));
		zombieBitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.zombie5));
		background = BitmapFactory.decodeResource(getResources(), R.drawable.doors);
	}
	private void resetZombies(){
		correctDoor = rand.nextInt(4);
	}
	
	private void drawZombies(Canvas canvas){
		for (int i = 0; i < 4; i++){
			if (i != correctDoor)
			{
				canvas.drawBitmap(zombieBitmaps.elementAt(rand.nextInt(zombieBitmaps.size())), null, panes.elementAt(i), null);
			}
		}
	}
	private int paneClicked(float clickX, float clickY){
		int ret = 4; // 4 is invalid...
		int halfWidth = (int) (main.screenWidth*.5);
		int halfHeight = (int) (main.screenHeight*.5);
			if (clickX<halfWidth && clickY < halfHeight)
				ret = 0;
			if (clickX>=halfWidth && clickY < halfHeight)
				ret = 1;
			if (clickX<halfWidth && clickY >= halfHeight)
				ret = 2;
			if (clickX>=halfWidth && clickY >= halfHeight)
				ret = 3;
		return ret;
	}
	private void exit(){
		main.resourceController.processEndOfView(ViewType.doors);
	}
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(background, null, backgroundR, null);
		drawZombies(canvas);
		try {  
			Thread.sleep(100);   
		} catch (InterruptedException e) { }      
		invalidate();
	}

}
