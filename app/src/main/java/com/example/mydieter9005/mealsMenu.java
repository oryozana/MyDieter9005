package com.example.mydieter9005;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Size;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class mealsMenu extends AppCompatActivity {

    TextView tvBreakfast, tvLunch, tvDinner;
    TextView tvTotalCalories, tvTotalTime;
    Button btBreakfast, btLunch, btDinner, btFinish;
    String breakfast, lunch, dinner;
    String[] list, meals = new String[3];
    int totalCalories = 0, totalTime = 0;
    Intent me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals_menu);

        tvBreakfast = (TextView) findViewById(R.id.tvBreakfast);
        tvLunch = (TextView) findViewById(R.id.tvLunch);
        tvDinner = (TextView) findViewById(R.id.tvDinner);

        btBreakfast = (Button) findViewById(R.id.btBreakfast);
        btLunch = (Button) findViewById(R.id.btLunch);
        btDinner = (Button) findViewById(R.id.btDinner);
        btFinish = (Button) findViewById(R.id.btFinish);

        tvTotalCalories = (TextView) findViewById(R.id.tvTotalCalories);
        tvTotalTime = (TextView) findViewById(R.id.tvTotalTime);

        try {
            me = getIntent();
            breakfast = me.getStringExtra("breakfast");
            list = organizeMeal(breakfast);
            tvBreakfast.setText("Your breakfast is: " + list[0] + ".");
            btBreakfast.setText(list[1] + "." + "\n" + list[2]);
            totalCalories += Integer.parseInt(separateInfo(list[1]));
            totalTime += Integer.parseInt(separateInfo(list[2]));
            meals[0] = list[0];
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        try {
            me = getIntent();
            lunch = me.getStringExtra("lunch");
            list = organizeMeal(lunch);
            tvLunch.setText("Your lunch is: " + list[0] + ".");
            btLunch.setText(list[1] + "." + "\n" + list[2]);
            totalCalories += Integer.parseInt(separateInfo(list[1]));
            totalTime += Integer.parseInt(separateInfo(list[2]));
            meals[1] = list[0];
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        try {
            me = getIntent();
            dinner = me.getStringExtra("dinner");
            list = organizeMeal(dinner);
            tvDinner.setText("Your dinner is: " + list[0] + ".");
            btDinner.setText(list[1] + "." + "\n" + list[2]);
            totalCalories += Integer.parseInt(separateInfo(list[1]));
            totalTime += Integer.parseInt(separateInfo(list[2]));
            meals[2] = list[0];
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        tvTotalCalories.setText("Total calories: " + totalCalories + " calories.");
        tvTotalTime.setText("Total time: " + totalTime + " minutes.");
    }

    public void sendToMealSelection(View v){
        int id = v.getId();
        if(id == btBreakfast.getId()) {
            me.setClass(this, breakfastSelection.class);
            startActivity(me);
        }
        if(id == btLunch.getId()) {
            me.setClass(this, lunchSelection.class);
            startActivity(me);
        }
        if(id == btDinner.getId()) {
            me.setClass(this, dinnerSelection.class);
            startActivity(me);
        }
        if(id == btFinish.getId()) {
            me.setClass(this, ingredientsPickup.class);
            me.putExtra("meals", meals);
            me.putExtra("totalCalories", totalCalories);
            startActivity(me);
        }
    }

    public String[] organizeMeal(String meal){
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

    public String separateInfo(String listPart){
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