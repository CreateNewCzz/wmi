package com.example.task;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.config.Config;
import com.example.wmi.MainActivity;

public class Login_task extends AsyncTask<String, Void, Integer> {
	private Context context;
	private ProgressDialog dialog;
	private String account;
	private String pwd;

	public Login_task(Context context) {
		this.context = context;
		dialog = new ProgressDialog(context);
		dialog.setTitle("提示:");
		dialog.setTitle("正在登陆");
		dialog.show();
	}

	protected Integer doInBackground(String... params) {
		account = params[0];
		pwd = params[1];

		try {
			URL url = new URL(Config.KEY_URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setReadTimeout(5000);
			conn.setRequestMethod("POST");

			conn.setRequestProperty(Config.REQUEST_TYPE, "login");
			conn.setRequestProperty(Config.KEY_ACCOUNT, params[0]);
			conn.setRequestProperty(Config.KEY_PASSWORD, params[1]);

			int registe_state = conn.getHeaderFieldInt(Config.KEY_LOGIN_STATE,
					0);

			return registe_state;

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);

		dialog.cancel();
		String res = null;
		
		if (result == null || result == 0) {
			res = "请检查网络" + result;
		} else {
			if (result == 2000) {
				res = "登陆成功";
				if (writerValue()) {
					//登陆 只有一次 销毁这个 act 
					((Activity) context).finish();
					
					context.startActivity(new Intent(context,
							MainActivity.class));
				}

			} else if (result == 2001) {
				res = "登陆失败";
			}
		}
		Toast.makeText(context, res, Toast.LENGTH_SHORT).show();

	}

	private boolean writerValue() {
		SharedPreferences start_info = context.getSharedPreferences(
				"is_start.xml", 0);
		SharedPreferences.Editor editor = start_info.edit();
		editor.putString("start", "true");
		editor.putString("username", account);
		editor.putString("pwd", pwd);
		return editor.commit();

	}
}
