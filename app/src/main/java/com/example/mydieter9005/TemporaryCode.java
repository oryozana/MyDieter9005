package com.example.mydieter9005;

public class TemporaryCode extends Code {
    private String expirationTime;

    public TemporaryCode(String code, String expirationTime) {
        super(code);
        this.expirationTime = expirationTime.replaceAll("\\.", "*");
        this.type= "TemporaryCode";
    }

    public String getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(String expirationTime) {
        this.expirationTime = expirationTime;
    }
}
