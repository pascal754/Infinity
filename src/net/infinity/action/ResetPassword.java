package net.infinity.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.infinity.db.EmpDAO;

public class ResetPassword implements Action{

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	//	HttpSession mySession = request.getSession();
	//	String id = (String)mySession.getAttribute("id");
		
		ActionForward af = new ActionForward();
		af.setPath("adminPage.jsp");
		af.setRedirect(false);
				
		
	
		String emp_no = request.getParameter("empNo");
		if (emp_no == null)
			return af;
		
		int empNo = Integer.parseInt(emp_no);
		String newpass = request.getParameter("newpass");
		
		System.out.println("empNo: " + empNo);
		System.out.println("newpass: " + newpass);

		EmpDAO empDao = new EmpDAO();
		empDao.resetPassword(empNo,newpass);
		empDao.dbClose();
		
		
		return af;
	}

}

