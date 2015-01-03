package SignInTiles;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

enum LetterTileType {LETTER,CLEAR,SUBMIT}
public class LetterTile {
	LetterTileType letterTileType;
	int tileId;
	Paint paint = new Paint();
	RectF bounds = new RectF(); //used for drawing object
	char symbol = ' ';


	public LetterTile (char s, RectF bounds){
		symbol = s;
		letterTileType = LetterTileType.LETTER;
		this.bounds = bounds;
		}
	public LetterTile(LetterTileType tileType, RectF bounds){
		letterTileType = tileType;
		this.bounds = bounds;
	}
	public boolean isClicked(float clickedX, float clickedY){
		boolean ret = false;
			if (bounds.contains(clickedX, clickedY))
				ret = true;
		return ret;
	}
	public void drawSelf(Canvas canvas){
		
		paint.setColor(Color.GRAY);
		canvas.drawRect(bounds, paint);
		paint.setColor(Color.BLACK);
		paint.setTextSize(bounds.width()*.4f);
		switch (letterTileType){
		case CLEAR:
			canvas.drawText("CLR", bounds.centerX()-bounds.width()*.35f, bounds.centerY()-bounds.height()*.2f, paint);
			break;
		case LETTER:
			canvas.drawText(symbol+"",bounds.centerX()-bounds.width()*.25f,bounds.centerY()+bounds.width()*.15f,paint);
			break;
		case SUBMIT:
			paint.setTextSize(bounds.width()*.2f);
			canvas.drawText("SUBMIT", bounds.centerX()-bounds.width()*.35f, bounds.centerY()-bounds.height()*.1f, paint);
			break;
		default:
			break;
		}
	}
}