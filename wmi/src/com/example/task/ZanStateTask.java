package com.example.task;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.example.config.Config;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class ZanStateTask extends AsyncTask<String, Void, Integer> {

	private Context context;

	public ZanStateTask(Context context) {
		this.context = context;

	}

	protected Integer doInBackground(String... params) {

		String aid = params[0];
		String zan_type = params[1];
		String account = params[2];

		try {
			URL url = new URL(Config.KEY_URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(5000);
			conn.setRequestMethod("POST");

			conn.setRequestProperty(Config.REQUEST_TYPE, "zan");
			conn.setRequestProperty(Config.KEY_AID, aid);
			conn.setRequestProperty("zan_type", zan_type);
			conn.setRequestProperty(Config.KEY_ACCOUNT, account);

			int zan_state = conn.getHeaderFieldInt("zan_state", -1);

			return zan_state;

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

}
