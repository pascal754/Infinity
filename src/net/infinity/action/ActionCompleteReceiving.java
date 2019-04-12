package net.infinity.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.infinity.db.ApprovalDAO;

public class ActionCompleteReceiving implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		HttpSession mySession = request.getSession();

		String id = (String)mySession.getAttribute("id");
		String docNo = (String)request.getParameter("doc_no");
		
		ApprovalDAO appDao = new ApprovalDAO();
		if (appDao.completeReceiving(docNo, Integer.parseInt(id)) == 1) System.out.println("ActionCompleteReceiving successful");
		else System.out.println("ActionCompleteReceiving failed");
		appDao.dbClose();
		
		ActionForward af = new ActionForward();
		af.setPath("documentPendingReceivingToTeamMember.do");
		af.setRedirect(false);

		return af;
	}

}
