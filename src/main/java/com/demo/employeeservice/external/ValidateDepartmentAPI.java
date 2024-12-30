package com.demo.employeeservice.external;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ValidateDepartmentAPI {

    private static final Logger logger = LoggerFactory.getLogger(ValidateDepartmentAPI.class);
    @CircuitBreaker(name = "externalApiService", fallbackMethod = "fallbackValidateDepartment")
    @RateLimiter(name = "externalApiService")
    public boolean validateDepartment(String department) {
        //Calling a third party API
        logger.info("Calling external department validation API for: {}", department);
        //Sample
        if (department == null || department.isEmpty()) {
            throw new RuntimeException("Third party API response : invalid department");
        }
        return switch (department) {
            case "HR", "FinTech", "IT" -> true;
            default -> throw new RuntimeException("Department not found by the third party API");
        };
    }

    public boolean fallbackValidateDepartment(String department, Throwable t) {
        logger.error("Fallback for department validation: {}", t.getMessage());
        //Return either false or a default department
        return false;
    }
}
