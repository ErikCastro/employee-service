package com.company.employeeservice.infrastructure.adapter.output;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.company.employeeservice.domain.model.Employee;
import com.company.employeeservice.domain.port.output.EmployeeRepositoryPort;
import com.company.employeeservice.infrastructure.persistence.repository.EmployeeJpaRepository;
import com.company.employeeservice.infrastructure.persistence.entity.EmployeeEntity;
import com.company.employeeservice.infrastructure.persistence.mapper.EmployeeMapper;

@Repository
public class EmployeePersistenceAdapter implements EmployeeRepositoryPort {

    private final EmployeeJpaRepository employeeJpaRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeePersistenceAdapter(
            EmployeeJpaRepository employeeJpaRepository,
            EmployeeMapper employeeMapper) {
        this.employeeJpaRepository = employeeJpaRepository;
        this.employeeMapper = employeeMapper;
    }
    
    @Override
    public Employee save(Employee employee) {

        EmployeeEntity entity = employeeMapper.toEntity(employee);

        EmployeeEntity savedEntity = employeeJpaRepository.save(entity);

        return employeeMapper.toDomain(savedEntity);
    }
    
    @Override
    public Optional<Employee> findById(Long id) {

        return employeeJpaRepository.findById(id)
                .map(employeeMapper::toDomain);
    }
    
    @Override
    public List<Employee> findAll() {

        return employeeJpaRepository.findAll()
                .stream()
                .map(employeeMapper::toDomain)
                .toList();
    }
    
    @Override
    public void deleteById(Long id) {
    	employeeJpaRepository.deleteById(id);
    }
    
    @Override
    public boolean existsById(Long id) {
        return employeeJpaRepository.existsById(id);
    }
    
    @Override
    public List<Employee> findByName(String name) {

        return employeeJpaRepository.findByName(name)
                .stream()
                .map(employeeMapper::toDomain)
                .toList();
    }
    
    @Override
    public List<Employee> saveAll(List<Employee> employees) {

        List<EmployeeEntity> entities = employees.stream()
                .map(employeeMapper::toEntity)
                .toList();

        return employeeJpaRepository.saveAll(entities).stream()
                .map(employeeMapper::toDomain)
                .toList();
    }
    
}