package net.infinity.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.infinity.db.ApprovalDAO;

public class ActionReportToCEO implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String docNo = (String)request.getParameter("doc_no");
		ApprovalDAO appDao = new ApprovalDAO();
		appDao.reportToCEO(docNo);
		appDao.dbClose();
		System.out.println("ActionReportToCEO docNo: " + docNo);

		
		ActionForward af = new ActionForward();
		af.setPath("pendingSending.do");
		af.setRedirect(false);
		
		
		
		
		return af;
	}

}
