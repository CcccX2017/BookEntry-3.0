package com.cx.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.cx.dao.DateBase;
import com.cx.ui.Login;
import com.cx.bean.LiveMoneyBean;

public class ControlLiveMoney {
	public static int insertLiveMoney(String sql , LiveMoneyBean liveMoneyBean){
		int result = 0;
		try {
			Connection conn = DateBase.getCon();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,liveMoneyBean.getUsername());
			pstmt.setString(2,liveMoneyBean.getStartDate());
			pstmt.setString(3,liveMoneyBean.getEndDate());
			pstmt.setFloat(4,liveMoneyBean.getLivemoney());
			pstmt.setInt(5, liveMoneyBean.getStartMonth());
			pstmt.setInt(6, liveMoneyBean.getStartDay());
			result = pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
		}
		return result;
	}
	public static LiveMoneyBean selectLiveMoney(String sql){
		LiveMoneyBean liveMoneyBean = new LiveMoneyBean();
		try {
			Connection conn = DateBase.getCon();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				liveMoneyBean.setUsername(rs.getString(1));
				liveMoneyBean.setStartDate(rs.getString(2));
				liveMoneyBean.setEndDate(rs.getString(3));
				liveMoneyBean.setLivemoney(rs.getFloat(4));
				liveMoneyBean.setStartMonth(rs.getInt(5));
				liveMoneyBean.setStartDay(rs.getInt(6));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
		}
		return liveMoneyBean;
	}
	
	public static ResultSet selectPartLiveMoney(){
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String username = Login.username;
		try {
			String sql = "select startDate,endDate,livemoney from livemoney where username = '"+username+"'";
			conn = DateBase.getCon();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
		}
		return rs;
	}
	
	public static int updateLiveMoney(String sql , LiveMoneyBean liveMoneyBean){
		int result = 0;
		try {
			Connection conn = DateBase.getCon();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, liveMoneyBean.getStartDate());
			pstmt.setString(2, liveMoneyBean.getEndDate());
			pstmt.setFloat(3, liveMoneyBean.getLivemoney());
			pstmt.setInt(4, liveMoneyBean.getStartMonth());
			pstmt.setInt(5, liveMoneyBean.getStartDay());
			result = pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
		}
		return result;
	}
	
	public static int deleteLiveMoney(String sql){
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
}
