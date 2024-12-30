package com.demo.employeeservice.external;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

//@Slf4j
@Service
public class ValidateEmailAPI {

    private static final Logger logger = LoggerFactory.getLogger(ValidateEmailAPI.class);
    @CircuitBreaker(name = "externalApiService", fallbackMethod = "fallbackValidateEmail")
    @RateLimiter(name = "externalApiService")
    public boolean validateEmail(String email) {
        //Calling a third party API Mailgun or ZeroBounce
        logger.info("Calling third party email validation API for: {}", email);
        //Sample
        if (email == null || !email.contains("@")) {
            throw new RuntimeException("Mailgun service response : invalid email");
        }
        return true;
    }

    public boolean fallbackValidateEmail(String email, Throwable t) {
        logger.error("Fallback for email validation: {}", t.getMessage());
        //Return either false or a default value
        return false;
    }

}
