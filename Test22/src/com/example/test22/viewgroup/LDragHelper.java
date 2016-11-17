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
				//return true��ʾ���Բ����view������Ը��ݴ���ĵ�һ������������Щview���Ա�����
				//mEdgeTrackerView��ֱֹ���ƶ�������ֱ���ƶ�ǰ������
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
			//��ָ�ͷŵ�ʱ��ص�
			@Override
			public void onViewReleased(View releasedChild, float xvel,
					float yvel) {
				//mAutoBackView��ָ�ͷ�ʱ�Զ���ȥ
				if(releasedChild==mAutoBackView){
					mDragger.settleCapturedViewAt(mOrginPos.x, mOrginPos.y);
					invalidate();
				}
				//super.onViewReleased(releasedChild, xvel, yvel);
			}
			//�ڱ߽��϶���ʱ��ص�����
			@Override
			public void onEdgeDragStarted(int edgeFlags, int pointerId) {
				//���߽��ϻ�����ʱ��Ҫ�����view
				mDragger.captureChildView(mEdgeTrackerView, pointerId);
				//super.onEdgeDragStarted(edgeFlags, pointerId);
			}
			/*��View�������¼�����ô�������ƣ�DOWN-MOVE*-UP������ֱ�ӽ���onTouchEvent��
			 * ��onTouchEvent��DOWN��ʱ���ȷ����captureView����������¼�����ô�ͻ�����onInterceptTouchEvent�������ж��Ƿ���Բ���
			 * �����жϵĹ����л�ȥ�ж����������ص��ķ�����getViewHorizontalDragRange��getViewVerticalDragRange��ֻ���������������ش���0��ֵ���������Ĳ���
			���ԣ��������Button���ԣ����߸�TextView�����clickable = true �����ǵ���д����������������*/
			@Override
			public int getViewHorizontalDragRange(View child) {
				 return getMeasuredWidth()-child.getMeasuredWidth();
			}
			@Override
			public int getViewVerticalDragRange(View child) {
				  return getMeasuredHeight()-child.getMeasuredHeight();
			}
		});
		//���û�����߽��ʱ����Բ���view
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
