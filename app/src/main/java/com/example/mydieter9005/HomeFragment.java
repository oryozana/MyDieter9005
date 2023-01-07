package com.example.mydieter9005;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AnalogClock;
import android.widget.Button;
import android.widget.LinearLayout;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class HomeFragment extends Fragment implements View.OnClickListener {

    TextView tvBreakfastMain, tvLunchMain, tvDinnerMain;
    TextView tvTotalProteinsMain, tvTotalFatsMain, tvTotalCaloriesMain;
    Button btMealsMenu, btWriteMealsToExternalFile, btReadMealsFromExternalFile;
    LinearLayout mainActivityLinearLayout;

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

        tvBreakfastMain = (TextView) view.findViewById(R.id.tvBreakfastMain);
        tvBreakfastMain.setMovementMethod(new ScrollingMovementMethod());
        tvLunchMain = (TextView) view.findViewById(R.id.tvLunchMain);
        tvLunchMain.setMovementMethod(new ScrollingMovementMethod());
        tvDinnerMain = (TextView) view.findViewById(R.id.tvDinnerMain);
        tvDinnerMain.setMovementMethod(new ScrollingMovementMethod());

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

        updateMealsIfNeeded();
    }

    public void updateMealsIfNeeded(){
        if(todayMenu.hasBreakfast())
            tvBreakfastMain.setText("Your breakfast: " + todayMenu.getUnitedBreakfastName());
        if(todayMenu.hasLunch())
            tvLunchMain.setText("Your lunch: " + todayMenu.getUnitedLunchName());
        if(todayMenu.hasDinner())
            tvDinnerMain.setText("Your dinner: " + todayMenu.getUnitedDinnerName());

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



//    public void sendToSelected(View v) {
//        int id = v.getId();
//        if (id == btMealsMenu.getId()) {
//            me.setClass(this, mealsMenu.class);
//            startActivity(me);
//        }
//    }

//    public void write(){
//        if(me.hasExtra("meals")){
//            try {
//                fos = openFileOutput(todayDate, Context.MODE_PRIVATE);
//                osw = new OutputStreamWriter(fos);
//                bw = new BufferedWriter(osw);
//
//                bw.write(todayDate + "\n");
//
//                bw.write("Your breakfast: ");
//                if(me.hasExtra("breakfast"))
//                    bw.write(me.getStringExtra("breakfast"));
//
//                bw.write("\n" + "Your lunch: ");
//                if(me.hasExtra("lunch"))
//                    bw.write(me.getStringExtra("lunch"));
//
//                bw.write("\n" + "Your dinner: ");
//                if(me.hasExtra("dinner"))
//                    bw.write(me.getStringExtra("dinner"));
//
//                bw.write("\n" + tvTotalCaloriesMain.getText().toString() + "\n");
//                //bw.write(tvCaloriesLeftMain.getText().toString());
//
//                bw.close();
//
//                Toast.makeText(this, todayDate + " wrote.", Toast.LENGTH_SHORT).show();
//            }
//            catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        else{
//            Toast.makeText(this, "You didn't choose any meal yet.", Toast.LENGTH_SHORT).show();
//        }
//    }

//    public void showFileData(){
//        String[] dataParts = getFileData(todayDate).split("\n");
//        String savedBreakfast="", savedLunch="", savedDinner="", savedTotalCalories="", savedCaloriesLeft="";
//        String[] meals = new String[3];
//
//        if(dataParts[1].replaceAll(":", "").length() == dataParts[1].length() - 2)
//            savedBreakfast = dataParts[1].split(": ")[1] + ": " + dataParts[1].split(": ")[2];
//        if(!savedBreakfast.equals("") && !savedBreakfast.equals(" "))
//            me.putExtra("breakfast", savedBreakfast);
//
//        if(dataParts[2].replaceAll(":", "").length() == dataParts[2].length() - 2)
//            savedLunch = dataParts[2].split(": ")[1] + ": " + dataParts[2].split(": ")[2];
//        if(!savedLunch.equals("") && !savedLunch.equals(" "))
//            me.putExtra("lunch", savedLunch);
//
//        if(dataParts[3].replaceAll(":", "").length() == dataParts[3].length() - 2)
//            savedDinner = dataParts[3].split(": ")[1] + ": " + dataParts[3].split(": ")[2];
//        if(!savedDinner.equals("") && !savedDinner.equals(" "))
//            me.putExtra("dinner", savedDinner);
//
//        savedTotalCalories = dataParts[4].split(": ")[1];
//        if(!savedTotalCalories.equals("") && !savedTotalCalories.equals(" "))
//            me.putExtra("totalCalories", multiUsageFunctions.getCaloriesOrMinutesOutOfString(savedTotalCalories));
//        savedCaloriesLeft = dataParts[5].split(": ")[1];
//        if(!savedCaloriesLeft.equals("") && !savedCaloriesLeft.equals(" "))
//            me.putExtra("caloriesLeft", multiUsageFunctions.getCaloriesOrMinutesOutOfString(savedCaloriesLeft));
//
//        meals[0] = savedBreakfast.split(":")[0];
//        meals[1] = savedLunch.split(":")[0];
//        meals[2] = savedDinner.split(":")[0];
//        me.putExtra("meals", meals);
//
//        updateMealsIfNeeded();
//    }



    @Override
    public void onClick(View v) {
        int viewId = v.getId();
//
//        if(viewId == btMealsMenu.getId())
//            sendToSelected(v);
//
//        if(viewId == btWriteMealsToExternalFile.getId())
//            write();
//
//        if(viewId == btReadMealsFromExternalFile.getId())
//            showFileData();
    }
}