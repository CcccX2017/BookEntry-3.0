package com.cx.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.cx.dao.*;

public class LoginCheck {
	public static int logincheck(String username,String password){
		int result = 0;
		String sql = "select username,password from user";
		try {
			Connection conn = DateBase.getCon();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				if(rs.getString(1).equals(username)){
					if(rs.getString(2).equals(password)){
						result = 1;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
}
