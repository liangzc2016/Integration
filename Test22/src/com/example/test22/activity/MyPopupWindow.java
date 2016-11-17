package com.example.test22.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.test22.R;

public class MyPopupWindow extends PopupWindow implements OnClickListener {
	private View view;
	private LinearLayout ll_content;
	private ListView listView;
	private TextView tv_province, tv_city, tv_district;
	private View line1, line2, line3;
	private Activity activity;
	private WindowManager.LayoutParams params;
	private List<String> provinces;// ʡ��
	private List<String> citys;// ����
	private List<String> districts;// ����
	/** ʡ�ݣ����У�����Ĭ��ѡ�е�λ�� */
	private int provinceSelected = 0, citySelected = 0, districtSelected = 0;
	/** ��ǰѡ�񲿷֣�0����ʡ�ݣ�1���У�2���� */
	private int currentPos = 0;
	private MyAdapter myAdapter;
	SelectProvince selectProvince;
	SelectCity selectCity;
	SelectDistrict selectDistrict;

	/**
	 * @param activity
	 * @param parent
	 * @param provinces ��ʼ����ʡ������
	 */
	public MyPopupWindow(final Activity activity, final View parent,
			List<String> provinces) {
		this.activity = activity;
		this.provinces = provinces;
		citys = new ArrayList<String>();
		districts =new ArrayList<String>();
		view = LayoutInflater.from(activity).inflate(R.layout.my_pop, null);
		view.startAnimation(AnimationUtils.loadAnimation(activity,
				R.anim.fade_ins));
		view.startAnimation(AnimationUtils.loadAnimation(activity,
				R.anim.push_bottom_in));
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		// ʵ����һ��ColorDrawable��ɫΪ��͸��
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// ����SelectPicPopupWindow��������ı���
		this.setBackgroundDrawable(dw);
		setFocusable(true);
		setOutsideTouchable(true);
		setContentView(view);
		initView();
		initListener();
		initData();
	}
	public void showPopupWindow(View parent){
		params = activity.getWindow().getAttributes();
		params.alpha = 0.7f;
		activity.getWindow().setAttributes(params);
		showAtLocation(parent, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
	}
	private void initData(){
		myAdapter = new MyAdapter(provinces);
		listView.setAdapter(myAdapter);
	}
	private void initListener() {
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int pos,
					long id) {
				if(currentPos==0){//ʡ��
					provinceSelected = pos;
					//����ѡ��ʡ��ʱ�����ó��к͵���
					citySelected = 0;
					districtSelected = 0;
					tv_city.setVisibility(View.VISIBLE);
					showLine(1);
					if(selectProvince!=null){
						selectProvince.onSelectProvince(provinces.get(pos),myAdapter);
					}
					tv_province.setText(provinces.get(pos));
					tv_city.setText("ѡ����");
					tv_district.setText("ѡ����");
				}else if(currentPos==1){//����
					citySelected = pos;
					//���õ���
					districtSelected = 0;
					tv_district.setVisibility(View.VISIBLE);
					showLine(2);
					if(selectCity!=null){
						selectCity.onSelectCity(citys.get(pos), myAdapter);
					}
					tv_city.setText(citys.get(pos).toString());
					tv_district.setText("ѡ����");
				}else if(currentPos==2){//����
					districtSelected = pos;
					if(selectDistrict!=null){
						selectDistrict.onSelectDistrict(districts.get(pos).toString(), myAdapter);
					}
					tv_district.setText(districts.get(pos).toString());
					//ѡ����������
					dismiss();
				}
			}
		});
		tv_province.setOnClickListener(this);
		tv_city.setOnClickListener(this);
		tv_district.setOnClickListener(this);
		// �����������ȡ��popupview
		view.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					int y = (int) event.getY();
					// û�е�������ֵ�����
					if (isShowing()) {
						if (ll_content != null) {
							if (y < getScreenHeight()
									- (ll_content.getHeight() + 50)) {
								dismiss();
							}
						} else {
							if (y < getScreenHeight() - (view.getHeight() + 50)) {
								dismiss();
							}
						}
					}
					break;
				default:
					break;
				}

				return true;
			}
		});
		setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				params = activity.getWindow().getAttributes();
				params.alpha = 1f;
				activity.getWindow().setAttributes(params);
			}
		});
	}
	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.tv_province:
			myAdapter.setDatas(provinces);
			myAdapter.notifyDataSetChanged();
			showLine(0);
			//������ѡ�е�λ��
			listView.smoothScrollToPosition(provinceSelected);
			break;
		case R.id.tv_city:
			myAdapter.setDatas(citys);
			myAdapter.notifyDataSetChanged();
			showLine(1);
			listView.smoothScrollToPosition(citySelected);
			break;
		case R.id.tv_district:
			myAdapter.setDatas(districts);
			myAdapter.notifyDataSetChanged();
			showLine(2);
			//������ѡ�е�λ��
			listView.smoothScrollToPosition(districtSelected);
			break;
		}
	}
	
	/**
	 * @author Lenovo
	 *ѡ��ʡ�ݺ�ص��¼�����ʱ���Ҫ��ѯʡ�ݣ���Ӧ�ĳ��У���Ҫ���б���ʡ�ݵ�ֵ��֮���ڴ�
	 */
	public interface SelectProvince{
		/**param ѡ�е�ʡ��*/
		public void onSelectProvince(String province,MyAdapter myAdapter);
	}
	/**
	 * @author Lenovo
	 *ѡ����к�ص��¼�����ʱ��Ҫ��ѯ���ж�Ӧ�ĵ�������Ҫ���б�����е�ֵ��֮���ڴ�
	 */
	public interface SelectCity{
		public void onSelectCity(String city,MyAdapter myAdapter);
	}
	/**
	 * @author Lenovo
	 *ѡ�������ص��¼�
	 */
	public interface SelectDistrict{
		public void onSelectDistrict(String district,MyAdapter myAdapter);
	}
	
	
	private void initView(){
		ll_content = (LinearLayout)view.findViewById(R.id.ll_content);
		tv_province = (TextView)view.findViewById(R.id.tv_province);
		tv_city = (TextView)view.findViewById(R.id.tv_city);
		tv_district = (TextView)view.findViewById(R.id.tv_district);
		line1 = (View)view.findViewById(R.id.line1);
		line2 = (View)view.findViewById(R.id.line2);
		line3 = (View)view.findViewById(R.id.line3);
		listView = (ListView)view.findViewById(R.id.lv_data);
		tv_city.setVisibility(View.INVISIBLE);
		tv_district.setVisibility(View.INVISIBLE);
		line2.setVisibility(View.INVISIBLE);
		line3.setVisibility(View.INVISIBLE);
	}

	class MyAdapter extends BaseAdapter {
		List<String> datas;

		public MyAdapter(List<String> datas) {
			this.datas = datas;
		}
		public void setDatas(List<String> datas){
			this.datas=null;
			this.datas = datas;
		}

		@Override
		public int getCount() {
			return datas != null ? datas.size() : 0;
		}

		@Override
		public Object getItem(int arg0) {
			return datas != null ? datas.get(arg0) : null;
		}

		@Override
		public long getItemId(int arg0) {
			return datas != null ? arg0 : 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh;
			if (convertView == null) {
				vh = new ViewHolder();
				convertView = LayoutInflater.from(activity).inflate(
						R.layout.list_item, null);
				vh.iv_select = (ImageView) convertView
						.findViewById(R.id.iv_select);
				vh.tv_area = (TextView) convertView.findViewById(R.id.tv_area);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			if (currentPos == 0) {
				if (position == provinceSelected) {
					vh.iv_select.setVisibility(View.VISIBLE);
				} else {
					vh.iv_select.setVisibility(View.INVISIBLE);
				}
			} else if (currentPos == 1) {
				if (position == citySelected) {
					vh.iv_select.setVisibility(View.VISIBLE);
				} else {
					vh.iv_select.setVisibility(View.INVISIBLE);
				}
			} else if (currentPos == 2) {
				if (position == districtSelected) {
					vh.iv_select.setVisibility(View.VISIBLE);
				} else {
					vh.iv_select.setVisibility(View.INVISIBLE);
				}
			}
			vh.tv_area.setText(datas.get(position).toString());
			return convertView;
		}

		class ViewHolder {
			ImageView iv_select;
			TextView tv_area;
		}
	}
	/**pos 0,1,2�ֱ���ʾ���ײ��ĺ���*/
	private void showLine(int pos){
		switch(pos){
		case 0:
			currentPos = 0;
			line1.setVisibility(View.VISIBLE);
			line2.setVisibility(View.INVISIBLE);
			line3.setVisibility(View.INVISIBLE);
			break;
		case 1:
			currentPos = 1;
			line1.setVisibility(View.INVISIBLE);
			line2.setVisibility(View.VISIBLE);
			line3.setVisibility(View.INVISIBLE);
			break;
		case 2:
			currentPos = 2;
			line1.setVisibility(View.INVISIBLE);
			line2.setVisibility(View.INVISIBLE);
			line3.setVisibility(View.VISIBLE);
			break;
		}
	}
	
	

	/**����ѡ��ʡ�ݺ�Ļص��¼�
	 * @param selectProvince
	 */
	public void setSelectProvinceListener(SelectProvince selectProvince) {
		this.selectProvince = selectProvince;
	}
	/**����ѡ����к�Ļص��¼�
	 * @param selectCity
	 */
	public void setSelectCityListener(SelectCity selectCity) {
		this.selectCity = selectCity;
	}
	/**����ѡ�������Ļص��¼�
	 * @param selectDistrict
	 */
	public void setSelectDistrictListener(SelectDistrict selectDistrict) {
		this.selectDistrict = selectDistrict;
	}
	/**
	 * ��listview��Item���ؿ�ʼλ��
	 */
	public void resetListViewPos(){
		listView.smoothScrollToPosition(0);
	}
	public List<String> getCitys() {
		return citys;
	}
	public void setCitys(List<String> citys) {
		this.citys = citys;
	}
	public List<String> getDistricts() {
		return districts;
	}
	public void setDistricts(List<String> districts) {
		this.districts = districts;
	}
	/**
	 * ��ȡ��Ļ�߶�
	 * 
	 * @return
	 */
	public int getScreenHeight() {
		int screenHeight = 0;
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		if (dm != null) {
			int screenWidth = dm.widthPixels;// ��Ļ���
			int height = dm.heightPixels;// ȫ��Ļ�߶�
			int statusBarHeight = getStatusBarHeight(activity);// ״̬���߶�
			screenHeight = height - statusBarHeight;// ��Ļ�߶�
		}
		return screenHeight;
	}

	/**
	 * ��ȡ״̬���߶�
	 * 
	 * @param activity
	 * @return
	 */
	public int getStatusBarHeight(Activity activity) {
		int result = 0;
		int resourceId = activity.getResources().getIdentifier(
				"status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = activity.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}
	
}
