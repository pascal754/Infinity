package net.infinity.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.infinity.db.ApprovalDAO;
import net.infinity.db.DocumentVO;
import net.infinity.db.EmpDAO;

public class ActionDocumentCompleteByTeamLeader implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession mySession = request.getSession();

		String id = (String)mySession.getAttribute("id");
		EmpDAO empDao = new EmpDAO();
		int teamLeaderNo = empDao.getTeamLeaderNoFromEmpNo(Integer.parseInt(id));
		empDao.dbClose();
		ApprovalDAO appDao = new ApprovalDAO();
		List<DocumentVO> list = appDao.getCompleteByTeamLeader(teamLeaderNo);
		appDao.dbClose();
		
		request.setAttribute("docList", list);
		
		ActionForward af = new ActionForward();
		af.setPath("documentCompleteByTeamLeader.jsp");
		af.setRedirect(false);
		
		return af;
	}

}
