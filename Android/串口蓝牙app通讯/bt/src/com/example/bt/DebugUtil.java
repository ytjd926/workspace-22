package com.example.bt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import android.graphics.Point;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;


public class DebugUtil {
	private static String tag="DebugUtil";
	private static boolean isDebug=true;
	
	public static void showToast(Object message){
		if(isDebug) {
            Toast.makeText(BaseAPP.getInstance().getApplicationContext(), message.toString(), Toast.LENGTH_SHORT).show();
        }
	}
	public static void showLog(Object message){
		if(isDebug)
		Log.i(tag, message.toString());
	}
	
	
	/**
	 * 保存错误信息到文件中
	 * 
	 * @param ex
	 * @return	返回文件名称,便于将文件传送到服务器
	 */
	public static String saveString2File(float x,float y,String detail) {
		String path = "/sdcard/crash/";
		//用于格式化日期,作为日志文件名的一部分
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String time = formatter.format(new Date());
		
		StringBuffer sb = new StringBuffer(time+": "+detail+"  Point: x="+x+" y="+y+"        \r\n");
		try {
			String fileName = "point.log";
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				File dir = new File(path);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				FileOutputStream fos =new FileOutputStream(path + fileName,true);
				fos.write(sb.toString().getBytes());
				fos.close();
			}
			return fileName;
		} catch (Exception e) {
		}
		return null;
	}
	
}

