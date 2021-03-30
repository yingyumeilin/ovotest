package com.example.oto01;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.oto01.imageload.ImgLoad;
import com.example.oto01.model.Consumer;
import com.example.oto01.model.Login;
import com.example.oto01.model.ShopInfo;
import com.example.oto01.services.CommonService;
import com.example.oto01.services.GoodService;
import com.example.oto01.services.LoginManager;
import com.example.oto01.utils.DateUtil;
import com.example.oto01.utils.NetConn;
import com.example.oto01.utils.ToastUtil;
import com.example.oto01.views.PullRefreshView;
import com.example.oto01.views.PullRefreshView.OnFooterRefreshListener;
import com.example.oto01.views.PullRefreshView.OnHeaderRefreshListener;
/**
 * 店铺详情界面
 * @author liqq
 *
 */
public class ShopDetailsActivity extends BaseActivity implements OnHeaderRefreshListener,OnFooterRefreshListener{
	private ImageView shopLogoImageView;
	private TextView shopNameTextView ,addressTextView,commonNumTextView ,collectNumTextView;
	private RatingBar levelBar;
	private ListView commonListView;
	private PullRefreshView pullRefreshView;
	private static final int UPLOAD_SHOP_DETAILS = 3;//下载店铺详情信息
	private static final int UPLOAD_COMMON_LIST = 4;//下载评论列表
	 //手指在屏幕上移动距离小于此值不会被认为是手势  
    private static final int FLING_MIN_DISTANCE = 120;  
    //手指在屏幕上移动速度小于此值不会被认为手势  
    private static final int FLING_MIN_VELOCITY = 300;  
	private static final int HEADER = 0;
	private static final int NEWEST = 1;
	private static final int FOOTER = 2;
	private int current_page = 1;
	private int total_page = 0;
	private List<Consumer> list = new ArrayList<Consumer>();
	private MyAdapter adapter;
	private Dialog dialog;
	
	private int shopsid;
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPLOAD_SHOP_DETAILS:
				ShopInfo shopInfo = (ShopInfo)msg.obj;
				if(shopInfo != null){
					System.out.println("------shopInfo---->"+shopInfo);
					shopNameTextView.setText(shopInfo.getShopsname());
					addressTextView.setText(shopInfo.getAddress());
//					commonNumTextView.setText(shopInfo.getConnum());
//					collectNumTextView.setText(shopInfo.getAttnum());
					if((float)Math.rint(shopInfo.getLevel())==0){
						levelBar.setRating(1);
					}else{
						levelBar.setRating((float)Math.rint(shopInfo.getLevel()));
					}
					initShopCommonListData(NEWEST);
				}
				break;

