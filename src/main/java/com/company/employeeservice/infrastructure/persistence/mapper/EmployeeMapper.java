package com.company.employeeservice.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import com.company.employeeservice.domain.model.Employee;
import com.company.employeeservice.infrastructure.persistence.entity.EmployeeEntity;

@Component
public class EmployeeMapper {
	
	public EmployeeEntity toEntity(Employee employee) {
		
		if (employee == null) {
		    return null;
		}

	    return new EmployeeEntity(
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
	
	public Employee toDomain(EmployeeEntity entity) {
		
		if (entity == null) {
		    return null;
		}
		
		return new Employee(
		        entity.getId(),
		        entity.getFirstName(),
		        entity.getMiddleName(),
		        entity.getLastName(),
		        entity.getSecondLastName(),
		        entity.getAge(),
		        entity.getGender(),
		        entity.getBirthDate(),
		        entity.getPosition(),
		        entity.getCreatedAt(),
		        entity.isActive()
		    );
		
	}
	

}
