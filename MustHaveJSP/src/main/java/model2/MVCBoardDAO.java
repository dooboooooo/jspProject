package model2;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import commom.DBConnPool;

public class MVCBoardDAO extends DBConnPool { // 커넥션 풀 상속

	public MVCBoardDAO() {
		super();
	} 
	
	// 검색 조건에 맞는 게시물의 개수를 반환한다.
	public int selectCount(Map<String, Object> map) {
		int totalCount = 0;
		
		String query = "select count(*) from mvcboard ";
		if(map.get("searchWord") != null) {
			query += " where " + map.get("searchField") + " " + " like '%" + map.get("searchWord") + "%'";
		}
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			rs.next();						// 커서를 첫번째 행으로 이동 .. ?!
			totalCount = rs.getInt(1);		// 쿼리문의 1번째 컬럼 값을 가져온다.
		} catch (Exception e) {
			System.out.println("게시물 카운트 중 예외 발생 : MVCBoardDAO.selectCount() 확인 필요");
			e.printStackTrace();
		}
		return totalCount;
	} // selectCount()
	
	// 검색 조건에 맞는 게시물 목록을 반환한다.(페이징O)
	public List<MVCBoardDTO> selectListPage(Map<String, Object> map){
		List<MVCBoardDTO> board = new Vector<MVCBoardDTO>();
		
		String query = "select * from (select Tb.*, rownum rNum from (select * from mvcboard ";
		if(map.get("searchWord") != null) {
			query += " where " + map.get("searchField") + " " + " like '%" + map.get("searchWord") + "%'" ;
		}
		query += " order by idx desc)Tb) where rNum between ? and ?";
		try {
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, map.get("start").toString());
			pstmt.setString(2, map.get("end").toString());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				MVCBoardDTO dto = new MVCBoardDTO();
				
				dto.setIdx(rs.getString(1));
				dto.setName(rs.getString(2));
				dto.setTitle(rs.getString(3));
				dto.setContent(rs.getString(4));
				dto.setPostdate(rs.getDate(5));
				dto.setOfile(rs.getString(6));
				dto.setSfile(rs.getString(7));
				dto.setDowncount(rs.getInt(8));
				dto.setPass(rs.getString(9));
				dto.setVisitcount(rs.getInt(10)); // 객체에 값 넣어서
				
				board.add(dto); // List에 추가
			}
		} catch (Exception e) {
			System.out.println("게시물 조회 중 예외 발생 : MVCBoardDAO.selectListPage() 확인 필요");
			e.printStackTrace();
		}
		
		return board;
	} // selectListPage
	
	// 작성완료 버튼 누르면 db에 저장된다.
	public int insertWrite(MVCBoardDTO dto) {
		int result = 0;
		try {
			String query = "insert into mvcboard(idx, name, title, content, ofile, sfile, pass) values(seq_board_num.nextval, ?, ?, ?, ?, ?, ?)";
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, dto.getName());
			pstmt.setString(2, dto.getTitle());
			pstmt.setString(3, dto.getContent());
			pstmt.setString(4, dto.getOfile());
			pstmt.setString(5, dto.getSfile());
			pstmt.setString(6, dto.getPass());
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("게시물 입력중 예외 발생 : MVCBoardDAO.insertWrite() 확인 필요");
			e.printStackTrace();
		}
		return result;
	} // insertWrite
	
	// 주어진 일련번호에 해당하는 게시물의 조회수를 1 증가시킨다.
	public void updateVisitCount(String idx) {
		String query = "update mvcboard set visitcount=visitcount+1 where idx = ?";
		try {
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, idx);
			pstmt.executeQuery();
		} catch (Exception e) {
			System.out.println("게시물 조회수 증가 중 예외 발생 : MVCBoardDAO.updateVisitCount() 확인 필요");
			e.printStackTrace();
		}
	} // updateVisitCount
	
	public MVCBoardDTO selectView(String idx) {
		MVCBoardDTO dto = new MVCBoardDTO();
		String query = "select * from mvcboard where idx = ?";
		try {
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, idx);
			rs = pstmt.executeQuery();
			
			if(rs.next()) { // 결과는 하나만 나옴 = if
				dto.setIdx(rs.getString(1));
				dto.setName(rs.getString(2));
				dto.setTitle(rs.getString(3));
				dto.setContent(rs.getString(4));
				dto.setPostdate(rs.getDate(5));
				dto.setOfile(rs.getString(6));
				dto.setSfile(rs.getString(7));
				dto.setDowncount(rs.getInt(8));
				dto.setPass(rs.getString(9));
				dto.setVisitcount(rs.getInt(10));
			}
		} catch (Exception e) {
			System.out.println("게시물 상세보기 중 예외 발생 : MVCBoardDAO.selectView() 확인 필요");
			e.printStackTrace();
		}
		return dto;
	} // selectView
	
	// 목록보기나 상세보기 화면에서 첨부파일 다운로드 링크를 클릭하면 다운로드수가 증가한다.
	public void downCountPlus(String idx) {
		String query = "update mvcboard set downcount=downcount+1 where idx = ?";
		try {
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, idx);
			pstmt.executeQuery();
		} catch (Exception e) {}
	} // downCountPlus
	
	// 입력한 비밀번호가 지정한 일련번호의 게시물의 비밀번호와 일치하는지 확인한다.
	public boolean confirmPassword(String pass, String idx) {
		boolean isCorr = true;
		try {
			String query = "select count(*) from mvcboard where pass = ? and idx = ?";
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, pass);
			pstmt.setString(2, idx);
			rs = pstmt.executeQuery();
			rs.next(); // 커서를 첫번째 행으로 이동 .. ?!
			if(rs.getInt(1) == 0) { // 검색된 갯수가 없으면(검색된 갯수가 있으면 아마도 1일것임)
				isCorr = false;
			}
		} catch (Exception e) {
			isCorr = false;
			e.printStackTrace();
		}
		return isCorr;
	} // confirmPassword
	
	// 지정한 일련번호의 게시물을 삭제한다.
	public int deletePost(String idx) {
		int result = 0;
		try {
			String query = "delete from mvcboard where idx = ?";
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, idx);
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("게시물 삭제 중 예외 발생 : MVCBoardDAO.deletePost() 확인 필요");
			e.printStackTrace();
		}
		return result;
	} // deletePost
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
