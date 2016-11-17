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
	 * ��ʼ�ĽǶ�
	 */
	private float startAngle = 0;
	private float[] angles;
	/**
	 * ��ʹ�õĽǶ�
	 */
	private float useAngle = 5;
	/**
	 * ʣ��ĽǶ�
	 */
	private float overplusAngle = 355;
	/**
	 * �뾶
	 */
	private int radius;
	private Paint mPaint;
	/**
	 * ���ֵĴ�С
	 */
	private int textSize = 14;
	private String overplusText = "ʣ������";
	private float overplusNum = 120.20f;
	private String usedText = "��ʹ������";
	private float usedNum = 478.20f;

	ValueAnimator animator;
	/**
	 * ����ʱ��
	 */
	private  int animationDuration = 3000;
	/**
	 * ����ִ��ʱ�õ���ֵ
	 */
	private float animatedValue = 0;
	/**
	 * ʣ���������ڵ�����
	 */
	private RectF overplusRec;
	/**
	 * ��ʹ���������ڵ�����
	 */
	private RectF useRec;
	/**
	 * ����Բ�ĵľ���
	 */
	private int circleDistance = 3;
	/**
	 * ��С�ĽǶȣ�С�������Ͳ����ǽǶ�
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
			// ȷ��Բ�İ뾶
			radius = Math.min(width, height) / 2 - 10;
			// ��Ϊ��ͼ�����ĵ��Ƶ�Բ�����ڵ�λ�ã����Ծ��ε���ߺ��ϱ��Ǹ���
			useRec.left = useRec.top = -radius;
			useRec.right = useRec.bottom = radius;
			overplusRec.left = overplusRec.top = -radius;
			overplusRec.right = overplusRec.bottom = radius;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// super.onDraw(canvas);
		// ��canvas����ȥ������ʱ���
		canvas.setDrawFilter(drawFilter);
		float currentStartAngle = 0;
		canvas.save();
		//��Բ���ƶ����м�
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
				//����ɫ���ֵ�����
				mPaint.setColor(blue);
				canvas.drawArc(useRec, currentStartAngle, drawAngle, true,
						mPaint);
			} else {
				//����ɫ���ֵ�����
				float tempX =(float)(circleDistance*(Math.cos(overplusAngle/2/180*Math.PI)));
				float tempY = (float) (circleDistance*(Math.sin(overplusAngle/2/180*Math.PI)));
				//����ɫ���ֵ�Բ��ƽ��һ�㣬�����Ϳ�����С�����β���ͻ��
				canvas.translate(tempX, -tempY);
				mPaint.setColor(green);
				canvas.drawArc(overplusRec, currentStartAngle, drawAngle, true,
						mPaint);
			}
			//���������Ѿ����ĽǶ�
			currentStartAngle += angle;
		}
		canvas.restore();
		drawText(canvas);
	}

	/**
	 * ��ʼ����
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
		//�ѻ����ƶ������ĵ�
		canvas.translate(width / 2, height / 2);
		canvas.rotate(startAngle);
		//�����������ֵľ���
		int distance=dip2px(getContext(), 10);
		mPaint.setColor(white);
		mPaint.setTextSize(textSize);
		Rect rf = new Rect();
		mPaint.getTextBounds(usedText, 0, usedText.length(), rf);
		//����ʹ�������ı�
		if(useAngle>45){
			if(useAngle-180>=0.0001&&useAngle-180<=0.0001){
				canvas.drawText(usedText, -rf.width()/2, radius/2, mPaint);
				String num=usedNum+"M";
				//��ȡ�ı��ķ�Χ
				mPaint.getTextBounds(num, 0, num.length(), rf);
				//���ı�
				canvas.drawText(num, -rf.width()/2, radius/2+rf.height()+distance, mPaint);
			}else if(-useAngle+360<=10){
				canvas.drawText(usedText, -rf.width()/2, -distance, mPaint);
				String num=usedNum+"M";
				//��ȡ�ı��ķ�Χ
				mPaint.getTextBounds(num, 0, num.length(), rf);
				//���ı�
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
		
		//��ʣ���������ı�
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
//		if(useAngle<45||overplusAngle<45){//���ұ���ʾ���Ƕ�С�ľͲ�����Բ����
			int rectWidth=dip2px(getContext(), 10);//���εĿ��
			mPaint.getTextBounds(usedText, 0, usedText.length(), rf);
			int x2,y2;
			x2 = (int) (radius*Math.cos(Math.PI*30/180))+3;
			y2 = -(int) (radius*Math.sin(Math.PI*30/180))-15;
			//����ɫ�ľ��κ���ʹ���������ı�
			Rect r = new Rect(x2,y2-rf.height()*2/3,x2+rectWidth,y2);
			mPaint.setColor(blue);
			canvas.drawRect(r, mPaint);
			mPaint.setColor(black);
			canvas.drawText(usedText.substring(1, usedText.length()), x2+rectWidth+rectWidth/3, y2, mPaint);
			canvas.drawText(usedNum+"M", x2+rectWidth+rectWidth/3, y2+distance/3+rf.height(), mPaint);
			
			//��ʣ����������ɫ����
			float temp3=y2-rf.height()*2/3;//��ɫ���ζ���λ��
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
	 * �ֱ����������������ռ�ĽǶ�
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
		//����̫С���Ͳ�����
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
	 * ������ʹ�ú�ʣ�������
	 * 
	 * @param usedNum
	 *            ��ʹ�õ�������û����0
	 * @param overplusNum
	 *            ʣ���������û����0
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
	 * �����ֻ��ķֱ��ʴ� dp �ĵ�λ ת��Ϊ px(����)
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
		 * ������ʹ�ú�ʣ�������
		 * @param usedNum
		 *            ��ʹ�õ�������û����0
		 * @param overplusNum
		 *            ʣ���������û����0
		 */
		public static CircleBuilder setUsedAndOverplusNum(float usedNum, float overplusNum){
			mPercentCircle.setUsedAndOverplusNum(usedNum, overplusNum);
			return mCircleBuilder;
		}
		/**�����ı��Ĵ�С
		 * @param textSize
		 * @return
		 */
		public static CircleBuilder setTextSize(int textSize){
			mPercentCircle.setTextSize(textSize);
			return mCircleBuilder;
		}
		/**��������Բ�ĵľ���
		 * @param circleDistance
		 * @return
		 */
		public static CircleBuilder setCircleDistance(int circleDistance){
			mPercentCircle.setCircleDistance(circleDistance);
			return mCircleBuilder;
		}
		/**���������ı�������
		 * @param usedText ��ʹ�õ�����
		 * @param overplusText ʣ������
		 * @return
		 */
		public static CircleBuilder setTextContent(String usedText,String overplusText){
			mPercentCircle.setUsedText(usedText);
			mPercentCircle.setOverplusText(overplusText);
			return mCircleBuilder;
		}
		/**������ִ��ʱ��
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
