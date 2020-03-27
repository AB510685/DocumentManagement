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
<script type="text/javascript" src="./js/FN03_0001.js"></script>
<script type="text/javascript">

var msg=document.getElementById("msg").value;
if(msg!=""){
	alert(msg);
	document.getElementById("msg").value = "";
}

// FN03_0001.jsに移動すると下の関数が動かないのはなぜ？
// documentとかformといったシンボルが見えないのか？
function jumpList( actionType ) {
	//alert(actionType);
	var newAction = "./FN03_0001";
	switch(actionType) {
		case 'add':
			newAction = "./FN03_0002";
			break;
		case 'update':
			newAction = "./FN03_0003";
			break;
		case 'delete':
			newAction = "./FN03_0004";
			break;
		case 'back':
			newAction = "./FN02_0001";
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
String txt_search_user_id = (String)request.getAttribute("txt_search_user_id");
if(txt_search_user_id==null){
	txt_search_user_id="";
}
String txt_search_user_name = (String)request.getAttribute("txt_search_user_name");
if(txt_search_user_name==null){
	txt_search_user_name="";
}

String msg = (String)request.getAttribute("msg");
if(msg==null){
	msg="";
}

%>

<body class="body">

	<table> 
		<FORM method="POST" action="./FN03_0001">
		<tr>
			<td id="systemNameTd" colspan="2"><label class="systemName">ドキュメント管理システム</label></td>
		</tr>
    	<tr>
    		<td class="inputLabel">ユーザID</td>
    		<td>
    			<input class="inputLogin" type="text" id="id_txt_user_id" name="txt_search_user_id" maxlength="20" size="20" value="<%= txt_search_user_id %>"/>
    		</td>
    	</tr>
       	<tr>
       		<td class="inputLabel">ユーザ名</td>
       		<td>
       			<input class="inputLogin" type="text" id="id_txt_user_name" name="txt_search_user_name" maxlength="100" size="100" value="<%= txt_search_user_name %>" />
       		</td>
       		
       		<td rowspan="2" valign="bottom">
				<input class="button" type="submit" name="btn_search" value="検索" />
			</td>
		</tr>
       	</FORM>
	</table>
	<br>

	<table border="1" width="900"> 
		<form method="POST" id="form" name="form" action="dummy">
		<!-- dummyの値は関数jumpListで更新される -->
		<tr>
			<td width="50" align="center">選択</td>	<td width="600"  align="center">ユーザ名</td>	<td width="200" align="center">ユーザID</td>	<td width="50" align="center">権限</td>
		</tr>
<% 
String user_name = "";
List<UserInfo> userInfo = (List<UserInfo>)request.getAttribute("userinfo");
String index = "";
if ( userInfo != null) {
	session.setAttribute("userInfo", userInfo);

	for(int i = 0; i < userInfo.size(); i++) {
		user_name = userInfo.get(i).getUser_name();
		String user_id =  userInfo.get(i).getUser_id();
		String authority = "";
		int auth = userInfo.get(i).getAuthority();
		switch(auth) {
			case IPPAN:
				authority = "一般";
				break;
			case KANRI:
				authority = "管理";
				break;
			default:
				authority = "不明";
				break;
		}

%>
	
		<tr> 
			<td align="center"><input type="radio" name="select" value=<%= i + "" %> <%= i == 0 ? "checked" : "" %>/></td>
			<td><%= user_name %></td>
			<td><%= user_id %></td>
			<td align="center"><%= authority %></td>
		</tr>
<% 
	}
}
%>
	</table>
	<br>
			
	<table>
		<tr>
			<td id="btn_login_leftTd"></td>
			<td>
				<input class="button" type="submit" name="btn_add" value="追加" onClick="jumpList('add')" />
			</td>
			<td>
				<input class="button" type="submit" name="btn_upd" value="変更" onClick="jumpList('update')" />
			</td>
			<td>
				<input class="button" type="submit" name="btn_del" value="削除" onClick="jumpList('delete')" />
			</td>
			<td colspan="10">
				<div align="left">
				<input class="button" type="submit" name="btn_back" value="戻る"  onClick="jumpList('back')" />
				<input type="hidden" id="msg" value="<%= msg %>" />
				</div>
			</td>
		</tr>
	</table>
		<input type="hidden" id="msg" value="<%= msg %>" />
		</FORM>



</body>
</html>