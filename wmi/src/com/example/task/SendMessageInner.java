package com.example.task;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import com.example.config.Config;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class SendMessageInner extends AsyncTask<String, Void, Integer> {

	Context context;

	public SendMessageInner(Context context) {
		this.context = context;
	}

	protected Integer doInBackground(String... params) {
		// 0 account 1 text 2 imagename
		String account = params[0];
		String text = params[1];
		String img_name = params[2];

		try {
			URL url = new URL(Config.KEY_URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setConnectTimeout(5000);
			conn.setRequestMethod("POST");

			String aid = account + System.currentTimeMillis();

			conn.setRequestProperty("request_type", "set_text");
			conn.setRequestProperty("account", account);
			conn.setRequestProperty("aid", aid);
			conn.setRequestProperty("text", URLEncoder.encode(text, "utf-8"));
			conn.setRequestProperty("img_type", "inner");
			conn.setRequestProperty("img_name", img_name);

			int set_text_state = conn.getHeaderFieldInt("set_text_state", -1);
			return set_text_state;

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);

		String res = "";
		if (result == null || result == -1) {
			res = "请检查网络 + " + result;

		} else if (result == 6000) {
			res = "发送成功";
		} else if (result == 6001) {
			res = "参数错误";
		} else if (result == 6002) {
			res = "服务器错误";
		}

		Toast.makeText(context, res, Toast.LENGTH_SHORT).show();

	}

}
