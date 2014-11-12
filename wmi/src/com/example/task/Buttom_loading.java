package com.example.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bean.Show_info_bean;
import com.example.config.Config;

public class Buttom_loading extends AsyncTask<String, Void, Integer> {
	private ArrayList<Show_info_bean> datas;
	private StringBuffer jsonData;
	TextView foot_text;
	ProgressBar progressBar;

	public Buttom_loading(ArrayList<Show_info_bean> datas, TextView foot_text,
			ProgressBar progressBar) {
		this.datas = datas;
		this.foot_text = foot_text;
		this.progressBar = progressBar;
	}

	protected void onPreExecute() {
		super.onPreExecute();
		foot_text.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
	}

	protected Integer doInBackground(String... params) {

		try {
			URL url = new URL(Config.KEY_URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(5000);
			conn.setRequestMethod("POST");
			conn.setRequestProperty(Config.REQUEST_TYPE, "get_text");
			conn.setRequestProperty(Config.KEY_ACCOUNT, params[0]);
			conn.setRequestProperty(Config.KEY_ORI_TYPE, params[1]);
			conn.setRequestProperty("cur_aid", params[2]);

			int get_text_state = conn.getHeaderFieldInt(
					Config.KEY_GET_TEXT_STATE, 0);

			InputStream in = conn.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line;

			jsonData = new StringBuffer();

			while ((line = br.readLine()) != null) {

				jsonData.append(line);
			}

			return get_text_state;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	private void resolveJson() {

		try {
			JSONObject jsonObject = new JSONObject(URLDecoder.decode(
					jsonData.toString(), "UTF-8"));
			JSONArray programmers = jsonObject.getJSONArray("articles");
			ArrayList<Show_info_bean> data = new ArrayList<Show_info_bean>();
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

				data.add(new Show_info_bean(img_name, zan, report, zan_state,
						text, aid, relationShip));

			}

			for (int i = data.size() - 1; i >= 0; i--) {

				datas.add(data.get(i));
			}

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);

		if (result == 5001) {
			System.out.println("buttom  请求成功");
			resolveJson();
			Data_loading.adapter.notifyDataSetChanged();

			foot_text.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);

		} else if (result == 5002) {
			System.out.println("请求失败 缺少参数");

		} else if (result == 5003) {
			System.out.println("请求失败其他原因");

		} else {
			System.out.println("莫名原因 " + result);
		}

	}

}
