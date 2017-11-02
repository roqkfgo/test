<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style>
.btn {
    border: none;
    color: white;
    padding: 7px 14px;
    font-size: 16px;
    cursor: pointer;
  	/* width : 130px; */  /* 일정한 크기의 버튼 만들려면 너비를 고정해준다 */
}

.login {background-color: #4CAF50;} /* Green */
.login:hover {background-color: #46a04f;}

.notice {background-color: #2196F3;} /* Blue */
.notice:hover {background: #0b7ddf;}

.ajax {background-color: orange;} /* Blue */
.ajax:hover {background: olive;}

</style>
</head>
<body>
	<a href="joinus/login.do" ><button class="btn login">로그인</button></a>
	<a href="customer/notice.do" ><button class="btn notice">게시판</button></a>
	<a href="joinus/ex02_jspAjax03.html" ><button class="btn ajax">ajax1</button></a>
	
</body>
</html>