package com.example.bt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Launcher extends Activity  {
	 @Override  
	  protected void onCreate(Bundle savedInstanceState) {  
	   super.onCreate(savedInstanceState);  
	   setContentView(R.layout.launcher);
         Analysis.getInstance();

       new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				
				
			    Intent intent = new Intent();
                //Intent传递参数
                intent.setClass(Launcher.this, ChooseDeal.class);
                Launcher.this.startActivity(intent);
                Launcher.this.finish();
			}
       }, 2000); //启动等待3秒钟

	 }
	 
	 @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		 Launcher.this.finish();
		super.onDestroy();
	}
}
