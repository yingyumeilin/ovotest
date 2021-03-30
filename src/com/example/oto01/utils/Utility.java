package com.example.oto01.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Utility {
public static void setListViewHeightBasedOnChildren(ListView listView) {
//获取ListView对应的Adapter
ListAdapter listAdapter = listView.getAdapter();
if (listAdapter == null) {
// pre-condition
return;
}

int totalHeight = 0;
for (int i = 0, len = listAdapter.getCount(); i < len; i++) { //listAdapter.getCount()返回数据项的数目
View listItem = listAdapter.getView(i, null, listView);
listItem.measure(0, 0); //计算子项View 的宽�?
totalHeight += listItem.getMeasuredHeight(); //统计�?��子项的�?高度
}

ViewGroup.LayoutParams params = listView.getLayoutParams();
params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//listView.getDividerHeight()获取子项间分隔符占用的高�?
//params.height�?��得到整个ListView完整显示�?��的高�?
listView.setLayoutParams(params);
}
}
