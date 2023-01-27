package com.example.mydieter9005;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ingredientsPickup extends AppCompatActivity implements View.OnClickListener {

    private MediaPlayer mediaPlayer;

    TextView tvFoodName, tvFoodGrams, tvCounterShow;
    ImageButton ibtNext, ibtPrevious;
    Button btFinishIngredientsPickup;
    ImageView ivFoodImg;

    ArrayList<Ingredient> ingredients, finalIngredients;
    int ingredientsCounter = 0, ingredientsAmount = 0;
    ArrayList<Ingredient> ingredientsToShow;
    String lastClicked = "next";

    DailyMenu todayMenu = DailyMenu.getTodayMenu();
    FileAndDatabaseHelper fileAndDatabaseHelper;
    Song activeSong = Song.getSongs().get(0);

    Intent me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_pickup);

        me = getIntent();
        if(me.hasExtra("activeSong"))
            activeSong = (Song) me.getSerializableExtra("activeSong");

        ingredients = Ingredient.getIngredientsList();  // All the ingredients that inside the app.
        finalIngredients = new ArrayList<Ingredient>();  // The needed ingredients to make the meals.

        tvCounterShow = (TextView) findViewById(R.id.tvCounterShow);
        tvFoodName = (TextView) findViewById(R.id.tvFoodName);
        tvFoodGrams = (TextView) findViewById(R.id.tvFoodGrams);

        ivFoodImg = (ImageView) findViewById(R.id.ivFoodImg);

        btFinishIngredientsPickup = (Button) findViewById(R.id.btFinishIngredientsPickup);
        btFinishIngredientsPickup.setOnClickListener(this);
        ibtPrevious = (ImageButton) findViewById(R.id.ibtPrevious);
        ibtPrevious.setOnClickListener(this);
        ibtNext = (ImageButton) findViewById(R.id.ibtNext);
        ibtNext.setOnClickListener(this);

        fileAndDatabaseHelper = new FileAndDatabaseHelper(this, me);
        activeSong = fileAndDatabaseHelper.implementSettingsData();

        initiateIngredientsToShow();
        initiateMediaPlayer();
    }

    public int getIngredientIndexInArrayList(Ingredient ingredient, ArrayList<Ingredient> ingredientsArrayList){
        for(int i = 0; i < ingredientsArrayList.size(); i++){
            if(ingredientsArrayList.get(i).getName().equals(ingredient.getName()))
                return i;
        }
        return -1;
    }

    public void initiateIngredientsToShow(){
        ingredientsToShow = todayMenu.generateAllIngredientsNeededArrayList();
        ingredientsAmount = ingredientsToShow.size();

        tvCounterShow.setText("Item: " + ingredientsCounter + " out of " + ingredientsAmount);
        if(ingredientsAmount == 0)
            ibtNext.setVisibility(View.INVISIBLE);
    }

    public void nextItem(){
        if(ingredientsCounter + 1 == ingredientsAmount)
            ibtNext.setVisibility(View.INVISIBLE);
        if(ibtPrevious.getVisibility() == View.INVISIBLE && ingredientsCounter != 0)
            ibtPrevious.setVisibility(View.VISIBLE);

        ingredientsCounter++;
        setIngredientInfo();

        lastClicked = "next";
        tvCounterShow.setText("Item: " + ingredientsCounter + " out of " + ingredientsAmount);
    }

    public void previousItem(){
        if(ingredientsCounter == 2)
            ibtPrevious.setVisibility(View.INVISIBLE);
        if(ibtNext.getVisibility() == View.INVISIBLE)
            ibtNext.setVisibility(View.VISIBLE);

        ingredientsCounter--;
        setIngredientInfo();

        lastClicked = "previous";
        tvCounterShow.setText("Item: " + ingredientsCounter + " out of " + ingredientsAmount);
    }

    public void setIngredientInfo(){
        Ingredient ingredient = ingredientsToShow.get(ingredientsCounter - 1);
        tvFoodName.setText("Name: " + ingredient.getName());
        tvFoodGrams.setText("Grams: " + ingredient.getGrams());

        if(getIngredientIndexInArrayList(ingredient, ingredients) != -1)
            ivFoodImg.setImageResource(ingredient.getImgId());
        else
            ivFoodImg.setImageResource(R.drawable.image_not_available);
    }

    public void finishIngredientsPickup(){
        me.setClass(this, MainActivity.class);
        startActivity(me);
    }

    public void initiateMediaPlayer(){
        mediaPlayer = MediaPlayer.create(ingredientsPickup.this, activeSong.getId());
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
            me.setClass(ingredientsPickup.this, musicMaster.class);
            me.putExtra("cameToMusicMasterFrom", getLocalClassName());
            startActivity(me);
        }

        if(itemID == R.id.sendToSettings){
            me.setClass(ingredientsPickup.this, settingsSetter.class);
            me.putExtra("cameToSettingsFrom", getLocalClassName());
            startActivity(me);
        }

        if(itemID == R.id.sendToUserScreen){
            me.setClass(ingredientsPickup.this, UserInfoScreen.class);
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

        if(viewId == ibtNext.getId())
            nextItem();

        if(viewId == ibtPrevious.getId())
            previousItem();

        if(viewId == btFinishIngredientsPickup.getId())
            finishIngredientsPickup();
    }
}