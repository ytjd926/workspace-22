package com.ian.mhstop.apis.screenadapter;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.ian.mhstop.main.R;

/**
 * @version <1.0>
 * @Company <Ji'nan Ian-soft Tech. Co., Ltd>
 * @Project <MHSTOP>
 * @Author <Ryze>
 * @Date <2014-6-9>
 * @description <适配演示界面>
 */

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        Scene fullScene=new Scene(this, 300,300);
        fullScene.setBackgroundColor(Color.RED);


        ImageView image1=new ImageView(this);
        image1.setBackgroundResource(R.drawable.child_phne_button);
        fullScene.addView(image1, 44, 30 ,22, 22);

        ImageView image2=new ImageView(this);
        image2.setBackgroundResource(R.drawable.child_phne_button);
        fullScene.addView(image2, 88, 60 ,30, 30);

        setContentView(fullScene);
        super.onCreate(savedInstanceState);
    }

}
