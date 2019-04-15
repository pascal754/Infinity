package net.infinity.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.infinity.db.RejectedDocumentDAO;
import net.infinity.db.RejectedDocumentVO;

public class ActionDocumentReturnedConfirmed implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		HttpSession mySession = request.getSession();
		String id = (String)mySession.getAttribute("id");
		
		RejectedDocumentDAO rejDocDao = new RejectedDocumentDAO();
		List<RejectedDocumentVO> list = rejDocDao.getDocumentReturnedConfirmed(Integer.parseInt(id));
		rejDocDao.dbClose();
		
		request.setAttribute("docList", list);
		
		ActionForward af = new ActionForward();
		af.setPath("documentReturnedConfirmed.jsp");
		af.setRedirect(false);
		
		return af;
	}

}
