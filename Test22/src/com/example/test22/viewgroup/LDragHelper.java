package com.example.test22.viewgroup;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
public class LDragHelper extends LinearLayout {
    private ViewDragHelper mDragger;
    private View mDragView,mAutoBackView,mEdgeTrackerView;
    private Point mOrginPos = new Point();
    
	public LDragHelper(Context context, AttributeSet attrs, int defStyle) {
		this(context, attrs);
	}
	public LDragHelper(Context context) {
		this(context,null);
	}
	public LDragHelper(Context context, AttributeSet attrs) {
		super(context, attrs);
		mDragger = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
			
			@Override
			public boolean tryCaptureView(View view, int pointerId) {
				//return true表示可以捕获该view，你可以根据传入的第一个参数决定哪些view可以被捕获
				//mEdgeTrackerView禁止直接移动，可以直接移动前面两个
				return view==mAutoBackView||view==mDragView;
				
			}
			@Override
			public int clampViewPositionHorizontal(View child, int left, int dx) {
				final int leftBound = getPaddingLeft();
				final int rightBound = getWidth()-child.getWidth()-getPaddingRight();
				final int newLeft=Math.min(Math.max(left, leftBound),rightBound);
				return newLeft;
			}
			@Override
			public int clampViewPositionVertical(View child, int top, int dy) {
				return top;
			}
			//手指释放的时候回调
			@Override
			public void onViewReleased(View releasedChild, float xvel,
					float yvel) {
				//mAutoBackView手指释放时自动回去
				if(releasedChild==mAutoBackView){
					mDragger.settleCapturedViewAt(mOrginPos.x, mOrginPos.y);
					invalidate();
				}
				//super.onViewReleased(releasedChild, xvel, yvel);
			}
			//在边界拖动的时候回调方法
			@Override
			public void onEdgeDragStarted(int edgeFlags, int pointerId) {
				//当边界上滑动的时候，要捕获的view
				mDragger.captureChildView(mEdgeTrackerView, pointerId);
				//super.onEdgeDragStarted(edgeFlags, pointerId);
			}
			/*子View不消耗事件，那么整个手势（DOWN-MOVE*-UP）都是直接进入onTouchEvent，
			 * 在onTouchEvent的DOWN的时候就确定了captureView。如果消耗事件，那么就会先走onInterceptTouchEvent方法，判断是否可以捕获，
			 * 而在判断的过程中会去判断另外两个回调的方法：getViewHorizontalDragRange和getViewVerticalDragRange，只有这两个方法返回大于0的值才能正常的捕获。
			所以，如果你用Button测试，或者给TextView添加了clickable = true ，都记得重写下面这两个方法：*/
			@Override
			public int getViewHorizontalDragRange(View child) {
				 return getMeasuredWidth()-child.getMeasuredWidth();
			}
			@Override
			public int getViewVerticalDragRange(View child) {
				  return getMeasuredHeight()-child.getMeasuredHeight();
			}
		});
		//设置滑动左边界的时候可以捕获view
		mDragger.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return mDragger.shouldInterceptTouchEvent(ev);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mDragger.processTouchEvent(event);
		return true;
	}
	@Override
	public void computeScroll() {
		if(mDragger.continueSettling(true)){
			invalidate();
		}
		//super.computeScroll();
	}
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		mOrginPos.x = mAutoBackView.getLeft();
		mOrginPos.y = mAutoBackView.getRight();
	}
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mDragView = getChildAt(0);
		mAutoBackView = getChildAt(1);
		mEdgeTrackerView = getChildAt(2);
	}
	
	
}
