package com.example.test22.view;

import java.io.IOException;
import java.io.InputStream;

import com.example.test22.R;

import android.app.Activity;
import android.os.Bundle;

public class Test_LargeImageView extends Activity {
	LargImageView iv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_large_image_view);
		iv = (LargImageView)findViewById(R.id.iv1);
		try {
			InputStream is = getAssets().open("large.jpg");
			iv.setInputStream(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
