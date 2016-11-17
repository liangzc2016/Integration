package com.example.test22.viewgroup.slidingmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

public class MyLiearLayout extends LinearLayout {
	private static int index=0;

	public MyLiearLayout(Context context) {
		super(context);
	}

	public MyLiearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		setChildrenDrawingOrderEnabled(true);
	}

	public MyLiearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	@Override
	protected int getChildDrawingOrder(int childCount, int i) {
//		Log.e("getChildDrawingOrder==", i+"");
		
		if(i==0){
			Log.e("return", 1+"");
			index+=1;
			return 1;
		}
		if(i==2){
			index=0;
			Log.e("return", 2+"");
			return 2;
		}
		if(i==1){
			index+=1;
			Log.e("return", 0+"");
			return 0;
		}
		return super.getChildDrawingOrder(childCount, i);
	}

}
