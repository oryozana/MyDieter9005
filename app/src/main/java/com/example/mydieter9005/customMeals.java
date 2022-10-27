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
import android.widget.TextView;
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

public class customMeals extends AppCompatActivity implements View.OnClickListener {

    private MediaPlayer mediaPlayer;
    private VideoView videoView;

    Button btSendToCustomSelection, btShowMealInfo, btFinishCustomize;
    Song activeSong = Song.getSongs().get(0);
    Ingredient addedIngredient;
    ListView lvCustomMealIngredients;
    ArrayAdapter<Ingredient> adapter;
    Meal customMeal;
    EditText etCustomMeal;
    String cameFrom;

    FileOutputStream fos;
    OutputStreamWriter osw;
    BufferedWriter bw;
    String fileName = "customMealsNames";

    FileInputStream is;
    InputStreamReader isr;
    BufferedReader br;
    Intent me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_meals);

        me = getIntent();
        if(me.hasExtra("activeSong"))
            activeSong = (Song) me.getSerializableExtra("activeSong");

        etCustomMeal = (EditText) findViewById(R.id.etCustomMeal);

        if(me.hasExtra("addedIngredient")){
            addedIngredient = (Ingredient) me.getSerializableExtra("addedIngredient");
            me.removeExtra("addedIngredient");
        }

        if(me.hasExtra("savedMeal")) {
            customMeal = (Meal) me.getSerializableExtra("savedMeal");
            customMeal.addNeededIngredientForMeal(addedIngredient);
            etCustomMeal.setText(customMeal.getName());
        }
        else
            customMeal = new Meal("");

        cameFrom = me.getStringExtra("cameFrom");

        videoView = (VideoView) findViewById(R.id.customVideoView);

        btSendToCustomSelection = (Button) findViewById(R.id.btSendToCustomSelection);
        btSendToCustomSelection.setOnClickListener(this);
        btShowMealInfo = (Button) findViewById(R.id.btShowMealInfo);
        btShowMealInfo.setOnClickListener(this);
        btFinishCustomize = (Button) findViewById(R.id.btFinishCustomize);
        btFinishCustomize.setOnClickListener(this);

        lvCustomMealIngredients = (ListView) findViewById(R.id.lvCustomMealIngredients);

        etCustomMeal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                customMeal.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        setAdapters();
        initiateVideoPlayer();
        initiateMediaPlayer();
        implementSettingsData();
    }

    public void setAdapters() {
        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        if(customMeal != null)
            ingredients = customMeal.getNeededIngredientsForMeal();

        adapter = new ArrayAdapter<Ingredient>(this, android.R.layout.simple_list_item_1, ingredients);
        lvCustomMealIngredients.setAdapter(adapter);
        lvCustomMealIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Ingredient selectedItem = (Ingredient) parent.getItemAtPosition(position);

                showIngredientInfo(selectedItem);
            }
        });
    }

    public void showIngredientInfo(Ingredient ingredient){
        AlertDialog ad;
        AlertDialog.Builder adb;
        adb = new AlertDialog.Builder(this);
        adb.setTitle("Your ingredient: ");
        adb.setMessage(ingredient.getIngredientInfo());
        adb.setIcon(ingredient.getImgId());

        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        ad = adb.create();
        ad.show();
    }


    public void showMealInfo(){
        AlertDialog ad;
        AlertDialog.Builder adb;
        adb = new AlertDialog.Builder(this);
        adb.setTitle("Your meal nutrition: ");
        adb.setMessage(customMeal.getGrams() + " grams." + "\n" + customMeal.getProteins() + " proteins." + "\n" + customMeal.getFats() + " fats." + "\n" + customMeal.getCalories() + " calories.");
        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        adb.setNegativeButton("Ingredients", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showMealIngredientsInfo();
            }
        });

        adb.setNeutralButton("Edit meal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                me.setClass(customMeals.this, mealModifier.class);
                me.putExtra("mealToModify", customMeal);
                me.putExtra("cameToMealModifierFrom", getLocalClassName());
                startActivity(me);
            }
        });

        ad = adb.create();
        ad.show();
    }

    public void showMealIngredientsInfo(){
        AlertDialog ad;
        AlertDialog.Builder adb;
        adb = new AlertDialog.Builder(this);
        adb.setTitle("Your meal ingredients: ");
        String mealInfo = "";
        for(Ingredient ingredient : customMeal.getNeededIngredientsForMeal())
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
                showMealInfo();
            }
        });

        adb.setNeutralButton("Edit meal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                me.setClass(customMeals.this, mealModifier.class);
                me.putExtra("mealToModify", customMeal);
                me.putExtra("cameToMealModifierFrom", getLocalClassName());
                startActivity(me);
            }
        });

        ad = adb.create();
        ad.show();
    }

    public void saveCustomMealInAFile(){
        boolean passTheTests = true;

        if(customMeal == null){
            passTheTests = false;
            Toast.makeText(this, "You didn't create any meal yet.", Toast.LENGTH_SHORT).show();
        }
        else{
            if(customMeal.getName().replaceAll(" ", "").equals("")){
                passTheTests = false;
                Toast.makeText(this, "You didn't give your meal a name.", Toast.LENGTH_SHORT).show();
            }
            else{
                if(customMeal.getNeededIngredientsForMeal().size() == 0){
                    passTheTests = false;
                    Toast.makeText(this, "You need to add at least one ingredient.", Toast.LENGTH_SHORT).show();
                }
            }
        }

        if(passTheTests){
            try {
                fos = openFileOutput("customMeal: " + customMeal.getName(), Context.MODE_PRIVATE);
                osw = new OutputStreamWriter(fos);
                bw = new BufferedWriter(osw);

                bw.write(customMeal.getName() + "\n");

                bw.write(customMeal.getNeededIngredientsForMeal().get(0).toString());
                for(int i = 1; i < customMeal.getNeededIngredientsForMeal().size(); i++)
                    bw.write("\n" + customMeal.getNeededIngredientsForMeal().get(i).toString());

                bw.close();

                saveCustomMealNameInsideFile();
                Toast.makeText(this, customMeal.getName() + " added.", Toast.LENGTH_SHORT).show();
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveCustomMealNameInsideFile(){
        try {
            fos = openFileOutput(fileName, Context.MODE_APPEND);
            osw = new OutputStreamWriter(fos);
            bw = new BufferedWriter(osw);

            bw.write(customMeal.getName() + "\n");

            bw.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] getSavedCustomMealsNames(){
        String[] customMealsNames = getFileData(fileName).split("\n");

        for(int i = 1; i < customMealsNames.length; i++)  // Get rid of first line.
            customMealsNames[i - 1] = customMealsNames[i];

        return customMealsNames;
    }

    public ArrayList<Ingredient> getIngredientsFromFile(String mealName){
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

    public void viewFinalMeal(){
        AlertDialog ad;
        AlertDialog.Builder adb;
        adb = new AlertDialog.Builder(this);
        adb.setTitle("Your meal is: ");
        adb.setMessage(customMeal.getMealInfo());
        adb.setNegativeButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        adb.setNeutralButton("Finish", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishCustomize();
            }
        });

        adb.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                saveCustomMealInAFile();
            }
        });

        ad = adb.create();
        ad.show();
    }

    public void sendToCustomSelection(){
        me.setClass(customMeals.this, customSelection.class);
        startActivity(me);
    }

    public void finishCustomize(){
        me.setClass(customMeals.this, mealsMenu.class);
        me.putExtra(cameFrom, etCustomMeal.getText().toString());
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
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.custom_background_video);
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
        mediaPlayer = MediaPlayer.create(customMeals.this, activeSong.getId());
        mediaPlayer.setLooping(true);
        if(me.getBooleanExtra("playMusic", true)){
            mediaPlayer.start();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.modify_and_custom_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();
        if(itemID == R.id.sendToIngredientsSelection){
            me.setClass(customMeals.this, ingredientsSelection.class);
            me.putExtra("cameToIngredientsSelectionFrom", getLocalClassName());
            me.putExtra("savedMeal", customMeal);
            startActivity(me);
        }

        if(itemID == R.id.sendToMusicMaster){
            me.setClass(customMeals.this, musicMaster.class);
            me.putExtra("cameToMusicMasterFrom", getLocalClassName());
            startActivity(me);
        }

        if(itemID == R.id.sendToSettings){
            me.setClass(customMeals.this, settingsSetter.class);
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
            findViewById(R.id.customMealsLinearLayout).setBackground(getDrawable(R.drawable.custom_meals_background));
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

        if(viewId == btSendToCustomSelection.getId())
            sendToCustomSelection();

        if(viewId == btShowMealInfo.getId())
            showMealInfo();

        if(viewId == btFinishCustomize.getId())
            viewFinalMeal();
    }
}