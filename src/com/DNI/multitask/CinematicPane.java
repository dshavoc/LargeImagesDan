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
	public CinematicPane(Rect bounds, Animation cowboyAnimation, Animation zombieAnimation, int difficultyLevel){
		this.bounds = bounds;
		this.cowboyAnimation = cowboyAnimation;
		this.zombieAnimation = zombieAnimation;
		this.difficultyLevel = difficultyLevel;
		rand = new Random();
		player = new Cowboy(bounds.left+bounds.width()*.2f,bounds.centerY(),cowboyAnimation);
		player.setDestination(bounds.right, bounds.centerY());
		addZombie();
	}
	
	private void addZombie(){
		zombies.add(new Zombie(bounds.left+bounds.width()*.05f,bounds.centerY(),player,zombieAnimation));
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
		}
	}
	private void drawStills(Canvas canvas){
		for (int i = 0; i < zombies.size(); i++){
			zombies.elementAt(i).drawStill(canvas);
		}
		player.drawStill(canvas);
	}
	public void update(Canvas canvas){
		if (running){
			if (rand.nextBoolean()) running = false;
			player.update(canvas);
		}
		if (player.isShooting) {
			player.setDestination(bounds.left, bounds.centerY());
			if (System.currentTimeMillis()-timeLastUpdate>300)
				{
				timeLastUpdate = System.currentTimeMillis();
				player.update(canvas);
				}
		}
		else player.setDestination(bounds.right, bounds.centerY());
			
		if (System.currentTimeMillis()-timeLastUpdate>(5-difficultyLevel)*500)
			{
			updateZombies(canvas);
			}
		drawStills(canvas);
		
	}
	
}
