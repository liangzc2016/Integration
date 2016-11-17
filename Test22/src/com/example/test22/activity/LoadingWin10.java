package com.example.test22.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.test22.R;

public class LoadingWin10 extends Activity implements OnClickListener {
	private Button bt_start;
	private LinearLayout ll1, ll2, ll3, ll4;
	private boolean isStart = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_loading_win10);
		super.onCreate(savedInstanceState);
		initView();
	}

	private void initView() {
		bt_start = (Button) findViewById(R.id.bt_start);
		ll1 = (LinearLayout) findViewById(R.id.ll1);
		ll2 = (LinearLayout) findViewById(R.id.ll2);
		ll3 = (LinearLayout) findViewById(R.id.ll3);
		ll4 = (LinearLayout) findViewById(R.id.ll4);
		bt_start.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_start:
			if(isStart){
				isStart = false;
				bt_start.setText("¿ªÊ¼");
			}else{
				loading();
				isStart = true;
				bt_start.setText("Í£Ö¹");
			}
			
			break;
		}
	}
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(isStart){
				loading();
			}
		};
	};
	private void loading() {
		ObjectAnimator oa1 = ObjectAnimator.ofFloat(ll1, "rotation", 0, 360);
		oa1.setDuration(1500);
		oa1.setInterpolator(new AccelerateDecelerateInterpolator());
//		oa1.setRepeatCount(5);
		// oa1.setRepeatMode(ObjectAnimator.REVERSE);

		ObjectAnimator oa2 = ObjectAnimator.ofFloat(ll2, "rotation", 0, 360);
		oa2.setDuration(1500+150*1);
		oa2.setStartDelay(150);
		oa2.setInterpolator(new AccelerateDecelerateInterpolator());
//		oa2.setRepeatCount(5);

		ObjectAnimator oa3 = ObjectAnimator.ofFloat(ll3, "rotation", 0, 360);
		oa3.setDuration(1500 + 150 *2);
		oa3.setStartDelay(150 * 2);
		oa3.setInterpolator(new AccelerateDecelerateInterpolator());
//		oa3.setRepeatCount(5);

		ObjectAnimator oa4 = ObjectAnimator.ofFloat(ll4, "rotation", 0, 360);
		oa4.setDuration(1500 + 150 * 3);
		oa4.setStartDelay(150 * 3);
//		oa4.setRepeatCount(5);
		oa4.setInterpolator(new AccelerateDecelerateInterpolator());

		AnimatorSet as = new AnimatorSet();
		as.play(oa1).with(oa2).with(oa3).with(oa4);
		as.start();
		handler.sendMessageDelayed(new Message(), 1500+150*3+150*3);
		

	}
}
