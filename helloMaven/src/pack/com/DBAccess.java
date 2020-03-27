package pack.com;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBAccess {

	private static DBAccess dbaccess = null;
	private Connection connection = null;
	
	/**
	 * コンストラクタ
	 * 外部からnewさせない
	 */
	private DBAccess(){
		
	}
	/**
	 * 自クラスインスタンス取得
	 * @return　DBAccess
	 */
	public static DBAccess getInstance(){
		if(dbaccess==null){
			dbaccess = new DBAccess();
		}
		return dbaccess;
		
	}
	/**
	 * コネクション生成
	 * @return
	 * @throws Exception
	 */
	public Connection getConnection() throws Exception{
		try{
	    	//postgresJDBCドライバ
	        Class.forName(Const.Driver);
			connection=DriverManager.getConnection(Const.Url,
					Const.User,
					Const.Pass);
			return connection;
		}
		catch(Exception e){
			throw e;
		}

	}
	/**
	 * コネクションクローズ
	 * @throws Exception
	 */
	public void closeConnection() {
		try{
			if(!connection.isClosed()){
				connection.close();
			}
		}
		catch(Exception e){
		}
	}

}
