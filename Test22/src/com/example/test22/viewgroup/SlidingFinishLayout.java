package com.example.test22.viewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

/**自定义可以滑动的RelativeLayout,当我们要使用 
 * 此功能的时候，需要将该Activity的顶层布局设置为SildingFinishLayout， 
 * 然后需要调用setTouchView()方法来设置需要滑动的View 
 * @author zc
 *
 */
public class SlidingFinishLayout extends FrameLayout implements OnTouchListener {
	/**
	 * SlidingFinishLayout的父布局
	 */
	private ViewGroup vParent;
	private Scroller scroller;
	private int mTouchSlop;//最小滑动的距离
	private View touchView;//处理滑动逻辑的view
	private int downX;//按下点的x坐标
	private int tempX;//临时存储的x坐标
	private int downY;//按下点y坐标
	private boolean isSliding;//判读是否在滑动
	private int viewWidth;//SlidingFinishLayout的宽度
	private boolean isFinish;
	OnSildingFinishListener onSlidingFinishListener;
	GestureDetector gd;

	public SlidingFinishLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		scroller = new Scroller(context);
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		gd = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener(){
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				int dx = (int) (e2.getX() - e1.getX()); //计算滑动的距离 
				int dy = (int)(e2.getY()-e1.getY());
				if((velocityX>15&&e1.getX()<100)||dx>0&&dx>mTouchSlop&&Math.abs(dy)<mTouchSlop){
					if(onSlidingFinishListener!=null){
						onSlidingFinishListener.onSlidingFinish();
						return true;
					}
				}
				return super.onFling(e1, e2, velocityX, velocityY);
			}
		});
	}
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if(changed){
			vParent = (ViewGroup) this.getParent();
			viewWidth = this.getWidth();
		}
	}
	/**设置Touch的view
	 * @param touchView
	 */
	public void setTouchView(View touchView){
		this.touchView = touchView;
		this.touchView.setOnTouchListener(this);
	}
	public View getTouchView(){
		return this.touchView;
	}
	/** touch的view是否是AbsListView,如ListView,gridView及其子类
	 * @return
	 */
	private boolean isTouchOnAbsListView(){
		return touchView instanceof AbsListView;
	}
	/**touchView是否是ScrollView及其子类
	 * @return
	 */
	private boolean isTouchScrollView(){
		return touchView instanceof ScrollView;
	}
	/**
	 * 滚动出界面
	 */
	private void scrollRight(){
		Log.e("getScrollX()", ""+vParent.getScrollX());
		int delta = viewWidth+vParent.getScrollX();
		scroller.startScroll(vParent.getScrollX(), 0, -delta+1, 0, Math.abs(delta));
	    postInvalidate();	
	}
	/**
	 * 滚动回原点
	 */
	private void scrollOrigin(){
		Log.e("getScrollX()", ""+vParent.getScrollX());
		int delta = vParent.getScrollX();
		scroller.startScroll(vParent.getScrollX(), 0, -delta, 0, Math.abs(delta));
		postInvalidate();
	}
	public SlidingFinishLayout(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public SlidingFinishLayout(Context context) {
		this(context,null);
	}
	public void setOnSlidingFinishListener(OnSildingFinishListener onSlidingFinish){
		this.onSlidingFinishListener = onSlidingFinish;
	}
	public interface OnSildingFinishListener{
		public void onSlidingFinish();
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(gd.onTouchEvent(event))return true;
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			downX = tempX = (int)event.getRawX();
			downY = (int)event.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			int moveX = (int)event.getRawX();
			int deltaX = tempX-moveX;
			tempX = moveX;
			if(Math.abs(moveX-downX)>mTouchSlop&&Math.abs(event.getRawY()-downY)<mTouchSlop){
				isSliding = true;
				if(isTouchOnAbsListView()){
					// 若touchView是AbsListView，  
	                // 则当手指滑动，取消item的点击事件，不然我们滑动也伴随着item点击
					MotionEvent cancelEvent = MotionEvent.obtain(event);
					cancelEvent.setAction(MotionEvent.ACTION_CANCEL  
                            | (event.getActionIndex() << MotionEvent.ACTION_POINTER_INDEX_SHIFT));
					v.onTouchEvent(cancelEvent);
				}
			}
			if(moveX-downX>=0&&isSliding){
				vParent.scrollBy(deltaX, 0);
				//屏蔽在滑动过程中listView、scrollView的滑动事件
				if(isTouchOnAbsListView()||isTouchScrollView()){
					return true;
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			isSliding = false;
			if(vParent.getScrollX()<-viewWidth/3){
				isFinish = true;
				scrollRight();
			}else{
				scrollOrigin();
				isFinish = false;
			}
			break;
		}
		 // 假如touch的view是AbsListView或者ScrollView 我们处理完上面自己的逻辑之后  
        // 再交给AbsListView, ScrollView自己处理其自己的逻辑  
        if (isTouchScrollView() || isTouchOnAbsListView()) {  
            return v.onTouchEvent(event);  
        } 
		return true;
	}
	
	@Override
	public void computeScroll() {
		// 调用startScroll的时候scroller.computeScrollOffset()返回true，  
		if(scroller.computeScrollOffset()){
			vParent.scrollTo(scroller.getCurrX(), scroller.getCurrY());
			postInvalidate();
			if(scroller.isFinished()){
				if(onSlidingFinishListener!=null&&isFinish){
					onSlidingFinishListener.onSlidingFinish();
				}
			}
		}
		
//		super.computeScroll();
	}

}
