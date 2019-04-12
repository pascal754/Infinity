package net.infinity.db;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class saveDocument
 */
@WebServlet("/saveDocument")
public class saveDocument extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public saveDocument() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		request.setCharacterEncoding("UTF-8");
		String docNo = request.getParameter("doc_no");
		String empNo = request.getParameter("emp_no");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String startTime = request.getParameter("startTime");
		String approvalLine = request.getParameter("approvalLine");
		String[] teams = request.getParameterValues("teams");
		//List<String> allTeams = (List<String>)request.get("allTeams");
		//request.setAttribute("allTeams", allTeams);

		System.out.println("**saveDocument.java**");		
		System.out.println(docNo);
		System.out.println(empNo);
		System.out.println(title);
		System.out.println(content);
		System.out.println(startTime);
		

		/*
		System.out.println("all teams: ");
		for (String x : allTeams) {
			System.out.println(x);
		}
		*/
		
		
		System.out.println("selected teams: ");
		if (teams != null)
			for (String x : teams) {
				System.out.println(x);
			}
		
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		
		try {
			Context init = new InitialContext();
			DataSource ds = (DataSource)init.lookup("java:comp/env/jdbc/infinity");
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(
				"SELECT doc_no from document WHERE doc_no=?"
			);
			pstmt.setString(1, docNo);
			rs = pstmt.executeQuery();
			
			if (rs.next()) { //update db
				pstmt2 = conn.prepareStatement(
					"UPDATE document SET title=?, content=?, save_time=now() WHERE doc_no=?"
				);
				pstmt2.setString(1, title);
				pstmt2.setString(2, content);
				pstmt2.setString(3, docNo);
				pstmt2.executeUpdate();
			} else {
				pstmt2 = conn.prepareStatement(
					"INSERT INTO document (doc_no, emp_no, title, content, start_time, save_time)" + 
					"VALUES (?, ?, ?, ?, ?, now())"
				);
				pstmt2.setString(1, docNo);
				pstmt2.setInt(2, Integer.parseInt(empNo));
				pstmt2.setString(3, title);
				pstmt2.setString(4, content);
				pstmt2.setTimestamp(5, Timestamp.valueOf(startTime));
				pstmt2.executeUpdate();
				
				
				
				
			} // insert into db
			
			// add approval line in approval table for sender
			pstmt3 = conn.prepareStatement(
				"SELECT * FROM approval WHERE doc_no=? and type=1"
			);
			pstmt3.setString(1, docNo);
			rs2 = pstmt3.executeQuery();
			
			if (!rs2.next()) { //if not exist, add
				// 1) add writer
				System.out.println("sender approval line: add routine");
				pstmt3 = conn.prepareStatement(
					"INSERT INTO approval (doc_no, type, team_code, approver, approval_order, approved)" + 
					"VALUES (?, ?, ?, ?, ?, ?)"
				);

				EmpDAO empDao = new EmpDAO();
				int teamCode = empDao.getTeamCodeFromEmpNo(Integer.parseInt(empNo));
				int approver = empDao.getTeamLeaderNoFromTeamCode(teamCode);

				empDao.dbClose();
				pstmt3.setString(1, docNo);
				pstmt3.setInt(2, 1);
				pstmt3.setInt(3, teamCode);
				pstmt3.setInt(4,  Integer.parseInt(empNo));
				pstmt3.setInt(5, 1);
				pstmt3.setInt(6, 0);
				pstmt3.executeUpdate();
				
				// 2) add team leader
				pstmt3 = conn.prepareStatement(
					"INSERT INTO approval (doc_no, type, team_code, approver, approval_order, approved)" + 
					"VALUES (?, ?, ?, ?, ?, ?)"
				);
				pstmt3.setString(1, docNo);
				pstmt3.setInt(2, 1);
				pstmt3.setInt(3, teamCode);
				pstmt3.setInt(4,  approver);
				pstmt3.setInt(5, 2);
				pstmt3.setInt(6, 0);
				pstmt3.executeUpdate();
				
				// 3) add ceo if selected
				if (approvalLine.equals("ceo")) {
					pstmt3 = conn.prepareStatement(
						"INSERT INTO approval (doc_no, type, team_code, approver, approval_order, approved)" + 
						"VALUES (?, ?, ?, ?, ?, ?)"
					);
					EmpDAO ceoDao = new EmpDAO();
					approver = ceoDao.getCeoVO().getEmpNo();
					pstmt3.setString(1, docNo);
					pstmt3.setInt(2, 1);
					pstmt3.setInt(3, teamCode);
					pstmt3.setInt(4,  approver);
					pstmt3.setInt(5, 3);
					pstmt3.setInt(6, 0);
					pstmt3.executeUpdate();
					ceoDao.dbClose();
				}
				
			} //end of not exist
			
			
			
			//update approval table
			//  1) delete existing data
			pstmt3 = conn.prepareStatement(
				"DELETE FROM approval WHERE doc_no=? and type=2"
			);
			pstmt3.setString(1, docNo);
			pstmt3.executeUpdate();
			
			//  2) put receivers again
			if (teams != null)
				for (String x : teams) {
					TeamDAO teamDao = new TeamDAO();
					EmpDAO empDao = new EmpDAO();
					int teamCode = (teamDao.getTeamVO(x)).getTeamCode();
					int approver = empDao.getTeamLeaderNoFromTeamCode(teamCode);
					teamDao.dbClose();
					empDao.dbClose();
					
					pstmt3 = conn.prepareStatement(
						"INSERT INTO approval (doc_no, type, team_code, approver, approval_order, approved)" + 
						"VALUES (?, ?, ?, ?, ?, ?)"
					);
					pstmt3.setString(1, docNo);
					pstmt3.setInt(2, 2);
					pstmt3.setInt(3, teamCode);
					pstmt3.setInt(4,  approver);
					pstmt3.setInt(5, 1);
					pstmt3.setInt(6, 0);
					pstmt3.executeUpdate();
					System.out.println("update receiver");
					System.out.println(docNo);
					System.out.println(teamCode);
					System.out.println(approver);
				}
			
			
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {if(pstmt != null) {pstmt.close();}} catch(Exception e) {e.printStackTrace();}
			try {if(pstmt2 != null) {pstmt2.close();}} catch(Exception e) {e.printStackTrace();}
			try {if(pstmt3 != null) {pstmt2.close();}} catch(Exception e) {e.printStackTrace();}
			try {if(rs != null) {rs.close();}} catch(Exception e) {e.printStackTrace();}
			try {if(rs2 != null) {rs.close();}} catch(Exception e) {e.printStackTrace();}
			try {if(conn != null) {conn.close();}} catch(Exception e) {e.printStackTrace();}
		}
			
		DocumentVO docVo = new DocumentVO();
		docVo.setDocNo(docNo);
		docVo.setEmpNo(Integer.parseInt(empNo));
		docVo.setTitle(title);
		docVo.setContent(content);
		docVo.setStartTime(Timestamp.valueOf(startTime));
		//docVo.setSaveTime(saveTime);
		
		EmpDAO empDao = new EmpDAO();
		EmpVO empVo = empDao.getEmpVO(Integer.parseInt(empNo));
		empDao.dbClose();
		
		request.setAttribute("docVo", docVo);
		request.setAttribute("teams", teams);
		request.setAttribute("approvalLine", approvalLine);
		request.setAttribute("empVo", empVo);
		
		RequestDispatcher rd = request.getRequestDispatcher("savedDocument.jsp");
		rd.forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
