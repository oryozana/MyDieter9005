package com.example.mydieter9005;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btMealsMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btMealsMenu = (Button) findViewById(R.id.btMealsMenu);
    }

    public void sendToSelected(View v) {
        int id = v.getId();
        if (id == btMealsMenu.getId()) {
            Intent nextActivity = new Intent(this, mealsMenu.class);
            // nextActivity.putExtra("shoppingList", shoppingList);
            startActivity(nextActivity);
        }
    }
}