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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.mydieter9005.databinding.ActivityCustomMealsBinding;
import com.example.mydieter9005.databinding.ActivityCustomSelectionBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class customSelection extends AppCompatActivity implements View.OnClickListener {

    private MediaPlayer mediaPlayer;
    private VideoView videoView;

    Button btSentToWorldSavedCustomMeals, btFinishCustomSelection;
    ListView listView;

    ArrayList<Meal> customMealsList;
    MealListAdapter adapter;

    String[] customMealsNames;
    String cameFrom;

    DailyMenu todayMenu = DailyMenu.getTodayMenu();
    Song activeSong = Song.getSongs().get(0);

    FirebaseDatabase recipesDb;
    DatabaseReference databaseReference;
    String userName = User.getCurrentUser().getUsername();

    FileInputStream is;
    InputStreamReader isr;
    BufferedReader br;
    String fileName = "customMealsNames";

    Intent me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_selection);

        me = getIntent();
        if(me.hasExtra("activeSong"))
            activeSong = (Song) me.getSerializableExtra("activeSong");
        cameFrom = me.getStringExtra("cameFrom");

        listView = (ListView) findViewById(R.id.listViewCustom);
        videoView = (VideoView) findViewById(R.id.customSelectionVideoView);

        btSentToWorldSavedCustomMeals = (Button) findViewById(R.id.btSentToWorldSavedCustomMeals);
        btSentToWorldSavedCustomMeals.setOnClickListener(this);
        btFinishCustomSelection = (Button) findViewById(R.id.btFinishCustomSelection);
        btFinishCustomSelection.setOnClickListener(this);

        initiateCustomMealsList();
        implementSettingsData();
        initiateVideoPlayer();
        initiateMediaPlayer();

        checkIfAtLeastOneCustomMealAdded();
    }

    public void initiateCustomMealsList(){
        customMealsList = new ArrayList<Meal>();
        customMealsNames = getSavedCustomMealsNames();

        for(int i = 0; i < customMealsNames.length - 1; i++)  // - 1 to get rid of the empty last element in this array.
            customMealsList.add(new Meal(customMealsNames[i], getIngredientsFromFileByCustomMealName(customMealsNames[i])));

        setListViewAdapter();
    }

    public String[] getSavedCustomMealsNames(){
        String[] customMealsNames = getFileData(fileName).split("\n");

        for(int i = 1; i < customMealsNames.length; i++)  // Get rid of first line.
            customMealsNames[i - 1] = customMealsNames[i];

        customMealsNames[customMealsNames.length - 1] = "";
        return customMealsNames;
    }

    public ArrayList<Ingredient> getIngredientsFromFileByCustomMealName(String mealName){
        String[] mealIngredients = getFileData("customMeal: " + mealName).split("\n");
        ArrayList<Ingredient> ingredientsFound = new ArrayList<Ingredient>();

        for(int i = 1; i < mealIngredients.length; i++){  // i = 1 to skip the custom meal name line.
            String name = getName(mealIngredients[i]);
            double grams = getGrams(mealIngredients[i]);

            Ingredient ingredient = Ingredient.getIngredientByName(name);
            ingredientsFound.add(new Ingredient(ingredient, grams));
        }

        return ingredientsFound;
    }

    public String getName(String nameAndGrams){  // Needed when get ingredient from file.
        return nameAndGrams.split(": ")[0];
    }

    public double getGrams(String nameAndGrams){  // Needed when get ingredient from file.
        return Double.parseDouble((nameAndGrams.split(": ")[1]).split(" ")[0]);
    }

    public void checkIfAtLeastOneCustomMealAdded(){
        String[] savedCustomMealsNames = getSavedCustomMealsNames();

        if(savedCustomMealsNames.length == 1){
            notEvenOneCustomMealAdded();
        }
    }

    public void notEvenOneCustomMealAdded(){
        AlertDialog ad;
        AlertDialog.Builder adb;
        adb = new AlertDialog.Builder(this);
        adb.setTitle("Custom meals not found!");
        adb.setMessage("It's seems like you didn't saved any custom meal so far, give it a try and come back again.");
        adb.setIcon(R.drawable.ic_food_icon);
        adb.setCancelable(false);

        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                me.setClass(customSelection.this, customMeals.class);
                startActivity(me);
            }
        });

        adb.setNegativeButton("World saved", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendToWorldSavedCustomSelection();
            }
        });

        ad = adb.create();
        ad.show();
    }

    public void setListViewAdapter(){
        adapter = new MealListAdapter(this, customMealsList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Meal selectedItem = (Meal) adapterView.getItemAtPosition(i);

                if(cameFrom.equals("breakfast"))
                    todayMenu.addBreakfast(selectedItem);

                if(cameFrom.equals("lunch"))
                    todayMenu.addLunch(selectedItem);

                if(cameFrom.equals("dinner"))
                    todayMenu.addDinner(selectedItem);

                me.setClass(customSelection.this, mealsMenu.class);
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
        adb.setMessage(meal.getMealInfo());
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

        adb.setNegativeButton("Publish", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ArrayList<String> mealIngredientsInfo = new ArrayList<String>();
                for(int i = 0; i < meal.getNeededIngredientsForMeal().size(); i++)
                    mealIngredientsInfo.add(meal.getNeededIngredientsForMeal().get(i).toString());

                recipesDb = FirebaseDatabase.getInstance();
                databaseReference = recipesDb.getReference("recipes");
                databaseReference.child(userName + " - " + meal.getName()).setValue(mealIngredientsInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(customSelection.this, "Successfully published.", Toast.LENGTH_SHORT).show();
                    }
                });
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

        adb.setNegativeButton("Publish", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ArrayList<String> mealIngredientsInfo = new ArrayList<String>();
                for(int i = 0; i < meal.getNeededIngredientsForMeal().size(); i++)
                    mealIngredientsInfo.add(meal.getNeededIngredientsForMeal().get(i).toString());

                recipesDb = FirebaseDatabase.getInstance();
                databaseReference = recipesDb.getReference("recipes");
                databaseReference.child(userName + " - " + meal.getName()).setValue(mealIngredientsInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(customSelection.this, "Successfully published.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        ad = adb.create();
        ad.show();
    }

    public void returnToCustomize(){
        me.setClass(customSelection.this, customMeals.class);
        startActivity(me);
    }

    public void sendToWorldSavedCustomSelection(){
        me.setClass(customSelection.this, WorldSavedCustomMeals.class);
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
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.custom_selection_background_video);
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
        mediaPlayer = MediaPlayer.create(customSelection.this, activeSong.getId());
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
            me.setClass(customSelection.this, musicMaster.class);
            me.putExtra("cameToMusicMasterFrom", getLocalClassName());
            startActivity(me);
        }

        if(itemID == R.id.sendToSettings){
            me.setClass(customSelection.this, settingsSetter.class);
            me.putExtra("cameToSettingsFrom", getLocalClassName());
            startActivity(me);
        }

        if(itemID == R.id.sendToUserScreen){
            me.setClass(customSelection.this, UserInfoScreen.class);
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
            findViewById(R.id.customSelectionLinearLayout).setBackground(getDrawable(R.drawable.custom_selection_background));
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

        if(viewId == btSentToWorldSavedCustomMeals.getId())
            sendToWorldSavedCustomSelection();

        if(viewId == btFinishCustomSelection.getId())
            returnToCustomize();
    }
}