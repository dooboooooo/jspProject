package model2.mvcboard;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model2.MVCBoardDAO;
import model2.MVCBoardDTO;

@WebServlet("/mvcboard/view.kse")
public class ViewController extends HttpServlet {
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시물 가져오기
		MVCBoardDAO dao = new MVCBoardDAO();
		String idx = req.getParameter("idx");
		dao.updateVisitCount(idx); // 조회수 증가시키고
		MVCBoardDTO dto = dao.selectView(idx); // 게시물 가져오기
		dao.close();
		
		// 줄바꿈 처리
		dto.setContent(dto.getContent().replaceAll("\r\n", "<br/>"));
		
		// 게시물(dto) 저장 후 뷰로 포워트
		req.setAttribute("dto", dto);
		req.getRequestDispatcher("/14MVCBoard/View.jsp").forward(req, resp);
	}
	
}
