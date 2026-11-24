package com.company.employeeservice.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.company.employeeservice.domain.port.output.EmployeeRepositoryPort;
import com.company.employeeservice.domain.model.Employee;
import com.company.employeeservice.domain.exception.EmployeeNotFoundException;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
	
	@Mock
    private EmployeeRepositoryPort employeeRepositoryPort;

    @InjectMocks
    private EmployeeService employeeService;
    
    @Test
    void shouldCreateEmployee() {

        Employee employee = new Employee();
        employee.setFirstName("Juan");
        employee.setLastName("Perez");

        when(employeeRepositoryPort.save(any(Employee.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Employee result = employeeService.createEmployee(employee);

        assertNotNull(result.getCreatedAt());
        assertTrue(result.isActive());
        assertEquals("Juan", result.getFirstName());

        verify(employeeRepositoryPort, times(1)).save(employee);
    }
    
    @Test
    void shouldThrowExceptionWhenEmployeeDoesNotExist() {

        when(employeeRepositoryPort.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                EmployeeNotFoundException.class,
                () -> employeeService.getEmployeeById(99L)
        );

        verify(employeeRepositoryPort, times(1)).findById(99L);
    }
    
    @Test
    void shouldUpdateEmployee() {

        Employee existingEmployee = new Employee();
        existingEmployee.setId(1L);
        existingEmployee.setFirstName("Juan");
        existingEmployee.setLastName("Perez");
        existingEmployee.setActive(true);

        Employee updateData = new Employee();
        updateData.setFirstName("Juan Carlos");
        updateData.setLastName("Garcia");

        when(employeeRepositoryPort.findById(1L))
                .thenReturn(Optional.of(existingEmployee));

        when(employeeRepositoryPort.save(any(Employee.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Employee result =
                employeeService.updateEmployee(1L, updateData);

        assertEquals(1L, result.getId());
        assertEquals("Juan Carlos", result.getFirstName());
        assertEquals("Garcia", result.getLastName());
        assertTrue(result.isActive());

        verify(employeeRepositoryPort).findById(1L);
        verify(employeeRepositoryPort).save(existingEmployee);
    }
    
    @Test
    void shouldDeleteEmployee() {

        when(employeeRepositoryPort.existsById(1L))
                .thenReturn(true);

        employeeService.deleteEmployee(1L);

        verify(employeeRepositoryPort, times(1)).existsById(1L);
        verify(employeeRepositoryPort, times(1)).deleteById(1L);
    }
    
    @Test
    void shouldThrowExceptionWhenDeletingNonExistingEmployee() {

        when(employeeRepositoryPort.existsById(99L))
                .thenReturn(false);

        assertThrows(
                EmployeeNotFoundException.class,
                () -> employeeService.deleteEmployee(99L)
        );

        verify(employeeRepositoryPort, times(1)).existsById(99L);

        verify(employeeRepositoryPort, never()).deleteById(99L);
    }
    
    @Test
    void shouldFindEmployeesByName() {

        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("Juan");

        when(employeeRepositoryPort.findByName("Juan"))
                .thenReturn(List.of(employee));

        List<Employee> result =
                employeeService.findEmployeesByName("Juan");

        assertEquals(1, result.size());
        assertEquals("Juan", result.get(0).getFirstName());

        verify(employeeRepositoryPort, times(1)).findByName("Juan");
    }
    
    @Test
    void shouldGetAllEmployees() {

        Employee employee1 = new Employee();
        employee1.setId(1L);
        employee1.setFirstName("Juan");

        Employee employee2 = new Employee();
        employee2.setId(2L);
        employee2.setFirstName("Carlos");

        when(employeeRepositoryPort.findAll())
                .thenReturn(List.of(employee1, employee2));

        List<Employee> result = employeeService.getAllEmployees();

        assertEquals(2, result.size());
        assertEquals("Juan", result.get(0).getFirstName());
        assertEquals("Carlos", result.get(1).getFirstName());

        verify(employeeRepositoryPort, times(1)).findAll();
    }
    
    @Test
    void shouldCreateMultipleEmployees() {

        Employee employee1 = new Employee();
        employee1.setFirstName("Juan");

        Employee employee2 = new Employee();
        employee2.setFirstName("Carlos");

        List<Employee> employees = List.of(employee1, employee2);

        when(employeeRepositoryPort.saveAll(employees))
                .thenReturn(employees);

        List<Employee> result =
                employeeService.createEmployees(employees);

        assertEquals(2, result.size());
        assertTrue(result.get(0).isActive());
        assertTrue(result.get(1).isActive());

        assertNotNull(result.get(0).getCreatedAt());
        assertNotNull(result.get(1).getCreatedAt());

        verify(employeeRepositoryPort, times(1)).saveAll(employees);
    }

}
