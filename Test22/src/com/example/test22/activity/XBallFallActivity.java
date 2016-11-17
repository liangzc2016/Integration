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
	 * �Զ��嶯�����XBallView
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
			//����action_up
			if(event.getAction()==MotionEvent.ACTION_UP){
				return false;
			}
			//��Action_Down�¼�����������С��
			XShapeHolder newBall = addBall(event.getX(), event.getY());
			//����С�����䶯����ʼ����
			float startY = newBall.getY();
			//����С���������ʱY������,��View�߶ȼ�ȥС��ĸ߶�
			float endY = getHeight()-BallSize;
			//��ȡView�ĸ߶�
			float h = getHeight();
			float eventY = event.getY();
			//���㶯���ĳ���ʱ��
			int duration = (int)(FULLTIME*(h-eventY)/h);
			/*
			 * ����С������䣬�ŵ�ѹ�⣬�����ȶ���
			 */
			//����С�����䶯��
			ValueAnimator fallAni = ObjectAnimator.ofFloat(newBall, "y", startY,endY);
			//���ó���ʱ��
			fallAni.setDuration(duration);
			//���ü��ٲ�ֵ��
			fallAni.setInterpolator(new AccelerateDecelerateInterpolator());
			//���addUpdateListener����������ValueAnimator����ֵ�ı�ʱ�ᴥ���¼���������
			fallAni.addUpdateListener(this);
			//����С��ѹ�⶯��������С��x�������ư�����
            ValueAnimator squashAni1 = ObjectAnimator.ofFloat(newBall, "x", newBall.getX(),newBall.getX()-BallSize/2);
            squashAni1.setDuration(duration/4);
            squashAni1.setRepeatCount(1);
            squashAni1.setRepeatMode(ValueAnimator.REVERSE);
            squashAni1.setInterpolator(new AccelerateDecelerateInterpolator());
            squashAni1.addUpdateListener(this);
            //����С��ѹ�⶯��������С���ȼӱ�
            int w = (int) newBall.getWidth();
            ValueAnimator squashAni2 = ObjectAnimator.ofFloat(newBall, "width", newBall.getWidth(),newBall.getWidth()+BallSize);
            squashAni2.setDuration(duration/4);
            squashAni2.setRepeatCount(1);
            squashAni2.setRepeatMode(ValueAnimator.REVERSE);
            squashAni2.setInterpolator(new AccelerateDecelerateInterpolator());
            squashAni2.addUpdateListener(this);
            //����С�����춯��������С���y�������ư���߶�
            ValueAnimator stretchAni1 = ObjectAnimator.ofFloat(newBall, "y", endY,endY+BallSize/2);
            stretchAni1.setDuration(duration/4);
            stretchAni1.setRepeatCount(1);
            stretchAni1.setRepeatMode(ValueAnimator.REVERSE);
            stretchAni1.setInterpolator(new AccelerateDecelerateInterpolator());
            stretchAni1.addUpdateListener(this);
            //����С�����춯��������С��ĸ߶ȼ���
            ValueAnimator strectAni2 = ObjectAnimator.ofFloat(newBall, "height", newBall.getHeight(),newBall.getHeight()-BallSize/2);
            strectAni2.setDuration(duration/4);
            strectAni2.setRepeatCount(1);
            strectAni2.setRepeatMode(ValueAnimator.REVERSE);
            strectAni2.setInterpolator(new AccelerateDecelerateInterpolator());
            strectAni2.addUpdateListener(this);
            //����С���𶯻�
            ValueAnimator bounceAni = ObjectAnimator.ofFloat(newBall, "y", endY,startY);
            bounceAni.setDuration(duration);
            bounceAni.setInterpolator(new AccelerateDecelerateInterpolator());
            bounceAni.addUpdateListener(this);
         // ����AnimatorSet����˳�򲥷�[���䡢ѹ��&���졢����]����
            AnimatorSet set = new AnimatorSet();
            //��squashAni1֮ǰ����fallAni
            set.play(fallAni).before(squashAni1);
            /**
             * ����С����ʱѹ�⣬����ȼӱ���x�������ƣ��߶ȼ��룬y��������
             * ��˲���squashshAni1��ͬʱ��Ҫ����squashshAni2��stretchAni1��stretchAni2
             */
            set.play(squashAni1).with(squashAni2);
            set.play(squashAni1).with(stretchAni1);
            set.play(squashAni1).with(strectAni2);
            //����strectAni2֮�󲥷�bounceAni
            set.play(bounceAni).after(strectAni2);
            //newball����Ľ�������
            ObjectAnimator fadeAni = ObjectAnimator.ofFloat(newBall, "alpha", 1f,0f);
            fadeAni.setDuration(200);
            fadeAni.addUpdateListener(this);
            fadeAni.addListener(new AnimatorListenerAdapter() {
            	@Override
            	public void onAnimationEnd(Animator animation) {
            		shapeHolders.remove(((ObjectAnimator) (animation)).getTarget());
            	}
			});
            //�ٴζ���һ��AnimatorSet
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
			//����һ����Բ
			OvalShape circle = new OvalShape();
			//������Բ�Ŀ��
			circle.resize(BallSize, BallSize);
			//����Բ��װ��drawable����
			ShapeDrawable drawable = new ShapeDrawable(circle);
			//����XShapeHolder ����
			XShapeHolder holder = new XShapeHolder(drawable);
			//����holder����
			holder.setX(x-BallSize/2);
			holder.setY(y-BallSize/2);
			//���������ϵ�ARGB��ɫ
			int red = (int)(Math.random()*255);
			int green = (int)(Math.random()*255);
			int blue = (int)(Math.random()*255);
			//��red��green��blue��������ɫ�������ϳ�rgb��ɫ
			int color = 0xff000000+red<<16|green<<8|blue;
			// ��red��green��blue������ɫ���������4�õ���ֵ��ϳ�ARGB��ɫ
            int darkColor = 0xff000000 | red / 4 << 16 | green / 4 << 8 | blue / 4;
            //����Բ�εĽ���Ч��
            RadialGradient gradient = new RadialGradient(37.5f, 12.5f, BallSize, color, darkColor, Shader.TileMode.CLAMP);
            //��ȡdrawable�����Ļ���
            Paint paint = drawable.getPaint();
            paint.setShader(gradient);
            //ΪXShapeHolder�������û���
            holder.setPaint(paint);
            shapeHolders.add(holder);
            
			return holder;
		}
		@Override
		public void onAnimationUpdate(ValueAnimator animation) {
			//�ػ����
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
