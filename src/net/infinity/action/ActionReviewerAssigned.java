package net.infinity.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.infinity.db.ApprovalDAO;

public class ActionReviewerAssigned implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession mySession = request.getSession();

		String id = (String)mySession.getAttribute("id");
		
		String docNo = (String)request.getParameter("docNo");
		
		int empNo = Integer.parseInt(request.getParameter("reviewer"));
		
		//1. set teamLeader approved to 1
		ApprovalDAO appDao = new ApprovalDAO();
		if (appDao.setApproval(docNo, Integer.parseInt(id), 1) == 1) System.out.println("ActionReviewerAssigned team leader approved");
		else System.out.println("ActionReviewerAssigned team leader not approved");
		//2. add reviewer into approval table
		
		
		if (appDao.addReviewer(docNo, empNo) == 1) System.out.println("ActionReviewerAssigned reviewer added");
		else System.out.println("ActionReviewerAssigned reviewer not added");
		appDao.dbClose();
		ActionForward af = new ActionForward();
		af.setPath("documentPendingReceving.do");
		af.setRedirect(false);
		
		return af;
	}

}
