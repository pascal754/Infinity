package net.infinity.action;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class Controller
 */
@WebServlet("*.do")
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Controller() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    
    protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//System.out.println("annotation test");
    	String RequestURI = request.getRequestURI();
    	StringBuffer RequestURL = request.getRequestURL();
    	
    	String ContextPath = request.getContextPath();
    	System.out.println("URI: " + RequestURI);
    	System.out.println("URL: " + RequestURL);
    	System.out.println("ContextPath: " + ContextPath);
		
    	String command = RequestURI.substring(ContextPath.length()+1);
    	System.out.println(command);
    	
    	ActionForward af = null;
    	Action action = null;
    	    	
    	if (command.equals("loginProcess.do")) {
    		af = new ActionForward();
    		af.setRedirect(false);
    		af.setPath("/loginProcess.jsp");
    	} else if (command.equals("main.do")) {
    		af = new ActionForward();
    		af.setRedirect(false);
    		af.setPath("/main.jsp");
    	} else if (command.equals("pendingSendingToTeamLeader.do")) {
    		action = new ActionDocumentPendingSendingToTeamLeader();
    		af = action.execute(request, response);
     	} else if (command.equals("ReportToCEO.do")) {
    		action = new ActionReportToCEO();
    		af = action.execute(request, response);
     	} else if (command.equals("documentBeingSentByTeam.do")) {
    		action = new ActionDocumentBeingSentByTeam();
    		af = action.execute(request, response);
     	} else if (command.equals("CompleteReportByCEO.do")) {
    		action = new ActionCompleReportByCEO();
    		af = action.execute(request, response);
     	} else if (command.equals("documentCompleteByCEO.do")) {
    		action = new ActionCompleteByCEO();
    		af = action.execute(request, response);
     	} else if (command.equals("documentCompleteByTeamMember.do")) {
    		action = new ActionDocumentCompleteByTeamMember();
    		af = action.execute(request, response);
     	} else if (command.equals("documentCompleteByTeamLeader.do")) {
    		action = new ActionDocumentCompleteByTeamLeader();
    		af = action.execute(request, response);
     	} else if (command.equals("saveDocument.do")) {
    		action = new ActionSaveDocument();
    		af = action.execute(request, response);
     	} else if (command.equals("documentPendingReceiving.do")) {
    		action = new ActionDocumentPendingReceiving();
    		af = action.execute(request, response);
     	} else if (command.equals("assignReviewer.do")) {
    		action = new ActionAssignReviewer();
    		af = action.execute(request, response);
     	} else if (command.equals("reviewerAssigned.do")) {
    		action = new ActionReviewerAssigned();
    		af = action.execute(request, response);
     	} else if (command.equals("documentPendingReceivingToTeamMember.do")) {
    		action = new ActionDocumentPendingReceivingToTeamMember();
    		af = action.execute(request, response);
     	} else if (command.equals("CompleteReceiving.do")) {
    		action = new ActionCompleteReceiving();
    		af = action.execute(request, response);
     	} else if (command.equals("documentCompleteReceivingByTeamMember.do")) {
    		action = new ActionDocumentCompleteReceivingByTeamMember();
    		af = action.execute(request, response);
     	} else if (command.equals("documentBeingReceived.do")) {
    		action = new ActionDocumentBeingReceived();
    		af = action.execute(request, response);
     	} else if (command.equals("documentCompleteReceiving.do")) {
    		action = new ActionDocumentCompleteReceiving();
    		af = action.execute(request, response);
     	} else if (command.equals("documentPendingSendingToCEOByTeam.do")) {
    		action = new ActionDocumentPendingSendingToCEOByTeam();
    		af = action.execute(request, response);
     	} else if (command.equals("documentPendingSendingToCEO.do")) {
    		action = new ActionDocumentPendingSendingToCEO();
    		af = action.execute(request, response);
     	}
    	
//    	} else if ( command.equals("/BoardAddAction.bo")) {
//    		action = new ;
//    		af = action.execute(request, response);
//    	} else if ( command.equals("/redirect.bo")) {
//    		// redirect 방식
//
//    		//response.sendRedirect("redirect.jsp");
//    		
//        	//dispatcher 방식
//        	/*
//        	RequestDispatcher dispatcher = request.getRequestDispatcher(arg0);
//        	request.setAttribute("id",  "abc");
//        	dispatcher.forward(request,  response);
//        	*/
//    	} else if ( command.equals(".do")) {
//    		action = new ();
//    		//af = bAction.execute(request, response);
//    		af = action.execute(request, response);
//    	} else if (command.equals(".do")) {
//    		//BoardDeleteAction bAction = new BoardDeleteAction();
//    		action = new ();
//    		//af = bAction.execute(request, response);
//    		af = action.execute(request, response);
//    		
//    		
//    	} else if (command.equals(".do")) {
//    		af = new ActionForward();
//    		af.setRedirect(false);
//    		System.out.println("before board delete jsp");
//    		af.setPath("/BoardDelete.jsp");
//    	} else if (command.equals(".do")) {
//    		af = new ActionForward();
//    		af.setRedirect(false);
//    		System.out.println("before board reply jsp");
//    		af.setPath("/BoardReply.jsp");
//    	} else if (command.equals("/BoardReplyAction.bo")) {
//    		action = new BoardReplyAction();
//    		af = action.execute(request, response);
//    	} else if (command.equals("/BoardListSearch.bo")) {
//    		action = new BoardSearchAction();
//    		af = action.execute(request, response);
//    	} else if (command.equals("/BoardModify1.bo")) {
//    		af = new ActionForward();
//    		af.setRedirect(false);
//    		//System.out.println("before board modify jsp");
//    		af.setPath("/BoardModify1.jsp");
//    	} else if (command.equals("/BoardModifyAction2.bo")) {
//    		action = new BoardModifyAction2();
//    		af = action.execute(request, response);
//    	} else if (command.equals("/BoardModifyAction3.bo")) {
//    		action = new BoardModifyAction3();
//    		af = action.execute(request, response);
//    	}
    	
    	if (af != null) {
    		if (af.isRedirect()) {
    			response.sendRedirect(af.getPath());
    		} else {
    			RequestDispatcher dispatcher = request.getRequestDispatcher(af.getPath());
    			dispatcher.forward(request, response);
    		}
    	}
	}

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

}
