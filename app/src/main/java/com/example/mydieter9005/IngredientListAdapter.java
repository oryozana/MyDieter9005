package com.example.mydieter9005;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class IngredientListAdapter extends ArrayAdapter<Ingredient> {

    public IngredientListAdapter(Context context, ArrayList<Ingredient> ingredients){
        super(context, R.layout.basic_ingredient_list_view, ingredients);
    }

    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent){
        Ingredient ingredient = getItem(position);
        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.basic_ingredient_list_view, parent, false);

        ImageView civIngredientImage = convertView.findViewById(R.id.civIngredientImage);
        TextView tvIngredientName = convertView.findViewById(R.id.tvListIngredientName);
        TextView tvIngredientDescription = convertView.findViewById(R.id.tvListIngredientDescription);

        civIngredientImage.setImageResource(ingredient.getImgId());
        tvIngredientName.setText(ingredient.getName() + ":");
        tvIngredientDescription.setText(ingredient.getGrams() + " grams and " + ingredient.getCalories() + " calories.");

        return convertView;
    }
}
