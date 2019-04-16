package net.infinity.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.infinity.db.RejectedDocumentDAO;
import net.infinity.db.RejectedDocumentVO;

public class ActionDocumentReturnedReceivedToCEO implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		HttpSession mySession = request.getSession();
		String id = (String)mySession.getAttribute("id");
		
		RejectedDocumentDAO rejDocDao = new RejectedDocumentDAO();
		List<RejectedDocumentVO> list = rejDocDao.getDocumentReturnedReceivedToCEO();
		rejDocDao.dbClose();
		
		request.setAttribute("docList", list);
		
		ActionForward af = new ActionForward();
		af.setPath("documentReturnedReceivedToCEO.jsp");
		af.setRedirect(false);
		
		return af;
	}

}
