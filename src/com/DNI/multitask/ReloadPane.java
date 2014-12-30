package com.DNI.multitask;

import java.util.Vector;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

public class ReloadPane {

	Rect bounds;
	Vector<NumberBubble> numbers; 
	Paint numberPaint;
	
	public ReloadPane(Rect bounds) {
		this.bounds = bounds;
		numbers = new Vector<NumberBubble>();
	}
	
	private void drawSelf(Canvas canvas) {
		
		//Draw background
		
		//Draw floating numbers
		for(int i=0; i<numbers.size(); i++) {
			numbers.elementAt(i).drawSelf(canvas);
		}
		
	}
	
	public void update(Canvas canvas) {
		
		drawSelf(canvas);
	}
	
	
	class NumberBubble {
		public int radius;
		public PointF loc;
		private PointF velocity;
		int number;
		private static final int SHADOW_OFFSET = 3;
		private boolean isUpdated;
		
		Paint shadowPaint, numberPaint, circlePaint;
		
		public NumberBubble(Point p, int radius) {
			this.radius = radius;
			loc = new PointF(p);
			velocity = new PointF(0f, 0f);
			
			shadowPaint = new Paint();
			shadowPaint.setColor(Color.DKGRAY);
			
			circlePaint = new Paint();
			circlePaint.setColor(Color.CYAN);
			
			numberPaint = new Paint();
			numberPaint.setColor(Color.BLACK);
			numberPaint.setTextSize((float) (radius * 1.6));
		}
		
		public void setNumber(int num) {
			number = num;
		}
		
		public void detectAndHandleCollisionWith(NumberBubble other) {
			PointF normal;
			float distance = PointF.length(other.loc.x - loc.x, other.loc.y - loc.y);
			float myVelocityNorm;
			float otherVelocityNorm;
			
			//Detect collision
			if(distance <= radius + other.radius) {
				//Find normal vector towards target
				normal = new PointF(other.loc.x - loc.x, other.loc.y - loc.y);
				float normalMag = normal.length();
				normal.set(normal.x/normalMag, normal.y/normalMag);		//normalize
				
				//Update my velocity
				myVelocityNorm = dotProduct(velocity, normal);
				velocity.offset( -myVelocityNorm * normal.x, -myVelocityNorm * normal.y);
				
				//Update other velocity
				otherVelocityNorm = dotProduct(other.velocity, normal);	//this normal is opposite
				other.velocity.offset(									//so this delta is, too, to compensat
					otherVelocityNorm * normal.x,
					otherVelocityNorm * normal.y
				);		
			}
		}
		
		public void detectAndHandleCollisionWithBoundary(Rect bound) {
			
			//Collision with horizontal wall
			if(loc.x <= 0 || loc.x + 2*radius >= bound.right) {
				velocity.x = -velocity.x;
			}
			if(loc.y <= 0 || loc.y + 2*radius >= bound.bottom) {
				velocity.y = -velocity.y;
			}
		}
		
		private float dotProduct(PointF a, PointF b) {
			return a.x * b.x + a.y * b.y;
		}
		
		public void updatePhysics() {
			detectAndHandleCollisionsWith
		}
		
		public void drawSelf(Canvas canvas) {
			RectF circleRect = new RectF(loc.x, loc.y, loc.x + 2*radius, loc.y + 2*radius);
			RectF shadowRect = new RectF(circleRect);
			shadowRect.offset(SHADOW_OFFSET, SHADOW_OFFSET);

			//Draw shadow
			canvas.drawOval(shadowRect, shadowPaint);
			
			//Draw circle
			canvas.drawOval(circleRect, circlePaint);
			
			//Draw number
			canvas.drawText(String.valueOf(number), circleRect.left, circleRect.top, numberPaint);
		}
	}
	
}
