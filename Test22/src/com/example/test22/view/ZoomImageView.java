package com.example.test22.view;

import java.lang.annotation.Target;
import java.security.acl.LastOwnerException;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewConfiguration;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;

/**
 * @author zc
 *ͼ��ĵ�����
 */
public class ZoomImageView extends ImageView implements OnScaleGestureListener, OnTouchListener, OnGlobalLayoutListener {
	public static final float SCALE_MAX=4.0f;
	public static final  float SCLAE_MID = 2.0f;
	/**
	 * ��ʼ��ʱ�����ű��������ͼƬ���ߴ�����Ļ����ֵ��С��1
	 */
	public float initScale=1.0f;
	/**
	 * ���ڴ�ž����9��ֵ
	 */
	private final float[] matrixValues = new float[9];
	private boolean once = true;
	/**
	 * �������Ƶļ��
	 */
	private ScaleGestureDetector mScaleGestureDetector;
	private final Matrix mScaleMatrix = new Matrix();
	RectF rect = new RectF();
	private int mTouchSlop;
	GestureDetector mGestureDetector;
	private boolean isAutoScale = false;
	
	public ZoomImageView(Context context) {
		this(context,null);
	}
	public ZoomImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		super.setScaleType(ScaleType.MATRIX);
		mScaleGestureDetector = new ScaleGestureDetector(context, this);
		mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
			@Override
			public boolean onDoubleTap(MotionEvent e) {
				if(isAutoScale){
					return true;
				}
				float x = e.getX();
				float y = e.getY();
				Log.e("DoubleTap",""+getScale()+","+initScale);
				if(getScale()<SCLAE_MID){
					ZoomImageView.this.postDelayed(new AutoScaleRunnable(SCLAE_MID, x, y), 16);
					isAutoScale = true;
				}else if(getScale()>=SCLAE_MID&&getScale()<SCALE_MAX){
					ZoomImageView.this.postDelayed(new AutoScaleRunnable(SCALE_MAX, x, y), 16);
					isAutoScale = true;
				}else if(getScale()==SCALE_MAX){
					ZoomImageView.this.postDelayed(new AutoScaleRunnable(initScale, x, y), 16);
					isAutoScale = true;
				}
				return true;
			}
		});
		//mTouchSlop = 48;
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		this.setOnTouchListener(this);
	}
	/**
	 * @author zc
	 *�Զ����ŵ�����
	 */
	private class AutoScaleRunnable implements Runnable{
		static final float BIGGER = 1.07f;
		static final float SMALLER = 0.93f;
		private float mTargetScale;
		private float tmpScale;
		//���ŵ�����
		private float x;
		private float y;
		/**����Ŀ��ֵ������Ŀ��ֵ�뵱ǰֵ���ж�Ӧ�÷Ŵ�����С
		 * @param targetScale
		 * @param x
		 * @param y
		 */
		public AutoScaleRunnable(float targetScale, float x, float y){
			this.mTargetScale = targetScale;
			this.x = x;
			this.y = y;
			if(getScale()<mTargetScale){
				tmpScale=BIGGER;
			}else{
				tmpScale = SMALLER;
			}
		}
		@Override
		public void run() {
			//��������
			mScaleMatrix.postScale(tmpScale, tmpScale, x, y);
			checkBorderAndCenter();
			setImageMatrix(mScaleMatrix);
			final float currentScale=getScale();
			//���ֵ�ں���ķ�Χ�ڣ���������
			if(((tmpScale>1f)&&(currentScale<mTargetScale))||
					((tmpScale<1f)&&(currentScale>mTargetScale))){
				ZoomImageView.this.postDelayed(new AutoScaleRunnable(mTargetScale,x,y), 16);
			}else{//����ΪĿ������ű�������Ϊ���ܻ�ŵù��󣬻��߹�С
				final float deltaScale=mTargetScale/currentScale;
				mScaleMatrix.postScale(deltaScale, deltaScale, x, y);
				checkBorderAndCenter();
				setImageMatrix(mScaleMatrix);
				isAutoScale=false;
			}
		}
		
	}
	@Override
	public boolean onScale(ScaleGestureDetector detector) {
	    float scale = getScale();//��һ�ε����ŵ�ֵ������ֵ����scale*scaleFactor
	    Log.e("getScale==", ""+scale);
	    float scaleFactor = detector.getScaleFactor();//Ҫ���ŵı���
	    Log.e("scaleFactor1==",""+scaleFactor);
	    if(getDrawable()==null)return true;
	    //���ŷ�Χ�Ŀ��ƣ��ֱ��ǷŴ����С�������ж�
	    if((scale<SCALE_MAX&&scaleFactor>1.0f)||(scale>initScale&&scaleFactor<1.0f)){
	    	//�µ�scale=scaleFactor*scale��
	    	Log.e("scaleFactor*scale", scaleFactor*scale+"");
	    	//��Сֵ�����ֵ�ж�
	    	if(scaleFactor*scale<initScale){
	    		scaleFactor = initScale/scale;
	    		Log.e("initScale/scale",""+scaleFactor);
	    	}
	    	if(scaleFactor*scale>SCALE_MAX){
	    		scaleFactor = SCALE_MAX/scale;
	    		Log.e("SCALE_MAX/scale",""+scaleFactor);
	    	}
	    	Log.e("scaleFactor2==",""+scaleFactor);
	    	//�������ű���
//	    	mScaleMatrix.postScale(scaleFactor, scaleFactor, getWidth()/2, getHeight()/2);//��������
	    	mScaleMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());//����λ������
	    	checkBorderAndCenter();//���߽�
	    	setImageMatrix(mScaleMatrix);
	    }
		return true;
	}
	@Override
	public boolean onScaleBegin(ScaleGestureDetector arg0) {
//		Log.e("getFocusX()1", arg0.getFocusX()+"");
		return true;
	}
	@Override
	public void onScaleEnd(ScaleGestureDetector arg0) {
//		Log.e("getFocusX()2", arg0.getFocusX()+"");
	}
	private float mLastX=0,mLastY=0;
	private float lastPointerCount=0;
	private boolean isCanDrag =false;
	@Override
	public boolean onTouch(View arg0, MotionEvent event) {
		if(mGestureDetector.onTouchEvent(event)){
			return true;
		}
		mScaleGestureDetector.onTouchEvent(event);
		myMove2(event);
//		myMove(event);
		return true;
	}
	private void myMove2(MotionEvent event) {
		float x = 0;
		float y = 0;
		//�õ�������ĸ���
		final int pointerCount = event.getPointerCount();
		//�õ�����������x��y ��ƽ��ֵ
		for(int i=0;i<pointerCount;i++){
			x+=event.getX(i);
			y+=event.getY(i);
		}
		x = x/pointerCount;
		y = y/pointerCount;
		Log.e("x/pointerCount����y/pointerCount", ""+x/pointerCount+" "+y/pointerCount);
		//ÿ�������㷢���仯ʱ������mlastx,mlasty
		if(pointerCount!=lastPointerCount){
			mLastX = x;
			mLastY = y;
			isCanDrag = false;
		}
		lastPointerCount = pointerCount;
		switch(event.getAction()){
		case MotionEvent.ACTION_MOVE:
			float dx = x-mLastX;
			float dy = y-mLastY;
			if(!isCanDrag){
				isCanDrag = isCanDrag(dx,dy);
			}
			if(isCanDrag){
				myMove3(dx,dy);
			}
			mLastX = x;
			mLastY = y;
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			lastPointerCount = 0;
			break;
		}
	}
	private boolean isCanDrag(float dx, float dy) {
		return Math.sqrt((dx*dx)+(dy*dy))>=mTouchSlop;
	}
	private float getScale(){
		mScaleMatrix.getValues(matrixValues);
		return matrixValues[Matrix.MSCALE_X];
	}
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		getViewTreeObserver().addOnGlobalLayoutListener(this);
	}
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		getViewTreeObserver().removeGlobalOnLayoutListener(this);
	}
	@Override
	public void onGlobalLayout() {
		if(once){
			Drawable d = getDrawable();
			if(d==null)return;
			Log.e("ͼƬ��͸�", ""+d.getIntrinsicWidth()+" "+d.getIntrinsicHeight());
			int width = getWidth();
			int height = getHeight();
			//�õ�ͼƬ�Ŀ�͸�
			int dw = d.getIntrinsicWidth();
			int dh = d.getIntrinsicHeight();
			float scale = 1.0f;
			//���ͼƬ�Ŀ��ߴ�����Ļ�Ŀ��ߣ�����������Ļ�Ŀ���
			if(dw>width&&dh<=height){
				scale = 1.0f*width/dw;
				Log.e("���scale==", ""+scale);
			}
			if(dh>height&&dw<=width){
				scale = 1.0f*height/dh;
			}
			//�����͸߶�������Ļ�������䰴������Ӧ��Ļ��С
			if(dh>height&&dw>width){
				scale = Math.min(1.0f*width/dw, 1.0f*height/dh);
			}
			initScale = scale;
			//ͼƬƽ�ƶ�����Ļ���ģ�����Ϊ�ƶ��ľ���
			mScaleMatrix.postTranslate((width-dw)/2, (height-dh)/2);
			Log.e("(width-dw)/2��(height-dh)/2", ""+(width-dw)/2+" "+(height-dh)/2);
			Log.e("���ؼ��Ŀ�͸�",""+width+" "+height);
			mScaleMatrix.postScale(scale, scale, getWidth()/2, getHeight()/2);
			setImageMatrix(mScaleMatrix);
			once = false;
		}
	}
	/**
	 * ������ʱ�򣬽���ͼƬ��ʾ��Χ����
	 */
	private void checkBorderAndCenter(){
		RectF rect = getMatrixRectF();
		float deltaX = 0;
		float deltaY = 0;
		int width = getWidth();
		int height = getHeight();
		//������ߴ�����Ļ������Ʒ�Χ
		if(rect.width()>=width){
			if(rect.left>0){
				deltaX = -rect.left;
			}
			if(rect.right<width){
				deltaX = width-rect.right;
			}
		}
		if(rect.height()>=height){
			if(rect.top>0){
				deltaY = -rect.top;
			}
			if(rect.bottom<height){
				deltaY = height-rect.bottom;
			}
		}
		//�������С����Ļ�����������
		if(rect.width()<width){
			deltaX = width*0.5f-(rect.right-0.5f*rect.width());
		}
		if(rect.height()<height){
			deltaY = height*0.5f-(rect.bottom-0.5f*rect.height());
		}
		Log.e("rect.width��rect.height", ""+rect.width()+" "+rect.height());
		Log.e("deltaX��deltaY", ""+deltaX+" "+deltaY);
		mScaleMatrix.postTranslate(deltaX, deltaY);
	}
	/**���ݵ�ǰͼƬ��Matrix ���ͼƬ�ķ�Χ
	 * @return
	 */
	private RectF getMatrixRectF() {
		Matrix matrix =mScaleMatrix;
		Drawable d = getDrawable();
		if(d!=null){
			rect.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
			matrix.mapRect(rect);
		}
		Log.e("rect.left��rect.right", rect.left+" "+rect.right);
		Log.e("rect.top��rect.bottom", rect.top+" "+rect.bottom);
		return rect;
	}
	float common=0.001f;
	float canMove=0.001f;
	private void myMove(MotionEvent event){
//		checkBorderAndCenter();
		float x = event.getX();
		float y = event.getY();
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			mLastX = x;
			mLastY = y;
			break;
		case MotionEvent.ACTION_MOVE:
			getMatrixRectF();
			float dx = -mLastX + x;
			float dy = -mLastY + y;
			//����ƶ��ı߽�
			if(dy>canMove){//�����ƶ�
				if(rect.top<common){
					if(Math.abs(rect.top)-dy<common){
						dy = -rect.top;
					}
				}else{
					dy=0.0f;
				}
			}else if(dy<canMove){//�����ƶ�
				if(rect.bottom-getHeight()>common){
					if(Math.abs(dy)-rect.bottom>common){
						dy = -(rect.bottom-getHeight());
					}
				}else{
					dy=0.0f;
				}
			}
			if(dx>canMove){//�����ƶ�
				if(rect.left<common){
					if(Math.abs(rect.left)-dx<=common){
						dx = -rect.left;
					}
				}else{
					dx=0.0f;
				}
			}else if(dx<canMove){//�����ƶ�
				if(rect.right-getWidth()>common){
					if(Math.abs(dx)-rect.right>common){
						dx = -(rect.right-getWidth());
					}
				}else{
					dx=0.0f;
				}
			}
			mLastX = x;
			mLastY = y;
			mScaleMatrix.postTranslate(dx, dy);
			//setImageMatrix(mScaleMatrix);
			reSet();
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			
			break;
		}
	}
	/**
	 * ���ٻ�����ʱ���ұߺ��±߶�����ֿհף���ʱ��Ҫ�ָ�
	 */
	private void reSet(){
		getMatrixRectF();
		float dx=0,dy=0;
		if(rect.right<getWidth()){
			dx = (getWidth()-rect.right);
		}
		if(rect.bottom<getBottom()&&rect.top<=0){
			dy = getHeight()-rect.bottom;
		}
		mScaleMatrix.postTranslate(dx, dy);
		setImageMatrix(mScaleMatrix);
	}
	
	private void myMove3(float dx,float dy){
//		checkBorderAndCenter();
		getMatrixRectF();
		//����ƶ��ı߽�
		if(dy>canMove){//�����ƶ�
			if(rect.top<common){
				if(Math.abs(rect.top)-dy<common){
					dy = -rect.top;
				}
			}else{
				dy=0.0f;
			}
		}else if(dy<canMove){//�����ƶ�
			if(rect.bottom-getHeight()>common){
				if(Math.abs(dy)-rect.bottom>common){
					dy = -(rect.bottom-getHeight());
				}
			}else{
				dy=0.0f;
			}
		}
		if(dx>canMove){//�����ƶ�
			if(rect.left<common){
				if(Math.abs(rect.left)-dx<=common){
					dx = -rect.left;
				}
			}else{
				dx=0.0f;
			}
		}else if(dx<canMove){//�����ƶ�
			if(rect.right-getWidth()>common){
				if(Math.abs(dx)-rect.right>common){
					dx = -(rect.right-getWidth());
				}
			}else{
				dx=0.0f;
			}
		}
		mScaleMatrix.postTranslate(dx, dy);
		//setImageMatrix(mScaleMatrix);
		reSet();
	}

}
