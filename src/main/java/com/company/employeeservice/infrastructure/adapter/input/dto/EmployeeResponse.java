package com.company.employeeservice.infrastructure.adapter.input.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class EmployeeResponse {
	
	private Long id;
	private String firstName;
	private String middleName;
	private String lastName;
	private String secondLastName;
	private Integer age;
	private String gender;
	private LocalDate birthDate;
	private String position;
	private LocalDateTime createdAt;
	private boolean active;
	
	public EmployeeResponse () {
		
	}

	public EmployeeResponse(Long id, String firstName, String middleName, String lastName, String secondLastName,
			Integer age, String gender, LocalDate birthDate, String position, LocalDateTime createdAt, boolean active) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.secondLastName = secondLastName;
		this.age = age;
		this.gender = gender;
		this.birthDate = birthDate;
		this.position = position;
		this.createdAt = createdAt;
		this.active = active;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
}
