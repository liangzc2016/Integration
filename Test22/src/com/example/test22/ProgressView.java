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
	private Paint paint;//画笔对象
	private int roundColor;//圆环的颜色
	private int roundProgressColor;//圆环进度的颜色
    private int textColor;//进度字体的颜色
    private float textSize;//字体的大小
    private float roundWidth;//圆环的宽度
    private int max;//最大的进度
    private int progress;//当前的进度
    private boolean textIsDisplay;//是否显示中间的进度
    private int style;//进度的方格，实心或空心
    public static int stroke=0;
    public static int fill=1;
	public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		paint = new Paint();
		TypedArray mTypeArray = context.obtainStyledAttributes(attrs,R.styleable.RoundProgressBar);
		//获取自定义属性的值
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
		//画最外层的圆环
		int center = getWidth()/2;//圆心的坐标
		int radius = (int) (center-roundWidth/2);//圆环的半径
		paint.setColor(roundColor);//设置圆环的颜色
		paint.setStyle(Paint.Style.STROKE);//设置空心
		paint.setStrokeWidth(roundWidth);//设置圆环的宽度
		paint.setAntiAlias(true);//消除锯齿
		canvas.drawCircle(center, center, radius, paint);//画出圆环
		//画百分比的文字内容
		paint.setStrokeWidth(0);
		paint.setColor(textColor);
		paint.setTextSize(textSize);
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		int percent = (int) (progress/(float)max*100);
		float textWidth=paint.measureText(percent+"%");
		if(textIsDisplay&&percent!=0&&style==stroke){
			canvas.drawText(percent+"%", center-textWidth/2, center+textSize/2, paint);
		}
		//画圆弧
		paint.setStrokeWidth(roundWidth);
		paint.setColor(roundProgressColor);
		//用于定义圆弧的形状和大小
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
