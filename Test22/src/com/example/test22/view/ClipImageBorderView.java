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
	 * ˮƽ������View�ı߾�
	 */
	private int mHorizontalPadding = 20;
	private int mVerticalPadding;//��ֱ������View�ı߾�
	/**
	 * ���ƾ��εĿ��
	 */
	private int mWidth;
	private int mBorderWidth=1;//�߿�Ŀ��
	private int mBorderColor=Color.parseColor("#ffffff");//�߿����ɫ
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
		//������εĿ��
		mWidth = getWidth()-2*mHorizontalPadding;
		mVerticalPadding = (getHeight()-mWidth)/2;
		mPaint.setColor(Color.parseColor("#aa000000"));
		mPaint.setStyle(Style.FILL);
		//��ߵľ���
		canvas.drawRect(0, 0, mHorizontalPadding, getHeight(), mPaint);
		//�ϱߵľ���
		canvas.drawRect(mHorizontalPadding, 0, getWidth(), mVerticalPadding, mPaint);
		//�ұߵľ���
		canvas.drawRect(getWidth()-mHorizontalPadding, mVerticalPadding, getWidth(), getHeight(), mPaint);
		//�±ߵľ���
		canvas.drawRect(mHorizontalPadding, getHeight()-mVerticalPadding, getWidth()-mHorizontalPadding, getHeight(), mPaint);
		//������߿�
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
