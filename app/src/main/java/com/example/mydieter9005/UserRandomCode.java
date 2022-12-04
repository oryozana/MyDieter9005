package com.example.mydieter9005;

import java.util.ArrayList;

public class UserRandomCode extends TemporaryCode {
    private ArrayList<User> users;

    public UserRandomCode(ArrayList<User> users, String expirationTime) {
        super(null, expirationTime);
        super.code = this.generateRandomCode();
        this.users = users;
        this.type = "UserRandomCode";
    }

    public String generateRandomCode(){
        String allLettersAndDigits = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz0123456789";

        String randomCode = "";
        for(int i = 0; i < codeMaximumLength; i++)
            randomCode += allLettersAndDigits.charAt((int)(Math.random() * allLettersAndDigits.length()));

        return randomCode;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
}
