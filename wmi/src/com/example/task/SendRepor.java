package com.example.task;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.example.adapter.ReportAdapter;
import com.example.bean.ReportBean;
import com.example.config.Config;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SendRepor extends AsyncTask<String, Void, Integer> {
	private Context context;
	private EditText report_info;

	private String aid;
	private String account;
	private String text;
	private String rid;
	String relation;
	ArrayList<ReportBean> datas;
	ListView reportinfo;

	public SendRepor(Context context, EditText report_info, String relation,
			ArrayList<ReportBean> datas, ListView reportinfo) {
		this.context = context;
		this.report_info = report_info;
		this.datas = datas;
		this.relation = relation;
		this.reportinfo = reportinfo;

	}

	protected Integer doInBackground(String... params) {
		// 0 aid 1: account 2: text 3: rid

		aid = params[0];
		account = params[1];
		text = params[2];
		rid = params[3];

		try {
			URL url = new URL(Config.KEY_URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(5000);
			conn.setRequestMethod("POST");

			conn.setRequestProperty(Config.REQUEST_TYPE, "set_report");
			conn.setRequestProperty(Config.KEY_AID, aid);
			conn.setRequestProperty(Config.KEY_ACCOUNT, account);
			conn.setRequestProperty("report_text", text);
			conn.setRequestProperty("rid", rid);

			int set_report_state = conn
					.getHeaderFieldInt("set_report_state", 1);
			return set_report_state;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		String res = "";
		if (result == 10000) {
			res = "评论成功";
			report_info.setText("");

			String host = "no";
			if ("自己".equals(relation)) {
				host = "yes";

			}

			System.out.println(datas.size());
			datas.add(new ReportBean("no", "0", text, host, res, res, "0"));
			System.out.println(datas.size());
			GetReport.reportAdapter.notifyDataSetChanged();

			setListViewHeightBasedOnChildren(reportinfo);
		} else if (result == 10001) {
			res = "失败 参数错误";
		} else if (result == 10002) {
			res = "失败 服务端错误";
		} else {
			res = "异常错误" + result;
		}

		Toast.makeText(context, res, Toast.LENGTH_SHORT).show();

	}

	public void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() + 80));
		((MarginLayoutParams) params).setMargins(10, 10, 10, 10);
		listView.setLayoutParams(params);
	}

}
