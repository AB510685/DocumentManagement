<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<title>ドキュメント管理システム</title>
<script type="text/javascript" src="./js/FN01_0001.js"></script>
<link rel=stylesheet type="text/css" href="${pageContext.request.contextPath}/css/com.css" />
<link rel=stylesheet type="text/css" href="${pageContext.request.contextPath}/css/FN01_0001.css" />
</head>
<%
request.setCharacterEncoding("Shift_JIS");
String msg_id = (String)request.getAttribute("msg_id");
if(msg_id==null){
	msg_id="";
}
String msg_pw = (String)request.getAttribute("msg_pw");
if(msg_pw==null){
	msg_pw="";
}

String msg = (String)request.getAttribute("msg");
if(msg==null){
	msg="";
}

%>

<body class="body">
<FORM method="POST" action="./FN01_0001" onSubmit="return checkLogin()">
	<table> 
		<tr>
			<td id="systemNameTd" colspan="2"><label class="systemName">ドキュメント管理システム</label></td>
		</tr>
    	<tr>
    		<td class="inputLabel">ユーザID</td>
    		<td>
    			<input class="inputLogin" type="text" id="id_txt_user_id" name="txt_user_id" maxlength="20" value="<%= msg_id %>"/>
    		</td>
    	</tr>
       	<tr>
       		<td class="inputLabel">パスワード</td>
       		<td>
       			<input class="inputLogin" type="password" id="id_txt_password" name="txt_password" maxlength="20" value="<%= msg_pw %>" />
       		</td>
       	</tr>
	</table>
	<br><br>
	
	<table>
		<tr>
			<td id="btn_login_leftTd"></td>
			<td>
				<input class="button" type="submit" name="btn_login" value="ログイン" />
			</td>
		</tr>
		<tr>
			<td rowspan="2"></td>
		</tr>
		<tr>
			<td id="btn_login_leftTd"></td>
			<td>
				<input class="button"  type="submit" name="btn_end" value="終了" onClick="window.close()"/>
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