package model1;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletContext;

import commom.JDBConnect;

public class BoardDAO extends JDBConnect{ // 3번째 생성자로 활용
	
	public BoardDAO(ServletContext app) { 
		// ServletContext app -> WEB-INF / web.xml에 있는 값 활용
		super(app);
	} // con, stmt, pstmt, rs, close()를 제공받는다.
	
	// 게시물 개수 세는 메서드(목록에 출력할 게시물을 얻어오기 위한 메서드)
	public int selectCount(Map<String, Object> map) {
		int totalCount = 0; // 결과(게시물 수)를 담을 변수
		
		// 게시물 수를 얻어오는 쿼리문 작성
		String query = "select count(*) from board";
		if(map.get("searchWord") != null) {
			query += " where " + map.get("searchField") + " " + " like '%" + map.get("searchWord") + "%'";
		}
		try {
			stmt = con.createStatement();       // 쿼리문 생성(인파라미터 안씀)
			rs = stmt.executeQuery(query);		// 쿼리 실행
			rs.next();							// 커서를 첫 번째 행으로 이동
			totalCount = rs.getInt(1);			// 첫 번째 칼럼 값을 가져옴
			
		} catch (Exception e) {
			System.out.println("게시물 수를 구하는 중 예외 발생(BoardDAO - selectCount 메서드를 확인하세요)");
			e.printStackTrace();
		}
		return totalCount;
	} // selectCount()
	
	// 검색 조건에 맞는 게시물 목록을 반환한다.(페이징X)
	public List<BoardDTO> selectList(Map<String, Object> map){
		List<BoardDTO> bbs = new Vector<BoardDTO>(); // 결과(게시물 목록)를 담을 리스트 변수 // Vector 하나의 스레드가 작업을 완료해야만 다음 스레드가 접근 가능(여러사람이 사용할 때)
		
		String query = "select * from board "; // 조건이 없을 때
		if(map.get("searchWord") != null) {
			query += " where " + map.get("searchField") + " " + " like '%" + map.get("searchWord") + "%' " ; // 조건이 있을 때
		}
		query += " order by num DESC " ; 
		
		try {
			stmt = con.createStatement(); // 쿼리문 생성
			rs = stmt.executeQuery(query); // 쿼리 실행
			
			while(rs.next()) { // 한 행(게시물 하나)의 내용을 DTO에 저장
				BoardDTO dto = new BoardDTO();
				
				dto.setNum(rs.getString("num"));                // 일련변호
				dto.setTitle(rs.getString("title"));			// 제목
				dto.setContent(rs.getString("content"));		// 내용
				dto.setPostdate(rs.getDate("postdate"));		// 작성일
				dto.setId(rs.getString("id"));					// 작성자 아이디
				dto.setVisitcount(rs.getString("visitcount"));	// 조회수
				
				bbs.add(dto);
			}
		} catch (Exception e) {
			System.out.println("게시물 조회 중 예외 발생(BoardDAO - selectList(페이징X) 메서드를 확인하세요)");
			e.printStackTrace();
		}
		return bbs;
	} // selectList()
	
	// 검색 조건에 맞는 게시물 목록을 반환한다.(페이징O)
	public List<BoardDTO> selectListPage(Map<String, Object> map){
		List<BoardDTO> bbs = new Vector<BoardDTO>(); // 결과(게시물 목록)를 담을 리스트 변수 // Vector 하나의 스레드가 작업을 완료해야만 다음 스레드가 접근 가능(여러사람이 사용할 때)
		
		String query = "select * from (select Tb.*, rownum rNum from (select * from board "; // 조건이 없을 때
		if(map.get("searchWord") != null) {
			query += " where " + map.get("searchField") + " " + " like '%" + map.get("searchWord") + "%' " ; // 조건이 있을 때
		}
		query += " order by num desc) Tb) where rNum between ? and ? " ; 
		
		try {
			pstmt = con.prepareStatement(query); // 쿼리문 생성
			pstmt.setString(1, map.get("start").toString());
			pstmt.setString(2, map.get("end").toString());
			rs = pstmt.executeQuery(); // 쿼리 실행
			
			while(rs.next()) { // 한 행(게시물 하나)의 내용을 DTO에 저장
				BoardDTO dto = new BoardDTO();
				
				dto.setNum(rs.getString("num"));                // 일련변호
				dto.setTitle(rs.getString("title"));			// 제목
				dto.setContent(rs.getString("content"));		// 내용
				dto.setPostdate(rs.getDate("postdate"));		// 작성일
				dto.setId(rs.getString("id"));					// 작성자 아이디
				dto.setVisitcount(rs.getString("visitcount"));	// 조회수
				
				bbs.add(dto);
			}
		} catch (Exception e) {
			System.out.println("게시물 조회 중 예외 발생(BoardDAO - selectListPage(페이징O) 메서드를 확인하세요)");
			e.printStackTrace();
		}
		return bbs;
	} // selectListPage()
	
