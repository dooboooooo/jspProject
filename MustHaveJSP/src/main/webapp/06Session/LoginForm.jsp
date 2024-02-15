<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head><title>LoginForm.jsp</title></head>
<body>
	<jsp:include page="../Common/Link.jsp" />
    <h2>로그인 페이지</h2>
    <span style="color: red; font-size: 1.2em;"> 
    <%= request.getAttribute("LoginErrMsg") == null ? "" : request.getAttribute("LoginErrMsg") %>
    </span>
    <%
    if (session.getAttribute("UserId") == null) {  // 로그인 상태 확인
        // 로그아웃 상태
    %>
    <script>
    function validateForm(form) {
        if (!form.user_id.value) { // 넘겨받은 form의 user_id의 값이 없으면 .. ? 느낌표 붙이면 없는거임 ?
            alert("아이디를 입력하세요."); // 경고창 출력
            return false;
        }
        if (form.user_pw.value == "") {
            alert("패스워드를 입력하세요.");
            return false;
        }
    }
    </script>
    <form action="LoginProcess.jsp" method="post" name="loginFrm" onsubmit="return validateForm(this);">
    			<!-- onsubmit="return validateForm(this) : onsubmit(submit이 on될 조건), 자신의 폼(this)를 가지고 validateForm에 들어가서 return 값을 받고 true여야 action 실행됨 -->
        아이디 : <input type="text" name="user_id" /><br />
        패스워드 : <input type="password" name="user_pw" /><br />
        <input type="submit" value="로그인하기" />
    </form>
    <%
    } else { // 로그인된 상태
    %>
        <%= session.getAttribute("UserName") %> 회원님, 로그인하셨습니다.<br />
        <a href="Logout.jsp">[로그아웃]</a>
    <%
    }
    %>
</body>
</html>