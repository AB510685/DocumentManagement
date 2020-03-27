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



// 本来は下のinfoに入っているはずの情報だが、これだけ扱いにくい
//本来は下のinfoに入っているはずの情報だが、これだけ扱いにくい
int[] id = new int[CATEGORY_NUM];
String[] idStr = new String[CATEGORY_NUM] ;
for(int i = 0; i < idStr.length; i++) {
	idStr[i] = (String)session.getAttribute("category" + ( i + 1 ) + "_select_id" );
	System.out.println("idStr[" + i + "]:" + idStr[i] );
	if( idStr[i] != null ) {
		id[i] = Integer.parseInt(idStr[i]);
	} else {
		idStr[i] = "";
		id[i] = 0;
	}
}

List<List<CategoryInfo>> categories = (List<List<CategoryInfo>>)session.getAttribute("categories");

int index = INVALID_SELECT; // バグがあれば検知できるように不正な値を入れておく
String select = (String)session.getAttribute("session_select");
if ( select != null ) {
	index = Integer.parseInt(select);
}
List<FileInfo> fileInfo = (List<FileInfo>)session.getAttribute("fileInfo");
FileInfo info = null;
String fileName = "";
if ( fileInfo != null ) {
	info = fileInfo.get(index);
	fileName = info.getFile_name();
}

%>


<body class="body">

<FORM method="POST" enctype="multipart/form-data" >

	<table> 
		<tr>
			<td id="systemNameTd" colspan="2"><label class="systemName">ドキュメント管理システム</label></td>
		</tr>
<%
	for( int i = 0; i < categories.size(); i++ ) { //カテゴリの種類の数だけループ
		String numString =  (i + 1) + "";		
%>
    	<tr>
    		<td class="inputLabel">カテゴリ<%= numString %></td>
    		<td>
        		<select id="category<%= numString %>" name="category_id<%= numString %>" style="width:700px;">
 <%
 		for( CategoryInfo category : categories.get(i) ) {
 			String selected = "";
 			if ( id[i] == category.getCategory_id() ) {
 				selected = "selected";
 			}
%> 					
       				<option <%= selected %> value="<%= category.getCategory_id() %>"><%= category.getCategory_name() %></option>
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
       		<td class="inputLabel">ファイル名</td>
       		<td width="700px">
       			<input class="inputAdd" type="text" id="id_txt_file_name" name="txt_file_name" maxlength="200" value="<%= fileName %>" />
       		</td>
       	</tr>
 
        	<tr>
       		<td></td>
       		<td width="700px">
       			<input type="file" name="bin_file_contents" value="参照" />
       		</td>
       	</tr>
	</table>
	<br><br>

	
	<table>
		<tr>
			<td id="btn_login_leftTd"></td>
			<td>
				<input class="button" type="submit" name="btn_submit" value="変更" formaction="./FN04_0003" />

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