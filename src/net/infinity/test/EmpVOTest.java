package net.infinity.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import net.infinity.db.EmpDAO;
import net.infinity.db.EmpVO;

public class EmpVOTest {
		public static void main(String[] args) {
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try {
				Class.forName("org.mariadb.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/infinity", "galaxy_mit", "1234");
				
				//Context init = new InitialContext();
				//DataSource ds = (DataSource)init.lookup("java:comp/env/jdbc/infinity");
				//conn = ds.getConnection();
				System.out.println("success");
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("DB connection failed");
			}
			
			EmpVO empVo = new EmpVO();
			int empNo = 101;
			int teamCode = 90001;
			int teamLeaderNo = 0;
			String teamName = "";
			
			try {
				pstmt = conn.prepareStatement("SELECT team_code FROM emp WHERE emp_no = ?");
				
				pstmt.setInt(1, empNo);
				
				rs = pstmt.executeQuery();
				if (rs.next()) {
					teamCode = rs.getInt(1);
				}
				
				if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
				if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
				
				pstmt = conn.prepareStatement("SELECT team_name FROM team WHERE team_code = ?");
				
				pstmt.setInt(1, teamCode);
				
				rs = pstmt.executeQuery();
				if (rs.next()) {
					teamName = rs.getString(1);
				}
				System.out.println(teamName);
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
				if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
				if (conn != null) try { conn.close(); } catch (Exception e) { e.printStackTrace(); }
			}
			
			
	}
}
