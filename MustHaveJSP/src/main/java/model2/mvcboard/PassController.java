package model2.mvcboard;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model2.FileUtil;
import model2.MVCBoardDAO;
import model2.MVCBoardDTO;
import utils.JSFunction;

@WebServlet("/mvcboard/pass.kse")
public class PassController extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("mode", req.getParameter("mode")); // request로 넘어온 mode 파라미터 값을 다시 보내기 위해 setAttribute로 설정
		req.getRequestDispatcher("/14MVCBoard/Pass.jsp").forward(req, resp);
	} // doGat()
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 매개변수 저장
		String idx = req.getParameter("idx");
		String mode = req.getParameter("mode");
		String pass = req.getParameter("pass");
		
		// 비밀번호 확인
		MVCBoardDAO dao = new MVCBoardDAO();
		boolean confirmed = dao.confirmPassword(pass, idx);
		dao.close();
		
		if(confirmed) { // 넘겨받은 게시글 인덱스와 비밀번호가 일치하면 모드 값을 보고 수정인지, 삭제인지 판단
			if(mode.equals("edit")) { // 수정이면
				HttpSession session = req.getSession();
				session.setAttribute("pass", pass);
				resp.sendRedirect("../mvcboard/edit.kse?idx="+idx);
			} else if(mode.equals("delete")) { // 삭제면
				dao = new MVCBoardDAO();
				MVCBoardDTO dto = dao.selectView(idx); // 삭제할 게시물을 dto로 가져온다.
				int result = dao.deletePost(idx);
				dao.close();
				if(result == 1) { // 삭제에 성공하면
					String saveFileName = dto.getSfile(); // dto에서 파일 이름을 가져와서
					FileUtil.deleteFile(req, "/Uploads", saveFileName); // 파일도 삭제
				}
				JSFunction.alertLocation(resp, "삭제되었습니다.", "../mvcboard/lise.kse");
			}
		} else {
			JSFunction.alertBack(resp, "비밀번호가 잘못되었습니다.");
		}
	} // doPost()
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
