package net.infinity.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.infinity.db.ApprovalDAO;

public class ActionReturnDocumentByTeamLeaderConfirmed implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		HttpSession mySession = request.getSession();

		String id = (String)mySession.getAttribute("id");
		
		request.setCharacterEncoding("UTF-8");
		String docNo = request.getParameter("doc_no");
		System.out.println("ActionReturnDocumentByTeamLeaderConfirmed doc no: " + docNo);
		
		ApprovalDAO appDao = new ApprovalDAO();
		appDao.returnDocumentByTeamLeader(docNo, Integer.parseInt(id));
		appDao.dbClose();
		
		
		ActionForward af = new ActionForward();
		af.setPath("documentReturnedPending.do");
		af.setRedirect(true);
		
		return af;
	}
}
