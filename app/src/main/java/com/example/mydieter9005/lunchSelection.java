package com.example.mydieter9005;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class lunchSelection extends AppCompatActivity {

    Intent me;
    ArrayList<String> mealsList;
    ArrayAdapter<String> adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch_selection);

        mealsList = new ArrayList<>();
        mealsList.add("Toast");
        mealsList.add("400");
        mealsList.add("15");

        mealsList.add("Toast with tomato and cucumber");
        mealsList.add("435");
        mealsList.add("20");

        mealsList.add("Toast with tomato");
        mealsList.add("425");
        mealsList.add("18");

        mealsList.add("Toast with cucumber");
        mealsList.add("410");
        mealsList.add("18");

        mealsList.add("Toast with olive and corn");
        mealsList.add("500");
        mealsList.add("20");

        mealsList.add("Toast with olive");
        mealsList.add("440");
        mealsList.add("18");

        mealsList.add("Toast with corn");
        mealsList.add("460");
        mealsList.add("18");

        mealsList.add("Clear selection");
        mealsList.add("0");
        mealsList.add("0");

        me = getIntent();
        listView = (ListView) findViewById(R.id.listViewLunch);

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

                me.setClass(lunchSelection.this, mealsMenu.class);
                me.putExtra("lunch", selectedItem);
                startActivity(me);
            }
        });
    }
}