package net.infinity.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.infinity.db.ApprovalDAO;

public class ActionRejectDocumentByCEOConfirmed implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		String docNo = request.getParameter("docNo");
		String id = request.getParameter("id");
		String comment = request.getParameter("comment");
		System.out.println("ActionRejectDocumentByCEOConfirmed doc no: " + docNo);
		System.out.println("ActionRejectDocumentByCEOConfirmed id: " + id);
		System.out.println("ActionRejectDocumentByCEOConfirmed comment: " + comment);
		
		ApprovalDAO appDao = new ApprovalDAO();
		appDao.rejectDocumentByCEO(docNo, Integer.parseInt(id), comment);
		appDao.dbClose();
		
		ActionForward af = new ActionForward();
		af.setPath("documentPendingSendingToCEO.do");
		af.setRedirect(true);
		
		return af;
	}

}
