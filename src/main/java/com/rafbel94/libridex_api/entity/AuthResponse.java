package com.rafbel94.libridex_api.entity;

import java.util.List;
import java.util.Map;

public class AuthResponse {
    boolean success;
    List<String> message;
    Map<String, Object> data;

    public AuthResponse() {
    }

    public AuthResponse(boolean success, List<String> message, Map<String, Object> data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setErrors(List<String> message) {
        this.message = message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ApiResponse [success=" + success + ", message=" + message + ", data=" + data + "]";
    }
}
