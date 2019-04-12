package net.infinity.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.infinity.db.ApprovalDAO;
import net.infinity.db.DocumentDAO;

public class ActionCompleReportByCEO implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession mySession = request.getSession();

		String id = (String)mySession.getAttribute("id");
		String docNo = (String)request.getParameter("doc_no");
		ApprovalDAO appDao = new ApprovalDAO();
		appDao.completeReportByCEO(docNo, Integer.parseInt(id));
		appDao.dbClose();
		System.out.println("ActionCompleReportByCEO docNo: " + docNo);

		
		ActionForward af = new ActionForward();
		af.setPath("documentCompleteInCEO.do");
		af.setRedirect(false);
		
		
		
		return af;
	}

}
