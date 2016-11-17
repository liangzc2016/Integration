package com.example.test22.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.test22.R;
import com.example.test22.view.HorizontalListView;
import com.example.test22.viewgroup.HorizontalScrollListView;

public class ListViewHorizontal extends Activity {
	HorizontalListView lv1;
	ListView lv2;
	float orginX,orginY;
	List<String> datas=new ArrayList<String>();
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_listviewhor);
	        lv1 = (HorizontalListView)findViewById(R.id.lv1);
	        lv2 = (ListView)findViewById(R.id.lv2);
	        ArrayList<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();
	        for(int i=0;i<30;i++){
	        	HashMap<String,String> m = new HashMap<String, String>();
	        	m.put("test", "test"+i);
	        	list.add(m);
	        	datas.add("test"+i);
	        }
	        lv1.setAdapter(new SimpleAdapter(this, list, android.R.layout.simple_list_item_1, new String[]{"test"}, new int[]{android.R.id.text1}));
//	        lv2.setAdapter(new SimpleAdapter(this, list, android.R.layout.simple_list_item_1, new String[]{"test"}, new int[]{android.R.id.text1}));
	        lv2.setAdapter(new MyAdapter());
//	        hs = (HorizontalScrollView)findViewById(R.id.hs);
//	        ll_lv2.setOnTouchListener(new OnTouchListener() {
//				
//				@Override
//				public boolean onTouch(View v, MotionEvent event) {
//					int act = event.getAction();
//					float nowX = event.getX();
//					float nowY = event.getY();
//					switch(act){
//					case MotionEvent.ACTION_DOWN:
//						orginX = nowX;
//						orginY = nowY;
//						break;
//					case MotionEvent.ACTION_MOVE:
//						float dx = nowX-orginX;
//						float dy = nowY-orginY;
//						orginX = nowX;
//						orginY = nowY;
//						if(Math.abs(dx)>Math.abs(dy)){
//							ll_lv2.scrollBy(-(int) dx, 0);
//							return true;
//						}
//						break;
//					case MotionEvent.ACTION_UP:
//						break;
//					}
//					return false;
//				}
//			});
	}
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return datas.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return datas.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh;
			if(convertView==null){
				vh = new ViewHolder();
				convertView = LayoutInflater.from(ListViewHorizontal.this).inflate(R.layout.listview_item2, null);
				vh.tv_title = (TextView)convertView.findViewById(R.id.tv_title);
				convertView.setTag(vh);
			}else{
				vh = (ViewHolder)convertView.getTag();
			}
			vh.tv_title.setText(datas.get(position));
			return convertView;
		}
		class ViewHolder {
			TextView tv_title;
		}
	}

}
