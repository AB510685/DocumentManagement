package pack;

import javax.servlet.annotation.WebServlet;
import java.io.*;
import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.PreparedStatement;
import java.sql.Statement; //Preparedとどう違うのか？
//import java.sql.ResultSet;
//import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;
import pack.bean.UserInfo;
import pack.com.DBAccess;
import pack.com.Util;

/**
 * Servlet implementation class FN03_0003
 */
@WebServlet(name = "FN03_0004", urlPatterns = { "/FN03_0004" })
public class FN03_0004 extends HttpServlet implements Servlet{
	private static final long serialVersionUID = 1L;
	
	private static UserInfo info = null; //FN03_0001でもらったユーザー情報
	private static final String defaultDisp = "/FN03_0004.jsp"; //元画面

    public FN03_0004() {
        super();
    }

    /**
     * @see HttpServlet#HttpServlet()
     */


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("txt_user_id");
		String name = request.getParameter("txt_user_name");
		String auStr = request.getParameter("authority"); 
		
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");

		// FN03_0001.jspから遷移時の処理
		String select = request.getParameter("select");
		if ( select != null ) {
			String msg = "";
			info = Util.getUserInfo( request, select );
			if ( info == null ) {
				msg = "セッションからユーザー情報を取得できませんでした";
				System.err.println(msg);

			}
			Util.userDisp( request, response, msg, "", "", defaultDisp);
			return;
		}


		// 戻るボタン処理
		String back = request.getParameter("btn_back");
		if( back != null ) {
			Util.userDisp( request, response, "", "", "", "/FN03_0001.jsp");
			return;
		}
		
		int auth = Integer.parseInt(auStr);
	
		info.setUser_id(id);
		info.setUser_name(name);
		info.setAuthority(auth);

		// ■ユーザー情報正常
        DBAccess dbaccess = DBAccess.getInstance();
        try ( Connection con = dbaccess.getConnection() ){
        	//　ユーザー削除
        	deleteUserInfo( con, info );
				
		} catch (Exception e ){
			e.printStackTrace();
		} 

		request.setAttribute("msg", "ユーザ情報削除成功");
		RequestDispatcher dispatch = request.getRequestDispatcher(defaultDisp);
		dispatch.forward(request, response);
	}

	/**
	 * ユーザ情報削除
	 * @param con
	 * @param id
	 * @param pw
	 * @return
	 */
	private void deleteUserInfo(Connection con, UserInfo info){
		// sql作成
		StringBuilder sql = new StringBuilder();
		
        sql.append("delete from m_user ")
        .append("where user_id='" + info.getUser_id() + "'")
        ;

        System.out.println( "sql:" + sql.toString() );
 
        try ( Statement stmt = con.createStatement() ){
        	if (stmt.executeUpdate(sql.toString()) != 1 ) {
        		System.err.println( "DB Delete Err" );
        		
        	} else {
        		System.out.println("DB Delete Succeeded!");
        	}
        }catch(Exception e){
        	e.printStackTrace();
    	}
        
        return;

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
