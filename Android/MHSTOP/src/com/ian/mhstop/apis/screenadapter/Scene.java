package com.ian.mhstop.apis.screenadapter;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;

/**
 * @version <1.0>
 * @Company <Ji'nan Ian-soft Tech. Co., Ltd>
 * @Project <MHSTOP>
 * @Author <Ryze>
 * @Date <2014-6-9>
 * @description <创建适配Scene> 1、该场景由创建者指定宽高 2、多个场景可重叠 3、该场景和Activity生命周期一致
 * 
 */

public class Scene extends FrameLayout {

	private int SceneW;// 该场景宽度
	private int SceneH;// 该场景高度
	private int SceneX = 0;// 该场景所处位置X
	private int SceneY = 0;// 该场景所处位置Y
	private Activity context;

	public Scene(Activity context, int width, int height) {
		super(context);
		this.context = context;
		this.SceneW = (int) (width * Screen.getScaleW());
		this.SceneH = (int) (height * Screen.getScaleH());

		LayoutParams layoutParams = new LayoutParams(width, height);
		setLayoutParams(layoutParams);
	}

	public Scene(Activity context, int width, int height, int x, int y) {
		super(context);
		this.context = context;
		this.SceneW = (int) (width * Screen.getScaleW());
		this.SceneH = (int) (height * Screen.getScaleH());
		this.SceneX = (int) (x * Screen.getScaleW());
		this.SceneY = (int) (y * Screen.getScaleH());

		LayoutParams layoutParams = new LayoutParams(width, height);
		layoutParams.leftMargin = SceneX;
		layoutParams.topMargin = SceneY;
		setLayoutParams(layoutParams);
	}

	/**
	 * 添加child
	 * 
	 * @param child
	 *            子view
	 * @param width
	 *            子view的宽度 占父View的百分比
	 * @param height
	 *            子view的高度 占父View的百分比
	 * @param x
	 *            子view的x坐标值 占父view的百分比
	 * @param y
	 *            子view的y坐标值 占父view的百分比
	 */
	public void addView(View child, float width, float height, float left,
			float top) {
		// TODO 重写addView方法，实现适配功能
		LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
		int w = (int) (width * SceneW);
		int h = (int) (height * SceneH);
		int x = (int) (left * SceneW);
		int y = (int) (top * SceneH);
		if (layoutParams == null) {
			layoutParams = new LayoutParams(w, h);
		}
		layoutParams.width = w;
		layoutParams.height = h;
		layoutParams.leftMargin = x;
		layoutParams.topMargin = y;
		child.setLayoutParams(layoutParams);
		this.addView(child);
	}

	/**
	 * 添加child
	 * 
	 * @param child
	 *            子view
	 * @param width
	 *            子view在设计图中的宽度
	 * @param height
	 *            子view在设计图中的高度
	 * @param x
	 *            子view在设计图中的x坐标值
	 * @param y
	 *            子view在设计图中的Y坐标值
	 */
	public void addView(View child, int width, int height, int left, int top) {
		// TODO 重写addView方法，实现适配功能
		LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
		int w = (int) (width * Screen.getScaleW());
		int h = (int) (height * Screen.getScaleH());
		int x = (int) (left * Screen.getScaleW());
		int y = (int) (top * Screen.getScaleH());
		if (layoutParams == null) {
			layoutParams = new LayoutParams(w, h);
		}
		layoutParams.width = w;
		layoutParams.height = h;
		layoutParams.leftMargin = x;
		layoutParams.topMargin = y;
		child.setLayoutParams(layoutParams);
		this.addView(child);
	}

}
