package pack.com;

public class Const {
	public static final String Driver = "org.postgresql.Driver";
	public static final String Url = "jdbc:postgresql://localhost:5432/db_doc_manage";
	public static final String User = "postgres";
	public static final String Pass = "postgres";
	
	public static final int IPPAN = 0; //一般ユーザ
	public static final int KANRI = 9; //管理ユーザ
	
	public static final int CATEGORY_NUM = 3; //カテゴリ数
	public static final int FILESIZE_MAX = 5 * 1024 * 1024; // 5MBが限界
	
	public static final int INVALID_SELECT = -1;

}
