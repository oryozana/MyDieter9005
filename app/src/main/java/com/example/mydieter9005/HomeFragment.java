package com.example.mydieter9005;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.PrecomputedText;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AnalogClock;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

public class HomeFragment extends Fragment implements View.OnClickListener {

    TextView tvTotalProteinsMain, tvTotalFatsMain, tvTotalCaloriesMain;
    Button btMealsMenu, btWriteMealsToExternalFile, btReadMealsFromExternalFile;
    LinearLayout breakfastLinearLayout, lunchLinearLayout, dinnerLinearLayout;
    LinearLayout mainActivityLinearLayout;

    MealOverviewFragment mealOverviewFragment;

    MealCirclesListAdapter breakfastMealListAdapter, lunchMealListAdapter, dinnerMealListAdapter;
    ListView lvBreakfastMeals, lvLunchMeals, lvDinnerMeals;

    DailyMenu todayMenu = DailyMenu.getTodayMenu();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        tvBreakfastMain = (TextView) view.findViewById(R.id.tvBreakfastMain);
//        tvBreakfastMain.setMovementMethod(new ScrollingMovementMethod());
//        tvLunchMain = (TextView) view.findViewById(R.id.tvLunchMain);
//        tvLunchMain.setMovementMethod(new ScrollingMovementMethod());
//        tvDinnerMain = (TextView) view.findViewById(R.id.tvDinnerMain);
//        tvDinnerMain.setMovementMethod(new ScrollingMovementMethod());

        tvTotalCaloriesMain = (TextView) view.findViewById(R.id.tvTotalCaloriesMain);
        tvTotalProteinsMain = (TextView) view.findViewById(R.id.tvTotalProteinsMain);
        tvTotalFatsMain = (TextView) view.findViewById(R.id.tvTotalFatsMain);

        btReadMealsFromExternalFile = (Button) view.findViewById(R.id.btReadMealsFromExternalFile);
        btReadMealsFromExternalFile.setOnClickListener(this);
        btWriteMealsToExternalFile = (Button) view.findViewById(R.id.btWriteMealsToExternalFile);
        btWriteMealsToExternalFile.setOnClickListener(this);
        btMealsMenu = (Button) view.findViewById(R.id.btMealsMenu);
        btMealsMenu.setOnClickListener(this);

        mainActivityLinearLayout = (LinearLayout) view.findViewById(R.id.mainActivityLinearLayout);

        lvBreakfastMeals = (ListView) view.findViewById(R.id.lvBreakfastMeals);
        lvLunchMeals = (ListView) view.findViewById(R.id.lvLunchMeals);
        lvDinnerMeals = (ListView) view.findViewById(R.id.lvDinnerMeals);

