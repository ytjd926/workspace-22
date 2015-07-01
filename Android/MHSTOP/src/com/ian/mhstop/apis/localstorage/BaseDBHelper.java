package com.ian.mhstop.apis.localstorage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.ian.mhstop.apis.BaseAPP;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * @version <1.0>
 * @Company <Ji'nan Ian-soft Tech. Co., Ltd>
 * @Project <MHSTOP>
 * @Author <Ryze>
 * @Date <2014-6-12>
 * @description <TODO>
 */

public class BaseDBHelper extends OrmLiteSqliteOpenHelper {

    private final static String dbNamePrefix = "MSH";

    public static BaseDBHelper dbHelper;

    public synchronized static BaseDBHelper getInstance() {
        if (dbHelper == null) {
            dbHelper = OpenHelperManager.getHelper(BaseAPP.getInstance(),
                    BaseDBHelper.class);
        }
        return dbHelper;
    }

    public BaseDBHelper(Context context, String databaseName,
                        CursorFactory factory, int databaseVersion) {
        super(context, databaseName, factory, databaseVersion);
    }

    public BaseDBHelper(Context context) {
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
//			TableUtils.createTableIfNotExists(arg1, Student.class);

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
