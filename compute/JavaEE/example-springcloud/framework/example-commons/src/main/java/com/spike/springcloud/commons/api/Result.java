package com.spike.springcloud.commons.api;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

public class Result<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String id;
    private long time = new Date().getTime();
    private boolean success = true;
    private T data;
    private String msg;
    private Request<T> request;

    public Result(Request<T> request) {
        this.request = request;
        this.id = request.getId();
    }

    public Result(Request<T> request, T data) {
        this.data = data;

        this.request = request;
        this.id = request.getId();
    }

    public Result(T data) {
        this.data = data;

        this.request = null;
        this.id = null;
    }

    public Result(Request<T> request, boolean success, String msg, T data) {
        this.success = success;
        this.msg = msg;
        this.data = data;

        this.request = request;
        this.id = request.getId();
    }

    public Result(boolean success, String msg, T data) {
        this.success = success;
        this.msg = msg;
        this.data = data;

        this.request = null;
        this.id = null;
    }

    public static <T> Result<T> success(T data) {
        return new Result<T>(data);
    }

    public static <T> Result<T> fail(String msg) {
        return new Result<T>(false, msg, null);
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Request<T> getRequest() {
        return request;
    }

    public void setRequest(Request<T> request) {
        this.request = request;
    }

    public String getId() {
        return id;
    }

}
