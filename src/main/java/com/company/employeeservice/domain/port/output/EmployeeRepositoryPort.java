package com.company.employeeservice.domain.port.output;

import java.util.List;
import java.util.Optional;

import com.company.employeeservice.domain.model.Employee;

public interface EmployeeRepositoryPort {

    Employee save(Employee employee);

    Optional<Employee> findById(Long id);

    List<Employee> findAll();

    List<Employee> findByName(String name);

    void deleteById(Long id);

    boolean existsById(Long id);
    
    List<Employee> saveAll(List<Employee> employees);
}