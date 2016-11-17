package com.example.test22.view;

import com.example.test22.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class ColorTrackView extends View {
	private int mTextStart;
	public enum Direction{
		left,right;
	}
	private int mDirection=DERECTION_LEFT;
	private static final int DERECTION_LEFT=0;
	private static final int DERECTION_RIGHT=1;
    private String mText="×ÖÌåÑÕÉ«±ä»»";
    private Paint paint;
    private int textSize=18;
    private int orginColor=0xff000000;
    private int changeColor=0xffff0000;
    private Rect mTextBound = new Rect();
    private int textWidth;
    private int realWidth;
    private float mProgress=0.1f;
	public ColorTrackView(Context context) {
		this(context,null);
	}

	@SuppressLint("Recycle")
	public ColorTrackView(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.ColorTrackView);
		mText=array.getString(R.styleable.ColorTrackView_text);
		orginColor = array.getColor(R.styleable.ColorTrackView_text_orgin_color, orginColor);
		changeColor=array.getColor(R.styleable.ColorTrackView_text_change_color, changeColor);
		textSize = array.getDimensionPixelSize(R.styleable.ColorTrackView_text_size, textSize);
		mProgress = array.getFloat(R.styleable.ColorTrackView_progress, 0);
		mDirection = array.getInt(R.styleable.ColorTrackView_direction, mDirection);
		array.recycle();
		paint.setTextSize(textSize);
		measureTextSize();
	}
	private void measureTextSize(){
		textWidth =  (int)paint.measureText(mText);
		paint.getTextBounds(mText, 0, mText.length(), mTextBound);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int mode = MeasureSpec.getMode(widthMeasureSpec);
		int val = MeasureSpec.getSize(widthMeasureSpec);
		int mode2 = MeasureSpec.getMode(heightMeasureSpec);
		int val2 = MeasureSpec.getSize(heightMeasureSpec);
		int result=0,result2=0;
		switch(mode){
		case MeasureSpec.EXACTLY:
			result = val;
			break;
		case MeasureSpec.AT_MOST:
		case MeasureSpec.UNSPECIFIED:
			result = textWidth;
			break;
		}
		result = mode==MeasureSpec.AT_MOST?Math.min(result, val):result;
		result = result+getPaddingLeft()+getPaddingRight();
		
		switch(mode2){
		case MeasureSpec.EXACTLY:
			result2 = val2;
			break;
		case MeasureSpec.AT_MOST:
		case MeasureSpec.UNSPECIFIED:
			result2 = mTextBound.height();
			break;
		}
		result2 = mode2==MeasureSpec.AT_MOST?Math.min(result2, val2):result2;
		result2 = result2+getPaddingTop()+getPaddingBottom();
		setMeasuredDimension(result, result2);
		realWidth = getMeasuredWidth()-getPaddingLeft()-getPaddingRight();
		mTextStart = realWidth/2-textWidth/2;
		//super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int r = (int) (mProgress*realWidth+mTextStart);
		if(mDirection==DERECTION_LEFT){
			drawChageLeft(canvas,r);
			drawOriginLeft(canvas,r);
		}else{
			drawOriginRight(canvas,r);
			drawChangeRight(canvas,r);
		}
	}

	private void drawChangeRight(Canvas canvas, int r) {
		drawText(canvas,changeColor,(int)(mTextStart+realWidth-mProgress*realWidth),mTextStart+realWidth);
	}

	private void drawOriginRight(Canvas canvas, int r) {
		drawText(canvas, orginColor, mTextStart, (int)(mTextStart+realWidth-mProgress*realWidth));
		
	}

	private void drawOriginLeft(Canvas canvas, int r) {
		drawText(canvas,orginColor,r,mTextStart+realWidth);
		
	}

	private void drawChageLeft(Canvas canvas, int r) {
		drawText(canvas,changeColor,mTextStart,r);
	}

	private void drawText(Canvas canvas, int color,int start,int end) {
		paint.setColor(color);
		canvas.save(Canvas.CLIP_SAVE_FLAG);
		canvas.clipRect(start, 0, end, getMeasuredHeight());
		canvas.drawText(mText, (int)mTextStart, getMeasuredHeight()/2+mTextBound.height()/2, paint);
		canvas.restore();
	}

	public ColorTrackView(Context context, AttributeSet attrs, int defStyle) {
		this(context, attrs);
	}

	public int getmDirection() {
		return mDirection;
	}

	public void setmDirection(int mDirection) {
		this.mDirection = mDirection;
	}

	public String getmText() {
		return mText;
	}

	public void setmText(String mText) {
		this.mText = mText;
	}

	public float getmProgress() {
		return mProgress;
	}

	public void setmProgress(float mProgress) {
		this.mProgress = mProgress;
		invalidate();
	}

}
