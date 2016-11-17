package com.example.test22.viewgroup.slidingmenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.test22.R;
import com.example.test22.utils.ScreenUtils;

public class SlidingMenu1 extends HorizontalScrollView {
	private int screenWidth;
	private int mSlidingWidth,halfWidth;
	private int rightPadding=5;
	private LinearLayout mWrapper;
    ViewGroup mLeftMenu,mContent,mRightMenu;
    private boolean once = false;
	public SlidingMenu1(Context context) {
		this(context,null);
	}

	public SlidingMenu1(Context context, AttributeSet attrs) {
		super(context, attrs);
		screenWidth = ScreenUtils.getSreenWidth(context);
		TypedArray arrays = context.obtainStyledAttributes(attrs, R.styleable.SlidingMenu);
		rightPadding = arrays.getDimensionPixelSize(R.styleable.SlidingMenu_rightPadding,(int)TypedValue.applyDimension
				(TypedValue.COMPLEX_UNIT_DIP, 50f, getResources().getDisplayMetrics()));
		arrays.recycle();
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if(!once){
		mSlidingWidth = screenWidth-rightPadding;
		halfWidth = mSlidingWidth/2;
		mWrapper =(LinearLayout) getChildAt(0);
		mLeftMenu = (ViewGroup)mWrapper.getChildAt(0);
		mContent = (ViewGroup)mWrapper.getChildAt(1);
		mRightMenu = (ViewGroup)mWrapper.getChildAt(2);
		mLeftMenu.getLayoutParams().width = mSlidingWidth;
		mContent.getLayoutParams().width = screenWidth;
		mRightMenu.getLayoutParams().width = mSlidingWidth;
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if(changed){
			this.scrollTo(mSlidingWidth, 0);//将菜单隐藏
			once = true;
		}
	}
	private boolean isOperateLeft=false,isOperateRight = false;
	private boolean isLeftOpen=false,isRightOpen=false;
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch(action){
		case MotionEvent.ACTION_UP:
			int x = getScrollX();
			if(isOperateLeft){
				if(isLeftOpen&&x>halfWidth){//关闭菜单
					smoothScrollTo(mSlidingWidth, 0);
					isLeftOpen = false;
				}else if(!isLeftOpen){
					smoothScrollTo(0, 0);
					isLeftOpen = true;
				}
			}
			if(isOperateRight){
				if(!isRightOpen&&x>halfWidth+screenWidth){
					smoothScrollTo(mSlidingWidth+screenWidth, 0);
					isRightOpen = true;
				}else{
					smoothScrollTo(mSlidingWidth, 0);
					isRightOpen = false;
				}
			}
			return true;
		}
		return super.onTouchEvent(ev);
	}
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		Log.e("onScrollchanged", "l=="+l+" mSlidingWidth="+mSlidingWidth);
		if(l>mSlidingWidth) {
			isOperateRight = true;
			isOperateLeft = false;
		}else{
			isOperateRight = false;
			isOperateLeft = true;
		}
	}

	public SlidingMenu1(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
	}

}