        updateMealsIfNeeded();
    }


    @Override
    public void onResume() {
        super.onResume();
        updateMealsIfNeeded();
    }

    public void updateMealsIfNeeded(){
        int totalHeight;
        int mealsAmount;

        todayMenu = DailyMenu.getTodayMenu();
        todayMenu.correctNutritiousValues();

        if(todayMenu.hasBreakfast()) {
            lvBreakfastMeals.setVisibility(View.VISIBLE);
            breakfastMealListAdapter = new MealCirclesListAdapter(getActivity(), todayMenu.getBreakfastAsMealsTypeOnly());
            lvBreakfastMeals.setAdapter(breakfastMealListAdapter);

            lvBreakfastMeals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Meal selectedItem = (Meal) adapterView.getItemAtPosition(i);

                    mealOverviewFragment = new MealOverviewFragment(selectedItem, "Breakfast");
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityFrameLayout, mealOverviewFragment).commit();
                }
            });

            totalHeight = 0;
            mealsAmount = todayMenu.getBreakfast().size();

            ViewGroup.LayoutParams params = lvBreakfastMeals.getLayoutParams();
            if(mealsAmount < 3) {
                for (int i = 0; i < mealsAmount; i++) {
                    View listItem = breakfastMealListAdapter.getView(i, null, lvBreakfastMeals);
                    listItem.measure(0, 0);
                    totalHeight += listItem.getMeasuredHeight();
                }
                params.height = (totalHeight + (lvBreakfastMeals.getDividerHeight() * (mealsAmount)));
            }
            else {
                for (int i = 0; i < 3; i++) {
                    View listItem = breakfastMealListAdapter.getView(i, null, lvBreakfastMeals);
                    listItem.measure(0, 0);
                    totalHeight += listItem.getMeasuredHeight();
                }
                params.height = (totalHeight + (lvBreakfastMeals.getDividerHeight() * 3));
            }
            lvBreakfastMeals.setLayoutParams(params);
        }
        else
            lvBreakfastMeals.setVisibility(View.GONE);

        if(todayMenu.hasLunch()){
            lvLunchMeals.setVisibility(View.VISIBLE);
            lunchMealListAdapter = new MealCirclesListAdapter(getActivity(), todayMenu.getLunchAsMealsTypeOnly());
            lvLunchMeals.setAdapter(lunchMealListAdapter);

            lvLunchMeals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Meal selectedItem = (Meal) adapterView.getItemAtPosition(i);

                    mealOverviewFragment = new MealOverviewFragment(selectedItem, "Lunch");
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityFrameLayout, mealOverviewFragment).commit();
                }
            });

            totalHeight = 0;
            mealsAmount = todayMenu.getLunch().size();

            ViewGroup.LayoutParams params = lvLunchMeals.getLayoutParams();
            if(mealsAmount < 3) {
                for (int i = 0; i < mealsAmount; i++) {
                    View listItem = lunchMealListAdapter.getView(i, null, lvLunchMeals);
                    listItem.measure(0, 0);
                    totalHeight += listItem.getMeasuredHeight();
                }
                params.height = (totalHeight + (lvLunchMeals.getDividerHeight() * (mealsAmount)));
            }
            else {
                for (int i = 0; i < 3; i++) {
                    View listItem = lunchMealListAdapter.getView(i, null, lvLunchMeals);
                    listItem.measure(0, 0);
                    totalHeight += listItem.getMeasuredHeight();
                }
                params.height = (totalHeight + (lvLunchMeals.getDividerHeight() * 3));
            }
            lvLunchMeals.setLayoutParams(params);
        }
        else
            lvLunchMeals.setVisibility(View.GONE);

        if(todayMenu.hasDinner()){
            lvDinnerMeals.setVisibility(View.VISIBLE);
            dinnerMealListAdapter = new MealCirclesListAdapter(getActivity(), todayMenu.getDinnerAsMealsTypeOnly());
            lvDinnerMeals.setAdapter(dinnerMealListAdapter);

            lvDinnerMeals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Meal selectedItem = (Meal) adapterView.getItemAtPosition(i);

                    mealOverviewFragment = new MealOverviewFragment(selectedItem, "Dinner");
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityFrameLayout, mealOverviewFragment).commit();
                }
            });

            totalHeight = 0;
            mealsAmount = todayMenu.getDinner().size();

            ViewGroup.LayoutParams params = lvDinnerMeals.getLayoutParams();
            if(mealsAmount < 3) {
                for (int i = 0; i < mealsAmount; i++) {
                    View listItem = dinnerMealListAdapter.getView(i, null, lvDinnerMeals);
                    listItem.measure(0, 0);
                    totalHeight += listItem.getMeasuredHeight();
                }
                params.height = (totalHeight + (lvDinnerMeals.getDividerHeight() * (mealsAmount)));
            }
            else {
                for (int i = 0; i < 3; i++) {
                    View listItem = dinnerMealListAdapter.getView(i, null, lvDinnerMeals);
                    listItem.measure(0, 0);
                    totalHeight += listItem.getMeasuredHeight();
                }
                params.height = (totalHeight + (lvDinnerMeals.getDividerHeight() * 3));
            }
            lvDinnerMeals.setLayoutParams(params);
        }
        else
            lvDinnerMeals.setVisibility(View.GONE);

        tvTotalProteinsMain.setText("Total Proteins: " + todayMenu.getTotalProteins() + " .");
        tvTotalFatsMain.setText("Total Fats: " + todayMenu.getTotalFats() + " .");
        tvTotalCaloriesMain.setText("Total calories: " + todayMenu.getTotalCalories() + " .");
    }

//    public void saveDailyMenuIntoFile(DailyMenu dailyMenu){
//        try {
//            fos = openFileOutput("dailyMenusFile", Context.MODE_PRIVATE);
//            osw = new OutputStreamWriter(fos);
//            bw = new BufferedWriter(osw);
//
//            bw.write("Daily menus: " + "\n");
//
//            DailyMenu.removeDailyMenuDuplicationsAndAddAnotherOne(dailyMenu);
//            for(int i = 0; i < DailyMenu.getDailyMenus().size(); i++)
//                bw.write(DailyMenu.getDailyMenus().get(i).generateDailyMenuDescriptionForFiles() + "\n");
//
//            bw.close();
//        }
//        catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
    }
}