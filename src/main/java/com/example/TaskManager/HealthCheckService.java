package com.example.TaskManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.example.TaskManager.dao.TaskDao;

@Service
public class HealthCheckService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean checkDatabaseConnection() {
        try {
            Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            return result != null && result == 1;
        } catch (Exception e) {
            return false;
        }
    }
}
