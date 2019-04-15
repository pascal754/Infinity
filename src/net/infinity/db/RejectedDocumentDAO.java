package net.infinity.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class RejectedDocumentDAO {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	public RejectedDocumentDAO() {
			
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
	
	public List<RejectedDocumentVO> getRejectedDocument(int teamLeaderNo) {
		List<RejectedDocumentVO> list = new ArrayList<RejectedDocumentVO>();

		try {
			pstmt = conn.prepareStatement(
					"SELECT * from  approval a, document b "
					+ "WHERE  a.approval_order=2 AND a.type = 1 AND a.approved=2 AND a.approver = ? AND a.doc_no = b.doc_no"
					);
			pstmt.setInt(1, teamLeaderNo);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				RejectedDocumentVO rejDocVo = new RejectedDocumentVO();
				rejDocVo.setDocNo(rs.getString("doc_no"));
				rejDocVo.setEmpNo(rs.getInt("emp_no"));
				rejDocVo.setTitle(rs.getString("title"));
				rejDocVo.setContent(rs.getString("content"));
				rejDocVo.setStartTime(rs.getTimestamp("start_time"));
				rejDocVo.setSaveTime(rs.getTimestamp("save_time"));
				rejDocVo.setRejectedDate(rs.getDate("approved_time"));
				rejDocVo.setComment(rs.getString("comment"));
				rejDocVo.setApprovalOrder(rs.getInt("approval_order"));
				rejDocVo.setApprover(rs.getInt("approver"));
				
				list.add(rejDocVo);
			}
			
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
			
			EmpDAO empDao = new EmpDAO();
			EmpVO ceoVo = empDao.getCeoVO();
			empDao.dbClose();
			
			
			pstmt = conn.prepareStatement(
					"SELECT * from  approval a, document b "
					+ "WHERE  a.approval_order=3 AND a.type = 1 AND a.approved=2 AND a.approver = ? AND a.doc_no = b.doc_no"
					);
			pstmt.setInt(1, ceoVo.getEmpNo());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				RejectedDocumentVO rejDocVo = new RejectedDocumentVO();
				rejDocVo.setDocNo(rs.getString("doc_no"));
				rejDocVo.setEmpNo(rs.getInt("emp_no"));
				rejDocVo.setTitle(rs.getString("title"));
				rejDocVo.setContent(rs.getString("content"));
				rejDocVo.setStartTime(rs.getTimestamp("start_time"));
				rejDocVo.setSaveTime(rs.getTimestamp("save_time"));
				rejDocVo.setRejectedDate(rs.getDate("approved_time"));
				rejDocVo.setComment(rs.getString("comment"));
				rejDocVo.setApprovalOrder(rs.getInt("approval_order"));
				rejDocVo.setApprover(rs.getInt("approver"));
				
				list.add(rejDocVo);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
		
		return list;
	}
	
	
	public List<RejectedDocumentVO> getRejectedDocumentByCEO(int empNo) {
		List<RejectedDocumentVO> list = new ArrayList<RejectedDocumentVO>();

		try {
			pstmt = conn.prepareStatement(
					"SELECT * from  approval a, document b "
					+ "WHERE  a.approval_order=3 AND a.type = 1 AND a.approved=2 AND a.approver = ? AND a.doc_no = b.doc_no"
					);
			pstmt.setInt(1, empNo);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				RejectedDocumentVO rejDocVo = new RejectedDocumentVO();
				rejDocVo.setDocNo(rs.getString("doc_no"));
				rejDocVo.setEmpNo(rs.getInt("emp_no"));
				rejDocVo.setTitle(rs.getString("title"));
				rejDocVo.setContent(rs.getString("content"));
				rejDocVo.setStartTime(rs.getTimestamp("start_time"));
				rejDocVo.setSaveTime(rs.getTimestamp("save_time"));
				rejDocVo.setRejectedDate(rs.getDate("approved_time"));
				rejDocVo.setComment(rs.getString("comment"));
				rejDocVo.setApprovalOrder(rs.getInt("approval_order"));
				rejDocVo.setApprover(rs.getInt("approver"));
				
				list.add(rejDocVo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
		
		return list;
	}
	
	public List<RejectedDocumentVO> getDocumentReturnedPendingToTeamLeader(int empNo) {
		List<RejectedDocumentVO> list = new ArrayList<RejectedDocumentVO>();
		
		EmpDAO empDao = new EmpDAO();
		int teamCode = empDao.getTeamCodeFromEmpNo(empNo);
		empDao.dbClose();

		try {
			pstmt = conn.prepareStatement(
					"SELECT * from approval a, document b WHERE a.team_code=? and TYPE = 2 and approval_order=2 and approved=2 AND a.doc_no=b.doc_no"
					);
			pstmt.setInt(1, teamCode);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				RejectedDocumentVO rejDocVo = new RejectedDocumentVO();
				rejDocVo.setDocNo(rs.getString("doc_no"));
				rejDocVo.setEmpNo(rs.getInt("emp_no"));
				rejDocVo.setTitle(rs.getString("title"));
				rejDocVo.setContent(rs.getString("content"));
				rejDocVo.setStartTime(rs.getTimestamp("start_time"));
				rejDocVo.setSaveTime(rs.getTimestamp("save_time"));
				rejDocVo.setRejectedDate(rs.getDate("approved_time"));
				rejDocVo.setComment(rs.getString("comment"));
				rejDocVo.setApprovalOrder(rs.getInt("approval_order"));
				rejDocVo.setApprover(rs.getInt("approver"));
				
				list.add(rejDocVo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
		
		return list;
	}
}