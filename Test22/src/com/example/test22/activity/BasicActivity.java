package com.example.test22.activity;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.example.test22.R;

public class BasicActivity extends Activity {
	private int guideResourceId;
	@Override
	protected void onStart() {
		super.onStart();
		addGuideImage();
	}
	private void addGuideImage(){
		View view = getWindow().getDecorView().findViewById(R.id.content_view);
		if(view==null)return;
		ViewParent vp = view.getParent();
		if(vp==null)return;
		if(vp instanceof FrameLayout){
			final FrameLayout fl = (FrameLayout) vp;
			if(guideResourceId!=0){//…Ë÷√“˝µºÕº∆¨
				final ImageView guideImage = new ImageView(BasicActivity.this);
				FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
//				WindowManager.LayoutParams wl = new LayoutParams();
//				wl.alpha = 0.5f;
//				wl.width = LayoutParams.MATCH_PARENT;
//				wl.height = LayoutParams.MATCH_PARENT;
				
				guideImage.setLayoutParams(lp);
				guideImage.setScaleType(ScaleType.FIT_CENTER);
				guideImage.setImageResource(guideResourceId);
				guideImage.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						fl.removeView(guideImage);
					}
				});
				fl.addView(guideImage);
			}
		}
	}
	public void setImageResource(int id){
		this.guideResourceId = id;
	}
}
