package com.example.mydieter9005;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.VideoView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class dinnerSelection extends AppCompatActivity implements View.OnClickListener {

    private MediaPlayer mediaPlayer;
    private VideoView videoView;

    Button btSendDinnerToCustomize, btClearDinnerSelection, btBackFromDinnerSelect;
    EditText etFilterDinner;
    ListView listView;

    DailyMenu todayMenu = DailyMenu.getTodayMenu();
    Song activeSong = Song.getSongs().get(0);
    ArrayAdapter<Meal> adapter;
    ArrayList<Meal> mealsList;

    FileInputStream is;
    InputStreamReader isr;
    BufferedReader br;
    Intent me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dinner_selection);

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

        listView = (ListView) findViewById(R.id.listViewDinner);
        videoView = (VideoView) findViewById(R.id.dinnerVideoView);

        btSendDinnerToCustomize = (Button) findViewById(R.id.btSendDinnerToCustomize);
        btSendDinnerToCustomize.setOnClickListener(this);
        btClearDinnerSelection = (Button) findViewById(R.id.btClearDinnerSelection);
        btClearDinnerSelection.setOnClickListener(this);
        btBackFromDinnerSelect = (Button) findViewById(R.id.btBackFromDinnerSelect);
        btBackFromDinnerSelect.setOnClickListener(this);

        etFilterDinner = (EditText) findViewById(R.id.etFilterDinner);
        etFilterDinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                (dinnerSelection.this).adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

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

                todayMenu.addDinner(selectedItem);

                me.setClass(dinnerSelection.this, mealsMenu.class);
                startActivity(me);
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

        ad = adb.create();
        ad.show();
    }

    public void sendToCustomize(){
        me.setClass(dinnerSelection.this, customMeals.class);
        me.putExtra("cameFrom", "dinner");
        startActivity(me);
    }

    public void backToMealsMenu(){
        me.setClass(dinnerSelection.this, mealsMenu.class);
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

    public void initiateVideoPlayer(){
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.dinner_selection_background_video);
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
        mediaPlayer = MediaPlayer.create(dinnerSelection.this, activeSong.getId());
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
            me.setClass(dinnerSelection.this, musicMaster.class);
            me.putExtra("cameToMusicMasterFrom", getLocalClassName());
            startActivity(me);
        }

        if(itemID == R.id.sendToSettings){
            me.setClass(dinnerSelection.this, settingsSetter.class);
            me.putExtra("cameToSettingsFrom", getLocalClassName());
            startActivity(me);
        }

        if(itemID == R.id.sendToUserScreen){
            me.setClass(dinnerSelection.this, UserInfoScreen.class);
            me.putExtra("cameToUserScreenFrom", getLocalClassName());
            startActivity(me);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        videoView.resume();
        if(!me.getBooleanExtra("useVideos", true)){
            findViewById(R.id.dinnerSelectionLinearLayout).setBackground(getDrawable(R.drawable.dinner_selection_background));
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

        if(viewId == btSendDinnerToCustomize.getId())
            sendToCustomize();

        if(viewId == btClearDinnerSelection.getId())
            todayMenu.getDinner().clear();

        if(viewId == btBackFromDinnerSelect.getId())
            backToMealsMenu();
    }
}