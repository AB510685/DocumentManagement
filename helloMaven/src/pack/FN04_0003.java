package pack;

import javax.servlet.annotation.WebServlet;
import java.io.*;
import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.PreparedStatement;
import java.sql.Statement; //Preparedとどう違うのか？
import java.time.LocalDateTime;

//import java.sql.ResultSet;
//import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.xml.bind.DatatypeConverter;

//import pack.bean.CategoryInfo;
import pack.bean.FileInfo;
import pack.com.DBAccess;
import pack.com.Util;
//import static pack.com.Const.*;


/**
 * Servlet implementation class FN04_0003
 */
@WebServlet(name = "FN04_0003", urlPatterns = { "/FN04_0003" })
public class FN04_0003 extends HttpServlet implements Servlet{
	private static final long serialVersionUID = 1L;
	private static final String defaultDisp = "/FN04_0003.jsp"; //元画面
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FN04_0003() {
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

		HttpSession session = request.getSession();
		String user_id = (String)session.getAttribute("user_id");
		if (user_id == null) {
			//画面を戻す
			Util.docDisp( request, response, "更新ユーザID取得失敗。", defaultDisp );
			return;					
		}
		// 作成ユーザではなく更新ユーザにセット
		fileInfo.setUpdate_user_id(user_id);
	
		// 現在時刻を更新日時に設定
		fileInfo.setUpdate_date(LocalDateTime.now());

		// 入力正常
        DBAccess dbaccess = DBAccess.getInstance();
        try ( Connection con = dbaccess.getConnection() ){
     	
        	//　ファイル変更
        	updateFileInfo( con, fileInfo );
				
		} catch (Exception e ){
			e.printStackTrace();
		} 

        // 正常に画面遷移
        Util.docDisp( request, response, "ファイル変更成功",  defaultDisp );

	}


	/**
	 * ファイル変更
	 * @param con
	 * @param id
	 * @param pw
	 * @return
	 */
	private void updateFileInfo(Connection con, FileInfo fileInfo){
		// sql作成
		StringBuilder sql = new StringBuilder();
		
        sql.append("update d_file set ")
        .append( "category_1=" + fileInfo.getCatetory_1() + " , ")
        .append( "category_2=" + fileInfo.getCatetory_2() +  " , ")
        .append( "category_3=" + fileInfo.getCatetory_3() +  " , ")
        .append( "file_name='" + fileInfo.getFile_name() + "' , ")
        .append( "file_contents='" + DatatypeConverter.printHexBinary(fileInfo.getFile_contents()) + "' , ") // byte[]型だがStringのように扱っていいのか？
        .append( "update_user_id='" + fileInfo.getUpdate_user_id() + "' , ")
        .append( "update_date='=" + fileInfo.getUpdate_date() + "' ")
        .append( "where file_id=" + fileInfo.getFile_id())
        ;

        //System.out.println( "sql:" + sql.toString() );
 
        try ( Statement stmt = con.createStatement() ){
        	if (stmt.executeUpdate(sql.toString()) != 1 ) {
        		System.err.println( "DB Update Err" );
        		
        	} else {
        		System.out.println("DB Update Succeeded!");
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
