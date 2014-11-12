package com.example.wmi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.Pager_Adapter;
import com.example.config.Config;
import com.example.task.SendMessageInner;
import com.example.task.SendMessageOuter;

public class AddMessage extends Activity implements OnClickListener,
		TextWatcher {

	private ImageView finish, sendmsg, msg_fun, msg_more, fun_album, moulds,
			fun_camera;
	private EditText msg_content;
	private ImageView bgimg;
	private FrameLayout layout_add;
	private View function, more, img_back;
	private LinearLayout linear01;
	private boolean flag1, flag2;
	private String picturePath;
	private boolean is_change, isSend;
	private int number;
	private View view;
	private ViewPager vp;
	private ArrayList<View> data;
	private ImageView[] img_point;
	private final String[] l_ = { "l_1", "l_2", "l_3", "l_4", "l_5", "l_6",
			"l_7", "l_8", "l_9", "l_10", "l_11", "l_12", "l_13", "l_14",
			"l_15", "l_16", "l_17", "l_18", "l_19", "l_20", "l_21", "l_22",
			"l_23", "l_24", "l_25", "l_26", "l_27", "l_28", "l_29", "l_30", };

	private HoldeView holdeView;

	private int pagepoint = 0;
	private TextView msg_text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.message);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.title_bar_msg);
		init();

	}

	private void init() {
		finish = (ImageView) this.findViewById(R.id.msg_title_img_finish);
		sendmsg = (ImageView) this.findViewById(R.id.msg_sendmsg);
		msg_content = (EditText) this.findViewById(R.id.msg_content);
		bgimg = (ImageView) this.findViewById(R.id.msg_bgimg);
		msg_fun = (ImageView) this.findViewById(R.id.msg_fun);
		msg_more = (ImageView) this.findViewById(R.id.msg_more);
		layout_add = (FrameLayout) this.findViewById(R.id.msg_layoutadd);
		linear01 = (LinearLayout) this.findViewById(R.id.msg_linear01);
		msg_text = (TextView) this.findViewById(R.id.msg_text);
		function = LayoutInflater.from(this).inflate(R.layout.function_select,
				null);
		moulds = (ImageView) function.findViewById(R.id.fun_moulds);
		more = LayoutInflater.from(this).inflate(R.layout.msg_more, null);

		fun_album = (ImageView) function.findViewById(R.id.fun_album);

		fun_camera = (ImageView) function.findViewById(R.id.fun_camera);

		// 初始化模板
		mould();
		msg_content.addTextChangedListener(this);
		fun_album.setOnClickListener(this);
		msg_fun.setOnClickListener(this);
		msg_more.setOnClickListener(this);
		finish.setOnClickListener(this);
		sendmsg.setOnClickListener(this);
		moulds.setOnClickListener(this);
		msg_content.setOnClickListener(this);
		msg_text.setOnClickListener(this);
		fun_camera.setOnClickListener(this);
		number = new Random().nextInt(Config.l_.length);
		bgimg.setBackgroundResource(Config.l_[number]);

	}

	@SuppressWarnings("unused")
	@Override
	public void onClick(View v) {

		String account = getSharedPreferences("is_start.xml", 0).getString(
				"username", "");

		switch (v.getId()) {
		case R.id.msg_title_img_finish:

			finish();
			break;

		case R.id.msg_sendmsg:

			if (isSend) {
				String text = msg_content.getText().toString();

				if (is_change) {
					new SendMessageOuter(this, picturePath).execute(text,
							account, "outer");
				} else {

					String name = l_[number];
					new SendMessageInner(this).execute(account, text, name);

				}

				finish();
			} else {
				Toast.makeText(this, "内容为空~  -_-||| ", Toast.LENGTH_SHORT)
						.show();
			}

			break;
		case R.id.msg_fun:

			if (!flag1) {
				layout_add.removeView(more);
				layout_add.removeView(function);
				layout_add.removeView(view);
				layout_add.addView(function);
				setLayoutY(linear01, -50);
				flag1 = true;
				flag2 = false;
				msg_fun.setImageResource(R.drawable.ic_publish_operation_bar_keyboard);
				msg_more.setImageResource(R.drawable.ic_publish_operation_bar_option);
			}

			break;
		case R.id.msg_more:
			if (!flag2) {
				layout_add.removeView(function);
				layout_add.removeView(more);
				layout_add.removeView(view);
				layout_add.addView(more);
				setLayoutY(linear01, -50);
				flag1 = false;
				flag2 = true;
				msg_fun.setImageResource(R.drawable.ic_publish_operation_bar_photo);
				msg_more.setImageResource(R.drawable.ic_publish_operation_bar_keyboard);
			}
			break;

		case R.id.fun_album:

			Intent i = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

			startActivityForResult(i, 1);

			break;

		case R.id.fun_moulds:
			layout_add.removeView(function);
			layout_add.removeView(more);
			layout_add.addView(view);

			break;

		case R.id.mouldvp_img01:

			bg_mould(0);
			break;
		case R.id.mouldvp_img02:

			bg_mould(1);

			break;
		case R.id.mouldvp_img03:

			bg_mould(2);

			break;
		case R.id.mouldvp_img04:
			bg_mould(3);

			break;
		case R.id.mouldvp_img05:
			bg_mould(4);

			break;
		case R.id.mouldvp_img06:
			bg_mould(5);

			break;
		case R.id.mouldvp_img07:
			bg_mould(6);

			break;
		case R.id.mouldvp_img08:
			bg_mould(7);

			break;
		case R.id.mouldvp_img09:
			bg_mould(8);

			break;
		case R.id.mouldvp_img10:
			bg_mould(9);
			break;
		case R.id.mould_back:

			// 在此小心父级关系

			layout_add.removeView(function);
			layout_add.removeView(more);
			layout_add.removeView(view);
			layout_add.addView(function);
			break;

		case R.id.msg_content:

			if (flag1 || flag2) {
				layout_add.removeView(function);
				layout_add.removeView(more);
				layout_add.removeView(view);
				layout_add.removeView(function);
				setLayoutY(linear01, 1);
				msg_fun.setImageResource(R.drawable.ic_publish_operation_bar_photo);
				msg_more.setImageResource(R.drawable.ic_publish_operation_bar_option);
			}

			break;

		case R.id.msg_text:

			AlertDialog.Builder builder = new AlertDialog.Builder(this);

			builder.setTitle("发表秘密安全吗？")
					.setMessage("你的朋友只知道他们认识的某个人发的，但无法知道具体是谁")
					.setNeutralButton("知道了", null).create().show();

			break;
		case R.id.fun_camera:

			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent, 2);

			break;

		}
	}

	// 设置 系统自带的背景
	private void bg_mould(int number) {

		switch (pagepoint) {
		case 0:
			this.number = 10 * pagepoint + number;

			break;
		case 1:
			this.number = 10 * pagepoint + number;

			break;
		case 2:
			this.number = 10 * pagepoint + number;

			break;
		}
		bgimg.setBackgroundResource(Config.l_[this.number]);

	}

	private void mould() {
		holdeView = new HoldeView();
		view = LayoutInflater.from(this).inflate(R.layout.moulds, null);
		vp = (ViewPager) view.findViewById(R.id.mould_vp);
		data = new ArrayList<View>();
		img_back = (ImageView) view.findViewById(R.id.mould_back);
		img_back.setOnClickListener(this);
		for (int i = 0; i < Config.s_.length; i += 10) {
			View view = LayoutInflater.from(this).inflate(
					R.layout.mouldvp_cell, null);
			holdeView.img01 = (ImageView) view.findViewById(R.id.mouldvp_img01);
			holdeView.img02 = (ImageView) view.findViewById(R.id.mouldvp_img02);
			holdeView.img03 = (ImageView) view.findViewById(R.id.mouldvp_img03);
			holdeView.img04 = (ImageView) view.findViewById(R.id.mouldvp_img04);
			holdeView.img05 = (ImageView) view.findViewById(R.id.mouldvp_img05);
			holdeView.img06 = (ImageView) view.findViewById(R.id.mouldvp_img06);
			holdeView.img07 = (ImageView) view.findViewById(R.id.mouldvp_img07);
			holdeView.img08 = (ImageView) view.findViewById(R.id.mouldvp_img08);
			holdeView.img09 = (ImageView) view.findViewById(R.id.mouldvp_img09);
			holdeView.img10 = (ImageView) view.findViewById(R.id.mouldvp_img10);
			holdeView.img01.setImageResource(Config.s_[i]);
			holdeView.img02.setImageResource(Config.s_[i + 1]);
			holdeView.img03.setImageResource(Config.s_[i + 2]);
			holdeView.img04.setImageResource(Config.s_[i + 3]);
			holdeView.img05.setImageResource(Config.s_[i + 4]);
			holdeView.img06.setImageResource(Config.s_[i + 5]);
			holdeView.img07.setImageResource(Config.s_[i + 6]);
			holdeView.img08.setImageResource(Config.s_[i + 7]);
			holdeView.img09.setImageResource(Config.s_[i + 8]);
			holdeView.img10.setImageResource(Config.s_[i + 9]);
			data.add(view);

			holdeView.img01.setOnClickListener(this);
			holdeView.img02.setOnClickListener(this);
			holdeView.img03.setOnClickListener(this);
			holdeView.img04.setOnClickListener(this);
			holdeView.img05.setOnClickListener(this);
			holdeView.img06.setOnClickListener(this);
			holdeView.img07.setOnClickListener(this);
			holdeView.img08.setOnClickListener(this);
			holdeView.img09.setOnClickListener(this);
			holdeView.img10.setOnClickListener(this);

		}

		vp.setAdapter(new Pager_Adapter(data));

		LinearLayout layout = (LinearLayout) view
				.findViewById(R.id.mould_linear);
		img_point = new ImageView[3];
		for (int i = 0; i < 3; i++) {
			img_point[i] = new ImageView(this);
			img_point[i].setPadding(0, 0, 5, 0);
			if (i == 0) {

				img_point[i]
						.setImageResource(R.drawable.ic_publish_template_pager_indicator_selected);
			} else {

				img_point[i]
						.setImageResource(R.drawable.ic_publish_template_pager_indicator_normal);
			}

			layout.addView(img_point[i]);
		}

		vp.setOnPageChangeListener(new OnPageChangeListener() {

			public void onPageSelected(int point) {
				pagepoint = point;
				for (int i = 0; i < img_point.length; i++) {
					if (point == i) {
						img_point[i]
								.setImageResource(R.drawable.ic_publish_template_pager_indicator_selected);
					} else {

						img_point[i]
								.setImageResource(R.drawable.ic_publish_template_pager_indicator_normal);
					}
				}

			}

			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			public void onPageScrollStateChanged(int arg0) {

			}
		});

	}

	private class HoldeView {
		ImageView img01, img02, img03, img04, img05, img06, img07, img08,
				img09, img10;
	}

	/*
	 * 设置控件所在的位置Y，并且不改变宽高， Y为绝对位置，此时X可能归0
	 */
	private void setLayoutY(View view, int y) {
		MarginLayoutParams margin = new MarginLayoutParams(
				view.getLayoutParams());
		margin.setMargins(margin.leftMargin, y, margin.rightMargin, y
				+ margin.height);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				margin);
		view.setLayoutParams(layoutParams);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1 && resultCode == RESULT_OK && null != data) {

			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			picturePath = cursor.getString(columnIndex);
			System.out.println(picturePath);

			cursor.close();
			Bitmap bitmap_album = BitmapFactory.decodeFile(picturePath);
			bgimg.setImageBitmap(bitmap_album);
			is_change = true;

		}

		if (requestCode == 2 && resultCode == RESULT_OK && null != data) {

			String sdStatus = Environment.getExternalStorageState();
			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
				return;
			}
			String name = System.currentTimeMillis() + ".jpg";
			Toast.makeText(this, name, Toast.LENGTH_LONG).show();
			Bundle bundle = data.getExtras();
			Bitmap bitmap = (Bitmap) bundle.get("data");

			FileOutputStream b = null;
			File file = new File(Environment.getExternalStorageDirectory(),
					"/myImage/"); // 设置保存到本地的文件夹
			file.mkdirs();
			picturePath = file.getAbsolutePath() + "/" + name;

			try {
				b = new FileOutputStream(picturePath);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					b.flush();
					b.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			// bgimg.setBackground(new BitmapDrawable(bitmap));
			bgimg.setImageBitmap(bitmap); // bgimg 设置背景
			is_change = true;
		}

	}

	// editText 监听
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void afterTextChanged(Editable s) {

		if (TextUtils.isEmpty(msg_content.getText().toString())) {
			sendmsg.setBackgroundResource(R.drawable.ic_send_btn_disabled);
			isSend = false;
		} else {
			sendmsg.setBackgroundResource(R.drawable.ic_send_btn_enabled);
			isSend = true;

		}

	}

}
