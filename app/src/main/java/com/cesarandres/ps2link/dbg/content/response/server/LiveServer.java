package com.cesarandres.ps2link.dbg.content.response.server;

public class LiveServer {
    private String name;
    private String age;
    private int ageSeconds;
    private String status;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public int getAgeSeconds() {
        return ageSeconds;
    }

    public void setAgeSeconds(int ageSeconds) {
        this.ageSeconds = ageSeconds;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}