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
 * 自定义可填充渐变颜色的曲线图
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
	// 线的颜色
	private int lineColor = black;
	// x轴的刻度
	float xStart, xEnd;
	// y轴的刻度
	float yStart, yEnd;
	Paint paint;
	Paint textPaint;
	Paint linePaint;
	private DrawFilter drawFilter;
	private int scaleDistance = 5;// 刻度与坐标系的距离
	private int scaleLen = 2;// 刻度的长度
	// 传进来的x，y的坐标
	private float[] xValues;
	private float[] yValues;
	float perLengthX;
	float perLengthY;
	// 比较的值，比这个值大就把这个点也绘制出来
	private float compareValue;
	// 是否画曲线下的颜色
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
		// 在画布上去除锯齿
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
		 * 处理为wrap_content情况，那么它的specMode是AT_MOST模式，在这种模式下它的宽/高
		 * 等于spectSize，这种情况下view的spectSize是parentSize,而parentSize是
		 * 父容器目前可以使用大小，就是父容器当前剩余的空间大小， 就相当于使用match_parent一样 的效果，因此我们可以设置一个默认的值
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
	 * 画坐标系
	 * 
	 * @param canvas
	 */
	private void drawCoordinate(Canvas canvas) {
		Rect rect = new Rect();
		textPaint.getTextBounds("300", 0, 3, rect);
		// 所画的坐标系的原点位置
		int startX = getPaddingLeft() + rect.width() + scaleDistance;
		int startY = mHeight - rect.height() - getPaddingBottom()
				- scaleDistance;
		// X轴的长度
		int lengthX = mWidth - getPaddingRight() - startX;
		// Y轴的长度
		int lengthY = startY - getPaddingTop();
		float countX, countY;
		countX = xEnd - xStart;
		countY = yEnd - yStart;
		// x轴每个刻度的长度
		perLengthX = 1.0f * lengthX / countX;
		// y轴每个刻度的长度
		perLengthY = 1.0f * lengthY / countY;
		// 画横坐标
		canvas.drawLine(startX, startY, mWidth - getPaddingRight(), startY,
				paint);
		// 画纵坐标
		canvas.drawLine(startX, startY, startX, getPaddingTop(), paint);
		// 画x轴的刻度
		for (int i = 0; i <= countX; i++) {
			if (i == 0) {
				// 画原点的数字
				canvas.drawText("" + (int) xStart, startX, mHeight
						- getPaddingBottom(), textPaint);
				continue;
			}
			float x = startX + i * perLengthX;
			float y1 = startY - scaleLen;
			float y2 = startY - 2 * scaleLen;
			if (i % 5 == 0) {
				// 加长一点
				canvas.drawLine(x, startY, x, y2, paint);
				// 画下面的数字
				canvas.drawText("" + (int) (xStart + i), x - rect.width() / 2,
						mHeight - getPaddingBottom(), textPaint);
			} else {
				canvas.drawLine(x, startY, x, y1, paint);
			}
		}
		// 画y轴的刻度
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
				// 加长一点
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
		// 用于保存y值大于compareValue的值
		float[] storageX = new float[xValues.length];
		float[] storageY = new float[xValues.length];
		Rect rect = new Rect();
		textPaint.getTextBounds("300", 0, 3, rect);
		int startX = getPaddingLeft() + rect.width() + scaleDistance;
		int startY = mHeight - rect.height() - getPaddingBottom()
				- scaleDistance;
		linePaint = new Paint();
		linePaint.setColor(fillColor);
		// 把拐点设置成圆的形式，参数为圆的半径，这样就可以画出曲线了
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
			// 乘以这个fraction是为了添加动画特效
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
			// 填充颜色
			if (isFillDownLineColor && i == 0) {
				// 形成封闭的图形
				path2.moveTo(x, y);
				path.moveTo(x, startY);
				path.lineTo(x, y);
			}
			// // 填充颜色
			// if (isFillDownLineColor && i == count - 1) {
			// path.lineTo(x, startY);
			// }
			path.cubicTo(x2, y2, x3, y3, x4, y4);
			path2.cubicTo(x2, y2, x3, y3, x4, y4);
		}
		if (isFillDownLineColor) {
			// 形成封闭的图形
			path.lineTo(startX + (xValues[count - 1] - xStart) * perLengthX,
					startY);
		}
		Paint rectPaint = new Paint();
		rectPaint.setColor(blue);
		float left = startX + (xValues[0] - xStart) * perLengthX;
		float top = getPaddingTop();
		float right = startX + (xValues[count - 1] - xStart) * perLengthX;
		float bottom = startY;
		// 渐变的颜色
		LinearGradient lg = new LinearGradient(left, top, left, bottom,
				Color.parseColor("#00ffffff"), Color.parseColor("#bFffffff"),
				Shader.TileMode.CLAMP);// CLAMP重复最后一个颜色至最后
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
	 * 画虚线
	 * 
	 * @param x
	 * @param y
	 * @param startY
	 * @param canvas
	 */
	private void drawDashAndPoint(float[] x, float[] y, float startY,
			Canvas canvas) {
		PathEffect pe = new DashPathEffect(new float[] { 10, 10 }, 1);
		// 要设置不是填充的，不然画一条虚线是没显示出来的
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
	 * 执行属性动画
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
		 * 设置x，y轴的刻度
		 * 
		 * @param xStart
		 *            X轴开始的刻度
		 * @param xEnd
		 *            X轴结束的刻度
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
		 * 是否填充曲线下面的颜色，默认值为true，
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
		 * 设置填充的颜色
		 * 
		 * @param fillColor
		 * @return
		 */
		public static CurveChartBuilder setFillDownColor(int fillColor) {
			curveChart.setFillColor(fillColor);
			return cBuilder;
		}

		/**
		 * 比较的值，比这个值大就把这个点也绘制出来
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
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
}
