package com.example.test22.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.test22.R;
import com.example.test22.activity.MyPopupWindow.MyAdapter;
import com.example.test22.activity.MyPopupWindow.SelectCity;
import com.example.test22.activity.MyPopupWindow.SelectDistrict;
import com.example.test22.activity.MyPopupWindow.SelectProvince;
import com.example.test22.view.SmileView;

public class PopupWindowAct extends Activity implements OnClickListener {
	Button bt1,bt2,bt3;
	SmileView smile;
	List<String> provinces,citys,districts;
	MyPopupWindow pop;
	LinearLayout content;
	String area="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_popupwindow);
		bt1 = (Button)findViewById(R.id.bt1);
		bt1.setOnClickListener(this);
		bt2 = (Button)findViewById(R.id.bt2);
		bt2.setOnClickListener(this);
		bt3 = (Button)findViewById(R.id.bt3);
		bt3.setOnClickListener(this);
		smile = (SmileView)findViewById(R.id.smile);
		content = (LinearLayout)findViewById(R.id.content);
		initData();
		change();
	}
	private void initData() {
		provinces = new ArrayList<String>();
		citys = new ArrayList<String>();
		districts = new ArrayList<String>();
		for(int i=0;i<30;i++){
			provinces.add("广东"+i);
			citys.add("广州市"+i);
			districts.add("天河区"+i);
		}
		pop = new MyPopupWindow(this, content, provinces);
		pop.setSelectProvinceListener(new SelectProvince() {
			
			@Override
			public void onSelectProvince(String province, MyAdapter myAdapter) {
				area = province;
				bt1.setText(area);
				//查询对应的城市
				pop.setCitys(citys);
				myAdapter.setDatas(citys);
				myAdapter.notifyDataSetChanged();
				pop.resetListViewPos();
			}
		});
		pop.setSelectCityListener(new SelectCity() {
			@Override
			public void onSelectCity(String city, MyAdapter myAdapter) {
				area += city;
				bt1.setText(area);
				//查询对应的区
				pop.setDistricts(districts);
				myAdapter.setDatas(districts);
				myAdapter.notifyDataSetChanged();
				pop.resetListViewPos();
			}
		});
		pop.setSelectDistrictListener(new SelectDistrict() {
			
			@Override
			public void onSelectDistrict(String district, MyAdapter myAdapter) {
				area += district;
				bt1.setText(area);
			}
		});
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.bt1:
			area="";
			bt1.setText(area);
			pop.showPopupWindow(content);
			break;
		case R.id.bt2:
			smile.setDuration(2000);
			smile.setRepeaCount(8);
			smile.performAnim();
			break;
		case R.id.bt3:
			smile.cancelAnim();
			break;
		}
	}
	public void change(){
//		Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.img_test);
//		iv.setImageBitmap(b);
	}
	
}
