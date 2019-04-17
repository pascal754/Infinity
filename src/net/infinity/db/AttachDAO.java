package net.infinity.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class AttachDAO {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	public AttachDAO() {
			
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
	
	public List<String> getFilename(String doc_no) {
		List<String> filename = new ArrayList<String>();
		try {
			pstmt = conn.prepareStatement("SELECT filename FROM attach WHERE doc_no = ?");
			pstmt.setString(1,  doc_no);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				filename.add(rs.getString(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {if (rs != null) {rs.close();}} catch (Exception e) {e.printStackTrace();}
			try {if (pstmt != null) {pstmt.close();}} catch (Exception e) {e.printStackTrace();}
		}
		
		return filename;
	}
	
	public void FileDelete(String doc_no) {
		
		try {
			pstmt = conn.prepareStatement("DELETE FROM attach WHERE doc_no = ?");
			pstmt.setString(1,  doc_no);
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {if (pstmt != null) {pstmt.close();}} catch (Exception e) {e.printStackTrace();}
		}
		
	}
public void FileNameDelete(String filename) {
		
		try {
			pstmt = conn.prepareStatement("DELETE FROM attach WHERE filename = ?");
			pstmt.setString(1,  filename);
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {if (pstmt != null) {pstmt.close();}} catch (Exception e) {e.printStackTrace();}
		}
		
	}
}