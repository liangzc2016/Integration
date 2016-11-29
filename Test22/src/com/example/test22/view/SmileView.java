package com.example.test22.view;

import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

public class SmileView extends View {
	private final int blue = 0xff4aadff;
	private int mWidth = 200;
	private int mHeight = 200;
	private int radius = 20;
	private int lineWidth = 5;
	private float eyeRadius;
	Paint paint,eyePaint;
	DrawFilter drawFilter;
	Path path, pathCircle,pathCircle2;
	PathMeasure pm,pm2;
	float length;// Բ�ܳ�
	float fraction = -1;
	long duration = 2000;
	int repeaCount = 8;
	float x = 0, y = 0;
	ValueAnimator val;

	public SmileView(Context context) {
		this(context, null);
	}

	public SmileView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public SmileView(Context context, AttributeSet attrs, int defStyleAttr) {
		this(context, attrs);
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
			if (changed) {
				mWidth = right - left;
				mHeight = bottom - top;
			}
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		//�ӻ�����ȥ�����
		canvas.setDrawFilter(drawFilter);
		canvas.translate(mWidth / 2, mHeight / 2);
		if (fraction == -1||!val.isRunning())
			first(canvas);
		//�ӵײ���ʼ��һ�����˶���Բ���˶�ʱ��Ϊ0-3/4
		if (0 < fraction && fraction < 0.75) {
			drawCircle(canvas);
		}
		//������
		if (fraction > 1.0 * 3 / 8&&fraction<1.0*6/4) {
			drawOneEye(canvas,0);
		}
		//������
		if(fraction>1.0*5/8&&fraction<1.0*6/4){
			drawOneEye(canvas, 1);
		}
		//����
		if(fraction>=0.75&&fraction<=5.0/4){
			drawFace(canvas);
		}
		//�����˶�����
		if(fraction>=5.0/4&&fraction<=(5.0/4+1.0/4)){
			rotateFace(canvas);
		}
		if(fraction>=6.0/4){
			drawLastFact(canvas);
		}
	}

	/**��̬��Ц��
	 * @param canvas
	 */
	private void first(Canvas canvas) {
		pm2.getSegment(10, length / 2-10, path, true);
		canvas.drawPath(path, paint);
		path = new Path();
		drawEye(canvas);
	}

	/**�ӵײ���ʼ��һ�����˶���Բ���˶�ʱ��Ϊ0-3/4
	 * ����270�ȿ�ʼ����ʱ�뵽0��
	 * @param canvas
	 */
	private void drawCircle(Canvas canvas) {
		float degree = 270 - 270 * 4 / 3 * fraction;
		float x = (float) ((radius ) * Math.cos(Math.PI * degree/180));
		float y = -(float) ((radius ) * Math.sin(Math.PI * degree/ 180));
		canvas.drawCircle(x, y, eyeRadius, eyePaint);
	}

	/**��Բ���˶������۵�λ��ʱ��ͬʱ�������ۣ�ʱ��Ϊ1/4+1/8=3/8
	 * �˶�������λ��ʱ�������ۣ�ʱ��Ϊ1/4+1/8+1/4=5/8
	 * �����۾���λ�ö���Ϊ45��
	 * @param canvas
	 * @param pos 0�������ۣ�1��������
	 */
	public void drawOneEye(Canvas canvas, int pos) {
		float x = (float) ((radius) * Math.cos(Math.PI * 45 / 180));
		float y = x;
		if (pos == 0) {
			canvas.drawCircle(-x, -y, eyeRadius, eyePaint);
		}else if(pos==1){
			canvas.drawCircle(x, -y, eyeRadius , eyePaint);
		}
	}

