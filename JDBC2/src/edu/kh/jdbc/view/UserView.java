package edu.kh.jdbc.view;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import edu.kh.jdbc.model.dto.User;
import edu.kh.jdbc.model.service.UserService;

// View : 사용자와 직접 상호작용하는 화면(UI)담당
// 사용자에게 입력을 받고 결과를 출력하는 역할
public class UserView {

	private UserService service = new UserService();
	private Scanner sc = new Scanner(System.in);
	
	
	/**
	 * User 관리 프로그램 메인 메뉴 UI(View)
	 */
	public void mainMenu() {
		
		int input = 0;		// 메뉴 선택용 변수
		
		do {
			
			try {
				
				System.out.println("\n===== User 관리 프로그램 =====\n");
				System.out.println("1. User 등록(INSERT)");
				System.out.println("2. User 전체 조회(SELECT)");
				System.out.println("3. User 중 이름에 검색어가 포함된 회원 조회 (SELECT)");
				System.out.println("4. USER_NO를 입력 받아 일치하는 User 조회(SELECT)");
				System.out.println("5. USER_NO를 입력 받아 일치하는 User 삭제(DELETE)");
				System.out.println("6. ID, PW가 일치하는 회원이 있을 경우 이름 수정(UPDATE)");
				System.out.println("7. User 등록(아이디 중복 검사)");
				System.out.println("8. 여러 User 등록하기");
				System.out.println("0. 프로그램 종료");
				
				System.out.print("메뉴 선택 : ");
				input = sc.nextInt();
				sc.nextLine(); // 버퍼에 남은 개행문자 제거
				
				switch (input) {
				case 1: insertUser(); break;
				case 2: selectAll(); break;
				case 3: selectName(); break;
				case 4: selectUser(); break;
				case 5: deleteUser(); break;
				case 6: updateName(); break;
				case 7: insertUser2(); break;
				case 8: multiInsertUser(); break;
				case 0: System.out.println("\n[프로그램 종료]\n"); break;
				default: System.out.println("\n[메뉴 번호만 입력하세요]\n");
				}
				
				System.out.println("\n-------------------------------------\n");
				
				
			} catch (InputMismatchException e) {
				// 스캐너를 이용한 입력 시 자료형이 잘못된 경우
				System.out.println("\n****잘못 입력하셨습니다****\n");
				
				input = -1;		// 잘못 입력해서 while문이 처음부터 멈추는 일 방지
				sc.nextLine();	// 입력버퍼에 남아있는 잘못된 문자 제거
				
				
			} catch (Exception e2) {
				// 발생되는 예외를 모두 해당 catch 구문에 모아서 예외처리
				e2.printStackTrace();
			}
			
		} while(input != 0);
		
	}

	/**
	 * 1. User 등록 관련된 View
	 * 
	 */
	private void insertUser() throws Exception {
		
		System.out.println("\n==== 1. User 등록 ====\n");
		
		System.out.print("ID : ");
		String userId = sc.next();
		
		System.out.print("PW : ");
		String userPw = sc.next();
		
		System.out.print("Name : ");
		String userName = sc.next();
		
		// 입력받은 값 3개를 한 번에 묶어서 전달할 수 있도록
		// User DTO 객체를 생성한 후 필드에 값을 세팅
		
		User user = new User();
		
		// Setter 이용
		user.setUserId(userId);
		user.setUserPw(userPw);
		user.setUserName(userName);
		
		// 서비스 호출(INSERT) 후 결과 반환(int, 결과 행의 갯수) 받기
		int result = service.insertUser(user);
		// service 객체(UserService)에 있는 insertUser() 메서드 호출
		
		// 반환된 결과에 따라 출력할 내용 선택
		if(result > 0) {
			System.out.println("\n" + userId + " 사용자가 등록되었습니다.\n");
			
		} else {
			System.out.println("\n*** 등록 실패 ***\n");
		}
	}

	/**
	 * 2. User 전체 조회 관련 View(SELECT) 
	 */
	private void selectAll() throws Exception {
		System.out.println("\n==== 2. User 전체 조회 ====\n");
		
		// 서비스(SELECT) 호출 후 결과 반환(List<User>) 받기
		List<User> userList = service.selectAll();
		
		// 조회 결과가 없을 경우
		if(userList.isEmpty()) {
			System.out.println("\n*** 조회 결과가 없습니다 ***\n");
			return;
		}
		
		// 조회 결과가 있을 경우
		// userList에 있는 모든 User 객체 출력
		// 향상된 for문 이용
		
		for(User user : userList) {
			System.out.println(user);
		}
		
		
	}
	
	/**
	 * 3. User 중 이름에 검색어가 포함된 회원 조회
	 * 검색어 입력 : 유
	 * 이름에 '유' 포함된 데이터 모두 출력
	 */
	private void selectName() throws Exception{
		System.out.println("\n==== 3. User 중 이름에 검색어가 포함된 회원 조회 ====\n");
		
		System.out.print("검색어 입력 : ");
		String keyword = sc.next();
		
		
		List<User> userList = service.selectName(keyword);
		
		if(userList.isEmpty()) {
			System.out.println("\n*** 검색 결과가 없습니다 ***\n");
			return;
		}
		
		for(User user : userList) {
			System.out.println(user);
		}
		
		
	}

	/**
	 * 4. User_NO를 입력받아 일치하는 User 조회
	 * 딱 1행만 조회되거나, 일치하는 행을 못 찾거나 둘 중 하나
	 * 찾았을 때 : User 객체 출력
	 * 못 찾았을 때 : USER_NO가 일치하는 회원 없음
	 *
	 */
	private void selectUser() throws Exception{
		System.out.println("\n==== 4. USER_NO를 입력 받아 일치하는 User 조회 ====\n");
		
		System.out.print("검색할 User 번호 입력 : ");
		int userNo = sc.nextInt();
		sc.nextLine();
		
		User user = service.selectUser(userNo); 
		
		if (user != null) {
			System.out.println(user); 
		} else {
			System.out.println("\nUSER_NO " + userNo + "번과 일치하는 회원 없음.\n");
		}
		
	}
	
	/** 5. USER_NO를 입력받아 일치하는 User 삭제(DELETE)
	 * * DML임
	 * 
	 * -- 삭제 성공 시 : 삭제 성공 출력
	 * -- 삭제 실패 시 : 사용자 번호가 일치하는 User가 존재하지 않음
	 */
	private void deleteUser() throws Exception { 
		System.out.println("\n==== 5. USER_NO를 입력 받아 일치하는 User 삭제 ====\n");
		
		System.out.print("삭제할 User 번호 입력 : ");
		int userNo = sc.nextInt(); 
		sc.nextLine();
		
		int result = service.deleteUser(userNo);
		
		if (result > 0) {
			System.out.println("삭제 성공");
		} else {
			System.out.println("사용자 번호가 일치하는 User가 존재하지 않음");
		}
	}


	/** 6. 
	 * 
	 */
	private void updateName() {
		System.out.println("\n==== 6. ID, PW가 일치하는 회원이 있을 경우 이름 수정 ====\n");
	}
	
	private void insertUser2() {
		
	}

	
	private void multiInsertUser() {
		
	}













	
	
}
