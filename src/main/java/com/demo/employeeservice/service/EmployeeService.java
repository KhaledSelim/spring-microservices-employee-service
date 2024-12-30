package com.demo.employeeservice.service;

import com.demo.employeeservice.exception.EmployeeNotFoundException;
import com.demo.employeeservice.exception.InvalidInputException;
import com.demo.employeeservice.dto.EmployeeDto;
import com.demo.employeeservice.entity.Employee;
import com.demo.employeeservice.external.ValidateDepartmentAPI;
import com.demo.employeeservice.external.ValidateEmailAPI;
import com.demo.employeeservice.logging.AuditLogger;
import com.demo.employeeservice.mapper.EmployeeMapper;
import com.demo.employeeservice.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

//@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final ValidateDepartmentAPI validateDepartmentAPI;
    private final ValidateEmailAPI validateEmailAPI;
    private final NotificationService notificationService;
    private final AuditLogger auditLogger;

    public EmployeeDto createEmployee(EmployeeDto dto) {
        auditLogger.log("Starting employee creation...");

        boolean validEmail = validateEmailAPI.validateEmail(dto.getEmail());
        if (!validEmail) {
            throw new InvalidInputException("Invalid Email");
        }

        boolean validDept = validateDepartmentAPI.validateDepartment(dto.getDepartment());
        if (!validDept) {
            throw new InvalidInputException("Invalid Department");
        }

        Employee entity = employeeMapper.toEntity(dto);
        Employee saved = employeeRepository.save(entity);
        auditLogger.log("Employee created with ID: " + saved.getId());

        notificationService.sendEmployeeCreatedNotification(saved);

        return employeeMapper.toDto(saved);
    }

    public EmployeeDto getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));
        return employeeMapper.toDto(employee);
    }

    public EmployeeDto updateEmployee(Long id, EmployeeDto dto) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));

        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setEmail(dto.getEmail());
        existing.setDepartment(dto.getDepartment());
        existing.setSalary(dto.getSalary());

        Employee updated = employeeRepository.save(existing);
        return employeeMapper.toDto(updated);
    }

    public void deleteEmployee(Long id) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));
        employeeRepository.delete(existing);
    }

    public List<EmployeeDto> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(employeeMapper::toDto)
                .toList();
    }
}