package com.example.test22.suspendsion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.test22.R;
import com.example.test22.activity.BaseActivity;

/**
 * @author zc
 *Ðü¸¡´°
 */
public class SuspendActivity extends BaseActivity implements OnClickListener {
	Button bt_start;
	Button bt_finish;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_suspension);
		bt_start = (Button)fl_content.findViewById(R.id.bt_start);
		bt_finish = (Button)fl_content.findViewById(R.id.bt_finish);
		bt_start.setOnClickListener(this);
		bt_finish.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.bt_start:
			Intent intent = new Intent(SuspendActivity.this,FloatWindowService.class);
			startService(intent);
//			MyWindowManager.createSmallWindow(getApplicationContext());
			finish();
			break;
		case R.id.bt_finish:
			Intent intent2 = new Intent(this,FloatWindowService.class);
			startService(intent2);
			finish();
//			MyWindowManager.removeSmallWindow(getApplicationContext());
			break;
		}
		
	}
}
