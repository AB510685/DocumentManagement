<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.util.List"%>
<%@ page import="pack.bean.MenuInfo"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<title>ドキュメント管理システム</title>
<script type="text/javascript" src="./js/FN02_0001.js"></script>
<link rel=stylesheet type="text/css" href="${pageContext.request.contextPath}/css/com.css" />

</head>
<body class="body">
	<table> 
		<tr>
			<td id="systemNameTd" colspan="2"><label class="systemName">ドキュメント管理システム</label></td>
		</tr>
    	<tr>
			<td>
				<table>
					<tr>
						<td>
						</td>
						<td>
						</td>
					</tr>

<%
request.setCharacterEncoding("Shift_JIS");
String user_id = (String)session.getAttribute("user_id");
if(user_id == null){
	user_id="";
}
String user_name = (String)session.getAttribute("user_name");
if(user_name == null){
	user_name="";
}

List<MenuInfo>info = (List<MenuInfo>)session.getAttribute("MenuData");
for(int i=0;i<info.size();i++){
	String jsp = "";
	String jump = "";
	String screen_id = info.get(i).getScreen_id();
	if( screen_id == null) {
		screen_id = "";
	}
	switch( screen_id ) {
	case "user_disp":
		jsp = "FN03_0001.jsp";
		jump = "FN03_0001";
		break;
	case "document_disp":
		jsp = "FN04_0001.jsp";
		jump = "FN04_0001";
		break;			
	default:
		System.err.println("screen_id:" + screen_id + "is inValid");
		break;
	}
%>
<!-- 
					<FORM method="POST" action="./<%= jsp %>">
-->
					<FORM method="POST" action="./<%= jump %>">
					<tr>
						<td colspan="2" height="10px">
						</td>
					</tr>
					<tr>
						<td width="120px">
						</td>
						<td>

							<button class="button" type="submit" name="btn_menu" id="<%= info.get(i).getScreen_id() %>" value="<%= info.get(i).getScreen_id() %>" ><%= info.get(i).getDisp_name() %></button>
							
						</td>
					</tr>
					</FORM>
<%
}
%>
					<FORM method="POST" action="./FN01_0001.jsp">

					<tr>
						<td colspan="2" height="10px">
						</td>
					</tr>
					<tr>
						<td width="120px">
						</td>
						<td>

							<button class="button" type="submit" name="btn_menu" id="back" value="back">戻る</button>

						</td>
					</tr>
					</FORM>
				</table>
			</td>
    	</tr>
	</table>

<input type="hidden" id="user_id" value="<%= user_id %>" />
<input type="hidden" id="user_name" value="<%= user_name %>" />

</body>
</html>
