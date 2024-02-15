package model2.mvcboard;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model2.FileUtil;
import model2.MVCBoardDAO;

@WebServlet("/mvcboard/download.kse")
public class DownloadController extends HttpServlet { // 파일을 다운로드 해주는 서블릿
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 매개변수 받기
		String ofile = req.getParameter("ofile");
		String sfile = req.getParameter("sfile");
		String idx = req.getParameter("idx");
		
		// 파일 다운로드
		FileUtil.download(req, resp, "/Uploads", sfile, ofile);
		
		// 해당 게시물 다운로드 카운트 증가
		MVCBoardDAO dao = new MVCBoardDAO();
		dao.downCountPlus(idx);
		dao.close();
	} // doGet()
}
