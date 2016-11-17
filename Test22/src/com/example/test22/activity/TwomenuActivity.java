package com.example.test22.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.test22.R;
import com.example.test22.viewgroup.BididSlidingLayout;

public class TwomenuActivity extends Activity {
	/** 
     * 双向滑动菜单布局 
     */  
    private BididSlidingLayout bidirSldingLayout;  
  
    /** 
     * 在内容布局上显示的ListView 
     */  
    private ListView contentList;  
  
    /** 
     * ListView的适配器 
     */  
    private ArrayAdapter<String> contentListAdapter;  
  
    /** 
     * 用于填充contentListAdapter的数据源。 
     */  
    private String[] contentItems = { "Content Item 1", "Content Item 2", "Content Item 3",  
            "Content Item 4", "Content Item 5", "Content Item 6", "Content Item 7",  
            "Content Item 8", "Content Item 9", "Content Item 10", "Content Item 11",  
            "Content Item 12", "Content Item 13", "Content Item 14", "Content Item 15",  
            "Content Item 16" };  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_twomenu);  
        bidirSldingLayout = (BididSlidingLayout) findViewById(R.id.bidir_sliding_layout);  
        contentList = (ListView) findViewById(R.id.contentList);  
        contentListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,  
                contentItems);  
        contentList.setAdapter(contentListAdapter);  
        bidirSldingLayout.setScrollEvent(contentList);  
        contentList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(TwomenuActivity.this, contentItems[position], Toast.LENGTH_SHORT).show();
			}
		});
    }  
  
}  
