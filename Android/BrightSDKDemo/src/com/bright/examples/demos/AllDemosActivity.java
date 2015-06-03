package com.bright.examples.demos;

import java.util.Arrays;

import com.bright.examples.NDK.NativeClass;
import com.brtbeacon.sdk.BRTBeaconManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * 演示功能列表
 */
public class AllDemosActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.all_demos);
		// 注册应用，初始化，在应用启动Activity或者Application派生类执行
		BRTBeaconManager.registerApp(this, "00000000000000000000000000000000");

		findViewById(R.id.distance_demo_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AllDemosActivity.this, BRTBeaconManagerListBeaconsActivity.class);
				intent.putExtra(BRTBeaconManagerListBeaconsActivity.EXTRAS_TARGET_ACTIVITY,
						BRTBeaconDistanceActivity.class.getName());
				startActivity(intent);
			}
		});
		findViewById(R.id.notify_demo_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AllDemosActivity.this, BRTBeaconManagerListBeaconsActivity.class);
				intent.putExtra(BRTBeaconManagerListBeaconsActivity.EXTRAS_TARGET_ACTIVITY,
						BRTNotifyDemoActivity.class.getName());
				startActivity(intent);
			}
		});
		findViewById(R.id.characteristics_demo_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AllDemosActivity.this, BRTBeaconManagerListBeaconsActivity.class);
				intent.putExtra(BRTBeaconManagerListBeaconsActivity.EXTRAS_TARGET_ACTIVITY,
						BRTBeaconConnectionDemoActivity.class.getName());
				startActivity(intent);
			}
		});
		
	        int[] array = new int[] { 1, 2,  3};       
	        String str = "数组，调用C++前" + Arrays.toString(array);   
	        boolean isCopyOfArrayInCpp = NativeClass.jniArrayAdd(array,             1);   
	        str += "\n在C++中为副本？  " + isCopyOfArrayInCpp;   
	        str += "\n数组，调用C++后：" + Arrays.toString(array);   

	}

}
