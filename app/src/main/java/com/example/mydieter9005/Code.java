package com.example.mydieter9005;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Code {
    private String code;
    private ArrayList<User> users;
    private String expirationTime;

    public Code(String code, ArrayList<User> users, String expirationTime) {
        this.code = code;
        this.users = users;
        this.expirationTime = expirationTime.replaceAll("\\.", "*");
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public String getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(String expirationTime) {
        this.expirationTime = expirationTime;
    }
}
