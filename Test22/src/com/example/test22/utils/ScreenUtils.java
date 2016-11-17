package com.example.test22.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;


public class ScreenUtils {
	public static int getSreenWidth(Context context) {
		WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay().getWidth();
		//metrics.heightPixels;
	}
	/**
	 * �����ֻ��ķֱ��ʴ� dp �ĵ�λ ת��Ϊ px(����)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

}
