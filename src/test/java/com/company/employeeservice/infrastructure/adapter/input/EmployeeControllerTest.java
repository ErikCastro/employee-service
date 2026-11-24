package com.company.employeeservice.infrastructure.adapter.input;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.ArgumentMatchers.eq;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.company.employeeservice.domain.model.Employee;
import com.company.employeeservice.domain.port.input.EmployeeServicePort;
import com.company.employeeservice.infrastructure.adapter.input.dto.EmployeeRequest;
import com.company.employeeservice.infrastructure.adapter.input.dto.EmployeeResponse;
import com.company.employeeservice.infrastructure.adapter.input.mapper.EmployeeDtoMapper;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {
	
	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeServicePort employeeServicePort;

    @MockBean
    private EmployeeDtoMapper employeeDtoMapper;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void shouldCreateEmployee() throws Exception {

        EmployeeRequest request = new EmployeeRequest();
        request.setFirstName("Juan");
        request.setLastName("Perez");
        request.setAge(30);
        request.setGender("Male");
        request.setBirthDate(LocalDate.of(1996, 5, 15));
        request.setPosition("Java Developer");

        Employee employee = new Employee();
        employee.setFirstName("Juan");
        employee.setLastName("Perez");

        EmployeeResponse response = new EmployeeResponse();
        response.setId(1L);
        response.setFirstName("Juan");
        response.setLastName("Perez");

        when(employeeDtoMapper.toDomain(any(EmployeeRequest.class)))
                .thenReturn(employee);

        when(employeeServicePort.createEmployee(any(Employee.class)))
                .thenReturn(employee);

        when(employeeDtoMapper.toResponse(any(Employee.class)))
                .thenReturn(response);

        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Juan"));
    }
    
    @Test
    void shouldCreateMultipleEmployees() throws Exception {

        EmployeeRequest request1 = new EmployeeRequest();
        request1.setFirstName("Juan");
        request1.setLastName("Perez");
        request1.setAge(30);
        request1.setGender("Male");
        request1.setBirthDate(LocalDate.of(1996, 5, 15));
        request1.setPosition("Developer");

        EmployeeRequest request2 = new EmployeeRequest();
        request2.setFirstName("Maria");
        request2.setLastName("Lopez");
        request2.setAge(28);
        request2.setGender("Female");
        request2.setBirthDate(LocalDate.of(1998, 3, 10));
        request2.setPosition("Tester");

        Employee employee1 = new Employee();
        employee1.setFirstName("Juan");

        Employee employee2 = new Employee();
        employee2.setFirstName("Maria");

        EmployeeResponse response1 = new EmployeeResponse();
        response1.setId(1L);
        response1.setFirstName("Juan");

        EmployeeResponse response2 = new EmployeeResponse();
        response2.setId(2L);
        response2.setFirstName("Maria");

        when(employeeDtoMapper.toDomain(any(EmployeeRequest.class)))
                .thenReturn(employee1, employee2);

        when(employeeServicePort.createEmployees(anyList()))
                .thenReturn(List.of(employee1, employee2));

        when(employeeDtoMapper.toResponse(employee1))
                .thenReturn(response1);

        when(employeeDtoMapper.toResponse(employee2))
                .thenReturn(response2);

        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        List.of(request1, request2))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("Juan"))
                .andExpect(jsonPath("$[1].firstName").value("Maria"));
    }
    
    @Test
    void shouldReturnBadRequestWhenEmployeeIsInvalid() throws Exception {

        String requestJson = """
                {
                    "firstName": "",
                    "lastName": "Perez",
                    "age": 30,
                    "gender": "Male",
                    "birthDate": "15-05-1996",
                    "position": "Developer"
                }
                """;

        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void shouldGetEmployeeById() throws Exception {

        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("Juan");
        employee.setLastName("Perez");

        EmployeeResponse response = new EmployeeResponse();
        response.setId(1L);
        response.setFirstName("Juan");
        response.setLastName("Perez");

        when(employeeServicePort.getEmployeeById(1L))
                .thenReturn(employee);

        when(employeeDtoMapper.toResponse(employee))
                .thenReturn(response);

        mockMvc.perform(get("/employees/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Juan"));

        verify(employeeServicePort, times(1)).getEmployeeById(1L);
    }
    
    @Test
    void shouldGetAllEmployees() throws Exception {

        Employee employee1 = new Employee();
        employee1.setId(1L);
        employee1.setFirstName("Juan");

        Employee employee2 = new Employee();
        employee2.setId(2L);
        employee2.setFirstName("Carlos");

        EmployeeResponse response1 = new EmployeeResponse();
        response1.setId(1L);
        response1.setFirstName("Juan");

        EmployeeResponse response2 = new EmployeeResponse();
        response2.setId(2L);
        response2.setFirstName("Carlos");

        when(employeeServicePort.getAllEmployees())
                .thenReturn(List.of(employee1, employee2));

        when(employeeDtoMapper.toResponse(employee1))
                .thenReturn(response1);

        when(employeeDtoMapper.toResponse(employee2))
                .thenReturn(response2);

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("Juan"))
                .andExpect(jsonPath("$[1].firstName").value("Carlos"));
    }
    
    @Test
    void shouldFindEmployeesByName() throws Exception {

        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("Juan");

        EmployeeResponse response = new EmployeeResponse();
        response.setId(1L);
        response.setFirstName("Juan");

        when(employeeServicePort.findEmployeesByName("Juan"))
                .thenReturn(List.of(employee));

        when(employeeDtoMapper.toResponse(employee))
                .thenReturn(response);

        mockMvc.perform(get("/employees/search")
                .param("name", "Juan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Juan"));

        verify(employeeServicePort, times(1)).findEmployeesByName("Juan");
    }
    
    @Test
    void shouldUpdateEmployee() throws Exception {

        Long id = 1L;

        EmployeeRequest request = new EmployeeRequest();
        request.setFirstName("Carlos");
        request.setLastName("Lopez");
        request.setAge(30);
        request.setGender("Masculino");
        request.setBirthDate(LocalDate.of(1996, 5, 10));
        request.setPosition("Backend Developer");

        Employee employee = new Employee();
        employee.setId(id);
        employee.setFirstName("Carlos");

        EmployeeResponse response = new EmployeeResponse();
        response.setId(id);
        response.setFirstName("Carlos");

        when(employeeDtoMapper.toDomain(any(EmployeeRequest.class)))
                .thenReturn(employee);

        when(employeeServicePort.updateEmployee(eq(id), any(Employee.class)))
                .thenReturn(employee);

        when(employeeDtoMapper.toResponse(employee))
                .thenReturn(response);

        mockMvc.perform(put("/employees/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Carlos"));

        verify(employeeServicePort, times(1))
                .updateEmployee(eq(id), any(Employee.class));
    }
    
    @Test
    void shouldDeleteEmployee() throws Exception {

        Long id = 1L;

        doNothing().when(employeeServicePort).deleteEmployee(id);

        mockMvc.perform(delete("/employees/{id}", id))
                .andExpect(status().isNoContent());

        verify(employeeServicePort, times(1)).deleteEmployee(id);
    }

}
