package net.infinity.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.infinity.db.ApprovalDAO;

public class ActionRejectDocumentByTeamLeaderConfirmed implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		String docNo = request.getParameter("docNo");
		String id = request.getParameter("id");
		String comment = request.getParameter("comment");
		System.out.println("ActionRejectDocumentByTeamLeaderConfirmed doc no: " + docNo);
		System.out.println("ActionRejectDocumentByTeamLeaderConfirmed id: " + id);
		System.out.println("ActionRejectDocumentByTeamLeaderConfirmed comment: " + comment);
		
		ApprovalDAO appDao = new ApprovalDAO();
		appDao.rejectDocumentByTeamLeader(docNo, Integer.parseInt(id), comment);
		appDao.dbClose();
		
		ActionForward af = new ActionForward();
		af.setPath("documentPendingSendingToTeamLeader.do");
		af.setRedirect(true);
		
		return af;
	}

}
