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

import android.content.Context;
import android.graphics.Point;
import android.os.AsyncTask;

import com.aeal.pullrefreshlistview.MyListView;
import com.example.adapter.LoadData_Adapter;
import com.example.bean.Show_info_bean;
import com.example.config.Config;

public class Data_loading extends AsyncTask<String, Void, Integer> {

	private MyListView main_lv;
	private StringBuffer jsonData;
	public ArrayList<Show_info_bean> datas;
	public static LoadData_Adapter adapter;

	public Data_loading(Context context, MyListView main_lv,
			ArrayList<Show_info_bean> datas) {
		this.main_lv = main_lv;
		this.datas = datas;
	}

	protected Integer doInBackground(String... params) {
		try {
			URL url = new URL(Config.KEY_URL);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setReadTimeout(5000);
			conn.setRequestProperty(Config.REQUEST_TYPE, "get_text");
			conn.setRequestProperty(Config.KEY_ACCOUNT, params[0]);
			conn.setRequestProperty(Config.KEY_ORI_TYPE, params[1]);

			int get_text_state = conn.getHeaderFieldInt(
					Config.KEY_GET_TEXT_STATE, 0);

			InputStream in = conn.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line;
			jsonData = new StringBuffer();

			while ((line = br.readLine()) != null) {
				jsonData.append(line);
			}
			br.close();
			return get_text_state;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * resolveJson:解析从网络获取的额json数据
	 * 
	 * @return
	 * @throws JSONException
	 */
	private void resolveJson() {
		try {
			JSONObject jsonObject = new JSONObject(jsonData.toString());

			JSONArray programmers = jsonObject.getJSONArray("articles");

			for (int i = 0; i < programmers.length(); i++) {
				String img_name = programmers.getJSONObject(i).getString(
						"img_name");

				String zan = programmers.getJSONObject(i).getString("zan");

				String report = programmers.getJSONObject(i)
						.getString("report");

				String zan_state = programmers.getJSONObject(i).getString(
						"zan_state");

				String text = programmers.getJSONObject(i).getString("text");

				String aid = programmers.getJSONObject(i).getString("aid");

				String relationShip = programmers.getJSONObject(i).getString(
						"relationShip");

				datas.add(new Show_info_bean(img_name, zan, report, zan_state,
						text, aid, relationShip));

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);

		if (result != null) {
			if (result == 5000) {
				System.out.println("top  请求成功");
				resolveJson();

				adapter = new LoadData_Adapter(datas,main_lv);
				if (main_lv != null) {
					main_lv.setAdapter(adapter);
				}
				adapter.notifyDataSetChanged();

			} else if (result == 5001) {
				System.out.println("buttom  请求成功");

			} else if (result == 5002) {
				System.out.println("请求失败 缺少参数");

			} else if (result == 5003) {
				System.out.println("请求失败其他原因");

			} else {
				System.out.println("莫名原因 " + result);
			}

		}

	}
}
