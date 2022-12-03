package com.example.mydieter9005;

public class multiUsageFunctions {

    public static String[] organizeMeal(String meal){  // Get a full meal name and separate it into: name, calories and minutes.
        String mealName = "", mealCalories = "", mealTime = "";
        int start = 0;
        for(int i = 0; i < meal.length(); i++){
            if(meal.charAt(i) != ':'){
                mealName += meal.charAt(i);
            }
            else {
                start = i + 2;
                break;
            }
        }
        for(int i = start; i < meal.length(); i++){
            if(meal.charAt(i) != ','){
                mealCalories += meal.charAt(i);
            }
            else {
                start = i + 2;
                break;
            }
        }
        for(int i = start; i < meal.length(); i++){
            mealTime += meal.charAt(i);
        }
        return new String[] {mealName, mealCalories, mealTime};
    }

    public static int getCaloriesOrMinutesOutOfString(String caloriesOrMinutes){  // Get a string who contain one number somewhere and return the number.
        char currentChar;
        String amount = "";
        for(int i = 0; i < caloriesOrMinutes.length(); i++){
            currentChar = caloriesOrMinutes.charAt(i);
            if(Character.isDigit(currentChar)) {
                amount += currentChar;
            }
        }
        return Integer.parseInt(amount);
    }

    public static String separateInfo(String listPart){
        String info = "";
        for(int i = 0; i < listPart.length(); i++){
            if(!Character.toString(listPart.charAt(i)).equals(",") && !Character.toString(listPart.charAt(i)).equals(" ") && !Character.toString(listPart.charAt(i)).equals(":")){
                info += Character.toString(listPart.charAt(i));
            }
            else {
                return info;
            }
        }
        return info;
    }
}