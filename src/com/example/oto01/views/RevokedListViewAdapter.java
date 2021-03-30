package com.example.oto01.views;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oto01.R;
import com.example.oto01.model.Revoke;


//适配�?
public class RevokedListViewAdapter extends BaseAdapter {

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}
	private LayoutInflater inflater;
	List<Revoke> list ;
	Context c;
	int  currentID=0;
	public RevokedListViewAdapter(Context context,List<Revoke> mRevokedBean) {
		inflater=LayoutInflater.from(context);
		this.c=context;
		this.list=mRevokedBean;
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder myHolder;
		Revoke mRevokedBean = list.get(position);
		if (convertView==null) {
			myHolder=new Holder();
			convertView=inflater.inflate(R.layout.revokedlist_item, null);
			myHolder.tv2=(TextView)convertView.findViewById(R.id.info);
			myHolder.iv=(ImageView)convertView.findViewById(R.id.img);
			myHolder.ed=(EditText)convertView.findViewById(R.id.edit);
			
			convertView.setTag(myHolder);
		}
		else
		{
			myHolder=(Holder)convertView.getTag();
		}
	
		//myHolder.iv.setBackgroundResource((Integer) list.get(position).get("img"));
		if(position==this.currentID) {
			myHolder.iv.setBackgroundResource(R.drawable.checkbox_selected);
		} else {
			myHolder.iv.setBackgroundResource(R.drawable.checkbox_no_selected);
		}
		myHolder.tv2.setText(mRevokedBean.getReason());
		return convertView;
	}
	private class Holder{
		ImageView iv;
		TextView tv2;
		EditText ed;
	}
	public void setCurrentID(int currentID) {
		this.currentID = currentID;
	}

}
