package net.infinity.db;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;


public class EmpDAO {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	public EmpDAO() {
			
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
	
	
	
	public EmpVO getEmpVO(int empNo) {
		EmpVO empVO = new EmpVO();
		try {
			pstmt = conn.prepareStatement("SELECT * FROM emp WHERE emp_no = ?");
			pstmt.setInt(1,  empNo);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				empVO.setEmpNo(empNo);
				empVO.setName(rs.getString("name"));
				empVO.setTeamCode(rs.getInt("team_code"));
				empVO.setTitleCode(rs.getInt("title_code"));
				empVO.setPassword(rs.getString("password"));
				empVO.setTel(rs.getInt("tel"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {if (rs != null) {rs.close();}} catch (Exception e) {e.printStackTrace();}
			try {if (pstmt != null) {pstmt.close();}} catch (Exception e) {e.printStackTrace();}
		}
		
		System.out.println("EmpDAO::getEmpVO()");
		System.out.println(empVO.getName());
		return empVO;
	}
	
	//get team leader's emp_no whose team code is same as that of empNo
	public int getTeamLeaderNoFromEmpNo(int empNo) {
		int teamLeaderNo = 0;
		try {
			pstmt = conn.prepareStatement("select emp_no from emp where title_code=2 and team_code = (select team_code from emp where emp_no = ?)");
			
			pstmt.setInt(1, empNo);
			
			rs = pstmt.executeQuery();
			if (rs.next()) {
				teamLeaderNo = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
		System.out.println("EmpDAO::getTeamLeaderNoFromEmpNo()");
		System.out.println(teamLeaderNo);
		return teamLeaderNo;
	}
	
	public int getTeamLeaderNoFromTeamCode(int teamCode) {
		int teamLeaderNo = 0;
		try {
			pstmt = conn.prepareStatement("SELECT emp_no FROM emp WHERE team_code = ? and title_code=2");
			
			pstmt.setInt(1,  teamCode);
			
			rs = pstmt.executeQuery();
			if (rs.next()) {
				teamLeaderNo = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
		
		
		return teamLeaderNo;
	}
	
	public int getTeamCodeFromEmpNo(int empNo) {
		int teamCode = 0;
		try {
			pstmt = conn.prepareStatement("SELECT team_code FROM emp WHERE emp_no = ?");
			
			pstmt.setInt(1, empNo);
			
			rs = pstmt.executeQuery();
			if (rs.next()) {
				teamCode = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
		
		return teamCode;
	}
	
	public EmpVO getCeoVO() {
		EmpVO empVO = new EmpVO();
		try {
			pstmt = conn.prepareStatement("SELECT * FROM emp WHERE title_code = 3");
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				empVO.setEmpNo(rs.getInt("emp_no"));
				empVO.setName(rs.getString("name"));
				empVO.setTeamCode(rs.getInt("team_code"));
				empVO.setTitleCode(rs.getInt("title_code"));
				empVO.setPassword(rs.getString("password"));
				empVO.setTel(rs.getInt("tel"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
		
		return empVO;
	}
	
	
	public String getTeamName(int empNo) {
		String teamName = "";
		try {
			pstmt = conn.prepareStatement("select team_name from team where team_code = (select team_code from emp where emp_no = ?)");
			
			pstmt.setInt(1, empNo);
			
			rs = pstmt.executeQuery();
			if (rs.next()) {
				teamName = rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
		
		return teamName;
	}
	
	public List<String> getAllTeams() {
		List<String> teams = new ArrayList<String>();
		
		try {
			pstmt = conn.prepareStatement("SELECT team_name FROM team WHERE team_code <> 90000 order by team_name");
			rs = pstmt.executeQuery();
			while (rs.next())
				teams.add(rs.getString(1));
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
		
		
		return teams;
	}
	
	public List<EmpVO> getTeamMembers(int empNo) {
		List<EmpVO> list = new ArrayList<EmpVO>();
		try {
			pstmt = conn.prepareStatement("select * from emp where team_code = (select team_code from emp where emp_no = ?) and title_code=1");
			pstmt.setInt(1, empNo);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				EmpVO empVo = new EmpVO();
				empVo.setEmpNo(rs.getInt("emp_no"));
				empVo.setName(rs.getString("name"));
				empVo.setTitleCode(rs.getInt("title_code"));
				empVo.setPassword(rs.getString("password"));
				empVo.setTel(rs.getInt("tel"));
				list.add(empVo);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
		return list;
	}
	
	public String getEmpName(int empNo) {
		String name = "";
		try {
			pstmt = conn.prepareStatement("select name from emp where emp_no = ?");
			pstmt.setInt(1, empNo);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				name = rs.getString(1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
		return name;
	}


	public List<EmpVO> getAllMembers() {
		List<EmpVO> list = new ArrayList<EmpVO>();
		try {
			pstmt = conn.prepareStatement("select * from emp");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				EmpVO empVo = new EmpVO();
				empVo.setEmpNo(rs.getInt("emp_no"));
				empVo.setName(rs.getString("name"));
				empVo.setTitleCode(rs.getInt("title_code"));
				empVo.setPassword(rs.getString("password"));
				empVo.setTel(rs.getInt("tel"));
				list.add(empVo);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
		return list;
	}


	public int resetPassword(int empNo, String newpass) {
		int result=0;
		
		SecurityUtil securityUtil = new SecurityUtil();
		String ppwd = securityUtil.encryptSHA256(newpass);
		
		try {
			pstmt = conn.prepareStatement("UPDATE emp SET password=? where emp_no=?");
			
			pstmt.setString(1, ppwd);
			pstmt.setInt(2, empNo);
			result = pstmt.executeUpdate();
			System.out.println("reset");
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
		
		return result;
	}

	public String getTitle(int empNo) {
		String title = "";
		try {
			pstmt = conn.prepareStatement(
					"select title_name from title where title_code=(SELECT title_code FROM emp WHERE emp_no = ?)"
					);
			pstmt.setInt(1, empNo);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				title = rs.getString(1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close();} catch (Exception e) {e.printStackTrace();}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {e.printStackTrace();}
		}
		return title;
	}
}
