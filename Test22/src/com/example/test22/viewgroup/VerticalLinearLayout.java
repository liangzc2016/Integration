package com.example.test22.viewgroup;


import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Scroller;

public class VerticalLinearLayout extends ViewGroup {
	private int mScreenHeight;//屏幕的高度
	private Scroller mScroller;//滑动辅助类
	private VelocityTracker mVelocityTracker;//加速度检测
	private int mMaximumVelocity,mMinmumVelocity;//最大滑动速度和最小的滑动速度
	private int mStartY;//手指按下是获取的getScrollY
	private int mEndY;//手指抬起的时候获取的getScrollY;
	private int mLastY;//记录移动时的Y
	private boolean isScroll = false;//是否在滚动
	public VerticalLinearLayout(Context context) {
		this(context,null);
	}

	public VerticalLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		mScreenHeight = outMetrics.heightPixels;
		Log.e("mScreenHeight", ""+outMetrics.heightPixels);
		mScroller = new Scroller(context);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int childCount = getChildCount();
		for(int i=0;i<childCount;i++){
			View child = getChildAt(i);
			measureChild(child, widthMeasureSpec, heightMeasureSpec);
		}
	}
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if(changed){
			int count = getChildCount();
			//设置主布局的高度
			MarginLayoutParams lp = (MarginLayoutParams)getLayoutParams();
			lp.height = count*mScreenHeight;
			setLayoutParams(lp);
			for(int i=0;i<count;i++){
				View v = getChildAt(i);
				if(v.getVisibility()!=View.GONE)
				v.layout(l, i*mScreenHeight, r, (i+1)*mScreenHeight);
			}
		}
		//super.onLayout(changed, l, t, r, b);
	}
	private boolean isScrolling =false;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//如果当前正在移动，调用父类的onTouchEvent;
		//scrollBy(0, 100);y方向为正则向上滚动
		if(isScrolling)return super.onTouchEvent(event);
		int action = event.getAction();
		int y = (int)event.getY();//手指按下的位置
		obainVelocity(event);
		switch(action){
		case MotionEvent.ACTION_DOWN:
			mStartY = getScrollY();
			mLastY = y;
			break;
		case MotionEvent.ACTION_MOVE:
			if(!mScroller.isFinished()){
				mScroller.abortAnimation();
			}
			//边界检测
			int scrollY = getScrollY();
			Log.e("getScrollY()", ""+scrollY);
			int dy = mLastY-y;
			Log.e("dy==", ""+mLastY+"-"+y);
			//已经到达顶端，下拉多少，就往上移动多少
			if(dy<0&&scrollY+dy<0){
				dy=-scrollY;
			}
			//已经到达底部，上拉多少，就往下移动多少
			if(dy>0&&scrollY+dy>getHeight()-mScreenHeight){
				dy = getHeight()-mScreenHeight-scrollY;
				break;
			}
			//y为负则向上移动，为正则向下移动
			scrollBy(0, dy);
			mLastY = y;
			break;
		case MotionEvent.ACTION_UP:
			mEndY = getScrollY();
			int dScrollY = mEndY-mStartY;
//			scrollTo(0, 1*mScreenHeight);
			if(wantScrollToNext()){//往上滑动
				if(shouldScrollToNext()){
					//mScreenHeight-dScrollY,代表mScroller还需要滚动的距离
					mScroller.startScroll(0, getScrollY(), 0, mScreenHeight-dScrollY);
				}else{
					//-dScrollY,如果是负数就向下移动，在computeScroll()方法里，view也是向下移动
					mScroller.startScroll(0, getScrollY(), 0, -dScrollY);
					//scrollTo(0, 0);
				}
			}
			if(wantScrollToPre()){//向下滑动
				if(shouldScrollToPre()){
					mScroller.startScroll(0, getScrollY(), 0, -mScreenHeight-dScrollY);
				}else{
					mScroller.startScroll(0, getScrollY(), 0, -dScrollY);
				}
			}
			isScrolling = true;
			postInvalidate();
			recyleVelocity();
			break;
		}
		return true;
	}
	private void recyleVelocity(){
		if(mVelocityTracker!=null){
			mVelocityTracker.recycle();
			mVelocityTracker = null;
		}
	}
	public void obainVelocity(MotionEvent event){
		if(mVelocityTracker==null){
			mVelocityTracker=VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
	}
	/**
	 * 是否想往上滑动到下一页
	 */
	private boolean wantScrollToNext(){
		return mEndY>mStartY;
	}
	/**能否到下一页
	 * @return
	 */
	private boolean shouldScrollToNext(){
		return mEndY-mStartY>mScreenHeight/3||(Math.abs(getVelocity())>600);
	}
	/**是否想往下滑动到上一页
	 * @return
	 */
	private boolean wantScrollToPre(){
		return mEndY<mStartY;
	}
	/**能否滑动到上一页
	 * @return
	 */
	private boolean shouldScrollToPre(){
		return -mEndY+mStartY>mScreenHeight/3||(Math.abs(getVelocity())>600);
	}
	int currentPage=0;
	OnPageChangeLisener listener;
	@Override
	public void computeScroll() {
		super.computeScroll();
		if(mScroller.computeScrollOffset()){
			Log.e("mScroller.getCurrY()", mScroller.getCurrY()+"");
			scrollTo(0, mScroller.getCurrY());
			postInvalidate();
		}else{
			int position = getScrollY()/mScreenHeight;
			Log.e("第几页",""+position);
			if(position!=currentPage){
				if(listener!=null){
					currentPage = position;
					listener.OnPageChange(currentPage);
				}
			}
			isScrolling = false;
		}
	}

	public VerticalLinearLayout(Context context, AttributeSet attrs,
			int defStyle) {
		this(context, attrs);
	}
	public interface OnPageChangeLisener{
		void OnPageChange(int currentPage);
	}
	public void setOnPageChangeLisener(OnPageChangeLisener listener){
		this.listener = listener;
	}
	private int getVelocity(){
		mVelocityTracker.computeCurrentVelocity(1000);
		return (int)mVelocityTracker.getYVelocity();
	}

}
