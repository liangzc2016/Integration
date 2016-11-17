package com.example.test22.viewgroup.folderlayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class FolderLayout extends ViewGroup {
	private static int NUM_OF_POINT = 8;
	/**
	 * 图片折叠后的宽度
	 */
	private float mTranslateDis;
	private float mFactor=0.6f;//缩放的比例
	private int mNumOfFolds=8;//分成多少块
	private Matrix[] matries = new Matrix[mNumOfFolds];
	
	Paint mSolidPaint;
	Paint mShadowPaint;
	private Matrix mShadowGradientMatrix;
	private LinearGradient mShadowGradientShader;
	
	private float mFlodWidth;//每一块的宽度
	private float mTranslateDisPerFold;//折叠后的每一块的宽度
	Bitmap mBitmap;
	Canvas mCanvas = new Canvas();;

	public FolderLayout(Context context) {
		this(context,null);
	}

	public FolderLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		for(int i=0;i<mNumOfFolds;i++){
			matries[i] = new Matrix();
		}
		mSolidPaint = new Paint();
		mShadowPaint =new Paint();
		mShadowPaint.setStyle(Style.FILL);
		mShadowGradientShader = new LinearGradient(0, 0, 0.5f, 0, Color.BLACK, Color.TRANSPARENT, TileMode.CLAMP);
		mShadowPaint.setShader(mShadowGradientShader);
		mShadowGradientMatrix = new Matrix();
		this.setWillNotDraw(false);
	}

	public FolderLayout(Context context, AttributeSet attrs, int defStyle) {
		this(context, attrs);
	}

	@Override
	protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
		View child = getChildAt(0);
		child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
		mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Config.ARGB_8888);
		mCanvas.setBitmap(mBitmap);
		updateFold();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		View child = getChildAt(0);
		measureChild(child, widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(child.getMeasuredWidth(), child.getMeasuredHeight());
		//super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	protected void updateFold() {
		int w  = getWidth();
		int h = getHeight();
		mTranslateDis = mFactor*w;
		mFlodWidth = w/mNumOfFolds;
		mTranslateDisPerFold = mTranslateDis/mNumOfFolds;
		//int alpha = (int)(255*mFactor);
		int alpha = (int)(255*0.6*0.8f);
		mSolidPaint.setColor(Color.argb(alpha, 0, 0, 0));
		mShadowGradientMatrix.setScale(mFlodWidth, 1);
		mShadowGradientShader.setLocalMatrix(mShadowGradientMatrix);
		mShadowPaint.setAlpha(alpha);
		float mDepth = (float)Math.sqrt((mFlodWidth*mFlodWidth-mTranslateDisPerFold*mTranslateDisPerFold)/2);
		float[] src = new float[NUM_OF_POINT];
		float[] dst = new float[NUM_OF_POINT];
		for(int i=0;i<NUM_OF_POINT;i++){
			matries[i].reset();
			src[0]=i*mFlodWidth;
			src[1]=0;
			src[2]=src[0]+mFlodWidth;
			src[3]=0;
			src[4]=src[2];
			src[5]=h;
			src[6]=src[0];
			src[7]=h;
			boolean isEvent=i%2==0;//判断是否奇数点还是偶数点
			dst[0]=i*mTranslateDisPerFold;
			dst[1]=isEvent?0:mDepth;
			dst[2]=(i+1)*mTranslateDisPerFold;
			dst[3]=isEvent?mDepth:0;
			dst[4]=dst[2];
			dst[5]=isEvent?h-mDepth:h;
			dst[6]=dst[0];
			dst[7]=isEvent?h:h-mDepth;
			for(int j=0;j<NUM_OF_POINT;j++){
				dst[j] = Math.round(dst[j]);
			}
			matries[i].setPolyToPoly(src, 0, dst, 0, src.length>>1);
		}
	}
	private boolean isReady;
     @Override
    protected void dispatchDraw(Canvas canvas) {
    	if(mFactor==0)return;
    	if(mFactor==1){
    		super.dispatchDraw(canvas);
    		return;
    	}
    	for(int i=0;i<mNumOfFolds;i++){
    		canvas.save();
    		canvas.concat(matries[i]);
    		//设置显示区域
    		canvas.clipRect(i*mFlodWidth, 0, (i+1)*mFlodWidth, getHeight());
    		if(isReady){
    			canvas.drawBitmap(mBitmap, 0,0, null);
    		}else{
    			super.dispatchDraw(mCanvas);
    			canvas.drawBitmap(mBitmap, 0, 0, null);
    			isReady=true;
    		}
    		canvas.translate(i*mFlodWidth, 0);
    		if(i%2==0){
    			//
    			canvas.drawRect(0, 0, mFlodWidth, getHeight(), mSolidPaint);
    		}else{
    			canvas.drawRect(0, 0, mFlodWidth, getHeight(), mShadowPaint);
    		}
    		canvas.restore();
    	}
    	
    }
     public void setFactor(float factor){
 		this.mFactor=factor;
 		updateFold();
 		invalidate();
 	}
     public float getFactor(){
    	 return this.mFactor;
     }
}
