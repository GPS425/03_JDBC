package edu.kh.jdbc.homework.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor	// 기본 생성자
@AllArgsConstructor	// 모든 매개변수 포함 생성자



public class Student {
	private int stdNo;
	private String stdName;
	private int stdAge;
	private String major;
	private String entDate;
	
	@Override
	public String toString() {
	    return String.format("| %-1d | %-4s | %-2d | %-11s | %-11s |", 
	            stdNo, stdName, stdAge, major, entDate);
	}
}

