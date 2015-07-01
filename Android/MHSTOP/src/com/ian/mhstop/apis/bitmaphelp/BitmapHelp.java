package com.ian.mhstop.apis.bitmaphelp;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;

/**
 * Author: wyouflf Date: 13-11-12 Time: 上午10:24
 */
public class BitmapHelp {
	private BitmapHelp() {
	}

	private static BitmapUtils bitmapUtils;

	/**
	 * BitmapUtils不是单例的 根据需要重载多个获取实例的方法
	 * 
	 * @param appContext
	 *            application context
	 * @return
	 */
	public static BitmapUtils getBitmapUtils(Context appContext) {
		if (bitmapUtils == null) {
			bitmapUtils = new BitmapUtils(appContext);
		}
		return bitmapUtils;
	}

	public static void display(Context appContext, ImageView container,
			String uri) {
		getBitmapUtils(appContext).display(container, uri);
	}
	
	public static void clearCache(){
		bitmapUtils.clearCache();
	}
	
	public static void clearMemoryCache(){
		bitmapUtils.clearMemoryCache();
	}
}

/**
 * // 加载网络图片 bitmapUtils.display(testImageView,
 * "http://bbs.lidroid.com/static/image/common/logo.png");
 * 
 * // 加载本地图片(路径以/开头， 绝对路径) bitmapUtils.display(testImageView,
 * "/sdcard/test.jpg");
 * 
 * // 加载assets中的图片(路径以assets开头) bitmapUtils.display(testImageView,
 * "assets/img/wallpaper.jpg");
 * 
 * // 使用ListView等容器展示图片时可通过PauseOnScrollListener控制滑动和快速滑动过程中时候暂停加载图片
 * listView.setOnScrollListener(new PauseOnScrollListener(bitmapUtils, false,
 * true)); listView.setOnScrollListener(new PauseOnScrollListener(bitmapUtils,
 * false, true, customListener));
 * 
 * 
 * 
 */

