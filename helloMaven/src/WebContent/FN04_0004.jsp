<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.util.List"%>
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
<script type="text/javascript" src="./js/FN04_0002.js"></script>

<link rel=stylesheet type="text/css" href="${pageContext.request.contextPath}/css/com.css" />
</head>
<%
request.setCharacterEncoding("Shift_JIS");

String msg = (String)request.getAttribute("msg"); //　sessionではなくrequestにしてある。Util確認
if(msg==null){
	msg="";
}

List<CategoryInfo> category1 = (List<CategoryInfo>)session.getAttribute("category1");
List<CategoryInfo> category2 = (List<CategoryInfo>)session.getAttribute("category2");
List<CategoryInfo> category3 = (List<CategoryInfo>)session.getAttribute("category3");

int index = INVALID_SELECT; // バグがあれば検知できるように不正な値を入れておく
String select = (String)session.getAttribute("session_select");
if ( select != null ) {
	index = Integer.parseInt(select);
}
List<FileInfo> fileInfo = (List<FileInfo>)session.getAttribute("fileInfo");
FileInfo info = null;
if ( fileInfo != null ) {
	info = fileInfo.get(index);
	//fileName = info.getFile_name();
}

String fileName = (String)request.getAttribute("fileName"); // 画面更新時にはデータが取得できる
if (fileName == null) { // FN04_0001から遷移してきたときはnullだろう
	fileName = info.getFile_name(); 
}
%>


<body class="body">
<!--
enctype="multipart/form-data"の指定をしないほうが.javaで扱いやすいはずだが、追加画面・変更画面と合わせる 
-->
<FORM method="POST" enctype="multipart/form-data" >

	<table> 
		<tr>
			<td id="systemNameTd" colspan="2"><label class="systemName">ドキュメント管理システム</label></td>
		</tr>

    	<tr>
    		<td class="inputLabel">カテゴリ1</td>
    		<td>
        		<select id="category1" name="category_id1" style="width:700px;">
       				<option value="<%= info.getCatetory_1() %>"><%= category1.get(info.getCatetory_1()).getCategory_name() %></option>
       				</select>
    		</td>
    	</tr>
    	<tr>
    		<td class="inputLabel">カテゴリ2</td>
    		<td>
        		<select id="category2" name="category_id2" style="width:700px;">
       				<option value="<%= info.getCatetory_2() %>"><%= category2.get(info.getCatetory_2()).getCategory_name() %></option>
       				</select>
    		</td>
    	</tr>
     	<tr>
    		<td class="inputLabel">カテゴリ3</td>
    		<td>
        		<select id="category3" name="category_id3" style="width:700px;">
       				<option value="<%= info.getCatetory_3() %>"><%= category3.get(info.getCatetory_3()).getCategory_name() %></option>
       				</select>
    		</td>
    	</tr>

       	<tr>
       		<td class="inputLabel">ファイル名</td>
       		<td width="700px">
       			<input class="inputAdd" type="text" id="id_txt_file_name" name="txt_file_name" maxlength="200" value="<%= fileName %>" readonly="readonly" />
       		</td>
       	</tr>
 
	</table>
	<br><br>

	
	<table>
		<tr>
			<td id="btn_login_leftTd"></td>
			<td>
				<input class="button" type="submit" name="btn_submit" value="削除" formaction="./FN04_0004" />

			</td>
		</tr>
		<tr>
			<td id="btn_login_rightTd"></td>
			<td>
				<input class="button" type="submit" name="btn_back" value="戻る" formaction="./FN04_0001" />				
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