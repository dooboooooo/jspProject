<%@page import="membership.MemberDTO"%>
<%@page import="membership.MemberDAO"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
// 로그인 폼으로부터 받은 아이디와 패스워드
String userId = request.getParameter("user_id"); 
String userPwd = request.getParameter("user_pw"); // 왜 request? = 아마 .. 로그인 폼으로부터 받은 정보들은 로그인, 로그인 실패 이후까지 가지고 갈 필요가 없어서 .. ??

// web.xml에서 가져온 데이터베이스 연결 정보
String oracleDriver = application.getInitParameter("OracleDriver");
String oracleURL = application.getInitParameter("OracleURL");
String oracleId = application.getInitParameter("OracleId");
String oraclePwd = application.getInitParameter("OraclePwd");

// 회원 테이블 DAO를 통해 회원 정보 DTO 획득
MemberDAO dao = new MemberDAO(oracleDriver, oracleURL, oracleId, oraclePwd); // db 연결한 dao 객체 생성
MemberDTO dto = dao.getMemberDTO(userId, userPwd); // dao 객체 안의 getMemberDTO 메서드 안으로 로그인폼으로부터 받은 id와 pw를 가지고 들어가서 해당 id, pw가 동일한 회원이 있는지 찾아서 반환(dto)
dao.close();

// 로그인 성공 여부에 따른 처리
if (dto.getId() != null) {
    // 로그인 성공
    session.setAttribute("UserId", dto.getId()); 
    session.setAttribute("UserName", dto.getName()); 
    response.sendRedirect("LoginForm.jsp");
}
else {
    // 로그인 실패
    request.setAttribute("LoginErrMsg", "로그인 오류입니다."); 
    request.getRequestDispatcher("LoginForm.jsp").forward(request, response);
}
%>