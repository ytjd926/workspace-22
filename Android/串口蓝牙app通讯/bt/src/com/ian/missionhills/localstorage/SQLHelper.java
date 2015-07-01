package com.ian.missionhills.localstorage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bt.BaseAPP;

  


public class SQLHelper {
	private static String tag = "com.ian.missionhills.localstorage.SQLHelper";
	private static Context c;
	public static SQLHelper sqlHelper;
	public static SQLHelper getInstance() {
		if (sqlHelper == null) {
			c = BaseAPP.getInstance().getApplicationContext();
			sqlHelper = new SQLHelper();
		}
		return sqlHelper;
	}
	
	private SQLiteDatabase db;
	public void createDB(String DBName) {
		DBName="SQL_MISSIONHILLS";
		//打开或创建test.db数据库
		db = c.openOrCreateDatabase(DBName+".db", c.MODE_PRIVATE, null);
		
	}
	public void createTable(String tableName){
		db.execSQL("DROP TABLE IF EXISTS person");
		db.execSQL("CREATE TABLE person (_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, age SMALLINT)");
	}
	public void insert(){
		db.execSQL("INSERT INTO person VALUES (NULL, ?, ?)", new Object[]{"john",30});
	}
	public void query(){
		Cursor cursor=db.rawQuery("SELECT * FROM person", null);
		 while (cursor.moveToNext()) {  
	            int _id = cursor.getInt(cursor.getColumnIndex("_id"));  
	            String name = cursor.getString(cursor.getColumnIndex("name"));  
	            int age = cursor.getInt(cursor.getColumnIndex("age"));  
	        }
        if(!cursor.isClosed()) {
            cursor.close();
            cursor=null;
            System.err.println("SQLHeper cursor release！");
        }
	}
}
