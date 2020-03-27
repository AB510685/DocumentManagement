package pack.com;

import static pack.com.Const.FILESIZE_MAX;

//import javax.servlet.annotation.WebServlet;
import java.io.*;
//import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.PreparedStatement;
//import java.sql.Statement; //Preparedとどう違うのか？
//import java.sql.ResultSet;
//import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;
//import pack.bean.UserInfo;
//import pack.com.DBAccess;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import pack.bean.FileInfo;
import pack.bean.UserInfo;

/**
 * Utility class 
 */
public class Util {
	//private static final long serialVersionUID = 1L;

	
    /**
     * @see HttpServlet#HttpServlet()
     */
    private Util() { //インスタンスを作らない
        ;
    }

	//ユーザー画面遷移  
    public static void logDisp( HttpServletRequest request, HttpServletResponse response, 
    		String msg, String id, String pass, String disp ) {

    	// ここはrequestでありsessionではないことに注意
		request.setAttribute("msg", msg);
		request.setAttribute("userId", id);
		request.setAttribute("userName", pass);
		System.out.println("logDisp msg:" + msg);
		
		RequestDispatcher dispatch = request.getRequestDispatcher(disp);
		try {
			dispatch.forward(request, response);   
		} catch ( ServletException | IOException e) {
			e.printStackTrace();
		}
    }

	//ユーザー画面遷移  
    public static void menuDisp( HttpServletRequest request, HttpServletResponse response, 
    		String msg, String id, String name, String disp ) {

    	// ここはrequestでありsessionではないことに注意
		request.setAttribute("msg", msg);
		request.setAttribute("txt_search_user_id", id);
		request.setAttribute("txt_search_user_name", name);
		System.out.println("menuDisp msg:" + msg);
		
		RequestDispatcher dispatch = request.getRequestDispatcher(disp);
		try {
			dispatch.forward(request, response);   
		} catch ( ServletException | IOException e) {
			e.printStackTrace();
		}
    }    
    
	//ユーザー画面遷移  
    public static void userDisp( HttpServletRequest request, HttpServletResponse response, 
    		String msg, String id, String name, String disp ) {

		request.setAttribute("msg", msg);
		request.setAttribute("userId", id);
		request.setAttribute("userName", name);
		System.out.println("userDisp msg:" + msg);
		
		RequestDispatcher dispatch = request.getRequestDispatcher(disp);
		try {
			dispatch.forward(request, response);   
		} catch ( ServletException | IOException e) {
			e.printStackTrace();
		}
    }

	// ドキュメント画面遷移  
    public static void docDisp( HttpServletRequest request, HttpServletResponse response, 
    		String msg, String disp ) {

		request.setAttribute("msg", msg); // sessionではなくてかまわないか
		System.out.println("docDisp msg:" + msg);

		RequestDispatcher dispatch = request.getRequestDispatcher(disp);
		try {
			dispatch.forward(request, response);   
		} catch ( ServletException | IOException e) {
			e.printStackTrace();
		}
    }    
    
	// FN03_0001.jspから遷移時、UserInfoを取得する
	public static UserInfo getUserInfo(HttpServletRequest request, String select) {
		UserInfo ret = null;
	
		HttpSession session = request.getSession();
		if ( session == null ) {
			System.err.println("sessionオブジェクト取得失敗");
			return ret;
		}
		List<UserInfo> userInfo = (List<UserInfo>)session.getAttribute("userInfo");
		if ( userInfo == null ) {
			System.err.println("UserInfoのList取得失敗");
			return ret;
		}
		
		ret = userInfo.get(Integer.parseInt(select)); // FN03_0001.jspで選択したユーザー情報
		session.setAttribute("info", ret); //sessionに入れないと消えてしまうよ
	
		return ret;
	}

