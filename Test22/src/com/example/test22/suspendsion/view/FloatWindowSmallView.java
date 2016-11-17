package com.example.test22.suspendsion.view;

import java.lang.reflect.Field;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.test22.R;
import com.example.test22.suspendsion.MyWindowManager;

/**
 * @author zc
 *小悬浮窗
 */
public class FloatWindowSmallView extends LinearLayout {
	/**
	 * 记录小悬浮窗的宽度
	 */
	public static int viewWidth;
	/**
	 * 记录小悬浮窗高度
	 */
	public static int viewHeight;
	/**
	 * 系统状态栏高度
	 */
	private static int statusBarHeight;
	/**
	 * 用于更新小悬浮窗的位置
	 */
	private WindowManager windowManager;
	/**
	 * 小悬浮窗的参数
	 */
	private WindowManager.LayoutParams mParams;
	/**
	 * 记录手指在屏幕上的横坐标
	 */
	private float xInScreen;
	/**
	 * 记录手指在屏幕上的纵坐标
	 */
	private float yInScreen;
	/**
	 * 手指按下时在屏幕上的x坐标
	 */
	private float xDownInScreen;
	/**
	 * 手指按下时在屏幕上Y左边
	 */
	private float yDownInScreen;
	/**
	 * 在view上的x坐标
	 */
	private float xInView;
	/**
	 * 在View上的Y坐标
	 */
	private float yInView;
	
	public FloatWindowSmallView(Context context) {
		this(context,null);
	}

	public FloatWindowSmallView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public FloatWindowSmallView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		LayoutInflater.from(context).inflate(R.layout.pupsesion_small, this);
		View view = findViewById(R.id.ll_smallView);
		viewWidth = view.getLayoutParams().width;
		viewHeight = view.getLayoutParams().height;
		TextView tv_percent =(TextView) view.findViewById(R.id.tv_percent);
		tv_percent.setText("50%");
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			xInView = event.getX();
			yInView = event.getY();
			xDownInScreen = event.getRawX();
			yDownInScreen = event.getRawY()-getStatusBarHeight();
			xInScreen = event.getRawX();
			yInScreen = event.getRawY()-getStatusBarHeight();
			break;
		case MotionEvent.ACTION_MOVE:
			xInScreen = event.getRawX();
			yInScreen = event.getRawY()-getStatusBarHeight();
			//手指移动时候更新小悬浮窗的位置
			updateViewPosition();
			break;
		case MotionEvent.ACTION_UP:
			//如果手指离开屏幕时，xDownInScreen==xInScreen&&yDownInScreen==yInScreen
			//则弹出大的悬浮窗
			if(xDownInScreen==xInScreen&&yDownInScreen==yInScreen){
				openBigWindow();
			}
			break;
		}
		return true;
	}

	/**
	 * 打开大悬浮窗，同时关闭小悬浮窗
	 */
	private void openBigWindow() {
		MyWindowManager.createBigWindow(getContext());
		MyWindowManager.removeSmallWindow(getContext());
	}

	private void updateViewPosition() {
		mParams.x = (int)(xInScreen-xInView);
		mParams.y = (int)(yInScreen-yInView);
		windowManager.updateViewLayout(this, mParams);
	}

	/**用户获取状态栏高度
	 * @return 返回状态栏高度的像素值
	 */
	private float getStatusBarHeight() {
		if(statusBarHeight==0){
			try {
				Class<?> c = Class.forName("com.android.internal.R$dimen");
				Object o = c.newInstance();
				Field field = c.getField("status_bar_height");
				int x = (Integer)field.get(o);
				statusBarHeight = getResources().getDimensionPixelSize(x);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return statusBarHeight;
	}
	public  void setParams(android.view.WindowManager.LayoutParams lp){
		mParams = lp;
	}

}
