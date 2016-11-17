package com.example.test22.view;

import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.os.Debug;
import android.support.v4.util.DebugUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;

public class LargImageView extends View {
	private BitmapRegionDecoder mDecoder;
	private int mImageWidth,mImageHeight;//图片的宽和高
	private volatile Rect mRect = new Rect();//绘制区域
    ScaleGestureDetector mDetector;
    Context context;
    private static final BitmapFactory.Options options = new BitmapFactory.Options();
    static{
    	options.inPreferredConfig = Bitmap.Config.RGB_565;
    }
    public void setInputStream(InputStream is){
    	try {
			mDecoder = BitmapRegionDecoder.newInstance(is,false);
			BitmapFactory.Options tmpOptions = new BitmapFactory.Options();
			tmpOptions.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(is, null, tmpOptions);
			mImageWidth = tmpOptions.outWidth;
			mImageHeight = tmpOptions.outHeight;
			Log.e("width", ""+mImageWidth);
			Log.e("height",""+mImageHeight);
			requestLayout();
			invalidate();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try{
				if(is!=null)is.close();
			}catch(Exception e){
				
			}
		}
    }
    public void init(){
    }
    private void checkWidth(){
    	if(mRect.right>mImageWidth){
    		mRect.right = mImageWidth;
    		mRect.left = mImageWidth-getWidth();
    	}
    	if(mRect.left<0){
    		mRect.right = getWidth();
    		mRect.left = 0;
    	}
    }
    private void checkHeight(){
    	if(mRect.top<0){
    		mRect.top = 0;
    		mRect.bottom = getHeight();
    	}
    	if(mRect.bottom>mImageHeight){
    		mRect.top = mImageHeight-getHeight();
    		mRect.bottom = mImageHeight;
    	}
    }
	public LargImageView(Context context, AttributeSet attrs, int defStyle) {
		this(context, attrs);
	}

	public LargImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public LargImageView(Context context) {
		this(context,null);
	}
	int lastX=0,lastY=0;
	@SuppressLint("NewApi")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
//		mDetector.onTouchEvent(event);
//		return true;
		int moveX1 = 0,moveY1=0;
		if(event.getAction()==event.ACTION_DOWN){
			moveX1 = (int)event.getX();
			moveY1 = (int)event.getY();
			Log.e("movex1", moveX1+"&&Imagewidth=="+mImageWidth);
			Log.e("movey1",moveY1+"&&ImageHeight=="+mImageHeight);
		}
        if(event.getAction() ==event.ACTION_UP){
		int moveX = (int) event.getX();
		int moveY = (int) event.getY();
		if(moveX1>moveX)
		moveX-=moveX1;
		moveY-=moveY1;
		Log.e("movex", moveX+"&&Imagewidth=="+mImageWidth);
		Log.e("movey",moveY+"&&ImageHeight=="+mImageHeight);
		if(mImageWidth>getWidth()){
			mRect.offset(-moveX, 0);
			Log.e("rect.left",mRect.left+"");
			Log.e("rect.right", mRect.right+"");
			checkWidth();
			invalidate();
		}
		if(mImageHeight>getHeight()){
			mRect.offset(0, -moveY);
			checkHeight();
			invalidate();
		}
        }
		return true;
	
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		Bitmap bm = mDecoder.decodeRegion(mRect, options);
		canvas.drawBitmap(bm, 0, 0, null);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = getMeasuredWidth();
		int height = getMeasuredHeight();
		int imageWidth = mImageWidth;
		int imageHeight = mImageHeight;
		//可以自己设置，默认直接显示图片的中心区域
		mRect.left = mImageWidth/2-width/2;
		mRect.right = mRect.left+width;
		mRect.top = mImageHeight/2-height/2;
		mRect.bottom = mImageHeight+height;
	}

}
