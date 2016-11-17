package com.example.test22.viewgroup.folderlayout;

import com.example.test22.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author zc
 *折叠效果
 */
public class FolderLayoutActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new PolyToPolyView(this));
	}
	class PolyToPolyView extends View{
		private static final int NUM_OF_POINT=8;
		private int mTranslateDis;//图片折叠后的总宽度
		private float mFactor = 0.8f;//折叠后的总宽度与原图宽度的比例
		private int mNumOfFolds = 8;//折叠块数
		private Matrix[] mMatrices = new Matrix[mNumOfFolds];
		private Bitmap mBitmap;
		private Paint mSolidPaint;//绘制黑色透明区域的画笔
		private Paint mShadowPaint;//绘制阴影
		private Matrix mShadowGradientMatrix;
		private LinearGradient mShadowGradientShader;
		private int mFlodWidth;//原图每块的宽度
		private int mTranslateDisPerFold;//折叠时，每块的宽度
		public PolyToPolyView(Context context) {
			super(context);
			mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.large);
			//折叠后的总宽度
			mTranslateDis = (int)(mBitmap.getWidth()*mFactor);
			//原图每块的宽度
			mFlodWidth = mBitmap.getWidth()/mNumOfFolds;
			//折叠时每块的宽度
			mTranslateDisPerFold = mTranslateDis/mNumOfFolds;
			
			//初始化matrix
			for(int i=0;i<mNumOfFolds;i++){
				mMatrices[i] = new Matrix();
			}
			mSolidPaint = new Paint();
			int alpha = (int)(255*mFactor*0.8f);
			mSolidPaint.setColor(Color.argb((int)(alpha*0.8F), 0, 0, 0));
			
			/*
			 * new LinearGradient(0, 0, 0.5f, 0,Color.BLACK, Color.TRANSPARENT, TileMode.CLAMP);
起点（0，0）、终点（0.5f，0）；颜色从和BLACK到透明；模式为CLAMP，也就是拉伸最后一个像素。这里你可能会问，这才为0.5个像素的区域设置了渐变，
不对呀，恩，是的，继续看接下来我们使用了setLocalMatrix(mShadowGradientMatrix);，而这个
mShadowGradientMatrix将和坐标扩大了mBitmap.getWidth()倍，也就是说现在设置渐变的区域为（0.5f*mFlodWidth，0）一半的大小，那么后半部分？
后半张应用CLAMP模式，拉伸的透明。
			 */
			mShadowPaint = new Paint();
			mShadowPaint.setStyle(Style.FILL);
			mShadowGradientShader = new LinearGradient(0, 0, 0.5f, 0, Color.BLACK, Color.TRANSPARENT, TileMode.CLAMP);
			mShadowPaint.setShader(mShadowGradientShader);
			mShadowGradientMatrix = new Matrix();
			mShadowGradientMatrix.setScale(mFlodWidth, 1);
			mShadowGradientShader.setLocalMatrix(mShadowGradientMatrix);
			mShadowPaint.setAlpha(alpha);
			//纵轴减小的那个高度，使用勾股定理计算
			int depth =(int) Math.sqrt(mFlodWidth*mFlodWidth-mTranslateDisPerFold*mTranslateDisPerFold)/2;
			//转换点
			float[] src = new float[NUM_OF_POINT];
			float[] dst = new float[NUM_OF_POINT];
			//原图中的每一块，对应折叠后的每一块，方向为左上、右上、右下、左下
			for(int i=0;i<mNumOfFolds;i++){
				src[0] = i*mFlodWidth;
				src[1]=0;
				src[2]=src[0]+mFlodWidth;
				src[3] = 0;
				src[4] = src[2];
				src[5] = mBitmap.getHeight();
				src[6] = src[0];
				src[7] = src[5];
				
				boolean isEvent = i%2==0;
				dst[0]=i*mTranslateDisPerFold;
				dst[1] = isEvent?0:depth;
				dst[2] = dst[0]+mTranslateDisPerFold;
				dst[3] = isEvent?depth:0;
				dst[4] = dst[2];
				dst[5] = isEvent?mBitmap.getHeight()-depth:mBitmap.getHeight();
				dst[6] = dst[0];
				dst[7] = isEvent?mBitmap.getHeight():mBitmap.getHeight()-depth;
				//setPolyToPoly    src.lenth>>1 是8>>1变成4，转换点最多是4个
				//public boolean setPolyToPoly(float[] src, int srcIndex,  float[] dst, int dstIndex,int pointCount) 
				//src代表变换前的坐标；dst代表变换后的坐标；从src到dst的变换，可以通过srcIndex和dstIndex来制定第一个变换的点(即从哪个位置的点，变换到哪一个位置)，一般可能都设置位0。
				//pointCount代表支持的转换坐标的点数，最多支持4个。
				mMatrices[i].setPolyToPoly(src, 0, dst, 0, src.length>>1);
			}
			
		}
		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			//绘制mNumOfFold次
			for(int i=0;i<mNumOfFolds;i++){
				canvas.save();
				//将matrix应用到canvas 
				canvas.concat(mMatrices[i]);
				//控制显示的大小,每一个折叠都是一张图片，只是只显示部分内容，这样叠加起来又是一张图片了
				canvas.clipRect(mFlodWidth*i, 0, mFlodWidth*(i+1), mBitmap.getHeight());
				//绘制图片
				canvas.drawBitmap(mBitmap, 0, 0, null);
				//移动绘制阴影,其实就是把画布移动到下一个位置，如果不调用这个方法，下面的绘制阴影和遮盖的位置就只会在第一个位置了
				canvas.translate(mFlodWidth*i,0);
				if(i%2==0){
					//绘制黑色遮盖
					canvas.drawRect(0, 0, mFlodWidth, mBitmap.getHeight(), mSolidPaint);
				}else{
					//绘制阴影
					canvas.drawRect(0, 0, mFlodWidth, mBitmap.getHeight(), mShadowPaint);
				}
				canvas.restore();
			}
		}
	}

}
