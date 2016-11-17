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
 *С������
 */
public class FloatWindowSmallView extends LinearLayout {
	/**
	 * ��¼С�������Ŀ��
	 */
	public static int viewWidth;
	/**
	 * ��¼С�������߶�
	 */
	public static int viewHeight;
	/**
	 * ϵͳ״̬���߶�
	 */
	private static int statusBarHeight;
	/**
	 * ���ڸ���С��������λ��
	 */
	private WindowManager windowManager;
	/**
	 * С�������Ĳ���
	 */
	private WindowManager.LayoutParams mParams;
	/**
	 * ��¼��ָ����Ļ�ϵĺ�����
	 */
	private float xInScreen;
	/**
	 * ��¼��ָ����Ļ�ϵ�������
	 */
	private float yInScreen;
	/**
	 * ��ָ����ʱ����Ļ�ϵ�x����
	 */
	private float xDownInScreen;
	/**
	 * ��ָ����ʱ����Ļ��Y���
	 */
	private float yDownInScreen;
	/**
	 * ��view�ϵ�x����
	 */
	private float xInView;
	/**
	 * ��View�ϵ�Y����
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
			//��ָ�ƶ�ʱ�����С��������λ��
			updateViewPosition();
			break;
		case MotionEvent.ACTION_UP:
			//�����ָ�뿪��Ļʱ��xDownInScreen==xInScreen&&yDownInScreen==yInScreen
			//�򵯳����������
			if(xDownInScreen==xInScreen&&yDownInScreen==yInScreen){
				openBigWindow();
			}
			break;
		}
		return true;
	}

	/**
	 * �򿪴���������ͬʱ�ر�С������
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

	/**�û���ȡ״̬���߶�
	 * @return ����״̬���߶ȵ�����ֵ
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
