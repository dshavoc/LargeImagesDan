package com.DNI.multitask;

import java.util.Random;
import java.util.Vector;

import com.DNI.largeimagesdan.Animation;
import com.DNI.largeimagesdan.Cowboy;
import com.DNI.largeimagesdan.Zombie;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class CinematicPane {
	Rect bounds;
	Random rand;
	Bitmap background;
	boolean running;
	Animation cowboyAnimation, zombieAnimation;
	Vector<Zombie>zombies = new Vector<Zombie>(0);
	Cowboy player;
	long timeLastUpdate;
	int difficultyLevel;
	int failures=0;
	public CinematicPane(Rect bounds, Animation cowboyAnimation, Animation zombieAnimation, int difficultyLevel,Bitmap background){
		this.bounds = bounds;
		this.background = background;
		this.cowboyAnimation = cowboyAnimation;
		this.zombieAnimation = zombieAnimation;
		this.difficultyLevel = difficultyLevel;
		rand = new Random();
		player = new Cowboy(bounds.left+bounds.width()*.2f,bounds.height()*.8f,cowboyAnimation);
		player.setDestination(bounds.right, (int) (bounds.height()*.9));
		player.setSpeed((int) (bounds.width()*.05));
		addZombie();
	}
	
	private void addZombie(){
		Zombie zombie = new Zombie(bounds.left+bounds.width()*.05f,bounds.height()*.8f,player,zombieAnimation);
		zombie.radius = player.radius*1.5f;
		zombies.add(zombie);
	}
	public void shootZombie(){
		Zombie zombie;
		player.shoot();	
		for (int i = 0; i < zombies.size(); i++ ){
			zombie = zombies.elementAt(i);
			if (!zombie.isDying) {
				zombie.killed();
				i = zombies.size();
			}
			
		}
		addZombie();
	}
	
	public void run(){
		running = true;
	}
	
	public void shoot(){
		if (!player.isShooting)player.shoot();
	}
	
	private void updateZombies(Canvas canvas){
		timeLastUpdate = System.currentTimeMillis();
		for (int i = 0; i < zombies.size(); i++){
			zombies.elementAt(i).update(canvas);
			if (zombies.elementAt(i).isAttacking){
				failures++;
			}
		}
	}
	private void drawStills(Canvas canvas){
		for (int i = 0; i < zombies.size(); i++){
			zombies.elementAt(i).drawStill(canvas);
		}
		player.drawStill(canvas);
	}
	public boolean update(Canvas canvas){
		boolean ret = false;
		canvas.drawBitmap(background, null, bounds, null);
		if (running){
			player.update(canvas);
			player.update(canvas);
			running = false;
		}
		if (player.isShooting) {
			player.setDestination(bounds.left, (int) (bounds.height()*.8f));
			if (System.currentTimeMillis()-timeLastUpdate>300)
				{
				timeLastUpdate = System.currentTimeMillis();
				player.update(canvas);
				}
		}
		else player.setDestination(bounds.right, (int) (bounds.height()*.8f));
			
		if (System.currentTimeMillis()-timeLastUpdate>(5-difficultyLevel)*200)
			{
			updateZombies(canvas);
			}
		drawStills(canvas);
		if(player.isAtDestination)ret = true;
		return ret;
	}
	
}
