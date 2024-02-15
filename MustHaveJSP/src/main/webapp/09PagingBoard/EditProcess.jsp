<!-- 전달받은 DTO를 DAO에서 업데이트 하도록 처리한다. -->
<%@page import="model1.BoardDAO"%>
<%@page import="model1.BoardDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="./IsLoggedIn.jsp"%>
<%
// 수정 내용 얻기
String num = request.getParameter("num"); 
String title = request.getParameter("title");
String content = request.getParameter("content"); // Edit.jsp에서 넘어온 파라미터 값 처리

// DTO에 저장
BoardDTO dto = new BoardDTO();
dto.setNum(num);
dto.setTitle(title);
dto.setContent(content); 

// DB에 반영
BoardDAO dao = new BoardDAO(application); 
int affected = dao.updateEdit(dto);
dao.close();

// 성공/실패 처리
if (affected == 1) { 
    // 성공 시 상세 보기 페이지로 이동
    response.sendRedirect("View.jsp?num=" + dto.getNum()); 
} 
else {
    // 실패 시 이전 페이지로 이동
    JSFunction.alertBack("수정하기에 실패하였습니다.", out);
}
%>

