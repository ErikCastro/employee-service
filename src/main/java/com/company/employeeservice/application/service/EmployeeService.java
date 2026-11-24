package com.company.employeeservice.application.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.company.employeeservice.domain.exception.EmployeeNotFoundException;
import com.company.employeeservice.domain.model.Employee;
import com.company.employeeservice.domain.port.input.EmployeeServicePort;
import com.company.employeeservice.domain.port.output.EmployeeRepositoryPort;

@Service
public class EmployeeService implements EmployeeServicePort {

    private final EmployeeRepositoryPort employeeRepositoryPort;

    public EmployeeService(EmployeeRepositoryPort employeeRepositoryPort) {
        this.employeeRepositoryPort = employeeRepositoryPort;
    }
    
    @Override
    public Employee createEmployee(Employee employee) {
    	initializeEmployee(employee);
        return employeeRepositoryPort.save(employee);
    }
    
    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepositoryPort.findAll();
    }
    
    @Override
    public Employee getEmployeeById(Long id) {
        return employeeRepositoryPort.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(
                		"Employee not found with id: " + id));
    }
    
    @Override
    public List<Employee> findEmployeesByName(String name) {
        return employeeRepositoryPort.findByName(name);
    }
    
    @Override
    public Employee updateEmployee(Long id, Employee employee) {

        Employee existingEmployee = employeeRepositoryPort.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(
                        "Employee not found with id: " + id));

        copyEmployeeData(employee, existingEmployee);

        return employeeRepositoryPort.save(existingEmployee);
    }
    
    @Override
    public void deleteEmployee(Long id) {

        if (!employeeRepositoryPort.existsById(id)) {
            throw new EmployeeNotFoundException(
                    "Employee not found with id: " + id);
        }

        employeeRepositoryPort.deleteById(id);
    }
    
    @Override
    public List<Employee> createEmployees(List<Employee> employees) {
    	
    	employees.forEach(this::initializeEmployee);
    	
        return employeeRepositoryPort.saveAll(employees);
    }
    
    private void initializeEmployee(Employee employee) {
        employee.setCreatedAt(LocalDateTime.now());
        employee.setActive(true);
    }
    
    private void copyEmployeeData(Employee source, Employee target) {
        target.setFirstName(source.getFirstName());
        target.setMiddleName(source.getMiddleName());
        target.setLastName(source.getLastName());
        target.setSecondLastName(source.getSecondLastName());
        target.setAge(source.getAge());
        target.setGender(source.getGender());
        target.setBirthDate(source.getBirthDate());
        target.setPosition(source.getPosition());
    }
    
}