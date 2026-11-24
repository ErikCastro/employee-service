package com.company.employeeservice.infrastructure.adapter.input.dto;

import java.time.LocalDate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

public class EmployeeRequest {
	
	@NotBlank(message = "First name is required")
	private String firstName;

	private String middleName;

	@NotBlank(message = "Last name is required")
	private String lastName;

	private String secondLastName;

	@NotNull(message = "Age is required")
	@Min(value = 0, message = "Age cannot be negative")
	private Integer age;

	@NotBlank(message = "Gender is required")
	private String gender;

	@NotNull(message = "Birth date is required")
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate birthDate;

	@NotBlank(message = "Position is required")
	private String position;
	
	public EmployeeRequest() {
		
	}

	public EmployeeRequest(String firstName, String middleName, String lastName, String secondLastName, Integer age,
			String gender, LocalDate birthDate, String position) {
		super();
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.secondLastName = secondLastName;
		this.age = age;
		this.gender = gender;
		this.birthDate = birthDate;
		this.position = position;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getSecondLastName() {
		return secondLastName;
	}

	public void setSecondLastName(String secondLastName) {
		this.secondLastName = secondLastName;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

}
