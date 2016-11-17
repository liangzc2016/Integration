package com.example.test22.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class ClipImageBorderView extends View {
	/**
	 * 水平方向与View的边距
	 */
	private int mHorizontalPadding = 20;
	private int mVerticalPadding;//垂直方向与View的边距
	/**
	 * 绘制矩形的宽度
	 */
	private int mWidth;
	private int mBorderWidth=1;//边框的宽度
	private int mBorderColor=Color.parseColor("#ffffff");//边框的颜色
	private Paint mPaint;
	public ClipImageBorderView(Context context) {
		this(context,null,0);
	}

	public ClipImageBorderView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public ClipImageBorderView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mHorizontalPadding = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding, getResources().getDisplayMetrics());
		mBorderWidth = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mBorderWidth, getResources().getDisplayMetrics());
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//计算矩形的宽度
		mWidth = getWidth()-2*mHorizontalPadding;
		mVerticalPadding = (getHeight()-mWidth)/2;
		mPaint.setColor(Color.parseColor("#aa000000"));
		mPaint.setStyle(Style.FILL);
		//左边的矩形
		canvas.drawRect(0, 0, mHorizontalPadding, getHeight(), mPaint);
		//上边的矩形
		canvas.drawRect(mHorizontalPadding, 0, getWidth(), mVerticalPadding, mPaint);
		//右边的矩形
		canvas.drawRect(getWidth()-mHorizontalPadding, mVerticalPadding, getWidth(), getHeight(), mPaint);
		//下边的矩形
		canvas.drawRect(mHorizontalPadding, getHeight()-mVerticalPadding, getWidth()-mHorizontalPadding, getHeight(), mPaint);
		//绘制外边框
		mPaint.setColor(mBorderColor);
		mPaint.setStrokeWidth(mBorderWidth);
		mPaint.setStyle(Style.STROKE);
		canvas.drawRect(mHorizontalPadding, mVerticalPadding, getWidth()-mHorizontalPadding, getHeight()-mVerticalPadding, mPaint);
	}

	public int getmHorizontalPadding() {
		return mHorizontalPadding;
	}

	public void setmHorizontalPadding(int mHorizontalPadding) {
		this.mHorizontalPadding = mHorizontalPadding;
		invalidate();
	}

}
