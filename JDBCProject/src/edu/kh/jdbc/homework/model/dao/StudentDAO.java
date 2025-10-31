package edu.kh.jdbc.homework.model.dao;

import static edu.kh.jdbc.homework.common.JDBCTemplate.close;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import edu.kh.jdbc.homework.model.dto.Student;

public class StudentDAO {
	
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	public int insertStudent(Connection conn, Student std) throws Exception {
		int result = 0;
		
		try {
			
			String sql = """
					INSERT INTO KH_STUDENT
					VALUES(SEQ_KH_STUDENT.NEXTVAL, ?, ?, ?, DEFAULT)
					""";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, std.getStdName());
			pstmt.setInt(2, std.getStdAge());
			pstmt.setString(3, std.getMajor());
			
			result = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
		}
		return result;
	}

	public List<Student> selectAll(Connection conn) throws Exception {
		
		List<Student> stdList = new ArrayList<Student>();
		
		try {
			
			String sql = """
					SELECT STD_NO, STD_NAME, STD_AGE, MAJOR,
					TO_CHAR(ENT_DATE, 'YYYY"-"MM"-"DD') ENT_DATE
					FROM KH_STUDENT
					ORDER BY STD_NO ASC
					""";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				int stdNo = rs.getInt("STD_NO");
				String stdName = rs.getString("STD_NAME");
				int stdAge = rs.getInt("STD_AGE");
				String major = rs.getString("MAJOR");
				String entDate = rs.getString("ENT_DATE");
				
				Student std = new Student(stdNo, stdName, stdAge, major, entDate);
				
				stdList.add(std);
			}
			
					
		} finally {
			close(rs);
			close(pstmt);
		}
		
		return stdList;
	}
	
	public int updateStudentSelect(Connection conn, Student std) throws Exception {
		int result = 0;

		PreparedStatement pstmt = null; 
		
		try {
			String sql = "UPDATE KH_STUDENT SET ";
			
			// SET 절에 들어갈 내용과 물음표(?) 개수를 카운트 할 리스트
			List<String> setList = new ArrayList<>();
			
			// 이름 수정 여부 확인
			if (!std.getStdName().isEmpty()) {
				setList.add("STD_NAME = ?");
			}
			
			// 나이 수정 여부 확인
			if (std.getStdAge() > 0) {
				setList.add("STD_AGE = ?");
			}
			
			// 전공 수정 여부 확인
			if (!std.getMajor().isEmpty()) {
				setList.add("MAJOR = ?");
			}
			
			// 만약 수정할 항목이 하나도 없다면 수정 X
			if (setList.isEmpty()) {
				return 0; 
			}
			
			// SET 절 조합
			sql += String.join(", ", setList); 
			
			// WHERE 절 추가
			sql += " WHERE STD_NO = ?"; 
			
			pstmt = conn.prepareStatement(sql);
			
			// 위치홀더에 값 대입
			int paramIndex = 1;
			
			if (!std.getStdName().isEmpty()) {
				pstmt.setString(paramIndex++, std.getStdName());
			}
			
			if (std.getStdAge() > 0) {
				pstmt.setInt(paramIndex++, std.getStdAge());
			}
			
			if (!std.getMajor().isEmpty()) {
				pstmt.setString(paramIndex++, std.getMajor());
			}
			
			pstmt.setInt(paramIndex, std.getStdNo());
			
			result = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
		}
		
		return result;
		
	}
	
	public int deleteStudent(Connection conn, int stdNo) throws Exception {
		int result = 0;
		
		try {
			
			String sql = """
					DELETE FROM KH_STUDENT
					WHERE STD_NO = ?
					""";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, stdNo);
			
			result = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	public List<Student> selectByMajor(Connection conn, String major) throws Exception {
		
		List<Student> stdList = new ArrayList<Student>();
		
		try {
			
			String sql = """
					SELECT STD_NO, STD_NAME, STD_AGE, MAJOR,
					TO_CHAR(ENT_DATE, 'YYYY"-"MM"-"DD') ENT_DATE
					FROM KH_STUDENT
					WHERE MAJOR = ? 
					ORDER BY STD_NO ASC
					""";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, major);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				int stdNo = rs.getInt("STD_NO");
				String stdName = rs.getString("STD_NAME");
				int stdAge = rs.getInt("STD_AGE");
				String major1 = rs.getString("MAJOR");
				String entDate = rs.getString("ENT_DATE");
				
				Student std = new Student(stdNo, stdName, stdAge, major1, entDate);
				
				stdList.add(std);
			}
			
		} finally {
			close(rs);
			close(pstmt);
		}
		
		return stdList;
	}

}
