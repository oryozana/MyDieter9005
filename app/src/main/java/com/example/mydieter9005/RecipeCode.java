package com.example.mydieter9005;

public class RecipeCode {
    final String allLettersAndDigits = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz0123456789";
    final int codeMaximumLength = 8;
    private String expirationTime;
    private String code;
    private FirebaseMeal firebaseMeal;

    public RecipeCode(FirebaseMeal firebaseMeal, String expirationTime) {
        this.expirationTime = expirationTime.replaceAll("\\.", "*"); // Some problems involving with ".".
        this.code = this.generateRandomCode();
        this.firebaseMeal = firebaseMeal;
    }

    public String generateRandomCode(){
        String randomCode = "";
        for(int i = 0; i < codeMaximumLength; i++)
            randomCode += allLettersAndDigits.charAt((int)(Math.random() * allLettersAndDigits.length()));

        return randomCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(String expirationTime) {
        this.expirationTime = expirationTime;
    }

    public FirebaseMeal getMeal() {
        return this.firebaseMeal;
    }

    public void setMeal(FirebaseMeal firebaseMeal) {
        this.firebaseMeal = firebaseMeal;
    }
}
