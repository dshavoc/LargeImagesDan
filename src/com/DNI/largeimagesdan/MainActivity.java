package com.DNI.largeimagesdan;
import com.DNI.PlanetHop.PlanetHopView;
import com.DNI.multitask.MultiTaskView;
import com.example.largeimagesdan.R;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

enum ViewType {doors,ticTacToe,cowboy,lander,multitask,planetHop};
public class MainActivity extends ActionBarActivity {
	public SQLiteDatabase mydb;
	public int screenHeight;
	public int screenWidth;
	
	TicTacView ticTacView;
	FourSquareView fourSquaresView;
	ZombieView zombieView;
	LanderView landerView;
	MultiTaskView multiTaskView;
	PlanetHopView planetHopView;
	public boolean zombiesLoaded = false; 
	User u;
	private static String DBNAME = "DBreath.db";    // THIS IS THE SQLITE DATABASE FILE NAME
	DatabaseManager dbm;
	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
		screenHeight = metrics.heightPixels;
		screenWidth = metrics.widthPixels;
		//System.out.println("checkItAll " + screenHeight);
		//setContentView(R.layout.activity_b__main);
		u = new User("DAN");
		mydb = openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
		dbm = DatabaseManager.get(mydb);	//Initialize database manager
		
		dbm.establishUser(u);
		
		ticTacView = new TicTacView(this);
		fourSquaresView = new FourSquareView(this);
		landerView = new LanderView(this);
		planetHopView = new PlanetHopView(this, landerView.landerAnimation, landerView.explosionAnimation);
		//zombieView = new ZombieView(this);
		//multiTaskView = new MultiTaskView(this);
		//zombieView.setup();
		//multiTaskView.setup(zombieView.cowboyAnimation, zombieView.zombieAnimation);
		/*new Thread(new Runnable() {// load zombies ... (it takes 25 ish seconds.... bad bad bad us)
			@Override
			public void run() {
				long startTime =  System.currentTimeMillis();
				System.out.println("Start Load Phase");
				zombieView.setup();
				multiTaskView.setup(zombieView.cowboyAnimation, zombieView.zombieAnimation);
				zombiesLoaded = true;
				System.out.println("Finish Load Phase. Load took " + (System.currentTimeMillis()-startTime) );
			}
		}).start(); */
		changeViews(ViewType.planetHop);
		
		//u.time = 60;
		dbm.updateUser(u, 300, "TTTTIME");
		//System.out.println("Say Call");
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	public synchronized void changeViews(ViewType viewType){
		switch (viewType){
		case cowboy:setContentView(zombieView);
			break;
		case doors:setContentView(fourSquaresView);
			break;
		case ticTacToe:setContentView(ticTacView);
			break;
		case lander:setContentView(landerView);
		break;
		case multitask:setContentView(multiTaskView);
		break;
		case planetHop:setContentView(planetHopView);
		default:
			break;
		
		}
			
	}
}
