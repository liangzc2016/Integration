package com.example.test22.view;

import com.example.test22.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**雷达图（蜘蛛网图）
 * @author zc
 *
 */
public class NetPicture extends View {
	Paint paint;
	Paint textPaint;
	Paint contentPaint;
	DrawFilter drawFilter;
	int mWidth = 200, mHeight = 200;
	// 默认的颜色值
	private final int green = 0xaf93d150;
	private final int blue = 0xff4aadff;
	private final int white = 0xffffffff;
	private final int black = 0xff000000;
	// 自定义的属性值
	private int lineColor;// 线的颜色
	private int contentColor;// 图形的内部的颜色
	private float side;// 三角形的边长
	private float distance;// 当前三角形和上一个三角形的距离
	private int num;// 三角形的数量

	private String[] texts;
	// 6个能力的数值
	private float[] abilitys;
	// 文字与顶点的距离
	private float textDistance = 10;
	private int textSize = 16;
	private Context context;

	public NetPicture(Context context) {
		this(context, null);
	}

	public NetPicture(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public NetPicture(Context context, AttributeSet attrs, int defStyle) {
		this(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		this.context = context;
		// 获取自定义属性的值
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.MyNetPic);
		lineColor = a.getColor(R.styleable.MyNetPic_lineColor, blue);
		contentColor = a.getColor(R.styleable.MyNetPic_cotentColor, green);
		side = a.getDimension(R.styleable.MyNetPic_side, 25);
		distance = a.getDimension(R.styleable.MyNetPic_distance, 25);
		num = a.getInteger(R.styleable.MyNetPic_number, 5);
		a.recycle();
		paint = new Paint();
		textPaint = new Paint();
		contentPaint = new Paint();
		// 把dp转换为px
		side = dip2px(context, side);
		distance = dip2px(context, distance);
		textDistance = dip2px(context, textDistance);
		textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
				textSize, getResources().getDisplayMetrics());
		drawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
				| Paint.FILTER_BITMAP_FLAG);
		texts = new String[] { "个人", "团队", "意识", "领悟", "思维", "敏捷" };
		abilitys = new float[] { 150, 145, 130, 160, 120, 105 };
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
		// 从canvas层面去除绘制时锯齿
		canvas.setDrawFilter(drawFilter);
		// 移到区域的中心
		canvas.translate(mWidth / 2, mHeight / 2);
		// 将y轴翻转
		// canvas.scale(1f, -1f);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(1f);
		drawBackGroundPic(canvas);
		drawMyText(canvas);
		drawContent(canvas);
	}

	/**
	 * 画作为背景的正六边形
	 * 
	 * @param canvas
	 */
	private void drawBackGroundPic(Canvas canvas) {
		paint.setAntiAlias(true);
		paint.setColor(lineColor);
		// 先画三角形
		Path path = new Path();
		float x2, x3;
		int AngleCount = 6;
		float xArray[] = new float[num];// 存储x坐标
		float yArray[] = new float[num];// 存储y坐标
		for (int j = 0; j < AngleCount; j++) {
			canvas.save();
			canvas.rotate(j * 60);
			// 计算每个三角形第三个点的坐标
			for (int i = 0; i < num; i++) {
				x2 = side + i * distance;// 第二个点
				xArray[i] = x3 = x2 / 2.0f;// 第三个点的横坐标，因为cos60=1/2;
				// 用勾股定理计算第三个点的y坐标
				yArray[i] = -(float) Math.sqrt(x2 * x2 - x3 * x3);
			}
			// 先画最大那个三角形的两条边
			path.moveTo(0, 0);
			path.lineTo(side + (num - 1) * distance, 0);
			path.moveTo(0, 0);
			path.lineTo(xArray[num - 1], yArray[num - 1]);
			// 再画每个三角形的第三条边
			for (int i = 0; i < num; i++) {
				path.moveTo(xArray[i], yArray[i]);
				path.lineTo(side + i * distance, 0);
			}
			canvas.drawPath(path, paint);
			canvas.restore();
		}
	}

	/**
	 * 逆时针画文字，最右边的为第一个
	 * 
	 * @param canvas
	 */
	private void drawMyText(Canvas canvas) {
		textPaint.setColor(black);
		textPaint.setTextSize(textSize);
		// 文字距离原点的大小，为最大的三角形边长+文字距离三角形的大小
		float d = side + (num - 1) * distance + textDistance;
		// 因为图形是对称的，所以直接计算其中一个角度的坐标，之后就可以重复使用了
		float dx = (float) (d * Math.cos(60.0 * Math.PI / 180));
		float dy = (float) (d * Math.sin(60.0 * Math.PI / 180));
		Rect textRect = new Rect();
		textPaint.getTextBounds(texts[0], 0, texts[0].length(), textRect);
		canvas.drawText(texts[0], d, textRect.height() / 2, textPaint);
		canvas.drawText(texts[1], dx, -dy, textPaint);
		canvas.drawText(texts[2], -dx - textRect.width(), -dy, textPaint);
		canvas.drawText(texts[3], -d - textRect.width(), textRect.height() / 2,
				textPaint);
		canvas.drawText(texts[4], -dx - textRect.width(),
				dy + textRect.height(), textPaint);
		canvas.drawText(texts[5], dx, dy + textRect.height(), textPaint);
	}

	/**画能力值形成的图形
	 * @param canvas
	 */
	private void drawContent(Canvas canvas) {
		contentPaint.setColor(contentColor);
		float d =side + (num - 1) * distance;
		//用两个数组来保存6个点的坐标
		float xArray[] = new float[abilitys.length];
		float yArray[] = new float[abilitys.length];
		int count = abilitys.length;
		//计算6个能力值的x,y坐标
		for (int i = 0; i < count; i++) {
			float conX = (float) (Math.cos(i * 60.0 * Math.PI / 180));
			float conY = (float) (Math.sin(i * 60.0 * Math.PI / 180));
			// 为了防止能力值比最大的三角形的边长还要大，这里就求余
			xArray[i] = abilitys[i] % d * conX;
			yArray[i] = -abilitys[i] % d * conY;
		}
		//画图形
		Path path = new Path();
		path.moveTo(xArray[0], yArray[0]);
		for(int i=1;i<count;i++){
			path.lineTo(xArray[i], yArray[i]);
		}
		path.close();
		//画6个顶点
		canvas.drawPath(path, contentPaint);
		contentPaint.setColor(black);
		for(int i=0;i<count;i++){
			canvas.drawCircle(xArray[i], yArray[i],dip2px(context, 3),contentPaint);
		}
	}
	public void show(){
		postInvalidate();
	}
	public static class NetPicBuilder {
		private static NetPicture netPicture;
		private static NetPicBuilder netPicBuilder;
		private NetPicBuilder(){
		}
		public static NetPicBuilder createBuilder(NetPicture netPic){
			netPicture = netPic;
			synchronized (NetPicBuilder.class) {
				if(netPicBuilder==null){
					netPicBuilder = new NetPicBuilder();
				}
			}
			return netPicBuilder;
		}
		/**设置文本的内容
		 * @param s
		 * @return
		 */
		public static NetPicBuilder setTextContent(String[] s){
			netPicture.setTexts(s);
			return netPicBuilder;
		}
		/**设置能力值
		 * @param ab
		 * @return
		 */
		public static NetPicBuilder setAbilitys(float[] ab){
			netPicture.setAbilitys(ab);
			return netPicBuilder;
		}
		/**
		 * 把图形显示出来
		 */
		public static void show(){
			if(netPicture==null){
				throw new NullPointerException("NetPicBuilder is null");
			}
			netPicture.show();
		}
	}

	public void setAbilitys(float[] ab) {
		abilitys = ab;
	}

	public void setTexts(String[] s) {
		this.texts = s;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

}