	// 게시글 데이터를 BoardDTO 타입으로 받아 DB에 추가한다.
	public int insertWrite(BoardDTO dto) {
		int result = 0;
		try {
			// insert 쿼리문 작성
			String query = "insert into board (num, title, content, id, visitcount) values(seq_board_num.nextval, ?, ?, ?, 0) ";
			pstmt = con.prepareStatement(query); // 동적 쿼리 생성
			pstmt.setString(1, dto.getTitle());
			pstmt.setString(2, dto.getContent());
			pstmt.setString(3, dto.getId()); // 세션에 있는 아이디(로그인 된 아이디)
			result = pstmt.executeUpdate(); // 쿼리 실행
		} catch (Exception e) {
			System.out.println("게시물 입력 중 예외 발생(BoardDAO - insertWrite 메서드를 확인하세요)");
			e.printStackTrace();
		}
		return result;
	} // insertWrite()
	
	// 게시물을 클릭했을 때 조회수 증가하는 메서드
	public void updateVisitCount(String num) {
		// 쿼리문 준비
		String query = "update board set visitcount=visitcount+1 where num=?";
		try {
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, num);    // 인파라미터를 일련번호로 설정
			pstmt.executeQuery();		// 쿼리 실행
			
		} catch (Exception e) {
			System.out.println("게시물 조회수 증가 중 예외 발생(BoardDAO - updateVisitCount 메서드를 확인하세요)");
			e.printStackTrace();
		}		
	}
	
	// 자세히 보기(번호를 받아서 dto로 리턴)
	public BoardDTO selectView(String num) {
		BoardDTO dto = new BoardDTO(); // dto 객체 생성
		
		// 쿼리문 준비
		String query = "select B.*, M.name from member M inner join board B on M.id=B.id where num=?";
		
		try {
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, num);        // 인파라미터를 일련번호로 설정
			rs = pstmt.executeQuery();		// 쿼리 실행 후 결과를 표로 받음
			
			// 결과 처리
			if(rs.next()) {
				dto.setNum(rs.getString("num"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setPostdate(rs.getDate("postdate"));
				dto.setId(rs.getString("id"));
				dto.setVisitcount(rs.getString("visitcount"));
				dto.setName(rs.getString("name")); // dto 객체에 값 저장
			}
			
		} catch (Exception e) {
			System.out.println("게시물 상세보기 중 예외 발생(BoardDAO - selectView 메서드를 확인하세요)");
			e.printStackTrace();
		}
		return dto;
	} // selectView()
	
	// 업데이트용 메서드(dto를 받아 int로 리턴)
	public int updateEdit(BoardDTO dto) {
		int result = 0;
		try {
			String query = "update board set title=?, content=? where num=?";
			pstmt = con.prepareStatement(query); // 동적 쿼리문 생성
			pstmt.setString(1, dto.getTitle());
			pstmt.setString(2, dto.getContent());
			pstmt.setString(3, dto.getNum());
			
			result = pstmt.executeUpdate(); // 쿼리문 실행하여 결과를 int로 받음
		} catch (Exception e) {
			System.out.println("게시물 수정 중 예외 발생(BoardDAO - updateEdit 메서드를 확인하세요)");
			e.printStackTrace();
		}
		return result;
	} // updateEdit()
	
	// 지정한 게시물을 삭제한다.
	public int deletePost(BoardDTO dto) {
		int result = 0;
		try {
			String query = "delete from board where num=?";
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, dto.getNum());
			
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("게시물 삭제 중 예외 발생(BoardDAO - deletePost 메서드를 확인하세요)");
			e.printStackTrace();
		}
		return result;
	} // deletePost() 
	
} // class
