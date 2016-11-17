package com.example.test22.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;

import com.example.test22.R;
import com.example.test22.view.CosAndCircle;
import com.example.test22.view.PercentCircle;
import com.example.test22.view.PercentCircle.CircleBuilder;


/**
 * @author zc
 *Öð½¥ÏÔÊ¾view»òÒþ²Øview
 */
public class LittleByLittleAct extends BaseActivity implements OnClickListener {
	Button bt_start,bt_test;
	boolean isOpen = true;
	int width=0,height=0;
	CosAndCircle cc;
	float percent=0;
	PercentCircle percentCircle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_littlebylittle);
		bt_start = (Button)fl_content.findViewById(R.id.bt_start);
		bt_start.setOnClickListener(this);
		cc = (CosAndCircle) fl_content.findViewById(R.id.cc);
		bt_test = (Button)fl_content.findViewById(R.id.bt_test);
		bt_test.setOnClickListener(this);
		percent = 0.56f;
		cc.setmPercent(percent);
		percentCircle = (PercentCircle)fl_content.findViewById(R.id.percentCircle);
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.bt_start:
			if(!isOpen){
				openView();
			}else{
				closeView();
			}
			break;
		case R.id.bt_test:
			test();
			break;
		}
	}
	public void openView(){
		fl_content.setVisibility(View.VISIBLE);
		isOpen = true;
		Animation animation = new Animation(){
			@Override
			protected void applyTransformation(float interpolatedTime,
					Transformation t) {
				if(interpolatedTime==1){
					fl_content.getLayoutParams().height = height;
				}else{
					fl_content.getLayoutParams().height = (int)(interpolatedTime*height);
				}
				fl_content.requestLayout();
			}
			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};
		animation.setDuration(5000);
		fl_content.startAnimation(animation);
	}
	public void closeView(){
		width = fl_content.getMeasuredWidth();
		height=fl_content.getHeight();
		isOpen = false;
		Animation animation = new Animation(){
			@Override
			protected void applyTransformation(float interpolatedTime,
					Transformation t) {
				if(interpolatedTime==1){
					fl_content.setVisibility(View.VISIBLE);
					fl_content.getLayoutParams().height =height;
					fl_content.requestLayout();
				}else{
					fl_content.getLayoutParams().height =height- (int)(interpolatedTime*height);
					fl_content.requestLayout();
				}
				
			}
			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};
		animation.setDuration(5*1000);
		fl_content.startAnimation(animation);
	}
	private void test(){
		Thread thread = new Thread(new Runnable(){

			@Override
			public void run() {
				percent = 0;
				while(percent<=1){
					cc.setmPercent(percent);
					percent+=0.01f;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				percent = 0.5f;
				cc.setmPercent(percent);
			}
			
		});
		thread.start();
	}
	@Override
	protected void onResume() {
		super.onResume();
		CircleBuilder.createBuilder(percentCircle).setUsedAndOverplusNum(250f, 80.34f).show();
//		percentCircle.startAnim();
	}
}
