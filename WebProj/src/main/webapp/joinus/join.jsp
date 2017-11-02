<!-- 
	### ajax 로 아이디 사용여부 체크하기
	1) join.jsp 파일에서 <input id="btnCheckUid" > 버튼 만들기 , 입력 input태그에 id속성 주기
	2) 버튼 클릭되었을떄 ajax코드 구현 ==> idcheck.do 로 서버에 요청
	3) IdcheckController 구현 , idcheck.jsp 구현
	4) uriMap.txt 에 맵핑 정보 입력
	5) 아이디 4글자 이상일때만 ajax 가동하게 구현

 -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
		<title>index</title>
		<link href="join.css" type="text/css" rel="stylesheet" />
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
		<script type="text/javascript">
			// 아이디체크 ajax
			var idOk =0;
			$( document ).ready(function(){	
			    $('#btnCheckUid').click(function(){
			    	if($("#mid").val().length >= 4){
			    		$.ajax({
				            url:"idcheck.do",
				            type:"POST",
				            data:{"mid": $('#mid').val()},
				            dataType:'text',
				            error:function(jqXHR){
								alert(jqXHR.status);
								alert(jqXHR.statusText);
					        },
				            success:function(data){
				                if($.trim(data) == "yes"){
				                	idOk=1;
				                    alert("사용가능한 아이디입니다.");
				                }else{
				                	idOk=0;
				                    alert("이미 존재하는 아이디입니다.");
				                }
				            }
				        });  
			    	}else{
			    		alert("아이디는 4글자 이상이어야 합니다.");
			    	}
			          
			    });
			    
			});  
			
			/* 비밀번호 체크 및 생일 날자 만들기 ==> 각각의 DO 들 id 속성을 주어야 함.*/
			function validate() {
				if(idOk!=1){
					alert("아이디 중복확인을 해주세요.!"); 
					return false;
				}
				
				if($('#pwd').val() == "" && $('#pwd2').val() == "") {
			        alert("비밀번호는 필수 입력 요소 입니다!"); 
			        return false;
				}else if($('#pwd').val() != $('#pwd2').val()){
					alert("비밀 번호가 일치 하지 않습니다. 비밀번호를 확인해주세요!"); 
			        return false;
				}else{
					var bday = $('#year').val() + "-" + $('#month').val() + "-" + $('#day').val();
					$('#birthday').val(bday);
				}
				
				
			} 
			
		</script>
	</head>
	<body>
		<div id="main">
			<div class="top-wrapper clear">
				<div id="content">
					<form action="join.do" method="post" onsubmit="return validate()">
						<h2>회원가입</h2>
						<h3 class="hidden">방문페이지 로그</h3>
						<p id="breadscrumb" class="block_hlist clear"><img alt="Step1 개인정보 등록" src="images/step2.png" /></p>
						<h3 class="hidden">회원가입 폼</h3>
						
						<div id="join-form" class="join-form margin-large" >
							
							<dl class="join-form-row">
								<dt class="join-form-title">
									*아이디
								</dt>
								<dd class="join-form-data">
									<input type="text" id="mid" name="mid" /><span> 4~12자리 영문자(단 영어로 시작)</span>
									<input id="btnCheckUid" class="button" type="button" value="중복확인" />   <!-- 이버튼추가 : 위에 input에도 속성 id="mid" 추가할것 -->
								</dd>
							</dl>
							<dl class="join-form-row">
								<dt class="join-form-title">
									*비밀번호
								</dt>
								<dd class="join-form-data">
									<input type="password" name="pwd" id="pwd"  />
								</dd>
							</dl>
							<dl class="join-form-row">
								<dt class="join-form-title">
									*비밀번호 확인
								</dt>
								<dd class="join-form-data" >
									<input type="password" name="pwd2" id="pwd2"/>
								</dd>
							</dl>
							<dl class="join-form-row">
								<dt class="join-form-title">
									*이름
								</dt>
								<dd class="join-form-data">
									<input type="text" name="name"  />
								</dd>
							</dl>
							<dl class="join-form-row">
								<dt class="join-form-title">
									*성별
								</dt>
								<dd class="join-form-data">
									<select id="gender" name="gender">
										<option value="male">남성</option>
										<option value="female" selected="selected">여성</option>
									</select>
									<!-- <input type="radio" name="gender" id="male" value="남자" checked="checked" />남자
	                                <input type="radio" name="gender" id="female"  value="여자" />여자 -->
								</dd>
							</dl>
							<dl class="join-form-row birthday">
								<dt class="join-form-title">
									*생년월일
								</dt>
								<dd class="join-form-data">								
	                                <span>
	                                    <input type="text" name="year" id="year"  />년
	                                    <input type="text" name="month" id="month"   />월
	                                    <input type="text" name="day" id="day"  />일
	                                    <input type="hidden" name="birthday" id="birthday"  />
	                                </span>
	                                <span class="moon">
										<input type="radio" name="isLunar" value="Solar" id="IsSolar" checked="checked" />양력
										<input type="radio" name="isLunar" value="Lunar" id="IsLunar" />음력
									</span>
								</dd>
							</dl>
							<dl class="join-form-row">
								<dt class="join-form-title">
									*나이
								</dt>
								<dd class="join-form-data">
									<input type="text" name="age"  />
								</dd>
							</dl>
							<dl class="join-form-row">
								<dt class="join-form-title">
									*핸드폰 번호
								</dt>
								<dd class="join-form-data">
									<input type="text" name="phone"  /><span> 대시(-)를 포함할 것: 예) 010-3456-2934 </span>
								</dd>
							</dl>
							<dl class="join-form-row">
								<dt class="join-form-title">
									취미
								</dt>
								<dd class="join-form-data habit">
									<input type="checkbox" name="habit" id="music" value="music" /><label for="music">음악</label>
									<input type="checkbox" name="habit" id="movie" value="movie" /><label for="movie">영화</label>
									<input type="checkbox" name="habit" id="trip"  value="trip" /><label for="trip">여행</label>
									<input type="checkbox" name="habit" id="art"  value="art" /><label for="art">미술</label>
									<input type="checkbox" name="habit" id="sports" value="sports" /><label for="sports">스포츠</label>
								</dd>
							</dl>						
						</div>
						
					<div id="buttonLine">
						<input class="btn-okay button" type="submit" value="가입" />
					</div>
					</form>
				</div>
				
			</div>
		</div>
		
	</body>
</html>
