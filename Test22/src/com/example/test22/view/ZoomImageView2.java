package com.example.test22.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;

/**
 * @author zc
 *图像的的缩放，和图像的截取
 */
public class ZoomImageView2 extends ImageView implements OnScaleGestureListener, OnTouchListener, OnGlobalLayoutListener {
	private int mHorizonpadding = 20;
	private int mVerticalpadding;
	private int mWidth;//正方形边框的长度
	public static float SCALE_MAX=4.0f;
	public static float SCLAE_MID = 2.0f;
	/**
	 * 初始化时的缩放比例，如果图片宽或高大于屏幕，此值将小于1
	 */
	public float initScale=1.0f;
	/**
	 * 用于存放矩阵的9个值
	 */
	private final float[] matrixValues = new float[9];
	private boolean once = true;
	/**
	 * 缩放手势的检测
	 */
	private ScaleGestureDetector mScaleGestureDetector;
	private final Matrix mScaleMatrix = new Matrix();
	RectF rect = new RectF();
	private int mTouchSlop;
	GestureDetector mGestureDetector;
	private boolean isAutoScale = false;
	
	public ZoomImageView2(Context context) {
		this(context,null);
	}
	public ZoomImageView2(Context context, AttributeSet attrs) {
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
					ZoomImageView2.this.postDelayed(new AutoScaleRunnable(SCLAE_MID, x, y), 16);
					isAutoScale = true;
				}else if(getScale()>=SCLAE_MID&&getScale()<SCALE_MAX){
					ZoomImageView2.this.postDelayed(new AutoScaleRunnable(SCALE_MAX, x, y), 16);
					isAutoScale = true;
				}else if(getScale()==SCALE_MAX){
					ZoomImageView2.this.postDelayed(new AutoScaleRunnable(initScale, x, y), 16);
					isAutoScale = true;
				}
				return true;
			}
		});
		//mTouchSlop = 48;
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		mHorizonpadding = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mHorizonpadding, getResources().getDisplayMetrics());
		this.setOnTouchListener(this);
	}
	/**
	 * @author zc
	 *自动缩放的任务
	 */
	private class AutoScaleRunnable implements Runnable{
		static final float BIGGER = 1.07f;
		static final float SMALLER = 0.93f;
		private float mTargetScale;
		private float tmpScale;
		//缩放的中心
		private float x;
		private float y;
		/**传入目标值，根据目标值与当前值，判断应该放大还是缩小
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
			//进行缩放
			mScaleMatrix.postScale(tmpScale, tmpScale, x, y);
//			checkBorderAndCenter();
			checkBorder();
			setImageMatrix(mScaleMatrix);
			final float currentScale=getScale();
			//如果值在合理的范围内，继续缩放
			if(((tmpScale>1f)&&(currentScale<mTargetScale))||
					((tmpScale<1f)&&(currentScale>mTargetScale))){
				ZoomImageView2.this.postDelayed(new AutoScaleRunnable(mTargetScale,x,y), 16);
			}else{//设置为目标的缩放比例，因为可能会放得过大，或者过小
				final float deltaScale=mTargetScale/currentScale;
				mScaleMatrix.postScale(deltaScale, deltaScale, x, y);
//				checkBorderAndCenter();
				checkBorder();
				setImageMatrix(mScaleMatrix);
				isAutoScale=false;
			}
		}
		
	}
	@Override
	public boolean onScale(ScaleGestureDetector detector) {
	    float scale = getScale();//上一次的缩放的值，它的值等于scale*scaleFactor
	    Log.e("getScale==", ""+scale);
	    float scaleFactor = detector.getScaleFactor();//要缩放的比例
	    Log.e("scaleFactor1==",""+scaleFactor);
	    if(getDrawable()==null)return true;
	    //缩放范围的控制，分别是放大和缩小的条件判断
	    if((scale<SCALE_MAX&&scaleFactor>1.0f)||(scale>initScale&&scaleFactor<1.0f)){
	    	//新的scale=scaleFactor*scale；
	    	Log.e("scaleFactor*scale", scaleFactor*scale+"");
	    	//最小值、最大值判断
	    	if(scaleFactor*scale<initScale){
	    		scaleFactor = initScale/scale;
	    		Log.e("initScale/scale",""+scaleFactor);
	    	}
	    	if(scaleFactor*scale>SCALE_MAX){
	    		scaleFactor = SCALE_MAX/scale;
	    		Log.e("SCALE_MAX/scale",""+scaleFactor);
	    	}
	    	Log.e("scaleFactor2==",""+scaleFactor);
	    	//设置缩放比例
//	    	mScaleMatrix.postScale(scaleFactor, scaleFactor, getWidth()/2, getHeight()/2);//居中缩放
	    	mScaleMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());//任意位置缩放
//	    	checkBorderAndCenter();//检查边界
	    	checkBorder();
	    	setImageMatrix(mScaleMatrix);
	    }
		return true;
	}
	@Override
	public boolean onScaleBegin(ScaleGestureDetector arg0) {
		return true;
	}
	@Override
	public void onScaleEnd(ScaleGestureDetector arg0) {
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
		//拿到触摸点的个数
		final int pointerCount = event.getPointerCount();
		//得到多个触摸点的x与y 的平均值
		for(int i=0;i<pointerCount;i++){
			x+=event.getX(i);
			y+=event.getY(i);
		}
		x = x/pointerCount;
		y = y/pointerCount;
		Log.e("x/pointerCount、、y/pointerCount", ""+x/pointerCount+" "+y/pointerCount);
		//每当触摸点发生变化时，重置mlastx,mlasty
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
		return Math.sqrt((dx*dx)+(dy*dy))>=mTouchSlop/2;
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
		/*如果图片的宽或者高只要一个小于我们的正方形的边长，我们会直接把较小的尺寸放大至正方形的边长；
		如果图片的宽和高都大于我们的正方形的边长，我们仅仅把图片移动到我们屏幕的中央，不做缩放处理；*/
		if(once){
			Drawable d = getDrawable();
			if(d==null)return;
			Log.e("图片宽和高", ""+d.getIntrinsicWidth()+" "+d.getIntrinsicHeight());
			int width = getWidth();
			int height = getHeight();
			//正方形的边长
			mWidth = width-2*mHorizonpadding;
			mVerticalpadding = (height-mWidth)/2;
			//拿到图片的宽和高
			int dw = d.getIntrinsicWidth();
			int dh = d.getIntrinsicHeight();
			float scale = 1.0f;
			/*//如果图片的宽或高大于屏幕的宽或高，则缩放至屏幕的宽或高
			if(dw>width&&dh<=height){
				scale = 1.0f*width/dw;
				Log.e("宽大，scale==", ""+scale);
			}
			if(dh>height&&dw<=width){
				scale = 1.0f*height/dh;
			}
			//如果宽和高都大于屏幕，则让其按比例适应屏幕大小
			if(dh>height&&dw>width){
				scale = Math.min(1.0f*width/dw, 1.0f*height/dh);
			}*/
			//如果图片的宽小于截取的正方形的边长，则把图片放大
			if(dw<mWidth&&dh>=mWidth){
				scale = mWidth*1.0f/dw;
			}
			//如果图片的高小于截取的正方形的边长，把图片放大,并取最大的放大值
			if(dh<mWidth&&dw>=mWidth){
				scale = mWidth*1.0f/dh;
			}
			//宽和高都小于边长
			if(dh<mWidth&&dw<mWidth){
				scale=Math.max(mWidth*1.0f/dh, scale);
			}
			initScale = scale;
			SCALE_MAX = initScale*4;
			SCLAE_MID = initScale*2;
			//图片平移动至屏幕中心，参数为移动的距离
			mScaleMatrix.postTranslate((width-dw)/2, (height-dh)/2);
			Log.e("(width-dw)/2、(height-dh)/2", ""+(width-dw)/2+" "+(height-dh)/2);
			Log.e("父控件的宽和高",""+width+" "+height);
			mScaleMatrix.postScale(scale, scale, getWidth()/2, getHeight()/2);
			setImageMatrix(mScaleMatrix);
			once = false;
		}
	}
	/**剪切图片，返回剪切后的bitmap对象
	 * @return
	 */
	public Bitmap clip(){
		Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		draw(canvas);
		return Bitmap.createBitmap(bitmap, mHorizonpadding, mVerticalpadding, mWidth, mWidth);
	}
	/**
	 * 边界检测，以正方形边框为标准
	 */
	private void checkBorder(){
		RectF rect = getMatrixRectF();
		float deltaX = 0;
		float deltaY = 0;
		if(rect.width()>=mWidth){
			if(rect.left>mHorizonpadding){
				deltaX = -rect.left+mHorizonpadding;
			}
			if(rect.right<mWidth+mHorizonpadding){
				deltaX = mWidth+mHorizonpadding-rect.right;
			}
		}
		if(rect.height()>=mWidth){
			if(rect.top>mVerticalpadding){
				deltaY = mVerticalpadding-rect.top;
			}
			if(rect.bottom<mWidth+mVerticalpadding){
				deltaY = mWidth+mVerticalpadding-rect.bottom;
			}
		}
		Log.e("rect.width、rect.height", ""+rect.width()+" "+rect.height());
		Log.e("deltaX、deltaY", ""+deltaX+" "+deltaY);
		mScaleMatrix.postTranslate(deltaX, deltaY);
	}
	/**
	 * 在缩放时候，进行图片显示范围控制
	 */
	private void checkBorderAndCenter(){
		RectF rect = getMatrixRectF();
		float deltaX = 0;
		float deltaY = 0;
		int width = getWidth();
		int height = getHeight();
		//如果宽或高大于屏幕，则控制范围
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
		//如果宽或高小于屏幕，则让其居中
		if(rect.width()<width){
			deltaX = width*0.5f-(rect.right-0.5f*rect.width());
		}
		if(rect.height()<height){
			deltaY = height*0.5f-(rect.bottom-0.5f*rect.height());
		}
		Log.e("rect.width、rect.height", ""+rect.width()+" "+rect.height());
		Log.e("deltaX、deltaY", ""+deltaX+" "+deltaY);
		mScaleMatrix.postTranslate(deltaX, deltaY);
	}
	/**根据当前图片的Matrix 获得图片的范围
	 * @return
	 */
	private RectF getMatrixRectF() {
		Matrix matrix =mScaleMatrix;
		Drawable d = getDrawable();
		if(d!=null){
			rect.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
			matrix.mapRect(rect);
		}
		Log.e("rect.left、rect.right", rect.left+" "+rect.right);
		Log.e("rect.top、rect.bottom", rect.top+" "+rect.bottom);
		return rect;
	}
	float common=0.001f;
	float canMove=0.001f;
	/**
	 * 快速滑动的时候，右边和下边都会出现空白，这时候要恢复
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
		getMatrixRectF();
		//检测移动的边界
		if(dy>canMove){//向下移动
			if(rect.top<mVerticalpadding){
				if(rect.top+dy>mVerticalpadding){
					dy = mVerticalpadding-rect.top;
				}
			}else{
				dy=0.0f;
			}
		}else if(dy<canMove){//向上移动
			if(rect.bottom>(mWidth+mVerticalpadding)){
				if(rect.bottom+dy<(mWidth+mVerticalpadding)){
					dy = -(rect.bottom-(mWidth+mVerticalpadding));
				}
			}else{
				dy=0.0f;
			}
		}
		if(dx>canMove){//向右移动
			if(rect.left<mHorizonpadding){
				if(rect.left+dx>mHorizonpadding){
					dx = mHorizonpadding-rect.left;
				}
			}else{
				dx=0.0f;
			}
		}else if(dx<canMove){//向左移动
			if(rect.right>mHorizonpadding+mWidth){
				if(rect.right+dx<mHorizonpadding+mWidth){
					dx = -(rect.right-(mHorizonpadding+mWidth));
				}
			}else{
				dx=0.0f;
			}
		}
		mScaleMatrix.postTranslate(dx, dy);
		setImageMatrix(mScaleMatrix);
//		reSet();
	}
	public int getmHorizonpadding() {
		return mHorizonpadding;
	}
	public void setmHorizonpadding(int mHorizonpadding) {
		this.mHorizonpadding = mHorizonpadding;
		invalidate();
	}

}
