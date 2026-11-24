package com.company.employeeservice.domain.port.input;

import java.util.List;

import com.company.employeeservice.domain.model.Employee;

public interface EmployeeServicePort {

    Employee createEmployee(Employee employee);

    List<Employee> getAllEmployees();

    Employee getEmployeeById(Long id);

    List<Employee> findEmployeesByName(String name);

    Employee updateEmployee(Long id, Employee employee);

    void deleteEmployee(Long id);

    List<Employee> createEmployees(List<Employee> employees);
}