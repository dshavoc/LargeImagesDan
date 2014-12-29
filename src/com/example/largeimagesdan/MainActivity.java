package com.example.largeimagesdan;
import com.example.largeimagesdan.LargeBitmapView;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
//import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

enum ViewType {doors,ticTacToe,cowboy,lander};
public class MainActivity extends ActionBarActivity {
	public SQLiteDatabase mydb;
	int screenHeight;
	int screenWidth;
	
	TicTacView ticTacView;
	FourSquareView fourSquaresView;
	ZombieView zombieView;
	LanderView landerView;
	
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
		//zombieView = new ZombieView(this);
		landerView = new LanderView(this);
		
		changeViews(ViewType.lander);
		
		//u.time = 60;
		dbm.updateUser(u, 300, "TIMETTT");
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
	public void changeViews(ViewType viewType){
		switch (viewType){
		case cowboy:setContentView(zombieView);
			break;
		case doors:setContentView(fourSquaresView);
			break;
		case ticTacToe:setContentView(ticTacView);
			break;
		case lander:setContentView(landerView);
		break;
		
		default:
			break;
		
		}
			
	}
}
