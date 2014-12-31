package com.DNI.multitask;

import java.util.Random;
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
	int difficultyLevel;
	
	float ballSpeed;
	
	Random rand;
	
	public ReloadPane(Rect bounds, int difficultyLevel) {
		this.bounds = bounds;
		this.difficultyLevel = difficultyLevel;
		numbers = new Vector<NumberBubble>();
		rand = new Random();
		
		resetBasedOnDifficulty(difficultyLevel);
	}
	
	public void resetBasedOnDifficulty(int difficultyLevel) {
		//TODO: Tweak all of these numbers
		
		setBallSpeed(8 + difficultyLevel * 4);
		
		//Add new set of number bubbles
		numbers.clear();
		//for(int i=0; i<(5+2*difficultyLevel); i++) {
			//addNumber(rand.nextInt(9+difficultyLevel));
			addNumber(5);
		//}		
	}
	
	public void setBallSpeed(float speedInPx) {
		ballSpeed = speedInPx;
	}
	
	public void addNumber(int number) {
		int radius = (int)(bounds.width() * 0.04);
		Point startLocation = new Point(
			rand.nextInt( (int)(bounds.width()-2*radius) ),
			rand.nextInt( (int)(bounds.height()-2*radius) )
		);
		
		numbers.add(new NumberBubble(startLocation, number, radius, ballSpeed));
	}

	//Returns the value of the number clicked, or zero if no number clicked
	public int processClick(int clickX, int clickY) {
		int valueClicked=0;
		
		//Check to see if the click is in this pane. If not, skip next checks
		if(bounds.contains(clickX, clickY)) {

			//Check each number to see if it contains the clicked point
			for(int i=0; i<numbers.size() && valueClicked==0; i++) {
				if(numbers.elementAt(i).containsPoint(clickX, clickY)) {

					//Capture the value
					valueClicked = numbers.elementAt(i).number;
					
					//Remove the number
					numbers.removeElementAt(i);
					
					break;
				}
			}
		}
		return valueClicked;
	}
	
	private void drawSelf(Canvas canvas) {
		
		//Draw background
		
		//Draw floating numbers
		for(int i=0; i<numbers.size(); i++) {
			numbers.elementAt(i).drawSelf(canvas);
		}
	}
	
	//Update the physics and render the canvas
	public void updateAndRender(Canvas canvas) {
		int i, j;
		
		//Update physics for all pairs of bubbles. This will mark them updated.
		for(i=0; i<numbers.size()-1; i++) {
			for(j=i+1; j<numbers.size(); j++) {
				numbers.elementAt(i).detectAndHandleCollisionWith( numbers.elementAt(j) );
				numbers.elementAt(i).detectAndHandleCollisionWithBoundary(bounds);
			}
		}
		
		//Reset isUpdated state so they can be updated in next pass
		for(i=0; i<numbers.size(); i++) {
			numbers.elementAt(i).clearUpdatedState();
		}
		
		drawSelf(canvas);
	}
	
	class NumberBubble {
		public int radius;
		public PointF loc;
		private PointF velocity;
		private float speed;
		int number;
		private static final int SHADOW_OFFSET = 3;
		
		private boolean isUpdated;	//keeps track wether this bubble has been updated this tick
			// because some bubbles will be updated by other bubbles when collisions occur,
			// and two balls that collide with each other should both be updated by this event
			// only one time. If three simultaneously collide, this may create an indistinguishable
			// overlap for one or two frames. Collision with the boundary should not set this flag.
		
		Paint shadowPaint, numberPaint, circlePaint;
		
		//Location in pixels, angle in range [0, 1+], radius and speed in px
		public NumberBubble(Point location, float angle, int radius, float speed) {
			this.radius = radius;
			this.speed = speed;
			
			loc = new PointF(location);
			
			//Randomize starting direction
			velocity = new PointF(
				(float)(speed * Math.cos(angle*2*Math.PI)),
				(float)(speed * Math.sin(angle*2*Math.PI))
			);

			
			shadowPaint = new Paint();
			shadowPaint.setColor(Color.DKGRAY);
			
			circlePaint = new Paint();
			circlePaint.setColor(Color.CYAN);
			
			numberPaint = new Paint();
			numberPaint.setColor(Color.BLACK);
			numberPaint.setTextSize((float) (1.4*radius));
			numberPaint.setTextAlign(Paint.Align.CENTER);
		}
		
		public void setNumber(int num) {
			number = num;
		}
		
		public boolean containsPoint(int x, int y) {
			return Math.hypot((double)(x-loc.x-radius), (double)(y-loc.y-radius)) < radius;
		}
		
		public void detectAndHandleCollisionWith(NumberBubble other) {
			PointF normal;
			float distance = PointF.length(other.loc.x - loc.x, other.loc.y - loc.y);
			float myVelocityNorm;
			float otherVelocityNorm;
			
			if(!isUpdated) {	//Skip if this has been already been marked updated
				
				//Detect collision
				if(distance <= radius + other.radius) {
					//Find normal vector towards target
					normal = new PointF(other.loc.x - loc.x, other.loc.y - loc.y);
					float normalMag = normal.length();
					normal.set(normal.x/normalMag, normal.y/normalMag);		//normalize
					
					//Update my velocity
					myVelocityNorm = dotProduct(velocity, normal);
					velocity.offset( -myVelocityNorm * normal.x, -myVelocityNorm * normal.y);
					isUpdated = true;
					
					//Update other velocity
					otherVelocityNorm = dotProduct(other.velocity, normal);	//this normal is opposite
					other.velocity.offset(									//so this delta is, too, to compensat
						otherVelocityNorm * normal.x,
						otherVelocityNorm * normal.y
					);
					other.isUpdated = true;
				}
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
			//Deliberately did not set isUpdated here... I may be wrong.
		}
		
		public void clearUpdatedState() {
			isUpdated = false;
		}
		
		private float dotProduct(PointF a, PointF b) {
			return a.x * b.x + a.y * b.y;
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
			canvas.drawText(String.valueOf(number),
				circleRect.left+(float)(radius),
				circleRect.top+(float)(radius*1.4),
				numberPaint
			);
		}
	}
	
}
