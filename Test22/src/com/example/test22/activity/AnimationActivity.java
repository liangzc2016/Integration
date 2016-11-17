package com.example.test22.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.example.test22.R;

public class AnimationActivity extends Activity implements OnClickListener {
	private ImageView iv1,iv2,iv3,iv4;
	private Button bt1,bt2,bt3,bt4;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_animation);
		initView();
	}
	private void initView(){
		iv1=(ImageView)findViewById(R.id.iv1);
		iv2=(ImageView)findViewById(R.id.iv2);
		iv3=(ImageView)findViewById(R.id.iv3);
		iv4=(ImageView)findViewById(R.id.iv4);
		bt1=(Button)findViewById(R.id.bt1);
		bt2=(Button)findViewById(R.id.bt2);
		bt3=(Button)findViewById(R.id.bt3);
		bt4=(Button)findViewById(R.id.bt4);
		bt1.setOnClickListener(this);
		bt2.setOnClickListener(this);
		bt3.setOnClickListener(this);
		bt4.setOnClickListener(this);
	}
	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.bt1:
			rotateClick();
			break;
		case R.id.bt2:
			scaleClick();
			break;
		case R.id.bt3:
			alphaClick();
			break;
		case R.id.bt4:
			translateClick();
			break;
		}
	}
	private void alphaClick(){
		Animation animation = AnimationUtils.loadAnimation(AnimationActivity.this, R.anim.alpha);
		iv3.startAnimation(animation);
//		//创建一个AnimationSet对象，参数为true
//		//表示使用animation的interpolator，false则是使用自己的
//		AnimationSet animationSet = new AnimationSet(true);
//		//创建一个AlphaAnimation对象，参数从完全的透明度到完全的不透明度
//		AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.0f);
//		alphaAnimation.setDuration(500);
//		animationSet.addAnimation(alphaAnimation);
////		iv3.setAnimation(animationSet);
//		animationSet.setFillAfter(true);
//		iv3.startAnimation(animationSet);
	}
	private void rotateClick(){
		AnimationSet animationSet = new AnimationSet(true);
		/*
		 * 参数1：从哪个旋转角度开始，2转到什么角度
		 * 后4个参数用于设置围绕旋转的圆的圆心在哪里
		 * 3：确定X轴坐标类型，有Absolut绝对坐标、relate_to_self相对于自身坐标
		 * relate_to_parent相对于父控件的坐标
		 * 4：x轴的值，0.5f表明是以自身这个控件的一半长度为X轴
		 * 5：确定y轴坐标的类型
		 * 6：y轴的值，0.5f表明是以自身这个控件的一半长度为Y轴
		 */
		RotateAnimation rotateAnimation = new RotateAnimation(0,360,
				Animation.RELATIVE_TO_SELF,0.5f,
				Animation.RELATIVE_TO_SELF,0.5f);
		rotateAnimation.setDuration(1000);
		animationSet.addAnimation(rotateAnimation);
		iv1.startAnimation(animationSet);
	}
	private void scaleClick(){
		AnimationSet animationSet = new AnimationSet(true);
		/*
		 * 参数1:x轴的初始值
		 * 2:x轴的收缩后的值
		 * 3：y轴的初始值
		 * 4、Y轴收缩后的值
		 * 5、确定X轴的坐标类型
		 * 6、X轴的值，0.5f表明以自身这个控件的一半长度为X轴
		 * 7、y轴的类型
		 * 8、y轴的值，0.5f表明以自身这个控件的一半长度为Y轴
		 */
		ScaleAnimation scaleAnimation = new ScaleAnimation(
				0,2f,0,2f,Animation.RELATIVE_TO_SELF,0.5f
				,Animation.RELATIVE_TO_SELF,0.5f);
		scaleAnimation.setDuration(1000);
		animationSet.setFillAfter(true);
		animationSet.addAnimation(scaleAnimation);
		iv2.startAnimation(animationSet);
	}
	private void translateClick(){
		AnimationSet animationSet = new AnimationSet(true);
		/*
		 * 参数1-2：x轴的开始位置
		 * 参数3-4：X轴的结束位置
		 * 参数5-6：y轴的开始位置
		 * 参数7-8：y轴的结束位置
		 */
	    TranslateAnimation translateAnimation = new TranslateAnimation(
	    		Animation.RELATIVE_TO_SELF,0f,
	    		Animation.RELATIVE_TO_SELF,2f,
	    		Animation.RELATIVE_TO_SELF,0f,
	    		Animation.RELATIVE_TO_SELF,2f);
	    translateAnimation.setDuration(1000);
//	    translateAnimation.setFillAfter(true);
	    animationSet.setFillAfter(true);
	    animationSet.addAnimation(translateAnimation);
	    iv4.startAnimation(animationSet);
	}
}
