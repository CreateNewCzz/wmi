package com.example.wmi;

import java.util.ArrayList;

import android.app.Activity;
import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.adapter.Pager_Adapter;

public class Welcome extends Activity implements OnPageChangeListener,
		OnClickListener {
	private ViewPager pager;
	private ArrayList<View> view_datas;
	private Button register, login;
	private int[] imgs = { R.drawable.account_guidance_0,
			R.drawable.account_guidance_1, R.drawable.account_guidance_2,
			R.drawable.account_guidance_3 };
	private ImageView[] imgview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.welcome_pager);
		createShortCut();

		pager = (ViewPager) this.findViewById(R.id.view_pager);
		view_datas = new ArrayList<View>();
		for (int i = 0; i < imgs.length; i++) {
			View v = LayoutInflater.from(this).inflate(R.layout.vp_cell, null);
			ImageView img = (ImageView) v.findViewById(R.id.vp_img);
			img.setImageResource(imgs[i]);
			view_datas.add(v);
		}
		Pager_Adapter adapter = new Pager_Adapter(view_datas);
		pager.setAdapter(adapter);
		LinearLayout view_linear = (LinearLayout) this
				.findViewById(R.id.pag_view_linear);
		imgview = new ImageView[imgs.length];
		for (int i = 0; i < imgs.length; i++) {
			imgview[i] = new ImageView(this);
			imgview[i].setPadding(0, 0, 5, 0);

			if (i == 0) {
				imgview[i]
						.setImageResource(R.drawable.ic_publish_template_pager_indicator_selected);
			} else {
				imgview[i]
						.setImageResource(R.drawable.ic_publish_template_pager_indicator_normal);
			}
			view_linear.addView(imgview[i]);
		}
		pager.setOnPageChangeListener(this);

		register = (Button) this.findViewById(R.id.pag_reg_btn);
		login = (Button) this.findViewById(R.id.pag_login_btn);
		register.setOnClickListener(this);
		login.setOnClickListener(this);

	}

	public void createShortCut() {

		Intent shortcutintent = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");
		shortcutintent.putExtra("duplicate", false);

		shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "wmi");

		Parcelable icon = Intent.ShortcutIconResource.fromContext(
				getApplicationContext(), R.drawable.ic_launcher);

		shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);

		shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(this,
				MainActivity.class));
		sendBroadcast(shortcutintent);
	}

	public void onPageSelected(int point) {

		// µãµÄÇÐ»»
		for (int i = 0; i < imgs.length; i++) {
			if (point == i) {
				imgview[i]
						.setImageResource(R.drawable.ic_publish_template_pager_indicator_selected);
			} else {

				imgview[i]
						.setImageResource(R.drawable.ic_publish_template_pager_indicator_normal);
			}
		}

	}

	@Override
	public void onClick(View v) {

		Intent intent = null;
		switch (v.getId()) {
		case R.id.pag_reg_btn:

			intent = new Intent(this, Reg.class);

			break;

		case R.id.pag_login_btn:
			this.finish();
			intent = new Intent(this, Login.class);

			break;
		}
		if (intent != null) {
			startActivity(intent);
		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

}
