package com.company.employeeservice.infrastructure.adapter.input;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.Validator;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import com.company.employeeservice.domain.port.input.EmployeeServicePort;
import com.company.employeeservice.domain.model.Employee;
import com.company.employeeservice.infrastructure.adapter.input.dto.EmployeeRequest;
import com.company.employeeservice.infrastructure.adapter.input.dto.EmployeeResponse;
import com.company.employeeservice.infrastructure.adapter.input.mapper.EmployeeDtoMapper;

@Tag(name = "Employees", description = "API for employee management")
@RestController
@RequestMapping("/employees")
public class EmployeeController {

	private final EmployeeServicePort employeeServicePort;
	private final EmployeeDtoMapper employeeDtoMapper;
	private final ObjectMapper objectMapper;
	private final Validator validator;

	public EmployeeController(EmployeeServicePort employeeServicePort, EmployeeDtoMapper employeeDtoMapper,
			ObjectMapper objectMapper, Validator validator) {
		this.employeeServicePort = employeeServicePort;
		this.employeeDtoMapper = employeeDtoMapper;
		this.objectMapper = objectMapper;
		this.validator = validator;
	}

	@Operation(summary = "Create employee or employees", description = "Creates one employee or multiple employees")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Employee or employees created successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid request or validation error") })
	@PostMapping
	public ResponseEntity<?> createEmployee(@RequestBody JsonNode body) {

		if (body.isArray()) {

			if (body.isEmpty()) {
				throw new IllegalArgumentException("Employee list cannot be empty");
			}

			List<EmployeeRequest> requests = objectMapper.convertValue(body,
					new TypeReference<List<EmployeeRequest>>() {
					});

			requests.forEach(this::validateEmployeeRequest);

			List<Employee> employees = requests.stream().map(employeeDtoMapper::toDomain).toList();

			List<Employee> savedEmployees = employeeServicePort.createEmployees(employees);

			List<EmployeeResponse> responses = savedEmployees.stream().map(employeeDtoMapper::toResponse).toList();

			return ResponseEntity.status(HttpStatus.CREATED).body(responses);

		} else if (body.isObject()) {
			EmployeeRequest request = objectMapper.convertValue(body, EmployeeRequest.class);

			validateEmployeeRequest(request);

			Employee employee = employeeDtoMapper.toDomain(request);

			Employee savedEmployee = employeeServicePort.createEmployee(employee);

			EmployeeResponse response = employeeDtoMapper.toResponse(savedEmployee);

			return ResponseEntity.status(HttpStatus.CREATED).body(response);

		} else {
			throw new IllegalArgumentException("Invalid request format");
		}

	}

	private void validateEmployeeRequest(EmployeeRequest request) {

		Set<ConstraintViolation<EmployeeRequest>> violations = validator.validate(request);

		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
	}

	@Operation(summary = "Get all employees", description = "Retrieves all registered employees")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Employees retrieved successfully") })
	@GetMapping
	public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {

		List<Employee> employees = employeeServicePort.getAllEmployees();

		List<EmployeeResponse> responses = employees.stream().map(employeeDtoMapper::toResponse).toList();

		return ResponseEntity.ok(responses);
	}

	@Operation(summary = "Get employee by ID", description = "Retrieves an employee using its unique identifier")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Employee found successfully"),
			@ApiResponse(responseCode = "404", description = "Employee not found") })
	@GetMapping("/{id}")
	public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable Long id) {

		Employee employee = employeeServicePort.getEmployeeById(id);

		EmployeeResponse response = employeeDtoMapper.toResponse(employee);

		return ResponseEntity.ok(response);
	}

	@Operation(summary = "Search employees by name", description = "Searches employees using a complete or partial name")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Search completed successfully") })
	@GetMapping("/search")
	public ResponseEntity<List<EmployeeResponse>> findEmployeesByName(@RequestParam String name) {

		List<Employee> employees = employeeServicePort.findEmployeesByName(name);

		List<EmployeeResponse> responses = employees.stream().map(employeeDtoMapper::toResponse).toList();

		return ResponseEntity.ok(responses);
	}

	@Operation(summary = "Update employee", description = "Updates an existing employee using its unique identifier")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Employee updated successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid employee data"),
			@ApiResponse(responseCode = "404", description = "Employee not found") })
	@PutMapping("/{id}")
	public ResponseEntity<EmployeeResponse> updateEmployee(@PathVariable Long id,
			@Valid @RequestBody EmployeeRequest request) {

		Employee employee = employeeDtoMapper.toDomain(request);

		Employee updatedEmployee = employeeServicePort.updateEmployee(id, employee);

		EmployeeResponse response = employeeDtoMapper.toResponse(updatedEmployee);

		return ResponseEntity.ok(response);
	}

	@Operation(summary = "Delete employee", description = "Deletes an employee using its unique identifier")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Employee deleted successfully"),
			@ApiResponse(responseCode = "404", description = "Employee not found") })
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {

		employeeServicePort.deleteEmployee(id);

		return ResponseEntity.noContent().build();
	}
}
