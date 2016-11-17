package com.example.test22.suspendsion;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.IBinder;
import android.test.suitebuilder.annotation.SmallTest;

public class FloatWindowService extends Service {
	/**
	 * �û����߳��д������Ƴ�������
	 */
	private Handler handler = new Handler();
	/**
	 * ��ʱ������ʱ���м�⵱ǰӦ�ô��������Ƴ�������
	 */
	private Timer timer;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//������ʱ����ÿ��0.5sˢ��һ��
		if(timer==null){
			timer = new Timer();
			timer.scheduleAtFixedRate(new RefreshTask(), 0, 500);
		}
		return super.onStartCommand(intent, flags, startId);
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		//Service����ֹ��ͬʱҲֹͣ��ʱ��
		timer.cancel();
		timer = null;
	}
	class RefreshTask extends TimerTask{

		@Override
		public void run() {
//			��ǰ���������棬��û�����������򴴽�������
			if(isHome()&&!MyWindowManager.isWindowShowing()){
				handler.post(new Runnable(){

					@Override
					public void run() {
						MyWindowManager.createSmallWindow(getApplicationContext());
					}
					
				});
			}else if(!isHome()&&MyWindowManager.isWindowShowing()){
				//��ǰ���治�����棬������������ʾ������
				handler.post(new Runnable(){
					@Override
					public void run() {
						MyWindowManager.removeSmallWindow(getApplicationContext());
						MyWindowManager.removeBigWindow(getApplicationContext());
					}
				});
			}else if(isHome()&&MyWindowManager.isWindowShowing()){
				//��ǰ���������棬������������ʾ������ڴ�
				handler.post(new Runnable(){
					@Override
					public void run() {
						MyWindowManager.updateUserPercent(getApplicationContext());
					}
				});
			}
		}
		
	}
	/**�жϵ�ǰ�����Ƿ�������
	 * @return
	 */
	private boolean isHome(){
		ActivityManager mActivityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
		return getHomes().contains(rti.get(0).topActivity.getPackageName());
	}
	/** ������������Ӧ�õ�Ӧ�ð���
	 * @return ���ذ������а������ַ����б�
	 */
	private List<String> getHomes(){
		List<String> names = new ArrayList<String>();
		PackageManager packegeManager = this.getPackageManager();
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		List<ResolveInfo> resolveInfo = packegeManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		for(ResolveInfo ri:resolveInfo){
			names.add(ri.activityInfo.packageName);
		}
		return names;
	}
	
}
