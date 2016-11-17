package com.example.test22.viewgroup.folderlayout;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class TouchFoldLayout extends FolderLayout {
	private GestureDetector mSrollGestureDetector;
	public TouchFoldLayout(Context context) {
		this(context,null);
	}

	public TouchFoldLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context,attrs);
	}
	public TouchFoldLayout(Context context, AttributeSet attrs, int defStyle) {
		this(context, attrs);
	}
	private void init(Context context, AttributeSet attrs) {
		mSrollGestureDetector = new GestureDetector(context, new ScrollGuestureDetector());
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return mSrollGestureDetector.onTouchEvent(event);
	}
	private int mTranslation=-1;
	@Override
	protected void dispatchDraw(Canvas canvas) {
		if(mTranslation==-1)
			mTranslation=getWidth();
		super.dispatchDraw(canvas);
	}
	class ScrollGuestureDetector extends GestureDetector.SimpleOnGestureListener{
		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			mTranslation-=distanceX;
			if(mTranslation<0){
				mTranslation=0;
			}
			if(mTranslation>getWidth()){
				mTranslation = getWidth();
			}
			float factor = Math.abs((float)mTranslation/(float)getWidth());
			setFactor(factor);
			return true;
		}
	}

}
