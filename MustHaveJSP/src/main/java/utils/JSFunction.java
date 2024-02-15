package utils;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;

public class JSFunction { // 로그인
	// jsp 파일에서 alert 창을 띄우려면 <% 스크립틀릿 %> 이후에 작성을 해야한다.
	// java 코드가 이어지는 부분에서는 코드가 복잡해진다. -> 따로 뺀다
	
	// 메세지 알림창을 띄운 후 명시한 URL로 이동한다. JspWriter의 내장객체out= html 코드를 실행하게함
	public static void alertLocation(String msg, String url, JspWriter out) {
		try {
			String script = "" + "<script> alert('" + msg + "'); location.href='" + url + "'; </script>";
			// 경고창으로 메세지를 띄우고 location.href에서 지정한 url로 이동
			out.println(script); // 자바스크립트 코드를 out 내장 객체로 출력(삽입) // jsp 코드로 출력된다 = 삽입과 같은효과
		} catch (Exception e) {}
	} // alertLocation()
	
	public static void alertBack(String msg, JspWriter out) { // 실패했을 때 경고창을 띄우고 무조건 뒤로 간다(url이 필요없음)
		try {
			String script = "" + "<script> alert('" + msg + "'); history.back(); </script>";
			// 경고창으로 메세지를 띄우고 history.back()메서드로 이전 경로로 이동
			out.println(script); // jsp 코드로 삽입
		} catch (Exception e) {}
	} // alertBack()
	
	
	
	// 서블릿을 통한 alert 메시지 전달을 위한 메서드
	public static void alertLocation(HttpServletResponse resp, String msg, String url) {
		try {
			resp.setContentType("text/html;charset=UTF-8"); // utf-8로 사용할 것이라는 의미
			PrintWriter writer = resp.getWriter();
			String script = "" + "<script> alert('" + msg + "'); location.href='" + url + "'; </script>";
											// msg를 담은 경고창을 띄우고 url로 이동한다.
			writer.print(script); // 스크립트 코드를 삽입한다.
		} catch (Exception e) {}
	} // alertLocation()
	
	public static void alertBack(HttpServletResponse resp, String msg) {
		try {
			resp.setContentType("text/html;charset=UTF-8"); // utf-8로 사용할 것이라는 의미
			PrintWriter writer = resp.getWriter();
			String script = "" + "<script> alert('" + msg + "'); history.back(); </script>";
											// msg를 담은 경고창을 띄우고 뒤로 이동한다.
			writer.print(script); // 스크립트 코드를 삽입한다.
		} catch (Exception e) {}
	} // alertBack()
	
	
	
	
} // class
