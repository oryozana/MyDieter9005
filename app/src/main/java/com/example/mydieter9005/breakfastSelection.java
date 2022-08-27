package com.example.mydieter9005;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class breakfastSelection extends AppCompatActivity {

    Intent me;
    ArrayList<String> mealsList;
    ArrayAdapter<String> adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breakfast_selection);

        mealsList = new ArrayList<>();
        mealsList.add("Cereals");
        mealsList.add("185");
        mealsList.add("1");

        mealsList.add("Chocolate cereals");
        mealsList.add("230");
        mealsList.add("1");

        mealsList.add("Yogurt (small)");
        mealsList.add("100");
        mealsList.add("1");

        mealsList.add("Chocolate flavored yogurt");
        mealsList.add("200");
        mealsList.add("1");

        listView = (ListView) findViewById(R.id.listViewBreakfast);
        me = getIntent();

        String[] fields = new String[mealsList.size() / 3];
        double total = 0;
        for(int i = 0; i < mealsList.size(); i += 3){
            String field = mealsList.get(i) + ": " + mealsList.get(i + 1) + " calories, " + mealsList.get(i + 2) + " minutes.";
            fields[i / 3] = field;
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fields);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = (String) adapterView.getItemAtPosition(i);

                me.setClass(breakfastSelection.this, mealsMenu.class);
                me.putExtra("breakfast", selectedItem);
                startActivity(me);
            }
        });
    }
}