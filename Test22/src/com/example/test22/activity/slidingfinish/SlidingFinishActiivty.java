package com.example.test22.activity.slidingfinish;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewParent;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.test22.MyApplication;
import com.example.test22.R;

/**»¬¶¯É¾³ýactivity
 * @author zc
 *
 */
public class SlidingFinishActiivty extends Activity implements OnClickListener {
	Button bt1,bt2,bt3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_slidingfinish);
		MyApplication.mInstance.addActivity(this);
		initView();
	}
	private void initView(){
		bt1 = (Button)findViewById(R.id.bt1);
		bt2 = (Button)findViewById(R.id.bt2);
		bt3 = (Button)findViewById(R.id.bt3);
		bt1.setOnClickListener(this);
		bt2.setOnClickListener(this);
		bt3.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		Intent i1 = new Intent(this,SlidingFinishChild1.class);
		switch(v.getId()){
		case R.id.bt1:
			i1.putExtra("type", 0);
			break;
		case R.id.bt2:
			i1.putExtra("type", 2);
			break;
		case R.id.bt3:
			i1.putExtra("type", 1);
			break;
		}
		startActivity(i1);
		overridePendingTransition(R.anim.left_in, R.anim.left_out);
		myAnim();
	}
	public void myAnim(){
		TranslateAnimation tl = new TranslateAnimation(Animation.RELATIVE_TO_SELF,-0.3f,
	    		Animation.RELATIVE_TO_SELF,0f,
	    		Animation.RELATIVE_TO_SELF,0f,
	    		Animation.RELATIVE_TO_SELF,0f);
		tl.setDuration(3000);
	    AnimationSet as = new AnimationSet(true);
//	    as.setFillAfter(true);
	    as.addAnimation(tl);
	    View view =  getWindow().getDecorView().findViewById(R.id.ll_content);
	    view.startAnimation(as);
//	    if(view==null)return;
//		ViewParent vp = view.getParent();
//		if(vp==null)return;
//		if(vp instanceof FrameLayout){
//			final FrameLayout fl = (FrameLayout) vp;
//			  fl.startAnimation(as);
//		}
	}
}
