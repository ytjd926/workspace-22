package com.ian.mhstop.apis;

import android.app.Application;

/**
 * @version <1.0>
 * @Company <Ji'nan Ian-soft Tech. Co., Ltd>
 * @Project <MHSTOP>
 * @Author <Ryze>
 * @Date <2014-6-9>
 * @description <TODO>
 */

public class BaseAPP extends Application {
	static BaseAPP mainApp;
    public static String localVersion = "0";
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

