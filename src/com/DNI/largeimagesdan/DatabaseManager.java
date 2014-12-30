package com.DNI.largeimagesdan;


import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseManager extends Activity{

	SQLiteDatabase mydb;
	private static String TABLE = "GARRAM";     
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
		createTable();
	}

	private void dropTable(){
		mydb.execSQL("DROP TABLE " + TABLE);
	}
	private void createTable(){
		try{
			mydb.execSQL("CREATE TABLE IF NOT EXISTS  "+ TABLE+" (PID INTEGER PRIMARY KEY, INITIALS TEXT, DOORSTIME INT,DOORSFAIL INT, TTTTIME INT, TTTFAILS INT);");
			//mydb.close();
		}catch(Exception e){
			say ("error creating table");
		}
	}

	private void insertUser(User u ){ // this is private because establish will insert if user does not already exist.
		String exec =  "INSERT INTO " + TABLE + "(INITIALS, TIMETTT) VALUES('"+u.initials + "','" + u.time +"')";
		try{
			mydb.execSQL(exec);
		}		
		catch(Exception e){
			say ("insert error");
		}
	}
	public void updateUser(User u, int value, String variableName){ //confirmed functional
		establishUser(u);
		String exec =   "UPDATE " + TABLE + " SET " + variableName + "= '" 	+ 	value 	+"'  WHERE INITIALS = '" + u.initials + "';";
		try{
			mydb.execSQL(exec);
			sayValue(variableName,u.initials);
		}		
		catch(Exception e){
			say ("update user error for variable named " + variableName + " and a value of " + value + ".");
		}
		
	}
	public String returnValue(String variableName, String initials){ //confirmed functional
		String ret = "";
		String exec = "SELECT (" + variableName + ")  FROM " + TABLE + " WHERE INITIALS = '" + initials + "';";  
		Cursor allrows = mydb.rawQuery(exec, null);
		if(allrows.moveToFirst()){
			do{
				ret = allrows.getString(0);
			}
			while(allrows.moveToNext());
		}
		return ret;	
	}
	public void sayValue(String variableName, String initials){ //confirmed functional
		String exec = "SELECT (" + variableName + ")  FROM " + TABLE + " WHERE INITIALS = '" + initials + "';";  

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
			Cursor allrows  = mydb.rawQuery("SELECT * FROM "+  TABLE + " WHERE INITIALS = '" + u.initials + "'", null); 
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