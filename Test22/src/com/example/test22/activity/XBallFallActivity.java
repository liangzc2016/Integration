package com.example.test22.activity;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;

import com.example.test22.R;

public class XBallFallActivity extends Activity {
	private LinearLayout ll_content;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xball);
		ll_content = (LinearLayout)findViewById(R.id.ll_content);
		ll_content.addView(new XBallView(this));
	}
	/*
	 * 自定义动画组件XBallView
	 */
	public class XBallView extends View implements AnimatorUpdateListener{
		private float BallSize = 100f;
		private float FULLTIME = 1000f;
		public final ArrayList<XShapeHolder> shapeHolders = new ArrayList<XShapeHolder>();
		

		public XBallView(Context context) {
			super(context);
			setBackgroundColor(Color.WHITE);
		}
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			//屏蔽action_up
			if(event.getAction()==MotionEvent.ACTION_UP){
				return false;
			}
			//在Action_Down事件发生点生成小球
			XShapeHolder newBall = addBall(event.getX(), event.getY());
			//计算小球下落动画开始坐标
			float startY = newBall.getY();
			//计算小球下落结束时Y的坐标,即View高度减去小球的高度
			float endY = getHeight()-BallSize;
			//获取View的高度
			float h = getHeight();
			float eventY = event.getY();
			//计算动画的持续时间
			int duration = (int)(FULLTIME*(h-eventY)/h);
			/*
			 * 定义小球的下落，着地压扁，反弹等动画
			 */
			//定义小球下落动画
			ValueAnimator fallAni = ObjectAnimator.ofFloat(newBall, "y", startY,endY);
			//设置持续时间
			fallAni.setDuration(duration);
			//设置加速插值器
			fallAni.setInterpolator(new AccelerateDecelerateInterpolator());
			//添加addUpdateListener监听器，当ValueAnimator属性值改变时会触发事件监听方法
			fallAni.addUpdateListener(this);
			//定义小球压扁动画，控制小球x坐标左移半个宽度
            ValueAnimator squashAni1 = ObjectAnimator.ofFloat(newBall, "x", newBall.getX(),newBall.getX()-BallSize/2);
            squashAni1.setDuration(duration/4);
            squashAni1.setRepeatCount(1);
            squashAni1.setRepeatMode(ValueAnimator.REVERSE);
            squashAni1.setInterpolator(new AccelerateDecelerateInterpolator());
            squashAni1.addUpdateListener(this);
            //定义小球压扁动画，控制小球宽度加倍
            int w = (int) newBall.getWidth();
            ValueAnimator squashAni2 = ObjectAnimator.ofFloat(newBall, "width", newBall.getWidth(),newBall.getWidth()+BallSize);
            squashAni2.setDuration(duration/4);
            squashAni2.setRepeatCount(1);
            squashAni2.setRepeatMode(ValueAnimator.REVERSE);
            squashAni2.setInterpolator(new AccelerateDecelerateInterpolator());
            squashAni2.addUpdateListener(this);
            //定义小球拉伸动画，控制小球的y坐标下移半个高度
            ValueAnimator stretchAni1 = ObjectAnimator.ofFloat(newBall, "y", endY,endY+BallSize/2);
            stretchAni1.setDuration(duration/4);
            stretchAni1.setRepeatCount(1);
            stretchAni1.setRepeatMode(ValueAnimator.REVERSE);
            stretchAni1.setInterpolator(new AccelerateDecelerateInterpolator());
            stretchAni1.addUpdateListener(this);
            //定义小球拉伸动画，控制小球的高度减半
            ValueAnimator strectAni2 = ObjectAnimator.ofFloat(newBall, "height", newBall.getHeight(),newBall.getHeight()-BallSize/2);
            strectAni2.setDuration(duration/4);
            strectAni2.setRepeatCount(1);
            strectAni2.setRepeatMode(ValueAnimator.REVERSE);
            strectAni2.setInterpolator(new AccelerateDecelerateInterpolator());
            strectAni2.addUpdateListener(this);
            //定义小球弹起动画
            ValueAnimator bounceAni = ObjectAnimator.ofFloat(newBall, "y", endY,startY);
            bounceAni.setDuration(duration);
            bounceAni.setInterpolator(new AccelerateDecelerateInterpolator());
            bounceAni.addUpdateListener(this);
         // 定义AnimatorSet，按顺序播放[下落、压扁&拉伸、弹起]动画
            AnimatorSet set = new AnimatorSet();
            //在squashAni1之前播放fallAni
            set.play(fallAni).before(squashAni1);
            /**
             * 由于小球弹起时压扁，即宽度加倍，x坐标左移，高度减半，y坐标下移
             * 因此播放squashshAni1的同时还要播放squashshAni2，stretchAni1，stretchAni2
             */
            set.play(squashAni1).with(squashAni2);
            set.play(squashAni1).with(stretchAni1);
            set.play(squashAni1).with(strectAni2);
            //播放strectAni2之后播放bounceAni
            set.play(bounceAni).after(strectAni2);
            //newball对象的渐隐动画
            ObjectAnimator fadeAni = ObjectAnimator.ofFloat(newBall, "alpha", 1f,0f);
            fadeAni.setDuration(200);
            fadeAni.addUpdateListener(this);
            fadeAni.addListener(new AnimatorListenerAdapter() {
            	@Override
            	public void onAnimationEnd(Animator animation) {
            		shapeHolders.remove(((ObjectAnimator) (animation)).getTarget());
            	}
			});
            //再次定义一个AnimatorSet
            AnimatorSet aniSet = new AnimatorSet();
            aniSet.play(set).before(fadeAni);
            aniSet.start();
			return true;
		}
		@Override
		protected void onDraw(Canvas canvas) {
			for(XShapeHolder holder:shapeHolders){
				canvas.save();
				canvas.translate(holder.getX(), holder.getY());
				holder.getShapeDrawable().draw(canvas);
				canvas.restore();
			}
		}
		private XShapeHolder addBall(float x,float y){
			//创建一个椭圆
			OvalShape circle = new OvalShape();
			//设置椭圆的宽高
			circle.resize(BallSize, BallSize);
			//把椭圆包装成drawable对象
			ShapeDrawable drawable = new ShapeDrawable(circle);
			//创建XShapeHolder 对象
			XShapeHolder holder = new XShapeHolder(drawable);
			//设置holder坐标
			holder.setX(x-BallSize/2);
			holder.setY(y-BallSize/2);
			//生产随机组合的ARGB颜色
			int red = (int)(Math.random()*255);
			int green = (int)(Math.random()*255);
			int blue = (int)(Math.random()*255);
			//把red、green、blue、三个颜色随机数组合成rgb颜色
			int color = 0xff000000+red<<16|green<<8|blue;
			// 把red，green，blue三个颜色随机数除以4得到商值组合成ARGB颜色
            int darkColor = 0xff000000 | red / 4 << 16 | green / 4 << 8 | blue / 4;
            //创建圆形的渐变效果
            RadialGradient gradient = new RadialGradient(37.5f, 12.5f, BallSize, color, darkColor, Shader.TileMode.CLAMP);
            //获取drawable关联的画笔
            Paint paint = drawable.getPaint();
            paint.setShader(gradient);
            //为XShapeHolder对象设置画笔
            holder.setPaint(paint);
            shapeHolders.add(holder);
            
			return holder;
		}
		@Override
		public void onAnimationUpdate(ValueAnimator animation) {
			//重绘界面
			this.invalidate();
		}
	}
	public class XShapeHolder{
		private float x = 0,y=0;
		private ShapeDrawable shapeDrawable;
		private int color;
		private RadialGradient gradient;
		private float alpha = 1f;
		private Paint paint;
		public XShapeHolder(ShapeDrawable shapeDrawable){
			this.shapeDrawable = shapeDrawable;
		}
		public void setWidth(float width){
			Shape s = shapeDrawable.getShape();
			s.resize(width, s.getHeight());
		}
		public float getWidth(){
			return shapeDrawable.getShape().getWidth();
		}
		public float getHeight(){
			return shapeDrawable.getShape().getHeight();
		}
		public void setHeight(float height){
			Shape s = shapeDrawable.getShape();
			s.resize(s.getWidth(), height);
		}
		public float getX(){
			return x;
		}
		public void setX(float x){
			this.x = x;
		}
		public float getY(){
			return y;
		}
		public void setY(float y){
			this.y = y;
		}
		public ShapeDrawable getShapeDrawable() {
			return shapeDrawable;
		}
		public void setShapeDrawable(ShapeDrawable shapeDrawable) {
			this.shapeDrawable = shapeDrawable;
		}
		public int getColor() {
			return color;
		}
		public void setColor(int color) {
			this.color = color;
		}
		public RadialGradient getGradient() {
			return gradient;
		}
		public void setGradient(RadialGradient gradient) {
			this.gradient = gradient;
		}
		public float getAlpha() {
			return alpha;
		}
		public void setAlpha(float alpha) {
			this.alpha = alpha;
		}
		public Paint getPaint() {
			return paint;
		}
		public void setPaint(Paint paint) {
			this.paint = paint;
		}
		
	}

}
