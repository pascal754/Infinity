package net.infinity.db;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class DocumentDAO {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	public DocumentDAO() {
			
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
	
	public List<DocumentVO> getDraft(int empNo) {
		List<DocumentVO> list = new ArrayList<DocumentVO>();
		try {
			pstmt = conn.prepareStatement("select * from document where doc_no in "
					+ "(select doc_no from approval where approver=? and approval_order=1 and approved=0 and type=1)"
					);
			pstmt.setInt(1, empNo);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				DocumentVO docVo = new DocumentVO();
				docVo.setDocNo(rs.getString("doc_no"));
				docVo.setEmpNo(rs.getInt("emp_no"));
				docVo.setTitle(rs.getString("title"));
				docVo.setContent(rs.getString("content"));
				docVo.setStartTime(rs.getTimestamp("start_time"));
				docVo.setSaveTime(rs.getTimestamp("save_time"));
				list.add(docVo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
		
		
		return list;
	}
	
	public List<DocumentVO> getDocumentBeingSent(int empNo) {
		List<DocumentVO> list = new ArrayList<DocumentVO>();

		try {
			pstmt = conn.prepareStatement("select * from document where doc_no in " + 
					" (select doc_no from approval where doc_no in\r\n" + 
					" (select doc_no from approval where approver = ? and approval_order=1 and approved=1 and type=1)" + 
					" and approval_order=2 and approved=0 and type=1)"
					);
			pstmt.setInt(1, empNo);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				DocumentVO docVo = new DocumentVO();
				docVo.setDocNo(rs.getString("doc_no"));
				docVo.setEmpNo(rs.getInt("emp_no"));
				docVo.setTitle(rs.getString("title"));
				docVo.setContent(rs.getString("content"));
				docVo.setStartTime(rs.getTimestamp("start_time"));
				docVo.setSaveTime(rs.getTimestamp("save_time"));
				list.add(docVo);
			}
			
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
			
			pstmt = conn.prepareStatement("select * from document where doc_no in" + 
					" (select doc_no from approval where doc_no in (select doc_no from approval where doc_no in" + 
					" (select doc_no from approval where approver= ? and approval_order=1 and approved=1 and type=1)" + 
					" and approval_order=2 and approved=1 and type=1)\r\n" + 
					" and approval_order=3 and approved=0 and type=1)"
					);
			pstmt.setInt(1, empNo);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				DocumentVO docVo = new DocumentVO();
				docVo.setDocNo(rs.getString("doc_no"));
				docVo.setEmpNo(rs.getInt("emp_no"));
				docVo.setTitle(rs.getString("title"));
				docVo.setContent(rs.getString("content"));
				docVo.setStartTime(rs.getTimestamp("start_time"));
				docVo.setSaveTime(rs.getTimestamp("save_time"));
				list.add(docVo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
		
		return list;
	}
	
	
	public DocumentVO getDraftDocument(String docNo) {
		DocumentVO docVo = new DocumentVO();
		
		try {
			pstmt = conn.prepareStatement("select * from document where doc_no = ?");
			pstmt.setString(1, docNo);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				docVo.setDocNo(docNo);
				docVo.setEmpNo(rs.getInt("emp_no"));
				docVo.setTitle(rs.getString("title"));
				docVo.setContent(rs.getString("content"));
				docVo.setStartTime(rs.getTimestamp("start_time"));
				docVo.setSaveTime(rs.getTimestamp("save_time"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
		
		return docVo;
	}
	

	public void reportToTeamLeader(String docNo) {
		try {
			pstmt = conn.prepareStatement("update approval set approved = 1, approved_time = now()" + 
				"where doc_no = ? and type = 1 and approval_order = 1");
			pstmt.setString(1, docNo);
			if (pstmt.executeUpdate() == 1)
				System.out.println("reportToTeamLeader succeeded");
			else
				System.out.println("reportToTeamLeader failed");
		} catch (SQLException e ) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
	}
	
	
	public void reportToCEO(String docNo) {
		try {
			pstmt = conn.prepareStatement("update approval set approved = 1, approved_time = now()" + 
				"where doc_no = ? and type = 1 and approval_order = 2");
			pstmt.setString(1, docNo);
			if (pstmt.executeUpdate() == 1)
				System.out.println("reportToCEO succeeded");
			else
				System.out.println("reportToCEO failed");
		} catch (SQLException e ) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
	}
	
	
	public List<DocumentVO> getDocumentPendingSendingToTeamLeader(int teamLeaderNo) {
		List<DocumentVO> list = new ArrayList<DocumentVO>();
		try {
			pstmt = conn.prepareStatement("select * from document where doc_no in" + 
					" (select doc_no from approval where doc_no in" + 
					" (select doc_no from approval where team_code in" + 
					" (select team_code from emp where emp_no= ?) and approval_order=1 and approved=1 and type=1)" + 
					" and approver= ? and approval_order=2 and approved=0 and type=1)"
					);
			pstmt.setInt(1, teamLeaderNo);
			pstmt.setInt(2, teamLeaderNo);
			rs = pstmt.executeQuery();
			while (rs.next() ) {
				DocumentVO docVo = new DocumentVO();
				docVo.setDocNo(rs.getString("doc_no"));
				docVo.setEmpNo(rs.getInt("emp_no"));
				docVo.setTitle(rs.getString("title"));
				docVo.setContent(rs.getString("content"));
				docVo.setStartTime(rs.getTimestamp("start_time"));
				docVo.setSaveTime(rs.getTimestamp("save_time"));
				list.add(docVo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
			
		return list;
	}
	
	
	public List<DocumentVO> getPendingSendingToCEO(int empNo) {
		List<DocumentVO> list = new ArrayList<DocumentVO>();
		try {
			pstmt = conn.prepareStatement("select * from document where doc_no in"
					+ " (select doc_no from approval where doc_no in (select doc_no from approval where approval_order=2 and approved=1)"
					+ "	and approval_order = 3 and approved=0)"
					);
			rs = pstmt.executeQuery();
			while (rs.next() ) {
				DocumentVO docVo = new DocumentVO();
				docVo.setDocNo(rs.getString("doc_no"));
				docVo.setEmpNo(rs.getInt("emp_no"));
				docVo.setTitle(rs.getString("title"));
				docVo.setContent(rs.getString("content"));
				docVo.setStartTime(rs.getTimestamp("start_time"));
				docVo.setSaveTime(rs.getTimestamp("save_time"));
				list.add(docVo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
		System.out.println("DocumentDAO::getPendingSendingToCEO executed");
		return list;
	}
	
	
	public List<DocumentVO> getDocumentCompleteByCEO() {
		List<DocumentVO> list = new ArrayList<DocumentVO>();

		try {
			pstmt = conn.prepareStatement("select * from document where doc_no in (select doc_no from approval where approval_order=3 and approved=1)"
					);
			rs = pstmt.executeQuery();
			while (rs.next() ) {
				DocumentVO docVo = new DocumentVO();
				docVo.setDocNo(rs.getString("doc_no"));
				docVo.setEmpNo(rs.getInt("emp_no"));
				docVo.setTitle(rs.getString("title"));
				docVo.setContent(rs.getString("content"));
				docVo.setStartTime(rs.getTimestamp("start_time"));
				docVo.setSaveTime(rs.getTimestamp("save_time"));
				list.add(docVo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
		
		System.out.println("DocumentDAO::getDocumentCompleteByCEO executed");		
		return list;
	}
	
	public List<DocumentVO> getDocumentPendingReceiving(int empNo) {
		List<DocumentVO> list = new ArrayList<DocumentVO>();
		DocumentDAO docDao = new DocumentDAO();

		try {
			pstmt = conn.prepareStatement("select * from document where doc_no in "
					+ "(select doc_no from approval where team_code = "
					+ "(select team_code from emp where emp_no= ?) "
					+ "and type=2 and approval_order=1 and approved=0)"
					);
			pstmt.setInt(1, empNo);
			rs = pstmt.executeQuery();
			while (rs.next() ) {
				DocumentVO docVo = new DocumentVO();
				docVo.setDocNo(rs.getString("doc_no"));
				docVo.setEmpNo(rs.getInt("emp_no"));
				docVo.setTitle(rs.getString("title"));
				docVo.setContent(rs.getString("content"));
				docVo.setStartTime(rs.getTimestamp("start_time"));
				docVo.setSaveTime(rs.getTimestamp("save_time"));
				
				if (docDao.getFinalSenderApprovalStatus(docVo.getDocNo()))
					list.add(docVo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
		docDao.dbClose();
		System.out.println("DocumentDAO::getDocumentPendingReceiving executed");		
		return list;
	}
	
	public boolean getFinalSenderApprovalStatus(String docNo) {
		int status = 0;
		try {
			pstmt = conn.prepareStatement("select approved from approval "
					+ "where approval_order=(select max(approval_order) from approval where doc_no = ?)"
					+ " and doc_no = ?;"
					);
			pstmt.setString(1, docNo);
			pstmt.setString(2, docNo);
			rs = pstmt.executeQuery();
			if (rs.next() ) {
				status = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
		return  (status == 0) ? false : true;
	}
	
	public List<DocumentVO> getCompleteReceivingByTeamMember(int empNo) {
		List<DocumentVO> list = new ArrayList<DocumentVO>();
		try {
			pstmt = conn.prepareStatement("select * from document where doc_no in "
					+ "(select doc_no from approval where approver = ? and approval_order=2 and approved=1 and type=2)"
					);
			pstmt.setInt(1, empNo);
			rs = pstmt.executeQuery();
			while (rs.next() ) {
				DocumentVO docVo = new DocumentVO();
				docVo.setDocNo(rs.getString("doc_no"));
				docVo.setEmpNo(rs.getInt("emp_no"));
				docVo.setTitle(rs.getString("title"));
				docVo.setContent(rs.getString("content"));
				docVo.setStartTime(rs.getTimestamp("start_time"));
				docVo.setSaveTime(rs.getTimestamp("save_time"));

				list.add(docVo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
		
		return list;
	}
	
	public List<DocumentVO> getDocumentBeingSentByTeam(int teamCode) {
		List<DocumentVO> list = new ArrayList<DocumentVO>();

		try {
			pstmt = conn.prepareStatement("select * from document where doc_no in" + 
					" (select doc_no from approval where doc_no in" + 
					" (select doc_no from approval where approval_order=1 and approved=1 and type=1 and team_code = ?)" + 
					" and approval_order=2 and approved=0 and type=1)"
					);
			pstmt.setInt(1, teamCode);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				DocumentVO docVo = new DocumentVO();
				docVo.setDocNo(rs.getString("doc_no"));
				docVo.setEmpNo(rs.getInt("emp_no"));
				docVo.setTitle(rs.getString("title"));
				docVo.setContent(rs.getString("content"));
				docVo.setStartTime(rs.getTimestamp("start_time"));
				docVo.setSaveTime(rs.getTimestamp("save_time"));
				list.add(docVo);
			}
			
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
			
			pstmt = conn.prepareStatement("select * from document where doc_no in" + 
					" (select doc_no from approval where doc_no in" + 
					" (select doc_no from approval where doc_no in" + 
					" (select doc_no from approval where approval_order=1 and approved=1 and type=1 and team_code = ?)" + 
					" and approval_order=2 and approved=1 and type=1)" + 
					" and approval_order=3 and approved=0 and type=1)"
					);
			pstmt.setInt(1, teamCode);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				DocumentVO docVo = new DocumentVO();
				docVo.setDocNo(rs.getString("doc_no"));
				docVo.setEmpNo(rs.getInt("emp_no"));
				docVo.setTitle(rs.getString("title"));
				docVo.setContent(rs.getString("content"));
				docVo.setStartTime(rs.getTimestamp("start_time"));
				docVo.setSaveTime(rs.getTimestamp("save_time"));
				list.add(docVo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
		
		return list;
	}
	
	public List<DocumentVO> getDocumentPendingSendingToCEOByTeam(int teamCode) {
		List<DocumentVO> list = new ArrayList<DocumentVO>();

		try {
			pstmt = conn.prepareStatement("select * from document where doc_no in" + 
					" (select doc_no from approval where doc_no in" + 
					" (select doc_no from approval where doc_no in" + 
					" (select doc_no from approval where approval_order=1 and approved=1 and type=1 and team_code = ?)" + 
					" and approval_order=2 and approved=1 and type=1)" + 
					" and approval_order=3 and approved=0 and type=1)"
					);
			pstmt.setInt(1, teamCode);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				DocumentVO docVo = new DocumentVO();
				docVo.setDocNo(rs.getString("doc_no"));
				docVo.setEmpNo(rs.getInt("emp_no"));
				docVo.setTitle(rs.getString("title"));
				docVo.setContent(rs.getString("content"));
				docVo.setStartTime(rs.getTimestamp("start_time"));
				docVo.setSaveTime(rs.getTimestamp("save_time"));
				list.add(docVo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
		
		return list;
	}
	
	public List<DocumentVO> getDocumentPendingSendingToCEO() {
		List<DocumentVO> list = new ArrayList<DocumentVO>();

		try {
			pstmt = conn.prepareStatement("select * from document where doc_no in" + 
					" (select doc_no from approval where doc_no in" + 
					" (select doc_no from approval where doc_no in" + 
					" (select doc_no from approval where approval_order=1 and approved=1 and type=1)" + 
					" and approval_order=2 and approved=1 and type=1)" + 
					" and approval_order=3 and approved=0 and type=1)"
					);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				DocumentVO docVo = new DocumentVO();
				docVo.setDocNo(rs.getString("doc_no"));
				docVo.setEmpNo(rs.getInt("emp_no"));
				docVo.setTitle(rs.getString("title"));
				docVo.setContent(rs.getString("content"));
				docVo.setStartTime(rs.getTimestamp("start_time"));
				docVo.setSaveTime(rs.getTimestamp("save_time"));
				list.add(docVo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
		
		return list;
	}
	
	public void deleteDocument(String docNo) {
		try {
			pstmt = conn.prepareStatement(
					"DELETE FROM document WHERE doc_no = ?"
					);
			pstmt.setString(1, docNo);
			pstmt.executeUpdate();
			
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
			
			pstmt = conn.prepareStatement(
					"DELETE FROM approval WHERE doc_no = ?"
					);
			pstmt.setString(1, docNo);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
	}
	
}
