package com.example.task;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.aeal.pullrefreshlistview.MyListView;
import com.example.adapter.LoadData_Adapter;
import com.example.bean.Show_info_bean;
import com.example.config.Config;

public class DelItemTask extends AsyncTask<String, Void, Integer> {
	Context context;
	ArrayList<Show_info_bean> datas;
	String aid;
	MyListView main_lv;

	public DelItemTask(Context context, ArrayList<Show_info_bean> datas,
			MyListView main_lv) {
		this.context = context;
		this.datas = datas;
		this.main_lv = main_lv;
	}

	protected Integer doInBackground(String... params) {
		String relation = params[0];
		aid = params[1];

		try {
			URL url = new URL(Config.KEY_URL);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(5000);
			conn.setRequestMethod("POST");

			conn.setRequestProperty(Config.REQUEST_TYPE, "delete");
			conn.setRequestProperty(Config.KEY_AID, aid);

			int delete_state = conn.getHeaderFieldInt("delete_state", -1);

			return delete_state;

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		String res = null;
		if (result == 8000) {
			res = "É¾³ý³É¹¦";

			for (int i = 0; i < datas.size(); i++) {
				Show_info_bean bean = datas.get(i);
				if (aid.equals(bean.getAid())) {
					datas.remove(i);
				}

			}

			main_lv.setAdapter(new LoadData_Adapter(datas, main_lv));

		} else if (result == 8001) {
			res = "É¾³ýÊ§°Ü£¬²ÎÊý´íÎó";
		} else if (result == 8002) {
			res = "É¾³ýÊ§°Ü£¬·þÎñ¶Ë´íÎó";
		} else {
			res = result + "";
		}
		Toast.makeText(context, res, Toast.LENGTH_SHORT).show();

	}

}
