package edu.kh.jdbc.homework.view;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import edu.kh.jdbc.homework.model.dto.Student;
import edu.kh.jdbc.homework.model.service.StudentService;

public class StudentView {

	private StudentService service = new StudentService();
	private Scanner sc = new Scanner(System.in);

	public void menu() {
		int input = 0;

		do {
			try {

				System.out.println("\n========= 학생 관리 프로그램 =========\n");
				System.out.println("1. 학생 등록");
				System.out.println("2. 전체 학생 조회");
				System.out.println("3. 학생 정보 수정");
				System.out.println("4. 학생 삭제");
				System.out.println("5. 전공별 학생 조회");
				System.out.println("0. 시스템 종료");

				System.out.print("메뉴 선택 : ");
				input = sc.nextInt();
				sc.nextLine();

				switch (input) {
				case 1:
					insertStudent();
					break;
				case 2:
					selectAll();
					break;
				case 3:
					updateStudent();
					break;
				case 4:
					deleteStudent();
					break;
				case 5:
					selectByMajor();
					break;
				case 0:
					System.out.println("프로그램을 종료합니다.");
				}


			} catch (InputMismatchException e) {
				// 스캐너를 이용한 입력 시 자료형이 잘못된 경우
				System.out.println("\n***잘못 입력하셨습니다***\n");

				input = -1;
				sc.nextLine();

			} catch (Exception e2) {
				e2.printStackTrace();
			}

		} while (input != 0);
	}

	private void insertStudent() throws Exception {
		System.out.println("\n**** 1. 새로운 학생 등록하기 ****\n");

		System.out.print("학생의 이름 : ");
		String name = sc.next();

		System.out.print("학생의 나이 : ");
		int age = sc.nextInt();
		sc.nextLine();

		System.out.print("학생의 전공 : ");
		String major = sc.next();

		Student std = new Student();
		std.setStdName(name);
		std.setStdAge(age);
		std.setMajor(major);

		int result = service.insertStudent(std);

		if (result > 0) {
			System.out.println("새로운 학생이 등록되었습니다.");
		} else {
			System.out.println("학생 등록에 실패하였습니다.");
		}

	}

	private void selectAll() throws Exception {
		System.out.println("\n**** 2. 학생 전체 조회하기 ****\n");

		List<Student> stdList = service.selectAll();

		if (stdList.isEmpty()) {
			System.out.println("학생 없어서 폐교합니다 GG");
			return;
		}

		for (Student std : stdList) {
			System.out.println(std);
		}

	}

	private void updateStudent() throws Exception {
		System.out.println("\n**** 3. 학생 정보 수정하기 ****\n");

		System.out.print("수정할 학생의 학번(STD_NO) 입력: ");
		int stdNo = sc.nextInt();
		sc.nextLine(); 

		int input = -1;
		String name = null;
		int age = -1;
		String major = null;

		do {
			try {
				System.out.println("\n**** 수정할 항목 선택 ****");
				System.out.println("1. 이름 변경");
				System.out.println("2. 나이 변경");
				System.out.println("3. 전공 변경");
				System.out.println("4. 모두 변경");
				System.out.println("0. 수정 취소/완료");

				System.out.print("선택 : ");
				input = sc.nextInt();
				sc.nextLine();

				if (input >= 1 && input <= 4) {
					switch (input) {
					case 1:
						System.out.print("새 이름 : ");
						name = sc.nextLine();
						break;
					case 2:
						System.out.print("새 나이 : ");
						age = sc.nextInt();
						sc.nextLine();
						break;
					case 3:
						System.out.print("새 전공 : ");
						major = sc.nextLine();
						break;
					case 4:
						System.out.print("새 이름 : ");
						name = sc.nextLine();
						System.out.print("새 나이 : ");
						age = sc.nextInt();
						sc.nextLine();
						System.out.print("새 전공 : ");
						major = sc.nextLine();
						break;
					}

					Student std = new Student();
					std.setStdNo(stdNo);

					// null 체크를 위해 Service/DAO에 전달할 때,
					// 이름/전공은 ""로, 나이는 -1(또는 0)로 미입력 상태임을 알림
					std.setStdName(name == null ? "" : name);
					std.setStdAge(age == -1 ? 0 : age);
					std.setMajor(major == null ? "" : major);

					int result = service.updateStudentSelect(std);

					if (result > 0) {
						System.out.println("학생 정보가 성공적으로 수정되었습니다.");
					} else {
						System.out.println("학생 정보 수정에 실패하였습니다.");
					}

					name = null;
					age = -1;
					major = null;

				} else if (input != 0) {
					System.out.println("\n잘못 입력하셨습니다.\n");
				}

			} catch (InputMismatchException e) {
				System.out.println("\n숫자만 입력해주세요.\n");
				input = -1;
				sc.nextLine();
			} catch (Exception e2) {
				e2.printStackTrace();
				input = 0;
			}
		} while (input != 0);

		if (input == 0) {
			System.out.println("\n**** 학생 정보 수정을 종료합니다. ****");
		}
	}

	private void deleteStudent() throws Exception { // throws Exception 추가
		System.out.println("\n**** 4. 학생 퇴학시키기 ****\n");
		
		System.out.print("퇴학시킬 학생의 학번(STD_NO) 입력: ");
		int stdNo = sc.nextInt();
		sc.nextLine();
		
		System.out.print(stdNo + "번 학생을 정말로 퇴학시키겠습니까? (Y/N): ");
		String check = sc.nextLine();
		
		if (check.toUpperCase().equals("Y")) {

			int result = service.deleteStudent(stdNo);
			
			if(result > 0) {
				System.out.println(stdNo + "번 학생이 강제 퇴학당했습니다 ㄷㄷ...");
			} else {
				System.out.println("데이터 처리 중 문제가 발생했습니다.");
			}
		} else {
			System.out.println("이번만입니다...");
		}
	}

	private void selectByMajor() throws Exception{
		System.out.println("\n**** 5. 전공별 학생 조회하기 ****\n");
		
		System.out.print("조회할 전공 이름 입력 : ");
		String major = sc.next();
		
		List<Student> stdList = service.selectByMajor(major);
		
		if(stdList.isEmpty()) {
			System.out.println("해당 전공의 학생이 존재하지 않습니다");
			return;
		}

		System.out.println("\n**** [" + major + "] 전공 학생 목록 ****\n");
		System.out.println("\n--------------------------------------------------------------");
		
		for (Student std : stdList) {
			System.out.println(std);
		}
		System.out.println("--------------------------------------------------------------");

	}

}
