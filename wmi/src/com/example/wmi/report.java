package com.example.wmi;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.adapter.ReportAdapter;
import com.example.bean.ReportBean;
import com.example.bean.Show_info_bean;
import com.example.config.Config;
import com.example.task.GetReport;
import com.example.task.InputImgforSdcar;
import com.example.task.SendRepor;
import com.example.task.ZanStateTask;

public class report extends Activity implements OnClickListener {

	private TextView report_context, report_reportNum, report_zan,
			report_relationShip;
	private ListView reportinfo;
	private EditText report_info;
	private ImageView report_send, title_zan, state_zan, repor_finish,
			report_more;
	private String aid;
	private String[] l_ = { "l_1", "l_2", "l_3", "l_4", "l_5", "l_6", "l_7",
			"l_8", "l_9", "l_10", "l_11", "l_12", "l_13", "l_14", "l_15",
			"l_16", "l_17", "l_18", "l_19", "l_20", "l_21", "l_22", "l_23",
			"l_24", "l_25", "l_26", "l_27", "l_28", "l_29", "l_30" };

	boolean flag = false;
	String relation;
	private ZanStateTask stateTask;
	private ArrayList<Show_info_bean> data;
	private int position;
	private String account;
	ArrayList<ReportBean> datas = new ArrayList<ReportBean>();
	ReportAdapter reportAdapter;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		account = getSharedPreferences("is_start.xml", 0).getString("username",
				"");
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.report);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.title_bar_report);
		init();
	}

	private void init() {
		reportinfo = (ListView) this.findViewById(R.id.report_reportinfo);
		report_context = (TextView) this.findViewById(R.id.report_context);
		report_reportNum = (TextView) this.findViewById(R.id.report_reportNum);
		report_zan = (TextView) this.findViewById(R.id.report_zan);
		report_relationShip = (TextView) this
				.findViewById(R.id.report_relationShip);
		report_info = (EditText) this.findViewById(R.id.report_info);
		report_send = (ImageView) this.findViewById(R.id.report_send);
		title_zan = (ImageView) this.findViewById(R.id.report_zan_state);
		state_zan = (ImageView) this.findViewById(R.id.report_title_zan);
		repor_finish = (ImageView) this
				.findViewById(R.id.report_title_img_finish);
		report_more = (ImageView) this.findViewById(R.id.report_title_more);
		title_zan.setOnClickListener(this);
		state_zan.setOnClickListener(this);
		repor_finish.setOnClickListener(this);
		report_more.setOnClickListener(this);

		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		position = bundle.getInt("position");
		position--;
		data = (ArrayList<Show_info_bean>) bundle.getSerializable("data");
		String text = data.get(position).getText();
		String zan = data.get(position).getZan();
		String report_num = data.get(position).getReport();
		String relation = data.get(position).getRelationShip();
		String zan_state = data.get(position).getZan_state();
		String img_name = data.get(position).getImg_name();
		aid = data.get(position).getAid();

		relation = data.get(position).getRelationShip();

		report_context.setText(text);
		report_reportNum.setText(report_num);
		report_zan.setText(zan);
		report_relationShip.setText(relation);

		report_send.setOnClickListener(this);

		int length = text.getBytes().length;

		ImageView img_back = (ImageView) this
				.findViewById(R.id.report_backgroud);

		LayoutParams para = img_back.getLayoutParams();
		if (length < 100) {
			para.height = 140;

		} else if (length < 150 && length >= 100) {
			para.height = 230;

		} else if (length >= 150 && length <= 240) {
			para.height = 300;
		}
		img_back.setLayoutParams(para);

		for (int i = 0; i < l_.length; i++) {
			if (img_name.equals(l_[i])) {
				img_back.setBackgroundResource(Config.l_[i]);
			} else {

				new InputImgforSdcar(img_back).execute(img_name);

			}
		}

		if ("yes".equals(zan_state)) {
			title_zan.setImageResource(R.drawable.ic_card_liked);
			state_zan.setImageResource(R.drawable.ic_card_liked);
			flag = true;

		}

		new GetReport(reportinfo, datas, reportAdapter).execute(account, aid);

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.report_send:
			// 0 aid 1: account 2: text 需要urlcode 3: rid

			try {
				String info = URLEncoder.encode(report_info.getText()
						.toString(), "utf-8");
				String rid = account + aid + System.currentTimeMillis();
				new SendRepor(this, report_info, relation, datas, reportinfo)
						.execute(aid, account, info, rid);

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			break;

		case R.id.report_zan_state:

			checkZan();

			break;
		case R.id.report_title_zan:
			checkZan();
			break;
		case R.id.report_title_img_finish:

			finish();

			break;
		case R.id.report_title_more:
			popwindow();
			break;
		}
	}

	// pop windows

	private void popwindow() {

		View view = LayoutInflater.from(this).inflate(R.layout.report_more,
				null);

		PopupWindow popupWindow = new PopupWindow(view,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		popupWindow.setBackgroundDrawable(new ColorDrawable(0));
		popupWindow.showAsDropDown(report_more);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.update();

	}

	// report 赞的处理
	private void checkZan() {

		if (flag) {
			state_zan.setImageResource(R.drawable.ic_card_like_grey);
			title_zan.setImageResource(R.drawable.ic_card_like_grey);
			flag = false;
			stateTask = new ZanStateTask(this);

			data.get(position).setZan_state("no");
			stateTask.execute(aid, "down", account);
			report_zan.setText((Integer
					.valueOf(report_zan.getText().toString()) - 1) + "");

			data.get(position).setZan(
					(Integer.valueOf(data.get(position).getZan()) - 1) + "");

		} else if (!flag) {
			title_zan.setImageResource(R.drawable.ic_card_liked);
			state_zan.setImageResource(R.drawable.ic_card_liked);
			flag = true;
			stateTask = new ZanStateTask(this);

			data.get(position).setZan_state("yes");
			stateTask.execute(aid, "up", account);
			report_zan.setText((Integer
					.valueOf(report_zan.getText().toString()) + 1) + "");
			data.get(position).setZan(
					(Integer.valueOf(data.get(position).getZan()) + 1) + "");
		}
	}

}
