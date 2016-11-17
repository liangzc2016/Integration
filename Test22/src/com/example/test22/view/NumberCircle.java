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
 *����ת��ת��
 */
public class NumberCircle extends View {
	private int width;
	private int height;
	/**
	 * ��Բ�뾶
	 */
	private int big_width;
	/**
	 * СԲ�뾶
	 */
	private int small_width;
	private Paint paint;
	private int pure_white;
	private int blue2;
	private int triagle_length = 8;// �����α߳�
	private DrawFilter drawFilter;
	private int degree = 0, degree2 = 0;
	/**
	 * �����������־���Բ�ߵľ���
	 */
	private int textPadding = 15;
	/**
	 * ���²������־���Բ�ߵľ���
	 */
	private int textPadding2 = 15;
	/**
	 * �ı����ֵĴ�С
	 */
	private int fourTextSize = 14;
	/**
	 * �м����ִ�С
	 */
	private int centerTextSize = 18;
	private String leftText = "����", topText = "���·���", rightText = "���",
			bottomText = "�豸��";
	/**
	 * Բ�Ķ��������ֵļ���
	 */
	private List<String> centerTopTexts;
	/**
	 * Բ�ĵײ����ֵļ���
	 */
	private List<String> centerBottomTexts;
	/**
	 * �ϴμ�ͷ���ڷ���
	 */
	private int lastLocation = 0;
	/**
	 * ��ͷ�Ƿ�����ת
	 */
	private boolean isRoating = false;
	/**
	 * ��ת180�ȵ�ʱ�䣬��λΪms
	 */
	private final int rotateTime=500;
	/**
	 * ��ת90�ȵ�ʱ�䣬��λΪms
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
			// ȷ����Բ�뾶
			big_width = Math.min(width, height) / 2 - 10;
			// ȷ��С԰�뾶
			small_width = big_width / 2;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// ��canvas����ȥ������ʱ���
		canvas.setDrawFilter(drawFilter);
		// ��Բ��
		paint.setColor(blue2);
		canvas.drawCircle(width / 2, height / 2, big_width, paint);
		paint.setColor(pure_white);
		canvas.drawCircle(width / 2, height / 2, small_width, paint);
		canvas.save();
		// ������ֱ��
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
	 * ���ı�
	 * 
	 * @param canvas
	 * @param paint2
	 */
	private void drawMyText(Canvas canvas, Paint paint) {
		paint.setColor(pure_white);
		paint.setTextSize(fourTextSize);// ������Ϊ��λ
		Rect rbound = new Rect();
		//��ߵ�����
		paint.getTextBounds(leftText, 0, leftText.length(), rbound);
		canvas.drawText(leftText, width / 2 - big_width + textPadding,
				height / 2+rbound.height()/2, paint);
		//�ϱߵ�����
		paint.getTextBounds(topText, 0, topText.length(), rbound);
		canvas.drawText(topText, width / 2 - rbound.width() / 2, height / 2
				- big_width + textPadding2 + rbound.height(), paint);
		//�ұߵ�����
		paint.getTextBounds(rightText, 0, rightText.length(), rbound);
		canvas.drawText(rightText,width / 2 + big_width - textPadding - rbound.width(),
				height / 2+rbound.height()/2, paint);
		//�±ߵ�����
		paint.getTextBounds(bottomText, 0, bottomText.length(), rbound);
		canvas.drawText(bottomText, width / 2 - rbound.width() / 2, height / 2
				+ big_width - textPadding2, paint);

		// ���м���ı�����
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
	 * ��������
	 * 
	 * @param canvas
	 * @param paint
	 */
	private void drawTriagle(Canvas canvas, Paint paint) {
		Path path = new Path();
		// path.setFillType(FillType.WINDING);
		paint.setColor(pure_white);
		// �����ε���������
		int x1, y1, x2, y2, x3, y3;
		canvas.save();
		//��ת
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
		path.close(); // ʹ��Щ�㹹�ɷ�յĶ����
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
					// ��ͷָ��
					changeAngle(flag);
					return true;
				}
			} else {
				// ����Բ��
				return false;
			}
		}
		return true;
	}
	/**
	 * ��ʼ��ת
	 */
	private void startAnim(){
		ValueAnimator anim = ValueAnimator.ofInt(degree,degree2);
		if(Math.abs(degree2-degree)==90){//��ת90��
			anim.setDuration(rotateTime2);
		}else{
			//��ת180��
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
	 * �����ֻ��ķֱ��ʴ� dp �ĵ�λ ת��Ϊ px(����)
	 */
	public int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * �жϵ��Ƿ���Բ��
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
	 * �жϵ������Ǹ���Χ��
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private int whichEdge(float x, float y) {
		int flag = -1;
		// ��б��Ϊ45�ȣ��ұ�����ֱ��y=-x+width/2+height/2;
		// ��ߵ�ֱ��Ϊ��y=x-(width/2-height/2),��תΪandorid ����ϵ��
		float y2 = -x + width / 2 + height / 2;
		float y3 = x - (width / 2 - height / 2);
//		Log.e("�㷶Χ", "y2=" + y2 + " y3=" + y3 + " y=" + y);
		if (y > y3 && y < y2)
			flag = 0;// ����෶Χ
		if (y < y3 && y < y2)
			flag = 1;// �ϱ߷�Χ
		if (y < y3 && y > y2)
			flag = 2;// �ұ߷�Χ
		if (y > y3 && y > y2)
			flag = 3;// �ײ���Χ
		return flag;
	}

	/**
	 * ��תǰ�ļ���
	 * 
	 * @param flag
	 */
	private void changeAngle(int flag) {
		if (lastLocation == flag) {// λ��һ��
			isRoating = false;
			return;
		}
		switch (lastLocation) {
		case 0:// ��0λ�õ�����λ��
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
		case 1:// ���ϱ�λ�õ�����λ��
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
		case 2:// ���ұߵ�����λ��
			switch (flag) {
			case 0:
				// ˳ʱ��ת
				degree2 = 360;
				degree = 180;
				break;
			case 1:// ��ʱ��ת
				degree2 = 90;
				degree = 180;
				break;
			case 3:// ˳ʱ��
				degree2 = 270;
				degree = 180;
				break;
			}
			break;
		case 3:// �ӵײ�λ��ת������λ��
			switch (flag) {
			case 0:// ˳ʱ��
				degree2 = 360;
				degree = 270;
				break;
			case 1:// ˳ʱ��
				degree2 = 90;
				degree = 270;
				break;
			case 2:// ��ʱ��
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
	 * ��ʼ��Բ��λ�õ�λ������
	 */
	private void initCenterTextContent(){
		centerTopTexts = new ArrayList<String>();
		centerBottomTexts = new ArrayList<String>();
		centerTopTexts.add("ʣ������");
		centerTopTexts.add("���·���");
		centerTopTexts.add("���");
		centerTopTexts.add("�豸��");
		
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
