package com.example.task;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.example.config.Config;
import com.example.wmi.Login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.widget.Toast;

public class Reg_number extends AsyncTask<String, Void, Integer> {

	Context context;
	ProgressDialog dialog;

	public Reg_number(Context context) {
		this.context = context;
		dialog = new ProgressDialog(context);
		dialog.setTitle("提示:");
		dialog.setMessage("正在链接网络，请稍后...");
		dialog.show();

	}

	protected Integer doInBackground(String... params) {

		String account = params[0];
		String pwd = params[1];

		try {
			URL url = new URL(Config.KEY_URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("POST");
			conn.setReadTimeout(5000);

			conn.setRequestProperty("request_type", "register");
			conn.setRequestProperty("account", account);
			conn.setRequestProperty("password", pwd);
			conn.setRequestProperty("phoneNumber", getLocalPhone());

			int registe_state = conn.getHeaderFieldInt("registe_state", 0);

			return registe_state;

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

		dialog.cancel();
		String res = null;

		if (result == 3000) {
			res = "注册成功";

			context.startActivity(new Intent(context, Login.class));

		} else if (result == 3001) {
			res = "用户已存在";
		}

		Toast.makeText(context, res + "  " + result, Toast.LENGTH_SHORT).show();
	}

	private String getLocalPhone() {
		Cursor cursor = context.getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
				null, null);
		StringBuffer buf = new StringBuffer();
		while (cursor.moveToNext()) {
			String number = cursor
					.getString(cursor
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			buf.append(number);
			buf.append(",");
		}
		return buf.toString();

	}
}
