package com.example.mydieter9005;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("SuspiciousRegexArgument")
public class customMeals extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    Button btFinishCustomize;
    TextView tvInstructions;
    EditText customMeal;
    String[] mealInfo;
    String cameFrom;
    Intent me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_meals);

        me = getIntent();
        cameFrom = me.getStringExtra("cameFrom");
        Toast.makeText(this, "selected meal: " + cameFrom, Toast.LENGTH_SHORT).show();

        btFinishCustomize = (Button) findViewById(R.id.btFinishCustomize);
        tvInstructions = (TextView) findViewById(R.id.tvInstructions);
        customMeal = (EditText) findViewById(R.id.customMeal);

        writeTheInstructions();

        mediaPlayer = MediaPlayer.create(customMeals.this, R.raw.my_song);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    public void writeTheInstructions(){
        tvInstructions.setText(
                "Make sure to use words like: 'with' and 'and' instead of using ','." + "\n" +
                "You need to use the special word 'flavored' to make something flavored, like this: " + "\n" +
                "chocolate flavored ice cream or chocolate flavored yogurt and so on..." + "\n" +
                "You can also use 'mini-meals' like: salad, toast and cereals in your text." + "\n" +
                "Not every ingredient have photo so it will just show you the name."
        );
    }

    public void setCustomFood(View v){
        String meal = customMeal.getText().toString();
        String testMeal;
        if(meal.contains(":") && meal.contains(",") && meal.contains(".") && meal.contains(" ")){
            testMeal = meal.replaceAll(":", "");
            if(meal.length() != testMeal.length() + 1){
                Toast.makeText(this, "There should be one ':' .", Toast.LENGTH_SHORT).show();
            }
            else{
                testMeal = meal.replaceAll(",", "");
                if(meal.length() != testMeal.length() + 1){
                    Toast.makeText(this, "There should be one ',' .", Toast.LENGTH_SHORT).show();
                }
                else{
                    char lastChar = meal.charAt(meal.length() - 1);
                    if(lastChar != '.'){
                        Toast.makeText(this, meal.length() + " " + testMeal.length(), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if(meal.contains("_") || meal.contains("-")){
                            Toast.makeText(this, "Make sure to use only the format symbols.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            viewInfo();
                        }
                    }
                }
            }
        }
        else{
            Toast.makeText(this, "Make sure to follow the format instructions.", Toast.LENGTH_SHORT).show();
        }
    }

    public void viewInfo(){
        AlertDialog ad;
        AlertDialog.Builder adb;
        adb = new AlertDialog.Builder(this);
        mealInfo = organizeMeal(customMeal.getText().toString());
        adb.setTitle("Your meal is: ");
        adb.setMessage("Name: " + mealInfo[0] + "\n" + "Calories: " + mealInfo[1] + "\n" + "Minutes: " + mealInfo[2]);
        adb.setCancelable(false);
        adb.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                customMeal.setText("");
            }
        });

        adb.setNeutralButton("Finish", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishCustomize();
            }
        });
        ad = adb.create();
        ad.show();
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

    public void finishCustomize(){
        me.setClass(customMeals.this, mealsMenu.class);
        me.putExtra(cameFrom, customMeal.getText().toString());
        startActivity(me);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }
}