package model2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;

public class FileUtil { // 파일 업로드를 담당한다.(multipart / form-data 요청) 처리
	
	public static MultipartRequest uploadFile(HttpServletRequest req, String saveDirectory, int maxPostSize) {
		try {
			// 업로드 진행
			return new MultipartRequest(req, saveDirectory, maxPostSize, "UTF-8"); // request, 저장할 폴더, 파일 최대 크기, 인코딩 방식
		} catch (Exception e) {
			// 업로드 실패
			e.printStackTrace();
			return null;
		}
	} // uploadFile()
	
	// 명시한 파일을 찾아 다운로드 한다.
	public static void download(HttpServletRequest req, HttpServletResponse resp, String directory, String sfileName, String ofileName) {
		String sDirectory = req.getServletContext().getRealPath(directory); // 실제 파일이 저장된 경로
		
		try {
			// 파일을 찾아 입력 스트림 생성
			File file = new File(sDirectory, sfileName);
			InputStream iStream = new FileInputStream(file);
			
			// 한글 파일명 깨짐 방지
			String client = req.getHeader("User-Agent");
			if(client.indexOf("WOW64") == -1) {
				ofileName = new String(ofileName.getBytes("UTF-8"), "ISO-8859-1");
			} else {
				ofileName = new String(ofileName.getBytes("KSC5601"), "ISO-8859-1");
			}
			
			// 파일 다운로드용 응답 헤더 설정
			resp.reset();
			resp.setContentType("application/octet-stream");
			resp.setHeader("Content-Disposition", "attachment; filename=\"" + ofileName + "\"");
			resp.setHeader("Content-Length", "" + file.length());
			
			// out.clear(); // 출력 스트림 초기화(jsp에서는 예외가 발생하지만 서블릿은 발생하지 않아 주석처리)
			
			// response 내장 객체로부터 새로운 출력 스트림 생성
			OutputStream oStream = resp.getOutputStream();
			
			// 출력 스트림에 파일 내용 출력
			byte b[] = new byte[(int)file.length()];
			int readBuffer = 0;
			while((readBuffer = iStream.read(b)) > 0) {
				oStream.write(b, 0, readBuffer);
			}
			
			// 입출력 스트림 닫음
			iStream.close();
			oStream.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("파일을 찾을 수 없습니다.");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("예외가 발생하였습니다.");
			e.printStackTrace();
		}
	} // download()
	
	// 파일 첨부형 게시판이므로 글 삭제시 첨부한 파일도 같이 삭제되게 한다. = 파일 삭제 메서드
	public static void deleteFile(HttpServletRequest req, String directory, String filename) {
		String sDirectory = req.getServletContext().getRealPath(directory); // 파일이 저장된 디렉토리의 물리적인 경로를 얻어와서
		File file = new File(sDirectory + File.separator + filename); // 해당 경로의 파일 객체 생성
		if(file.exists()) { // 해당 경로의 파일이 존재하면
			file.delete(); // 파일 삭제
		}
	} // deleteFile()
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
