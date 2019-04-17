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
	
	public List<RejectedDocumentVO> getRejectedDocument(int empNo) {
		List<RejectedDocumentVO> list = new ArrayList<RejectedDocumentVO>();
		EmpDAO empDao = new EmpDAO();
		int teamLeaderNo = empDao.getTeamLeaderNoFromEmpNo(empNo);
		EmpVO ceoVo = empDao.getCeoVO();
		int teamCode = empDao.getTeamCodeFromEmpNo(empNo);
		empDao.dbClose();

		try {
			pstmt = conn.prepareStatement(
					"SELECT * from  approval a, document b "
					+ "WHERE a.approval_order=2 AND a.type = 1 AND a.approved=2 AND a.approver = ? AND a.doc_no = b.doc_no"
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
			
			
			pstmt = conn.prepareStatement(
					"SELECT * from  approval a, document b "
					+ "WHERE a.team_code = ? and a.approval_order=3 AND a.type = 1 AND a.approved=2 AND a.approver = ? AND a.doc_no = b.doc_no"
					);
			pstmt.setInt(1, teamCode);
			pstmt.setInt(2, ceoVo.getEmpNo());
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
					"SELECT * from approval a, document b "
					+ "WHERE team_code = ? and type=2 and approval_order=2 and approved=2 and a.doc_no = b.doc_no AND a.doc_no IN "
					+ "(SELECT doc_no from approval a WHERE a.team_code = ? and type = 2 and approval_order=2 and approved=2 " 
					+ "except (SELECT doc_no from approval a WHERE a.team_code = ? and TYPE = 2 and approval_order=3 and approved=2))"
					);
			pstmt.setInt(1, teamCode);
			pstmt.setInt(2, teamCode);
			pstmt.setInt(3, teamCode);
			
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
	
	public List<RejectedDocumentVO> getDocumentReturnedConfirmed(int teamLeaderNo) {
		List<RejectedDocumentVO> list = new ArrayList<RejectedDocumentVO>();
		
		EmpDAO empDao = new EmpDAO();
		int teamCode = empDao.getTeamCodeFromEmpNo(teamLeaderNo);
		empDao.dbClose();

		try {
			pstmt = conn.prepareStatement(
					/*
					"SELECT * FROM approval a, document b "
					+ "WHERE TYPE=2 and team_code = ? and approver = ? and approval_order=3 and approved=2 and a.doc_no = b.doc_no AND a.doc_no IN "
					+ "(SELECT doc_no FROM approval WHERE type=2 AND team_code = ? AND approver = ? AND approval_order=3 AND approved=2)"
					*/
					/*"SELECT * FROM document a, "
					+ "(SELECT * FROM approval WHERE type=2 AND team_code = ? AND approver = ? AND approval_order=3 AND approved=2) b "
					+ "WHERE a.doc_no=b.doc_no;"
					*/
					"SELECT * FROM document a, "
					+ "(SELECT * FROM approval WHERE team_code = ? AND type=2 AND approval_order=2 AND approved=2 AND doc_no IN "
					+ "(SELECT doc_no FROM approval WHERE type=2 AND team_code = ? AND approval_order=3 AND approved=2)) b "
					+ "WHERE a.doc_no = b.doc_no"
					);
			/*
			pstmt.setInt(1, teamCode);
			pstmt.setInt(2, teamLeaderNo);
			pstmt.setInt(3, teamCode);
			pstmt.setInt(4, teamLeaderNo);
			*/
			/*
			pstmt.setInt(1, teamCode);
			pstmt.setInt(2, teamLeaderNo);
			
			*/
			pstmt.setInt(1, teamCode);
			pstmt.setInt(2, teamCode);
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
	
	public List<RejectedDocumentVO> getDocumentReturnedReceived(int empNo) {
		List<RejectedDocumentVO> list = new ArrayList<RejectedDocumentVO>();
		
		EmpDAO empDao = new EmpDAO();
		int teamCode = empDao.getTeamCodeFromEmpNo(empNo);
		empDao.dbClose();

		try {
			//extract data from reviewer
			pstmt = conn.prepareStatement(
					"SELECT * FROM document d, "
					+ "(SELECT a.* FROM approval a, approval b WHERE a.team_code=b.team_code AND a.approval_order=2 "
					+ "AND a.approved=2 AND b.approval_order=3 AND b.approved=2 AND a.doc_no=b.doc_no) c " 
					+ "WHERE d.doc_no=c.doc_no AND d.doc_no LIKE ?"
					);
			pstmt.setString(1, Integer.toString(teamCode) + "%");
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
	
	
	public List<RejectedDocumentVO> getDocumentReturnedReceivedToCEO() {
		List<RejectedDocumentVO> list = new ArrayList<RejectedDocumentVO>();
		
		try {
			pstmt = conn.prepareStatement(
					//extract data from reviewer
					"SELECT * FROM document a, " + 
					"(SELECT * FROM approval WHERE type=2 and approval_order=2 AND approved=2 AND doc_no IN " + 
					"(SELECT doc_no FROM approval WHERE type=1 AND approval_order=3 AND approved=1 AND doc_no IN " + 
					"(SELECT doc_no FROM approval WHERE type=2 AND approval_order=3 AND approved=2))) b " + 
					"WHERE a.doc_no=b.doc_no"
					);
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
