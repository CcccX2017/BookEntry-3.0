package com.cx.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.cx.bean.BillBean;
import com.cx.dao.DateBase;

public class ControlBill {
	
	public static int insertBill(String sql , BillBean billBean){
		int result = 0;
		try {
			Connection conn = DateBase.getCon();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, billBean.getUsername());
			pstmt.setString(2, billBean.getBdate());
			pstmt.setString(3, billBean.getShouzhi());
			pstmt.setString(4, billBean.getType());
			pstmt.setFloat(5, billBean.getMoney());
			result = pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			// TODO: handle exception
		}
		return result;
	}
	
	public static ResultSet selectBill(String sql){
		ResultSet rs = null;
		try {
			Connection conn = DateBase.getCon();
			Statement stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
		}
		return rs;
	}
	
	public static int deleteBill(String sql){
		int result = 0;
		try {
			Connection conn = DateBase.getCon();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			result = pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
		}
		return result;
	}
	
	public static int updateBill(String sql , BillBean billBean){
		int result = 0;
		try {
			Connection conn = DateBase.getCon();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, billBean.getBdate());
			pstmt.setString(2, billBean.getShouzhi());
			pstmt.setString(3, billBean.getType());
			pstmt.setFloat(4, billBean.getMoney());
			result = pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
		}
		return result;
	}
}
