package pack;

import javax.servlet.annotation.WebServlet;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.ResultSet;
//import java.sql.PreparedStatement;
//import java.sql.Statement; //Preparedとどう違うのか？
//import java.time.LocalDateTime;

//import java.sql.ResultSet;
//mport java.util.List;
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
@WebServlet(name = "FN04_0005", urlPatterns = { "/FN04_0005" })
public class FN04_0005 extends HttpServlet implements Servlet{
	private static final long serialVersionUID = 1L;
	private static final String defaultDisp = "/FN04_0005.jsp"; //元画面
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FN04_0005() {
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
		
		// FN04_0005.jspのためにデータセットしておく
		// 削除とちがってこのロジックはいらないかもしれない。要検討
		request.setAttribute( "fileName", fileInfo.getFile_name() );

		// 入力正常
        DBAccess dbaccess = DBAccess.getInstance();
        try ( Connection con = dbaccess.getConnection() ){
     	
        	//　ファイルダウンロード
        	downloadFileInfo( con, fileInfo, response );
				
		} catch (Exception e ){
			e.printStackTrace();
		} 

        // 正常に画面遷移
        // downloadFileInfoでレスポンスを送っている。
        // ここでdocDispを呼ぶとフォワードしようとし、
        // java.lang.IllegalStateException: レスポンスをコミットした後でフォワードできません
        // となるから呼べない
        //Util.docDisp( request, response, "ファイルダウンロード成功",  defaultDisp );

	}


	/**
	 * ファイルダウンロード
	 * 2017/7/28現在の仕様では、getFileInfoにファイルデータを詰めている。
	 * だからダウンロード時にDBアクセスは必要ないことになる。
	 * これだと巨大データをUploadしたあとでパフォーマンスが非常に劣化するから、
	 * 今後のことを考えてあえてDBアクセス型とすｒ
	 * @param con
	 * @param id
	 * @param pw
	 * @return
	 */
	private void downloadFileInfo(Connection con, FileInfo fileInfo, HttpServletResponse response){
		// sql作成
		StringBuilder sql = new StringBuilder();

        sql.append("select file_contents from d_file ")
        .append( "where file_id=" + fileInfo.getFile_id() )
        ;

        System.out.println( "sql:" + sql.toString() );
 
        byte[] file_contents = null;
       	try ( PreparedStatement pstmt  = con.prepareStatement( sql.toString() ) ){
        	ResultSet result = pstmt.executeQuery();
        	if ( !result.next() ) {
        		System.err.println("ダウンロード用DB検索失敗");
        		return;
        	}
        	//　file_contentsしかないから、引数はintで0でもよいとおもわれるが
        	String hexStr = result.getString("file_contents");
        	//System.out.println( "download hexStr:" + hexStr);
        	String temp = hexStr.substring(2); // PostgreSQLに格納する際に'\x'が先頭に付加されるので切り取り
        	byte[] temp2 = DatatypeConverter.parseHexBinary( temp );
        	file_contents = Util.DecArrayToBin( temp2 );
        } catch ( Exception e ) {
        		e.printStackTrace();
    	}
 
       	response.setContentType("application/octet-stream; charset=Shift_JIS");
       	response.setHeader("Content-Disposition","attachment; filename=" + fileInfo.getFile_name());
       	response.setContentLength(file_contents.length);

       	try (ServletOutputStream os = response.getOutputStream()){
       		os.write(file_contents);
       	} catch ( Exception e) {
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
