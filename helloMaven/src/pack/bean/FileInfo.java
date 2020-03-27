package pack.bean;

import java.time.*;

public class FileInfo {
	private int file_id;
	private int category_1;
	private int category_2;
	private int category_3;
	private String file_name;
	private byte[] file_contents;
	private String create_user_id;
	private String update_user_id;
	private LocalDateTime create_date;
	private LocalDateTime update_date;	
	
	
	public FileInfo() {
		super();
		//何もしない
	}

	// カテゴリID・ファイル名・ファイルデータがわかっている場合のコンストラクタ
	public FileInfo( int category_1, int category_2, int category_3, String file_name, byte[] file_contents ) {
		this();

		this.category_1 = category_1;
		this.category_2 = category_2;
		this.category_3 = category_3;
		this.file_name = file_name;
		this.file_contents = file_contents;
	}

	public int getFile_id() {
		return this.file_id;
	}
	
	public void setFile_id( int file_id ) {
		this.file_id = file_id;
	}	

	public int getCatetory_1() {
		return this.category_1;
	}
	
	public void setCategory_1( int category_1 ) {
		this.category_1 = category_1;
	}	

	public int getCatetory_2() {
		return this.category_2;
	}
	
	public void setCategory_2( int category_2 ) {
		this.category_2 = category_2;
	}	
	
	public int getCatetory_3() {
		return this.category_3;
	}
	
	public void setCategory_3( int category_3 ) {
		this.category_3 = category_3;
	}

	public String getFile_name() {
		return this.file_name;
	}
	
	public void setFile_name( String file_name ) {
		this.file_name = file_name;
	}

	
	public byte[] getFile_contents() {
		return this.file_contents;
	}
	
	public void setFile_contents(byte[] file_contents) {
		this.file_contents = file_contents;
	}
	
	public String getCreate_user_id() {
		return this.create_user_id;
	}
	
	public void setCreate_user_id( String create_user_id ) {
		this.create_user_id = create_user_id;
	}	
	
	public String getUpdate_user_id() {
		return this.update_user_id;
	}
	
	public void setUpdate_user_id( String update_user_id ) {
		this.update_user_id = update_user_id;
	}	

	public LocalDateTime getCreate_date() {
		return this.create_date;
	}
	
	public void setCreate_date( LocalDateTime create_date ) {
		this.create_date = create_date;
	}

	public LocalDateTime getUpdate_date() {
		return this.update_date;
	}
	
	public void setUpdate_date( LocalDateTime update_date ) {
		this.update_date = update_date;
	}
}
