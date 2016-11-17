package com.example.test22.suspendsion;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.example.test22.R;
import com.example.test22.suspendsion.view.FloatWindowBigView;
import com.example.test22.suspendsion.view.FloatWindowSmallView;

public class MyWindowManager {
	private static FloatWindowSmallView smallWindow;
	private static FloatWindowBigView bigWindow;
	/**
	 * 小悬浮窗的view参数
	 */
	private static LayoutParams smallLayoutParams;
	/**
	 * 大悬浮窗的view参数
	 */
	private static LayoutParams bigLayoutParams;
	/**
	 * 用于控制在屏幕上添加或移除悬浮窗
	 */
	private static WindowManager mWindowManager;
	/**
	 * 获取手机可用内存
	 */
	private static ActivityManager mActivityManager;
	/**创建一个小悬浮窗，初始位置为屏幕的右中间位置
	 * @param context 必须为应用程序的context
	 */
	public static void createSmallWindow(Context context){
		WindowManager windowManager = getWindowManager(context);
		int screenWidth = windowManager.getDefaultDisplay().getWidth();
		int screenHeight = windowManager.getDefaultDisplay().getHeight();
		if(smallWindow==null){
			smallWindow=new FloatWindowSmallView(context);
			if(smallLayoutParams==null){
				smallLayoutParams = new LayoutParams();
				smallLayoutParams.type = LayoutParams.TYPE_PHONE;
				//设置图片格式，效果为背景透明  
				smallLayoutParams.format = PixelFormat.RGBA_8888;
				smallLayoutParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL|LayoutParams.FLAG_NOT_FOCUSABLE;
				smallLayoutParams.gravity = Gravity.LEFT|Gravity.TOP;
				smallLayoutParams.width = FloatWindowSmallView.viewWidth;
				smallLayoutParams.height = FloatWindowSmallView.viewHeight;
				smallLayoutParams.x = screenWidth;
				smallLayoutParams.y = screenHeight/2;
			}
			smallWindow.setParams(smallLayoutParams);
			windowManager.addView(smallWindow, smallLayoutParams);
		}
		
	}
	/**将小悬浮窗从屏幕上移除
	 * @param context 必须为应用程序的context
	 */
	public static void removeSmallWindow(Context context){
		if(smallWindow!=null){
			WindowManager windowManager = getWindowManager(context);
			windowManager.removeView(smallWindow);
			smallWindow=null;
		}
	}
	/**创建一个大的悬浮窗，位置为屏幕中间
	 * @param context 必须为应用程序的context
	 */
	public static void createBigWindow(Context context){
		WindowManager windowManager = getWindowManager(context);
		int screenWidth= windowManager.getDefaultDisplay().getWidth();
		int screenHeight = windowManager.getDefaultDisplay().getHeight();
		if(bigWindow==null){
			bigWindow = new FloatWindowBigView(context);
			if(bigLayoutParams==null){
				bigLayoutParams = new LayoutParams();
				bigLayoutParams.x = screenWidth/2-FloatWindowBigView.viewWidth/2;
				bigLayoutParams.y = screenHeight/2-FloatWindowBigView.viewHeight/2;
				bigLayoutParams.type = LayoutParams.TYPE_PHONE;
				bigLayoutParams.format = PixelFormat.RGBA_8888;
				bigLayoutParams.gravity = Gravity.LEFT|Gravity.TOP;
				bigLayoutParams.width = FloatWindowBigView.viewWidth;
				bigLayoutParams.height = FloatWindowBigView.viewHeight;
			}
			windowManager.addView(bigWindow, bigLayoutParams);
		}
	}
	/**将大悬浮窗从屏幕上移除
	 * @param context 必须为应用程序的context
	 */
	public static void removeBigWindow(Context context){
		if(bigWindow!=null){
			WindowManager windowManager = getWindowManager(context);
			windowManager.removeView(bigWindow);
			bigWindow=null;
		}
	}
	/**
	 * @param context 必须为应用程序的conext
	 * @return WindowManager 实例，用于控制悬浮窗在屏幕上的添加或移除
	 */
	private static WindowManager getWindowManager(Context context){
		if(mWindowManager==null){
			mWindowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		}
		return mWindowManager;
	}
	/**
	 * @param context 可传入应用程序context
	 * @return ActivityManager 的实例，用于获取手机内存
	 */
	private static ActivityManager getActivityManager(Context context){
		if(mActivityManager==null){
			mActivityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		}
		return mActivityManager;
	}
	/** 计算已使用内存的百分比
	 * @param context 可传入应用程序上下文
	 * @return 已使用内存的百分比，以字符串形式返回
	 */
	public static String getUserPercentValue(Context context){
		String dir = "/proc/meminfo";
		try {
			FileReader fr = new FileReader(dir);
			BufferedReader br = new BufferedReader(fr,2048);
			String memoryLine = br.readLine();
			String subMemoryLine = memoryLine.substring(memoryLine.indexOf("MemTotal:"));
			br.close();
			long totalMemorySize = Integer.parseInt(subMemoryLine.replaceAll("\\D+", ""));
			long availableSize = getAvailableMemory(context)/1024;
			int percent = (int)(100*(totalMemorySize-availableSize)/totalMemorySize);
			return percent+"%";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "悬浮窗";
	}
	/**更新内存数据
	 * @param context 可传入应用程序上下文
	 */
	public static void updateUserPercent(Context context){
		if(smallWindow!=null){
			TextView tv_percent = (TextView) smallWindow.findViewById(R.id.tv_percent);
			tv_percent.setText(getUserPercentValue(context));
		}
	}
	/**获取当前可用内存，返回数据以字节为单位
	 * @param context 可传入应用程序上下文
	 * @return 当前可用内存
	 */
	private static long getAvailableMemory(Context context){
		ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
		getActivityManager(context).getMemoryInfo(mi);
		return mi.availMem;
	}
	public static boolean isWindowShowing(){
		return smallWindow!=null||bigWindow!=null; 
	}
	public MyWindowManager() {
	}

}
