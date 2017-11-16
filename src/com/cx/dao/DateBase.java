package com.cx.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class DateBase {
	private static Connection conn = null;
	public static Connection getCon(){
		if(conn == null){
			try {
				Class.forName("com.mysql.jdbc.Driver");
				String url = "jdbc:mysql://localhost:3306/bookentry";
				String username = "root";
				String password = "root";
				conn = DriverManager.getConnection(url,username,password);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "数据库服务未开启","数据库连接失败",0);
			}
		}
		return conn;
	}
}
