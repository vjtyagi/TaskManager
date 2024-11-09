package com.example.TaskManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class HealthCheckController {
    @Autowired
    private HealthCheckService healthCheckService;

    @GetMapping("/health-check")
    public String getMethodName() {
        return healthCheckService.checkDatabaseConnection() ? "Database connection successful"
                : "Database connection failed";
    }

}
