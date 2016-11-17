package com.example.test22;

import org.apache.http.client.CircularRedirectException;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

public class ProgressView extends View {
	private Paint paint;//���ʶ���
	private int roundColor;//Բ������ɫ
	private int roundProgressColor;//Բ�����ȵ���ɫ
    private int textColor;//�����������ɫ
    private float textSize;//����Ĵ�С
    private float roundWidth;//Բ���Ŀ��
    private int max;//���Ľ���
    private int progress;//��ǰ�Ľ���
    private boolean textIsDisplay;//�Ƿ���ʾ�м�Ľ���
    private int style;//���ȵķ���ʵ�Ļ����
    public static int stroke=0;
    public static int fill=1;
	public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		paint = new Paint();
		TypedArray mTypeArray = context.obtainStyledAttributes(attrs,R.styleable.RoundProgressBar);
		//��ȡ�Զ������Ե�ֵ
		roundColor = mTypeArray.getColor(R.styleable.RoundProgressBar_roundColor, Color.RED);
		roundProgressColor = mTypeArray.getColor(R.styleable.RoundProgressBar_roundProgressColor, Color.GREEN);
		textColor = mTypeArray.getColor(R.styleable.RoundProgressBar_textColor, Color.BLACK);
		textSize = mTypeArray.getDimension(R.styleable.RoundProgressBar_textSize, 20);
		roundWidth = mTypeArray.getDimension(R.styleable.RoundProgressBar_roundWidth,5);
		max = mTypeArray.getInteger(R.styleable.RoundProgressBar_max, 100);
		textIsDisplay = mTypeArray.getBoolean(R.styleable.RoundProgressBar_textDisplayable, true);
		style = mTypeArray.getInt(R.styleable.RoundProgressBar_style, 0);
		mTypeArray.recycle();
	}
	public ProgressView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}
	public ProgressView(Context context) {
		this(context,null);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//��������Բ��
		int center = getWidth()/2;//Բ�ĵ�����
		int radius = (int) (center-roundWidth/2);//Բ���İ뾶
		paint.setColor(roundColor);//����Բ������ɫ
		paint.setStyle(Paint.Style.STROKE);//���ÿ���
		paint.setStrokeWidth(roundWidth);//����Բ���Ŀ��
		paint.setAntiAlias(true);//�������
		canvas.drawCircle(center, center, radius, paint);//����Բ��
		//���ٷֱȵ���������
		paint.setStrokeWidth(0);
		paint.setColor(textColor);
		paint.setTextSize(textSize);
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		int percent = (int) (progress/(float)max*100);
		float textWidth=paint.measureText(percent+"%");
		if(textIsDisplay&&percent!=0&&style==stroke){
			canvas.drawText(percent+"%", center-textWidth/2, center+textSize/2, paint);
		}
		//��Բ��
		paint.setStrokeWidth(roundWidth);
		paint.setColor(roundProgressColor);
		//���ڶ���Բ������״�ʹ�С
		RectF rf = new RectF(center-radius,center-radius,center+radius,center+radius);
		switch(style){
		case 0:
			paint.setStyle(Paint.Style.STROKE);
			canvas.drawArc(rf, 0, 360*percent/100, false, paint);
			break;
		case 1:
			rf = new RectF(center-radius-(roundWidth/2),center-radius-(roundWidth/2),2*center,2*center);
			paint.setStyle(Paint.Style.FILL);
			canvas.drawArc(rf, 0, 360*percent/100, true, paint);
			break;
		}
	}
	public synchronized int getMax(){
		return max;
	}
	public synchronized void setMax(int max){
		if(max<0){
			throw new IllegalArgumentException("max not less than 0");
		}
		this.max = max;
	}
	public synchronized int getProgress(){
		return progress;
	}
	public synchronized void setProgress(int progress){
		if(progress<0)throw new IllegalArgumentException("progress not less than 0");
		if(progress>max){
			progress = max;
		}
		if(progress<=max){
			this.progress = progress;
			postInvalidate();
		}
	}
	public int getCircleColor(){
		return roundColor;
	}
	public void setCircleColor(int cirColor){
		this.roundColor = cirColor;
	}
	public int getCircleProgressColor(){
		return this.roundProgressColor;
	}
	public void setCircleProgressColor(int color){
		this.roundProgressColor = color;
	}
	public int getTextColor(){
		return textColor;
	}
	public void setTextColor(int textColor){
		this.textColor = textColor;
	}
	public float getTextSize(){
		return textSize;
	}
	public void setTextSize(float size){
		this.textSize = size;
	}
	public float getRoundWidth(){
		return roundWidth;
	}
	public void setRoundWidth(float width){
		this.roundWidth = width;
	}
}
