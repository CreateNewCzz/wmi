package com.example.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.Toast;

public class InputImgforSdcar extends AsyncTask<String, Void, Bitmap> {

	ImageView img;
	String img_name;

	public InputImgforSdcar(ImageView img) {
		this.img = img;
	}

	protected Bitmap doInBackground(String... params) {

		img_name = params[0];
		if (ExistSDCard()) {
			File file = new File(Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/albums");

			File[] files = file.listFiles();
			file.mkdirs();
			if (files.length > 0) {

				for (File f : files) {
					if (img_name.equals(f.getName())) {
						try {
							FileInputStream fis = new FileInputStream(f);
							// Bitmap bitmap = BitmapFactory.decodeStream(fis);

							BitmapFactory.Options opts = new BitmapFactory.Options();

							opts.inSampleSize = 4;

							Bitmap bitmap = BitmapFactory.decodeStream(fis,
									null, opts);

							return bitmap;
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

		return null;
	}

	private boolean ExistSDCard() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		} else
			return false;
	}

	protected void onPostExecute(Bitmap result) {
		super.onPostExecute(result);

		if (result != null) {
			img.setImageBitmap(result);

		} else {
			new GetImage(img).execute(img_name);

		}

	}

}
