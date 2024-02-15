<%@page import="commom.Person"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ApplicationMain.jsp</title>
</head>
<body>
<!-- was(톰켓)이 가지고 있는 메모리영역인 application 싱글톤 객체의 값을 저장과 출력 테스트 -->
	<h2>application 영역의 공유</h2>
	<%
	Map<String, Person> maps = new HashMap<>(); // key=<String> // val=<Person>
	maps.put("actor1", new Person("김성은", 25));
	maps.put("actor2", new Person("가가가", 30));
	application.setAttribute("maps", maps);
	%>
	application 영역에 속성이 저장되었습니다.
</body>
</html>