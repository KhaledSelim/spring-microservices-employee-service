package com.demo.employeeservice.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//@Slf4j
@Component
public class AuditLogger {

    private static final Logger logger = LoggerFactory.getLogger(AuditLogger.class);
    public void log(String message) {
        logger.info("[AUDIT] {}", message);
    }
}
