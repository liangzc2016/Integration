package com.example.test22.view;

import com.example.test22.R;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ChangeView extends View {
	private Paint mPaint;
	private int radius,r;
	private float cx,cy;
	private float width,height;
	private boolean isDrawing = false;
	public ChangeView(Context context) {
		super(context,null);
	}

	public ChangeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		radius = 0;
		cx = 0;
		cy = 0;
		r=0;
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(Color.GRAY);
	}

	public ChangeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
	}
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		width = getMeasuredWidth();
		height=getMeasuredHeight();
	}
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawCircle(cx, cy, r, mPaint);
		if(r==0)
		canvas.drawColor(R.color.blue);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			if(!isDrawing){
				cx = event.getX();
				cy = event.getY();
				calRadius();
				invalidate();
			}
			break;
		}
		return true;
	}
	public void calRadius(){
		float y1 = (height-cy);
		float x1 = width-cx;
		float left_r1 = (float) Math.sqrt(cx*cx+cy*cy);
		float left_r2 = (float)Math.sqrt(cx*cx+(height-cy)*(height-cy));
		float right_r1 = (float)Math.sqrt(x1*x1+cy*cy);
		float right_r2 = (float)Math.sqrt(x1*x1+y1*y1);
		radius = (int) (left_r1>left_r2?left_r1:left_r2);
		radius = (int) (radius>right_r1?radius:right_r1);
		radius = (int) (radius>right_r2?radius:right_r2);
		startAnimation();
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				while(r<=radius&&radius>0){
//					r=r+20;
//					try {
//						Thread.sleep(50);
//						handler.sendEmptyMessage(0);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}).start();
	}
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(r>=radius){
				r=0;
				radius=0;
			}
			invalidate();
		};
	};
	private void startAnimation(){
		isDrawing = true;
		ValueAnimator anim = ValueAnimator.ofInt(0,radius);
		anim.setDuration(1500);
		anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				r = (int)animation.getAnimatedValue();
				invalidate();
			}
		});
		anim.addListener(new AnimatorListenerAdapter(){
			@Override
			public void onAnimationEnd(Animator animation) {
				r=0;
				radius=0;
				isDrawing = false;
				invalidate();
				super.onAnimationEnd(animation);
			}
			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub
				super.onAnimationCancel(animation);
			}
		});
		anim.start();
	}
}
