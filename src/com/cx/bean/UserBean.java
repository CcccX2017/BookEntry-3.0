package com.cx.bean;

public class UserBean {
	private static String username;
	private static String password;
	private static String sex;
	private static String phone;
	public static String getUsername() {
		return username;
	}
	public static void setUsername(String username) {
		UserBean.username = username;
	}
	public static String getPassword() {
		return password;
	}
	public static void setPassword(String password) {
		UserBean.password = password;
	}
	public static String getSex() {
		return sex;
	}
	public static void setSex(String sex) {
		UserBean.sex = sex;
	}
	public static String getPhone() {
		return phone;
	}
	public static void setPhone(String phone) {
		UserBean.phone = phone;
	}
}
