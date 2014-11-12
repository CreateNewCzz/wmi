package com.example.bean;

import java.io.Serializable;

public class Show_info_bean implements Serializable {

	private static final long serialVersionUID = 1L;
	private String img_name;
	private String zan;
	private String report;
	private String zan_state;
	private String text;
	private String aid;
	private String relationShip;

	public Show_info_bean(String img_name, String zan, String report,
			String zan_state, String text, String aid, String relationShip) {
		this.img_name = img_name;
		this.zan = zan;
		this.report = report;
		this.zan_state = zan_state;
		this.text = text;
		this.aid = aid;
		this.relationShip = relationShip;

	}

	public String getImg_name() {
		return img_name;
	}

	public void setImg_name(String img_name) {
		this.img_name = img_name;
	}

	public String getZan() {
		return zan;
	}

	public void setZan(String zan) {
		this.zan = zan;
	}

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}

	public String getZan_state() {
		return zan_state;
	}

	public void setZan_state(String zan_state) {
		this.zan_state = zan_state;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public String getRelationShip() {
		return relationShip;
	}

	public void setRelationShip(String relationShip) {
		this.relationShip = relationShip;
	}

}
