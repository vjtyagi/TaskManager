package com.example.TaskManager.service;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cglib.core.Local;

import com.example.TaskManager.dao.TaskDao;
import com.example.TaskManager.dto.TaskDTO;
import com.example.TaskManager.dto.TaskResponseDTO;
import com.example.TaskManager.entity.Task;
import com.example.TaskManager.exception.TaskUpdateException;
import com.example.TaskManager.service.TaskService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TaskServiceTest {
    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskDao taskDao;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTask() {
        Task task = new Task(1L, "Test Task", "Description", "PENDING", LocalDate.now().plusDays(7));
        when(taskDao.createTask(task)).thenReturn(true);
        boolean result = taskService.createTask(task);
        assertTrue(result);
        verify(taskDao, times(1)).createTask(task);
    }

    @Test
    public void getAllTasks() {
        Task task1 = new Task(1L, "Task 1", "Desciption 1", "PENDING", LocalDate.now().plusDays(7));
        Task task2 = new Task(1L, "Task 2", "Desciption 2", "COMPLETED", LocalDate.now().plusDays(7));
        when(taskDao.getAllTasks()).thenReturn(Arrays.asList(task1, task2));
        List<TaskResponseDTO> tasks = taskService.getAllTasks();
        assertEquals(2, tasks.size());
        verify(taskDao, times(1)).getAllTasks();
    }

    @Test
    public void getTaskById() {
        Task task = new Task(1L, "Test Task", "Description", "PENDING", LocalDate.now().plusDays(7));
        when(taskDao.getTaskById(1L)).thenReturn(task);
        TaskResponseDTO taskResponseDTO = taskService.getTaskById(1L);
        assertNotNull(taskResponseDTO);
        assertEquals("Test Task", taskResponseDTO.getTitle());
        verify(taskDao, times(1)).getTaskById(1L);
    }

    @Test
    public void updateTaskById() {
        TaskDTO taskDTO = new TaskDTO("Updated Task", "Updated Description", "IN_PROGRESS",
                LocalDate.now().plusDays(7));
        Task updatedTask = new Task(1L, "Updated Task", "Updated Description", "IN_PROGRESS",
                LocalDate.now().plusDays(7));
        when(taskDao.updateTaskById(1L, taskDTO)).thenReturn(updatedTask);
        TaskResponseDTO taskResponseDTO = taskService.updateTaskById(1L, taskDTO);
        assertNotNull(taskResponseDTO);
        assertEquals("Updated Task", taskResponseDTO.getTitle());
        verify(taskDao, times(1)).updateTaskById(1L, taskDTO);
    }

    @Test
    public void testDeleteTask() {
        when(taskDao.deleteTask(1L)).thenReturn(true);
        boolean result = taskService.deleteTask(1L);
        assertTrue(result);
        verify(taskDao, times(1)).deleteTask(1L);
    }

    @Test
    public void testUpdatedTaskById_NotFound() {
        TaskDTO taskDTO = new TaskDTO("Updated Task", "Updated Description", "IN_PROGRESS",
                LocalDate.now().plusDays(7));
        when(taskDao.updateTaskById(1L, taskDTO)).thenReturn(null);
        TaskUpdateException thrown = assertThrows(TaskUpdateException.class, () -> {
            taskService.updateTaskById(1L, taskDTO);
        });
        assertEquals("Failed to update task with id 1", thrown.getMessage());
    }

    @Test
    public void testGetAllTasks_EmptyList() {
        when(taskDao.getAllTasks()).thenReturn(Collections.emptyList());
        List<TaskResponseDTO> tasks = taskService.getAllTasks();
        assertTrue(tasks.isEmpty());
        verify(taskDao, times(1)).getAllTasks();
    }

}