	// FileInfoにデータを詰め込む
	// 失敗したらメッセージをセット
	public static String getFileInfo(HttpServletRequest request, FileInfo fileInfo ) {
		String ret = ""; // 正常終了ならメッセージは空
		
		// file_idだけはユニークなのでこれを検索キーにする
		HttpSession session = request.getSession();
		List<FileInfo> rcvFileInfo = (List<FileInfo>)session.getAttribute("fileInfo");
		if ( rcvFileInfo == null ) {
			ret = "受信ファイル情報が取得できなかったので、file_idも取得できない。失敗";
			return ret;
		}
		int index = 0;
		String select = (String)session.getAttribute("session_select");
		if ( select != null ) {
			index = Integer.parseInt(select);
		}
		System.out.println( "select:" + select);
		fileInfo.setFile_id( rcvFileInfo.get(index).getFile_id() );

		// FileItemオブジェクトを解析してFileInfoに格納していく
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload sfu = new ServletFileUpload(factory);
		try {
			List<?> list = sfu.parseRequest(request);
			Iterator<?> iterator = list.iterator();
			while( iterator.hasNext() ) {
				FileItem item = (FileItem)iterator.next();
				if( item.isFormField() ) { //フィールド
					String paraName = item.getFieldName();
					String paraValue = item.getString();
					
					switch(paraName) {
					case "txt_file_name":
						fileInfo.setFile_name(paraValue);
						break;
					case "category_id1":
						fileInfo.setCategory_1( Integer.parseInt(paraValue) );
						break;
					case "category_id2":
						fileInfo.setCategory_2( Integer.parseInt(paraValue) );
						break;
					case "category_id3":
						fileInfo.setCategory_3( Integer.parseInt(paraValue) );
						break;
					case "btn_submit": //　使わないデータなので無視
						break;
					default:
						ret = "Invalid paraName.[" + paraName + "]";
						System.err.println(ret);
						
						return ret;
						// returnの後ろにbreakを入れるとコンパイラが怒る	
					}
					
				} else { //ファイル
					System.out.println("バイナリファイル読み込み");
					// 巨大なファイルもここで読み取るということがパフォーマンスに影響ないか気にはなる
					long fileSize = item.getSize();
					if ( fileSize > FILESIZE_MAX ) {
						ret = "ファイルサイズは5MBまでです。このファイルは[" + ( fileSize / (1024.0 * 1024.0) ) + "]Mbyteです。";
						return ret;						
					}
					fileInfo.setFile_contents( item.get() );
					System.out.println("filesize:" + fileInfo.getFile_contents().length + "byte.");
				}
			}
		} catch ( FileUploadException e ) {
			e.printStackTrace();
			
		}
		
		// ■ファイル入力チェック
		if ( fileInfo.getFile_name() == null || fileInfo.getFile_name().equals("") ) {
			ret =  "失敗。ファイル名を入力しましたか？[" + fileInfo.getFile_name() + "]";
			return ret;
		}

		// 正常終了
		return ret;
	}
	
	// FN04_0002からFN04_0004までの共通処理
	// この処理後に画面遷移
	public static boolean docCommon( HttpServletRequest request, HttpServletResponse response, String defaultDisp )  {
		HttpSession session = request.getSession();
		
		// FN04_0001.jspから遷移時の処理
		String gamenFlag = request.getParameter("gamenFlag");
		if ( gamenFlag != null && gamenFlag.equals("FN04_0001") ) {
			System.out.println("gamenFlag is FN04_0001");

			// パラメータからもらった情報をセッションに保存しておく
	
			String select = request.getParameter("select");
			if ( select != null ) {
				session.setAttribute("session_select", select);
			}
			System.out.println("select:" + select);
		
			docDisp( request, response, "", defaultDisp);
			
			return true;
		}

		// 戻るボタン処理
		String back = request.getParameter("btn_back");
		if( back != null ) {
			System.out.println("back button!");
			Util.docDisp( request, response, "",  "/FN04_0001.jsp");
			return true;
		}
		
		return false; // FN04_0001.jspから遷移時でも戻るボタンでもない場合はなにもせずfalseを返して終了
	}

	// byteを2文字ずつ食べて、16進文字列と解釈して、byteに格納する
	public static byte[] DecArrayToBin( byte[] in ) {
		byte[] out = new byte[in.length / 2];
		
		for( int i = 0; i < out.length; i++ )  {
			byte[] byteArTemp1 = { in[i * 2] };
			byte[] byteArTemp2 = { in[i * 2 + 1] };
			String StrTemp1 = new String( byteArTemp1 );
			String StrTemp2 = new String( byteArTemp2 );
			int byteTemp1 = Byte.parseByte( StrTemp1, 16 ) * 16;
			int byteTemp2 = Byte.parseByte( StrTemp2, 16 );
			out[i] = (byte)(byteTemp1 + byteTemp2);
		}
		
		return out;
	}
}
