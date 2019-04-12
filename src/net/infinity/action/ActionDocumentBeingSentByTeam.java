package net.infinity.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.infinity.db.DocumentDAO;
import net.infinity.db.DocumentVO;
import net.infinity.db.EmpDAO;

public class ActionDocumentBeingSentByTeam implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession mySession = request.getSession();

		String id = (String)mySession.getAttribute("id");
		EmpDAO empDao = new EmpDAO();
		int teamCode = empDao.getTeamCodeFromEmpNo(Integer.parseInt(id));
		empDao.dbClose();
		
		DocumentDAO docDao = new DocumentDAO();
		List<DocumentVO> list = docDao.getDocumentBeingSentByTeam(teamCode);
		docDao.dbClose();
		
		request.setAttribute("docList", list);
		
		ActionForward af = new ActionForward();
		af.setPath("documentBeingSentByTeam.jsp");
		af.setRedirect(false);
	
		System.out.println("ActionDocumentBeingSentByTeam executed");
		return af;
	}
}
