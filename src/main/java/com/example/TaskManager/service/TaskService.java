package com.example.TaskManager.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.TaskManager.dao.TaskDao;
import com.example.TaskManager.dto.TaskDTO;
import com.example.TaskManager.dto.TaskResponseDTO;
import com.example.TaskManager.entity.Task;
import com.example.TaskManager.exception.TaskDeleteException;
import com.example.TaskManager.exception.TaskNotFoundException;
import com.example.TaskManager.exception.TaskUpdateException;

@Service
public class TaskService {
    @Autowired
    private TaskDao taskDao;

    public boolean createTask(Task task) {
        try {
            boolean result = taskDao.createTask(task);
            return result;
        } catch (RuntimeException e) {
            System.err.println("failed to create task " + e.getMessage());
            return false;
        }
    }

    public List<TaskResponseDTO> getAllTasks() {
        try {
            List<Task> tasks = taskDao.getAllTasks();
            List<TaskResponseDTO> tasksList = new ArrayList<>();
            for (Task task : tasks) {
                TaskResponseDTO taskResponseDTO = TaskResponseDTO.fromTask(task);
                tasksList.add(taskResponseDTO);
            }
            return tasksList;
        } catch (RuntimeException e) {
            System.err.println("Failed to fetch tasks " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public TaskResponseDTO getTaskById(Long id) {
        try {
            Task task = taskDao.getTaskById(id);
            if (task != null) {
                TaskResponseDTO taskResponseDTO = TaskResponseDTO.fromTask(task);
                return taskResponseDTO;
            } else {
                return null;
            }
        } catch (RuntimeException e) {
            System.err.println("Failed to get task " + e.getMessage());
            return null;
        }
    }

    public TaskResponseDTO updateTaskById(Long id, TaskDTO taskDTO) {

        if (taskDTO == null) {
            throw new IllegalArgumentException("TaskDTO cannot be null");
        }
        try {
            Task updatedTask = taskDao.updateTaskById(id, taskDTO);
            if (updatedTask == null) {
                throw new TaskNotFoundException("No Task found with ID " + id);
            }
            return TaskResponseDTO.fromTask(updatedTask);
        } catch (Exception e) {
            System.err.println("Error updating task with Id: " + id + " : " + e.getMessage());
            throw new TaskUpdateException("Failed to update task with id " + id);
        }

    }

    public boolean deleteTask(Long id) {
        try {
            boolean result = taskDao.deleteTask(id);
            return result;
        } catch (Exception e) {
            System.err.println("Error deleting task with Id: " + id + " : " + e.getMessage());
            return false;
        }
    }
}
