package com.example.test22.view;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.DrawFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * �Զ������佥����ɫ������ͼ
 * 
 * @author zc
 * 
 */
public class CurveChart extends View {
	private int mWidth = 200, mHeight = 200;
	private final int green = 0xaf93d150;
	private final int blue = 0xff4aadff;
	private final int white = 0xffffffff;
	private final int black = 0xff000000;
	// �ߵ���ɫ
	private int lineColor = black;
	// x��Ŀ̶�
	float xStart, xEnd;
	// y��Ŀ̶�
	float yStart, yEnd;
	Paint paint;
	Paint textPaint;
	Paint linePaint;
	private DrawFilter drawFilter;
	private int scaleDistance = 5;// �̶�������ϵ�ľ���
	private int scaleLen = 2;// �̶ȵĳ���
	// ��������x��y������
	private float[] xValues;
	private float[] yValues;
	float perLengthX;
	float perLengthY;
	// �Ƚϵ�ֵ�������ֵ��Ͱ������Ҳ���Ƴ���
	private float compareValue;
	// �Ƿ������µ���ɫ
	private boolean isFillDownLineColor;
	private int fillColor = green;
	private float fraction = 0f;
	private Paint dashPaint;

	public CurveChart(Context context) {
		this(context, null);
	}

	public CurveChart(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public CurveChart(Context context, AttributeSet attrs, int defStyleAttr) {
		this(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		// �ڻ�����ȥ�����
		drawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
				| Paint.FILTER_BITMAP_FLAG);
		paint = new Paint();
		paint.setColor(black);
		textPaint = new Paint();
		dashPaint = new Paint();
		scaleDistance = dip2px(context, scaleDistance);
		scaleLen = dip2px(context, scaleLen);
		isFillDownLineColor = true;
		xStart = 0;
		yStart = 150;
		xEnd = 30;
		yEnd = 200;
		xValues = new float[] { 0, 1, 2, 3, 5, 7, 8, 15, 20, 30 };
		yValues = new float[] { 185, 195, 197, 195, 193, 195, 198, 193, 199,
				192 };
		compareValue = 195;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		/*
		 * ����Ϊwrap_content�������ô����specMode��AT_MOSTģʽ��������ģʽ�����Ŀ�/��
		 * ����spectSize�����������view��spectSize��parentSize,��parentSize��
		 * ������Ŀǰ����ʹ�ô�С�����Ǹ�������ǰʣ��Ŀռ��С�� ���൱��ʹ��match_parentһ�� ��Ч����������ǿ�������һ��Ĭ�ϵ�ֵ
		 */
		int widthSpectMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSpectSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSpectMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSpectSize = MeasureSpec.getSize(heightMeasureSpec);
		if (widthSpectMode == MeasureSpec.AT_MOST
				&& heightSpectMode == MeasureSpec.AT_MOST) {
			setMeasuredDimension(mWidth, mHeight);
		} else if (widthSpectMode == MeasureSpec.AT_MOST) {
			setMeasuredDimension(mWidth, heightSpectSize);
		} else if (heightSpectMode == MeasureSpec.AT_MOST) {
			setMeasuredDimension(widthSpectSize, mHeight);
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if (changed) {
			mWidth = right - left;
			mHeight = bottom - top;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.setDrawFilter(drawFilter);
		drawCoordinate(canvas);
		drawPoint(canvas);
	}

	/**
	 * ������ϵ
	 * 
	 * @param canvas
	 */
	private void drawCoordinate(Canvas canvas) {
		Rect rect = new Rect();
		textPaint.getTextBounds("300", 0, 3, rect);
		// ����������ϵ��ԭ��λ��
		int startX = getPaddingLeft() + rect.width() + scaleDistance;
		int startY = mHeight - rect.height() - getPaddingBottom()
				- scaleDistance;
		// X��ĳ���
		int lengthX = mWidth - getPaddingRight() - startX;
		// Y��ĳ���
		int lengthY = startY - getPaddingTop();
		float countX, countY;
		countX = xEnd - xStart;
		countY = yEnd - yStart;
		// x��ÿ���̶ȵĳ���
		perLengthX = 1.0f * lengthX / countX;
		// y��ÿ���̶ȵĳ���
		perLengthY = 1.0f * lengthY / countY;
		// ��������
		canvas.drawLine(startX, startY, mWidth - getPaddingRight(), startY,
				paint);
		// ��������
		canvas.drawLine(startX, startY, startX, getPaddingTop(), paint);
		// ��x��Ŀ̶�
		for (int i = 0; i <= countX; i++) {
			if (i == 0) {
				// ��ԭ�������
				canvas.drawText("" + (int) xStart, startX, mHeight
						- getPaddingBottom(), textPaint);
				continue;
			}
			float x = startX + i * perLengthX;
			float y1 = startY - scaleLen;
			float y2 = startY - 2 * scaleLen;
			if (i % 5 == 0) {
				// �ӳ�һ��
				canvas.drawLine(x, startY, x, y2, paint);
				// �����������
				canvas.drawText("" + (int) (xStart + i), x - rect.width() / 2,
						mHeight - getPaddingBottom(), textPaint);
			} else {
				canvas.drawLine(x, startY, x, y1, paint);
			}
		}
		// ��y��Ŀ̶�
		for (int i = 0; i <= countY; i++) {
			if (i == 0) {
				canvas.drawText("" + (int) yStart, getPaddingLeft(), startY,
						textPaint);
				continue;
			}
			float y = startY - i * perLengthY;
			float x1 = startX + scaleLen;
			float x2 = startX + 2 * scaleLen;
			if (i % 5 == 0) {
				// �ӳ�һ��
				canvas.drawLine(startX, y, x2, y, paint);
				canvas.drawText("" + (int) (yStart + i), getPaddingLeft(), y
						+ rect.height() / 2, textPaint);
			} else {
				canvas.drawLine(startX, y, x1, y, paint);
			}
		}
	}

	boolean stop = false;

	private void drawPoint(Canvas canvas) {
		// ���ڱ���yֵ����compareValue��ֵ
		float[] storageX = new float[xValues.length];
		float[] storageY = new float[xValues.length];
		Rect rect = new Rect();
		textPaint.getTextBounds("300", 0, 3, rect);
		int startX = getPaddingLeft() + rect.width() + scaleDistance;
		int startY = mHeight - rect.height() - getPaddingBottom()
				- scaleDistance;
		linePaint = new Paint();
		linePaint.setColor(fillColor);
		// �ѹյ����ó�Բ����ʽ������ΪԲ�İ뾶�������Ϳ��Ի���������
		PathEffect pe = new CornerPathEffect(45);
		// linePaint.setPathEffect(pe);
		if (!isFillDownLineColor) {
			linePaint.setStyle(Paint.Style.STROKE);
		}
		Path path = new Path();
		Path path2 = new Path();
		path.moveTo(startX + (xValues[0] - xStart) * perLengthX, startY
				- (yValues[0] - yStart) * perLengthY * fraction);
		int count = xValues.length;
		for (int i = 0; i < count - 1; i++) {
			float x, y, x2, y2, x3, y3, x4, y4;
			x = startX + (xValues[i] - xStart) * perLengthX;
			x4 = (startX + (xValues[i + 1] - xStart) * perLengthX);
			x2 = x3 = (x + x4) / 2;
			// �������fraction��Ϊ����Ӷ�����Ч
			y = startY - (yValues[i] - yStart) * perLengthY * fraction;
			y4 = startY - (yValues[i + 1] - yStart) * perLengthY * fraction;
			y2 = y;
			y3 = y4;
			if (yValues[i] > compareValue) {
				storageX[i] = x;
				storageY[i] = y;
			}
			if (!isFillDownLineColor && i == 0) {
				path2.moveTo(x, y);
				path.moveTo(x, y);
				continue;
			}
			// �����ɫ
			if (isFillDownLineColor && i == 0) {
				// �γɷ�յ�ͼ��
				path2.moveTo(x, y);
				path.moveTo(x, startY);
				path.lineTo(x, y);
			}
			// // �����ɫ
			// if (isFillDownLineColor && i == count - 1) {
			// path.lineTo(x, startY);
			// }
			path.cubicTo(x2, y2, x3, y3, x4, y4);
			path2.cubicTo(x2, y2, x3, y3, x4, y4);
		}
		if (isFillDownLineColor) {
			// �γɷ�յ�ͼ��
			path.lineTo(startX + (xValues[count - 1] - xStart) * perLengthX,
					startY);
		}
		Paint rectPaint = new Paint();
		rectPaint.setColor(blue);
		float left = startX + (xValues[0] - xStart) * perLengthX;
		float top = getPaddingTop();
		float right = startX + (xValues[count - 1] - xStart) * perLengthX;
		float bottom = startY;
		// �������ɫ
		LinearGradient lg = new LinearGradient(left, top, left, bottom,
				Color.parseColor("#00ffffff"), Color.parseColor("#bFffffff"),
				Shader.TileMode.CLAMP);// CLAMP�ظ����һ����ɫ�����
		rectPaint.setShader(lg);
		rectPaint.setXfermode(new PorterDuffXfermode(
				android.graphics.PorterDuff.Mode.SRC_ATOP));
		if (isFillDownLineColor) {
			canvas.drawPath(path, linePaint);
		}
		canvas.drawRect(left, top, right, bottom, rectPaint);
		// canvas.restoreToCount(layerId);
		rectPaint.setXfermode(null);
		linePaint.setStyle(Paint.Style.STROKE);
		linePaint.setColor(lineColor);
		canvas.drawPath(path2, linePaint);
		linePaint.setPathEffect(null);
		drawDashAndPoint(storageX, storageY, startY, canvas);
		if (!stop)
			performAnimator();
		if (fraction > 0.99) {
			performAnimator();
		}
	}

	/**
	 * ������
	 * 
	 * @param x
	 * @param y
	 * @param startY
	 * @param canvas
	 */
	private void drawDashAndPoint(float[] x, float[] y, float startY,
			Canvas canvas) {
		PathEffect pe = new DashPathEffect(new float[] { 10, 10 }, 1);
		// Ҫ���ò������ģ���Ȼ��һ��������û��ʾ������
		dashPaint.setStyle(Paint.Style.STROKE);
		dashPaint.setPathEffect(pe);
		dashPaint.setColor(lineColor);
		Paint pointPaint = new Paint();
		pointPaint.setColor(lineColor);
		for (int i = 0; i < x.length; i++) {
			if (y[i] > 1) {
				canvas.drawCircle(x[i], y[i], 2, pointPaint);
				Path path = new Path();
				path.moveTo(x[i], startY);
				path.lineTo(x[i], y[i]);
				canvas.drawPath(path, dashPaint);
			}
		}
	}

	public void setxStart(float xStart, float xEnd) {
		this.xStart = xStart;
		this.xEnd = xEnd;
	}

	public void setyStart(float yStart, float yEnd) {
		this.yStart = yStart;
		this.yEnd = yEnd;
	}

	public void setScaleDistance(int scaleDistance) {
		this.scaleDistance = scaleDistance;
	}

	public void setCompareValue(float compareValue) {
		this.compareValue = compareValue;
	}

	public void setFillDownLineColor(boolean isFillDownLineColor) {
		this.isFillDownLineColor = isFillDownLineColor;
	}

	public void setFillColor(int fillColor) {
		this.fillColor = fillColor;
	}

	public void setxValues(float[] xValues2) {
		this.xValues = xValues2;
	}

	public void setyValues(float[] yValues) {
		this.yValues = yValues;
	}

	int numCount = 1;

	/**
	 * ִ�����Զ���
	 */
	public void performAnimator() {
		if (numCount > 3)
			return;
		ValueAnimator va = ValueAnimator.ofFloat(0, 1);
		if (numCount == 1) {
			va = ValueAnimator.ofFloat(0, 1);
		} else if (numCount == 2) {
			va = ValueAnimator.ofFloat(0.85f, 1);
		} else if (numCount == 3) {
			va = ValueAnimator.ofFloat(0.95f, 1);
		}
		numCount++;
		va.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				fraction = (float) animation.getAnimatedValue();
				stop = true;
				postInvalidate();
			}
		});
		va.setDuration(1000);
		va.start();
	}

