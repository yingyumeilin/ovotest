package com.example.oto01;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnCameraChangeListener;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Inputtips.InputtipsListener;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.poisearch.PoiSearch.SearchBound;
import com.example.oto01.utils.AMapUtil;
import com.example.oto01.utils.ToastUtil;

/**
 * <pre>
 * 类名称：MyLocationActivity             
 * @version
 * </pre>
 */
public class MyLocationActivity extends BaseActivity implements OnClickListener,
		AMapLocationListener, TextWatcher, OnPoiSearchListener, LocationSource,
		OnGeocodeSearchListener {

	private static final String TAG = MyLocationActivity.class.getSimpleName();

	private Context context;
	private ListView mListView;
	private MapView mapView;
	private AMap aMap;
	private Button commitBtn;
	private Intent intent;
	private Double geoLat;
	private boolean isonClick = false;

	private Double geoLng;
	private AutoCompleteTextView searchText;// 输入搜索关键字
	private String keyWord = "";// 要输入的poi搜索关键字
	private ProgressDialog progDialog = null;// 搜索时进度条
	private PoiResult poiResult; // poi返回的结果
	private int currentPage = 0;// 当前页面，从0开始计数
	private PoiSearch.Query query;// Poi查询条件类
	private PoiSearch poiSearch;// POI搜索
	private ListAdapter adapter;
	private Marker locationMarker; // 选择的点
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;
	private ImageView now;
	private ListView listVIewSearch;
	private int i;
	private ListView listVIew;
	private List<PoiItem> list;
	private List<Tip> searchList;
	private Dialog proDialog;
	private int searchType = 0;// 搜索类型
	private SearchAdapter searchAdapter;
	private RelativeLayout fl_map;
	private GeocodeSearch geocoderSearch;
	private String name;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.context = this;
		setContentView(R.layout.activity_my_location);
		mapView = (MapView) findViewById(R.id.map_view);
		now = (ImageView) findViewById(R.id.now);
		mapView.onCreate(savedInstanceState);
		now.setOnClickListener(this);
		initAmap();

	}

	private void initAmap() {
		if (aMap == null) {
			aMap = mapView.getMap();
		}
		fl_map = (RelativeLayout) findViewById(R.id.fl_map);
		searchText = (AutoCompleteTextView) findViewById(R.id.keyWord);
		listVIew = (ListView) findViewById(R.id.listVIew);
		listVIewSearch = (ListView) findViewById(R.id.listVIewSearch);
		adapter = new ListAdapter();
		searchAdapter = new SearchAdapter();
		listVIewSearch.setAdapter(searchAdapter);
		listVIew.setAdapter(adapter);
		list = new ArrayList<PoiItem>();
		searchList = new ArrayList<Tip>();
		listVIew.setVisibility(View.VISIBLE);
		listVIewSearch.setVisibility(View.GONE);
		if (list.size() == 0 || list.isEmpty()) {

		} else {
			list.clear();
			adapter.removeAllDatas();
		}

		searchText.addTextChangedListener(this);// 添加文本输入框监听事件
		aMap.setOnCameraChangeListener(cameraChangeListener);
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.icon_blue));// 设置小蓝点的图标
		myLocationStyle.strokeColor(Color.TRANSPARENT);// 设置圆形的边框颜色
		myLocationStyle.strokeWidth(0f);
		myLocationStyle.radiusFillColor(Color.TRANSPARENT);// 设置圆形的填充颜色
		// // myLocationStyle.anchor(0.5f, 0f);
		// // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
		// myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setLocationSource(this);
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		mAMapLocationManager = LocationManagerProxy
				.getInstance(MyLocationActivity.this);
		// aMap.setLocationSource(locationSource);
		aMap.getUiSettings().setMyLocationButtonEnabled(true);
		aMap.setMyLocationEnabled(true);// 设置为true表示系统定位按钮显示并响应点击，false表示隐藏，默认是false
		aMap.getUiSettings().setZoomGesturesEnabled(false);

		geocoderSearch = new GeocodeSearch(this);
		geocoderSearch.setOnGeocodeSearchListener(this);
	}

	OnCameraChangeListener cameraChangeListener = new OnCameraChangeListener() {

		@Override
		public void onCameraChange(CameraPosition position) {
			// TODO Auto-generated method stub
			try {
				LatLng latLng = position.target;
				locationMarker.remove();
				LatLng latLng2 = new LatLng(latLng.latitude, latLng.longitude);
				MarkerOptions markerOption = new MarkerOptions();
				markerOption.position(latLng2);
				markerOption.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.map_location));
				markerOption.draggable(true);
				// aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
				// aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng)); //
				// 设置到屏幕中心点
				locationMarker = aMap.addMarker(markerOption);
				locationMarker.showInfoWindow();
			} catch (Exception e) {
			}

		}

		@Override
		public void onCameraChangeFinish(CameraPosition position) {
			// TODO Auto-generated method stub
			try {
				if (locationMarker != null) {
					final LatLng latLng = position.target;
					new Thread(new Runnable() {

						@Override
						public void run() {
							try {
								Bundle bundle = new Bundle();
								bundle.putDouble("LAT", latLng.latitude);
								bundle.putDouble("LNG", latLng.longitude);
								Message message = handler.obtainMessage(1);
								message.setData(bundle);
								handler.sendMessage(message);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}).start();

				}
			} catch (Exception e) {
			}

		}

	};
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				StringBuffer stringBuffer = new StringBuffer();
				Bundle myBundle = msg.getData();
				Double myLat = myBundle.getDouble("LAT");
				Double myLng = myBundle.getDouble("LNG");
				// locationDes = stringBuffer.toString();
				locationMarker.remove();
				LatLng latLng = new LatLng(myLat, myLng);
				MarkerOptions markerOption = new MarkerOptions();
				markerOption.position(latLng);
				markerOption.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.map_location));
				markerOption.draggable(true);

				System.out.println("--------------i------------------>" + i);
				if (i >= 1 && i < 3) {
					i++;
					System.out.println("123");
					return;
				} else if (i > 2 && i < 4) {
					// aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
					// aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
					// // 设置到屏幕中心点
					locationMarker = aMap.addMarker(markerOption);
					locationMarker.showInfoWindow();
					i++;
					doSearchQuery1(myLat, myLng);
					System.out.println("12321");
					return;
				} else if (i > 3) {
					markerOption.anchor(0.5f, 1f);
					// aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
					// aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
					// // 设置到屏幕中心点
					locationMarker = aMap.addMarker(markerOption);
					locationMarker.showInfoWindow();
					doSearchQuery1(myLat, myLng);
				}
				break;
			default:
				break;
			}
		}
	};

	private String adCode = "";

	private void SetCrilc() {
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.icon_blue));// 设置小蓝点的图标
		myLocationStyle.strokeColor(Color.TRANSPARENT);// 设置圆形的边框颜色
		myLocationStyle.strokeWidth(0f);
		myLocationStyle.radiusFillColor(Color.TRANSPARENT);// 设置圆形的填充颜色
		aMap.setMyLocationStyle(myLocationStyle);
	}

	/**
	 * 标记地图并移动地图到中心位置
	 * 
	 * @param lat
	 *            纬度
	 * @param lon
	 *            经度
	 */
	private void markerAndMoveMap(double lat, double lon) {

		i = 0;

		LatLng latLng = new LatLng(lat, lon);

		MarkerOptions markerOption = new MarkerOptions();
		markerOption.position(latLng);
		markerOption.icon(BitmapDescriptorFactory
				.fromResource(R.drawable.map_location));
		markerOption.draggable(true);

		if (i != 0) {
			// 锚点位置
			markerOption.anchor(0.5f, 1f);
		}
		aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
		aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng)); //
		// 设置到屏幕中心点
		locationMarker = aMap.addMarker(markerOption);

		locationMarker.showInfoWindow();
		// keyWord = "";
		// doSearchQuery1(lat, lon);
		i++;
		if (isonClick) {
			i = 3;
			isonClick = false;
		} else {

		}
		deactivate();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 开始进行poi搜索
	 */
	protected void doSearchQuery() {
		currentPage = 0;
		query = new PoiSearch.Query(keyWord, "", adCode);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
		query.setPageSize(10);// 设置每页最多返回多少条poiitem
		query.setPageNum(currentPage);// 设置查第一页

		poiSearch = new PoiSearch(this, query);
		poiSearch.setOnPoiSearchListener(this);
		poiSearch.searchPOIAsyn();
	}

	/**
	 * 开始进行poi搜索
	 */
	protected void doSearchQuery1(double lat, double lon) {
		aMap.setOnMapClickListener(null);// 进行poi搜索时清除掉地图点击事件

		if (list.size() == 0 || list.isEmpty()) {

		} else {
			list.clear();
			adapter.removeAllDatas();
		}
		System.out
				.println("--------------MyLocationActivity----adCode--------------------->"
						+ adCode);
		currentPage = 0;
		query = new PoiSearch.Query("", "", adCode);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
		query.setPageSize(10);// 设置每页最多返回多少条poiitem
		query.setPageNum(0);// 设置查第一页

		searchType = 0;
		LatLonPoint lp = new LatLonPoint(lat, lon);
		switch (searchType) {
		case 0: {// 所有poi
			query.setLimitDiscount(false);
			query.setLimitGroupbuy(false);
		}
			break;
		case 1: {// 有团购
			query.setLimitGroupbuy(true);
			query.setLimitDiscount(false);
		}
			break;
		case 2: {// 有优惠
			query.setLimitGroupbuy(false);
			query.setLimitDiscount(true);
		}
			break;
		case 3: {// 有团购或者优惠
			query.setLimitGroupbuy(true);
			query.setLimitDiscount(true);
		}
			break;
		}

		if (lp != null) {
			poiSearch = new PoiSearch(this, query);
			poiSearch.setOnPoiSearchListener(this);
			poiSearch.setBound(new SearchBound(lp, 2000, true));//
			// 设置搜索区域为以lp点为圆心，其周围2000米范围
			/*
			 * List<LatLonPoint> list = new ArrayList<LatLonPoint>();
			 * list.add(lp);
			 * list.add(AMapUtil.convertToLatLonPoint(Constants.BEIJING));
			 * poiSearch.setBound(new SearchBound(list));// 设置多边形poi搜索范围
			 */
			poiSearch.searchPOIAsyn();// 异步搜索
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
		mapView.onResume();
		System.out.println("------------onResume-------------->");
	}

	@Override
	public void onPause() {
		super.onPause();
		mapView.onPause();
		System.out.println("------------onPause-------------->");
		deactivate();
	}

	/**
	 * 返回
	 * 
	 * @param view
	 */
	public void back_onClick(View view) {
		finish();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (list.size() == 0 || list.isEmpty()) {
			} else {
				list.clear();
				adapter.removeAllDatas();
			}

		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

		switch (arg0.getId()) {
		case R.id.now:
			try {
				locationMarker.remove();
				isonClick = true;
				initAmap();
				adapter.notifyDataSetChanged();
			} catch (Exception e) {
				// TODO: handle exception
			}

			break;

		default:
			break;
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		// TODO Auto-generated method stub
		if (amapLocation != null && mListener != null) {
			geoLat = amapLocation.getLatitude();
			geoLng = amapLocation.getLongitude();
			mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
			UiSettings uiSettings = aMap.getUiSettings();
			uiSettings.setZoomControlsEnabled(true); // 缩放的按钮
			uiSettings.setZoomGesturesEnabled(true); // 手势的缩放
			uiSettings.setMyLocationButtonEnabled(false);
			uiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_LEFT);
			aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
			aMap.setMyLocationEnabled(true);
			aMap.setMyLocationRotateAngle(180);
			aMap.setMapType(AMap.MAP_TYPE_NORMAL);
			markerAndMoveMap(geoLat, geoLng);
			// locationMarker = aMap.addMarker(new MarkerOptions()
			// .anchor(0.5f, 1)
			// .icon(BitmapDescriptorFactory
			// .fromResource(R.drawable.point))
			// .position(new LatLng(lp.getLatitude(), lp.getLongitude()))
			// .title("西单为中心点，查其周边"));
			// locationMarker.showInfoWindow();
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

		String newText = s.toString().trim();
		Inputtips inputTips = new Inputtips(MyLocationActivity.this,
				new InputtipsListener() {

					@Override
					public void onGetInputtips(List<Tip> tipList, int rCode) {
						System.out.println("---------rCode------------->"
								+ rCode);
						if (rCode == 0) {// 正确返回
							if (searchList != null || !searchList.isEmpty()) {
								searchList.clear();
								searchAdapter.removeAllDatas();
								searchAdapter.notifyDataSetChanged();
							}
							fl_map.setVisibility(View.GONE);
							listVIew.setVisibility(View.GONE);
							listVIewSearch.setVisibility(View.VISIBLE);
							searchList.addAll(tipList);
							searchAdapter.addAllDatas(searchList);
							searchAdapter.notifyDataSetChanged();
						}
					}
				});
		try {
			inputTips.requestInputtips(newText, "");// 第一个参数表示提示关键字，第二个参数默认代表全国，也可以为城市区号

		} catch (AMapException e) {
			e.printStackTrace();
		}

	}

	/**
	 * poi没有搜索到数据，返回一些推荐城市的信息
	 */
	private void showSuggestCity(List<SuggestionCity> cities) {
		// String infomation = "推荐城市\n";
		// for (int i = 0; i < cities.size(); i++) {
		// infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
		// + cities.get(i).getCityCode() + "城市编码:"
		// + cities.get(i).getAdCode() + "\n";
		// }
		ToastUtil.show(MyLocationActivity.this, "没有搜索到数据");

	}

	@Override
	public void onPoiItemDetailSearched(PoiItemDetail arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@SuppressLint("ResourceAsColor")
	class ListAdapter extends BaseAdapter {
		private List<PoiItem> datas = new ArrayList<PoiItem>();

		/**
		 * 追加数据
		 * 
		 * @param list
		 */
		public void addAllDatas(List<PoiItem> list) {
			datas.addAll(list);
			notifyDataSetChanged();
		}

		/**
		 * 移除某个数据
		 * 
		 * @param position
		 */
		public void removeDatas(int position) {
			datas.remove(position);
			notifyDataSetChanged();
		}

		public List<PoiItem> getDatas() {
			return datas;
		}

		/**
		 * 移除某个数据
		 */
		public void removeAllDatas() {
			datas.clear();
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return datas.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			// return datas.get(arg0);
			return datas.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@SuppressLint("ResourceAsColor")
		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(MyLocationActivity.this,
						R.layout.activity_city_item, null);
				holder.tv_location = (TextView) convertView
						.findViewById(R.id.tv_location);
				holder.tv_address = (TextView) convertView
						.findViewById(R.id.tv_address);
				holder.iv_location = (ImageView) convertView
						.findViewById(R.id.iv_location);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final PoiItem poiItem = datas.get(position);

			if (position == 0) {
				holder.iv_location.setImageResource(R.drawable.red_location);
				holder.tv_location.setTextColor(Color.RED);
				holder.tv_address.setTextColor(Color.RED);
			} else {
				holder.iv_location.setImageResource(R.drawable.hui_location);
				holder.tv_location.setTextColor(Color.parseColor("#333333"));
				holder.tv_address.setTextColor(Color.parseColor("#666666"));
			}

			holder.tv_location.setText(poiItem.getTitle());
			holder.tv_address.setText(poiItem.getSnippet());

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent data = new Intent();
					Bundle bundle = new Bundle();
					bundle.putDouble("LNG", poiItem.getLatLonPoint()
							.getLongitude());
					bundle.putDouble("LAT", poiItem.getLatLonPoint()
							.getLatitude());
					bundle.putString("address", poiItem.getTitle());
					data.putExtra("bundle", bundle);
					setResult(RESULT_OK, data);
					finish();
				}
			});
			return convertView;
		}
	}

	static class ViewHolder {
		TextView tv_location;
		TextView tv_address;
		ImageView iv_location;
	}

	@Override
	public void onPoiSearched(PoiResult result, int rCode) {
		// TODO Auto-generated method stub

		if (rCode == 0) {
			if (result != null && result.getQuery() != null) {// 搜索poi的结果
				if (result.getQuery().equals(query)) {// 是否是同一条
					poiResult = result;
					// 取得搜索到的poiitems有多少页
					List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
					List<SuggestionCity> suggestionCities = poiResult
							.getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息

					if (poiItems != null && poiItems.size() > 0) {
						// aMap.clear();// 清理之前的图标
						// PoiOverlay poiOverlay = new PoiOverlay(aMap,
						// poiItems);
						// poiOverlay.removeFromMap();
						// poiOverlay.addToMap();
						// poiOverlay.zoomToSpan();

						list.addAll(poiItems);
						adapter.addAllDatas(list);
					} else if (suggestionCities != null
							&& suggestionCities.size() > 0) {
						showSuggestCity(suggestionCities);
					} else {
						ToastUtil.show(MyLocationActivity.this,
								R.string.no_result);
					}
				}
			} else {
				ToastUtil.show(MyLocationActivity.this, R.string.no_result);
			}
		} else if (rCode == 27) {
			ToastUtil.show(MyLocationActivity.this, R.string.error_network);
		} else if (rCode == 32) {
			ToastUtil.show(MyLocationActivity.this, R.string.error_key);
		} else {
			ToastUtil.show(MyLocationActivity.this,
					getString(R.string.error_other) + rCode);
		}

	}

	// private MyLocationStyle myLocationStyle;

	@Override
	public void activate(OnLocationChangedListener listener) {
		// TODO Auto-generated method stub
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
			/*
			 * mAMapLocManager.setGpsEnable(false);
			 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
			 * API定位采用GPS和网络混合定位方式
			 * ，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
			 */
			mAMapLocationManager.requestLocationUpdates(
					LocationProviderProxy.AMapNetwork, 2000, 10, this);
		}
	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destory();
		}
		mAMapLocationManager = null;
	}

	@SuppressLint("ResourceAsColor")
	class SearchAdapter extends BaseAdapter {
		private List<Tip> datas = new ArrayList<Tip>();

		/**
		 * 追加数据
		 * 
		 * @param list
		 */
		public void addAllDatas(List<Tip> list) {
			datas.addAll(list);
			notifyDataSetChanged();
		}

		/**
		 * 移除某个数据
		 * 
		 * @param position
		 */
		public void removeDatas(int position) {
			datas.remove(position);
			notifyDataSetChanged();
		}

		public List<Tip> getDatas() {
			return datas;
		}

		/**
		 * 移除某个数据
		 */
		public void removeAllDatas() {
			datas.clear();
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return datas.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			// return datas.get(arg0);
			return datas.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@SuppressLint("ResourceAsColor")
		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(MyLocationActivity.this,
						R.layout.activity_city_item, null);
				holder.tv_location = (TextView) convertView
						.findViewById(R.id.tv_location);
				holder.tv_address = (TextView) convertView
						.findViewById(R.id.tv_address);
				holder.iv_location = (ImageView) convertView
						.findViewById(R.id.iv_location);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final Tip tip = datas.get(position);
			holder.iv_location.setImageResource(R.drawable.hui_location);
			holder.tv_location.setText(tip.getName());
			holder.tv_address.setText(tip.getDistrict());

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// String s = tip.getAdcode().substring(0, 2);
					// System.out
					// .println("---------------tip.getAdcode().substring(0, 2)----------------->"
					// + tip.getAdcode().substring(0, 2));
					name = tip.getName();
					GeocodeQuery query = new GeocodeQuery(tip.getName(), tip
							.getAdcode());// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
					geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
				}
			});
			return convertView;
		}
	}

	@Override
	public void onGeocodeSearched(GeocodeResult result, int rCode) {
		// TODO Auto-generated method stub
		if (rCode == 0) {
			if (result != null && result.getGeocodeAddressList() != null
					&& result.getGeocodeAddressList().size() > 0) {
				GeocodeAddress address = result.getGeocodeAddressList().get(0);
				aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
						AMapUtil.convertToLatLng(address.getLatLonPoint()), 15));
				System.out
						.println("------------address.getLatLonPoint()------------------>"
								+ address.getLatLonPoint());
				Intent data = new Intent();
				Bundle bundle = new Bundle();
				bundle.putDouble("LNG", address.getLatLonPoint().getLongitude());
				bundle.putDouble("LAT", address.getLatLonPoint().getLatitude());
				bundle.putString("address", name);
				data.putExtra("bundle", bundle);
				setResult(RESULT_OK, data);
				finish();
			} else {
				ToastUtil.show(MyLocationActivity.this, "该地方的经纬度未获取到，请重新选择！");
			}

		} else if (rCode == 27) {
			// ToastUtil.show(MyLocationActivity.this, R.string.error_network);
		} else if (rCode == 32) {
			// ToastUtil.show(MyLocationActivity.this, R.string.error_key);
		} else {
			// ToastUtil.show(MyLocationActivity.this,
			// getString(R.string.error_other) + rCode);
		}
	}

	@Override
	public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
		// TODO Auto-generated method stub
		if (rCode == 0) {
			if (result != null && result.getRegeocodeAddress() != null
					&& result.getRegeocodeAddress().getFormatAddress() != null) {
				adCode = result.getRegeocodeAddress().getAdCode();
			} else {
			}
		} else if (rCode == 27) {
		} else if (rCode == 32) {
		} else {
		}
	}
}
