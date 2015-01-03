package SignInTiles;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;


public class LetterTile {
	float rx,ry,boxSize,frameWidth;
	int tileId;
	Paint paint = new Paint();
	RectF size = new RectF(); //used for drawing object
	RectF frame = new RectF(); // used for drawing object
	Bitmap bitmapX;
	Bitmap bitmapY;
	int invalidResponse = 99;
	char symbol = ' ';


	public LetterTile (char s, RectF bounds){
		symbol = s;
		this.size = bounds;
		frameWidth = bounds.width()*.02f+1;
		frame.set(size.left-frameWidth, size.top-frameWidth, size.bottom+frameWidth, size.right+frameWidth);
	}
	public boolean isClicked(float clickedX, float clickedY){
		boolean ret = false;
		if (clickedX > rx && clickedX < rx+boxSize && clickedY > ry && clickedY < ry + boxSize )
			ret = true;
		return ret;
	}
	public void drawSelf(Canvas canvas){
		paint.setColor(Color.WHITE);
		canvas.drawRect(frame, paint);
		paint.setColor(Color.BLACK);
		canvas.drawRect(size, paint);
		paint.setColor(Color.BLACK);
		paint.setTextSize(size.width()*.4f);
		canvas.drawText(symbol + "", rx+boxSize*.5f, ry+boxSize*.5f, paint);
	}
}