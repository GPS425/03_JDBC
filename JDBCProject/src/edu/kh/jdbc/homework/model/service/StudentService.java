package edu.kh.jdbc.homework.model.service;

import java.sql.Connection;
import java.util.List;

import edu.kh.jdbc.homework.common.JDBCTemplate;
import edu.kh.jdbc.homework.model.dao.StudentDAO;
import edu.kh.jdbc.homework.model.dto.Student;

public class StudentService {
	private StudentDAO dao = new StudentDAO();

	public int insertStudent(Student std) throws Exception{
		
		Connection conn = JDBCTemplate.getConnection();
		
		int result = dao.insertStudent(conn, std);
		
		if (result > 0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		return result;
	}

	public List<Student> selectAll() throws Exception {
		
		Connection conn = JDBCTemplate.getConnection();
		
		List<Student> allStdList = dao.selectAll(conn);
		
		JDBCTemplate.close(conn);
		
		return allStdList;
	}
	
	public int updateStudentSelect(Student std) throws Exception {
		
		Connection conn = JDBCTemplate.getConnection();
		
		int result = dao.updateStudentSelect(conn, std); 
		
		if (result > 0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
		return result;
	}
	
	public int deleteStudent(int stdNo) throws Exception {
		
		Connection conn = JDBCTemplate.getConnection();
		
		int result = dao.deleteStudent(conn, stdNo);
		
		if (result > 0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
		return result;
	}

	public List<Student> selectByMajor(String major) throws Exception{
		
		Connection conn = JDBCTemplate.getConnection();
		
		List<Student> stdList = dao.selectByMajor(conn, major);
		
		JDBCTemplate.close(conn);
		
		return stdList;
	}
	
	

}
