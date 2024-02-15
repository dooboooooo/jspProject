package utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieManager { // 로그인 // 쿠키를 관리하는 클래스
	// 쿠키 생성, 찾고, 삭제하는 공통의 코드
	
	// 명시한 이름, 값, 유지기간 조건으로 새로운 쿠키를 생성한다.
	public static void makeCookie(HttpServletResponse response, String cName, String cValue, int cTime) {
									// 보낼때 필요한 응답객체, 쿠키이름, 쿠키값, 쿠키유지기간
		Cookie cookie = new Cookie(cName, cValue); // 쿠키 생성(매개값으로 받은 쿠키이름, 쿠키값으로)
		cookie.setPath("/"); // 경로 설정
		cookie.setMaxAge(cTime); // 유지기간 설정
		response.addCookie(cookie); // 응답 객체에 추가
	} // makeCookie()
	
	public static String readCookie(HttpServletRequest request, String cName) {
									// 가져올때 필요한 요청객체, 찾을쿠키이름
		String cookieValue = ""; // 반환할 변수 생성
		
		Cookie[] cookies = request.getCookies(); // 요청한 곳의 모든 쿠키를 가져온다
		if(cookies != null) { // 쿠키가 있으면
			for(Cookie c : cookies) { // 각 쿠키들의
				String cookieName = c.getName(); // 이름을 가져온다
				if(cookieName.equals(cName)) { // 쿠키의 이름들 중 찾을쿠키이름과 같은게 있으면
					cookieValue = c.getValue(); // 반환변수에 해당 쿠키의 값을 넣는다.
				}
			}
		}
		return cookieValue; // 찾을쿠키이름의 값을 반환한다.
	} // readCookie()
	
	public static void deleteCookie(HttpServletResponse response, String cName) {
		makeCookie(response, cName, "", 0); // 위에서 만든 makeCookie를 활용하여 MaxAge를 0으로 만든다 = 삭제&수정
	} // deleteCookie()
} // class
