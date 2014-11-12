package com.example.task;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.UUID;

import com.example.bean.Show_info_bean;
import com.example.config.Config;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

public class SendMessageOuter extends AsyncTask<String, Void, Integer> {

	private static final int TIME = 10 * 1000;

	public static final String BOUNDARY = UUID.randomUUID().toString();

	public static final String PREFIX = "--";

	public static final String END_LINE = "\r\n";

	private Context context;

	String picturePath;

	public SendMessageOuter(Context context, String picturePath) {
		this.context = context;
		this.picturePath = picturePath;
	}

	protected Integer doInBackground(String... params) {

		// 0 text 1 account 2 params[img_type]

		String text = params[0];

		String account = params[1];

		String aid = account + System.currentTimeMillis();

		String img_type = params[2];

		String img_name = account + System.currentTimeMillis()
				+ UUID.randomUUID().toString() + ".png";

		try {
			URL url = new URL(Config.KEY_URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setReadTimeout(TIME);
			conn.setConnectTimeout(TIME);
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);

			conn.setRequestProperty(Config.REQUEST_TYPE, "set_text");
			conn.setRequestProperty(Config.KEY_ACCOUNT, account);
			conn.setRequestProperty(Config.KEY_AID, aid);
			conn.setRequestProperty("text", URLEncoder.encode(text, "utf-8"));
			conn.setRequestProperty("img_type", img_type);
			conn.setRequestProperty("img_name", img_name);

			conn.setRequestProperty("Charset", "utf-8");
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + BOUNDARY);

			StringBuffer buf = new StringBuffer();

			buf.append(PREFIX + BOUNDARY + END_LINE);
			File file = new File(picturePath);

			buf.append("Content-Disposition:form-data;name = \"file\";filename = "
					+ file.getName() + END_LINE);
			buf.append("Content-Type:application/octet-stream;charset=utf-8"
					+ END_LINE + END_LINE);

			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

			dos.write(buf.toString().getBytes());

			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(file));

			byte[] b = new byte[1024];
			int len;
			while ((len = bis.read(b)) != -1) {
				dos.write(b, 0, len);
			}

			dos.write(END_LINE.getBytes());

			String endstr = PREFIX + BOUNDARY + PREFIX + END_LINE;

			dos.write(endstr.getBytes());
			dos.flush();

			bis.close();
			dos.close();

			int set_text_state = conn.getHeaderFieldInt("set_text_state", -1);

			return set_text_state;

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
