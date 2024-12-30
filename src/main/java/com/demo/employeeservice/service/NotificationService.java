package com.demo.employeeservice.service;

import com.demo.employeeservice.entity.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

//@Slf4j
@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    @Async
    public void sendEmployeeCreatedNotification(Employee employee) {
        //Sending an email SendGrid or AWS SES
        try {
            Thread.sleep(1000);
            logger.info("Email sent to {} for employee creation", employee.getEmail());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Failed to send notification: {}", e.getMessage());
        }
    }
}
