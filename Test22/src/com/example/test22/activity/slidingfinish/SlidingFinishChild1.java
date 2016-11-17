package com.example.test22.activity.slidingfinish;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import com.example.test22.R;
import com.example.test22.viewgroup.SlidingFinishLayout;
import com.example.test22.viewgroup.SlidingFinishLayout.OnSildingFinishListener;

/**
 * @author zc
 *要对Activity设置为透明，即设置主题android:theme="@android:style/Theme.Translucent" 
 */
public class SlidingFinishChild1 extends Activity {
	private LinearLayout ll1;
	SlidingFinishLayout mSildingFinishLayout;
	int type=0;
	private ScrollView scrollView;
	private List<String> list = new ArrayList<String>();
	private ListView lv1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_slidingchild1);
		type = getIntent().getIntExtra("type", 0);
		initView();
	}
	private void initView(){
		mSildingFinishLayout = (SlidingFinishLayout) findViewById(R.id.sildingFinishLayout);
		ll1 = (LinearLayout)findViewById(R.id.ll1);
		scrollView = (ScrollView)findViewById(R.id.scrollView);
		lv1 = (ListView)findViewById(R.id.lv1);
		if(type==0){
			mSildingFinishLayout.setTouchView(ll1);
			scrollView.setVisibility(View.GONE);
		}
		if(type==1){
			ll1.setVisibility(View.GONE);
			scrollView.setVisibility(View.VISIBLE);
			mSildingFinishLayout.setTouchView(scrollView);
		}
		if(type==2){
			ll1.setVisibility(View.GONE);
			lv1.setVisibility(View.VISIBLE);
			mSildingFinishLayout.setTouchView(lv1);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					SlidingFinishChild1.this, android.R.layout.simple_list_item_1, list){

						@Override
						public int getCount() {
							return super.getCount();
						}

						@Override
						public View getView(int position, View convertView,
								ViewGroup parent) {
							
							System.out.println("getView");
							return super.getView(position, convertView, parent);
						}
				
			};
			lv1.setAdapter(adapter);
			
			for (int i = 0; i <= 30; i++) {
				list.add("测试数据" + i);
			}
		}
		mSildingFinishLayout.setOnSlidingFinishListener(new OnSildingFinishListener(){
	    	 @Override
	    	public void onSlidingFinish() {
	    		 finish();
	    	}
	     });
	}
	
}
