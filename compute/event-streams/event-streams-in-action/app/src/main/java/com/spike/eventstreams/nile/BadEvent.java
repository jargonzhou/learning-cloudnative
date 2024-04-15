package com.spike.eventstreams.nile;

public class BadEvent {
    private String error;

    public BadEvent(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
