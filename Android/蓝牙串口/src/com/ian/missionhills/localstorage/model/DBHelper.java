package com.ian.missionhills.localstorage.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.example.bt.BaseAPP;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;



public class DBHelper extends OrmLiteSqliteOpenHelper {

	private final static String dbNamePrefix = "MSH";

	public static DBHelper dbHelper;

	public synchronized static DBHelper getInstance() {
		if (dbHelper == null) {
			dbHelper = OpenHelperManager.getHelper(BaseAPP.getInstance(),
					DBHelper.class);
		}
		return dbHelper;
	}

	public DBHelper(Context context, String databaseName,
			CursorFactory factory, int databaseVersion) {
		super(context, databaseName, factory, databaseVersion);
	}

	public DBHelper(Context context) {
		super(context, dbNamePrefix + ".db", null, 1);
	}

	public ConnectionSource getConnectionSource() {
		return super.getConnectionSource();
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		try {
			/**
			 * 创建的所有表 都需要在这里create一下
			 */
			TableUtils.createTableIfNotExists(arg1, Student.class);
			TableUtils.createTableIfNotExists(arg1, YuLiang.class);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub

	}

}
