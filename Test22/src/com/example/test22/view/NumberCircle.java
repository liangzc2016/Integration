package com.example.test22.view;

import java.util.ArrayList;
import java.util.List;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.example.test22.R;

/**
 * @author zc
 *可旋转的转盘
 */
public class NumberCircle extends View {
	private int width;
	private int height;
	/**
	 * 大圆半径
	 */
	private int big_width;
	/**
	 * 小圆半径
	 */
	private int small_width;
	private Paint paint;
	private int pure_white;
	private int blue2;
	private int triagle_length = 8;// 三角形边长
	private DrawFilter drawFilter;
	private int degree = 0, degree2 = 0;
	/**
	 * 左右两边文字距离圆边的距离
	 */
	private int textPadding = 15;
	/**
	 * 上下部分文字距离圆边的距离
	 */
	private int textPadding2 = 15;
	/**
	 * 四边文字的大小
	 */
	private int fourTextSize = 14;
	/**
	 * 中间文字大小
	 */
	private int centerTextSize = 18;
	private String leftText = "流量", topText = "上月费用", rightText = "余额",
			bottomText = "设备数";
	/**
	 * 圆心顶部的文字的集合
	 */
	private List<String> centerTopTexts;
	/**
	 * 圆心底部文字的集合
	 */
	private List<String> centerBottomTexts;
	/**
	 * 上次箭头所在方向
	 */
	private int lastLocation = 0;
	/**
	 * 箭头是否在旋转
	 */
	private boolean isRoating = false;
	/**
	 * 旋转180度的时间，单位为ms
	 */
	private final int rotateTime=500;
	/**
	 * 旋转90度的时间，单位为ms
	 */
	private final int rotateTime2 = rotateTime/2;

	public NumberCircle(Context context) {
		this(context, null);
	}

