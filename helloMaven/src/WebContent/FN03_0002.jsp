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
	var newAction = "./FN03_0002";
	switch(actionType) {
		case 'add':
			newAction = "./FN03_0002";
			break;
		case 'back':
			newAction = "./FN03_0001";
			break;

		default:
			alert('不正な入力');
			break;
	}
	
	//フォームの送信先デフォルトaction="./dummy"　を切り替える
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

String userId = (String)request.getAttribute("userId");
if(userId==null){
	userId="";
}

String userName = (String)request.getAttribute("userName");
if(userName==null){
	userName="";
}

String password = "";
String rePassword = "";
%>


<body class="body">
<FORM method="POST" action="./FN03_0002">
	<table> 
		<tr>
			<td id="systemNameTd" colspan="2"><label class="systemName">ドキュメント管理システム</label></td>
		</tr>
    	<tr>
    		<td class="inputLabel">ユーザID</td>
    		<td>
    			<input class="inputAdd" type="text" id="id_txt_user_id" name="txt_user_id" maxlength="20" value="<%= userId %>"/>
    		</td>
    	</tr>
       	<tr>
       		<td class="inputLabel">ユーザ名</td>
       		<td>
       			<input class="inputAdd" type="text" id="id_txt_user_name" name="txt_user_name" maxlength="20" value="<%= userName %>" />
       		</td>
       	</tr>
        	<tr>
       		<td class="inputLabel">パスワード</td>
       		<td>
       			<input class="inputAdd" type="password" id="id_txt_password" name="txt_password" maxlength="20" value="<%= password %>" />
       		</td>
       	</tr>
        	<tr>
       		<td class="inputLabel">再パスワード</td>
       		<td>
       			<input class="inputAdd" type="password" id="id_txt_re_password" name="txt_re_password" maxlength="20" value="<%= rePassword %>" />
       		</td>
       	</tr>
        	<tr>
       		<td class="inputLabel">権限</td>
       		<td>
       			<select id="authority" name="authority">
       				<option selected value="0">一般ユーザ</option>
       				<option value="9">管理ユーザ</option>
       			</select>
       		</td>
       	</tr>
	</table>
	<br><br>

	
	<table>
		<tr>
			<td id="btn_login_leftTd"></td>
			<td>
				<input class="button" type="submit" name="btn_add" value="追加" onClick="jumpList('add')" />
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