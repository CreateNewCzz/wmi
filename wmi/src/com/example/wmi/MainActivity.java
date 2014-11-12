package com.example.wmi;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aeal.pullrefreshlistview.MyListView;
import com.aeal.pullrefreshlistview.MyListView.OnRefreshListener;
import com.example.adapter.LoadData_Adapter;
import com.example.bean.Show_info_bean;
import com.example.task.Buttom_loading;
import com.example.task.Data_loading;
import com.example.task.Top_loading;

public class MainActivity extends Activity implements OnClickListener,
		OnRefreshListener, OnItemClickListener {

	private MyListView main_lv;
	private ArrayList<Show_info_bean> datas = new ArrayList<Show_info_bean>();

	private TextView foot_text, sele_frend, sele_complete;
	private ProgressBar progressBar;
	private View v;
	private ImageView addMessage, main_more;
	private TextView title_select;
	private String account;
	PopupWindow popwindow;
	boolean flag01 = true;

	private void initPager() {
		main_lv = (MyListView) this.findViewById(R.id.main_lv);

		loadingData();
		v = LayoutInflater.from(this).inflate(R.layout.footerview, null);
		foot_text = (TextView) v.findViewById(R.id.foot_text);
		progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
		addMessage = (ImageView) this.findViewById(R.id.title_send_text);
		main_more = (ImageView) this.findViewById(R.id.title_main_more);
		main_more.setOnClickListener(this);
		addMessage.setOnClickListener(this);
		main_lv.setOnRefreshListener(this);
		setFooterView();
		main_lv.setOnItemClickListener(this);

		title_select = (TextView) this.findViewById(R.id.title_sele);
		title_select.setOnClickListener(this);

	}

	private void initpopuwindow() {

		View view = ((LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.select_option, null);
		popwindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);

		popwindow.setBackgroundDrawable(new ColorDrawable(0));
		popwindow.showAsDropDown(title_select);
		popwindow.setFocusable(true);
		popwindow.setOutsideTouchable(true);
		popwindow.update();

		sele_frend = (TextView) view.findViewById(R.id.select_option_frend);
		sele_frend.setOnClickListener(this);
		sele_complete = (TextView) view.findViewById(R.id.select_option_total);
		sele_complete.setOnClickListener(this);

	}

	// 首次加载数据
	private void loadingData() {

		new Data_loading(this, main_lv, datas).execute(account, "top");
	}

	private void main_more() {

		View view = LayoutInflater.from(this).inflate(R.layout.main_more, null);

		PopupWindow popupWindow = new PopupWindow(view,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(new ColorDrawable(0));
		popupWindow.showAsDropDown(main_more);
		popupWindow.setOutsideTouchable(true);
		popupWindow.update();

	}

	public void onClick(View v) {

		Intent intent = null;

		switch (v.getId()) {

		case R.id.foot_text:
			new Buttom_loading(datas, foot_text, progressBar).execute(account,
					"bottom", datas.get(datas.size() - 1).getAid());

			break;

		case R.id.title_send_text:

			intent = new Intent(this, AddMessage.class);

			break;

		case R.id.title_sele:
			initpopuwindow();
			break;
		case R.id.title_main_more:
			main_more();
			break;
		case R.id.select_option_frend:

			title_select.setText(sele_frend.getText().toString());
			popwindow.dismiss();
			ArrayList<Show_info_bean> data = new ArrayList<Show_info_bean>();

			for (Show_info_bean bean : datas) {
				if ("朋友".equals(bean.getRelationShip())) {
					data.add(bean);
				}
			}
			main_lv.setAdapter(new LoadData_Adapter(data, main_lv));
			break;
		case R.id.select_option_total:
			title_select.setText(sele_complete.getText().toString());
			main_lv.setAdapter(new LoadData_Adapter(datas, main_lv));
			popwindow.dismiss();
			break;

		}
		if (intent != null) {
			startActivity(intent);
		}
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SharedPreferences settings = getSharedPreferences("is_start.xml", 0);
		account = settings.getString("username", "");
		String start = settings.getString("start", "");
		boolean b = Boolean.valueOf(start);
		if (b) {
			requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
			setContentView(R.layout.activity_main);
			getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
					R.layout.title_bar);
			initPager();
		} else {
			this.finish();
			startActivity(new Intent(this, Welcome.class));
		}

	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position <= datas.size()) {
			Intent intent = new Intent(MainActivity.this, report.class);
			Bundle bundle = new Bundle();
			bundle.putInt("position", position);
			bundle.putSerializable("data", datas);
			intent.putExtras(bundle);
			startActivity(intent);
		}
	}

	// 下拉刷新
	public void onRefresh() {
		datas = new ArrayList<Show_info_bean>();
		new Top_loading(datas, main_lv).execute(account, "top");
	}

	private void setFooterView() {

		main_lv.addFooterView(v);
		foot_text.setOnClickListener(this);

	}

	public ArrayList<Show_info_bean> getDatas() {
		return datas;
	}

	@Override
	protected void onRestart() {
		super.onRestart();

		datas = new ArrayList<Show_info_bean>();
		new Data_loading(this, main_lv, datas).execute(account, "top");

	}

}
