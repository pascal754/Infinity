package net.infinity.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.infinity.db.EmpDAO;
import net.infinity.db.EmpVO;

public class ActionAssignReviewer implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		HttpSession mySession = request.getSession();

		String id = (String)mySession.getAttribute("id");
		String docNo = (String)request.getParameter("doc_no");
		EmpDAO empDao = new EmpDAO();
		List<EmpVO> list = empDao.getTeamMembers(Integer.parseInt(id));
		empDao.dbClose();
		
		request.setAttribute("teamMembers", list);
		request.setAttribute("docNo", docNo);
		ActionForward af = new ActionForward();
		af.setPath("selectReviewer.jsp");
		af.setRedirect(false);
		
		return af;
		
	}

}
