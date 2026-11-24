package com.company.employeeservice.infrastructure.adapter.input.mapper;

import org.springframework.stereotype.Component;

import com.company.employeeservice.domain.model.Employee;
import com.company.employeeservice.infrastructure.adapter.input.dto.EmployeeRequest;
import com.company.employeeservice.infrastructure.adapter.input.dto.EmployeeResponse;

@Component
public class EmployeeDtoMapper {
	
	public Employee toDomain(EmployeeRequest request) {

	    return new Employee(
	            null,
	            request.getFirstName(),
	            request.getMiddleName(),
	            request.getLastName(),
	            request.getSecondLastName(),
	            request.getAge(),
	            request.getGender(),
	            request.getBirthDate(),
	            request.getPosition(),
	            null,
	            true
	    );
	}
	
	public EmployeeResponse toResponse(Employee employee) {

	    return new EmployeeResponse(
	            employee.getId(),
	            employee.getFirstName(),
	            employee.getMiddleName(),
	            employee.getLastName(),
	            employee.getSecondLastName(),
	            employee.getAge(),
	            employee.getGender(),
	            employee.getBirthDate(),
	            employee.getPosition(),
	            employee.getCreatedAt(),
	            employee.isActive()
	    );
	}

}
