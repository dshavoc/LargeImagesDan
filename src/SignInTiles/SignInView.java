package SignInTiles;

import java.util.Random;
import java.util.Vector;

import com.DNI.largeimagesdan.MainActivity;
import com.DNI.largeimagesdan.R;
import com.DNI.largeimagesdan.User;
import com.DNI.largeimagesdan.ViewType;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Align;
import android.view.MotionEvent;
import android.view.View;

public class SignInView extends View {
	boolean confirmStage = false;
	public String initials;
	private final String startingText = "Input inititials";
	Paint paint;
	long timeAtFirstLetter;
	public int timeForCompletion;
	int numberOfClears=0;
	Bitmap confirmBMP;
	RectF screenBounds;
	MainActivity main;
	int screenWidth, screenHeight;
	Vector<LetterTile> letters = new Vector<LetterTile>();
	Vector<RectF> keys = new Vector<RectF>(); 
	Random rand = new Random();
	public SignInView(Context context) {
		super(context);
		main = (MainActivity) context;
		screenWidth = main.screenWidth;
		screenHeight = main.screenHeight;
		screenBounds = new RectF(0,0,main.screenWidth,main.screenHeight*.85f);
		initials = startingText;
		paint = new Paint();
		paint.setColor(Color.BLACK);
		confirmBMP = BitmapFactory.decodeResource(getResources(), R.drawable.confirm);
		// TODO Auto-generated constructor stub
		reset();
	}
	public boolean onTouchEvent(MotionEvent event) {
		int eventaction = event.getAction();
		System.out.println("current initials = " + initials);
		switch (eventaction) {
		case MotionEvent.ACTION_DOWN: 			// finger touches the screen
			processClick(event.getX(), event.getY());
			reset();
			break;
		case MotionEvent.ACTION_UP:

			break;
		}

		// tell the system that we handled the event and no further processing is required
		return true; 
	}
	private void processClick(float clickedX, float clickedY){
		boolean exit = false;
		LetterTile tile;
		System.out.println("process click called");
		if(confirmStage){
			if(clickedY < main.screenHeight*.3f) 
				{
				timeForCompletion = (int) (System.currentTimeMillis()-timeAtFirstLetter);
				main.resourceController.processEndOfView(ViewType.signIn);
				}
			if (clickedY > main.screenHeight*.65f)
			{
				initials = "";
				numberOfClears++;
				confirmStage = false;
			}
		}
		else
		for (int i = 0; i < letters.size()&&!exit;i++){
			tile = letters.elementAt(i);
			if (tile.isClicked(clickedX, clickedY))
			{
				switch(tile.letterTileType){

				case CLEAR:
					initials = "";
					numberOfClears++;
					break;
				case LETTER:
					if (initials == startingText)
						{
						timeAtFirstLetter = System.currentTimeMillis();
						initials = tile.symbol+"";
						}
					else 
						initials += tile.symbol;
					
					break;
				case SUBMIT:
					confirmStage = true;
					//future code
					break;
				default:
					break;

				}
				exit = true;
			}
		}
	}

	private void reset(){
		createEmptyKeys();
		assignLettersToKeys();
		createClearAndSubmit();
	}

	private void createEmptyKeys(){
		int dim = screenWidth/6;
		float startingPoint = screenHeight*.3f;
		float left, top, right, bottom;
		keys.clear();
		for (int i = 0; i <26; i ++){ 
			left = i%6 * dim;
			top  = i/6 * dim + startingPoint;
			right = left + dim;
			bottom = top + dim;
			keys.add(new RectF(left,top,right,bottom));	
		}

	}
	private RectF randomKey(){
		RectF ret = null;
		ret = keys.elementAt(rand.nextInt(keys.size()));
		keys.remove(ret);
		return ret;
	}
	private void assignLettersToKeys(){
		letters.clear();
		letters.add(new LetterTile('A', randomKey()));
		letters.add(new LetterTile('B', randomKey()));
		letters.add(new LetterTile('C', randomKey()));
		letters.add(new LetterTile('D', randomKey()));
		letters.add(new LetterTile('E', randomKey()));
		letters.add(new LetterTile('F', randomKey()));
		letters.add(new LetterTile('G', randomKey()));
		letters.add(new LetterTile('H', randomKey()));
		letters.add(new LetterTile('I', randomKey()));
		letters.add(new LetterTile('J', randomKey()));
		letters.add(new LetterTile('K', randomKey()));
		letters.add(new LetterTile('L', randomKey()));
		letters.add(new LetterTile('M', randomKey()));
		letters.add(new LetterTile('N', randomKey()));
		letters.add(new LetterTile('O', randomKey()));
		letters.add(new LetterTile('P', randomKey()));
		letters.add(new LetterTile('Q', randomKey()));
		letters.add(new LetterTile('R', randomKey()));
		letters.add(new LetterTile('S', randomKey()));
		letters.add(new LetterTile('T', randomKey()));
		letters.add(new LetterTile('U', randomKey()));
		letters.add(new LetterTile('V', randomKey()));
		letters.add(new LetterTile('W', randomKey()));
		letters.add(new LetterTile('X', randomKey()));
		letters.add(new LetterTile('Y', randomKey()));
		letters.add(new LetterTile('Z', randomKey()));

	}

	private void createClearAndSubmit(){
		int dim = screenWidth/6;
		float startingPoint = screenHeight*.3f;

		RectF clearBounds,submitBounds;
		clearBounds = new RectF(
				0,					//left
				startingPoint+5*dim,//top
				2*dim,				//right
				screenHeight-10//bottom
				);

		submitBounds = new RectF(
				2*dim,
				startingPoint+4*dim,
				screenWidth,
				screenHeight-10
				);

		letters.add(new LetterTile(LetterTileType.CLEAR,clearBounds));
		letters.add(new LetterTile(LetterTileType.SUBMIT,submitBounds));


	}
	private void drawLetters(Canvas canvas){
		for (int i = 0; i < letters.size(); i++)
			letters.elementAt(i).drawSelf(canvas);
	}
	protected void onDraw(Canvas canvas) {
		if (!confirmStage)
		{drawLetters(canvas);
		
		//canvas.drawText(initials, 0, screenHeight*.2f, paint);
		main.resourceController.printOnCanvas(initials, canvas, screenWidth*.1f, screenHeight*.2f, 38, Color.BLACK);
		}
		else 
		{
			canvas.drawBitmap(confirmBMP,null,screenBounds,null);
			paint.setTextAlign(Align.CENTER);
			paint.setColor(Color.RED);
			//main.resourceController.printCenteredOnCanvas(initials, canvas, main.screenWidth*.5f, main.screenHeight*.60f, paint);
			main.resourceController.printCenteredOnCanvas(initials, canvas,  main.screenWidth*.5f, main.screenHeight*.60f, 100, Color.RED);
		}
		
		try {  
			Thread.sleep(30);   
		} catch (InterruptedException e) { }      
		invalidate();
	}
	
}
