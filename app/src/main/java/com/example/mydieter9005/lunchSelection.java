package com.example.mydieter9005;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class lunchSelection extends AppCompatActivity implements View.OnClickListener {

    private MediaPlayer mediaPlayer;
    private VideoView videoView;

    Button btSendLunchToCustomize, btClearLunchSelection, btMultiLunchSelect;
    ArrayList<Meal> mealsList;
    ArrayAdapter<Meal> adapter;
    boolean multiSelect = false;
    Meal chosenMultiSelectLunch = null;
    int multiSelectCounter = 0;
    Song activeSong = Song.getSongs().get(0);
    ListView listView;

    FileInputStream is;
    InputStreamReader isr;
    BufferedReader br;
    String modifiedMealsFileName = "lunchSelectionModifiedMeals";
    Intent me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch_selection);

        me = getIntent();
        if(me.hasExtra("activeSong"))
            activeSong = (Song) me.getSerializableExtra("activeSong");

        mealsList = new ArrayList<>();

        ArrayList<Ingredient> ingredientsNeeded = new ArrayList<Ingredient>();  // For multi-ingredients meals.

        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("bread"), 150));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("yellow cheese"), 75));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("thousand island dressing"), 25));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("ketchup"), 25));
        mealsList.add(new Meal("Toast", ingredientsNeeded));
        ingredientsNeeded.clear();

        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("bread"), 150));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("yellow cheese"), 75));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("thousand island dressing"), 25));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("ketchup"), 25));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("tomato"), 100));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("cucumber"), 100));
        mealsList.add(new Meal("Toast with tomato and cucumber", ingredientsNeeded));
        ingredientsNeeded.clear();

        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("bread"), 150));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("yellow cheese"), 75));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("thousand island dressing"), 25));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("ketchup"), 25));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("tomato"), 100));
        mealsList.add(new Meal("Toast with tomato", ingredientsNeeded));
        ingredientsNeeded.clear();

        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("bread"), 150));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("yellow cheese"), 75));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("thousand island dressing"), 25));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("ketchup"), 25));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("cucumber"), 100));
        mealsList.add(new Meal("Toast with cucumber", ingredientsNeeded));
        ingredientsNeeded.clear();

        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("bread"), 150));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("yellow cheese"), 75));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("thousand island dressing"), 25));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("ketchup"), 25));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("olive"), 100));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("corn"), 100));
        mealsList.add(new Meal("Toast with olive and corn", ingredientsNeeded));
        ingredientsNeeded.clear();

        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("bread"), 150));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("yellow cheese"), 75));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("thousand island dressing"), 25));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("ketchup"), 25));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("olive"), 100));
        mealsList.add(new Meal("Toast with olive", ingredientsNeeded));
        ingredientsNeeded.clear();

        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("bread"), 150));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("yellow cheese"), 75));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("thousand island dressing"), 25));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("ketchup"), 25));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("corn"), 100));
        mealsList.add(new Meal("Toast with corn", ingredientsNeeded));
        ingredientsNeeded.clear();

        listView = (ListView) findViewById(R.id.listViewLunch);
        videoView = (VideoView) findViewById(R.id.lunchVideoView);

        btSendLunchToCustomize = (Button) findViewById(R.id.btSendLunchToCustomize);
        btSendLunchToCustomize.setOnClickListener(this);
        btClearLunchSelection = (Button) findViewById(R.id.btClearLunchSelection);
        btClearLunchSelection.setOnClickListener(this);
        btMultiLunchSelect = (Button) findViewById(R.id.btMultiLunchSelect);
        btMultiLunchSelect.setOnClickListener(this);

        updateIfMealModified();
        setListViewAdapter();
        initiateVideoPlayer();
        initiateMediaPlayer();
        implementSettingsData();
    }

    public void setListViewAdapter(){
        adapter = new ArrayAdapter<Meal>(this, android.R.layout.simple_list_item_1, mealsList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Meal selectedItem = (Meal) adapterView.getItemAtPosition(i);

                if(!multiSelect){
                    me.setClass(lunchSelection.this, mealsMenu.class);
                    me.putExtra("lunch", selectedItem);
                    startActivity(me);
                }
                else {
                    chosenMultiSelectLunch = new Meal(chosenMultiSelectLunch, selectedItem);
                    Toast.makeText(lunchSelection.this, selectedItem.getName() + " has added.", Toast.LENGTH_SHORT).show();
                    multiSelectCounter++;
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Meal selectedItem = (Meal) parent.getItemAtPosition(position);

                showMealIngredientsInfo(selectedItem);
                return true;
            }
        });
    }

    public void showMealInfo(Meal meal){
        AlertDialog ad;
        AlertDialog.Builder adb;
        adb = new AlertDialog.Builder(this);
        adb.setTitle("Your meal nutrition: ");
        adb.setMessage(meal.getGrams() + " grams." + "\n" + meal.getProteins() + " proteins." + "\n" + meal.getFats() + " fats." + "\n" + meal.getCalories() + " calories.");
        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        adb.setNegativeButton("Ingredients", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showMealIngredientsInfo(meal);
            }
        });

        adb.setNeutralButton("Edit meal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                me.setClass(lunchSelection.this, mealModifier.class);
                me.putExtra("mealToModify", meal);
                me.putExtra("cameToMealModifierFrom", getLocalClassName());
                startActivity(me);
            }
        });

        ad = adb.create();
        ad.show();
    }

    public void showMealIngredientsInfo(Meal meal){
        AlertDialog ad;
        AlertDialog.Builder adb;
        adb = new AlertDialog.Builder(this);
        adb.setTitle("Your meal ingredients: ");
        String mealInfo = "";
        for(Ingredient ingredient : meal.getNeededIngredientsForMeal())
            mealInfo += ingredient.getName() + ": " + ingredient.getGrams() + " grams." + "\n";
        adb.setMessage(mealInfo);
        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        adb.setNegativeButton("Nutrition", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showMealInfo(meal);
            }
        });

        adb.setNeutralButton("Edit meal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                me.setClass(lunchSelection.this, mealModifier.class);
                me.putExtra("mealToModify", meal);
                me.putExtra("cameToMealModifierFrom", getLocalClassName());
                startActivity(me);
            }
        });

        ad = adb.create();
        ad.show();
    }

    public void multiOrSingleSelectUpdate(){
        if(!multiSelect){
            Toast.makeText(this, "Multi select has enabled.", Toast.LENGTH_SHORT).show();
            btMultiLunchSelect.setText("Disable multi select");
            btClearLunchSelection.setText("Finish choosing");
            multiSelectCounter = 0;
            multiSelect = true;
        }
        else{
            Toast.makeText(this, "Multi select has disabled.", Toast.LENGTH_SHORT).show();
            btMultiLunchSelect.setText("Enable multi select");
            btClearLunchSelection.setText("Clear selection");
            chosenMultiSelectLunch = null;
            multiSelectCounter = 0;
            multiSelect = false;
        }
    }

    public void updateIfMealModified(){
        if(me.hasExtra("modifiedMeal")){
            Meal modifiedMeal = (Meal) me.getSerializableExtra("modifiedMeal");
            if(getMealIndexInMealsList(modifiedMeal) != -1)  // Check if exist inside mealsList.
                mealsList.set(getMealIndexInMealsList(modifiedMeal), modifiedMeal);
        }
    }

    public int getMealIndexInMealsList(Meal meal){
        for(int i = 0; i < mealsList.size(); i++){
            if(mealsList.get(i).getName().equals(meal.getName()))
                return i;
        }
        return -1;
    }

    public void sendToCustomize(){
        me.setClass(lunchSelection.this, customMeals.class);
        me.putExtra("cameFrom", "lunch");
        startActivity(me);
    }

    public void clearLunchSelectionOrFinishMultiSelect(){
        if(multiSelect){
            if(multiSelectCounter == 0){
                Toast.makeText(this, "You didn't choose anything yet.", Toast.LENGTH_SHORT).show();
            }
            else{
                me.setClass(lunchSelection.this, mealsMenu.class);
                me.putExtra("lunch", chosenMultiSelectLunch);
                startActivity(me);
            }
        }
        else{
            if(me.hasExtra("lunch")){
                me.removeExtra("lunch");
                me.setClass(lunchSelection.this, mealsMenu.class);
                startActivity(me);
            }
            else{
                Toast.makeText(this, "You didn't choose anything yet.", Toast.LENGTH_SHORT).show();
            }
        }
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
            if(fileName.equals(me.getStringExtra("todayDate")))
                Toast.makeText(this, "Today saved data not exists yet.", Toast.LENGTH_SHORT).show();
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

    public void initiateVideoPlayer(){
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.lunch_selection_background_video);
        videoView.setVideoURI(uri);

        if(me.getBooleanExtra("useVideos", true))
            videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
    }

    public void initiateMediaPlayer(){
        mediaPlayer = MediaPlayer.create(lunchSelection.this, activeSong.getId());
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
            me.setClass(lunchSelection.this, musicMaster.class);
            me.putExtra("cameToMusicMasterFrom", getLocalClassName());
            startActivity(me);
        }

        if(itemID == R.id.sendToSettings){
            me.setClass(lunchSelection.this, settingsSetter.class);
            me.putExtra("cameToSettingsFrom", getLocalClassName());
            startActivity(me);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        videoView.resume();
        if(!me.getBooleanExtra("useVideos", true)){
            findViewById(R.id.lunchSelectionLinearLayout).setBackground(getDrawable(R.drawable.lunch_selection_background));
            videoView.stopPlayback();
        }
        else
            videoView.start();
    }

    @Override
    protected void onRestart() {
        videoView.start();
        super.onRestart();
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
        videoView.suspend();
        mediaPlayer.pause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        videoView.stopPlayback();
        mediaPlayer.stop();
        mediaPlayer.release();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        if(viewId == btSendLunchToCustomize.getId())
            sendToCustomize();

        if(viewId == btMultiLunchSelect.getId())
            multiOrSingleSelectUpdate();

        if(viewId == btClearLunchSelection.getId())
            clearLunchSelectionOrFinishMultiSelect();
    }
}