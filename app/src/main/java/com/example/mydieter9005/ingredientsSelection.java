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
import android.text.InputType;
import android.text.TextWatcher;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ingredientsSelection extends AppCompatActivity implements View.OnClickListener {

    private VideoView videoView;
    private MediaPlayer mediaPlayer;

    Button btMultiIngredientsSelect, btFinishIngredientsSelection;
    LinearLayout ingredientsSelectionLinearLayout;

    ArrayList<String> ingredientsListViewInfo;
    Song activeSong = Song.getSongs().get(0);
    ArrayList<Ingredient> ingredients;
    ArrayAdapter<Ingredient> adapter;
    Ingredient ingredientSelected;
    EditText etFilterIngredients;
    ListView lvIngredients;

    FileInputStream is;
    InputStreamReader isr;
    BufferedReader br;
    Intent me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_selection);

        me = getIntent();
        if(me.hasExtra("activeSong"))
            activeSong = (Song) me.getSerializableExtra("activeSong");

        ingredientsSelectionLinearLayout = (LinearLayout) findViewById(R.id.ingredientsSelectionLinearLayout);
        videoView = (VideoView) findViewById(R.id.ingredientsSelectionVideoView);

        lvIngredients = (ListView) findViewById(R.id.lvIngredients);

        btFinishIngredientsSelection = (Button) findViewById(R.id.btFinishIngredientsSelection);
        btFinishIngredientsSelection.setOnClickListener(this);
        btMultiIngredientsSelect = (Button) findViewById(R.id.btMultiIngredientsSelect);
        btMultiIngredientsSelect.setOnClickListener(this);

        etFilterIngredients = (EditText) findViewById(R.id.etFilterIngredients);
        etFilterIngredients.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                (ingredientsSelection.this).adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        implementSettingsData();
        initiateMediaPlayer();
        initiateVideoPlayer();
        setAdapters();
    }

    public void initiateIngredientsList(){
        ArrayList<Ingredient> tempIngredients = Ingredient.getIngredientsList();
        ingredients = new ArrayList<Ingredient>();

        for(int i = 0; i < tempIngredients.size(); i++)
            ingredients.add(new Ingredient(tempIngredients.get(i), 100));
    }

//    public void initiateListViewFields(){
//        ingredientsListViewInfo = new ArrayList<String>();
//
//        for(int i = 0; i < ingredients.size(); i++){
//            Ingredient ingredient = ingredients.get(i);
//            ingredientsListViewInfo.add(ingredient.getName());  //  + ": " + ingredient.getProteins() * 100.0 + " proteins, " + ingredient.getFats() * 100.0 + " fats and " + ingredient.getCalories() + " calories."
//        }
//    }

    public void setAdapters() {
        initiateIngredientsList();

        adapter = new ArrayAdapter<Ingredient>(this, android.R.layout.simple_list_item_1, ingredients);
        lvIngredients.setAdapter(adapter);
        lvIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        adb.setNegativeButton("Change grams", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                changeIngredientInfo(ingredient);
            }
        });

        adb.setNeutralButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ingredientSelected = new Ingredient(ingredient);
                finishIngredientsSelection();
            }
        });

        ad = adb.create();
        ad.show();
    }

    public void finishIngredientsSelection(){
        String cameFrom = me.getStringExtra("cameToIngredientsSelectionFrom");
        me.putExtra("addedIngredient", ingredientSelected);

        if(cameFrom.equals("customMeals"))
            me.setClass(ingredientsSelection.this, customMeals.class);
        if(cameFrom.equals("mealModifier"))
            me.setClass(ingredientsSelection.this, mealModifier.class);

        startActivity(me);
    }

    public void changeIngredientInfo(Ingredient ingredient){
        AlertDialog ad;
        AlertDialog.Builder adb;
        adb = new AlertDialog.Builder(this);
        adb.setTitle("Set ingredient new gram value: ");
        adb.setMessage(ingredient.toString());

        final EditText etGetGrams = new EditText(this);
        etGetGrams.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etGetGrams.setText(ingredient.getGrams() + "");
        etGetGrams.setHint("Put new grams here: ");
        adb.setView(etGetGrams);

        adb.setNeutralButton("Change grams", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                double newGrams = Double.parseDouble(etGetGrams.getText().toString());
                ingredient.setGrams(newGrams);
                showIngredientInfo(ingredient);
            }
        });

        ad = adb.create();
        ad.show();
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
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.ingredients_selection_background_video);
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
        mediaPlayer = MediaPlayer.create(ingredientsSelection.this, activeSong.getId());
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
            me.setClass(ingredientsSelection.this, musicMaster.class);
            me.putExtra("cameToMusicMasterFrom", getLocalClassName());
            startActivity(me);
        }

        if(itemID == R.id.sendToSettings){
            me.setClass(ingredientsSelection.this, settingsSetter.class);
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
            ingredientsSelectionLinearLayout.setBackground(getDrawable(R.drawable.ingredients_selection_background));
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

        if(viewId == btFinishIngredientsSelection.getId())
            finishIngredientsSelection();
    }
}