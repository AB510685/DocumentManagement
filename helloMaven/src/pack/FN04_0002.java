package pack;

import javax.servlet.annotation.WebServlet;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
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
 * Servlet implementation class FN04_0002
 */
@WebServlet(name = "FN04_0002", urlPatterns = { "/FN04_0002" })
public class FN04_0002 extends HttpServlet implements Servlet{
	private static final long serialVersionUID = 1L;
	private static final String defaultDisp = "/FN04_0002.jsp"; //元画面
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FN04_0002() {
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
			Util.docDisp( request, response, "作成ユーザID取得失敗。", defaultDisp );
			return;
		}
		fileInfo.setCreate_user_id(user_id);
	
		// 現在時刻を作成日時に設定
		fileInfo.setCreate_date(LocalDateTime.now());		
		
		// 入力正常
        DBAccess dbaccess = DBAccess.getInstance();
        try ( Connection con = dbaccess.getConnection() ){
    		// ファイル重複チェック
    		if ( isFile( con, fileInfo ) ) {
    			//画面を戻す
    			Util.docDisp( request, response, "既存のファイルと重複します:" +  fileInfo.getFile_name(), defaultDisp );
    			return;			
    		}	
        	
        	//　ファイル追加
        	addFileInfo( con, fileInfo );
				
		} catch (Exception e ){
			e.printStackTrace();
		} 

        // 正常に画面遷移
        Util.docDisp( request, response, "ファイル追加成功",  defaultDisp );

	}


	/**
	 * ファイル追加
	 * @param con
	 * @param id
	 * @param pw
	 * @return
	 */
	private void addFileInfo(Connection con, FileInfo fileInfo){
		// sql作成
		StringBuilder sql = new StringBuilder();
		
        sql.append("insert into d_file ")
        .append("(category_1, category_2, category_3, file_name, file_contents, create_user_id, create_date) ")
        //.append("(category_1, category_2, category_3, file_name, create_user_id, create_date) ")
        .append("values (")
        //.append("DEFAULT, " ) // プライマリキーfile_idはユニークに。できれば連番か？
        .append( fileInfo.getCatetory_1() +  " , ")
        .append( fileInfo.getCatetory_2() +  " , ")
        .append( fileInfo.getCatetory_3() +  " , ")
        .append("'" + fileInfo.getFile_name() + "' , ")
        .append("'" + DatatypeConverter.printHexBinary(fileInfo.getFile_contents()) + "' , ") // byte[]型だがStringのように扱っていいのか？
        .append("'" + fileInfo.getCreate_user_id() + "' , ")
        .append("'" + fileInfo.getCreate_date() + "'")
        .append(" )")
        ;

        //System.out.println( "sql:" + sql.toString() );
        //System.out.println( "hexStr:" + DatatypeConverter.printHexBinary(fileInfo.getFile_contents()));
 
        try ( Statement stmt = con.createStatement() ){
        	if (stmt.executeUpdate(sql.toString()) != 1 ) {
        		System.err.println( "DB Insert Err" );
        		
        	} else {
        		System.out.println("DB Insert Succeeded!");
        	}
        } catch ( Exception e ) {
        	e.printStackTrace();
    	}
 
        /*
		StringBuilder sql2 = new StringBuilder();
		
        sql2.append("update d_file set file_contents=? ")
        .append("where file_id=" + fileInfo.getFile_id())
        ;
        
        System.out.println( "sql2:" + sql2.toString() );
        try ( PreparedStatement ps = con.prepareStatement( sql2.toString() ) ) {
          	ps.setObject(1, fileInfo.getFile_contents());
        	ps.executeUpdate();
        } catch ( Exception e ) {
        	e.printStackTrace();
        }
      */
        return;

	}

	// カテゴリ3つとファイル名が一致するものが既にd_fileにあればtrueを返す
	private boolean isFile( Connection con, FileInfo fileInfo ) {
		boolean ret = false;
		StringBuilder sql = new StringBuilder();
		
        sql.append("select * from d_file where ")
        .append("category_1=" + fileInfo.getCatetory_1() + " and ")
        .append("category_2=" + fileInfo.getCatetory_2() + " and ")
        .append("category_3=" + fileInfo.getCatetory_3() + " and ")
        .append("file_name='" + fileInfo.getFile_name() + "'")
   		;
        
        System.out.println("isFile sql:" + sql.toString());
        
        try ( PreparedStatement pstmt  = con.prepareStatement( sql.toString() ) ) {
            ResultSet result = pstmt.executeQuery();
            // 検索が1件ならtrueを返し、0件ならfalseを返す
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
