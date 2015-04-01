package doors;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class SignalBall {
	boolean met = false;
	int panel;
	RectF bounds;
	Paint paint;
	int color;
	public SignalBall(int panel, int sequenceId, int radius, float height){
		this.panel = panel;
		paint = new Paint();
		bounds = new RectF();
		if (panel == 0) color = Color.WHITE;
		if (panel == 1) color = Color.RED;
		if (panel == 2) color = Color.BLUE;
		if (panel == 3) color = Color.rgb(102, 51, 0);
		paint.setColor(color);
		bounds.set(radius+sequenceId*2*radius-radius, height-radius, radius + sequenceId*2*radius+radius,height + radius);
	}
	public void reset(){
		met = false;
		paint.setColor(color);
	}
	public void isClicked(){
		boolean ret = false;
			met = true;
			paint.setColor(Color.GREEN);
	}
	public void drawSelf(Canvas canvas){
		
		canvas.drawOval(bounds, paint);
	}
}
