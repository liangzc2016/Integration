package com.example.test22.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationSet;

public class Win10Loading extends View {
	private int mWidth=200;
	private int mHeight = 200;
	private final int green = 0xaf93d150;
	private final int blue = 0xff4aadff;
	private final int white = 0xffffffff;
	private final int black = 0xff000000;
	private int radius=80;//大圆的半径
	private int sRadius=5;//小圆的半径
	private int circleColor=blue;//小圆的颜色
	private int duration=1500;//圆远动一周的时间
	private Paint smallPaint;//小圆的画笔
	DrawFilter drawFilter;
	Path path;
	PathMeasure pm;
	float[] fractions;
	float length;//圆周长
	Matrix[] matrixs;
	private int circleNum=2;//球的数量
	Bitmap bitmap;
	public Win10Loading(Context context) {
		super(context);
		init(context,null);
	}

	public Win10Loading(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context,attrs);
	}

	public Win10Loading(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context,attrs);
	}
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if(changed){
			mWidth = right-left;
			mHeight = bottom-top;
		}
	}
	boolean isRun = false;
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.rotate(-180,mWidth/2,mHeight/2);
		canvas.setDrawFilter(drawFilter);
		path.addCircle(mWidth/2, mHeight/2, radius, Path.Direction.CW);
		Paint p = new Paint();
		p.setColor(black);
		p.setStyle(Style.STROKE);
		canvas.drawPath(path, p);
		pm = new PathMeasure(path, false);
	    length = pm.getLength();
	    for(int i=0;i<circleNum;i++){
	    	pm.getMatrix(fractions[i], matrixs[i], PathMeasure.POSITION_MATRIX_FLAG|PathMeasure.TANGENT_MATRIX_FLAG);
			canvas.drawBitmap(bitmap, matrixs[i], smallPaint);
	    }
		if(!isRun){
			perforAnim();
			isRun = true;
		}
	}
	private void perforAnim(){
		ValueAnimator va1 = ValueAnimator.ofFloat(0,length/4f);
		va1.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				fractions[0] = (float) animation.getAnimatedValue();
				postInvalidate();
			}
		});
		ValueAnimator va2 = ValueAnimator.ofFloat(0,length/3);
		va2.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				fractions[1] = (float) animation.getAnimatedValue();
//				postInvalidate();
			}
		});
//		va1.setRepeatCount(ValueAnimator.INFINITE);
//		va2.setRepeatCount(ValueAnimator.INFINITE);
		AnimatorSet as=new AnimatorSet();
		as.play(va1).with(va2);
		as.setDuration(1500);
		as.start();
	}

	private void init(Context context, AttributeSet attrs) {
		drawFilter= new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
				| Paint.FILTER_BITMAP_FLAG);
		radius = dip2px(context, radius);
		sRadius = dip2px(context, sRadius);
		smallPaint = new Paint();
		smallPaint.setColor(circleColor);
		path = new Path();
		matrixs = new Matrix[circleNum];
		for(int i=0;i<circleNum;i++){
			matrixs[i] = new Matrix();
		}
		//创建小球
		bitmap = Bitmap.createBitmap(2*sRadius+10, 2*sRadius+10, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bitmap);
		c.drawCircle(sRadius+5, sRadius+5, sRadius, smallPaint);
		c.setDrawFilter(drawFilter);
		fractions = new float[circleNum];
	}
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

}
