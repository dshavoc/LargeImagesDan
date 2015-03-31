package com.DNI.largeimagesdan;


import java.io.InputStream;
import java.util.Random;
import java.util.Vector;

import com.DNI.largeimagesdan.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class ZombieView extends View{
	Vector<Zombie> zombies = new Vector<Zombie>();
	Random rand;
	Bitmap zombieBitmap,cowboyBitmap,backGroundBmp;
	Animation zombieAnimation;
	Animation cowboyAnimation;
	Animation zombieStandAnimation;
	boolean testFailed = false;
	boolean intro = true;
	Cowboy player;
	int unitRadius;
	MainActivity main;
	long lastClickTime = System.currentTimeMillis()-300;

	public ZombieView(Context context) {
		super(context);
		rand = new Random();
		System.out.println("zombie view start");
		main = (MainActivity) context;
		unitRadius = (int) (main.screenHeight*.07);
		setup();
		reset();
		
	}
	public boolean onTouchEvent(MotionEvent event) {

		int eventAction = event.getAction();   
		float clickX, clickY;
		clickX = event.getX();
		clickY = event.getY();
		
		switch (eventAction) {
		case MotionEvent.ACTION_DOWN:         
			Zombie zombie;
			float closestHitDistance=9999;
			int closestHitID=-1;
			if(isDoubleClick())
			{
				if (intro){
					if(player.isClicked(clickX, clickY))
							intro = false;
				}
				else{
				if(!player.isShooting){
					player.shoot();
					for (int i = 0; i < zombies.size(); i++){
						zombie = zombies.elementAt(i);
						if(wasShot(clickX,clickY,zombie)&&zombie.distanceFrom(player)<=closestHitDistance && zombie.isAnimated && !zombie.isDying)//here!!!!!!!!
							{
							closestHitDistance = zombie.distanceFrom(player);
							closestHitID = i;
							}
					}	
				}
				if(closestHitID !=-1){
					zombies.elementAt(closestHitID).killed();
				}
				}
			}
			else {
				player.setDestination((int)event.getX(), (int)event.getY());
				lastClickTime = System.currentTimeMillis();
			}
			//if(newDestination)
		}
		return true;
	}
	private boolean wasShot(float targetX, float targetY, Zombie zombie){
		// shot follows y = mx + b
		if (targetX-zombie.rx==0 || targetX-player.rx == 0)targetX+=.0001f;
		if (targetX-zombie.ry==0 || targetY-player.ry == 0)targetY+=.0001f;
		
		float m = (targetY-player.ry)/(targetX-player.rx);
		float mp = -1/m;
		float b = player.ry-m*player.rx;
		float bp = zombie.ry-mp*zombie.rx;
		float intersectX = (bp-b)/(m-mp);
		float intersectY = m*intersectX + b;
		double dx = zombie.rx - intersectX;
		double dy = zombie.ry - intersectY;
		float distance = (float)Math.hypot(dx, dy);
		if (distance<zombie.radius*2)
			return true;
		else 
			return false;
		// perpendicular line will be yp = -1/m (xp) + Bp 
		// person will exist on perp line if not on original line. 
		// distance is then pyth of dx and dy
		
	}
	private boolean isDoubleClick(){
		boolean ret = false;
		//System.out.println("result = " +(System.currentTimeMillis()-lastClickTime));
		if(System.currentTimeMillis()-lastClickTime < 400)
			ret = true;
		if (ret) System.out.println("double detected");
		return ret;
	}
	private void createZombieWave(float percentageFromLeft, float percentageFromTop, int n){
		int spacer = (int) (main.screenWidth*.05);
		int verticalAdjust;
		for (int i = 0; i < n; i ++){
			verticalAdjust = (int)(i/7)*unitRadius;
			zombies.add(new Zombie(percentageFromLeft*main.screenWidth+spacer*(i%7),main.screenHeight*percentageFromTop + verticalAdjust,player,zombieAnimation));
		}
	}
	private void reset(){
		zombies.clear();

		player = new Cowboy(main.screenWidth*.5f,main.screenHeight*.8f,cowboyAnimation);
		createZombieWave(.2f, .2f, 7);

		player.radius = unitRadius*.8f;

		for (int i = 0; i < zombies.size(); i ++){
			zombies.elementAt(i).radius = unitRadius*1.5f;
		}
	}
	public void setup(){
		InputStream is = getResources().openRawResource(R.drawable.zombie);
		zombieAnimation = new Animation(AnimationModel.zombie,is);

		InputStream is2= getResources().openRawResource(R.drawable.cowboy);
		cowboyAnimation = new Animation(AnimationModel.cowboy, is2);

		backGroundBmp = BitmapFactory.decodeResource(getResources(), R.drawable.zombiebackground);
		backGroundBmp = Bitmap.createScaledBitmap(backGroundBmp, main.screenWidth, main.screenHeight, false);

		player = new Cowboy(main.screenWidth*.5f,main.screenHeight*.9f,cowboyAnimation);
		createZombieWave(main.screenWidth*.3f, main.screenHeight*.2f, 10);
		
	}
	private void bounceOffWalls(AnimatedObject bouncable){
		//specific data from source image


		float upperLeftX = 60;
		float lowerLeftX = 120;
		float lowerRightX = 406;

		float originWidth = 463;
		float originHeight = 606;
		//have to scale from image to drawn (drawn is stretched)
		float originXPos = bouncable.rx*originWidth/main.screenWidth;
		float originYPos = bouncable.ry*originHeight/main.screenHeight;



		//find slope for X given x = my
		double slope = (upperLeftX-lowerLeftX)/(originHeight);


		//adjustment based on y
		float adjustment = (float) (originYPos*slope);

		float distanceFromRightEdge = originWidth-originXPos; 
		if (originXPos<lowerLeftX+adjustment || distanceFromRightEdge<lowerLeftX+adjustment*1.1)
			bouncable.setDestination((int)(main.screenWidth*.5), (int)bouncable.ry);

	}
	private void detectColisions(){
		Zombie zombie, otherZombie;
		for (int zombieI = 0; zombieI < zombies.size(); zombieI++){
			{
				zombie = zombies.elementAt(zombieI);
				for (int otherZombieI = zombieI; otherZombieI < zombies.size(); otherZombieI++){
					otherZombie = zombies.elementAt(otherZombieI);
					zombie.detectCollision(otherZombie);
					bounceOffWalls(zombie);
				}
			}
		}
	}

	private void updateZombiesAbovePlayer(Canvas canvas){
		Zombie zombie;
		for (int i = 0; i < zombies.size(); i++){
			zombie = zombies.elementAt(i);
			bounceOffWalls(zombie);
			if (zombie.ry<=player.ry)
				zombie.update(canvas);

		}
	}

	private void updateZombiesBelowPlayer(Canvas canvas){
		Zombie zombie;
		for (int i = 0; i < zombies.size(); i++){
			zombie = zombies.elementAt(i);
			bounceOffWalls(zombie);
			if (zombie.ry>player.ry)
				zombie.update(canvas);
		}
	}
	private void updateDeadZombies(Canvas canvas){
		Zombie zombie;
		for (int i = 0; i < zombies.size(); i++){
			zombie = zombies.elementAt(i);
			if (!zombie.isAnimated)
				zombie.update(canvas);
		}
	}
	private void exit(){
		main.resourceController.processEndOfView(ViewType.cowboy);
	}
	
	private void checkExitConditions(){
		if (player.isDead) {
			testFailed = true;
			exit();	
		}
		if (player.ry < main.screenHeight*.2f){
			exit();
		}
	}
	protected void onDraw(Canvas canvas)
	{	
		
		checkExitConditions();
		if (rand.nextInt(20)==0)
				{
				Zombie zombie = new Zombie(main.screenWidth*.5f,main.screenHeight*.1f,player,zombieAnimation);
				zombie.radius = zombie.radius*1.5f;
				zombies.add(zombie);
				}
			
			canvas.drawBitmap(backGroundBmp,0,0,null);
			
			updateZombiesBelowPlayer(canvas);
			detectColisions();

		//drawGuys(canvas);
		if (intro){
			main.resourceController.printOnCanvas("Destination", canvas, main.screenWidth*.35f, main.screenHeight*.13f,18, Color.WHITE);
			main.resourceController.printOnCanvas("Double Tap to Shoot.", canvas, main.screenWidth*.15f, main.screenHeight*.45f,18, Color.WHITE);
			main.resourceController.printOnCanvas("Double Tap Cowboy to start.", canvas, main.screenWidth*.15f, main.screenHeight*.55f,18, Color.WHITE);
			player.setDestination((int)player.rx, (int)player.ry);
			player.update(canvas);
		}
		else{
			detectColisions();
			updateDeadZombies(canvas);
			updateZombiesAbovePlayer(canvas);
			bounceOffWalls(player);
			player.update(canvas);

		}
			try {  
			Thread.sleep(100);   
		} catch (InterruptedException e) { }      
		invalidate();
	}


}
