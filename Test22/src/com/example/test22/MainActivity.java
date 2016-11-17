package com.example.test22;


import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends Activity {
    ProgressView progressView,progressView2,progressView3;
    int progress=0;//进度值
    int max = 100;//最大值
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		progressView = (ProgressView)findViewById(R.id.pb1);
		progressView2 = (ProgressView)findViewById(R.id.pb2);
		progressView3 = (ProgressView)findViewById(R.id.pb3);
		progressView.setMax(max);
		new Thread(new Runnable(){

			@Override
			public void run() {
				while(progress!=max){
					try {
						progress+=10;
						progressView.setProgress(progress);
						progressView2.setProgress(progress);
						progressView3.setProgress(progress);
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
				
			}
			
		}).start();
		
		
	}
	
}
