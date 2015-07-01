package com.ian.mhstop.apis.screenadapter;


import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;


/**
 * @version <1.0>
 * @Company <Ji'nan Ian-soft Tech. Co., Ltd>
 * @Project <MHSTOP>
 * @Author <Ryze>
 * @Date <2014-6-9>
 * @description <TODO>
 */

public class Screen {
	private static int planWidth=640;//设计图宽度
	private static int planHeight=960;//设计图高度
	private static int screenWidth = 0;
	private static int screenHeigh = 0;
	private static float scaleW=0;
	private static float scaleH=0;
	
	public static float getScaleW() {
		return scaleW;
	}


	public static float getScaleH() {
		return scaleH;
	}

	public static int getScreenWidth() {
		if(screenWidth==0){
			Log.i("Screen", "Screen还未初始化");
		}
		return screenWidth;
	}

	public static int getScreenHeigh() {
		if(screenHeigh==0){
			Log.i("Screen", "Screen还未初始化");
		}
		return screenHeigh;
	}

	public static void init(Activity context){
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		Screen.screenWidth = dm.widthPixels;
		Screen.screenHeigh = dm.heightPixels;
		Screen.scaleW = (float) (Screen.screenWidth) / (float) Screen.planWidth;
		Screen.scaleH = (float) (Screen.screenHeigh) / (float) Screen.planHeight;
	}
	
	
}
