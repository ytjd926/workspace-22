/**
 * Date:2013-9-25
 * Author:Ryze
 * todo: 本地存储
 */
package com.ian.missionhills.localstorage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.example.bt.BaseAPP;

public class Local {
	private static String tag = "com.ian.missionhills.localstorage.local";

	public static Local local;
	private static Context c;
	private static Editor editor;
	private static SharedPreferences mPrefs;

	public static Local getInstance() {
		if (local == null) {
			c = BaseAPP.getInstance().getApplicationContext();
			ContextWrapper contextW = new ContextWrapper(c);
			editor = contextW.getSharedPreferences(Local.tag, 1).edit();
			mPrefs = c.getSharedPreferences(Local.tag, 1);
			local = new Local();
		}
		return local;
	}

	/**
	 * 轻量级的本地存储，存入键值对
	 * 
	 * @param key
	 * @param value
	 *            如该key已存在，则该value会覆盖原先value
	 */
	public void setValue(String key, String value) {
		editor.putString(key, value);
		editor.commit();
	}

	public void setValue(String key, int value) {
		editor.putInt(key, value);
		editor.commit();
	}

    public void setValue(String key,boolean value){
        editor.putBoolean(key,value);
        editor.commit();
    }

	/**
	 * 轻量级的本地存储，根据key值取值
	 * 
	 * @param key
	 * @param value
	 *            如key没存入任何值，则返回该value
	 * @return
	 */
	public String getValue(String key, String value) {
		return mPrefs.getString(key, value);
	}

	public int getValue(String key, int value) {
		return mPrefs.getInt(key, value);
	}

    public boolean getValue(String key,boolean value){
        return mPrefs.getBoolean(key,value);
    }
	/**
	 * 删除该键值对
	 * 
	 * @param key
	 */
	public void deleteValue(String key) {
		editor.remove(key);
		editor.commit();
	}

	private final String DIR = "/sdcard/MHS/testData/";
	private final String format = "UTF-8";
	public  void saveString(String path, String content) {
		File file = new File(path);
		File dir = new File(DIR);
		if (!dir.exists() || !dir.isDirectory())
			dir.mkdirs();
		try {
			if (!file.exists())
				file.createNewFile();
			FileOutputStream out = new FileOutputStream(file);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out,
					format));
			bw.write(content);
			bw.flush();
			out.flush();
			bw.close();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
