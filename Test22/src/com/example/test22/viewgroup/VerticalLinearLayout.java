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
	private int mScreenHeight;//��Ļ�ĸ߶�
	private Scroller mScroller;//����������
	private VelocityTracker mVelocityTracker;//���ٶȼ��
	private int mMaximumVelocity,mMinmumVelocity;//��󻬶��ٶȺ���С�Ļ����ٶ�
	private int mStartY;//��ָ�����ǻ�ȡ��getScrollY
	private int mEndY;//��ָ̧���ʱ���ȡ��getScrollY;
	private int mLastY;//��¼�ƶ�ʱ��Y
	private boolean isScroll = false;//�Ƿ��ڹ���
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
			//���������ֵĸ߶�
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
		//�����ǰ�����ƶ������ø����onTouchEvent;
		//scrollBy(0, 100);y����Ϊ�������Ϲ���
		if(isScrolling)return super.onTouchEvent(event);
		int action = event.getAction();
		int y = (int)event.getY();//��ָ���µ�λ��
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
			//�߽���
			int scrollY = getScrollY();
			Log.e("getScrollY()", ""+scrollY);
			int dy = mLastY-y;
			Log.e("dy==", ""+mLastY+"-"+y);
			//�Ѿ����ﶥ�ˣ��������٣��������ƶ�����
			if(dy<0&&scrollY+dy<0){
				dy=-scrollY;
			}
			//�Ѿ�����ײ����������٣��������ƶ�����
			if(dy>0&&scrollY+dy>getHeight()-mScreenHeight){
				dy = getHeight()-mScreenHeight-scrollY;
				break;
			}
			//yΪ���������ƶ���Ϊ���������ƶ�
			scrollBy(0, dy);
			mLastY = y;
			break;
		case MotionEvent.ACTION_UP:
			mEndY = getScrollY();
			int dScrollY = mEndY-mStartY;
//			scrollTo(0, 1*mScreenHeight);
			if(wantScrollToNext()){//���ϻ���
				if(shouldScrollToNext()){
					//mScreenHeight-dScrollY,����mScroller����Ҫ�����ľ���
					mScroller.startScroll(0, getScrollY(), 0, mScreenHeight-dScrollY);
				}else{
					//-dScrollY,����Ǹ����������ƶ�����computeScroll()�����viewҲ�������ƶ�
					mScroller.startScroll(0, getScrollY(), 0, -dScrollY);
					//scrollTo(0, 0);
				}
			}
			if(wantScrollToPre()){//���»���
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
	 * �Ƿ������ϻ�������һҳ
	 */
	private boolean wantScrollToNext(){
		return mEndY>mStartY;
	}
	/**�ܷ���һҳ
	 * @return
	 */
	private boolean shouldScrollToNext(){
		return mEndY-mStartY>mScreenHeight/3||(Math.abs(getVelocity())>600);
	}
	/**�Ƿ������»�������һҳ
	 * @return
	 */
	private boolean wantScrollToPre(){
		return mEndY<mStartY;
	}
	/**�ܷ񻬶�����һҳ
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
			Log.e("�ڼ�ҳ",""+position);
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