			default:
				List<Consumer> list = (List<Consumer>) msg.obj;
				statusFilter(list, msg.what, msg);
				break;
			}
		};
	};
	
	/**
	 * 通过数据类型显示数据
	 * @param newest
	 * @param type
	 * @param msg
	 */
	private void statusFilter(List<Consumer> newest, int type, Message msg) {
		if (newest != null && !newest.isEmpty()) {
			if (type == HEADER || type == NEWEST) {
				list.clear();
				list.addAll(newest);
			} else if (type == FOOTER) {
				int over = msg.getData().getInt("over", -1);
				if (over == 0) {
					ToastUtil.show(ShopDetailsActivity.this, "已经到底部了");
				} else if (over == -1) {
					list.addAll(newest);
					System.out.println("-----加载新数据----" + newest);
				}
			}
			adapter.notifyDataSetChanged();
			if (type == HEADER) {
				pullRefreshView.onHeaderRefreshComplete();
			} else if (type == FOOTER) {
				pullRefreshView.onFooterRefreshComplete();
			}
		} else {
			list.clear();
			adapter.notifyDataSetChanged();
			ToastUtil.show(this, "没有要显示的数据");
		}
		dialog.dismiss();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_details);
		dialog = new Dialog(ShopDetailsActivity.this, R.style.theme_dialog_alert);
		dialog.setContentView(R.layout.window_layout);
		dialog.show();
		LoginManager lm = LoginManager.getInstance(ShopDetailsActivity.this);
		Login login = lm.getLoginInstance();
		 if (login != null && login.getAdminId() != -1) {
			 shopsid = login.getAdminId();
		 }
		 shopsid = 47756;
		initView();
		initShopDetailsData();
	}
	
	/**
	 * 初始化界面
	 */
	private void initView(){
		shopLogoImageView = (ImageView) findViewById(R.id.shop_details_logo_imageview);
		shopNameTextView = (TextView) findViewById(R.id.shop_name_textview);
		levelBar = (RatingBar) findViewById(R.id.shop_details_ratingbar);
		addressTextView = (TextView) findViewById(R.id.shop_details_address);
		commonNumTextView = (TextView) findViewById(R.id.comment_num_textview);
		collectNumTextView = (TextView) findViewById(R.id.collect_num_textview);
		commonListView = (ListView) findViewById(R.id.lsitview);
		RelativeLayout ll = (RelativeLayout)findViewById(R.id.RelativeLayout123);
		ll.setLongClickable(true);
		ll.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				 return mVfDetector.onTouchEvent(event);  
			}
		});
		pullRefreshView = (PullRefreshView) findViewById(R.id.comment_list);
		pullRefreshView.setOnHeaderRefreshListener(this);
		pullRefreshView.setOnFooterRefreshListener(this);
		adapter = new MyAdapter(list);
		adapter.notifyDataSetChanged();
		commonListView.setAdapter(adapter);
	}
	
	
	private void initShopDetailsData(){
		if(!NetConn.checkNetwork(ShopDetailsActivity.this)){
//			NetConn.setNetwork(OrdersActivity.this);
		}else{
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					GoodService goodService = new GoodService(ShopDetailsActivity.this);
					ShopInfo shopInfo = goodService.getShopInfoDetails(shopsid);
					Message message  = handler.obtainMessage(UPLOAD_SHOP_DETAILS);
					message.obj = shopInfo;
					handler.sendMessage(message);
				}
			}).start();
		}
	}
	/**
	 * 下载评论列表数据
	 */
	private void initShopCommonListData(final int position){
		if(!NetConn.checkNetwork(ShopDetailsActivity.this)){
//			NetConn.setNetwork(OrdersActivity.this);
		}else{
			dialog.show();
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					CommonService goodService = new CommonService(ShopDetailsActivity.this);
					Message msg = handler.obtainMessage(position);
					Bundle bundle = new Bundle();
					List<Consumer> list = new ArrayList<Consumer>();
					System.out.println("---current_page->" + current_page
							+ "--total_page-->" + total_page);
					if (position == HEADER || position == NEWEST) {
						list = goodService.getConsumers(shopsid,0,1,1);
					} else if (position == FOOTER) {
						if (current_page >= total_page) {
							bundle.putInt("over", 0);
							current_page = total_page;
						} else if (current_page < total_page) {
							current_page++;
						}
						list = goodService.getConsumers(shopsid,0,1,current_page);
					}
					System.out.println("----list----->" + list);
					current_page = goodService.p;
					total_page = goodService.total_page;
					System.out.println("---current_page->" + current_page
							+ "--total_page-->" + total_page);
					msg.obj = list;
					msg.setData(bundle);
					handler.sendMessage(msg);
				}
			}).start();
		}
	}

	private class MyAdapter extends BaseAdapter{
		private List<Consumer> myList;
		private LayoutInflater inflater;
		public MyAdapter(List<Consumer> list){
			this.myList = list;
			inflater = LayoutInflater.from(ShopDetailsActivity.this);
		}
		
		@Override
		public int getCount() {
			return myList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return myList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if(view == null){
				view = inflater.inflate(R.layout.commodity_comment_listview_item, parent,false);
				viewHolder = new ViewHolder();
				viewHolder.logoImageView = (ImageView) view.findViewById(R.id.head_portrait_imageview);
				viewHolder.nameTextView =(TextView) view.findViewById(R.id.username);
				viewHolder.contentTextView =(TextView) view.findViewById(R.id.context);
				viewHolder.timeTextView =(TextView) view.findViewById(R.id.time);
				view.setTag(viewHolder);
			}
			viewHolder = (ViewHolder)view.getTag();
			Consumer consumer = myList.get(position);
			ImgLoad load = ImgLoad.getInstance();
			load.addTask(consumer.getAvatarpath(), viewHolder.logoImageView);
			load.doTask();
			viewHolder.nameTextView.setText(consumer.getNickname());
			viewHolder.contentTextView.setText(consumer.getContent());
			viewHolder.timeTextView.setText(DateUtil.getFormatedDate_5(consumer.getAddtime()));
			return view;
		}
		
	}
	
	private class ViewHolder{
		ImageView logoImageView;
		TextView nameTextView;
		TextView contentTextView;
		TextView timeTextView;
	}
	
	@SuppressWarnings({"deprecation" })
	private GestureDetector mVfDetector = new GestureDetector(new OnGestureListener() {
		
		@Override
		public boolean onSingleTapUp(MotionEvent arg0) {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public void onShowPress(MotionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
				float arg3) {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public void onLongPress(MotionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
//		    final int FLING_MIN_DISTANCE = 60, FLING_MIN_VELOCITY = 400; 
	        if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) { 
	            // Fling left  
	            Log.i("MyGesture", "Fling left"); 
	        } else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) { 
	            // Fling right  
	            Log.i("MyGesture", "Fling right"); 
	        } else if(e2.getY()-e1.getY()>FLING_MIN_DISTANCE && Math.abs(velocityY)>FLING_MIN_VELOCITY) {
	            // Fling down  
	        	findViewById(R.id.view_pager_relative).setVisibility(View.VISIBLE);
	            Log.i("MyGesture", "Fling down"); 
	        } else if(e1.getY()-e2.getY()>FLING_MIN_DISTANCE && Math.abs(velocityY)>FLING_MIN_VELOCITY) {
	            // Fling up  
	        	findViewById(R.id.view_pager_relative).setVisibility(View.GONE);
//	        	findViewById(R.id.view_pager_relative).startAnimation(mHideAnimation);
	            Log.i("MyGesture", "Fling up"); 
	        } 
			return false;
		}
		
		@Override
		public boolean onDown(MotionEvent arg0) {
			// TODO Auto-generated method stub
			return false;
		}
	});


	@Override
	public void onFooterRefresh(PullRefreshView view) {
		// TODO Auto-generated method stub
		initShopCommonListData(FOOTER);
	}

	@Override
	public void onHeaderRefresh(PullRefreshView view) {
		// TODO Auto-generated method stub
		initShopCommonListData(HEADER);
	}
}
