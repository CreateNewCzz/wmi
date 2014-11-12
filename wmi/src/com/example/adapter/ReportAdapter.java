package com.example.adapter;

import java.util.ArrayList;
import java.util.Random;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bean.ReportBean;
import com.example.config.Config;
import com.example.wmi.R;

public class ReportAdapter extends BaseAdapter {
	private ArrayList<ReportBean> datas;
	private boolean flag = false;

	public ReportAdapter(ArrayList<ReportBean> datas) {
		this.datas = datas;
	}

	public int getCount() {
		return datas.size();
	}

	public ReportBean getItem(int position) {
		return datas.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.report_item, parent, false);

			holder.rep_content = (TextView) convertView
					.findViewById(R.id.rep_content);

			holder.rep_photo = (ImageView) convertView
					.findViewById(R.id.rep_photo);

			holder.rep_zan = (TextView) convertView.findViewById(R.id.rep_zan);

			holder.rep_img = (ImageView) convertView.findViewById(R.id.rep_img);

			holder.rep_img1 = (ImageView) convertView
					.findViewById(R.id.rep_img1);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.rep_content.setText(datas.get(position).getReport_text());
		holder.rep_zan.setText(datas.get(position).getReport_zan());

		// 评论 换至图标....
		String isHost = datas.get(position).getHost();
		if ("yes".equals(isHost)) {
			holder.rep_photo.setImageResource(R.drawable.report_use_primary);
		} else {

			int temp = new Random().nextInt(Config.report_use_icon.length);
			holder.rep_photo.setImageResource(Config.report_use_icon[temp]);
			datas.get(position).setIcon(temp);

			for (int i = 0; i < position; i++) {
				if (datas.get(position).getNumber()
						.equals(datas.get(i).getNumber())) {
					datas.get(position).setIcon(datas.get(i).getIcon());
					holder.rep_photo
							.setImageResource(Config.report_use_icon[datas.get(
									i).getIcon()]);
				}
			}

		}

		// 评论 赞 初始化 以及监听事件

		if ("no".equals(datas.get(position).getReport_zan_state())) {
			holder.rep_img1
					.setImageResource(R.drawable.ic_comment_like_gray_big);

		} else {
			holder.rep_img1
					.setImageResource(R.drawable.ic_comment_like_red_big);
		}

		holder.rep_img1.setOnClickListener(new check_zan(position,
				holder.rep_img, holder.rep_zan));

		return convertView;
	}

	private class check_zan implements OnClickListener {
		private int position;
		private ImageView v1;
		TextView rep_zan;

		public check_zan(int position, ImageView v, TextView rep_zan) {
			this.position = position;
			this.v1 = v;
			this.rep_zan = rep_zan;

		}

		public void onClick(View v) {

			if ("no".equals(datas.get(position).getReport_zan_state())) {
				((ImageView) v)
						.setImageResource(R.drawable.ic_comment_like_red_big);

				v1.setImageResource(R.drawable.ic_liked_big);

				datas.get(position).setReport_zan_state("yes");

				rep_zan.setText((Integer.valueOf(rep_zan.getText().toString()) + 1)
						+ "");

			} else if ("yes".equals(datas.get(position).getReport_zan_state())) {

				((ImageView) v)
						.setImageResource(R.drawable.ic_comment_like_gray_big);
				datas.get(position).setReport_zan_state("no");
				v1.setImageResource(R.drawable.ic_like_big);
				rep_zan.setText((Integer.valueOf(rep_zan.getText().toString()) - 1)
						+ "");
			}

		}
	}

	private class ViewHolder {
		TextView rep_zan;
		ImageView rep_photo, rep_img, rep_img1;
		TextView rep_content;
	}
}