	public NumberCircle(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint = new Paint();
		pure_white = getResources().getColor(R.color.pure_white);
		blue2 = getResources().getColor(R.color.blue2);
		drawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
				| Paint.FILTER_BITMAP_FLAG);
		textPadding = dip2px(context, textPadding);
		textPadding2 = dip2px(context, textPadding2);
		fourTextSize = (int) TypedValue.applyDimension(  
	            TypedValue.COMPLEX_UNIT_SP, fourTextSize, getResources().getDisplayMetrics());
		centerTextSize = (int) TypedValue.applyDimension(  
	            TypedValue.COMPLEX_UNIT_SP, centerTextSize, getResources().getDisplayMetrics());
		triagle_length = dip2px(context, triagle_length);
		initCenterTextContent();
	}

	public NumberCircle(Context context, AttributeSet attrs, int defStyle) {
		this(context, attrs);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if (changed) {
			width = right - left;
			height = bottom - top;
			// 确定大圆半径
			big_width = Math.min(width, height) / 2 - 10;
			// 确定小园半径
			small_width = big_width / 2;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 从canvas层面去除绘制时锯齿
		canvas.setDrawFilter(drawFilter);
		// 画圆环
		paint.setColor(blue2);
		canvas.drawCircle(width / 2, height / 2, big_width, paint);
		paint.setColor(pure_white);
		canvas.drawCircle(width / 2, height / 2, small_width, paint);
		canvas.save();
		// 画两条直线
		canvas.rotate(45, width / 2, height / 2);
		paint.setColor(pure_white);
		paint.setStrokeWidth(dip2px(getContext(), 1));
		canvas.drawLine(width / 2 - big_width, big_width,
				width / 2 + big_width, big_width, paint);
		canvas.drawLine(width / 2, height / 2 - big_width, width / 2, height
				/ 2 + big_width, paint);
		canvas.restore();
		drawMyText(canvas, paint);
		drawTriagle(canvas, paint);
	}

	/**
	 * 画文本
	 * 
	 * @param canvas
	 * @param paint2
	 */
	private void drawMyText(Canvas canvas, Paint paint) {
		paint.setColor(pure_white);
		paint.setTextSize(fourTextSize);// 以像素为单位
		Rect rbound = new Rect();
		//左边的文字
		paint.getTextBounds(leftText, 0, leftText.length(), rbound);
		canvas.drawText(leftText, width / 2 - big_width + textPadding,
				height / 2+rbound.height()/2, paint);
		//上边的文字
		paint.getTextBounds(topText, 0, topText.length(), rbound);
		canvas.drawText(topText, width / 2 - rbound.width() / 2, height / 2
				- big_width + textPadding2 + rbound.height(), paint);
		//右边的文字
		paint.getTextBounds(rightText, 0, rightText.length(), rbound);
		canvas.drawText(rightText,width / 2 + big_width - textPadding - rbound.width(),
				height / 2+rbound.height()/2, paint);
		//下边的文字
		paint.getTextBounds(bottomText, 0, bottomText.length(), rbound);
		canvas.drawText(bottomText, width / 2 - rbound.width() / 2, height / 2
				+ big_width - textPadding2, paint);

		// 画中间的文本内容
		paint.setColor(blue2);
		paint.setTextSize(centerTextSize);
		paint.getTextBounds(centerTopTexts.get(lastLocation), 0, centerTopTexts.get(lastLocation).length(), rbound);
		canvas.drawText(centerTopTexts.get(lastLocation), width / 2 - rbound.width() / 2,
				height / 2 - 10, paint);
		paint.getTextBounds(centerBottomTexts.get(lastLocation), 0, centerBottomTexts.get(lastLocation).length(),
				rbound);
		canvas.drawText(centerBottomTexts.get(lastLocation), width / 2 - rbound.width() / 2,
				height / 2 + rbound.height() + 5, paint);
	}

	/**
	 * 画三角形
	 * 
	 * @param canvas
	 * @param paint
	 */
	private void drawTriagle(Canvas canvas, Paint paint) {
		Path path = new Path();
		// path.setFillType(FillType.WINDING);
		paint.setColor(pure_white);
		// 三角形的三个顶点
		int x1, y1, x2, y2, x3, y3;
		canvas.save();
		//旋转
		canvas.rotate(degree, width / 2, height / 2);
		isRoating = false;
		x1 = width / 2 - (small_width + triagle_length);
		y1 = height / 2;
		x2 = x1 + (triagle_length + 10);
		y2 = height / 2 - (triagle_length + 10);
		x3 = x2;
		y3 = height / 2 + (triagle_length + 10);
		path.moveTo(x1, y1);
		path.lineTo(x2, y2);
		path.lineTo(x3, y3);
		path.close(); // 使这些点构成封闭的多边形
		canvas.drawPath(path, paint);
		canvas.restore();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if(isRoating)return true;
			float downX = event.getX();
			float downY = event.getY();
			if (isInCircle(downX, downY)) {
				int flag = whichEdge(downX, downY);
				if (flag > -1) {
					// 箭头指向
					changeAngle(flag);
					return true;
				}
			} else {
				// 不在圆上
				return false;
			}
		}
		return true;
	}
	/**
	 * 开始旋转
	 */
	private void startAnim(){
		ValueAnimator anim = ValueAnimator.ofInt(degree,degree2);
		if(Math.abs(degree2-degree)==90){//旋转90度
			anim.setDuration(rotateTime2);
		}else{
			//旋转180度
			anim.setDuration(rotateTime);
		}
		anim.addUpdateListener(new AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				degree = (int) animation.getAnimatedValue();
				postInvalidate();
			}
		});
		anim.start();
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 判断点是否在圆上
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean isInCircle(float x, float y) {
		float distance = (float) Math.sqrt((x-width/2)*(x-width/2)+(y-height/2)*(y-height/2));
		if(distance>small_width&&distance<big_width)return true;
		return false;
	}

	/**
	 * 判断点落在那个范围上
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private int whichEdge(float x, float y) {
		int flag = -1;
		// 倾斜角为45度，右边那条直线y=-x+width/2+height/2;
		// 左边的直线为：y=x-(width/2-height/2),都转为andorid 坐标系了
		float y2 = -x + width / 2 + height / 2;
		float y3 = x - (width / 2 - height / 2);
//		Log.e("点范围", "y2=" + y2 + " y3=" + y3 + " y=" + y);
		if (y > y3 && y < y2)
			flag = 0;// 在左侧范围
		if (y < y3 && y < y2)
			flag = 1;// 上边范围
		if (y < y3 && y > y2)
			flag = 2;// 右边范围
		if (y > y3 && y > y2)
			flag = 3;// 底部范围
		return flag;
	}

	/**
	 * 旋转前的计算
	 * 
	 * @param flag
	 */
	private void changeAngle(int flag) {
		if (lastLocation == flag) {// 位置一样
			isRoating = false;
			return;
		}
		switch (lastLocation) {
		case 0:// 从0位置到其它位置
			switch (flag) {
			case 1:
				degree2 = 90;
				degree = 0;
				break;
			case 2:
				degree2 = 180;
				degree = 0;
				break;
			case 3:
				degree2 = -90;
				degree = 0;
				break;
			}
			break;
		case 1:// 从上边位置到其它位置
			switch (flag) {
			case 0:
				degree2 = 0;
				degree = 90;
				break;
			case 2:
				degree2 = 180;
				degree = 90;
				break;
			case 3:
				degree2 = 270;
				degree = 90;
				break;
			}
			break;
		case 2:// 从右边到其它位置
			switch (flag) {
			case 0:
				// 顺时针转
				degree2 = 360;
				degree = 180;
				break;
			case 1:// 逆时针转
				degree2 = 90;
				degree = 180;
				break;
			case 3:// 顺时针
				degree2 = 270;
				degree = 180;
				break;
			}
			break;
		case 3:// 从底部位置转到其它位置
			switch (flag) {
			case 0:// 顺时针
				degree2 = 360;
				degree = 270;
				break;
			case 1:// 顺时针
				degree2 = 90;
				degree = 270;
				break;
			case 2:// 逆时针
				degree2 = 180;
				degree = 270;
				break;
			}
			break;
		}
		lastLocation = flag;
		isRoating = true;
		startAnim();
	}
	/**
	 * 初始化圆心位置的位置内容
	 */
	private void initCenterTextContent(){
		centerTopTexts = new ArrayList<String>();
		centerBottomTexts = new ArrayList<String>();
		centerTopTexts.add("剩余流量");
		centerTopTexts.add("上月费用");
		centerTopTexts.add("余额");
		centerTopTexts.add("设备数");
		
		centerBottomTexts.add("132.0M");
		centerBottomTexts.add("50");
		centerBottomTexts.add("50");
		centerBottomTexts.add("50");
	}


	public void setTriagle_length(int triagle_length) {
		this.triagle_length = triagle_length;
	}


	public void setTextPadding(int textPadding) {
		this.textPadding = textPadding;
	}


	public void setFourTextSize(int fourTextSize) {
		this.fourTextSize = fourTextSize;
	}

	public void setCenterTextSize(int centerTextSize) {
		this.centerTextSize = centerTextSize;
	}


	public void setLeftText(String leftText) {
		this.leftText = leftText;
	}


	public void setTopText(String topText) {
		this.topText = topText;
	}


	public void setCenterTopTexts(List<String> centerTopTexts) {
		this.centerTopTexts = centerTopTexts;
	}


	public void setCenterBottomTexts(List<String> centerBottomTexts) {
		this.centerBottomTexts = centerBottomTexts;
	}

	
}
