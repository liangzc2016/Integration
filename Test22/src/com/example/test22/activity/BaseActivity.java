package com.example.test22.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

import com.example.test22.R;
import com.example.test22.viewgroup.SlidingFinishLayout;
import com.example.test22.viewgroup.SlidingFinishLayout.OnSildingFinishListener;

/**
 * @author zc
 *要对Activity设置为透明，即设置主题android:theme="@android:style/Theme.Translucent" 
 */
public class BaseActivity extends Activity {
	SlidingFinishLayout mSildingFinishLayout;
	protected FrameLayout fl_content;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_common);
		mSildingFinishLayout = (SlidingFinishLayout)findViewById(R.id.sildingFinishLayout);
		fl_content = (FrameLayout)findViewById(R.id.fl_content);
		mSildingFinishLayout.setTouchView(fl_content);
		mSildingFinishLayout.setOnSlidingFinishListener(new OnSildingFinishListener(){
	    	 @Override
	    	public void onSlidingFinish() {
	    		 finish();
	    	}
	     });
	}
	public void setContentLayout(int layoutId){
		View v = LayoutInflater.from(this).inflate(layoutId, null);
		fl_content.addView(v);
	}
}
