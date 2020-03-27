<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="pack.bean.CategoryInfo"%>
<%@ page import="pack.bean.FileInfo"%>
<%@ page import="static pack.com.Const.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<title>ドキュメント管理システム</title>
<script type="text/javascript" src="./js/FN04_0001.js"></script>
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
	var newAction = "./FN04_0001";
	switch(actionType) {
		case 'add':
			newAction = "./FN04_0002";
			break;
		case 'update':
			newAction = "./FN04_0003";
			break;
		case 'delete':
			newAction = "./FN04_0004";
			break;
		case 'download':
			newAction = "./FN04_0005";
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
List<CategoryInfo> category1 = (List<CategoryInfo>)session.getAttribute("category1");
List<CategoryInfo> category2 = (List<CategoryInfo>)session.getAttribute("category2");
List<CategoryInfo> category3 = (List<CategoryInfo>)session.getAttribute("category3");
List<List<CategoryInfo>> categories = new ArrayList<>();
categories.add(category1);
categories.add(category2);
categories.add(category3);
session.setAttribute("categories", categories);
	

String file_name = (String)request.getAttribute("file_name");
if(file_name == null){
	file_name = "";
}

String msg = (String)request.getAttribute("msg");
if(msg==null){
	msg="";
}

%>

<body class="body">

	<table> 
		<FORM method="POST" action="./FN04_0001">
		<tr>
			<td id="systemNameTd" colspan="2"><label class="systemName">ドキュメント管理システム</label></td>
		</tr>
<%
	for( int i = 0; i < categories.size(); i++ ) { //カテゴリの種類の数だけループ
		String numString =  (i + 1) + "";
		int select_id = INVALID_SELECT; // 不正値をいれておく
		String selectStr = (String)session.getAttribute("category" + (i + 1) + "_select_id"); // FN0004_0001.javaでセッションに登録したか
		if ( selectStr != null ) {
			select_id = Integer.parseInt( selectStr );
		}
%>
    	<tr>
    		<td class="inputLabel">カテゴリ<%= numString %></td>
    		<td>
        		<select id="category<%= numString %>" name="category_id<%= numString %>" style="width:700px;">
 <%
 		for( int j = 0; j < categories.get(i).size(); j++ ) {
 			CategoryInfo category = categories.get(i).get(j);
 			String selected = ""; // selectedオプションを入れるかどうか
 			if ( select_id != INVALID_SELECT && select_id == category.getCategory_id() ) {
 				selected = "selected";
 			}
%> 					
       				<option value="<%= category.getCategory_id() %>" <%= selected %>><%= category.getCategory_name() %></option>
<%
 		}
%>
       				</select>
    		</td>
    	</tr>
<%
 	}
%>
       	<tr>
       		<td colspan="2" align="right">
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
			<td width="50" align="center">選択</td>	<td width="850"  align="center">ファイル名</td>	
		</tr>
<% 
List<FileInfo> fileInfo = (List<FileInfo>)session.getAttribute("fileInfo");
String index = "";
if ( fileInfo != null) {

	for(int i = 0; i < fileInfo.size(); i++) {
		file_name = fileInfo.get(i).getFile_name();

%>
		<tr> 
			<td align="center"><input type="radio" name="select" value=<%= i + "" %> <%= i == 0 ? "checked" : "" %>/></td>
			<td><%= file_name %></td>
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
				<input type="hidden" id="gamenFlag" name = "gamenFlag" value="FN04_0001" />
			</td>
			<td>
				<input class="button" type="submit" name="btn_upd" value="変更" onClick="jumpList('update')" />
			</td>
			<td>
				<input class="button" type="submit" name="btn_del" value="削除" onClick="jumpList('delete')" />
			</td>
			<td>
				<input class="button" type="submit" name="btn_dow" value="ダウンロード" onClick="jumpList('download')" />
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