package net.infinity.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ActionRejectDocumentByTeamLeader implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String docNo = (String)request.getParameter("docNo");
		System.out.println("ActionRejectDocumentByTeamLeader doc no: " + docNo);
		
		request.setAttribute("docNo", docNo);
		ActionForward af = new ActionForward();
		af.setPath("rejectDocumentByTeamLeader.jsp");
		af.setRedirect(false);
		
		return af;
	}
}
