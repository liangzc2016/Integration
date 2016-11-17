package com.example.test22.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.example.test22.R;
import com.example.test22.drawable.CircleImageDrawable;
import com.example.test22.view.ColorTrackView;

public class ColorTrackViewActivity extends Activity {
	private ColorTrackView view1,view2,view3;
	private ImageView iv_circle;
	int progress=10;
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			view1.setmProgress(progress/100f);
			view2.setmProgress(1-progress/100f);
			view3.setmProgress(progress/100f);
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_color_track);
		initView();
		initCircle();
	}
	private void initCircle() {
		iv_circle = (ImageView)findViewById(R.id.iv_circle);
		Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.large);
		iv_circle.setImageDrawable(new CircleImageDrawable(mBitmap));
	}
	@SuppressLint("NewApi")
	private void initView() {
		view1 = (ColorTrackView)findViewById(R.id.first);
		view2 = (ColorTrackView)findViewById(R.id.second);
		view3 = (ColorTrackView)findViewById(R.id.third);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(progress<100){
					try {
						Thread.sleep(1500);
						progress+=10;
						handler.sendEmptyMessage(0);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
		}).start();
		/*ObjectAnimator.ofFloat(view1, "mProgress", 0,1).setDuration(3000).start();
		ObjectAnimator.ofFloat(view2, "mProgress", 0,1).setDuration(3000).start();
		ObjectAnimator.ofFloat(view3, "mProgress", 0,1).setDuration(3000).start();*/
	}


}
