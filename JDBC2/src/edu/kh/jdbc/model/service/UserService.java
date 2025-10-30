package edu.kh.jdbc.model.service;

import java.sql.Connection;
import java.util.List;

import edu.kh.jdbc.common.JDBCTemplate;
import edu.kh.jdbc.model.dao.UserDAO;
import edu.kh.jdbc.model.dto.User;

// (Model 중 하나) Service : 비즈니스 로직을 처리하는 계층,
// 데이터를 가공하고 트랜잭션(commit, rollback) 관리 수행

public class UserService {
	private UserDAO dao = new UserDAO();

	/**
	 * 1. User 등록 서비스
	 * 
	 * @param user : 입력받은 id, pw, name이 세팅된 객체
	 * @return insert된 결과 행의 개수
	 */
	public int insertUser(User user) throws Exception {

		// 1. 커넥션 생성
		Connection conn = JDBCTemplate.getConnection();

		// 2. 데이터 가공(할 것 없으면 생략)

		// 3. DAO 메서드 호출 후 결과 반환받기
		int result = dao.insertUser(conn, user);

		// 4. DML(INSERT) 수행 결과에 따라 트랜잭션 제어 처리
		if (result > 0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}

		// 5. Connection 반환하기
		JDBCTemplate.close(conn);

		// 결과 반환
		return result;
	}

	/**
	 * 2. User 전체 조회 서비스
	 * 
	 * @return 조회된 User들이 담긴 List
	 */
	public List<User> selectAll() throws Exception {

		// 1. 커넥션 생성
		Connection conn = JDBCTemplate.getConnection();

		// 2. 데이터 가공 (생략)
		// 3. DAO 메서드 호출(SELECT) 후 결과 반환(List<User>) 받기
		List<User> userList = dao.selectAll(conn);

		// 4. Connection 반환
		JDBCTemplate.close(conn);

		// 5. 결과 반환
		return userList;
	}

	public List<User> selectName(String keyword) throws Exception {
		Connection conn = JDBCTemplate.getConnection();

		List<User> searchList = dao.selectName(conn, keyword);

		JDBCTemplate.close(conn);

		return searchList;
	}

	public User selectUser(int userNo) throws Exception {

		Connection conn = JDBCTemplate.getConnection();

		User user = dao.selectUser(conn, userNo);

		JDBCTemplate.close(conn);

		return user;
	}
	
	/**
	 * 5. User 삭제 서비스
	 * @param userNo : 삭제할 User 번호
	 * @return delete 결과 행의 개수
	 */
	public int deleteUser(int userNo) throws Exception {
		
		Connection conn = JDBCTemplate.getConnection();
		
		int result = dao.deleteUser(conn, userNo);
		
		if(result > 0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
		return result;
	}

}
