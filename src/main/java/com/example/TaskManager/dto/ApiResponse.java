package com.example.TaskManager.dto;

public class ApiResponse<T> {
    private T data;
    private String error;

    public ApiResponse(T data, String error) {
        this.data = data;
        this.error = error;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<T>(data, null);
    }

    public static <T> ApiResponse<T> failure(String error) {
        return new ApiResponse<T>(null, error);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
