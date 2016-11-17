package com.example.test22.viewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.OverScroller;
import android.widget.ScrollView;

import com.example.test22.R;

/**
 * @author zc
 *仿360详情页
 */
public class StickyNavLayout extends LinearLayout {
	private View mTop;
	private View mNav;
	private ScrollView content;
	private ListView lv;
	private int mTopViewHeight;
	private ScrollView mInnerScrollView;
	private boolean isTopHidden;
	
	private OverScroller mScroller;
	private VelocityTracker mVelocityTracker;
	private int mTouchSlop;
	private int mMaximumVelocity,mMinmumVelocity;
	private float mLastY;
	private boolean isDragging;
	public StickyNavLayout(Context context) {
		this(context,null);
	}

	public StickyNavLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOrientation(LinearLayout.VERTICAL);
		
		mScroller = new OverScroller(context);
		mVelocityTracker = VelocityTracker.obtain();
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		mMaximumVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
		mMinmumVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
	}
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mTop = findViewById(R.id.topView);
		content = (ScrollView)findViewById(R.id.scrollView);
		lv = (ListView)findViewById(R.id.listView);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		//设置内容的高度
		ViewGroup.LayoutParams params = content.getLayoutParams();
		params.height = getMeasuredHeight()-lv.getMeasuredHeight();
	}
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mTopViewHeight = mTop.getMeasuredHeight();
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mVelocityTracker.addMovement(event);
		int action  = event.getAction();
		float y = event.getY();
		switch(action){
		case MotionEvent.ACTION_DOWN:
			if(!mScroller.isFinished()){
				mScroller.abortAnimation();
			}
			mVelocityTracker.clear();
			mVelocityTracker.addMovement(event);
			mLastY = y;
			return true;
//			break;
		case MotionEvent.ACTION_MOVE:
			float dy = y-mLastY;
			if(!isDragging&&Math.abs(dy)>mTouchSlop){
				isDragging = true;
			}
			if(isDragging){
				scrollBy(0, (int) -dy);
				mLastY = y;
			}
			break;
		case MotionEvent.ACTION_CANCEL:
			isDragging = false;
			if(mScroller.isFinished())
				mScroller.abortAnimation();
			break;
		case MotionEvent.ACTION_UP:
			isDragging = false;
			mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
			int velocityY = (int) mVelocityTracker.getYVelocity();
			if(Math.abs(velocityY)>mMinmumVelocity){
				fling(-velocityY);
			}
			mVelocityTracker.clear();
			break;
		}
		return super.onTouchEvent(event);
	}
	public void fling(int velocity){
		mScroller.fling(0, getScrollY(), 0, velocity, 0, 0,0,mTopViewHeight);
		invalidate();
	}
	@Override
	public void computeScroll() {
		if(mScroller.computeScrollOffset()){//并没有滑动完
			scrollTo(0, mScroller.getCurrY());
			Log.e("mScroller.getCurrY()", ""+mScroller.getCurrY());
//			invalidate();
		}
	}
	@Override
	public void scrollTo(int x, int y) {
		Log.e("getCurrY()&&GetScrollY()==", ""+y+" "+getScrollY());
		if(y<0){
			y=0;
		}
		if(y>mTopViewHeight){
			y = mTopViewHeight;
		}
		if(y!=getScrollY()){
			super.scrollTo(x, y);
		}
		isTopHidden=getScrollY()==mTopViewHeight;
		if(isTopHidden){
			Log.e("isTopHidden", "true");
		}
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		float y = ev.getY();
		switch(action){
		case MotionEvent.ACTION_DOWN:
			mLastY = y;
			break;
		case MotionEvent.ACTION_MOVE:
			float dy = y-mLastY;
			if(Math.abs(dy)>mTouchSlop){
				isDragging = true;
				if(!isTopHidden||content.getScrollY()==0&&isTopHidden&&dy>0){
					return true;
				}
			}
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}

	public StickyNavLayout(Context context, AttributeSet attrs, int defStyle) {
		this(context, attrs);
	}

}
