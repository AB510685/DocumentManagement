package pack;

import javax.servlet.annotation.WebServlet;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Statement; //Preparedとどう違うのか？
//import java.sql.ResultSet;
//import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;
import pack.bean.UserInfo;
import pack.com.DBAccess;
import pack.com.Util;

/**
 * Servlet implementation class FN03_0002
 */
@WebServlet(name = "FN03_0002", urlPatterns = { "/FN03_0002" })
public class FN03_0002 extends HttpServlet implements Servlet{
	private static final long serialVersionUID = 1L;
	private static final String defaultDisp = "/FN03_0002.jsp"; //元画面
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FN03_0002() {
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
			Util.userDisp( request, response, "", "", "", defaultDisp);
			return;
		}

	
		// 戻るボタン処理
		String back = request.getParameter("btn_back");
		if( back != null ) {
			Util.userDisp( request, response, "", "", "", "/FN03_0001.jsp");
			return;
		}
		
		// ■ユーザ情報check
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
		UserInfo info = new UserInfo( id, name, pass, auth );
		
		// ■ユーザー情報正常
        DBAccess dbaccess = DBAccess.getInstance();
        try ( Connection con = dbaccess.getConnection() ){
    		// ID重複チェック
    		if ( checkID( con, info ) ) {
    			//画面を戻す
    			Util.userDisp( request, response, "既存のIDと重複します:'" + id + "'", id, name, defaultDisp );
    			return;			
    		}	
        	
        	//　ユーザー追加
        	addUserInfo( con, info );
				
		} catch (Exception e ){
			e.printStackTrace();
		} 

		request.setAttribute("msg", "ユーザ情報追加成功");
		RequestDispatcher dispatch = request.getRequestDispatcher(defaultDisp);
		dispatch.forward(request, response);
	}


	/**
	 * ユーザ情報追加
	 * @param con
	 * @param id
	 * @param pw
	 * @return
	 */
	private void addUserInfo(Connection con, UserInfo info){
		// sql作成
		StringBuilder sql = new StringBuilder();
		
        sql.append("insert into m_user ")
        .append("(user_id, user_name, password, authority) ")
        .append("values (")
        .append("'" + info.getUser_id() + "' , ")
        .append("'" + info.getUser_name() + "' , ")
        .append("'" + info.getPassword() + "' , ")
        .append( info.getAuthority() )
        .append(" )")
        ;

        System.out.println( "sql:" + sql.toString() );
 
        try ( Statement stmt = con.createStatement() ){
        	if (stmt.executeUpdate(sql.toString()) != 1 ) {
        		System.err.println( "DB Insert Err" );
        		
        	} else {
        		System.out.println("DB Insert Succeeded!");
        	}
        }catch(Exception e){
        	e.printStackTrace();
    	}
        
        return;

	}

	// 入力IDと一致するIDが既にm_userにあればtrueを返す
	private boolean checkID( Connection con, UserInfo info ) {
		boolean ret = false;
		StringBuilder sql = new StringBuilder();
		
        sql.append("select user_id from m_user ")
        .append("where user_id='" + info.getUser_id() + "'")
        ;
        
        System.out.println("checkID sql:" + sql.toString());
        
        try ( PreparedStatement pstmt  = con.prepareStatement( sql.toString() ) ) {
            ResultSet result = pstmt.executeQuery();
	        ret = result.next();

        } catch (Exception e) {
        	e.printStackTrace();
        }
        
		return ret;
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
