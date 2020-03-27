package pack;

import javax.servlet.annotation.WebServlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;


/**
 * Servlet implementation class FN02_0001
 */
@WebServlet(name = "FN02_0001", urlPatterns = { "/FN02_0001" })
public class FN02_0001 extends HttpServlet implements Servlet{
	private static final long serialVersionUID = 1L;
	
	private String screen_id="";
	private int authority=0;
	
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FN02_0001() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("screen_id");
		String auth = request.getParameter("authority");
		
		if( id != null ){
			screen_id = id;
		}
		
		if ( auth != null ) {
			authority = Integer.parseInt(auth);
		}
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		
		String disp = "/FN02_0001.jsp";				
		
		switch( screen_id ) {
		case "user_disp":
			disp = "/FN03_0001.jsp";
			break;
		case "document_disp":
			disp = "/FN04_0001.jsp";
			break;			
		default:
			System.err.println("screen_id:" + screen_id + "is inValid");
			break;
		}
			
			
		RequestDispatcher dispatch = request.getRequestDispatcher(disp);
		dispatch.forward(request, response);

	}
	
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
