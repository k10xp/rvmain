package com.c2org.rvapi.api.models;

public class LogEntry {
    private String log_id;
    private String user_id;
    private String log_created;
    private String log_content;

    public void setLogId(String input) {
        this.log_id = input;
    }

    public String getLogId() {
        return this.log_id;
    }

    public void setUserId(String input) {
        this.user_id = input;
    }

    public String getUserId() {
        return this.user_id;
    }

    public void setLogCreated(String input) {
        this.log_created = input;
    }

    public String getLogCreated() {
        return this.log_created;
    }

    public void setLogContent(String input) {
        this.log_content = input;
    }

    public String getLogContent() {
        return this.log_content;
    }
}
