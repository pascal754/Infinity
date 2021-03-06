package net.infinity.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.infinity.db.ApprovalDAO;

public class ActionReturnDocument implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		HttpSession mySession = request.getSession();

		String id = (String)mySession.getAttribute("id");
		
		String docNo = request.getParameter("docNo");
		System.out.println("ActionReturnDocument doc no: " + docNo);
		request.setAttribute("docNo", docNo);
		ActionForward af = new ActionForward();
		af.setPath("returnDocumentByTeamMember.jsp");
		af.setRedirect(false);
		
		return af;
	}
}
