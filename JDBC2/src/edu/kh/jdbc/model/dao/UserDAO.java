package edu.kh.jdbc.model.dao;

// import static : 지정된 경로에 존재하는 static 구문을 모두 얻어와
// 클래스명.메서드명() 이 아닌 메서드명() 만 작성해도 호출 가능하게 함.
import static edu.kh.jdbc.common.JDBCTemplate.close;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.kh.jdbc.model.dto.User;

// (Model 중 하나) DAO (Data Access Object : 데이터 접근 객체)
// 데이터가 저장된 곳(DB)에 접근하는 용도의 객체
// >> DB에 접근하여 Java에서 원하는 결과를 얻기 위해
// SQL을 수행하고 결과를 반환하는 역할

public class UserDAO {

	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	/**
	 * 1. User 등록 DAO
	 * 
	 * @param conn : DB 연결정보가 담겨있는 Connection 객체
	 * @param user : 입력받은 id,pw,name이 세팅된 User 객체
	 * @return INSERT 결과 행의 갯수
	 */
	public int insertUser(Connection conn, User user) throws Exception {

		// 1. 결과 저장용 변수 선언
		int result = 0;

		try {
			// 2. SQL 작성
			String sql = """
					INSERT INTO TB_USER
					VALUES(SEQ_USER_NO.NEXTVAL, ?, ?, ?, DEFAULT)
					""";

			// 3. PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);

			// 4. 위치홀더 ?에 알맞은 값 대입
			pstmt.setString(1, user.getUserId());
			pstmt.setString(2, user.getUserPw());
			pstmt.setString(3, user.getUserName());

			// 5. SQL(INSERT) 수행(executeUpdate()) 후
			// 결과(삽입된 행의 개수) 반환 받기
			result = pstmt.executeUpdate();

		} finally {
			// 6. 사용한 JDBC 객체 자원 반환
			close(pstmt);
		}

		// 결과 저장용 변수에 저장된 최종 값 반환
		return result;

	}

	/**
	 * 2. User 전체 조회 DAO
	 * 
	 * @param conn
	 * @return List<User> userList
	 */
	public List<User> selectAll(Connection conn) throws Exception {

		// 1. 결과 저장용 변수 선언
		List<User> userList = new ArrayList<User>();

		try {

			// 2. SQL 작성
			String sql = """
					SELECT USER_NO, USER_ID, USER_PW, USER_NAME,
					TO_CHAR(ENROLL_DATE, 'YYYY"년" MM"월" DD"일"') ENROLL_DATE
					FROM TB_USER
					ORDER BY USER_NO ASC
					""";

			// 3. PreparedStatement 생성
			pstmt = conn.prepareStatement(sql);

			// 4. 위치홀더 ?에 알맞은 값 대입 (여기에는 ? 없음)

			// 5. SQL(SELECT) 수행(executeQuery()) 후 결과 ResultSet 반환
			rs = pstmt.executeQuery();

			// 6. 조회 결과(rs)를 한 행 씩 접근하여 컬럼 값 얻어오기
			// 몇 행이 조회될 지 모른다 >> while
			// 무조건 1행만 조회된다 >> if

			while (rs.next()) {
				int userNo = rs.getInt("USER_NO");
				String userId = rs.getString("USER_ID");
				String userPw = rs.getString("USER_PW");
				String userName = rs.getString("USER_NAME");
				String enrollDate = rs.getString("ENROLL_DATE");

				// >> java.sql.Date 타입으로 값을 저장하지 않은 이유
				// >> SELECT 문에서 TO_CHAR()를 이용하여 문자열 형태로
				// 변환해 조회해왔기 때문.

				// User 객체 새로 생성하여 DB에서 얻어온 컬럼값 필드로 세팅
				User user = new User(userNo, userId, userPw, userName, enrollDate);

				userList.add(user);
				// 4개의 유저 객체가 들어감

			}

		} finally {
			// 7. 사용한 자원 반환
			close(rs);
			close(pstmt);

		}

		// 조회 결과가 담긴 리스트 반환
		return userList;
	}

	public List<User> selectName(Connection conn, String keyword) throws Exception {
		List<User> searchList = new ArrayList<User>();

		try {

			String sql = """
					SELECT USER_NO, USER_ID, USER_PW, USER_NAME,
					TO_CHAR(ENROLL_DATE, 'YYYY"년" MM"월" DD"일"') ENROLL_DATE
					FROM TB_USER
					WHERE USER_NAME LIKE '%' || ? || '%'
					""";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, keyword);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				int userNo = rs.getInt("USER_NO");
				String userId = rs.getString("USER_ID");
				String userPw = rs.getString("USER_PW");
				String userName = rs.getString("USER_NAME");
				String enrollDate = rs.getString("ENROLL_DATE");

				User user = new User(userNo, userId, userPw, userName, enrollDate);

				searchList.add(user);
			}

		} finally {
			close(rs);
			close(pstmt);
		}
		return searchList;
	}

	public User selectUser(Connection conn, int userNo) throws Exception {

		User user = null;

		try {
			String sql = """
					SELECT USER_NO, USER_ID, USER_PW, USER_NAME,
					TO_CHAR(ENROLL_DATE, 'YYYY"년" MM"월" DD"일"') ENROLL_DATE
					FROM TB_USER
					WHERE USER_NO = ?
					""";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userNo); 

			rs = pstmt.executeQuery();

			if (rs.next()) {
				int uNo = rs.getInt("USER_NO");
				String userId = rs.getString("USER_ID");
				String userPw = rs.getString("USER_PW");
				String userName = rs.getString("USER_NAME");
				String enrollDate = rs.getString("ENROLL_DATE");

				user = new User(uNo, userId, userPw, userName, enrollDate);
			}

		} finally {
			close(rs);
			close(pstmt);
		}

		return user; // 조회 결과가 없으면 null 반환
	}
	
	/**
	 * 5. User 삭제 DAO
	 * @param conn
	 * @param userNo : 삭제할 User 번호
	 * @return delete 결과 행의 개수
	 */
	public int deleteUser(Connection conn, int userNo) throws Exception {
		
		int result = 0;
		
		try {
			String sql = """
					DELETE FROM TB_USER
					WHERE USER_NO = ?
					""";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, userNo);
			
			result = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
		}

		return result;		
	}
	
	
}
