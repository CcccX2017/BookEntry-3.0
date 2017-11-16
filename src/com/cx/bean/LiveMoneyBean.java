package com.cx.bean;

public class LiveMoneyBean {
	private String username;
	private String startDate;
	private String endDate;
	private float livemoney;
	private int StartMonth;
	private int StartDay;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public float getLivemoney() {
		return livemoney;
	}
	public void setLivemoney(float livemoney) {
		this.livemoney = livemoney;
	}
	
	public int getStartMonth() {
		return StartMonth;
	}
	public void setStartMonth(int startMonth) {
		StartMonth = startMonth;
	}
	public int getStartDay() {
		return StartDay;
	}
	public void setStartDay(int startDay) {
		StartDay = startDay;
	}
	
}
