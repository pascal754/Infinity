package net.infinity.db;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class TeamDAO {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	public TeamDAO() {
			
		try {
			Context init = new InitialContext();
			DataSource ds = (DataSource)init.lookup("java:comp/env/jdbc/infinity");
			conn = ds.getConnection();
			System.out.println("success");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("DB connection failed");
		}
	}
	
	
	public void dbClose() {
		if (conn != null)
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	public TeamVO getTeamVO(int teamCode) {
		TeamVO teamVO = new TeamVO();
		try {
			pstmt = conn.prepareStatement("SELECT * FROM team WHERE team_code = ?");
			pstmt.setInt(1,  teamCode);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				teamVO.setTeamCode(teamCode);
				teamVO.setTeamName(rs.getString("team_name"));
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {if (rs != null) {rs.close();}} catch (Exception e) {e.printStackTrace();}
			try {if (pstmt != null) {pstmt.close();}} catch (Exception e) {e.printStackTrace();}
		}
		
		return teamVO;
	}
	
	public TeamVO getTeamVO(String teamName) {
		TeamVO teamVO = new TeamVO();
		try {
			pstmt = conn.prepareStatement("SELECT * FROM team WHERE team_name = ?");
			pstmt.setString(1,  teamName);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				teamVO.setTeamCode(rs.getInt("team_code"));
				teamVO.setTeamName(teamName);
				System.out.println("team code in TeamDAO");
				System.out.println(teamVO.getTeamCode());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {if (rs != null) {rs.close();}} catch (Exception e) {e.printStackTrace();}
			try {if (pstmt != null) {pstmt.close();}} catch (Exception e) {e.printStackTrace();}
		}
		
		return teamVO;
	}
}
