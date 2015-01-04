package com.DNI.largeimagesdan;


import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseManager extends Activity{

	SQLiteDatabase mydb;
	private static String CALIBRATIONTABLE = "Calibration";
	private static String TESTTABLE = "Testing";
	private static DatabaseManager instance;
	
	public static DatabaseManager get(SQLiteDatabase mydb) {
		if(instance == null)
			instance = new DatabaseManager(mydb);
		return instance;
	}

	public static DatabaseManager get() {
		if(instance == null) {
			System.err.println("DatabaseManager has not been initialized!");
			return null;
		}
		else
			return instance;
	}

	private DatabaseManager(SQLiteDatabase db){
		mydb=db;
		//dropTable();
		createTable(TESTTABLE);
		createTable(CALIBRATIONTABLE);
	}

	private void dropTables(){
		mydb.execSQL("DROP TABLE " + CALIBRATIONTABLE);
		mydb.execSQL("DROP TABLE " + TESTTABLE);
	}
	
	private void createTable(String table){
		try{
			String exec = ("CREATE TABLE IF NOT EXISTS  "+ table+" (" +
					"PID INTEGER PRIMARY KEY," +
					"INITIALS TEXT, " +
					"SAMPLESIZE INT," +
					"LOGINTIME INT, " +
					"DOORTIME INT, " +
					"TICTACTIME INT," +
					"MULTITASKTIME INT," +
					"PLANETHOPTIME INT,"+
					"LANDERTIME INT" +
					");");
			mydb.execSQL(exec);
			//mydb.close();
		}catch(Exception e){
			say ("error creating " + table);
		}
	}

	private void insertUser(User u ){ // this is private because establish will insert if user does not already exist.
		String exec =  "INSERT INTO " + TESTTABLE + "(INITIALS) VALUES('"+u.initials +"')";
		try{
			mydb.execSQL(exec);
		}		
		catch(Exception e){
			say ("insert error");
		}
		exec =  "INSERT INTO " + CALIBRATIONTABLE + "(INITIALS) VALUES('"+u.initials +"')";
		try{
			mydb.execSQL(exec);
		}		
		catch(Exception e){
			say ("insert error");
		}
	}
	public void updateUser(User u, int value, DBItem variable, boolean calibration){ //confirmed functional
		establishUser(u);
		String TABLE;
		if (calibration) TABLE = CALIBRATIONTABLE;
		else TABLE = TESTTABLE;
		String exec =   "UPDATE " + TABLE + " SET " + variable.toString() + "= '" 	+ 	value 	+"'  WHERE INITIALS = '" + u.initials + "';";
		try{
			mydb.execSQL(exec);
			sayValue(variable,u.initials,calibration);
		}		
		catch(Exception e){
			say ("update user error for variable named " + variable.toString() + " and a value of " + value + ".");
		}
		
	}
	public String returnValue(String initials, DBItem variable, boolean calibration){ //confirmed functional
		String ret = "";
		String TABLE;
		if (calibration) TABLE = CALIBRATIONTABLE;
		else TABLE = TESTTABLE;
		String exec = "SELECT (" + variable.toString() + ")  FROM " + TABLE + " WHERE INITIALS = '" + initials + "';";  
		Cursor allrows = mydb.rawQuery(exec, null);
		if(allrows.moveToFirst()){
			do{
				ret = allrows.getString(0);
			}
			while(allrows.moveToNext());
		}
		return ret;	
	}
	
	public void sayValue(DBItem variable, String initials, boolean calibration){ //confirmed functional
		String TABLE;
		if (calibration) TABLE = CALIBRATIONTABLE;
		else TABLE = TESTTABLE;

		String exec = "SELECT (" + variable.toString() + ")  FROM " + TABLE + " WHERE INITIALS = '" + initials + "';";  
		Cursor allrows = mydb.rawQuery(exec, null);
		if(allrows.moveToFirst()){
			do{
				String ONE = allrows.getString(0);
				say("This is the say value call accessed from the database. Value = " + ONE);
			}
			while(allrows.moveToNext());
		}
	}
	private void say(String s){
		System.out.println(s);
	}

	public void establishUser(User u){
		try{
			Cursor allrows  = mydb.rawQuery("SELECT * FROM "+  TESTTABLE + " WHERE INITIALS = '" + u.initials + "'", null); 
			if(allrows.moveToFirst()){

			}
			else{
				say ("new user");
				insertUser(u);
			}
		}
		catch(Exception e){ 
		}
		
		try{
			Cursor allrows  = mydb.rawQuery("SELECT * FROM "+  CALIBRATIONTABLE + " WHERE INITIALS = '" + u.initials + "'", null); 
			if(allrows.moveToFirst()){

			}
			else{
				say ("new user");
				insertUser(u);
			}
		}
		catch(Exception e){ 
		}
	}
}