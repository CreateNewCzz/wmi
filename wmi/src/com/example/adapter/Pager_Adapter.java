package com.example.adapter;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class Pager_Adapter extends PagerAdapter {
	ArrayList<View> datas;

	public Pager_Adapter(ArrayList<View> datas) {
		this.datas = datas;
	}

	public int getCount() {
		return datas.size();
	}

	public boolean isViewFromObject(View view, Object obj) {
		return view == obj;
	}

	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(datas.get(position));
	}

	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(datas.get(position));
		return datas.get(position);
	}

}