	public void show() {
		numCount = 1;
		stop = false;
		postInvalidate();
	}

	public static class CurveChartBuilder {
		private static CurveChart curveChart;
		private static CurveChartBuilder cBuilder;

		private CurveChartBuilder() {
		}

		public static CurveChartBuilder createBuilder(CurveChart curve) {
			curveChart = curve;
			synchronized (CurveChartBuilder.class) {
				if (cBuilder == null) {
					cBuilder = new CurveChartBuilder();
				}
			}
			return cBuilder;
		}

		/**
		 * ����x��y��Ŀ̶�
		 * 
		 * @param xStart
		 *            X�Ὺʼ�Ŀ̶�
		 * @param xEnd
		 *            X������Ŀ̶�
		 * @param yStart
		 * @param yEnd
		 * @return
		 */
		public static CurveChartBuilder setXYCoordinate(float xStart,
				float xEnd, float yStart, float yEnd) {
			curveChart.setxStart(xStart, xEnd);
			curveChart.setyStart(yStart, yEnd);
			return cBuilder;
		}

		/**
		 * �Ƿ���������������ɫ��Ĭ��ֵΪtrue��
		 * 
		 * @param isFillDownLineColor
		 * @return
		 */
		public static CurveChartBuilder setIsFillDownColor(
				boolean isFillDownLineColor) {
			curveChart.setFillDownLineColor(isFillDownLineColor);
			return cBuilder;
		}

		/**
		 * ����������ɫ
		 * 
		 * @param fillColor
		 * @return
		 */
		public static CurveChartBuilder setFillDownColor(int fillColor) {
			curveChart.setFillColor(fillColor);
			return cBuilder;
		}

		/**
		 * �Ƚϵ�ֵ�������ֵ��Ͱ������Ҳ���Ƴ���
		 * 
		 * @param compareValue
		 * @return
		 */
		public static CurveChartBuilder setCompareValue(float compareValue) {
			curveChart.setCompareValue(compareValue);
			return cBuilder;
		}

		public static CurveChartBuilder setXYValues(float[] xValues,
				float[] yValues) {
			curveChart.setxValues(xValues);
			curveChart.setyValues(yValues);
			return cBuilder;
		}

		public void show() {
			if (curveChart == null) {
				throw new NullPointerException("CurveChart is null");
			}
			curveChart.show();
		}
	}

	/**
	 * �����ֻ��ķֱ��ʴ� dp �ĵ�λ ת��Ϊ px(����)
	 */
	public int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
}
