package com.example.mydieter9005;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class finishMeals extends AppCompatActivity implements View.OnClickListener {

    private MediaPlayer mediaPlayer;

    Button btSendToIngredientsPickup, btFinishMeals;
    TextView tvBreakfastInfo, tvLunchInfo, tvDinnerInfo;
    ListView lvBreakfastIngredients, lvLunchIngredients, lvDinnerIngredients;

    DailyMenu todayMenu = DailyMenu.getTodayMenu();
    Song activeSong = Song.getSongs().get(0);

    ArrayList<Ingredient> breakfastIngredientsList, lunchIngredientsList, dinnerIngredientsList;
    ArrayList<String> breakfastFields, lunchFields, dinnerFields;
    ArrayAdapter<String> breakfastIngredientsAdapter, lunchIngredientsAdapter, dinnerIngredientsAdapter;

    FileInputStream is;
    InputStreamReader isr;
    BufferedReader br;
    Intent me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_meals);

        me = getIntent();
        if(me.hasExtra("activeSong"))
            activeSong = (Song) me.getSerializableExtra("activeSong");

        tvBreakfastInfo = (TextView) findViewById(R.id.tvBreakfastInfo);
        tvBreakfastInfo.setMovementMethod(new ScrollingMovementMethod());
        tvLunchInfo = (TextView) findViewById(R.id.tvLunchInfo);
        tvLunchInfo.setMovementMethod(new ScrollingMovementMethod());
        tvDinnerInfo = (TextView) findViewById(R.id.tvDinnerInfo);
        tvDinnerInfo.setMovementMethod(new ScrollingMovementMethod());

        lvBreakfastIngredients = (ListView) findViewById(R.id.lvBreakfastIngredients);
        lvLunchIngredients = (ListView) findViewById(R.id.lvLunchIngredients);
        lvDinnerIngredients = (ListView) findViewById(R.id.lvDinnerIngredients);

        btSendToIngredientsPickup = (Button) findViewById(R.id.btSendToIngredientsPickup);
        btSendToIngredientsPickup.setOnClickListener(this);
        btFinishMeals = (Button) findViewById(R.id.btFinishMeals);
        btFinishMeals.setOnClickListener(this);

        DailyMenu.saveDailyMenuIntoFile(DailyMenu.getTodayMenu(), finishMeals.this);
        implementSettingsData();
        initiateMediaPlayer();
        setAdapters();
    }

    public void setAdapters(){
        initiateIngredientListsAndSetMealsNames();  // Must come before initiateListViewsFields()
        initiateListViewsFields();

        if(todayMenu.hasBreakfast()){
            breakfastIngredientsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, breakfastFields);
            lvBreakfastIngredients.setAdapter(breakfastIngredientsAdapter);
            lvBreakfastIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Ingredient selectedItem = breakfastIngredientsList.get(position);

                    showIngredientImage(selectedItem);
                }
            });
        }

        if(todayMenu.hasLunch()){
            lunchIngredientsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lunchFields);
            lvLunchIngredients.setAdapter(lunchIngredientsAdapter);
            lvLunchIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Ingredient selectedItem = lunchIngredientsList.get(position);

                    showIngredientImage(selectedItem);
                }
            });
        }

        if(todayMenu.hasDinner()){
            dinnerIngredientsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dinnerFields);
            lvDinnerIngredients.setAdapter(dinnerIngredientsAdapter);
            lvDinnerIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Ingredient selectedItem = dinnerIngredientsList.get(position);

                    showIngredientImage(selectedItem);
                }
            });
        }
    }

    public void showIngredientImage(Ingredient ingredient){
        AlertDialog ad;
        AlertDialog.Builder adb;
        adb = new AlertDialog.Builder(this);
        adb.setTitle("Your ingredient: " + ingredient.getName());
        adb.setIcon(R.drawable.ic_food_icon);

        final ImageView ivIngredientImage = new ImageView(finishMeals.this);
        ivIngredientImage.setImageResource(ingredient.getImgId());
        adb.setView(ivIngredientImage);

        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        ad = adb.create();
        ad.show();
    }

    public void initiateListViewsFields(){
        Ingredient ingredient;
        breakfastFields = new ArrayList<String>();
        for(int i = 0; i < breakfastIngredientsList.size(); i++){
            ingredient = breakfastIngredientsList.get(i);
            breakfastFields.add(ingredient.getName() + ": " + ingredient.getGrams() + " grams.");
        }

        lunchFields = new ArrayList<String>();
        for(int i = 0; i < lunchIngredientsList.size(); i++){
            ingredient = lunchIngredientsList.get(i);
            lunchFields.add(ingredient.getName() + ": " + ingredient.getGrams() + " grams.");
        }

        dinnerFields = new ArrayList<String>();
        for(int i = 0; i < dinnerIngredientsList.size(); i++){
            ingredient = dinnerIngredientsList.get(i);
            dinnerFields.add(ingredient.getName() + ": " + ingredient.getGrams() + " grams.");
        }
    }

    public void initiateIngredientListsAndSetMealsNames(){
        breakfastIngredientsList = new ArrayList<Ingredient>();
        if(todayMenu.hasBreakfast()) {
            tvBreakfastInfo.setText("Breakfast: " + todayMenu.getUnitedBreakfastName() + " .");
            breakfastIngredientsList = todayMenu.generateBreakfastIngredientsArray();
        }

        lunchIngredientsList = new ArrayList<Ingredient>();
        if(todayMenu.hasLunch()) {
            tvLunchInfo.setText("Lunch: " + todayMenu.getUnitedLunchName() + " .");
            lunchIngredientsList = todayMenu.generateLunchIngredientsArray();
        }

        dinnerIngredientsList = new ArrayList<Ingredient>();
        if(todayMenu.hasDinner()) {
            tvDinnerInfo.setText("Dinner: " + todayMenu.getUnitedDinnerName() + " .");
            dinnerIngredientsList = todayMenu.generateDinnerIngredientsArray();
        }
    }

    public void sendToSeeAllIngredients(){
        me.setClass(this, ingredientsPickup.class);
        startActivity(me);
    }

    public void finishFinishMeals(){
        me.setClass(this, MainActivity.class);
        startActivity(me);
    }

    public String getFileData(String fileName){
        String currentLine = "", allData = "";
        try{
            is = openFileInput(fileName);
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);

            currentLine = br.readLine();
            while(currentLine != null){
                allData += currentLine + "\n";
                currentLine = br.readLine();
            }
            br.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return allData;
    }

    public void implementSettingsData(){
        if(getFileData("settings") != null){
            String[] settingsParts = getFileData("settings").split("\n");
            Boolean playMusic, useVideos, useManuallySave;

            playMusic = Boolean.parseBoolean(settingsParts[0].split(": ")[1]);
            useVideos = Boolean.parseBoolean(settingsParts[1].split(": ")[1]);
            useManuallySave = Boolean.parseBoolean(settingsParts[2].split(": ")[1]);
            activeSong = Song.getSongByName(settingsParts[3].split(": ")[1]);

            me.putExtra("playMusic", playMusic);
            me.putExtra("useVideos", useVideos);
            me.putExtra("useManuallySave", useManuallySave);
            me.putExtra("activeSong", activeSong);
        }
    }

    public void initiateMediaPlayer(){
        mediaPlayer = MediaPlayer.create(finishMeals.this, activeSong.getId());
        mediaPlayer.setLooping(true);
        if(me.getBooleanExtra("playMusic", true)){
            mediaPlayer.start();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();
        if(itemID == R.id.sendToMusicMaster){
            me.setClass(finishMeals.this, musicMaster.class);
            me.putExtra("cameToMusicMasterFrom", getLocalClassName());
            startActivity(me);
        }

        if(itemID == R.id.sendToSettings){
            me.setClass(finishMeals.this, settingsSetter.class);
            me.putExtra("cameToSettingsFrom", getLocalClassName());
            startActivity(me);
        }

        if(itemID == R.id.sendToUserScreen){
            me.setClass(finishMeals.this, UserInfoScreen.class);
            me.putExtra("cameToUserScreenFrom", getLocalClassName());
            startActivity(me);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
        if(!me.getBooleanExtra("playMusic", true)){
            mediaPlayer.stop();
        }
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

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        if(viewId == btSendToIngredientsPickup.getId())
            sendToSeeAllIngredients();

        if(viewId == btFinishMeals.getId())
            finishFinishMeals();
    }
}