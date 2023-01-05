package com.example.mydieter9005;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class MealListAdapter extends ArrayAdapter<Meal> {
    public MealListAdapter(Context context, ArrayList<Meal> meals){
        super(context, R.layout.basic_meal_list_view, meals);
    }

    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent){
        Meal meal = getItem(position);
        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.basic_meal_list_view, parent, false);

        TextView tvMealName = convertView.findViewById(R.id.tvListMealName);
        TextView tvMealDescription = convertView.findViewById(R.id.tvListMealDescription);

        tvMealName.setText(meal.getName() + ":");
        tvMealDescription.setText(meal.getGrams() + " grams and " + meal.getCalories() + " calories.");

        return convertView;
    }
}
