package com.ian.mhstop.apis;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ian.mhstop.apis.screenadapter.Screen;

/**
 * @version <1.0>
 * @Company <Ji'nan Ian-soft Tech. Co., Ltd>
 * @Project <MHSTOP>
 * @Author <Ryze>
 * @Date <2014-6-9>
 * @description <项目首页>
 */

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Screen.init(this);

		ListView listView = new ListView(this);
		listView.setAdapter(new MyAdapter(this));
		setContentView(listView);

	}

	class MyAdapter extends BaseAdapter {
		List<String> data = new ArrayList<String>();
		Context context;

		MyAdapter(Context context) {
			this.context = context;
			data.add("全屏幕适配演示 com.ian.mhstop.apis.screenadapter.MainActivity");
			data.add("BitmapHelp com.ian.mhstop.apis.bitmaphelp.Simple");

            data.add("NavigationBa1r com.ian.mhstop.demo.NavigationBar");
            data.add("ActionBarDisplayOptions com.ian.mhstop.demo.ActionBarDisplayOptions");

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return data.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			String oneData = data.get(arg0);
			String[] _s = oneData.split(" ");
			String name = _s[0];
			String pkg = _s[1];

			TextView view = new TextView(context);
			view.setText(name);
			view.setTag(pkg);
			view.setTextSize(33);
			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					String pkg = arg0.getTag().toString();
					Intent i = new Intent();
					i.setClassName(MainActivity.this, pkg);
					context.startActivity(i);
				}
			});
			return view;
		}

	}

}
