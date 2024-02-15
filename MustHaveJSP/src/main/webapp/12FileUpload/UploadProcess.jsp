<%@page import="fileupload.MyfileDAO"%>
<%@page import="fileupload.MyfileDTO"%>
<%@page import="java.io.File"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.oreilly.servlet.MultipartRequest"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String saveDirectory = application.getRealPath("/Uploads"); // 저장할 디렉터리 // 폴더 만들었음
int maxPostSize = 1024 * 1024; // 파일 최대 크기 1MB로 제한(바이트 단위, 1MB = 1024 * 1KB)
String encoding = "UTF-8"; // 인코딩 방식

try {
// 1. MultipartRequest 객체 생성
MultipartRequest mr = new MultipartRequest(request, saveDirectory, maxPostSize, encoding);

// 2. 새로운 파일명 생성
String fileName = mr.getFilesystemName("attachedFile"); // attachedFile = 첨부파일명(원래 파일명)
String ext = fileName.substring(fileName.lastIndexOf(".")); // 파일 확장자 // 뒤에서 부터 .을 찾아서 .부터 끝까지 잘라서 가져온다
String now = new SimpleDateFormat("yyyyMMdd_HmsS").format(new Date());
String newFileName = now + ext; // 새로운 파일 이름("년년년년월월일일_시분초초.확장자")

// 3. 파일명 변경
File oldFile = new File(saveDirectory + File.separator + fileName); // java.io.File // File.separator = 분리자(/) c://Uploads/김성은.hwp
File newFile = new File(saveDirectory + File.separator + newFileName);
oldFile.renameTo(newFile); // 파일명 변경완료

// 4. 폼 값 받기
String name = mr.getParameter("name"); // post로 넘어온 name 값을 name 변수에 넣는다.
String title = mr.getParameter("title");
String[] cateArray = mr.getParameterValues("cate"); // 선택을 안할수도 있고, 여러 값을 선택할 수도 있는 cate
StringBuffer cateBuf = new StringBuffer(); // 글자 배열을 하나의 String으로 만들 때(,로 분리)
if(cateArray == null){
	cateBuf.append("선택 없음");
} else {
	for(String s : cateArray){
		cateBuf.append(s + ", "); // 영화, 음악, 문서, 
	}
}

// 5. DTO 생성
MyfileDTO dto = new MyfileDTO();
dto.setName(name);
dto.setTitle(title);
dto.setCate(cateBuf.toString());
dto.setOfile(fileName);
dto.setSfile(newFileName); // 폼으로 입력받는 내용을 변환하여 객체 생성

// 6. DAO를 통해 데이터베이스에 반영
MyfileDAO dao = new MyfileDAO(); // 커넥션 풀로 jdbc 연결
dao.insertFile(dto); // insertFile() 메서드에 dto 객체를 보내 insert 쿼리 실행
dao.close(); // 커넥션 풀 닫기

// 7. 파일 목록 JSP로 리디렉션
response.sendRedirect("FileList.jsp");

} catch(Exception e) {
	e.printStackTrace();
	request.setAttribute("errorMessage", "파일 업로드 오류");
	request.getRequestDispatcher("FileUploadMain.jsp").forward(request, response);
}
%>