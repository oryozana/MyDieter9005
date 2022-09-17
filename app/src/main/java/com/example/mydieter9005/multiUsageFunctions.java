package com.example.mydieter9005;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class multiUsageFunctions extends AppCompatActivity {

    public static String[] organizeMeal(String meal){  // Get a full meal name and separate it into: name, calories and minutes.
        String mealName = "", mealCalories = "", mealTime = "";
        int start = 0;
        for(int i = 0; i < meal.length(); i++){
            if(!Character.toString(meal.charAt(i)).equals(":")){
                mealName += Character.toString(meal.charAt(i));
            }
            else {
                start = i + 2;
                break;
            }
        }
        for(int i = start; i < meal.length(); i++){
            if(!Character.toString(meal.charAt(i)).equals(",")){
                mealCalories += Character.toString(meal.charAt(i));
            }
            else {
                start = i + 2;
                break;
            }
        }
        for(int i = start; i < meal.length(); i++){
            mealTime += Character.toString(meal.charAt(i));
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
}