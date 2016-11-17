package com.example.test22.viewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

public class HorizontalScrollListView extends ListView {
	private final String TAG = "HorizontalScrollListView";
	private GestureDetector mGestureDetector;
	View.OnTouchListener mGestureListener;

	public HorizontalScrollListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public HorizontalScrollListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mGestureDetector = new GestureDetector(new YScrollDetector());  
        setFadingEdgeLength(0);  
	}

	public HorizontalScrollListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		Log.e("onInterceptTouchEvent", ""+(super.onInterceptTouchEvent(ev) || mGestureDetector.onTouchEvent(ev)));  
        return super.onInterceptTouchEvent(ev)  
                || mGestureDetector.onTouchEvent(ev); 
	}
	 class YScrollDetector extends SimpleOnGestureListener {  
	        @Override  
	        public boolean onScroll(MotionEvent e1, MotionEvent e2,  
	                float distanceX, float distanceY) {  
	            if(distanceY !=0 && distanceX != 0){  
	                if (Math.abs(distanceY) >= Math.abs(distanceX)) {  
	                    return true;  
	                }  
	                return false;     
	            }  
	            return false;  
	        }  
	    }  
}
