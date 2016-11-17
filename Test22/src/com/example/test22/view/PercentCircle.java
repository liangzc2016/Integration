package com.example.test22.view;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class PercentCircle extends View {
	private DrawFilter drawFilter;
	private int width;
	private int height;
	private final int green = 0xff93d150;
	private final int blue = 0xff4aadff;
	private final int white = 0xffffffff;
	private final int black = 0xff000000;
	/**
	 * 开始的角度
	 */
	private float startAngle = 0;
	private float[] angles;
	/**
	 * 已使用的角度
	 */
	private float useAngle = 5;
	/**
	 * 剩余的角度
	 */
	private float overplusAngle = 355;
	/**
	 * 半径
	 */
	private int radius;
	private Paint mPaint;
	/**
	 * 文字的大小
	 */
	private int textSize = 14;
	private String overplusText = "剩余流量";
	private float overplusNum = 120.20f;
	private String usedText = "已使用流量";
	private float usedNum = 478.20f;

	ValueAnimator animator;
	/**
	 * 动画时间
	 */
	private  int animationDuration = 3000;
	/**
	 * 动画执行时拿到的值
	 */
	private float animatedValue = 0;
	/**
	 * 剩余流量所在的区域
	 */
	private RectF overplusRec;
	/**
	 * 已使用流量所在的区域
	 */
	private RectF useRec;
	/**
	 * 两个圆心的距离
	 */
	private int circleDistance = 3;
	/**
	 * 最小的角度，小于这数就不画那角度
	 */
	private int minAngle=5;

	public PercentCircle(Context context) {
		this(context, null);
	}

	public PercentCircle(Context context, AttributeSet attrs) {
		super(context, attrs);
		mPaint = new Paint();
		mPaint.setAntiAlias(false);
		overplusRec = new RectF();
		useRec = new RectF();
		angles = new float[] { useAngle, overplusAngle };
		circleDistance = dip2px(context, circleDistance);
		drawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
				| Paint.FILTER_BITMAP_FLAG);
		textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
				textSize, getResources().getDisplayMetrics());
	}

	public PercentCircle(Context context, AttributeSet attrs, int defStyle) {
		this(context, attrs);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if (changed) {
			width = right - left;
			height = bottom - top;
			// 确定圆的半径
			radius = Math.min(width, height) / 2 - 10;
			// 因为绘图的中心的移到圆心所在的位置，所以矩形的左边和上边是负数
			useRec.left = useRec.top = -radius;
			useRec.right = useRec.bottom = radius;
			overplusRec.left = overplusRec.top = -radius;
			overplusRec.right = overplusRec.bottom = radius;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// super.onDraw(canvas);
		// 从canvas层面去除绘制时锯齿
		canvas.setDrawFilter(drawFilter);
		float currentStartAngle = 0;
		canvas.save();
		//把圆心移动到中间
		canvas.translate(width / 2, height / 2);
		canvas.rotate(startAngle);
		float drawAngle;
		int count = angles.length;
		for (int i = 0; i < count; i++) {
			float angle = angles[i];
			if (Math.min(angle, animatedValue - currentStartAngle) >= 0.0001) {
				drawAngle = Math.min(angle, animatedValue - currentStartAngle);
			} else {
				drawAngle = 0;
			}
			if (animatedValue <= useAngle
					|| (currentStartAngle + drawAngle) <= useAngle) {
				//画蓝色部分的扇形
				mPaint.setColor(blue);
				canvas.drawArc(useRec, currentStartAngle, drawAngle, true,
						mPaint);
			} else {
				//画绿色部分的扇形
				float tempX =(float)(circleDistance*(Math.cos(overplusAngle/2/180*Math.PI)));
				float tempY = (float) (circleDistance*(Math.sin(overplusAngle/2/180*Math.PI)));
				//把绿色部分的圆心平移一点，这样就可以让小的扇形部分突出
				canvas.translate(tempX, -tempY);
				mPaint.setColor(green);
				canvas.drawArc(overplusRec, currentStartAngle, drawAngle, true,
						mPaint);
			}
			//保存现在已经画的角度
			currentStartAngle += angle;
		}
		canvas.restore();
		drawText(canvas);
	}

	/**
	 * 开始动画
	 */
	public void startAnim() {
		if (animator != null && animator.isRunning()) {
			animator.cancel();
			animator.start();
		} else {
			animator = ValueAnimator.ofFloat(0, 360);
			animator.setDuration(animationDuration);
			animator.addUpdateListener(new AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					animatedValue = (float) animation.getAnimatedValue();
					invalidate();
				}
			});
			animator.start();
		}
	}

	private void drawText(Canvas canvas) {
		canvas.save();
		//把画布移动到中心点
		canvas.translate(width / 2, height / 2);
		canvas.rotate(startAngle);
		//上下两行文字的距离
		int distance=dip2px(getContext(), 10);
		mPaint.setColor(white);
		mPaint.setTextSize(textSize);
		Rect rf = new Rect();
		mPaint.getTextBounds(usedText, 0, usedText.length(), rf);
		//画已使用流量文本
		if(useAngle>45){
			if(useAngle-180>=0.0001&&useAngle-180<=0.0001){
				canvas.drawText(usedText, -rf.width()/2, radius/2, mPaint);
				String num=usedNum+"M";
				//获取文本的范围
				mPaint.getTextBounds(num, 0, num.length(), rf);
				//画文本
				canvas.drawText(num, -rf.width()/2, radius/2+rf.height()+distance, mPaint);
			}else if(-useAngle+360<=10){
				canvas.drawText(usedText, -rf.width()/2, -distance, mPaint);
				String num=usedNum+"M";
				//获取文本的范围
				mPaint.getTextBounds(num, 0, num.length(), rf);
				//画文本
				canvas.drawText(num, -rf.width()/2, rf.height()+distance, mPaint);
			}else{
				float temp = useAngle/2;
				float x1,y1;
				if(temp<90){
					x1=(float) (radius*Math.cos(Math.PI*temp/180)/2);
					y1=(float)(radius*Math.sin(Math.PI*temp/180)/2);
				}else{
					temp=temp-90;
					x1=-(float)(radius*Math.sin(Math.PI*temp/180)/2);
					y1=(float)(radius*Math.cos(Math.PI*temp/180)/2);
				}
				canvas.drawText(usedText, x1-rf.width()/2, y1-rf.height()/2, mPaint);
				float temp2=y1-rf.height()/2;
				String num=usedNum+"M";
				mPaint.getTextBounds(num, 0, num.length(), rf);
				canvas.drawText(num, x1-rf.width()/2, temp2+rf.height()+distance, mPaint);
			}
		}
		
		//画剩余流量的文本
		mPaint.getTextBounds(overplusText, 0, overplusText.length(), rf);
		if(overplusAngle>45){
			if(overplusAngle-180>=0.0001&&overplusAngle-180<=0.0001){
				canvas.drawText(overplusText, -rf.width()/2, -radius/2-distance, mPaint);
				String num=overplusNum+"M";
				mPaint.getTextBounds(num, 0, num.length(), rf);
				canvas.drawText(num, -rf.width()/2, -radius/2+rf.height()+distance, mPaint);
			}else if(-overplusAngle+360<=10){
				canvas.drawText(overplusText, -rf.width()/2, -distance, mPaint);
				String num=overplusNum+"M";
				mPaint.getTextBounds(num, 0, num.length(), rf);
				canvas.drawText(num, -rf.width()/2, rf.height()+distance, mPaint);
			}else{
				float temp = overplusAngle/2;
				float x1,y1;
				if(temp<90){
					x1=(float)(radius*Math.cos(Math.PI*temp/180)/2);
					y1=-(float)(radius*Math.sin(Math.PI*temp/180)/2);
				}else{
					temp=temp-90;
					x1=-(float)(radius*Math.sin(Math.PI*temp/180)/2);
					y1=-(float)(radius*Math.cos(Math.PI*temp/180)/2);
				}
				float a = rf.width()/2;
				canvas.drawText(overplusText, x1-rf.width()/4, y1-distance+3*rf.height()/4, mPaint);
				String num=overplusNum+"M";
				mPaint.getTextBounds(num, 0, num.length(), rf);
				canvas.drawText(num, x1-rf.width()/4, y1+2*rf.height(), mPaint);
			}
		}
//		if(useAngle<45||overplusAngle<45){//在右边显示，角度小的就不画在圆上了
			int rectWidth=dip2px(getContext(), 10);//矩形的宽度
			mPaint.getTextBounds(usedText, 0, usedText.length(), rf);
			int x2,y2;
			x2 = (int) (radius*Math.cos(Math.PI*30/180))+3;
			y2 = -(int) (radius*Math.sin(Math.PI*30/180))-15;
			//画蓝色的矩形和已使用流量的文本
			Rect r = new Rect(x2,y2-rf.height()*2/3,x2+rectWidth,y2);
			mPaint.setColor(blue);
			canvas.drawRect(r, mPaint);
			mPaint.setColor(black);
			canvas.drawText(usedText.substring(1, usedText.length()), x2+rectWidth+rectWidth/3, y2, mPaint);
			canvas.drawText(usedNum+"M", x2+rectWidth+rectWidth/3, y2+distance/3+rf.height(), mPaint);
			
			//画剩余流量和绿色矩形
			float temp3=y2-rf.height()*2/3;//蓝色矩形顶部位置
			mPaint.getTextBounds(overplusText, 0, overplusText.length(), rf);
			r = new Rect(x2,(int)(temp3-distance-rf.height()-distance/3)-rf.height()*2/3,x2+rectWidth,(int)(temp3-distance-rf.height()-distance/3.0));
			mPaint.setColor(green);
			canvas.drawRect(r, mPaint);
			mPaint.setColor(black);
			canvas.drawText(overplusText,  x2+rectWidth+rectWidth/3, temp3-distance-rf.height()-distance/3, mPaint);
			canvas.drawText(overplusNum+"M",  x2+rectWidth+rectWidth/3, temp3-distance, mPaint);
//		}
		canvas.restore();
	}

	/**
	 * 分别计算两部分流量所占的角度
	 */
	private void calAngle() {
		if (usedNum <= 0.0001) {
			useAngle = 0;
			overplusAngle = 360;
			angles[0] = useAngle;
			angles[1] = overplusAngle;
			return;
		}
		if (overplusNum <= 0.0001) {
			useAngle = 360;
			overplusAngle = 0;
			angles[0] = useAngle;
			angles[1] = overplusAngle;
			return;
		}
		useAngle = (float) (1.0 * usedNum / (usedNum + overplusNum)) * 360;
		overplusAngle = 360 - useAngle;
		//度数太小，就不画了
		if(useAngle<minAngle){
			useAngle=0;
			overplusAngle=360;
		}
		if(overplusAngle<minAngle){
			useAngle=360;
			overplusAngle=0;
		}
		angles[0] = useAngle;
		angles[1] = overplusAngle;
	}

	/**
	 * 设置已使用和剩余的流量
	 * 
	 * @param usedNum
	 *            已使用的流量，没有则传0
	 * @param overplusNum
	 *            剩余的流量，没有则传0
	 */
	public void setUsedAndOverplusNum(float usedNum, float overplusNum) {
		this.usedNum = usedNum;
		this.overplusNum = overplusNum;
		calAngle();
	}

	public void setTextSize(int textSize) {
		this.textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
				textSize, getResources().getDisplayMetrics());
	}

	public void setOverplusText(String overplusText) {
		this.overplusText = overplusText;
	}

	public void setUsedText(String usedText) {
		this.usedText = usedText;
	}

	public void setCircleDistance(int circleDistance) {
		this.circleDistance = dip2px(getContext(), circleDistance);
	}
	

	public void setAnimationDuration(int animationDuration) {
		this.animationDuration = animationDuration;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
	public static class CircleBuilder{
		private static PercentCircle mPercentCircle;
		private static CircleBuilder mCircleBuilder;
		private CircleBuilder(){
		}
		public static CircleBuilder createBuilder(PercentCircle percentCircle){
			mCircleBuilder = new CircleBuilder();
			mPercentCircle = percentCircle;
			return mCircleBuilder;
		}
		/**
		 * 设置已使用和剩余的流量
		 * @param usedNum
		 *            已使用的流量，没有则传0
		 * @param overplusNum
		 *            剩余的流量，没有则传0
		 */
		public static CircleBuilder setUsedAndOverplusNum(float usedNum, float overplusNum){
			mPercentCircle.setUsedAndOverplusNum(usedNum, overplusNum);
			return mCircleBuilder;
		}
		/**绘制文本的大小
		 * @param textSize
		 * @return
		 */
		public static CircleBuilder setTextSize(int textSize){
			mPercentCircle.setTextSize(textSize);
			return mCircleBuilder;
		}
		/**设置两个圆心的距离
		 * @param circleDistance
		 * @return
		 */
		public static CircleBuilder setCircleDistance(int circleDistance){
			mPercentCircle.setCircleDistance(circleDistance);
			return mCircleBuilder;
		}
		/**设置两个文本的内容
		 * @param usedText 已使用的流量
		 * @param overplusText 剩余流量
		 * @return
		 */
		public static CircleBuilder setTextContent(String usedText,String overplusText){
			mPercentCircle.setUsedText(usedText);
			mPercentCircle.setOverplusText(overplusText);
			return mCircleBuilder;
		}
		/**动画的执行时间
		 * @param animationDuration
		 * @return
		 */
		public static CircleBuilder setDuration(int animationDuration){
			mPercentCircle.setAnimationDuration(animationDuration);
			return mCircleBuilder;
		}
		public static PercentCircle build(){
			return mPercentCircle;
		}
		/**
		 * 
		 */
		public void show(){
			if(mPercentCircle==null){
				throw new NullPointerException("PercentCirle is null");
			}
			mPercentCircle.startAnim();
		}
	}
}
