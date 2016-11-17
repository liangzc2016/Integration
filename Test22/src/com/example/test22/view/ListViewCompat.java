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
 *�໬ɾ����listview
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
	/*��ǰ��view���¼����������أ����¼���ᱻ���ݵ��÷�����
     return false������û�����Ѹ��¼����¼�������ð�ݵķ�ʽһֱ�����ݵ�
     �ϲ��view��Activity�е�onTouchEvent�¼�����
     ������ϲ��view��Activity�е�onTouchEvent���Ƿ���false��
     ����¼�����ʧ������������һϵ���¼�������ֱ�ӱ��ϲ��onTouchEvent��������
     return true: ���������˸��¼����¼����˽�����
     return super.onTouchEvent(event)��Ĭ���������return falseһ����
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
	/* Ĭ�Ϸ���false,�����¼��ķַ�������true ,���������Լ���onTouchEvent������
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
	/*�������¼�������ʱ�����Ȼᱻ��ǰ��activity���зַ�������ǰactivity��dispatchTouchEvent�����ᱻִ�С����ʱ��,�÷��������ַ��ص������
          return false�� �����¼����ᱻ���зַ����¼�����ð�ݵķ�ʽ�����ݸ��ϲ��view��activity��onTouchEvent�����������ѵ���
          return true��������ʱ���Ѿ��������¼��ᱻ��ǰview��activity��dispatchTouchEvent�����ѵ��������ٽ��д��ݣ��¼����˽�����
          return super.dispatchTouchEvent(ev)���������¼����ᱻ�ַ�����ʱ��ǰView��onIntercepterTouchEvent�����Ჶ����¼����ж��費��Ҫ�����¼������ء�
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
