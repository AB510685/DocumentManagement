package pack.bean;

public class UserInfo {
	private String user_id ="";
	private String user_name ="";
	private int authority;
	private String password = ""; //add Takeshi Mizuno 20170719
	
	public UserInfo() {
		//何もしない
	}

	public UserInfo( String user_id, String user_name, String password, int authority ) {
		this();
		this.user_id = user_id;
		this.user_name = user_name;
		this.authority = authority;
		this.password = password;
	}
	/**
	 * @return the user_id
	 */
	public String getUser_id() {
		return user_id;
	}
	/**
	 * @param user_id the user_id to set
	 */
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	/**
	 * @return the user_name
	 */
	public String getUser_name() {
		return user_name;
	}
	/**
	 * @param user_name the user_name to set
	 */
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	/**
	 * @return the authority
	 */
	public int getAuthority() {
		return authority;
	}
	/**
	 * @param authority the authority to set
	 */
	public void setAuthority(int authority) {
		this.authority = authority;
	}


	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

}