	/**һ��ʼ�����۾�
	 * @param canvas
	 */
	public void drawEye(Canvas canvas) {
		float x = (float) ((radius) * Math.cos(Math.PI * 45 / 180));
		float y = x;
		canvas.drawCircle(-x, -y, eyeRadius , eyePaint);
		canvas.drawCircle(x, -y, eyeRadius , eyePaint);
	}
	/**Ц�������ǰ��Բ����˽�ȡ����󳤶���length/2
	 * �õ�ʱ����1/2,����֮��fractionӦ�õ���5/4��ʱ���ˣ�1/4+1/8+1/4+1/8+1/2=5/4
	 * @param canvas
	 */
	public void drawFace(Canvas canvas){
		//��Ҫ���¸�path��ֵ
		path=null;
		path = new Path();
		//����ʱ������ȡһ�����ȵ�·�������浽path�У�ȡֵ��Χ(0��length/2)
		pm2.getSegment(0, (float) (length*(fraction-0.75)), path, true);
		canvas.drawPath(path, paint);
	}
	/**��Բ��������ʱ����ת��������ȡ��󳤶Ȼ���length/2,
	 * �˶���ʱ��Ϊ1/4ʱ�䣬��Ҫ���ϸı������յ㣬����Բ���Żᶯ����
	 * @param canvas
	 */
	public void rotateFace(Canvas canvas){
		path=null;
		path = new Path();
		pm2.getSegment((float)(length*(fraction-5.0/4)), (float)(length*(fraction-5.0/4)+length*0.5), path, true);
		canvas.drawPath(path, paint);
	}
	/**ʣ�µ�1/4ʱ�䣬��������һ��PathMeasure,���Բ��·�����
	 * �ǵײ��ģ��ڳ�ʼ��ʱ���Ѿ�����ת������Ϊ�������ñȽϷ���ļ���
	 * �����յ�λ�á�
	 * @param canvas
	 */
	public void drawLastFact(Canvas canvas){
		path = null;
		path = new Path();
		//������1/4���ȿ�ʼ��������ߵ�Բ��)����Բ��·�����յ㣨���ײ���
		pm.getSegment((float)(1.0/4*length+3.0/2*(fraction-3.0/2)*length), (float)(length/2.0+length/8.0+(fraction-3.0/2)*length), path, true);
		canvas.drawPath(path, paint);
	}

	/**
	 * ִ�ж���
	 */
	public void performAnim() {
		//��������ʱ�����������������2�����˶������ܣ������������Ϊ(0,2)
		val = ValueAnimator.ofFloat(0, 2);
		val.setDuration(duration);
		val.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator arg0) {
				fraction = (float) arg0.getAnimatedValue();
				postInvalidate();
			}
		});
		val.setRepeatCount(repeaCount);
		val.start();
		val.setRepeatMode(ValueAnimator.RESTART);
	}
	/**
	 * ֹͣ����
	 */
	public void cancelAnim(){
		if(val.isRunning()){
			val.cancel();
			fraction=-1;
		}
		postInvalidate();
	}
	public void reStart(){
		fraction=-1;
		postInvalidate();
	}

	/**���г�ʼ����֮���Կ����˶���Բ���ܹ����ұ�������ͬʱ����ߵ�Ҳ�ڼ���
	 * ��ʹ��PathMeasure���е�getSegment��������ȡ����һ�γ��ȵ�·��
	 * @param context
	 * @param attrs
	 */
	private void init(Context context, AttributeSet attrs) {
		drawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
				| Paint.FILTER_BITMAP_FLAG);
		lineWidth = dip2px(context, lineWidth);
		radius = dip2px(context, radius);
		path = new Path();
		pathCircle = new Path();
		pathCircle2 = new Path();
		//��path�����һ��˳ʱ���Բ����ʱ��·���������յ�������
		//�ڻ�ǰ�벿�ֵ������˶��е�������������ұ߱ȽϷ���ļ��㣬��������ǲ��֣��˶����յ�
		//����Բ�εĵײ���������·��Բ����ת�����ײ����������
		pathCircle.addCircle(0, 0, radius, Direction.CW);
		pathCircle2.addCircle(0, 0, radius, Direction.CW);
		//����Matrix����pathCircle�е�Բ��ת90�ȣ���������·���������յ㶼�ڵײ���
		Matrix m = new Matrix();
		m.postRotate(90);
		pathCircle.transform(m);
		//�����ı�
		paint = new Paint();
		//���۾��ı�
		eyePaint = new Paint();
		paint.setColor(blue);
		eyePaint.setColor(blue);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(lineWidth);
		eyePaint.setStrokeWidth(lineWidth);
		//���û����ıʵĶ˵�ΪԲ��(�������յ㶼��Բ��)
		paint.setStrokeCap(Paint.Cap.ROUND);
		//ʹ��PathMeasure����·������Ϣ
		pm = new PathMeasure();
		pm.setPath(pathCircle, false);
		pm2 = new PathMeasure();
		pm2.setPath(pathCircle2, false);
		//·���ĳ��ȣ�����·������Բ�Σ����ֻ��������һ������
		length = pm.getLength();
		eyeRadius = (float)(lineWidth/2.0+lineWidth/5.0);
	}
	/**
	 * @param d �˶���ʱ��
	 */
	public void setDuration(long d){
		duration = d;
	}
	/**
	 * @param count ѭ���Ĵ���
	 */
	public void setRepeaCount(int count){
		repeaCount = count;
	}

	/**
	 * �����ֻ��ķֱ��ʴ� dp �ĵ�λ ת��Ϊ px(����)
	 */
	public int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

}
