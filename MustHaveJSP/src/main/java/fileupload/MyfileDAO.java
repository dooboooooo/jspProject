package fileupload;

import java.sql.SQLException;

import commom.DBConnPool;

public class MyfileDAO extends DBConnPool{ // jdbc 연결해서 sql문을 처리
	// 새로운 게시물을 입력
	public int insertFile(MyfileDTO dto) {
		int applyResult = 0;
		try {
			String query = "insert into myfile(idx, name, title, cate, ofile, sfile) values(seq_board_num.nextval, ?, ?, ?, ?, ?)";
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, dto.getName());
			pstmt.setString(2, dto.getTitle());
			pstmt.setString(3, dto.getCate());
			pstmt.setString(4, dto.getOfile());
			pstmt.setString(5, dto.getSfile());
			
			applyResult = pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("insert 중 예외 발생(MyfileDAO.insertFile()을 확인하세요");
			e.printStackTrace();
		}
		return applyResult;
	}

}
