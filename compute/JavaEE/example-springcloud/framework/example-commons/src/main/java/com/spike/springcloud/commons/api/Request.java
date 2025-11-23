package com.spike.springcloud.commons.api;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

public class Request<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id; // for trace
    private long time = new Date().getTime();
    private T data;
    private Result<T> previousResponse = null; // for calling chain

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Result<T> getPreviousResponse() {
        return previousResponse;
    }

    public void setPreviousResponse(Result<T> previousResponse) {
        this.previousResponse = previousResponse;
    }

}
