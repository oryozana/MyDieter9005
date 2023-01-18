package com.example.mydieter9005;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class MealCirclesListAdapter extends ArrayAdapter<Meal> {
    public MealCirclesListAdapter(Context context, ArrayList<Meal> meals){
        super(context, R.layout.circles_meal_list_view, meals);
    }

    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent){
        Meal meal = getItem(position);
        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.circles_meal_list_view, parent, false);

        TextView tvMealName = convertView.findViewById(R.id.tvCircleListMealName);
        TextView tvMealDescription = convertView.findViewById(R.id.tvCircleListMealDescription);

   //     ImageButton ibtDeleteMeal = convertView.findViewById(R.id.ibtDeleteMeal);

        tvMealName.setText(meal.getName() + ":");
        tvMealDescription.setText(meal.getGrams() + " grams and " + meal.getCalories() + " calories.");

//        ibtDeleteMeal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        return convertView;
    }
}
