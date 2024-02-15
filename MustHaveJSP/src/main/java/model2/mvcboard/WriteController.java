package model2.mvcboard;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;

import model2.FileUtil;
import model2.MVCBoardDAO;
import model2.MVCBoardDTO;
import utils.JSFunction;

public class WriteController extends HttpServlet {
	
	@Override // /mvcboard/write.kse를 요청받으면 아래 경로로 바로 이동시킨다.
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/14MVCBoard/Write.jsp").forward(req, resp);
	} // doGet()
	
	@Override // Write.jsp의 작성완료 버튼을 누르면 post 방식으로 전송된다.(파일 업로드 처리를 먼저하고 폼데이터를 처리한다.)
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 1. 파일 업로드 처리
		// 업로드 디렉터리의 물리적 경로 확인
		String saveDirectory = req.getServletContext().getRealPath("/Uploads"); // getServletContext() : 파일을 실행할 때 저장하는 가상의 경로 + /Uploads
							// CATALINA_BASE: D:\workspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp0 + /Uploads
		
		// 초기화 매개변수로 설정한 첨부파일 최대용량 확인(web.xml)
		ServletContext application = getServletContext(); // 어플리케이션 영역의
		int maxPostSize = Integer.parseInt(application.getInitParameter("maxPostSize"));
		
		// 파일 업로드
		MultipartRequest mr = FileUtil.uploadFile(req, saveDirectory, maxPostSize); // 파일 업로드가 성공하면 MultipartRequest 객체가 생성되어 mr에 저장된다.
		if(mr == null) { // 파일 업로드 실패했을 때
			JSFunction.alertLocation(resp, "첨부파일이 제한 용량을 초과합니다.", "../mvcboard/write.kse"); // model2.mvcboard.WriteController로 이동해서 아마 doget 메서드가 실행될것같음 .. 
			return;
		}
		
		// 2. 파일 업로드 외 처리
		// 폼값을 dto에 저장
		MVCBoardDTO dto = new MVCBoardDTO();
		dto.setName(mr.getParameter("name"));
		dto.setTitle(mr.getParameter("title"));
		dto.setContent(mr.getParameter("content"));
		dto.setPass(mr.getParameter("pass"));
		
		// 원본 파일명과 저장된 파일 이름 설정
		String fileName = mr.getFilesystemName("ofile"); // 원본 파일명
		if(fileName != null) { // 첨부파일이 있을 경우, 새로운 파일명 생성하고 파일명 변경
			// 새로운 파일명 생성
			String now = new SimpleDateFormat("yyyyMMdd_HmsS").format(new Date()); // 겹치지 않기 위해 밀리초까지의 현재시간을 파일명으로 사용
			String ext = fileName.substring(fileName.lastIndexOf(".")); // 확장자
			String newFileName = now + ext; // 파일만의 이름
			
			// 파일명 변경				파일이 저장된 실제경로 + 분리자(/) + 파일이름
			File oldFile = new File(saveDirectory + File.separator + fileName); // File.separator = 분리자(/)
			File newFile = new File(saveDirectory + File.separator + newFileName);
			oldFile.renameTo(newFile); // oleFile에 저장된 해당 파일의 이름을 newFile에 저장된 해당 파일의 이름으로 변경(실제 파일의 이름을 변경)
			
			dto.setOfile(fileName); // 원래 파일만의 이름
			dto.setSfile(newFileName); // 서버에 저장된 파일만의 이름
		}
		
		// dao를 통해 DB에 게시 내용 저장
		MVCBoardDAO dao = new MVCBoardDAO();
		int result = dao.insertWrite(dto);
		dao.close();
		
		// 성공 ? 실패 ?
		if(result == 1) {
			resp.sendRedirect("../mvcboard/list.kse");
		} else {
			resp.sendRedirect("../mvcboard/write.kse");
		}
	} // doPost()
	
}
