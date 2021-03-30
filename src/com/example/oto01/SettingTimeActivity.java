package com.example.oto01;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class SettingTimeActivity extends BaseActivity{
	private List<String> hList = new ArrayList<String>();
	private List<String> hList2 = new ArrayList<String>();
	private List<String> hList3 = new ArrayList<String>();
	private List<String> hList4 = new ArrayList<String>();
	private List<String> mList = new ArrayList<String>();
	private MyHourAdapter adapter1 , adapter2 , adapter3, adapter4, adapter5;
	private Spinner h1,h2,h3,h4;
	private Spinner m1,m2,m3,m4;
	
	private String hour1, hour2, hour3, hour4;
	private String min1, min2, min3, min4;
	
	public List<String> getCurHour(int lastHour){
		System.out.println("------lastHour------>"+lastHour);
		int size = (hList.size()-lastHour-1);
		List<String> array = new ArrayList<String>();
		for (int i = 0; i < size ; i++) {
			int hour = ++lastHour;
			if(hour<10){
				
				array.add(i, "0"+hour);
			}else{
				array.add(i, ""+hour);
				
			}
		}
		System.out.println("------array------>"+array);
		return array;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_time);
		initListData();
		initView();
	}
	
	private class MyHourAdapter extends BaseAdapter{
		private List<String> myList;
		public MyHourAdapter(List<String> myList){
			this.myList = myList;
		}
		
		@Override
		public int getCount() {
			return this.myList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return this.myList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int poaition, View view, ViewGroup parent) {
			if(view == null){
				view = LayoutInflater.from(SettingTimeActivity.this).inflate(R.layout.drop_down_item, parent, false);
			}
			((TextView)view).setText(this.myList.get(poaition));
			return view;
		}
		
	}
	
	
	private void initView(){
		h1 = (Spinner) findViewById(R.id.h1);
		h2 = (Spinner) findViewById(R.id.h2);
		h3 = (Spinner) findViewById(R.id.h3);
		h4 = (Spinner) findViewById(R.id.h4);
		m1 = (Spinner) findViewById(R.id.m1);
		m2 = (Spinner) findViewById(R.id.m2);
		m3 = (Spinner) findViewById(R.id.m3);
		m4 = (Spinner) findViewById(R.id.m4);
		
		h1.setAdapter(adapter1);
		h2.setAdapter(adapter1);
		h3.setAdapter(adapter1);
		h4.setAdapter(adapter1);
		
		m1.setAdapter(adapter5);
		m2.setAdapter(adapter5);
		m3.setAdapter(adapter5);
		m4.setAdapter(adapter5);
		
		h1.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				hour1 = hList.get(arg2);
				System.out.println("------hour1------->"+hour1);
				List<String> nextList = getCurHour(Integer.parseInt(hour1));
				hList2 = nextList;
				System.out.println("-------hList2------>"+hList2);
				adapter2 = new MyHourAdapter(hList2);
				adapter2.notifyDataSetChanged();
				h2.setAdapter(adapter2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		h2.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				System.out.println("-------hList2-  ----->"+hList2);
				hour2 = hList2.get(arg2);
				List<String> nextList = getCurHour(Integer.parseInt(hour2));
				hList3 = nextList;
				adapter3 = new MyHourAdapter(hList3);
				adapter3.notifyDataSetChanged();
				h3.setAdapter(adapter3);
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		
		h3.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				hour3 = hList3.get(arg2);
				List<String> nextList = getCurHour(Integer.parseInt(hour3));
				hList4 = nextList;
				adapter4 = new MyHourAdapter(hList4);
				adapter4.notifyDataSetChanged();
				h4.setAdapter(adapter4);
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		
		h3.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				hour4 = hList4.get(arg2);
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		
		
		m1.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				min1 = mList.get(arg2);
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		m2.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				min2 =mList.get(arg2);
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		m3.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				min3 =mList.get(arg2);
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		m4.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				min4 =mList.get(arg2);
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
	}
	
	
	
	
	private void initListData(){
		hList.add(0, "00");
		hList.add(1, "01");
		hList.add(2, "02");
		hList.add(3, "03");
		hList.add(4, "04");
		hList.add(5, "05");
		hList.add(6, "06");
		hList.add(7, "07");
		hList.add(8, "08");
		hList.add(9, "09");
		hList.add(10, "10");
		hList.add(11, "11");
		hList.add(12, "12");
		hList.add(13, "13");
		hList.add(14, "14");
		hList.add(15, "15");
		hList.add(16, "16");
		hList.add(17, "17");
		hList.add(18, "18");
		hList.add(19, "19");
		hList.add(20, "20");
		hList.add(21, "21");
		hList.add(22, "22");
		hList.add(23, "23");
		
		mList.add(0,"00");
		mList.add(1,"30");
		
		adapter1 = new MyHourAdapter(hList);
		adapter2 = new MyHourAdapter(hList2);
		adapter2.notifyDataSetChanged();
		adapter3 = new MyHourAdapter(hList3);
		adapter3.notifyDataSetChanged();
		adapter4 = new MyHourAdapter(hList4);
		adapter4.notifyDataSetChanged();
		
		
		adapter5 = new MyHourAdapter(mList);
	}

	public void ok_onClick(View view){
		System.out.println("------time------->"+hour1+":"+min1+"--"+hour2+":"+min2+"-----"+hour3+":"+min3+"--"+hour4+":"+min4);
	}
}
