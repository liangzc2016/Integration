package com.example.test22.suspendsion.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.test22.R;
import com.example.test22.suspendsion.FloatWindowService;
import com.example.test22.suspendsion.MyWindowManager;

public class FloatWindowBigView extends LinearLayout implements OnClickListener {
	public static int viewWidth;
	public static int viewHeight;
	public FloatWindowBigView(Context context) {
		this(context,null);
	}

	public FloatWindowBigView(Context context, AttributeSet attrs) {
	    this(context, attrs,0);
	}

	public FloatWindowBigView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater.from(context).inflate(R.layout.pupsession_big, this);
		View view = findViewById(R.id.ll_big);
		Button bt_close = (Button)findViewById(R.id.bt_close);
		Button bt_back = (Button)findViewById(R.id.bt_back);
		bt_close.setOnClickListener(this);
		bt_back.setOnClickListener(this);
		viewWidth = view.getLayoutParams().width;
		viewHeight = view.getLayoutParams().height;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.bt_close:
			MyWindowManager.removeBigWindow(getContext());
			MyWindowManager.removeSmallWindow(getContext());
			Intent intent = new Intent(getContext(), FloatWindowService.class);  
			getContext().stopService(intent);  
			break;
		case R.id.bt_back:
			MyWindowManager.removeBigWindow(getContext());
			MyWindowManager.createSmallWindow(getContext());
			break;
		}
	}

}
