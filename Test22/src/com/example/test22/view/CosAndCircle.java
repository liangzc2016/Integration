package com.example.test22.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Style;
import android.graphics.Path.Direction;
import android.graphics.Region.Op;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.example.test22.R;

public class CosAndCircle extends FrameLayout {
	private static final int DEFAULT_TEXTCOLOR = 0xffffffff;
	private static final int DEFAULT_TEXTSIZE=250;
	private float mPercent;
	Paint paint;
	private Bitmap mBitmap;
	private Bitmap mScaleBitmap;
	private float mLeft,mTop;
	private int mSpeed = 15;
	private int mRepeatCount=0;
	private Status mFlag = Status.NONE;
	private int mTextColor = DEFAULT_TEXTCOLOR;
	private int mTextSize = DEFAULT_TEXTSIZE;
	
	
	public void setmPercent(float mPercent) {
		mFlag = Status.RUNNING;
		this.mPercent = mPercent;
		postInvalidate();
	}

	public void setmSpeed(int mSpeed) {
		this.mSpeed = mSpeed;
	}

	public void setmFlag(Status mFlag) {
		this.mFlag = mFlag;
	}

	public void setmTextColor(int mTextColor) {
		this.mTextColor = mTextColor;
	}

	public void setmTextSize(int mTextSize) {
		this.mTextSize = mTextSize;
	}
	public void clear(){
		mFlag = Status.NONE;
		if(mScaleBitmap!=null){
//			mScaleBitmap.recycle();
			mScaleBitmap=null;
		}
		if(mBitmap!=null){
//			mBitmap.recycle();
			mBitmap=null;
		}
	}
	public CosAndCircle(Context context) {
	this(context,null);
	}

	public CosAndCircle(Context context, AttributeSet attrs) {
		super(context, attrs,0);
		
	}

	public CosAndCircle(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs,defStyle);
	}
	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		paint = new Paint();
		int width = getWidth();
		int height=getHeight();
		//≤√ºÙ‘≤–Œ«¯”Ú
		Path path = new Path();
		canvas.save();
		path.reset();
		canvas.clipPath(path);
		path.addCircle(width/2, height/2, width/2, Direction.CCW);
		canvas.clipPath(path, Op.REPLACE);
		if(mFlag==Status.RUNNING){
			if(mScaleBitmap==null){
				mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wave);
				mScaleBitmap = Bitmap.createScaledBitmap(mBitmap,mBitmap.getWidth(),mBitmap.getHeight(),false);
//				mBitmap.recycle();
				mBitmap=null;
				mRepeatCount = (int)Math.ceil(getWidth()/mScaleBitmap.getWidth()+0.5)+1;
			}
			for(int i=0;i<mRepeatCount;i++){
				canvas.drawBitmap(mScaleBitmap, mLeft+(i-1)*mScaleBitmap.getWidth(), (1-mPercent)*getHeight(), null);
			}
			String str = (int)(mPercent*100)+"%";
			paint.setColor(mTextColor);
			paint.setTextSize(mTextSize);
			paint.setStyle(Style.FILL);
			canvas.drawText(str, (getWidth()-paint.measureText(str))/2, getHeight()/2+mTextSize/2, paint);
			mLeft+=mSpeed;
			if(mLeft>=mScaleBitmap.getWidth())mLeft=0;
			//¿L—uÕ‚àA≠h
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(4);
			paint.setAntiAlias(true);
			paint.setColor(Color.rgb(33, 211, 39));
			canvas.drawCircle(width/2, height/2, width/2, paint);
			postInvalidateDelayed(20);
		}
		canvas.restore();
	}
	private double degreeToRad(double degree){
		return degree*Math.PI/180;
	}
	
	private Bitmap getImage(){
		int width = getWidth();
		int height=getHeight()/2;
		Bitmap bt = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas can = new Canvas(bt);
		can.drawColor(R.color.blue);
		can.drawCircle(width/2,  height/2, height/2, paint);
		return bt;
	}
	 public enum Status {
	        RUNNING, NONE
	    }

}
