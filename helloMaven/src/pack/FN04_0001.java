package pack;

import javax.servlet.annotation.WebServlet;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.*;
import javax.servlet.http.*;

import pack.bean.CategoryInfo;
import pack.bean.FileInfo;
import pack.com.DBAccess;
import pack.com.Util;
import static pack.com.Const.*;

/**
 * Servlet implementation class FN04_0001
 */
@WebServlet(name = "FN04_0001", urlPatterns = { "/FN04_0001" })
public class FN04_0001 extends HttpServlet implements Servlet{
	private static final long serialVersionUID = 1L;
	
	private static List<CategoryInfo> category1 = null;
	private static List<CategoryInfo> category2 = null;
	private static List<CategoryInfo> category3 = null;

	private static final String defaultDisp = "/FN04_0001.jsp"; //元画面
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FN04_0001() {
        super();
    }

    // FN04_0001に初めて入ったときに、カテゴリを取得してセッション登録する
    private void initialize( HttpSession session ) {
		category1 = new ArrayList<>();
		category2 = new ArrayList<>();
		category3 = new ArrayList<>();
        DBAccess dbaccess = DBAccess.getInstance();
        try ( Connection con = dbaccess.getConnection() ){
    		getCategories(con);
    		session.setAttribute("category1", category1 );
    		session.setAttribute("category2", category2 );
    		session.setAttribute("category3", category3 );
        } catch( Exception e ) {
        	e.printStackTrace();
        	return;
        } 

    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	HttpSession session = request.getSession();
    	
    	// 初めてこの画面に来たら実行
    	if( category1 == null || category2 == null || category3 == null) {
    		initialize( session );
        	Util.docDisp(request, response, "", defaultDisp);
    		return;
    	}
    	
    	
    	String cate1 = request.getParameter("category_id1");
    	if ( cate1 != null ) {
    		session.setAttribute("category1_select_id", cate1 ); // パラメータからもらった情報をセッションにセットしておく
    	} else {
    		cate1 = "";
    	}
    	System.out.println("category1_select_id:" + cate1);
    	
    	String cate2 = request.getParameter("category_id2");
    	if ( cate2 != null ) {
    		session.setAttribute("category2_select_id", cate2 ); // パラメータからもらった情報をセッションにセットしておく
    	} else {
    		cate2 = "";
    	}
    	
    	String cate3 = request.getParameter("category_id3");
    	if ( cate3 != null ) {
    		session.setAttribute("category3_select_id", cate3 ); // パラメータからもらった情報をセッションにセットしておく
    	} else {
    		cate3 = "";
    	}
    
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");

		// カテゴリ選択チェック
		if(cate1.equals("") || cate2.equals("") || cate3.equals("")){
			//画面を戻す
    		session.setAttribute("category1", category1 );
    		session.setAttribute("category2", category2 );
    		session.setAttribute("category3", category3 );
        	Util.docDisp(request, response, "カテゴリID情報取得失敗", defaultDisp);
			return;
		}

	
		// ■入力項目あり
		DBAccess dbaccess = DBAccess.getInstance();
	    try ( Connection con = dbaccess.getConnection() ){
			// ファイル情報取得
	    	int id1 = Integer.parseInt(cate1);
	    	int id2 = Integer.parseInt(cate2);
	    	int id3 = Integer.parseInt(cate3);
	    	// Utilではなくこのクラス内のメソッドを呼び出している
	    	List<FileInfo> fileInfo = getFileInfoFromDb( con, id1, id2, id3 );
	    	
	    	// セッションにデータ保存
	    	session.setAttribute("fileInfo", fileInfo);
				
        } catch ( Exception e ) {
	        e.printStackTrace();
	    }
	    Util.docDisp(request, response, "", defaultDisp);
	}

	/**
	 * カテゴリ情報取得
	 * @param con
	 * @param id
	 * @param pw
	 * @return
	 */
    private void getCategories( Connection con ) {
    	
		// sql作成
		StringBuilder sqls[] = new StringBuilder[CATEGORY_NUM];
		String common = "select category_id, category_name from m_category";

		for( int i = 0; i < sqls.length; i++ ) {
			sqls[i] = new StringBuilder();
			sqls[i].append( common + (i + 1) );
		}
    

        System.out.println("sqls[0]:" + sqls[0].toString());

		for( int i = 0; i < sqls.length; i++ ) {
			List<CategoryInfo> category = null;
			switch(i) {
			case 0:
				category = category1;
				break;
			case 1:
				category = category2;
				break;
			case 2:
				category = category3;
				break;
			default:
				System.err.println("Invalid value");
				break;
			}
	
        	try ( PreparedStatement pstmt  = con.prepareStatement( sqls[i].toString() ) ){
            //pstmt.setString(1, id);
            //pstmt.setString(2, name);
        		ResultSet result = pstmt.executeQuery();
        		while ( result.next() ){
        			CategoryInfo record = new CategoryInfo();
        			record.setCategory_id( Integer.parseInt( result.getString("category_id") ) );
        			record.setCategory_name(result.getString("category_name"));
        			category.add(record);
        		}

        	} catch ( Exception e ) {
        		e.printStackTrace();
        	}
    	}


	}

	/**
	 * ファイル情報取得
	 * @param con
	 * @param id
	 * @param pw
	 * @return
	 */
    private List<FileInfo> getFileInfoFromDb( Connection con, int id1, int id2, int id3 ) {
    	List<FileInfo> ret = new ArrayList<>();
    	
		// sql作成 
		StringBuilder sql = new StringBuilder();
		sql.append( "select file_id, category_1, category_2, category_3, file_name from d_file where " )
		.append( "category_1=" + id1 + " and " )
		.append( "category_2=" + id2 + " and " )		
		.append( "category_3=" + id3  )
		;

        System.out.println("sql:" + sql.toString());

       	try ( PreparedStatement pstmt  = con.prepareStatement( sql.toString() ) ){
            //pstmt.setString(1, id);
            //pstmt.setString(2, name);
        	ResultSet result = pstmt.executeQuery();
        	while ( result.next() ){
        		FileInfo record = new FileInfo();
 
       			record.setFile_id(Integer.parseInt(result.getString("file_id")));
       			record.setCategory_1(Integer.parseInt(result.getString("category_1")));
       			record.setCategory_2(Integer.parseInt(result.getString("category_2")));
       			record.setCategory_3(Integer.parseInt(result.getString("category_3")));
       			record.setFile_name(result.getString("file_name"));

       			ret.add(record);
        	}

        } catch ( Exception e ) {
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
