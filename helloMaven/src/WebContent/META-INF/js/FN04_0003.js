
function checkLogin(){
	var result = true;
	result = checkUserId();
	result = checkPassword();
	return result;
}
	
function checkUserId(){
	var user_id = document.getElementById('id_txt_user_id').value;
	if(user_id == ""){
//		alert('ユーザーIDを入力してください');
		return false;
	}
	return true;
}
	
function checkPassword(){
	var password = document.getElementById('id_txt_password').value;
	if(password == ""){
//		alert('パスワードを入力してください');
		return false;
	}
	return true;
}



