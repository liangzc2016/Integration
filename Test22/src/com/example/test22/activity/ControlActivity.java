package com.example.test22.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;

import com.example.test22.R;
import com.example.test22.view.GuidePage;
import com.example.test22.view.Test_LargeImageView;
import com.example.test22.viewgroup.folderlayout.FolderLayoutActivity;
import com.example.test22.viewgroup.folderlayout.MoveFolderLayoutActivity;
import com.example.test22.viewgroup.slidingmenu.SlidingActivity1;

public class ControlActivity extends BasicActivity implements OnClickListener {
	private Button bt1,bt2,bt3,bt4,bt5,bt6,bt7,bt8,bt9,bt10,bt11,bt12,bt13,bt14,bt15,bt16;
	private Context context;
	private GuidePage guidePage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control);
		context=this;
		initView();
		setImageResource(R.drawable.ic_launcher);
	}
	private void initView() {
		/*guidePage = new GuidePage(context, getWindowManager(), 0, "知道了", 0);
		guidePage.showGuidePage();*/
		bt1 = (Button)findViewById(R.id.bt1);
		bt1.setOnClickListener(this);
		bt2 = (Button)findViewById(R.id.bt2);
		bt2.setOnClickListener(this);
		bt3 = (Button)findViewById(R.id.bt3);
		bt3 .setText("折叠控件");
		bt3.setOnClickListener(this);
		bt4 = (Button)findViewById(R.id.bt4);
		bt4.setText("可移动折叠控件");
		bt4.setOnClickListener(this);
		bt5 = (Button)findViewById(R.id.bt5);
		bt5.setText("文字的颜色变化");
		bt5.setOnClickListener(this);
		bt6 = (Button)findViewById(R.id.bt6);
		bt6.setText("仿360详情页");
		bt6.setOnClickListener(this);
		bt7 = (Button)findViewById(R.id.bt7);
		bt7.setText("侧滑菜单1");
		bt7.setOnClickListener(this);
		bt8 = (Button)findViewById(R.id.bt8);
		bt8.setOnClickListener(this);
		bt9 = (Button)findViewById(R.id.bt9);
		bt9.setText("图片的缩放");
		bt9.setOnClickListener(this);
		bt10 = (Button)findViewById(R.id.bt10);
		bt10.setText("图片的截取");
		bt10.setOnClickListener(this);
		bt11 = (Button)findViewById(R.id.bt11);
		bt11.setText("侧滑删除");
		bt11.setOnClickListener(this);
		bt12 = (Button)findViewById(R.id.bt12);
		bt12.setText("第二个页面");
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
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.bt1:
			Intent intent = new Intent(ControlActivity.this,DragActivity.class);
			startActivity(intent);
			break;
		case R.id.bt2:
			Intent intent2 = new Intent(ControlActivity.this,Test_LargeImageView.class);
			startActivity(intent2);
			break;
		case R.id.bt3:
			Intent intent3 = new Intent(context,FolderLayoutActivity.class);
			startActivity(intent3);
			break;
		case R.id.bt4:
			Intent intent4 = new Intent(context,MoveFolderLayoutActivity.class);
			startActivity(intent4);
			break;
		case R.id.bt5:
			Intent intent5 = new Intent(context,ColorTrackViewActivity.class);
			startActivity(intent5);
			break;
		case R.id.bt6:
			Intent i6 = new Intent(context,StickerLayoutActivity.class);
			startActivity(i6);
			break;
		case R.id.bt7:
			Intent i7 = new Intent(context,SlidingActivity1.class);
			startActivity(i7);
			break;
		case R.id.bt8:
			Intent i8 = new Intent(context,VerticalLinearLayoutActivity.class);
			startActivity(i8);
			break;
		case R.id.bt9:
			Intent i9 = new Intent(context,ZoomImageActivity.class);
			startActivity(i9);
			break;
		case R.id.bt10:
			Intent i10 = new Intent(context,ClipImageActivity.class);
			startActivity(i10);
			break;
		case R.id.bt11:
			Intent i11 = new Intent(context,SlideCancelActivity.class);
			startActivity(i11);
			break;
		case R.id.bt12:
			Intent i12 = new Intent(context,ControlActivity2.class);
			startActivity(i12);
//			Intent i12 = new Intent(context,AnimationActivity.class);
//			startActivity(i12);
			break;
		case R.id.bt13:
			Intent i13 = new Intent(context,ControlActivity2.class);
			startActivity(i13);
			break;
			
		}
	}

}
