package com.example.test22.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.example.test22.R;
import com.example.test22.utils.ScreenUtils;

/**
 * @author zc
 *侧滑删除
 */
public class SlideView extends LinearLayout {
	private Context context;
	private LinearLayout mContent;//具体的内容
	private RelativeLayout mHolder;//隐藏部分的内容
	private Scroller mScroller;
	private int mHolderWidth = 120;
	private OnSlideListener mOnSlideListener;
	private static final int TAN=2;
	public interface OnSlideListener{
		public static final int SLIDE_STATE_OFF=0;
		public static final int SLIDE_STATE_SCROLL=1;
		public static final int SLIDE_STATE_ON=2;
		/**
		 * @param view 当前滑动的view
		 * @param state 状态，SLIDE_STATE_ON或SLIDE_STATE_OFF
		 */
		public void onSlideListener(View view,int state);
	}
	public SlideView(Context context) {
		this(context,null);
	}

	public SlideView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
    private int screenWidth;
	private void initView(Context context) {
		screenWidth = ScreenUtils.getSreenWidth(context);
		setOrientation(LinearLayout.HORIZONTAL);
		View.inflate(getContext(), R.layout.slide_view_merge, this);
		mContent = (LinearLayout)findViewById(R.id.ll_content);
		mHolder = (RelativeLayout)findViewById(R.id.rl_holder);
		mScroller = new Scroller(context);
		mHolderWidth = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mHolderWidth, getResources().getDisplayMetrics());
		mHolder.getLayoutParams().width = mHolderWidth;
		mHolder.setVisibility(View.VISIBLE);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		//设置内容的高度
		ViewGroup.LayoutParams params = this.getLayoutParams();
		params.width = screenWidth+mHolderWidth;
		ViewGroup.LayoutParams params2 = mContent.getLayoutParams();
		params2.width = screenWidth;
		/*ViewGroup.LayoutParams params3 = mHolder.getLayoutParams();
		params3.width = mHolderWidth;*/
	}
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
	}
	public void setBtnText(CharSequence text){
		TextView t = (TextView)findViewById(R.id.bt_del);
		t.setText(text);
	}
	public void setContentView(View view){
		mContent.addView(view);
	}
	public void setOnSlideListener(OnSlideListener mOnSlideListener){
		this.mOnSlideListener = mOnSlideListener;
	}
	public void shrink(){
		if(getScrollX()!=0){
			this.smoothScrollTo(0,0);
		}
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return onRequireTouchEvent(event);
//		return true;
	}
	int mLastX=0,mLastY=0;
	public boolean onRequireTouchEvent(MotionEvent event){
		//scrollBy(-50, 0); X为负数，向右移动，否则向左移动
		int x = (int)event.getX();
		int y = (int)event.getY();
		int scrollX = getScrollX();
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:{
			Log.e("onRequireTouchEvent", "ACTION_DOWN");
			if(!mScroller.isFinished()){
				mScroller.abortAnimation();
			}
			if(mOnSlideListener!=null){
				mOnSlideListener.onSlideListener(this, OnSlideListener.SLIDE_STATE_ON);
			}
			break;
		}
		case MotionEvent.ACTION_MOVE:{
			Log.e("onRequireTouchEvent", "ACTION_MOVE");
			int deltaX = x-mLastX;
			int deltaY = y-mLastY;
			Log.e("deltaX", ""+deltaX);
			if(Math.abs(deltaX)<Math.abs(deltaY)*TAN){
				getParent().requestDisallowInterceptTouchEvent(false);
//				return false;
				break;
			}
			
			int newScrollX2 = scrollX-deltaX;
			if(deltaX!=0){
				if(newScrollX2<0){
					newScrollX2=0;
				}else if(newScrollX2>mHolderWidth){
					newScrollX2=mHolderWidth;
				}
				scrollTo(newScrollX2, 0);
			}
//			break;
			mLastX = x;
			mLastY = y;
		return true;
		}
		case MotionEvent.ACTION_CANCEL:{
			Log.e("onRequireTouchEvent", "ACTION_Cancel");
			smoothScrollTo(0, 0);
			break;
		}
		case MotionEvent.ACTION_UP:{
			Log.e("onRequireTouchEvent", "ACTION_up");
			int newScrollX = 0;
			if(scrollX>mHolderWidth/2){
				newScrollX = mHolderWidth;
			}
			smoothScrollTo(newScrollX, 0);
			if(mOnSlideListener!=null){
				int state = newScrollX==0?OnSlideListener.SLIDE_STATE_OFF:OnSlideListener.SLIDE_STATE_ON;
				mOnSlideListener.onSlideListener(this, state);
				}
			getParent().requestDisallowInterceptTouchEvent(false);
			break;
//			return false;
			}
		}
		return true;
	}
	private void smoothScrollTo(int destX,int destY){
	    //缓慢滚动到指定位置
		int scrollX = getScrollX();
		int delta = destX-scrollX;
		mScroller.startScroll(scrollX,0,delta,0,Math.abs(delta)*3);
		invalidate();
	}
	@Override
	public void computeScroll() {
		if(mScroller.computeScrollOffset()){
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
		super.computeScroll();
	}

	public SlideView(Context context, AttributeSet attrs, int defStyle) {
		this(context, attrs);
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
//		Log.e("dispatchTouchEvent", ""+ev.getAction());
//		getParent().requestDisallowInterceptTouchEvent(true);
	    switch (ev.getAction()) {
	    case MotionEvent.ACTION_DOWN:
	    	Log.e("dispatchTouchEvent", "down");
//			getParent().requestDisallowInterceptTouchEvent(true);
			break;
		case MotionEvent.ACTION_MOVE:
			Log.e("dispatchTouchEvent", "move");
			getParent().requestDisallowInterceptTouchEvent(true);
			break;
		case MotionEvent.ACTION_CANCEL:
			Log.e("dispatchTouchEvent", "cancel");
//			getParent().requestDisallowInterceptTouchEvent(true);
			break;
		case MotionEvent.ACTION_UP:
			Log.e("dispatchTouchEvent", "up");
			getParent().requestDisallowInterceptTouchEvent(false);
			break;
		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

}
