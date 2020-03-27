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
@WebServlet(name = "FN03_0003", urlPatterns = { "/FN03_0003" })
public class FN03_0003 extends HttpServlet implements Servlet{
	private static final long serialVersionUID = 1L;
	private static UserInfo info = null; //FN03_0001でもらったユーザー情報
	private static final String defaultDisp = "/FN03_0003.jsp"; //元画面

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FN03_0003() {
        super();
    }	
	

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("txt_user_id");
		String name = request.getParameter("txt_user_name");
		String pass = request.getParameter("txt_password");
		String rePass = request.getParameter("txt_re_password");
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
		
		// ■ユーザ情報check
		System.out.println("id:" + id + ",name:" + name + ",pass:" + pass + ",rePass:" + rePass + ",auStr:" + auStr);
		if(id == null || name == null || pass == null || rePass == null || auStr == null ||
				id.equals("") || name.equals("") || pass.equals("") || auStr.equals("") ){
			
			//画面を戻す			
			Util.userDisp( request, response, "失敗。入力漏れがありませんか？", id, name, defaultDisp );
			return;
		} 

		// パスワードチェック
		if ( !pass.equals(rePass) ) {
			//画面を戻す
			Util.userDisp( request, response, "パスワードが一致しません", id, name, defaultDisp );
			return;			
		}
		
		int auth = Integer.parseInt(auStr);
		
		info.setUser_name(name);
		info.setPassword(pass);
		info.setAuthority(auth);

		// ■ユーザー情報正常
        DBAccess dbaccess = DBAccess.getInstance();
        try ( Connection con = dbaccess.getConnection() ){
        	//　ユーザー更新
        	updateUserInfo( con, info );
				
		} catch (Exception e ){
			e.printStackTrace();
		} 

		request.setAttribute("msg", "ユーザ情報変更成功");
		RequestDispatcher dispatch = request.getRequestDispatcher(defaultDisp);
		dispatch.forward(request, response);
	}

	/**
	 * ユーザ情報更新
	 * @param con
	 * @param id
	 * @param pw
	 * @return
	 */
	private void updateUserInfo(Connection con, UserInfo info){
		// sql作成
		StringBuilder sql = new StringBuilder();
		
        sql.append("update m_user set ")
        .append("user_name='" + info.getUser_name() + "' , ")
        .append("password='" + info.getPassword() + "' ,")
        .append("authority=" + info.getAuthority() + " ")
        .append("where user_id='" + info.getUser_id() + "'")
        ;

        System.out.println( "sql:" + sql.toString() );
 
        try ( Statement stmt = con.createStatement() ){
        	if (stmt.executeUpdate(sql.toString()) != 1 ) {
        		System.err.println( "DB Upadate Err" );
        		
        	} else {
        		System.out.println("DB Update Succeeded!");
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
