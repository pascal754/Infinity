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

public class ApprovalDAO {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	public ApprovalDAO() {
			
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
	
	public ApprovalVO getApprovalVO(String docNo) {
		ApprovalVO appVO = new ApprovalVO();
		try {
			pstmt = conn.prepareStatement(
					"SELECT * FROM document WHERE doc_no = ?"
					);
			pstmt.setString(1,  docNo);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				appVO.setDocNo(docNo);
				appVO.setType(rs.getInt("type"));
				appVO.setTeamCode(rs.getInt("team_code"));
				appVO.setApprover(rs.getInt("approver"));
				appVO.setApprovalOrder(rs.getInt("approver_order"));
				appVO.setApproved(rs.getInt("approved"));
				appVO.setApprovedTime(rs.getTimestamp("approved_time"));
				appVO.setComment(rs.getString("comment"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {if (rs != null) {rs.close();}} catch (Exception e) {e.printStackTrace();}
			try {if (pstmt != null) {pstmt.close();}} catch (Exception e) {e.printStackTrace();}
		}
		
		return appVO;
	}
	
	
	public List<String> getReceivers(String docNo) {
		List<String> receivers = new ArrayList<String>();
		try {
			pstmt = conn.prepareStatement(
					"select team_name from team where team_code in (select team_code from approval where doc_no = ? and type = 2)"
					);
			pstmt.setString(1,  docNo);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				receivers.add(rs.getString(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {if (rs != null) {rs.close();}} catch (Exception e) {e.printStackTrace();}
			try {if (pstmt != null) {pstmt.close();}} catch (Exception e) {e.printStackTrace();}
		}
		
		return receivers;
	}
	
	
	public List<Integer> getReceiversCode(String docNo) {
		List<Integer> receivers = new ArrayList<Integer>();
		try {
			pstmt = conn.prepareStatement(
					"select team_code from approval where doc_no = ? AND type = 2 AND approval_order=1"
					);
			pstmt.setString(1,  docNo);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				receivers.add(rs.getInt(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {if (rs != null) {rs.close();}} catch (Exception e) {e.printStackTrace();}
			try {if (pstmt != null) {pstmt.close();}} catch (Exception e) {e.printStackTrace();}
		}
		
		return receivers;
	}
	
	
	public int getApprovalLine(String docNo) {
		int approvalLine = 0;
		try {
			pstmt = conn.prepareStatement(
					"select max(approval_order) from approval where doc_no = ? and type = 1;"
					);
			pstmt.setString(1,  docNo);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				approvalLine = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {if (rs != null) {rs.close();}} catch (Exception e) {e.printStackTrace();}
			try {if (pstmt != null) {pstmt.close();}} catch (Exception e) {e.printStackTrace();}
		}
		
		return approvalLine;
	}
	
	
	public void cancelReportToTeamLeader(String docNo) {
		try {
			System.out.println("cancelReportToTeamLeader doc no: " + docNo);
			pstmt = conn.prepareStatement(
					"update approval set approved = 0, approved_time = now()" + 
					"where doc_no = ? and type = 1 and approval_order = 1"
					);
			pstmt.setString(1, docNo);
			if (pstmt.executeUpdate() == 1)
				System.out.println("cancelReportToTeamLeader succeeded");
			else
				System.out.println("cancelReportToTeamLeader failed");
		} catch (SQLException e ) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
	}
	
	
	public Date getApprovedDate(String docNo, int approver) {
		Date date = null;
		try {
			pstmt = conn.prepareStatement(
					"select approved_time from approval where doc_no = ? and approver = ? and approved=1"
					);
			pstmt.setString(1, docNo);
			pstmt.setInt(2, approver);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				date = rs.getDate("approved_time");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
		
		return date;
	}
	

	public void completeReportByCEO(String docNo, int approver) {
		try {
			pstmt = conn.prepareStatement(
					"update approval set approved=1, approved_time=now()"
					+ " where doc_no = ?"
					+ " and approval_order=3 and type=1 and approver = ?"
					);
			pstmt.setString(1, docNo);
			pstmt.setInt(2, approver);
			if (pstmt.executeUpdate() == 1)
				System.out.println("completeReportByCEO succeeded");
			else
				System.out.println("completeReportByCEO failed");
		} catch (SQLException e ) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
	}
	
	
	public List<DocumentVO> getCompleteByTeamMember(int empNo) {
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
	
	public List<DocumentVO> getCompleteByTeamLeader(int teamLeaderNo) {
		List<DocumentVO> listByTeamLeader = new ArrayList<DocumentVO>();
		List<DocumentVO> listByCEO = new ArrayList<DocumentVO>();
		try {
			pstmt = conn.prepareStatement(
					"select * from document where doc_no in "
					+ "(select doc_no from approval where approver = ? and approved = 1 and type=1)"
					);
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
				listByTeamLeader.add(docVo);
			}
			
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
			
			pstmt = conn.prepareStatement(
					"select * from document where doc_no in "
					+ "(select doc_no from approval where doc_no in "
					+ "(select doc_no from approval where approver = ? and approved = 1 and type=1) "
					+ "and approval_order=3 and approved=0)");
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
					+ "(select doc_no from approval where approver = ? and approved = 1 and type=1) "
					+ "and approval_order=3 and approved=2)");
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
	
	public Date getFinalApprovedDate(String docNo) {
		Date date = null;
		try {
			pstmt = conn.prepareStatement(
					"select approved_time from approval where approval_order=(select max(approval_order) "
					+ "from approval where doc_no = ?) and doc_no = ? and approved=1");
			pstmt.setString(1, docNo);
			pstmt.setString(2, docNo);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				date = rs.getDate(1);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
		
		return date;
	}
	
	public int setApproval(String docNo, int teamLeaderNo, int value) {
		int result = 0;
		try {
			pstmt = conn.prepareStatement(
					"update approval set approved = ?, approved_time=now() "
					+ "where doc_no = ? and approver = ? and approval_order=1 and type=2"
					);
			pstmt.setInt(1, value);
			pstmt.setString(2, docNo);
			pstmt.setInt(3,  teamLeaderNo);
			result = pstmt.executeUpdate();

		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
		
		return result;
	}
	
	public int addReviewer(String docNo, int empNo) {
		int result = 0;
		try {
			pstmt = conn.prepareStatement(
					"insert into approval (doc_no, type, team_code, approver, approval_order, approved) " 
					+ "values(?, 2, (select team_code from emp where emp_no = ?), ?, 2, 0)"
					);
			pstmt.setString(1, docNo);
			pstmt.setInt(2, empNo);
			pstmt.setInt(3, empNo);

			result = pstmt.executeUpdate();

		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
		
		return result;
		
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
	
	public int completeReceiving(String docNo, int empNo) {
		int result = 0;
		try {
			pstmt = conn.prepareStatement(
					"update approval set approved=1, approved_time=now() "
					+ "where doc_no = ? and approver = ? and approval_order=2 and type=2"
					);
			pstmt.setString(1, docNo);
			pstmt.setInt(2, empNo);

			result = pstmt.executeUpdate();
			
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
		
		return result;
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
	
	public String getReviewerName(String docNo, int teamCode) {
		String reviewer = "";
		try {
			pstmt = conn.prepareStatement(
					"select name from emp where emp_no="
					+ "(select approver from approval where doc_no = ? and approval_order=2 and type=2 and team_code = ?)"
					);
			pstmt.setString(1, docNo);
			pstmt.setInt(2, teamCode);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				reviewer = rs.getString(1);
			}

		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
		
		return reviewer;
	}
	
	
	public int getReviewerStatus(String docNo, int teamCode) {
		int status = 0;
		try {
			pstmt = conn.prepareStatement(
					"SELECT approved from approval where doc_no = ? and approval_order=2 and type=2 and team_code = ?"
					);
			pstmt.setString(1, docNo);
			pstmt.setInt(2, teamCode);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				status = rs.getInt(1);
			}

		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
		
		return status;
	}
	
	
	public Date getAssignReviewerDate(String docNo, int teamCode) {
		Date date = null;
		
		try {
			pstmt = conn.prepareStatement(
					"select approved_time from approval where doc_no = ? and approval_order=1 and approved=1 and type=2 and team_code = ?"
					);
			pstmt.setString(1, docNo);
			pstmt.setInt(2, teamCode);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				date = rs.getDate(1);
			}

		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
		
		return date;
	}
	
	public Date getReviewDate(String docNo, int teamCode) {
		Date date = null;
		
		try {
			pstmt = conn.prepareStatement(
					"select approved_time from approval where doc_no = ? and approval_order=2 and approved=1 and type=2 and team_code = ?"
					);
			pstmt.setString(1, docNo);
			pstmt.setInt(2, teamCode);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				date = rs.getDate(1);
			}

		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
		
		return date;
	}
	
	
	public Date getRejectedDate(String docNo, int teamCode) {
		Date date = null;
		
		try {
			pstmt = conn.prepareStatement(
					"select approved_time from approval where doc_no = ? and approval_order=2 and approved=2 and type=2 and team_code = ?"
					);
			pstmt.setString(1, docNo);
			pstmt.setInt(2, teamCode);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				date = rs.getDate(1);
			}

		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
		
		return date;
	}
	
	public ReceiverStatus getReceiverStatus(String docNo, int teamCode) {
		ReceiverStatus rs = new ReceiverStatus();
		int status = 0;
		String name = getReviewerName(docNo, teamCode);
		
		if (name.equals("")) {
			rs.name = "";
			rs.date = null;
			rs.approval = ApprovalStatus.PENDING;
			return rs;
		}
		
		rs.name = name;
		status = getReviewerStatus(docNo, teamCode);
		
		if (status == 0) {
			rs.date = getAssignReviewerDate(docNo, teamCode);
			rs.approval = ApprovalStatus.RECEIVING;
		} else if (status == 1) {
			rs.date = getReviewDate(docNo, teamCode);
			rs.approval = ApprovalStatus.APPROVED;
		} else if (status == 2) {
			rs.date = getRejectedDate(docNo, teamCode);;
			rs.approval = ApprovalStatus.REJECTED;
		}
		System.out.println("ApprovalDAO::getReceiverStatus" + rs.name + " " + rs.date + " " + rs.approval);
		return rs;
	}
	
	public void rejectDocumentByTeamLeader(String docNo, int teamLeaderNo, String comment) {
		try {
			pstmt = conn.prepareStatement(
					"UPDATE approval SET approved=2, approved_time=NOW(), COMMENT = ? "
					+ "WHERE approver = ? AND doc_no = ? AND type=1 AND approval_order=2"
					);
			pstmt.setString(1, comment);
			pstmt.setInt(2, teamLeaderNo);
			pstmt.setString(3, docNo);

			pstmt.executeUpdate();

		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
	}
	
	
	public void rejectDocumentByCEO(String docNo, int CEONo, String comment) {
		try {
			pstmt = conn.prepareStatement(
					"UPDATE approval SET approved=2, approved_time=NOW(), COMMENT = ?" + 
					" WHERE approver = ? AND doc_no = ? AND type=1 AND approval_order=3"
					);
			pstmt.setString(1, comment);
			pstmt.setInt(2, CEONo);
			pstmt.setString(3, docNo);

			pstmt.executeUpdate();

		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
	}
	
	public void returnDocument(String docNo, int empNo, String comment) {
		try {
			pstmt = conn.prepareStatement(
					"UPDATE approval SET approved=2, approved_time=now(), comment = ? WHERE doc_no = ? AND type=2 AND approver = ? AND approval_order=2"
					);
			pstmt.setString(1,  comment);
			pstmt.setString(2, docNo);
			pstmt.setInt(3, empNo);

			pstmt.executeUpdate();

		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
	}
	
	public void returnDocumentByTeamLeader(String docNo, int teamLeaderNo) {
		EmpDAO empDao = new EmpDAO();
		int teamCode = empDao.getTeamCodeFromEmpNo(teamLeaderNo);
		empDao.dbClose();
		
		try {
			pstmt = conn.prepareStatement(
					"INSERT INTO approval "
					+ "(doc_no, TYPE, team_code, approver, approval_order, approved, approved_time) "
					+ "VALUES (?, 2, ?, ?, 3, 2, NOW());"
					);
			pstmt.setString(1,  docNo);
			pstmt.setInt(2, teamCode);
			pstmt.setInt(3, teamLeaderNo);

			pstmt.executeUpdate();

		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
	}
}

