package com.example.test22;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;


public class MyApplication extends Application {

	public static MyApplication mInstance;
	private List<Activity> mList = new LinkedList<Activity>();
	@Override
	public void onCreate() {
		mInstance = this;
		super.onCreate();
	}
	public void addActivity(Activity activity) {
		mList.add(activity);
	}
	public Activity getActivity(){
		return mList.get(0);
		
	}

}
