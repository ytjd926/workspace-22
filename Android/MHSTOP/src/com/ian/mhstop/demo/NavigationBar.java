package com.ian.mhstop.demo;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import com.ian.mhstop.main.R;

/**
 * Created by RyzeHz on 2015/7/1.
 */
public class NavigationBar extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigationbar);

        final ActionBar bar = getActionBar();
        int  flags = ActionBar.DISPLAY_HOME_AS_UP;
        int change = bar.getDisplayOptions() ^ flags;
        bar.setDisplayOptions(flags);
    }
}
