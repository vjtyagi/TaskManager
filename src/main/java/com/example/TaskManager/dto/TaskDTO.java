package com.example.TaskManager.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class TaskDTO {

    @NotNull(message = "Title cannot be null")
    @NotBlank(message = "Title cannot be blank")
    @Size(max = 255, message = "Title cannot exceed 255 characters")
    private String title;

    private String description;

    @NotBlank(message = "Status cannot be blank")
    @Pattern(regexp = "PENDING|IN_PROGRESS|COMPLETED", message = "Status must be either PENDING, IN_PROGRESS OR  COMPLETED")
    private String status;

    @NotNull(message = "Due date is required")
    private LocalDate dueDate;

    public TaskDTO() {
    }

    public TaskDTO(
            @NotNull(message = "Title cannot be null") @NotBlank(message = "Title cannot be blank") @Size(max = 255, message = "Title cannot exceed 255 characters") String title,
            String description,
            @NotBlank(message = "Status cannot be blank") @Pattern(regexp = "PENDING|IN_PROGRESS|COMPLETED", message = "Status must be either PENDING, IN_PROGRESS OR  COMPLETED") String status,
            @NotNull(message = "Due date is required") LocalDate dueDate) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.dueDate = dueDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

}
