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
	 * С��������view����
	 */
	private static LayoutParams smallLayoutParams;
	/**
	 * ����������view����
	 */
	private static LayoutParams bigLayoutParams;
	/**
	 * ���ڿ�������Ļ����ӻ��Ƴ�������
	 */
	private static WindowManager mWindowManager;
	/**
	 * ��ȡ�ֻ������ڴ�
	 */
	private static ActivityManager mActivityManager;
	/**����һ��С����������ʼλ��Ϊ��Ļ�����м�λ��
	 * @param context ����ΪӦ�ó����context
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
				//����ͼƬ��ʽ��Ч��Ϊ����͸��  
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
	/**��С����������Ļ���Ƴ�
	 * @param context ����ΪӦ�ó����context
	 */
	public static void removeSmallWindow(Context context){
		if(smallWindow!=null){
			WindowManager windowManager = getWindowManager(context);
			windowManager.removeView(smallWindow);
			smallWindow=null;
		}
	}
	/**����һ�������������λ��Ϊ��Ļ�м�
	 * @param context ����ΪӦ�ó����context
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
	/**��������������Ļ���Ƴ�
	 * @param context ����ΪӦ�ó����context
	 */
	public static void removeBigWindow(Context context){
		if(bigWindow!=null){
			WindowManager windowManager = getWindowManager(context);
			windowManager.removeView(bigWindow);
			bigWindow=null;
		}
	}
	/**
	 * @param context ����ΪӦ�ó����conext
	 * @return WindowManager ʵ�������ڿ�������������Ļ�ϵ���ӻ��Ƴ�
	 */
	private static WindowManager getWindowManager(Context context){
		if(mWindowManager==null){
			mWindowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		}
		return mWindowManager;
	}
	/**
	 * @param context �ɴ���Ӧ�ó���context
	 * @return ActivityManager ��ʵ�������ڻ�ȡ�ֻ��ڴ�
	 */
	private static ActivityManager getActivityManager(Context context){
		if(mActivityManager==null){
			mActivityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		}
		return mActivityManager;
	}
	/** ������ʹ���ڴ�İٷֱ�
	 * @param context �ɴ���Ӧ�ó���������
	 * @return ��ʹ���ڴ�İٷֱȣ����ַ�����ʽ����
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
		return "������";
	}
	/**�����ڴ�����
	 * @param context �ɴ���Ӧ�ó���������
	 */
	public static void updateUserPercent(Context context){
		if(smallWindow!=null){
			TextView tv_percent = (TextView) smallWindow.findViewById(R.id.tv_percent);
			tv_percent.setText(getUserPercentValue(context));
		}
	}
	/**��ȡ��ǰ�����ڴ棬�����������ֽ�Ϊ��λ
	 * @param context �ɴ���Ӧ�ó���������
	 * @return ��ǰ�����ڴ�
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
