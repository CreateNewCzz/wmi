package com.example.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.example.config.Config;

public class GetImage extends AsyncTask<String, Void, Object> {

	ImageView img;
	String img_name;

	public GetImage(ImageView img) {
		this.img = img;
	}

	protected Object doInBackground(String... params) {
		img_name = params[0];
		try {
			URL url = new URL(Config.KEY_URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(2 * 5000);
			conn.setConnectTimeout(2 * 5000);
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setUseCaches(false);

			conn.setRequestProperty("request_type", "image");
			conn.setRequestProperty("img_name", img_name);
			InputStream in = conn.getInputStream();
			Bitmap bitmap = BitmapFactory.decodeStream(in);

			return bitmap;

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	protected void onPostExecute(Object result) {

		if (result != null) {
			Bitmap bitmap = (Bitmap) result;
			img.setImageBitmap(bitmap);
			saveBitmap(bitmap);
		}

	}

	public void saveBitmap(Bitmap bitmap) {
		File f = new File(Environment.getExternalStorageDirectory()
				+ "/albums/");

		try {
			if (!f.exists()) {
				f.mkdirs();
			}
			File file = new File(f.getAbsolutePath() + "/" + img_name);
			FileOutputStream out = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
