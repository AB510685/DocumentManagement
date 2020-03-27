<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.util.List"%>
<%@ page import="pack.bean.UserInfo"%>
<%@ page import="static pack.com.Const.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<title>ドキュメント管理システム</title>
<script type="text/javascript" src="./js/FN03_0002.js"></script>
<script type="text/javascript">

function jumpList( actionType ) {
	//alert(actionType);
	var newAction = "./FN03_0004";
	switch(actionType) {
		case 'delete':
			newAction = "./FN03_0004";
			break;
		case 'back':
			newAction = "./FN03_0001";
			break;

		default:
			alert('不正な入力');
			break;
	}
	
	//フォームの送信先デフォルトaction="./FN01_0001"　を切り替える
	document.getElementById('form').action = newAction;
}

</script>
<link rel=stylesheet type="text/css" href="${pageContext.request.contextPath}/css/com.css" />
</head>
<%
request.setCharacterEncoding("Shift_JIS");

String msg = (String)request.getAttribute("msg");
if(msg==null){
	msg="";
}

UserInfo info = (UserInfo)session.getAttribute("info");
if (info == null) {
	System.err.println("sessionからinfo取得失敗");
}
String userId = info.getUser_id();
String userName = info.getUser_name();
int auth = info.getAuthority();
String password = "";
String rePassword = "";

%>


<body class="body">
<FORM method="POST" action="./FN03_0004">
	<table> 
		<tr>
			<td id="systemNameTd" colspan="2"><label class="systemName">ドキュメント管理システム</label></td>
		</tr>
    	<tr>
    		<td class="inputLabel">ユーザID</td>
    		<td>
    			<input class="inputAdd" type="text" id="id_txt_user_id" name="txt_user_id" maxlength="20" readonly="readonly" value="<%= userId %>"/>
    		</td>
    	</tr>
       	<tr>
       		<td class="inputLabel">ユーザ名</td>
       		<td>
       			<input class="inputAdd" type="text" id="id_txt_user_name" name="txt_user_name" maxlength="20" readonly="readonly" value="<%= userName %>" />
       		</td>
       	</tr>
        	<tr>
       		<td class="inputLabel">権限</td>
       		<td>

<%
	String user = "不明なユーザ";
		switch( auth ) {
		case IPPAN:
			user = "一般ユーザ";
			break;
		case KANRI:
			user = "管理ユーザ";
			break;
		default:
			System.err.println(user);
			break;
	}
%>	
       			<select id="authority" name="authority">	
					<option value=<%= auth + "" %>><%= user %></option>
       			</select>
       		</td>
       	</tr>
	</table>
	<br><br>

	
	<table>
		<tr>
			<td id="btn_login_leftTd"></td>
			<td>
				<input class="button" type="submit" name="btn_delete" value="削除" onClick="jumpList('delete')" />
			</td>
		</tr>
		<tr>
			<td id="btn_login_rightTd"></td>
			<td>
				<input class="button" type="submit" name="btn_back" value="戻る" onClick="jumpList('back')" />
			</td>
		</tr>
	</table>
	<input type="hidden" id="msg" value="<%= msg %>" />
</FORM>




<script type="text/javascript">
var msg=document.getElementById("msg").value;
if(msg!=""){
	alert(msg);
	document.getElementById("msg").value = "";
}
</script>

</body>
</html>