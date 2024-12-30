package com.demo.employeeservice.mapper;

import com.demo.employeeservice.dto.EmployeeDto;
import com.demo.employeeservice.entity.Employee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    EmployeeDto toDto(Employee employee);
    Employee toEntity(EmployeeDto dto);
}
