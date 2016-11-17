package com.example.test22.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.test22.R;

public class ShowImageActivity extends Activity {
	private ImageView iv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_show_image);
		iv = (ImageView)findViewById(R.id.iv1);
		try{
		byte[] b = getIntent().getByteArrayExtra("bitmap");
		Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
		if(bitmap!=null){
			iv.setImageBitmap(bitmap);
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		super.onCreate(savedInstanceState);
	}

}
