package com.example.test22.activity;

import com.example.test22.R;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;

public class MemoryAnalyActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_littlebylittle);
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		int heapSize = manager.getMemoryClass();
		System.out.println("heapSize=="+heapSize);
		LeakClass leak = new LeakClass();
		leak.start();
	}
	class LeakClass extends Thread{
		@Override
		public void run() {
			while(true){
				try {
					Thread.sleep(60*60*1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
