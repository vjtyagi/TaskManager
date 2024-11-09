package com.example.TaskManager.dao;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.internal.jdbc.JdbcTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.*;

import com.example.TaskManager.entity.Task;

import jakarta.transaction.Transactional;

@ExtendWith(MockitoExtension.class)
public class TaskDaoTest {

    @Mock
    private NamedParameterJdbcTemplate jdbcTemplate;

    @InjectMocks
    private TaskDao taskDao;

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setTitle("Test Task");
        task.setDescription("This is a test task");
        task.setStatus("PENDING");
        task.setDueDate(LocalDate.now());
    }

    @Test
    public void testCreateTask_Success() {
        // mock the behaviour of jdbctemplate.update to return (indicating success)
        when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(1);

        // Call the createTask method and assert it returns true
        boolean result = taskDao.createTask(task);
        assertTrue(result);
        // Verify jdbcTemplate.update was called once with expected SQL parameters
        verify(jdbcTemplate, times(1)).update(eq(
                "INSERT INTO task (title, description, status, due_date) VALUES (:title, :description, :status, :dueDate)"),
                anyMap());
    }

    @Test
    public void testCreateTask_Failure() {
        // Mock jdbcTemplate.update to throw DataAccessException
        when(jdbcTemplate.update(anyString(), anyMap())).thenThrow(new DataAccessException("Failed to insert") {

        });
        // call createTask method and assert it returns false
        boolean result = taskDao.createTask(task);
        assertFalse(result);
        // Verify jdbcTemplate.update was called once
        verify(jdbcTemplate, times(1)).update(anyString(), anyMap());
    }

}
