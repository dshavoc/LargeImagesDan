package com.DNI.largeimagesdan;

import com.DNI.PlanetHop.PlanetHopView;
import com.DNI.multitask.MultiTaskView;

import doors.DoorView;

import android.content.Context;
import SignInTiles.SignInView;

public class ResourceController {
	private static ResourceController instance = null;
	public SignInView signInView;
	public TicTacView ticTacView;
	public DoorView doorView;
	public ZombieView zombieView;
	public LanderView landerView;
	public MultiTaskView multiTaskView;
	public PlanetHopView planetHopView;
	boolean calibration = true; //needs to be updated later.
	boolean quickLoad = false;
	public boolean slowLoad = false;
	long loadStart, quickLoadSpeed, slowLoadSpeed;
	MainActivity main;
	DatabaseManager dbm;
	public User user;
	private static String DBNAME = "DBreath.db";    // THIS IS THE SQLITE DATABASE FILE NAME
	
	private ResourceController(Context context){
		loadStart = System.currentTimeMillis();
		main = (MainActivity)context;
		loadSlowShit();
		signInView = new SignInView(main);
		ticTacView = new TicTacView(main);
		landerView = new LanderView(main);
		doorView = new DoorView(main);
		planetHopView = new PlanetHopView(main, landerView.landerAnimation, landerView.explosionAnimation);
		quickLoad = true;
		dbm = DatabaseManager.get(main.mydb);	//Initialize database manager
		quickLoadSpeed = System.currentTimeMillis()-loadStart;
		System.out.println ("val: quick load time = " + quickLoadSpeed);
	}
	public static ResourceController getInstance(Context context){
		if (instance == null)
			instance = new ResourceController(context);
		return instance;
	}
	
	private void loadSlowShit(){
		new Thread(new Runnable() {
			public void run() {	
				zombieView = new ZombieView(main);
				multiTaskView = new MultiTaskView(main);
				zombieView.setup();
				multiTaskView.setup(zombieView.cowboyAnimation, zombieView.zombieAnimation);
				slowLoad = true;
				slowLoadSpeed = System.currentTimeMillis()-loadStart;
				System.out.println ("val: slow load time = " + slowLoadSpeed);
			}
		}).start();
	}
	
	public void processEndOfView(ViewType viewType){
		switch (viewType){//view calls its own end process and therefore has updated all local variables to exit state.
		case cowboy:
			break;
		case doors:
			dbm.updateUser(user, doorView.testTime, DBItem.DOORTIME, calibration);
			changeViews(ViewType.multitask);
			break;
		case lander:
			break;
		case multitask:
			dbm.updateUser(user, multiTaskView.testTime, DBItem.MULTITASKTIME, calibration);
			changeViews(ViewType.ticTacToe);
			break;
		case planetHop:
			break;
		case signIn:
			user = new User(signInView.initials);
			dbm.establishUser(user);
			dbm.updateUser(user, signInView.timeForCompletion, DBItem.LOGINTIME, calibration);
			dbm.sayValue(DBItem.LOGINTIME, user.initials, true);
			changeViews(ViewType.doors);
			break;
		case ticTacToe:
			dbm.updateUser(user, ticTacView.testTime, DBItem.TICTACTIME, calibration);
			changeViews(ViewType.planetHop);
			break;
		default:
			break;
		
		}
	}
	public void changeViews(ViewType viewType){
		switch (viewType){
		case cowboy:
			main.setContentView(zombieView);
			break;
		case doors:
			main.setContentView(doorView);
			break;
		case ticTacToe:
			main.setContentView(ticTacView);
			break;
		case lander:
			main.setContentView(landerView);
			break;
		case multitask:
			main.setContentView(multiTaskView);
			break;
		case planetHop:
			main.setContentView(planetHopView);
			break;
		case signIn:
			main.setContentView(signInView);
		default:
			break;
		}
			
	}
	
	public void loadNextView(){
		
	}
	
}
