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

/**�Զ�����Ի�����RelativeLayout,������Ҫʹ�� 
 * �˹��ܵ�ʱ����Ҫ����Activity�Ķ��㲼������ΪSildingFinishLayout�� 
 * Ȼ����Ҫ����setTouchView()������������Ҫ������View 
 * @author zc
 *
 */
public class SlidingFinishLayout extends FrameLayout implements OnTouchListener {
	/**
	 * SlidingFinishLayout�ĸ�����
	 */
	private ViewGroup vParent;
	private Scroller scroller;
	private int mTouchSlop;//��С�����ľ���
	private View touchView;//�������߼���view
	private int downX;//���µ��x����
	private int tempX;//��ʱ�洢��x����
	private int downY;//���µ�y����
	private boolean isSliding;//�ж��Ƿ��ڻ���
	private int viewWidth;//SlidingFinishLayout�Ŀ��
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
				int dx = (int) (e2.getX() - e1.getX()); //���㻬���ľ��� 
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
	/**����Touch��view
	 * @param touchView
	 */
	public void setTouchView(View touchView){
		this.touchView = touchView;
		this.touchView.setOnTouchListener(this);
	}
	public View getTouchView(){
		return this.touchView;
	}
	/** touch��view�Ƿ���AbsListView,��ListView,gridView��������
	 * @return
	 */
	private boolean isTouchOnAbsListView(){
		return touchView instanceof AbsListView;
	}
	/**touchView�Ƿ���ScrollView��������
	 * @return
	 */
	private boolean isTouchScrollView(){
		return touchView instanceof ScrollView;
	}
	/**
	 * ����������
	 */
	private void scrollRight(){
		Log.e("getScrollX()", ""+vParent.getScrollX());
		int delta = viewWidth+vParent.getScrollX();
		scroller.startScroll(vParent.getScrollX(), 0, -delta+1, 0, Math.abs(delta));
	    postInvalidate();	
	}
	/**
	 * ������ԭ��
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
					// ��touchView��AbsListView��  
	                // ����ָ������ȡ��item�ĵ���¼�����Ȼ���ǻ���Ҳ������item���
					MotionEvent cancelEvent = MotionEvent.obtain(event);
					cancelEvent.setAction(MotionEvent.ACTION_CANCEL  
                            | (event.getActionIndex() << MotionEvent.ACTION_POINTER_INDEX_SHIFT));
					v.onTouchEvent(cancelEvent);
				}
			}
			if(moveX-downX>=0&&isSliding){
				vParent.scrollBy(deltaX, 0);
				//�����ڻ���������listView��scrollView�Ļ����¼�
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
		 // ����touch��view��AbsListView����ScrollView ���Ǵ����������Լ����߼�֮��  
        // �ٽ���AbsListView, ScrollView�Լ��������Լ����߼�  
        if (isTouchScrollView() || isTouchOnAbsListView()) {  
            return v.onTouchEvent(event);  
        } 
		return true;
	}
	
	@Override
	public void computeScroll() {
		// ����startScroll��ʱ��scroller.computeScrollOffset()����true��  
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
