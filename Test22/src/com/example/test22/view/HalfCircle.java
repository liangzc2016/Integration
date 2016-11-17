package com.example.test22.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class HalfCircle extends View {
	Paint paint;
	public HalfCircle(Context context) {
		super(context);
	}

	public HalfCircle(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HalfCircle(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		paint = new Paint();
		paint.setStyle(Style.STROKE);
		paint.setStrokeCap(Cap.ROUND);
		paint.setStrokeWidth(15);
		RectF rf = new RectF(20, 20, 100, 100);
		canvas.drawArc(rf, 45, -270, false, paint);
	}

}
