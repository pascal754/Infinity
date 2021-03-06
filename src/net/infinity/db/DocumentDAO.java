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
	
	
	public List<DocumentVO> getDocumentPendingSendingToCEO(int empNo) {
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
		ApprovalDAO appDao = new ApprovalDAO();
		EmpDAO empDao = new EmpDAO();
		int teamCode = empDao.getTeamCodeFromEmpNo(empNo);
		int teamLeaderNo = empDao.getTeamLeaderNoFromEmpNo(empNo);
		empDao.dbClose();

		try {
			pstmt = conn.prepareStatement(
					"SELECT * FROM document WHERE doc_no IN " + 
					"(SELECT doc_no FROM approval WHERE TYPE=2 AND team_code = ? AND approver = ? AND approval_order=1 AND approved=0 " + 
					"except (SELECT doc_no from approval where TYPE=1 AND approval_order=2 AND approved=2) " + 
					"except (SELECT doc_no from approval where TYPE=1 AND approval_order=3 AND approved=2))"
					);
			pstmt.setInt(1, teamCode);
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
				
				if (appDao.getFinalSenderApprovalStatus(docVo.getDocNo()))
					list.add(docVo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
		appDao.dbClose();
		System.out.println("DocumentDAO::getDocumentPendingReceiving executed");		
		return list;
	}
	
	public List<DocumentVO> getDocumentCompleteReceivingByTeamMember(int empNo) {
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

	
	public List<DocumentVO> getDocumentCompleteByTeamMember(int empNo) {
		List<DocumentVO> listByTeamLeader = new ArrayList<DocumentVO>();
		List<DocumentVO> listByCEO = new ArrayList<DocumentVO>();
		EmpDAO empDao = new EmpDAO();
		int teamLeaderNo = empDao.getTeamLeaderNoFromEmpNo(empNo);
		int ceoNo = empDao.getCeoVO().getEmpNo();
		empDao.dbClose();
		
		try {
			pstmt = conn.prepareStatement(
					"select * from document where doc_no in "
					+ "(select doc_no from approval where doc_no in "
					+ "(select doc_no from approval where approver = ? and approval_order=1 and approved=1 and type=1) "
					+ "and approver = ? and approval_order=2 and approved=1 and type=1)"
					);
			pstmt.setInt(1, empNo);
			pstmt.setInt(2, teamLeaderNo);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				DocumentVO docVo = new DocumentVO();
				docVo.setDocNo(rs.getString("doc_no"));
				docVo.setEmpNo(rs.getInt("emp_no"));
				docVo.setTitle(rs.getString("title"));
				docVo.setContent(rs.getString("content"));
				docVo.setStartTime(rs.getTimestamp("start_time"));
				docVo.setSaveTime(rs.getTimestamp("save_time"));
				listByTeamLeader.add(docVo);
			}

			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
			
			pstmt = conn.prepareStatement(
					"select * from document where doc_no in "
					+ "(select doc_no from approval where doc_no in "
					+ "(select doc_no from approval where approver = ? and approval_order=1 and approved=1 and type=1) "
					+ "and approver = ? and approval_order=3 and approved=0 and type=1);"
					);
			pstmt.setInt(1, empNo);
			pstmt.setInt(2, ceoNo);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				DocumentVO docVo = new DocumentVO();
				docVo.setDocNo(rs.getString("doc_no"));
				docVo.setEmpNo(rs.getInt("emp_no"));
				docVo.setTitle(rs.getString("title"));
				docVo.setContent(rs.getString("content"));
				docVo.setStartTime(rs.getTimestamp("start_time"));
				docVo.setSaveTime(rs.getTimestamp("save_time"));
				listByCEO.add(docVo);
			}
			
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
			
			pstmt = conn.prepareStatement(
					"select * from document where doc_no in "
					+ "(select doc_no from approval where doc_no in "
					+ "(select doc_no from approval where approver = ? and approval_order=1 and approved=1 and type=1) "
					+ "and approver = ? and approval_order=3 and approved=2 and type=1);"
					);
			pstmt.setInt(1, empNo);
			pstmt.setInt(2, ceoNo);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				DocumentVO docVo = new DocumentVO();
				docVo.setDocNo(rs.getString("doc_no"));
				docVo.setEmpNo(rs.getInt("emp_no"));
				docVo.setTitle(rs.getString("title"));
				docVo.setContent(rs.getString("content"));
				docVo.setStartTime(rs.getTimestamp("start_time"));
				docVo.setSaveTime(rs.getTimestamp("save_time"));
				listByCEO.add(docVo);
			}
			
			
			for (DocumentVO x : listByCEO)
				for (int i = 0; i < listByTeamLeader.size(); ++i)
					if (listByTeamLeader.get(i).getDocNo().equals(x.getDocNo())) {
						listByTeamLeader.remove(i);
						break;
					}
			
		} catch (SQLException e ) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
		
		return listByTeamLeader;
	}
	
	public List<DocumentVO> getDocumentCompleteByTeamLeader(int teamLeaderNo) {
		List<DocumentVO> listByTeamLeader = new ArrayList<DocumentVO>();
		List<DocumentVO> listByCEO = new ArrayList<DocumentVO>();
		EmpDAO empDao = new EmpDAO();
		int teamCode = empDao.getTeamCodeFromEmpNo(teamLeaderNo);
		empDao.dbClose();
		
		try {
			pstmt = conn.prepareStatement(
					"select * from document where doc_no in "
					+ "(select doc_no from approval where approver = ? and team_code = ? and approval_order=2 and approved=1 and type=1)"
					);
			pstmt.setInt(1, teamLeaderNo);
			pstmt.setInt(2, teamCode);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				DocumentVO docVo = new DocumentVO();
				docVo.setDocNo(rs.getString("doc_no"));
				docVo.setEmpNo(rs.getInt("emp_no"));
				docVo.setTitle(rs.getString("title"));
				docVo.setContent(rs.getString("content"));
				docVo.setStartTime(rs.getTimestamp("start_time"));
				docVo.setSaveTime(rs.getTimestamp("save_time"));
				listByTeamLeader.add(docVo);
			}
			
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
			
			pstmt = conn.prepareStatement(
					"select * from document where doc_no in "
					+ "(select doc_no from approval where doc_no in "
					+ "(select doc_no from approval where approver = ? and approval_order=2 and approved=1 and type=1) "
					+ "and approval_order=3 and approved=0 and type=1)");
			pstmt.setInt(1, teamLeaderNo);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				DocumentVO docVo = new DocumentVO();
				docVo.setDocNo(rs.getString("doc_no"));
				docVo.setEmpNo(rs.getInt("emp_no"));
				docVo.setTitle(rs.getString("title"));
				docVo.setContent(rs.getString("content"));
				docVo.setStartTime(rs.getTimestamp("start_time"));
				docVo.setSaveTime(rs.getTimestamp("save_time"));
				listByCEO.add(docVo);
			}
			
			
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
			
			pstmt = conn.prepareStatement(
					"select * from document where doc_no in "
					+ "(select doc_no from approval where doc_no in "
					+ "(select doc_no from approval where approver = ? and approval_order=2 and approved=1 and type=1) "
					+ "and approval_order=3 and approved=2 and type=1)");
			pstmt.setInt(1, teamLeaderNo);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				DocumentVO docVo = new DocumentVO();
				docVo.setDocNo(rs.getString("doc_no"));
				docVo.setEmpNo(rs.getInt("emp_no"));
				docVo.setTitle(rs.getString("title"));
				docVo.setContent(rs.getString("content"));
				docVo.setStartTime(rs.getTimestamp("start_time"));
				docVo.setSaveTime(rs.getTimestamp("save_time"));
				listByCEO.add(docVo);
			}
			
			
			for (DocumentVO x : listByCEO)
				for (int i = 0; i < listByTeamLeader.size(); ++i)
					if (listByTeamLeader.get(i).getDocNo().equals(x.getDocNo())) {
						listByTeamLeader.remove(i);
						break;
					}
				
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
		return listByTeamLeader;
		
	}
	
	public List<DocumentVO> getDocumentPendingReceivingToTeamMember(int empNo) {
		List<DocumentVO> list = new ArrayList<DocumentVO>();
		
		try {
			pstmt = conn.prepareStatement(
					"select * from document where doc_no in "
					+ "(select doc_no from approval where approver = ? and type=2 and approval_order=2 and approved=0)"
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

		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
		
		return list;
	}
	
	public List<DocumentVO> getDocumentBeingReceived(int empNo) {
		List<DocumentVO> list = new ArrayList<DocumentVO>();
		
		try {
			pstmt = conn.prepareStatement(
					"select * from document where doc_no in "
					+ "(select doc_no from approval where team_code=(select team_code from emp where emp_no = ?) "
					+ "and approval_order=2 and type=2 and approved=0)"
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

		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
		
		return list;
	}
	
	public List<DocumentVO> getDocumentCompleteReceiving(int empNo) {
		List<DocumentVO> list = new ArrayList<DocumentVO>();
		
		try {
			pstmt = conn.prepareStatement(
					"select * from document where doc_no in "
					+ "(select doc_no from approval where team_code=(select team_code from emp where emp_no = ?) "
					+ "and approval_order=2 and type=2 and approved=1)"
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

		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
		
		return list;
	}
}
