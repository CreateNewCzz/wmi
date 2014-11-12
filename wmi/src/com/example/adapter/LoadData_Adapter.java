package com.example.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aeal.pullrefreshlistview.MyListView;
import com.example.bean.Show_info_bean;
import com.example.config.Config;
import com.example.task.DelItemTask;
import com.example.task.InputImgforSdcar;
import com.example.task.ZanStateTask;
import com.example.wmi.R;

public class LoadData_Adapter extends BaseAdapter {
	private ArrayList<Show_info_bean> datas;
	private Context context;
	private ViewHolder holder = null;
	private PopupWindow popupWindow;
	private String relation, aid;
	private MyListView main_lv;
	int position;
	private String[] l_ = { "l_1", "l_2", "l_3", "l_4", "l_5", "l_6", "l_7",
			"l_8", "l_9", "l_10", "l_11", "l_12", "l_13", "l_14", "l_15",
			"l_16", "l_17", "l_18", "l_19", "l_20", "l_21", "l_22", "l_23",
			"l_24", "l_25", "l_26", "l_27", "l_28", "l_29", "l_30" };

	public LoadData_Adapter(ArrayList<Show_info_bean> datas, MyListView main_lv) {
		this.datas = datas;
		this.main_lv = main_lv;
	}

	public int getCount() {
		return datas.size();
	}

	public Show_info_bean getItem(int position) {

		return datas.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("deprecation")
	public View getView(int position, View convertView, final ViewGroup parent) {
		context = parent.getContext();
		this.position = position;
		initPopWindow();

		if (convertView == null) {
			holder = new ViewHolder();

			convertView = LayoutInflater.from(context).inflate(
					R.layout.show_item, null);

			holder.background = (ImageView) convertView
					.findViewById(R.id.show_background);

			holder.action_more = (ImageView) convertView
					.findViewById(R.id.show_action_more);

			holder.check_zan = (CheckBox) convertView
					.findViewById(R.id.check_zan);

			holder.text = (TextView) convertView
					.findViewById(R.id.show_content);
			holder.report_num = (TextView) convertView
					.findViewById(R.id.show_report);

			holder.relationShip = (TextView) convertView
					.findViewById(R.id.show_relationShip);
			holder.relative = (RelativeLayout) convertView
					.findViewById(R.id.show_relative);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.action_more.setOnClickListener(new popAction(position));
		holder.check_zan.setOnClickListener((new CheckZan(position)));

		holder.check_zan.setText(datas.get(position).getZan());
		holder.text.setText(datas.get(position).getText());
		holder.report_num.setText(datas.get(position).getReport());
		holder.relationShip.setText(datas.get(position).getRelationShip());

		int temp = ((Activity) context).getWindowManager().getDefaultDisplay()
				.getHeight() * 3 / 5;

		LayoutParams relativeParams = holder.relative.getLayoutParams();
		LayoutParams imgparams = holder.background.getLayoutParams();

		relativeParams.height = temp;
		imgparams.height = temp;

		holder.relative.setLayoutParams(relativeParams);
		holder.background.setLayoutParams(imgparams);
		String img_name = datas.get(position).getImg_name();

		if (img_name.indexOf("l_") == 0) {
			int count = 0;
			for (int i = 0; i < l_.length; i++) {
				if (img_name.equals(l_[i])) {
					count = i;
				}
			}
			holder.background.setImageResource(Config.l_[count]);
		} else {
			new InputImgforSdcar(holder.background).execute(img_name);
		}

		if ("no".equals(datas.get(position).getZan_state())) {
			holder.check_zan.setChecked(false);
		} else {
			holder.check_zan.setChecked(true);

		}

		return convertView;
	}

	private class CheckZan implements OnClickListener {
		String aid;
		int position;
		String account = context.getSharedPreferences("is_start.xml", 0)
				.getString("username", "");

		public CheckZan(int position) {
			aid = datas.get(position).getAid();
			this.position = position;
		}

		public void onClick(View v) {
			ZanStateTask stateTask = new ZanStateTask(context);

			CheckBox box = (CheckBox) v;
			String value = (String) box.getText();
			int v1 = Integer.valueOf(value);

			if (((CheckBox) v).isChecked()) {
				stateTask.execute(aid, "up", account);
				box.setText((v1 + 1) + "");
				datas.get(position).setZan_state("yes");

				datas.get(position).setZan(
						(Integer.valueOf(datas.get(position).getZan()) + 1)
								+ "");

			} else {
				stateTask.execute(aid, "down", account);
				box.setText((v1 - 1) + "");
				datas.get(position).setZan_state("no");

				datas.get(position).setZan(
						(Integer.valueOf(datas.get(position).getZan()) - 1)
								+ "");

			}

		}
	}

	private void initPopWindow() {

		ViewHolder holder = new ViewHolder();
		View popView = LayoutInflater.from(context).inflate(
				R.layout.more_function, null);
		popupWindow = new PopupWindow(popView, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		popupWindow.setBackgroundDrawable(new ColorDrawable(0));

		holder.tv_del = (TextView) popView.findViewById(R.id.more_tv3);

		holder.tv_del.setOnClickListener(new delClick());

	}

	class delClick implements OnClickListener {

		public void onClick(View v) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);

			builder.setMessage("删除后不可撤销，确定删除？");
			builder.setNegativeButton("确定",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

							new DelItemTask(context, datas, main_lv).execute(
									relation, aid);

						}
					});
			builder.setNeutralButton("取消", null);
			builder.create().show();

			popupWindow.dismiss();

		}

	}

	public void showPop(View parent, int x, int y, int postion) {
		// 设置popwindow显示位置
		popupWindow.showAtLocation(parent, 0, x, y);
		// 获取popwindow焦点
		popupWindow.setFocusable(true);
		// 设置popwindow如果点击外面区域，便关闭。
		popupWindow.setOutsideTouchable(true);
		popupWindow.update();
		if (popupWindow.isShowing()) {

		}

	}

	private class popAction implements OnClickListener {
		int position;

		public popAction(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			getItemRelation();
			int[] arrayOfInt = new int[2];
			// 获取点击按钮的坐标
			v.getLocationOnScreen(arrayOfInt);
			int x = arrayOfInt[0];
			int y = arrayOfInt[1];
			showPop(v, x - 10, y - 70, position);
		}

		private void getItemRelation() {
			relation = datas.get(position).getRelationShip();
			aid = datas.get(position).getAid();
		}

	}

	private class ViewHolder {
		TextView text, tv_del;
		TextView relationShip;
		TextView report_num;
		TextView show_zan;
		ImageView action_more, background;
		CheckBox check_zan;
		RelativeLayout relative;

	}

}
