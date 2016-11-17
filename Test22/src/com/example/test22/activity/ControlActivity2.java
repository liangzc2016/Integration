package com.example.test22.activity;

import com.example.test22.R;
import com.example.test22.activity.cache.PhotoWallActivity;
import com.example.test22.activity.slidingfinish.SlidingFinishActiivty;
import com.example.test22.suspendsion.SuspendActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ControlActivity2 extends Activity implements OnClickListener {
	private Button bt1,bt2,bt3,bt4,bt5,bt6,bt7,bt8,bt9,bt10,bt11,bt12,bt13,bt14,bt15,bt16;
	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_control2);
		super.onCreate(savedInstanceState);
		initView();
	}
	private void initView(){
		bt1 = (Button)findViewById(R.id.bt1);
		bt1.setOnClickListener(this);
		bt1.setText("仿win10加载动画");
		bt2 = (Button)findViewById(R.id.bt2);
		bt2.setOnClickListener(this);
		bt2.setText("桌面小球");
		bt3 = (Button)findViewById(R.id.bt3);
		bt3.setOnClickListener(this);
		bt3.setText("侧滑删除activity");
		bt4 = (Button)findViewById(R.id.bt4);
		bt4.setOnClickListener(this);
		bt4.setText("可横向滑动的listView");
		bt5 = (Button)findViewById(R.id.bt5);
		bt5.setText("内容的渐现");
		bt5.setOnClickListener(this);
		bt6 = (Button)findViewById(R.id.bt6);
		bt6.setText("内存泄漏分析");
		bt6.setOnClickListener(this);
		bt7 = (Button)findViewById(R.id.bt7);
		bt7.setText("悬浮窗");
		bt7.setOnClickListener(this);
		bt8 = (Button)findViewById(R.id.bt8);
		bt8.setText("照片墙(使用缓存)");
		bt8.setOnClickListener(this);
		bt9 = (Button)findViewById(R.id.bt9);
		bt9.setText("双向滑动菜单");
		bt9.setOnClickListener(this);
		bt10 = (Button)findViewById(R.id.bt10);
		bt10.setText("popupWindow");
		bt10.setOnClickListener(this);
		bt11 = (Button)findViewById(R.id.bt11);
		bt11.setText("侧滑删除");
		bt11.setOnClickListener(this);
		bt12 = (Button)findViewById(R.id.bt12);
		bt12.setText("动画");
		bt12.setOnClickListener(this);
		
		bt13 = (Button)findViewById(R.id.bt13);
		bt13.setText("第二个页面");
		bt13.setOnClickListener(this);
		
		bt14 = (Button)findViewById(R.id.bt14);
		bt14.setText("");
		bt14.setOnClickListener(this);
		
		bt15 = (Button)findViewById(R.id.bt15);
		bt15.setText("");
		bt15.setOnClickListener(this);
		
		bt16 = (Button)findViewById(R.id.bt16);
		bt16.setText("");
		bt16.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.bt1:
			Intent i1 = new Intent(this,LoadingWin10.class);
			startActivity(i1);
			break;
		case R.id.bt2:
			Intent i2 = new Intent(this,XBallFallActivity.class);
			startActivity(i2);
			break;
		case R.id.bt3:
			Intent i3 = new Intent(this,SlidingFinishActiivty.class);
			startActivity(i3);
			overridePendingTransition(R.anim.left_in, R.anim.left_out);
			break;
		case R.id.bt4:
			Intent i4 = new Intent(this,ListViewHorizontal.class);
			startActivity(i4);
			break;
		case R.id.bt5:
			Intent i5 = new Intent(this,LittleByLittleAct.class);
			startActivity(i5);
			break;
		case R.id.bt6:
			Intent i6 = new Intent(this,MemoryAnalyActivity.class);
			startActivity(i6);
			break;
		case R.id.bt7:
			Intent i7 = new Intent(this,SuspendActivity.class);
			startActivity(i7);
			break;
		case R.id.bt8:
			Intent i8 = new Intent(this,PhotoWallActivity.class);
			startActivity(i8);
			break;
		case R.id.bt9:
			Intent i9 = new Intent(this,TwomenuActivity.class);
			startActivity(i9);
			break;
		case R.id.bt10:
			Intent i10 = new Intent(this,PopupWindowAct.class);
			startActivity(i10);
			break;
		}
		
	}

}
