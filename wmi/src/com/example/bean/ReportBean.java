package com.example.bean;

public class ReportBean {

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	private String report_zan_state;
	private String number;
	private String report_text;
	private String host;
	private String rid;
	private String aid;
	private String report_zan;

	private int icon;

	public ReportBean(String report_zan_state, String number,
			String report_text, String host, String rid, String aid,
			String report_zan) {
		this.report_zan_state = report_zan_state;
		this.number = number;
		this.report_text = report_text;
		this.host = host;
		this.rid = rid;
		this.aid = aid;
		this.report_zan = report_zan;
	}

	public String getReport_zan_state() {
		return report_zan_state;
	}

	public void setReport_zan_state(String report_zan_state) {
		this.report_zan_state = report_zan_state;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getReport_text() {
		return report_text;
	}

	public void setReport_text(String report_text) {
		this.report_text = report_text;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public String getReport_zan() {
		return report_zan;
	}

	public void setReport_zan(String report_zan) {
		this.report_zan = report_zan;
	}

}
