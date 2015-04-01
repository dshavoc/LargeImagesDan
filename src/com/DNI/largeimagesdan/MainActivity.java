package com.DNI.largeimagesdan;
import com.DNI.largeimagesdan.R;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity {
	public SQLiteDatabase mydb;
	public int screenHeight;
	public int screenWidth;
	public boolean calibration = true;
	private static String DBNAME = "DBreath.db";    // THIS IS THE SQLITE DATABASE FILE NAME
	public ResourceController resourceController;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
		screenHeight = metrics.heightPixels;
		screenWidth = metrics.widthPixels;
		mydb = openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
		resourceController = ResourceController.getInstance(this);
		resourceController.changeViews(ViewType.signIn);
		int maxID = resourceController.dbm.returnCurrentMaxPID(true);
		resourceController.dbm.sayAllInitials();
		System.out.println("Created");
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
	
}
