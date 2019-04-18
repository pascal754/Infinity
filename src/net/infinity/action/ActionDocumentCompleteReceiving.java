package net.infinity.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.infinity.db.DocumentDAO;
import net.infinity.db.DocumentVO;

public class ActionDocumentCompleteReceiving implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession mySession = request.getSession();

		String id = (String)mySession.getAttribute("id");
		
		DocumentDAO docDao = new DocumentDAO();
		List<DocumentVO> list = docDao.getDocumentCompleteReceiving(Integer.parseInt(id));
		docDao.dbClose();
		
		
		request.setAttribute("docList", list);
		
		ActionForward af = new ActionForward();
		af.setPath("documentBeingReceived.jsp");
		af.setRedirect(false);
		
		return af;
	}

}
