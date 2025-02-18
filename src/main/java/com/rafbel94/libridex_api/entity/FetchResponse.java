package com.rafbel94.libridex_api.entity;

import java.util.List;

public class FetchResponse {
    boolean success;
    List<String> message;
    List<Object> data;

    public FetchResponse() {
    }

    public FetchResponse(boolean success, List<String> message, List<Object> data) {
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

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ApiResponse [success=" + success + ", messageg=" + message + ", data=" + data + "]";
    }
}
