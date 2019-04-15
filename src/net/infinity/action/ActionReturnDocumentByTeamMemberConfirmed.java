package net.infinity.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.infinity.db.ApprovalDAO;

public class ActionReturnDocumentByTeamMemberConfirmed implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		HttpSession mySession = request.getSession();

		String id = (String)mySession.getAttribute("id");
		
		String docNo = request.getParameter("docNo");
		String comment = request.getParameter("comment");
		System.out.println("ActionReturnDocumentByTeamMemberConfirmed doc no: " + docNo);
		
		ApprovalDAO appDao = new ApprovalDAO();
		appDao.returnDocument(docNo, Integer.parseInt(id), comment);
		appDao.dbClose();
		
		request.setAttribute("docNo", docNo);
		
		ActionForward af = new ActionForward();
		af.setPath("documentPendingReceivingToTeamMember.do");
		af.setRedirect(false);
		
		return af;
	}
}
