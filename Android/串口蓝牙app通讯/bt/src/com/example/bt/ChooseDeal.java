package com.example.bt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by ian on 15/7/1.
 */
public class ChooseDeal extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choosedeal);


    }


    public void inMD1000(View view) {
        Intent i =new Intent(ChooseDeal.this,MainActivity1.class);
        ChooseDeal.this.startActivity(i);

    }

    public void inDanbingyihao(View view) {
        Intent i =new Intent(ChooseDeal.this,MainActivity1.class);
        ChooseDeal.this.startActivity(i);
    }
}
