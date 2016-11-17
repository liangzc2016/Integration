package com.example.test22.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.test22.R;
import com.example.test22.bean.MessageItem;
import com.example.test22.view.ListViewCompat;
import com.example.test22.view.SlideView;
import com.example.test22.view.SlideView.OnSlideListener;

public class SlideCancelActivity extends Activity {
	private Context context;
	private ListViewCompat mListViewCompat;
	private List<MessageItem> mMessageItem; 
	private int mSize=20;
	private SlideView mLastSlideView;
	private MyAdapter myAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_slide_cancel);
		mListViewCompat = (ListViewCompat)findViewById(R.id.list);
		context = this;
		initData();
		super.onCreate(savedInstanceState);
	}
	private void initData() {
		mMessageItem = new ArrayList<MessageItem>();
		for(int i=0;i<mSize;i++){
			MessageItem m = new MessageItem();
			m.time="2016-1-19";
			m.title="БъЬт"+i;
			mMessageItem.add(m);
		}
		myAdapter = new MyAdapter(); 
		mListViewCompat.setAdapter(myAdapter);
	}
	class MyAdapter extends BaseAdapter implements OnSlideListener{
		@Override
		public int getCount() {
			if(mMessageItem==null)return 0;
			return mMessageItem.size();
		}

		@Override
		public Object getItem(int arg0) {
			if(mMessageItem==null)
				return null;
			return mMessageItem.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
//			Log.e("position", position+"");
			SlideView slideView;
			slideView = (SlideView)convertView;
			ViewHolder holder;
			if(slideView==null){
				View v = LayoutInflater.from(context).inflate(R.layout.item_slide, null);
				slideView = new SlideView(context);
				slideView.setContentView(v);
				slideView.setOnSlideListener(this);
				holder = new ViewHolder(slideView);
				slideView.setTag(holder);
			}else{
				holder = (ViewHolder)slideView.getTag();
			}
			MessageItem m = mMessageItem.get(position);
			holder.title.setText(m.title);
			holder.time.setText(m.time);
			holder.cancel.setOnClickListener(new MOnClickListener(position));
			m.mSlideView = slideView;
			m.mSlideView.shrink();
			return slideView;
		}
		class ViewHolder {
			public ViewHolder(View view) {
				title = (TextView)view.findViewById(R.id.tv_title);
				time = (TextView)view.findViewById(R.id.tv_time);
				cancel = (TextView)view.findViewById(R.id.bt_del);
			}
			TextView title;
			TextView time;
			TextView cancel;
		}
		@Override
		public void onSlideListener(View view, int state) {
			if(mLastSlideView!=null&&view!=mLastSlideView){
				mLastSlideView.shrink();
			}
			if(state==OnSlideListener.SLIDE_STATE_ON){
				mLastSlideView = (SlideView)view;
			}
			
		}
        class MOnClickListener implements OnClickListener{
        	private int pos;
        	public MOnClickListener(int pos){
        		this.pos = pos;
        	}
			@Override
			public void onClick(View v) {
				mMessageItem.remove(pos);
				myAdapter.notifyDataSetChanged();
				
			}
        	
        }
	}

}
