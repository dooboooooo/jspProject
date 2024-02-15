<%@page import="utils.JSFunction"%>
<%@page import="model1.BoardDAO"%>
<%@page import="model1.BoardDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
// 폼 값 받기
String title = request.getParameter("title");			// post,get 방식으로 전달된 값들은 getParameter(매개값)로 받는다.
String content = request.getParameter("content");

// 폼 값을 DTO 객체에 저장
BoardDTO dto = new BoardDTO();                          // dto 객체 생성(지금은 값 없음)
dto.setTitle(title);									// dto 객체에 제목 값 입력
dto.setContent(content);								// dto 객체에 내용 값 입력
dto.setId(session.getAttribute("UserId").toString());	// 세션(Object타입)에 있는 id 입력(로그인 된 상태)

// DAO 객체를 통해 DB에 DTO 저장
BoardDAO dao = new BoardDAO(application);               // jdbc 연결 객체 생성
int iResult = dao.insertWrite(dto);						// insertWrite 메서드에 위에서 저장한 dto 값 전달하여 결과를 int로 받음(만든 메서드)

/* int iResult = 0;										// 페이징용 더미데이터 입력
for(int i = 1; i <= 100; i++){
	dto.setTitle(title +"-" + i); 						// 제목 글자에 -1 ~ -100까지 붙는다
	iResult = dao.insertWrite(dto); 					// insert 쿼리 실행
} */
dao.close();

// 성공 or 실패
if(iResult == 1){
	response.sendRedirect("List.jsp");					// 정상적으로 저장 성공 시 리스트 페이지로 이동(내장 메서드)
} else {
	JSFunction.alertBack("글쓰기에 실패하였습니다.", out);		// 실패 시 경고창과 메시지 띄운 후 뒤로 이동(만든 메서드)
}
%>