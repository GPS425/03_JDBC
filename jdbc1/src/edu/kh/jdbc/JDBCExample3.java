package edu.kh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class JDBCExample3 {
	public static void main(String[] args) {
		// 입력받은 최소 급여 이상
		// 입력받은 최대 급여 이하를 받는
		// 사원의 사번, 이름, 급여를 받는
		// 급여 내림차순으로 조회
		// >> 이클립스 콘솔 출력
		
		// [실행화면]
		// 최소 급여 : 1000000
		// 최대 급여 : 3000000
		// 사번 / 이름 / 급여
		// 사번 / 이름 / 급여
		// ...
		
		Connection conn = null; 
		Statement stmt = null; 
		ResultSet rs = null; 

		Scanner sc = null;

		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");

			String type = "jdbc:oracle:thin:@"; 
			String host = "localhost"; 
			String port = ":1521"; 
			String dbName = ":XE";

			String userName = "kh_jjm";
			String password = "kh1234";

			conn = DriverManager.getConnection(type + host + port + dbName, userName, password);

			sc = new Scanner(System.in);
			
			System.out.print("최소 급여 : ");
			int min = sc.nextInt();
			System.out.print("최대 급여 : ");
			int max = sc.nextInt();
			
			// JAVA 13 이상부터 지원하는 TEXT BLOCK(""") 문법
			String sql = """
					SELECT EMP_ID, EMP_NAME, SALARY
					FROM EMPLOYEE
					WHERE SALARY BETWEEN
					""" + min + " AND " + max + " ORDER BY SALARY DESC";
			
			
			stmt = conn.createStatement();
			
			rs = stmt.executeQuery(sql);
			
			
			while(rs.next()) {
				String empId = rs.getString("EMP_ID");
				String empName = rs.getString("EMP_NAME");
				int salary = rs.getInt("SALARY");
				
				System.out.printf("%s / %s / %d원 \n", empId, empName, salary);
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
				if(sc != null) sc.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
