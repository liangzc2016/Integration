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
 *�۵�Ч��
 */
public class FolderLayoutActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new PolyToPolyView(this));
	}
	class PolyToPolyView extends View{
		private static final int NUM_OF_POINT=8;
		private int mTranslateDis;//ͼƬ�۵�����ܿ��
		private float mFactor = 0.8f;//�۵�����ܿ����ԭͼ��ȵı���
		private int mNumOfFolds = 8;//�۵�����
		private Matrix[] mMatrices = new Matrix[mNumOfFolds];
		private Bitmap mBitmap;
		private Paint mSolidPaint;//���ƺ�ɫ͸������Ļ���
		private Paint mShadowPaint;//������Ӱ
		private Matrix mShadowGradientMatrix;
		private LinearGradient mShadowGradientShader;
		private int mFlodWidth;//ԭͼÿ��Ŀ��
		private int mTranslateDisPerFold;//�۵�ʱ��ÿ��Ŀ��
		public PolyToPolyView(Context context) {
			super(context);
			mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.large);
			//�۵�����ܿ��
			mTranslateDis = (int)(mBitmap.getWidth()*mFactor);
			//ԭͼÿ��Ŀ��
			mFlodWidth = mBitmap.getWidth()/mNumOfFolds;
			//�۵�ʱÿ��Ŀ��
			mTranslateDisPerFold = mTranslateDis/mNumOfFolds;
			
			//��ʼ��matrix
			for(int i=0;i<mNumOfFolds;i++){
				mMatrices[i] = new Matrix();
			}
			mSolidPaint = new Paint();
			int alpha = (int)(255*mFactor*0.8f);
			mSolidPaint.setColor(Color.argb((int)(alpha*0.8F), 0, 0, 0));
			
			/*
			 * new LinearGradient(0, 0, 0.5f, 0,Color.BLACK, Color.TRANSPARENT, TileMode.CLAMP);
��㣨0��0�����յ㣨0.5f��0������ɫ�Ӻ�BLACK��͸����ģʽΪCLAMP��Ҳ�����������һ�����ء���������ܻ��ʣ����Ϊ0.5�����ص����������˽��䣬
����ѽ�������ǵģ�����������������ʹ����setLocalMatrix(mShadowGradientMatrix);�������
mShadowGradientMatrix��������������mBitmap.getWidth()����Ҳ����˵�������ý��������Ϊ��0.5f*mFlodWidth��0��һ��Ĵ�С����ô��벿�֣�
�����Ӧ��CLAMPģʽ�������͸����
			 */
			mShadowPaint = new Paint();
			mShadowPaint.setStyle(Style.FILL);
			mShadowGradientShader = new LinearGradient(0, 0, 0.5f, 0, Color.BLACK, Color.TRANSPARENT, TileMode.CLAMP);
			mShadowPaint.setShader(mShadowGradientShader);
			mShadowGradientMatrix = new Matrix();
			mShadowGradientMatrix.setScale(mFlodWidth, 1);
			mShadowGradientShader.setLocalMatrix(mShadowGradientMatrix);
			mShadowPaint.setAlpha(alpha);
			//�����С���Ǹ��߶ȣ�ʹ�ù��ɶ������
			int depth =(int) Math.sqrt(mFlodWidth*mFlodWidth-mTranslateDisPerFold*mTranslateDisPerFold)/2;
			//ת����
			float[] src = new float[NUM_OF_POINT];
			float[] dst = new float[NUM_OF_POINT];
			//ԭͼ�е�ÿһ�飬��Ӧ�۵����ÿһ�飬����Ϊ���ϡ����ϡ����¡�����
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
				//setPolyToPoly    src.lenth>>1 ��8>>1���4��ת���������4��
				//public boolean setPolyToPoly(float[] src, int srcIndex,  float[] dst, int dstIndex,int pointCount) 
				//src����任ǰ�����ꣻdst����任������ꣻ��src��dst�ı任������ͨ��srcIndex��dstIndex���ƶ���һ���任�ĵ�(�����ĸ�λ�õĵ㣬�任����һ��λ��)��һ����ܶ�����λ0��
				//pointCount����֧�ֵ�ת������ĵ��������֧��4����
				mMatrices[i].setPolyToPoly(src, 0, dst, 0, src.length>>1);
			}
			
		}
		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			//����mNumOfFold��
			for(int i=0;i<mNumOfFolds;i++){
				canvas.save();
				//��matrixӦ�õ�canvas 
				canvas.concat(mMatrices[i]);
				//������ʾ�Ĵ�С,ÿһ���۵�����һ��ͼƬ��ֻ��ֻ��ʾ�������ݣ�����������������һ��ͼƬ��
				canvas.clipRect(mFlodWidth*i, 0, mFlodWidth*(i+1), mBitmap.getHeight());
				//����ͼƬ
				canvas.drawBitmap(mBitmap, 0, 0, null);
				//�ƶ�������Ӱ,��ʵ���ǰѻ����ƶ�����һ��λ�ã�����������������������Ļ�����Ӱ���ڸǵ�λ�þ�ֻ���ڵ�һ��λ����
				canvas.translate(mFlodWidth*i,0);
				if(i%2==0){
					//���ƺ�ɫ�ڸ�
					canvas.drawRect(0, 0, mFlodWidth, mBitmap.getHeight(), mSolidPaint);
				}else{
					//������Ӱ
					canvas.drawRect(0, 0, mFlodWidth, mBitmap.getHeight(), mShadowPaint);
				}
				canvas.restore();
			}
		}
	}

}
