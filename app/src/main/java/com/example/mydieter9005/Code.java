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
