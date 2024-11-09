package com.example.TaskManager.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import com.example.TaskManager.dto.TaskDTO;
import com.example.TaskManager.entity.Task;

import jakarta.transaction.Transactional;

@Repository
public class TaskDao {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public boolean createTask(Task task) {
        String sql = "INSERT INTO task (title, description, status, due_date) VALUES (:title, :description, :status, :dueDate)";
        Map<String, Object> params = Map.of(
                "title", task.getTitle(),
                "description", task.getDescription(),
                "status", task.getStatus(),
                "dueDate", task.getDueDate());
        try {
            jdbcTemplate.update(sql, params);
            return true;
        } catch (DataAccessException e) {
            System.err.println("Error saving task " + e.getMessage());
            return false;
        }
    }

    public List<Task> getAllTasks() {
        String sql = "SELECT id, title, description, status, due_date, created_at, updated_at FROM task";
        try {

            return jdbcTemplate.query(sql, new TaskRowMapper());
        } catch (DataAccessException e) {
            System.err.println("Error querying the database " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public Task getTaskById(Long id) {
        String sql = "SELECT id, title, description, status, due_date, created_at, updated_at FROM task WHERE id = :id";
        Map<String, Object> params = Map.of(
                "id", id);
        try {
            return jdbcTemplate.queryForObject(sql, params, new TaskRowMapper());
        } catch (EmptyResultDataAccessException e) {
            System.err.println("Task not found: " + e.getMessage());
            return null;
        }
    }

    @Transactional()
    public Task updateTaskById(Long id, TaskDTO taskDTO) {
        // acquire a lock on the row
        String lockSql = "SELECT * FROM task WHERE id = :id FOR UPDATE";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);

        // Lock the row
        List<Task> lockedTasks = jdbcTemplate.query(lockSql, params, new TaskRowMapper());

        // check if task exists before proceeding
        if (lockedTasks.isEmpty()) {
            return null;
        }

        // Prepare the update SQL
        StringBuilder updateSql = new StringBuilder("UPDATE task SET ");
        if (taskDTO.getTitle() != null) {
            updateSql.append("title = :title, ");
            params.put("title", taskDTO.getTitle());
        }
        if (taskDTO.getDescription() != null) {
            updateSql.append("description = :description, ");
            params.put("description", taskDTO.getDescription());
        }

        if (taskDTO.getStatus() != null) {
            updateSql.append("status = :status, ");
            params.put("status", taskDTO.getStatus());
        }

        if (taskDTO.getDueDate() != null) {
            updateSql.append("due_date = :dueDate, ");
            params.put("dueDate", taskDTO.getDueDate());
        }

        // remove trailing comma and space
        if (params.size() > 1) {
            // ensure atlease one field to update
            updateSql.setLength(updateSql.length() - 2);
            updateSql.append(" WHERE id = :id RETURNING *");

            // execute the update and handle the result
            List<Task> updatedTasks = jdbcTemplate.query(updateSql.toString(), params, new TaskRowMapper());
            return updatedTasks.isEmpty() ? null : updatedTasks.get(0);
        } else {
            throw new IllegalArgumentException("No fields to update");
        }
    }

    public boolean deleteTask(Long id) {
        String deleteSql = "DELETE FROM task where id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        int rowsAffected = jdbcTemplate.update(deleteSql, params);
        return rowsAffected > 0;
    }

    private static class TaskRowMapper implements RowMapper<Task> {

        @Override
        public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
            // TODO Auto-generated method stub
            Task task = new Task();
            task.setId(rs.getLong("id"));
            task.setTitle(rs.getString("title"));
            task.setDescription(rs.getString("description"));
            task.setStatus((rs.getString("status")));
            task.setDueDate(rs.getDate("due_date").toLocalDate());
            task.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
            task.setUpdatedAt(rs.getObject("updated_at", LocalDateTime.class));
            return task;
        }
    }

}
