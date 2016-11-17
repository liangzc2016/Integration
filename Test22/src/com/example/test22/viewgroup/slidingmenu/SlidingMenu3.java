package com.example.test22.viewgroup.slidingmenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.example.test22.R;
import com.example.test22.utils.ScreenUtils;
import com.nineoldandroids.view.ViewHelper;

/**
 * @author zc
 *�˵�������֮�µ�˫��໬
 */
public class SlidingMenu3 extends HorizontalScrollView {
	private int screenWidth;
	private int mSlidingWidth,halfWidth;
	private int rightPadding=5;
	private LinearLayout mWrapper;
    ViewGroup mLeftMenu,mContent,mRightMenu;
    private boolean once = false;
	public SlidingMenu3(Context context) {
		this(context,null);
	}

	public SlidingMenu3(Context context, AttributeSet attrs) {
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
			this.scrollTo(mSlidingWidth, 0);//���˵�����
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
			Log.e("scrollX==", ""+x);
			if(isOperateLeft){
				if(isLeftOpen&&x>halfWidth){//�رղ˵�
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
	/* (non-Javadoc)
	 * @see android.view.View#onScrollChanged(int, int, int, int)
	 */
	/* (non-Javadoc)
	 * @see android.view.View#onScrollChanged(int, int, int, int)
	 */
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		//l��ֵӦ���ǵ�ǰ���Լ�����ͼ�����Ͻǵ����꣬
		super.onScrollChanged(l, t, oldl, oldt);
		Log.e("onScrollchanged", "l=="+l+" mSlidingWidth="+mSlidingWidth);
		if(l>mSlidingWidth) {
			isOperateRight = true;
			isOperateLeft = false;
			float scale = l*1.0f/mSlidingWidth;
			/*�ڶ���������ָ���ƶ��ľ��� �����ٶ��ο����� �ʼ��λ������λ��Ϊ0������ôViewHelper.setTranslationY(view,100)���ǰ�view���£�����ԭʼ��λ�ã��ƶ�100����ô120�� 130�� 140 150��
			���Ǳ���ԭʼ��λ�ö�10����20����30����40������*/
			ViewHelper.setTranslationX(mRightMenu, -(int)mSlidingWidth*(2-scale));
		}else{
			float scale = l*1.0f/mSlidingWidth;
			isOperateRight = false;
			isOperateLeft = true;
			ViewHelper.setTranslationX(mLeftMenu, mSlidingWidth*scale);
		}
	}

	public SlidingMenu3(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
	}
	public void translateView(View view,int start){
		TranslateAnimation animation = new TranslateAnimation(0, start, 0, 0);
		//animation.setInterpolator(new OvershootInterpolator());
		animation.setDuration(1);
		animation.setFillAfter(true);
		view.startAnimation(animation);
	}

}
