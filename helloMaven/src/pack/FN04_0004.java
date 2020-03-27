package pack;

import javax.servlet.annotation.WebServlet;
import java.io.*;
import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.PreparedStatement;
import java.sql.Statement; //Preparedとどう違うのか？
//import java.time.LocalDateTime;

//import java.sql.ResultSet;
//import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;
//import pack.bean.CategoryInfo;
import pack.bean.FileInfo;
import pack.com.DBAccess;
import pack.com.Util;
//import static pack.com.Const.*;

/**
 * Servlet implementation class FN04_0004
 */
@WebServlet(name = "FN04_0004", urlPatterns = { "/FN04_0004" })
public class FN04_0004 extends HttpServlet implements Servlet{
	private static final long serialVersionUID = 1L;
	private static final String defaultDisp = "/FN04_0004.jsp"; //元画面
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FN04_0004() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
 
		// FN04_0002からFN04_0004までの共通処理
		boolean check = Util.docCommon( request, response, defaultDisp );
		if ( check ) { // 本当はこの分岐に入る前に画面遷移してしまうとおもうがやっておこう
			return;
		}
		
		// データ取得
		FileInfo fileInfo = new FileInfo();
		String msg = "";
		msg = Util.getFileInfo( request, fileInfo );
		//データ取得失敗
		if ( !msg.equals("") ) {
			//画面を戻す
			Util.docDisp( request, response, msg, defaultDisp );
			return;			
		}

		// FN04_0004.jspのためにデータセットしておく
		// これがないと削除終了後にindexとファイル名が変わってしまうよ
		request.setAttribute( "fileName", fileInfo.getFile_name() );

		HttpSession session = request.getSession();
		String user_id = (String)session.getAttribute("user_id");
		if (user_id == null) {
			//画面を戻す
			Util.docDisp( request, response, "削除ユーザID取得失敗。", defaultDisp );
			return;					
		}

		// 入力正常
        DBAccess dbaccess = DBAccess.getInstance();
        try ( Connection con = dbaccess.getConnection() ){
     	
        	//　ファイル削除
        	deleteFileInfo( con, fileInfo );
				
		} catch (Exception e ){
			e.printStackTrace();
		} 

        // 正常に画面遷移
        Util.docDisp( request, response, "ファイル削除成功",  defaultDisp );

	}


	/**
	 * ファイル削除
	 * @param con
	 * @param id
	 * @param pw
	 * @return
	 */
	private void deleteFileInfo(Connection con, FileInfo fileInfo){
		// sql作成
		StringBuilder sql = new StringBuilder();

        sql.append("delete from d_file ")
        .append( "where file_id=" + fileInfo.getFile_id() )
        ;

        System.out.println( "sql:" + sql.toString() );
 
        try ( Statement stmt = con.createStatement() ){
        	if (stmt.executeUpdate(sql.toString()) != 1 ) {
        		System.err.println( "DB Delete Err" );
        		
        	} else {
        		System.out.println("DB Delete Succeeded!");
        	}
        } catch ( Exception e ) {
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
