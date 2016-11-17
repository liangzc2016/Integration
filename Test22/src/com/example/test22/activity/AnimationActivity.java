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
//		//����һ��AnimationSet���󣬲���Ϊtrue
//		//��ʾʹ��animation��interpolator��false����ʹ���Լ���
//		AnimationSet animationSet = new AnimationSet(true);
//		//����һ��AlphaAnimation���󣬲�������ȫ��͸���ȵ���ȫ�Ĳ�͸����
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
		 * ����1�����ĸ���ת�Ƕȿ�ʼ��2ת��ʲô�Ƕ�
		 * ��4��������������Χ����ת��Բ��Բ��������
		 * 3��ȷ��X���������ͣ���Absolut�������ꡢrelate_to_self�������������
		 * relate_to_parent����ڸ��ؼ�������
		 * 4��x���ֵ��0.5f����������������ؼ���һ�볤��ΪX��
		 * 5��ȷ��y�����������
		 * 6��y���ֵ��0.5f����������������ؼ���һ�볤��ΪY��
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
		 * ����1:x��ĳ�ʼֵ
		 * 2:x����������ֵ
		 * 3��y��ĳ�ʼֵ
		 * 4��Y���������ֵ
		 * 5��ȷ��X�����������
		 * 6��X���ֵ��0.5f��������������ؼ���һ�볤��ΪX��
		 * 7��y�������
		 * 8��y���ֵ��0.5f��������������ؼ���һ�볤��ΪY��
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
		 * ����1-2��x��Ŀ�ʼλ��
		 * ����3-4��X��Ľ���λ��
		 * ����5-6��y��Ŀ�ʼλ��
		 * ����7-8��y��Ľ���λ��
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
