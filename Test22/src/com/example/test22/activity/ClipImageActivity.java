package com.example.test22.activity;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.test22.R;
import com.example.test22.view.ClipImageLayout;

public class ClipImageActivity extends Activity implements OnClickListener {
	Button bt;
	ClipImageLayout myImage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.clip_image);
		bt = (Button)findViewById(R.id.clip);
		bt.setOnClickListener(this);
		myImage = (ClipImageLayout)findViewById(R.id.myImage);
		myImage.setmHorizontalPadding(100);
		myImage.setMyImage(getResources().getDrawable(R.drawable.large));
		super.onCreate(savedInstanceState);
	}
	@Override
	public void onClick(View arg0) {
		try{
		Bitmap bitmap = myImage.clip();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] datas = baos.toByteArray();
		Intent intent = new Intent(ClipImageActivity.this,ShowImageActivity.class);
		intent.putExtra("bitmap", datas);
		startActivity(intent);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
