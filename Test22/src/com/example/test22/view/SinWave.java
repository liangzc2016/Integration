package com.example.test22.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.example.test22.R;
import com.example.test22.utils.ScreenUtils;

public class SinWave extends View {
	Paint paint;
	//������ɫ
	private static final int WAVE_PAINT_COLOR=0x880000aa;
	private static final float STRETCH_FACTOR_A = 20;  
    private static final int OFFSET_Y = 0; 
    //y = Asin(wx+b)+h;
	private int translate_x_speed = 7;
	private int mTotalWidth,mTotalHeight;
	private int mXOffsetSpeedOne;//�ƶ����ٶ�
	private int mXOneOffset;//Ҫ�ƶ��ľ���
	private float[] mYpositions;
	private float[] mResetOneYPositions;
	
	private Paint mWavePaint;
	private DrawFilter drawFilter;
	public SinWave(Context context) {
		this(context,null);
	}

	public SinWave(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public SinWave(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		//��dpת��Ϊpx���û����Ʋ�ͬ�ֱ������ƶ��ٶ�һ��
		mXOffsetSpeedOne = ScreenUtils.dip2px(context,translate_x_speed);
		//��ʼ�����ƵĻ���
		mWavePaint = new Paint();
		//ȥ�����
		mWavePaint.setAntiAlias(true);
		//���÷��Ϊʵ��
		mWavePaint.setStyle(Style.FILL);
		mWavePaint.setColor(WAVE_PAINT_COLOR);
		drawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
//		super.onDraw(canvas);
//		canvas.save();
		//��canvas����ȥ������ʱ���
		canvas.setDrawFilter(drawFilter);
		resetPositionY();
		for(int i=0;i<mTotalWidth;i++){
			canvas.drawLine(i, (mTotalHeight-mResetOneYPositions[i])-100, i, mTotalHeight, mWavePaint);
		}
		//�ı䲨�Ƶ��ƶ���
		mXOneOffset+=mXOffsetSpeedOne;
		//����ƶ�����β���������¿�ʼ
		if(mXOneOffset>=mTotalWidth){
			mXOneOffset=0;
		}
		Bitmap bitMap = null;
//		if(getBackground()!=null){
//			Drawable drawable = getBackground();
//			bitMap = Bitmap.createBitmap(
//                    drawable.getIntrinsicWidth(),
//                    drawable.getIntrinsicHeight(),
//                    drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
//                                    : Bitmap.Config.RGB_565);
//		}
//		bitMap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
//		int max = Math.max(mTotalWidth, mTotalHeight);
//		Bitmap mBitMap = Bitmap.createScaledBitmap(bitMap, mTotalWidth, mTotalHeight, true);
//		PorterDuffXfermode pd = new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP);
//		mWavePaint.setXfermode(pd);
//		canvas.drawBitmap(mBitMap, 0, 0, mWavePaint);
//		pd = null;
//		canvas.restore();
		postInvalidateDelayed(20);//���»���
	}
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mTotalWidth = w;
		mTotalHeight=h;
		//����ԭʼ���Ƶ�ֵ
		mYpositions = new float[mTotalWidth];
		//�ƶ����Ƶ�ֵ
		mResetOneYPositions = new float[mTotalWidth];
		//�����ڶ�Ϊview�ܿ��
		float time =(float) (2*Math.PI/mTotalWidth);
		//����y�����ֵ
		for(int i=0;i<mTotalWidth;i++){
			mYpositions[i] = (float) (STRETCH_FACTOR_A * Math.sin(time * i) +OFFSET_Y);
		}
	}
	private void resetPositionY() {
		//mXoneOffset����Ҫ�ƶ��ľ���
		int yInternal = mTotalWidth-mXOneOffset;
		//�ı�yֵ,������ԭ���飬��ʼ���Ƶ�λ�ã��µ����飬�����鿪ʼ��λ�ã�����
		System.arraycopy(mYpositions, mXOneOffset, mResetOneYPositions, 0, yInternal);
		System.arraycopy(mYpositions, 0, mResetOneYPositions, yInternal, mXOneOffset);
	}

}
