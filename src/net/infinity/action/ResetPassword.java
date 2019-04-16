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
		
		EmpDAO empDao = new EmpDAO();
	
		int empNo = Integer.parseInt(request.getParameter("empNo"));
		String newpass = request.getParameter("newpass");
		
		empDao.resetPassword(empNo,newpass);
		empDao.dbClose();
		
		ActionForward af = new ActionForward();
		af.setPath("adminPage.jsp");
		af.setRedirect(false);
		
		return af;
	}

}

