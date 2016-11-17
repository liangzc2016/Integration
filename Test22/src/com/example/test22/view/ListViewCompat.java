package com.example.test22.view;

import com.example.test22.bean.MessageItem;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

/**
 * @author zc
 *侧滑删除的listview
 */
public class ListViewCompat extends ListView {
	private static final String TAG="ListViewCompat";
	private SlideView mFocusedItem;

	public ListViewCompat(Context context) {
		super(context);
	}

	public ListViewCompat(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ListViewCompat(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	public void shrinkListItem(int pos){
		View item = getChildAt(pos);
		if(item!=null){
			try{
				((SlideView)item).shrink();
			}catch(ClassCastException e){
				e.printStackTrace();
			}
			
		}
	}
	/*当前的view把事件进行了拦截，则事件则会被传递到该方法中
     return false：表明没有消费该事件，事件将会以冒泡的方式一直被传递到
     上层的view或Activity中的onTouchEvent事件处理。
     如果最上层的view或Activity中的onTouchEvent还是返回false。
     则该事件将消失。接下来来的一系列事件都将会直接被上层的onTouchEvent方法捕获
     return true: 表明消费了该事件，事件到此结束。
     return super.onTouchEvent(event)：默认情况，和return false一样。
	 * (non-Javadoc)
	 * @see android.widget.AbsListView#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev){
		switch(ev.getAction()){
		case MotionEvent.ACTION_DOWN:
			Log.e("ListViewCompat-onTouchEvent", "Action_down");
			int x = (int)ev.getX();
			int y = (int)ev.getY();
			int position = pointToPosition(x, y);
			Log.e(TAG,"position="+position);
			if(position!=INVALID_POSITION){
				MessageItem data = (MessageItem)getItemAtPosition(position);
				mFocusedItem=data.mSlideView;
				Log.e(TAG,"mFocusedItem"+mFocusedItem);
			}
			return false;
//			break;
		case MotionEvent.ACTION_MOVE:{
			Log.e("ListViewCompat-onTouchEvent", "Action_MoVE");
			break;
		}
		case MotionEvent.ACTION_UP:{
			Log.e("ListViewCompat-onTouchEvent", "Action_up");
			break;
		}
		}
	
		if(mFocusedItem!=null){
//			mFocusedItem.onRequireTouchEvent(ev);
		}
		return super.onTouchEvent(ev);
	}
	int mlastX=0,mlastY=0;
	private static final int TAN=2;
	/* 默认返回false,进行事件的分发，返回true ,则留给个自己的onTouchEvent来处理
	 * @see android.widget.AbsListView#onInterceptTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		int x = (int) ev.getX();
		int y = (int) ev.getY();
		switch(ev.getAction()){
		case MotionEvent.ACTION_DOWN:
			mlastX = x;
			mlastY = y;
			Log.e("onInterceptTouchEvent", "ACTION_DOWN");
//			return true;
			break;
		case MotionEvent.ACTION_MOVE:
			Log.e("onInterceptTouchEvent", "ACTION_MOVE");
			Log.e("y-mlastY", ""+(y-mlastY));
			if(Math.abs(x-mlastX)<Math.abs(y-mlastY)*TAN){
				Log.e("Action_move", "return true");
				mlastY = y;
				mlastX = x;
				return true;
			}
			mlastY = y;
			mlastX = x;
//			return true;
			break;
		case MotionEvent.ACTION_UP:
			Log.e("onInterceptTouchEvent", "ACTION_UP");
			break;
		case MotionEvent.ACTION_CANCEL:
			Log.e("onInterceptTouchEvent", "ACTION_CANCEL");
			break;
		}
		boolean re = super.onInterceptTouchEvent(ev);
		Log.e("super.onInterceptTouchEvent(ev)", ""+re);
		return re;
	}
	/*当触摸事件发生的时候，首先会被当前的activity进行分发，即当前activity的dispatchTouchEvent方法会被执行。这个时候,该方法有三种返回的情况：
          return false： 表明事件不会被进行分发。事件会以冒泡的方式被传递给上层的view或activity的onTouchEvent方法进行消费掉。
          return true：表明该时间已经被处理。事件会被当前view或activity的dispatchTouchEvent给消费掉。不会再进行传递，事件到此结束。
          return super.dispatchTouchEvent(ev)：表明该事件将会被分发。此时当前View的onIntercepterTouchEvent方法会捕获该事件，判断需不需要进行事件的拦截。
	 * @see android.view.ViewGroup#dispatchTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch(ev.getAction()){
		case MotionEvent.ACTION_DOWN:
			Log.e("ListView--dispatchTouchEvent", "ACTION_DOWN");
			break;
	case MotionEvent.ACTION_UP:
		Log.e("ListView--dispatchTouchEvent", "ACTION_UP");
		break;
	case MotionEvent.ACTION_CANCEL:
		Log.e("ListView--dispatchTouchEvent", "ACTION_CANCEL");
		break;
	}
		return super.dispatchTouchEvent(ev);
	}
	
}
