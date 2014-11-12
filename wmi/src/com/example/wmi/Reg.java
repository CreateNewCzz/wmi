package com.example.wmi;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.task.Reg_number;

public class Reg extends Activity implements OnClickListener {
	EditText phoneNum, pwd;
	Button reg_btn;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.register);
		init();
	}

	private void init() {
		phoneNum = (EditText) this.findViewById(R.id.reg_phoneNum);
		pwd = (EditText) this.findViewById(R.id.reg_pwd);
		reg_btn = (Button) this.findViewById(R.id.reg_register_btn);
		reg_btn.setOnClickListener(this);
	}

	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.reg_register_btn:

			if (!TextUtils.isEmpty(phoneNum.getText().toString())
					&& !TextUtils.isEmpty(pwd.getText().toString())) {
				new Reg_number(this).execute(phoneNum.getText().toString(), pwd
						.getText().toString());

			}

			break;

		}

	}

	public boolean isNetworkConnected(Context context) {

		if (context != null) {
			// ConnectivityManager获取网络状态的管理者
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			// 获取网络当前状态一个类
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;

	}

}
