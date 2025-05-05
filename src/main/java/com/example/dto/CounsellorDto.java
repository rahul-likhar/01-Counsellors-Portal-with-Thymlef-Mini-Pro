package com.example.dto;

import lombok.Data;

@Data
//@Builder
public class CounsellorDto {

	private Integer counsellorId;
	private String name;
	private String email;
	private String pwd;
	private Long phno;

}
