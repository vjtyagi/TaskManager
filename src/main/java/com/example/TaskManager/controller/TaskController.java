package com.example.TaskManager.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.TaskManager.dto.ApiResponse;
import com.example.TaskManager.dto.TaskDTO;
import com.example.TaskManager.dto.TaskResponseDTO;
import com.example.TaskManager.entity.Task;
import com.example.TaskManager.exception.TaskNotFoundException;
import com.example.TaskManager.exception.TaskUpdateException;
import com.example.TaskManager.service.TaskService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class TaskController {
    @Autowired
    private TaskService taskService;

    @PostMapping("/tasks")
    public ResponseEntity<ApiResponse<String>> createTask(@Valid @RequestBody TaskDTO taskDTO) {
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setStatus(taskDTO.getStatus());
        task.setDueDate(taskDTO.getDueDate());
        boolean result = taskService.createTask(task);
        if (result) {
            return ResponseEntity.ok(ApiResponse.success("Task created succesfully"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.failure("Failed to create task"));
        }
    }

    @GetMapping("/tasks")
    public ResponseEntity<ApiResponse<List<TaskResponseDTO>>> getAllTasks() {
        List<TaskResponseDTO> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(ApiResponse.success(tasks));
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<ApiResponse<TaskResponseDTO>> getTaskById(@PathVariable("id") Long id) {
        TaskResponseDTO task = taskService.getTaskById(id);
        if (task != null) {
            return ResponseEntity.ok(ApiResponse.success(task));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.failure("Task not found"));
        }
    }

    @PatchMapping("/tasks/{id}")
    public ResponseEntity<ApiResponse<TaskResponseDTO>> updateTaskById(@PathVariable("id") Long id,
            @RequestBody TaskDTO taskDTO) {
        TaskResponseDTO taskResponseDTO = taskService.updateTaskById(id, taskDTO);
        if (taskResponseDTO != null) {
            return ResponseEntity.ok(ApiResponse.success(taskResponseDTO));
        } else {
            throw new TaskNotFoundException("Task not found");
        }
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<ApiResponse<String>> deleteTaskById(@PathVariable("id") Long id) {
        boolean result = taskService.deleteTask(id);
        if (result) {
            return ResponseEntity.ok(ApiResponse.success("Task deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.failure("Failed to delete task with id: " + id));
        }
    }

}
