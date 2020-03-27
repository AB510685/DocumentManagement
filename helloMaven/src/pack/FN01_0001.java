package pack;

import javax.servlet.annotation.WebServlet;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.*;
import javax.servlet.http.*;

import pack.bean.MenuInfo;
import pack.bean.UserInfo;
import pack.com.DBAccess;
import pack.com.Util;
/**
 * Servlet implementation class FN01_0001
 */
@WebServlet(name = "FN01_0001", urlPatterns = { "/FN01_0001" })
public class FN01_0001 extends HttpServlet implements Servlet{
	private static final long serialVersionUID = 1L;
	
	private String user_id="";
	private String user_pass="";
	
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FN01_0001() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("txt_user_id");
		String pw = request.getParameter("txt_password");
		
		if(!(id==null || pw==null)){
			user_id = id;
			user_pass = pw;
		}
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		
		//■ユーザーID・パスワード check
		if(user_id.equals("") || user_pass.equals("")){
			//初期画面に遷移
			Util.logDisp(request, response, "", user_id, user_pass, "/FN01_0001.jsp");
			return;
		}

		//■入力項目あり
        DBAccess dbaccess = DBAccess.getInstance();
        try (Connection con = dbaccess.getConnection() ){
        	// DBコネクト
				
			// ユーザ情報取得
			UserInfo userinfo = getUserInfo(con,user_id,user_pass);
			if(userinfo == null){
		        //元の画面【ログイン画面】に遷移
				Util.logDisp(request, response, "ユーザIDまたはパスワードが違います", user_id, user_pass, "/FN01_0001.jsp");
				return;
			}
			
			// メニュー情報取得
			List<MenuInfo> menuinfoList = getMenuInfoList(con,userinfo.getAuthority());
			HttpSession session = request.getSession();
			// ここだけrequestではなくsessionを使っている
        	session.setAttribute("user_id",   userinfo.getUser_id());
        	session.setAttribute("user_name", userinfo.getUser_name());
        	session.setAttribute("MenuData", menuinfoList);
			String disp = "/FN02_0001.jsp";					
			RequestDispatcher dispatch = request.getRequestDispatcher(disp);
			dispatch.forward(request, response);
			
        } catch ( Exception e ) {
	       	e.printStackTrace();
	    }
		
	}
	
	/**
	 * ユーザ情報取得
	 * @param con
	 * @param id
	 * @param pw
	 * @return
	 */
	private UserInfo getUserInfo(Connection con,String id,String pw){
        UserInfo ret = null;
		// sql作成
		StringBuilder sql = new StringBuilder();
		
        sql.append("select user_id, user_name, authority from m_user ")
        .append("where user_id = ? ")
        .append("and password = ?");

        try ( PreparedStatement pstmt  = con.prepareStatement( sql.toString() ) ){
            pstmt.setString(1, id);
            pstmt.setString(2, pw);
            ResultSet result=pstmt.executeQuery();
	        if(result.next()){
	            ret = new UserInfo();
	            ret.setUser_id(result.getString("user_id"));
	            ret.setUser_name(result.getString("user_name"));
	            ret.setAuthority(result.getInt("authority"));
	        }

        } catch ( Exception e ) {
        	e.printStackTrace();
    	}
        
        return ret;

	}
	/**
	 * メニュー情報取得
	 * @param con
	 * @param authority
	 * @return
	 */
	private List<MenuInfo> getMenuInfoList(Connection con,int authority){
        PreparedStatement pstmt  = null;
        List<MenuInfo> retList = new ArrayList<MenuInfo>();
		// sql作成
		StringBuilder sql = new StringBuilder();
		
		sql.append("select screen_id,disp_name from m_menu ")
		.append("where authority <= ? ")
		.append("order by order_no");

        try{
            pstmt  = con.prepareStatement(sql.toString());
            pstmt.setInt(1, authority);
            ResultSet result=pstmt.executeQuery();
	        while(result.next()){
	        	
	        	MenuInfo menu = new MenuInfo();
	        	menu.setScreen_id(result.getString("screen_id"));
	        	menu.setDisp_name(result.getString("disp_name"));
	        	retList.add(menu);
	        }
        }catch(Exception e){
        	e.printStackTrace();
    	}finally{
			if(pstmt!=null){
		        try{
		        	pstmt.close();
		        }catch(Exception e){
		        }
			}
    	}
		return retList;
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

