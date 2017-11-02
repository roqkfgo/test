<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>index</title>
<link href="join.css" type="text/css" rel="stylesheet" />
<title>Insert title here</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script type="text/javascript">
	/*  회원 정보 자바스크립트로 화면에 표시  */
	$(document).ready(function(){
		/* 생일 정보 화면에 표시 */
		var birth = "${m.birthday}";
		alert(birth);
		var births = birth.split("-");
		$("#year").val(births[0]);
		$("#month").val(births[1]);
		$("#day").val(births[2]);
		
		/* 취미 정보 화면에 표시 */
		var habit = "${m.habit}"; 
		alert(habit);
		var habits = habit.split(",");
		for (var i = 0; i < habits.length; i++) {
			if(habits[i] == "music"){
				$("#music").attr("checked", true);
			}else if(habits[i] == "movie"){
				$("#movie").attr("checked", true);
			}else if(habits[i] == "trip"){
				$("#trip").attr("checked", true);
			}else if(habits[i] == "art"){
				$("#art").attr("checked", true);
			}else if(habits[i] == "sports"){
				$("#sports").attr("checked", true);
			}
		}
		
	});



	function validate() {
		alert('validate()');
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
					<form action="memberUpdate.do" method="post" onsubmit="return validate()">
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
									${m.mid}
									<input type="hidden" name="mid" value="${m.mid}" />
								</dd>
							</dl>
							<dl class="join-form-row">
								<dt class="join-form-title">
									*비밀번호
								</dt>
								<dd class="join-form-data">
									<input type="password" name="pwd" id="pwd" value="" required />
								</dd>
							</dl>
							<dl class="join-form-row">
								<dt class="join-form-title">
									*비밀번호 확인
								</dt>
								<dd class="join-form-data" >
									<input type="password" name="pwd2" id="pwd2" required value="" />
								</dd>
							</dl>
							<dl class="join-form-row">
								<dt class="join-form-title">
									*이름
								</dt>
								<dd class="join-form-data">
									<%-- ${m.name} --%>
									<input type="text" name="name"  />
								</dd>
							</dl>
							<dl class="join-form-row">
								<dt class="join-form-title">
									*성별
								</dt>
								<dd class="join-form-data">
									<%-- ${m.gender} --%>
									<input type="radio" name="gender" value="male">남성
									<input type="radio" name="gender" value="female">여성
									<%-- <input type="hidden" name="gender" value="${m.gender}" /> --%>
								</dd>
							</dl>
							<dl class="join-form-row birthday">
								<dt class="join-form-title">
									*생년월일
								</dt>
								<dd class="join-form-data">								
	                                <span>
	                                    <input type="text" name="year" id="year" required value="" pattern="[12][09][0-9]{2}" />년
	                                    <input type="text" name="month" id="month" required value="" pattern="[01][0-9]"  />월
	                                    <input type="text" name="day" id="day" required value="" pattern="[0123][0-9]" />일
	                                    <input type="hidden" name="birthday" id="birthday"  />
	                                </span>
								</dd>
							</dl>
							<dl class="join-form-row">
								<dt class="join-form-title">
									*나이
								</dt>
								<dd class="join-form-data">
									<input type="text" name="age" required value="${m.age}" pattern="[0-9]{1,3}" />
								</dd>
							</dl>
							<dl class="join-form-row">
								<dt class="join-form-title">
									*핸드폰 번호
								</dt>
								<dd class="join-form-data">
									<input type="text" name="phone" value="${m.phone}" required  pattern="01[016-9]-\d{3,4}-\d{4}" /><span>[대시(-)를 포함할 것: 예) 010-3456-2934]</span>
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
						<a class="btn-cancel button" href="welcomelogin.do">취소</a>	
					</div>
					</form>
				</div>
				
			</div>
		</div>
		
	</body>
</html>