<%@page import="utils.JSFunction"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
if(session.getAttribute("UserId") == null){ // 세션 영역에 UserId가 없으면(로그인 안함)
	JSFunction.alertLocation("로그인 후 이용해주십시오", "../06Session/LoginForm.jsp", out); // 메시지, 이동할 url, JspWriter(script 코드에 삽입) out(내장객체)
	return;
}
%>
