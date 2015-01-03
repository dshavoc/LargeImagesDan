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

	private void createBubbles(int n){
		for (int i = 0; i < n; i++)
			addNumber(1);
	}

	public void resetBasedOnDifficulty(int difficultyLevel) {
		//TODO: Tweak all of these numbers

		setBallSpeed(8 + difficultyLevel * 4);

		//Add new set of number bubbles
		numbers.clear();
		//for(int i=0; i<(5+2*difficultyLevel); i++) {
		//addNumber(rand.nextInt(9+difficultyLevel));
		createBubbles(10);
		//}		
	}

	public void setBallSpeed(float speedInPx) {
		ballSpeed = speedInPx;// should be a percentage of screen
	}

	public void addNumber(int number) {
		int startX = rand.nextInt((int)(bounds.width()*.8))+(int)(bounds.width()*.1);
		int startY = rand.nextInt((int)(bounds.height()*.8))+(int)(bounds.height()*.1)+bounds.top;
		Point startLocation = new Point(//eventually check for if a ball is already here...
				startX,
				startY
				);
		float angle = rand.nextFloat();


		//Point location, float angle, int radius, float speed, int number
		numbers.add(new NumberBubble(startLocation, angle, ballSpeed,number));
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
					numbers.removeElementAt(i); //without your and clause above this would produce an error... 
					break;
				}
			}
			addNumber(1);
		}
		return valueClicked;
	}

	private void drawSelf(Canvas canvas) {

		//Draw background

		//Draw floating numbers
		for(int i=0; i<numbers.size(); i++) {		
			numbers.elementAt(i).update(canvas);  
		}
	}

	//Update the physics and render the canvas
	public void update(Canvas canvas) {
		int i, j;
		//Update physics for all pairs of bubbles. This will mark them updated.
		if (numbers.size()==1){
			numbers.elementAt(0).detectAndHandleCollisionWithBoundary(bounds);
		}
		else {
			for(i=0; i<numbers.size()-1; i++) {
				
				if (i == 0) numbers.elementAt(i).detectAndHandleCollisionWithBoundary(bounds); 
				
				for(j=i+1; j<numbers.size(); j++) {//update J
					numbers.elementAt(j).detectAndHandleCollisionWithBoundary(bounds);
					numbers.elementAt(i).detectAndHandleCollisionWith( numbers.elementAt(j) );
				}	
			}
	
			//Destroy all bubbles marked for death
			for(i=0; i<numbers.size(); i++) {
				if(numbers.elementAt(i).markedForDeath == true) {
					numbers.removeElementAt(i);
					i--;	//Decrement so that after i++, i will refer to the next bubble after that which was removed
				}
			}
			
			if (rand.nextInt(500)==1) addNumber(1);
			drawSelf(canvas);
		}
	}

	enum WhichBubbleRemoved {THIS, OTHER, NONE};
	
	class NumberBubble {
		public int radius;
		public PointF loc;
		private PointF velocity;
		int number;
		private static final int SHADOW_OFFSET = 3;

		//private boolean isUpdated;	//keeps track whether this bubble has been updated this tick
		// because some bubbles will be updated by other bubbles when collisions occur,
		// and two balls that collide with each other should both be updated by this event
		// only one time. If three simultaneously collide, this may create an indistinguishable
		// overlap for one or two frames. Collision with the boundary should not set this flag.
		// Superseded by markedForDeath since collision now results in destruction.
		
		public boolean markedForDeath;	//Used when looping through the vector of bubbles and this
		// bubble needs to be destroyed. It should not be destroyed immediately, else the iterator
		// will be compromised. After all bubbles have been updated, they should again be checked
		// for this flag set, and destroyed

		Paint shadowPaint, numberPaint, circlePaint;
		
		//Location in pixels, angle in range [0, 1+], radius and speed in px
		public NumberBubble(Point location, float angle, float speed, int number) { //balls speeds do not change once created...
			this.number = number; // number will signify mass
			int sizeUnit = (int)(bounds.width()*.01);
			radius = number*sizeUnit;
			//isUpdated = false; // creates item and says it has yet to be updated... RCK add 1-2
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
		
		public NumberBubble(Point location, PointF velocity,int number ){
			this.number = number; // number will signify mass
			int sizeUnit = (int)(bounds.width()*.01);
			radius = number*sizeUnit;
			//isUpdated = false; // creates item and says it has yet to be updated... RCK add 1-2
			loc = new PointF(location);
			this.velocity = velocity;
			//Randomize starting direction
			shadowPaint = new Paint();
			shadowPaint.setColor(Color.DKGRAY);
			circlePaint = new Paint();
			circlePaint.setColor(Color.CYAN);
			numberPaint = new Paint();
			numberPaint.setColor(Color.BLACK);
			numberPaint.setTextSize((float) (1.4*radius));
			numberPaint.setTextAlign(Paint.Align.CENTER);
		}

		public boolean containsPoint(int x, int y) {
			boolean ret = false;
			if (x>loc.x-radius && x < loc.x+radius && y > loc.y-radius && y < loc.y + radius) ret = true;
			return ret;
		}

		/*
		public void detectAndHandleCollisionWithDan(NumberBubble other) {
			PointF normal;
			float distance = PointF.length(other.loc.x - loc.x, other.loc.y - loc.y);
			float myVelocityNorm;
			float otherVelocityNorm;

			//if(!isUpdated) {	//Skip if this has been already been marked updated

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
			//}
		}*/
		
		public void detectAndHandleCollisionWith(NumberBubble other){
			boolean ret = false;
			float distance = PointF.length(other.loc.x - loc.x, other.loc.y - loc.y);
			if((distance <= radius + other.radius) && !markedForDeath && !other.markedForDeath) {
				numbers.add(
					new NumberBubble(
						new Point(	//location
							(int)((loc.x+other.loc.x)/2),
							(int)((loc.y+other.loc.y)/2)
						),
						new PointF(	//velocity
							(velocity.x*number + other.velocity.x*other.number)/(number+ other.number),
							(velocity.y*number + other.velocity.y*other.number)/(number+ other.number)
						),
						number+other.number // new number
					)
				);
				//numbers.remove(this);
				//numbers.remove(other);
				other.markedForDeath = true;
				this.markedForDeath = true;
			}
		}
		
		public void detectAndHandleCollisionWithBoundary(Rect bound) {

			//Collision with horizontal wall

			if(loc.x-radius < bounds.left){
				loc.x = bounds.left+radius;
				velocity.x = -velocity.x;
			}
			if (loc.x +radius > bounds.width()){
				loc.x = bounds.width()-radius;
				velocity.x = -velocity.x;
			}
			if(loc.y - radius < bound.top){
				loc.y = bound.top+radius;
				velocity.y = -velocity.y;
			}
			if (loc.y + radius > bound.bottom) {
				loc.y = bound.bottom-radius;
				velocity.y = -velocity.y;
			}
			//Deliberately did not set isUpdated here... I may be wrong.
		}

		//public void clearUpdatedState() {
		//	isUpdated = false;
		//}

		private float dotProduct(PointF a, PointF b) {
			return a.x * b.x + a.y * b.y;
		}
		private void move(){
			loc.x+=velocity.x;
			loc.y+=velocity.y;
		}

		private void drawSelf(Canvas canvas) {
			RectF circleRect = new RectF(loc.x-radius, loc.y-radius, loc.x + radius, loc.y + radius); // changed such that coordinate is of center;
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
		private void update(Canvas canvas){
			move();
			drawSelf(canvas);
		}
	}

}
