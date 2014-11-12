package com.example.wmi;

import com.example.task.Login_task;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Login extends Activity implements OnClickListener {

	EditText phoneEt, pwdEt;
	Button loginBtn;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.login);
		init();
	}

	private void init() {
		phoneEt = (EditText) this.findViewById(R.id.in_phoneEt);
		pwdEt = (EditText) this.findViewById(R.id.in_pwdEt);
		loginBtn = (Button) this.findViewById(R.id.in_loginBtn);
		loginBtn.setOnClickListener(this);

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.in_loginBtn:

			String phone = phoneEt.getText().toString();
			String pwd = pwdEt.getText().toString();
			if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(pwd)) {
				new Login_task(this).execute(phone, pwd);
			}

			break;

		}
	}

}
