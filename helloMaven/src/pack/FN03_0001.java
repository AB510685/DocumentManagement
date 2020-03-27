package pack;

import javax.servlet.annotation.WebServlet;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.*;
import javax.servlet.http.*;

import pack.bean.UserInfo;
import pack.com.DBAccess;
import pack.com.Util;
/**
 * Servlet implementation class FN03_0001
 */
@WebServlet(name = "FN03_0001", urlPatterns = { "/FN03_0001" })
public class FN03_0001 extends HttpServlet implements Servlet{
	private static final long serialVersionUID = 1L;
	
	private String txt_search_user_id="";
	private String txt_search_user_name="";
	
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FN03_0001() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	// FN02_0001から遷移してきたときの処理
    	String btn = request.getParameter("btn_menu");
    	if ( btn != null ) { 
    		Util.menuDisp(request, response, "", "", "", "/FN03_0001.jsp");
    		return;
    	}
    	
    	String id = request.getParameter("txt_search_user_id");
		String name = request.getParameter("txt_search_user_name");
		
		if( id != null ){
			txt_search_user_id = id;
		}
		
		if ( name != null ) {
			txt_search_user_name = name;
		}
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");

		//■ユーザID・ユーザ名 check
		if(txt_search_user_id.equals("") && txt_search_user_name.equals("")){
			//画面を戻す
			Util.menuDisp(request, response, "ユーザIDまたはユーザIDを入力してください", id, name, "/FN03_0001.jsp");
			return;
		}
		
		//■入力項目あり
		
	    DBAccess dbaccess = DBAccess.getInstance();
	    try (Connection con = dbaccess.getConnection() ){	
	    	// ユーザ情報取得
			List<UserInfo> userinfo = getUserInfo(con, txt_search_user_id, txt_search_user_name);
			request.setAttribute("txt_search_user_id", txt_search_user_id);
			request.setAttribute("txt_search_user_name", txt_search_user_name);
				
			if(userinfo.size() == 0){
		       	//元の画面に遷移
				//画面を戻す
				Util.menuDisp(request, response, "ユーザIDまたはユーザ名が違います", id, name, "/FN03_0001.jsp");
				return;
			} 
		    
			request.setAttribute("userinfo",   userinfo);
			System.out.println("UserInfo Search OK");
			Util.menuDisp(request, response, "", id, name, "/FN03_0001.jsp");
				
	    }catch(Exception e){
	       	e.printStackTrace();
	    }
	

	}

	/**
	 * ユーザ情報取得
	 * FN01_0001の同名メソッドと違い、絞り込んで一件取得
	 * @param con
	 * @param id
	 * @param pw
	 * @return
	 */
	private List<UserInfo> getUserInfo(Connection con, String id, String name){
        List<UserInfo> ret = new ArrayList<>();
        if( id.equals("") && name.equals("") ) {
        	return ret;
        }
		// sql作成
		StringBuilder sql = new StringBuilder();
		
        sql.append("select user_id, user_name, authority from m_user where ");
        if ( !id.equals("") ) {
        	sql.append( "user_id like '%")
        	.append( id )
        	.append( "%'")
        	;
        }
        
        if( !id.equals("") && !name.equals("") ) {
        	sql.append(" or ");
        }
        
        if( !name.equals("") ) {
        	sql.append("user_name like '%")
        	.append( name )
        	.append( "%'" )
        	;
        }
        System.out.println("sql:" + sql.toString());

        try( PreparedStatement pstmt  = con.prepareStatement(sql.toString())){
            //pstmt.setString(1, id);
            //pstmt.setString(2, name);
            ResultSet result = pstmt.executeQuery();
	        while ( result.next() ){
	            UserInfo record = new UserInfo();
	            record.setUser_id(result.getString("user_id"));
	            record.setUser_name(result.getString("user_name"));
	            record.setAuthority(result.getInt("authority"));
	            ret.add(record);
	        }

        }catch(Exception e){
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
