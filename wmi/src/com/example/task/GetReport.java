package com.example.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.adapter.ReportAdapter;
import com.example.bean.ReportBean;
import com.example.config.Config;

import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;

public class GetReport extends AsyncTask<String, Void, Integer> {
	StringBuffer buf;
	private ListView reportinfo;
	public static ReportAdapter reportAdapter;

	private ArrayList<ReportBean> datas;

	public GetReport(ListView reportinfo, ArrayList<ReportBean> datas,
			ReportAdapter reportAdapter) {
		this.reportinfo = reportinfo;
		this.datas = datas;
		this.reportAdapter = reportAdapter;

	}

	protected Integer doInBackground(String... params) {

		// params[0] 获取文章的 的account params[1] 获取文章的 aid

		try {
			URL url = new URL(Config.KEY_URL);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(5000);
			conn.setRequestMethod("POST");


			conn.setRequestProperty(Config.REQUEST_TYPE, "get_report");
			conn.setRequestProperty(Config.KEY_ACCOUNT, params[0]);
			conn.setRequestProperty(Config.KEY_AID, params[1]);

			int get_report_state = conn.getHeaderFieldInt(
					Config.KEY_GET_REPORT_STATE, 1);

			InputStream in = conn.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line;
			buf = new StringBuffer();
			while ((line = br.readLine()) != null) {
				buf.append(line);
			}
			br.close();

			return get_report_state;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private boolean jsonReport() throws JSONException {

		JSONObject jsonObject = new JSONObject(buf.toString());
		JSONArray jArr = jsonObject.getJSONArray("reports");
		if (buf != null) {

			for (int i = 0; i < jArr.length(); i++) {
				String report_zan_state = jArr.getJSONObject(i).getString(
						"report_zan_state");
				String number = jArr.getJSONObject(i).getString("number");

				String report_text = jArr.getJSONObject(i).getString(
						"report_text");

				String host = jArr.getJSONObject(i).getString("host");

				String rid = jArr.getJSONObject(i).getString("rid");

				String aid = jArr.getJSONObject(i).getString("aid");

				String report_zan = jArr.getJSONObject(i).getString(
						"report_zan");

				datas.add(new ReportBean(report_zan_state, number, report_text,
						host, rid, aid, report_zan));

			}

		}

		if (datas.size() == jArr.length()) {
			return true;
		}

		return false;

	}

	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);

		if (result == 9000) {
			System.out.println("评论响应码>>>>>" + "成功");

			try {
				if (jsonReport()) {
					reportAdapter = new ReportAdapter(datas);
					reportinfo.setAdapter(reportAdapter);

					// 设置listview的高度
					setListViewHeightBasedOnChildren(reportinfo);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

		} else if (result == 9001) {
			System.out.println("评论响应码>>>>>" + "失败,参数错误");

		} else if (result == 9002) {
			System.out.println("评论响应码>>>>>" + "失败，服务端错误");

		} else {
			System.out.println("评论响应码>>>>>" + "失败" + result);

		}

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
