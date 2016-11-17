package com.example.test22.viewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class LinearLayoutMove extends LinearLayout {

	public LinearLayoutMove(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public LinearLayoutMove(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public LinearLayoutMove(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	float orginX,orginY;
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		int act = event.getAction();
		float nowX = event.getX();
		float nowY = event.getY();
		switch(act){
		case MotionEvent.ACTION_DOWN:
			orginX = nowX;
			orginY = nowY;
			break;
		case MotionEvent.ACTION_MOVE:
			float dx = nowX-orginX;
			float dy = nowY-orginY;
			orginX = nowX;
			orginY = nowY;
			if(Math.abs(dx)>Math.abs(dy)){
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			break;
		}
		return super.onInterceptTouchEvent(event);
	}
	float orginX1,orginY1;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int act = event.getAction();
		float nowX = event.getX();
		float nowY = event.getY();
		switch(act){
		case MotionEvent.ACTION_DOWN:
			orginX1 = nowX;
			orginY1 = nowY;
			break;
		case MotionEvent.ACTION_MOVE:
			float dx = nowX-orginX1;
			float dy = nowY-orginY1;
			orginX1 = nowX;
			orginY1 = nowY;
			if(Math.abs(dx)>Math.abs(dy)){
				scrollBy(-(int) dx, 0);
			}
			break;
		case MotionEvent.ACTION_UP:
			break;
		}
		return super.onTouchEvent(event);
	}

}
