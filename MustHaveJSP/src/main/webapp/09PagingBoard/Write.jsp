<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="./IsLoggedIn.jsp" %> <!-- 로그인 확인에 대한 코드를 삽입(jsp:include 사용 안함 = 결과값만 들어오기 때문) (주의사항 html5태그 중복제거) -->
<!DOCTYPE html><html><head><meta charset="UTF-8">
	<script type="text/javascript">
		function validateForm(form){ // validateForm을 호출하면 form에 있는 값을 매개값으로 받는다.
			if(form.title.value == ""){ // 폼의 title에 value가 ""(없으면)
				alert("제목을 입력하세요."); // 경고창 출력
				form.title.focus(); // form의 title에 커서
				return false; // false를 return
			}
			if(form.content.value == ""){ // 폼의 content에 value가 ""(없으면)
				alert("내용을 입력하세요.")
				form.content.focus();
				return false;
			}
		}
	</script>
<title>Write.jsp</title></head>
<body>
	<jsp:include page="../Common/Link.jsp" /> <!-- 메뉴 출력(결과값만 포함하는 액션태그) -->
		<h2>회원제 게시판 - 글쓰기(Write)</h2>
		<form name="writeFrm" method="post" action="WriteProcess.jsp" onsubmit="return validateForm(this);">
			<table border="1" width="90%">
				<tr>
					<td>제목</td>
					<td><input type="text" name="title" style="width: 90%;" /></td>
				</tr>
				<tr>
					<td>내용</td>
					<td><textarea name="content" style="width: 90%; height: 100px;"></textarea></td> <!-- textarea : br태그 같은거 없이 입력받은데로 출력되는 박스 -->
				</tr>
				<tr>
					<td clospan="2" align="center">
						<button type="submit">작성 완료</button>
						<button type="reset">다시 입력</button>
						<button type="button" onclick="location.href='List.jsp';">목록 보기</button>
					</td>
				</tr>
			</table>
		</form>
</body>
</html>