package com.example.mydieter9005;

public class Code {
    final int codeMaximumLength = 8;
    protected String code;
    protected String type;

    public Code(String code) {
        this.code = code;
        this.type = "Code";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

class TemporaryCode extends Code {
    protected String expirationTime;

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
