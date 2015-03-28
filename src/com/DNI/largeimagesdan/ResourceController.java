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
				System.out.println("start load");
				zombieView = new ZombieView(main);
				multiTaskView = new MultiTaskView(main);
				//zombieView.setup();
				multiTaskView.setup(zombieView.cowboyAnimation, zombieView.zombieAnimation);
				slowLoad = true;
				slowLoadSpeed = System.currentTimeMillis()-loadStart;
				System.out.println ("val: slow load time = " + slowLoadSpeed);
			}
		}).start();
	}
	
	private void giveReport(){
		say (" sign in time = " + signInView.timeForCompletion);
		say (" doors time = " + doorView.testTime);
		say (" doors errors = " + doorView.wrongDoors);
		say (" tic tac time = " + ticTacView.testTime);
		say (" tic tac losses = " + ticTacView.losses);
		say (" multitask time = " + multiTaskView.testTime);
		say (" multitask fails = " + multiTaskView.failures);
		say (" planet hop time = " + planetHopView.testTime);
		say (" planet hop fails = " + planetHopView.failures);
		say (" lander time = " + landerView.testTime);
	}
	
	private void updateDatabase(){
		dbm.insertUser(signInView.initials, calibration);
		int targetPID = dbm.returnCurrentMaxPID(calibration);
		say("TARGET PID = "+targetPID);
		dbm.updatePID(targetPID, signInView.timeForCompletion, DBItem.LOGINTIME, calibration);
		dbm.updatePID(targetPID, doorView.testTime, DBItem.DOORTIME, calibration);
		dbm.updatePID(targetPID, ticTacView.testTime, DBItem.TICTACTIME, calibration);
		dbm.updatePID(targetPID, multiTaskView.testTime, DBItem.MULTITASKTIME, calibration);
		dbm.updatePID(targetPID, landerView.testTime, DBItem.LANDERTIME, calibration);
		dbm.updatePID(targetPID, planetHopView.testTime, DBItem.PLANETHOPTIME, calibration);		
	}
	public void processEndOfView(ViewType viewType){
		switch (viewType){//view calls its own end process and therefore has updated all local variables to exit state.
		case cowboy:
			changeViews(ViewType.multitask);
			break;
		case doors:
			changeViews(ViewType.cowboy);
			break;
		case lander:
			giveReport();
			updateDatabase();
			break;
		case multitask:
			changeViews(ViewType.ticTacToe);
			break;
		case planetHop:
			if (planetHopView.failures <3) // completedTest
				{
				changeViews(ViewType.lander);
				}
			else // failed test
				System.out.println("failed test");
			break;
		case signIn:
			changeViews(ViewType.doors);
			break;
		case ticTacToe:
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
	private void say(String s){
		System.out.println("Report: "+ s);
	}
	
}
