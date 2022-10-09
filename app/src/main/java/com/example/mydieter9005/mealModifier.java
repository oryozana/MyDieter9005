package com.example.mydieter9005;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class mealModifier extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private VideoView videoView;

    Button btDoNotSaveMealAndFinish, btSaveMealAndFinish;
    LinearLayout mealModifierLinearLayout;
    ListView listView;

    ArrayList<Ingredient> ingredientsForMeal = new ArrayList<Ingredient>();
    Song activeSong = Song.getSongs().get(0);
    ArrayAdapter<Ingredient> adapter;
    Meal meal;

    FileOutputStream fos;
    OutputStreamWriter osw;
    BufferedWriter bw;

    FileInputStream is;
    InputStreamReader isr;
    BufferedReader br;

    String cameFrom;
    Intent me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_modifier);

        me = getIntent();
        if(me.hasExtra("activeSong"))
            activeSong = (Song) me.getSerializableExtra("activeSong");

        meal = (Meal)(me.getSerializableExtra("mealToModify"));
        cameFrom = me.getStringExtra("cameToMealModifierFrom");

        videoView = (VideoView) findViewById(R.id.mealModifierVideoView);
        mealModifierLinearLayout = (LinearLayout) findViewById(R.id.mealModifierLinearLayout);

        listView = (ListView) findViewById(R.id.listViewMealIngredientsModifier);
        btDoNotSaveMealAndFinish = (Button) findViewById(R.id.btDoNotSaveMealAndFinish);
        btSaveMealAndFinish = (Button) findViewById(R.id.btSaveMealAndFinish);

        initiateListViewFields();
        setListViewAdapter();
        implementSettingsData();
        initiateVideoPlayer();
        initiateMediaPlayer();
    }

    public void initiateListViewFields(){
        ingredientsForMeal.clear();
        for(int i = 0; i < meal.getNeededIngredientsForMeal().size(); i++)
            ingredientsForMeal.add(new Ingredient(meal.getNeededIngredientsForMeal().get(i)));
    }

    public void setListViewAdapter(){
        adapter = new ArrayAdapter<Ingredient>(this, android.R.layout.simple_list_item_1, ingredientsForMeal);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Ingredient selectedItem = (Ingredient) adapterView.getItemAtPosition(i);

                showIngredientEditor(selectedItem);
            }
        });
    }

    public void updateListView(){
        ArrayAdapter<Ingredient> adapter = new ArrayAdapter<Ingredient>(this, android.R.layout.simple_list_item_1, ingredientsForMeal);
        listView.setAdapter(adapter);
    }

    public void showIngredientEditor(Ingredient ingredient){
        AlertDialog ad;
        AlertDialog.Builder adb;
        adb = new AlertDialog.Builder(this);
        adb.setTitle("Write ingredients grams here: ");

        ArrayList<Ingredient> ingredients = meal.getNeededIngredientsForMeal();
        int ingredientIndex = meal.getIngredientIndexInMealIngredients(ingredient);
        adb.setMessage("Ingredient grams was: " + ingredients.get(ingredientIndex).getGrams());

        final EditText etGetGrams = new EditText(mealModifier.this);
        etGetGrams.setText(ingredient.getGrams() + "");
        etGetGrams.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        adb.setView(etGetGrams);

        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        adb.setNegativeButton("Change grams", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                double newGramsValue = Double.parseDouble(etGetGrams.getText().toString());
                if(newGramsValue <= 0)
                    Toast.makeText(mealModifier.this, "Enter an amount greater than 0 grams.", Toast.LENGTH_SHORT).show();
                else {
                    ingredient.setGrams(newGramsValue);
                    updateListView();
                }
            }
        });

        adb.setNeutralButton("Remove", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ingredientsForMeal.remove(ingredientIndex);
                updateListView();
            }
        });

        ad = adb.create();
        ad.show();
    }

    public void finish(View v){
        if(v.getId() == btSaveMealAndFinish.getId()) {
            meal.setNeededIngredientsForMeal(ingredientsForMeal);
            saveModifiedMeal(meal);
        }
        ingredientsForMeal.clear();

        me.putExtra("modifiedMeal", meal);
        if(cameFrom.equals("breakfastSelection"))
            me.setClass(mealModifier.this, breakfastSelection.class);
        if(cameFrom.equals("lunchSelection"))
            me.setClass(mealModifier.this, lunchSelection.class);
        if(cameFrom.equals("dinnerSelection"))
            me.setClass(mealModifier.this, dinnerSelection.class);
        if(cameFrom.equals("customSelection"))
            me.setClass(mealModifier.this, customSelection.class);
        startActivity(me);
    }

    public void saveModifiedMeal(Meal modifiedMeal){
        String fileName = "";
        if(cameFrom.equals("breakfastSelection"))
            fileName = "breakfastSelectionModifiedMeals";
        if(cameFrom.equals("lunchSelection"))
            fileName = "lunchSelectionModifiedMeals";
        if(cameFrom.equals("dinnerSelection"))
            fileName = "dinnerSelectionModifiedMeals";
        if(cameFrom.equals("customSelection"))
            fileName = "customSelectionModifiedMeals";

        try {
            fos = openFileOutput(fileName, Context.MODE_APPEND);
            osw = new OutputStreamWriter(fos);
            bw = new BufferedWriter(osw);

            bw.write(fileName + "\n");

            bw.write(modifiedMeal.getName() + " , " + modifiedMeal.getGrams() + " , ");
            bw.write(modifiedMeal.getProteins() + " , " + modifiedMeal.getFats() + " , ");
            bw.write(modifiedMeal.getCalories() + "\n");

            bw.close();
            Toast.makeText(this, modifiedMeal.getName() + " added to " + fileName + " file.", Toast.LENGTH_SHORT).show();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public boolean isMealInFile();
//
//    public String[] getModifiedMealsNamesFromFiles(){
//        String[] mealsNames = getFileData(cameFrom).split(" , ");
//        if(mealsNames.length > 1){
//
//        }
//    }

    public String getFileData(String fileName) {
        String currentLine = "", allData = "";
        try {
            is = openFileInput(fileName);
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);

            currentLine = br.readLine();
            currentLine = br.readLine(); // Skip the file name.
            while (currentLine != null) {
                allData += currentLine + "\n";
                currentLine = br.readLine();
            }
            br.close();
        } catch (FileNotFoundException e) {
            if (fileName.equals(me.getStringExtra("todayDate")))
                Toast.makeText(this, "Today saved data not exists yet.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
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

    public void initiateVideoPlayer() {
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.meal_modifier_background_video);
        videoView.setVideoURI(uri);

        if (me.getBooleanExtra("useVideos", true))
            videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
    }

    public void initiateMediaPlayer() {
        mediaPlayer = MediaPlayer.create(mealModifier.this, activeSong.getId());
        mediaPlayer.setLooping(true);
        if (me.getBooleanExtra("playMusic", true)) {
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
            me.setClass(mealModifier.this, musicMaster.class);
            me.putExtra("cameToMusicMasterFrom", getLocalClassName());
            startActivity(me);
        }

        if(itemID == R.id.sendToSettings){
            me.setClass(mealModifier.this, settingsSetter.class);
            me.putExtra("cameToSettingsFrom", getLocalClassName());
            startActivity(me);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        videoView.resume();
        if (!me.getBooleanExtra("useVideos", true)) {
            findViewById(R.id.mealModifierLinearLayout).setBackground(getDrawable(R.drawable.meal_modifier_background));
            videoView.stopPlayback();
        } else
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
        if (!me.getBooleanExtra("playMusic", true)) {
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
}
