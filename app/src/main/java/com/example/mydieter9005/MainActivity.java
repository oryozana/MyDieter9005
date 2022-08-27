package com.example.mydieter9005;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView tvBreakfastMain, tvLunchMain, tvDinnerMain;
    TextView tvTotalCaloriesMain, tvCaloriesLeftMain;
    Button btMealsMenu;
    int totalCalories, caloriesLeft;
    String[] meals;
    Intent me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        me = getIntent();
        me = createTheFirstIntent(me);

        tvBreakfastMain = (TextView) findViewById(R.id.tvBreakfastMain);
        tvBreakfastMain.setMovementMethod(new ScrollingMovementMethod());
        tvLunchMain = (TextView) findViewById(R.id.tvLunchMain);
        tvLunchMain.setMovementMethod(new ScrollingMovementMethod());
        tvDinnerMain = (TextView) findViewById(R.id.tvDinnerMain);
        tvDinnerMain.setMovementMethod(new ScrollingMovementMethod());

        tvTotalCaloriesMain = (TextView) findViewById(R.id.tvTotalCaloriesMain);
        tvCaloriesLeftMain = (TextView) findViewById(R.id.tvCaloriesLeftMain);

        btMealsMenu = (Button) findViewById(R.id.btMealsMenu);

        try {
            meals = me.getStringArrayExtra("meals");
            tvBreakfastMain.setText("Your breakfast: " + meals[0]);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        try {
            meals = me.getStringArrayExtra("meals");
            tvLunchMain.setText("Your lunch: " + meals[1]);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        try {
            meals = me.getStringArrayExtra("meals");
            tvDinnerMain.setText("Your dinner: " + meals[2]);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        try {
            totalCalories = me.getIntExtra("totalCalories", 0);
            tvTotalCaloriesMain.setText("Total calories: " + totalCalories);
            caloriesLeft = 2000 - totalCalories;
            tvCaloriesLeftMain.setText("Calories left: " + caloriesLeft);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Intent createTheFirstIntent(Intent me){
        if(!me.hasExtra("meals")){
            me = new Intent(this, mealsMenu.class);
        }
        return me;
    }

    public void sendToSelected(View v) {
        int id = v.getId();
        if (id == btMealsMenu.getId()) {
            me.setClass(this, mealsMenu.class);
            startActivity(me);
        }
    }
}