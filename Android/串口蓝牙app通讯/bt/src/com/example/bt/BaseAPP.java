package com.example.bt;

/**
 * Date:2013-9-25
 * Author:Ryze
 * todo: 获取Application
 */

import android.app.Application;

//import dalvik.system.VMRuntime;
public class BaseAPP extends Application {
	static BaseAPP mainApp;
	public static String localVersion = "0";
	public static boolean childPush = false;

	public BaseAPP() {
		mainApp = this;
	}

	public static BaseAPP getInstance() {
		return mainApp;
	}


	@Override
	public void onCreate() {
		super.onCreate();
	}
}
