package com.ian.mhstop.apis.bitmaphelp;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;

import com.ian.mhstop.apis.screenadapter.Scene;
import com.ian.mhstop.apis.screenadapter.Screen;
import com.ian.mhstop.main.R;

/**
 * @version <1.0>
 * @Company <Ji'nan Ian-soft Tech. Co., Ltd>
 * @Project <MHSTOP>
 * @Author <Ryze>
 * @Date <2014-6-18>
 * @description <TODO>
 */

public class Simple extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Scene scene = new Scene(this, Screen.getScreenWidth(),
				Screen.getScreenHeigh());
		ImageView imageview = new ImageView(this);
		imageview.setImageResource(R.drawable.ic_launcher);
		imageview.setBackgroundColor(Color.BLUE);

		scene.addView(imageview, 200, 200, 20, 40);

		BitmapHelp.display(this, imageview,
				"http://su.bdimg.com/static/superplus/img/logo_white.png");

		setContentView(scene);
		super.onCreate(savedInstanceState);
	}
}
