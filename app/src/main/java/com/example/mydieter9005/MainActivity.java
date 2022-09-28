package com.example.mydieter9005;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    TextView tvBreakfastMain, tvLunchMain, tvDinnerMain;
    TextView tvTotalCaloriesMain, tvCaloriesLeftMain;
    Button btMealsMenu, btWriteMealsToExternalFile;
    int totalCalories, caloriesLeft;
    String[] meals;

    FileOutputStream fos;
    OutputStreamWriter osw;
    BufferedWriter bw;
    String todayDate;

    FileInputStream is;
    InputStreamReader isr;
    BufferedReader br;
    Intent me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        me = getIntent();
        initiateMediaPlayer();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd_MM_yyyy");
        LocalDateTime today = LocalDateTime.now();
        todayDate = dtf.format(today) + " saved file.";

        tvBreakfastMain = (TextView) findViewById(R.id.tvBreakfastMain);
        tvBreakfastMain.setMovementMethod(new ScrollingMovementMethod());
        tvLunchMain = (TextView) findViewById(R.id.tvLunchMain);
        tvLunchMain.setMovementMethod(new ScrollingMovementMethod());
        tvDinnerMain = (TextView) findViewById(R.id.tvDinnerMain);
        tvDinnerMain.setMovementMethod(new ScrollingMovementMethod());

        tvTotalCaloriesMain = (TextView) findViewById(R.id.tvTotalCaloriesMain);
        tvCaloriesLeftMain = (TextView) findViewById(R.id.tvCaloriesLeftMain);

        btMealsMenu = (Button) findViewById(R.id.btMealsMenu);
        btWriteMealsToExternalFile = (Button) findViewById(R.id.btWriteMealsToExternalFile);

        me = createTheFirstIntent(me);
        implementSettingsData();
        updateMealsIfNeeded();
    }

    public void updateMealsIfNeeded(){
        if (me.hasExtra("meals")) {
            meals = me.getStringArrayExtra("meals");
            if (me.hasExtra("breakfast")) {
                tvBreakfastMain.setText("Your breakfast: " + meals[0]);
            }
            if (me.hasExtra("lunch")) {
                tvLunchMain.setText("Your lunch: " + meals[1]);
            }
            if (me.hasExtra("dinner")) {
                tvDinnerMain.setText("Your dinner: " + meals[2]);
            }

            totalCalories = me.getIntExtra("totalCalories", 0);
            tvTotalCaloriesMain.setText("Total calories: " + totalCalories);
            caloriesLeft = 2000 - totalCalories;
            tvCaloriesLeftMain.setText("Calories left: " + caloriesLeft);
        }
    }

    public Intent createTheFirstIntent(Intent me){
        if(!me.hasExtra("exists")){
            me = new Intent(this, mealsMenu.class);
            me.putExtra("exists", true);

            me.putExtra("playMusic", true);
            me.putExtra("useVideos", true);
            me.putExtra("useManuallySave", true);
            me.putExtra("todayDate", todayDate);

            Ingredient.initiateIngredientsList();
            initiateIngredientsPictures();
        }
        return me;
    }

    public void initiateIngredientsPictures(){  // Save all the ingredients pictures IDs.
        String customIngredientName;
        for(Ingredient ingredient : Ingredient.getIngredientsList()) {
            if(Ingredient.getIngredientsList().contains(ingredient)){
                if(ingredient.getName().contains(" ")){
                    customIngredientName = ingredient.getName().replaceAll(" ", "_");
                    ingredient.setImgId(getResources().getIdentifier(customIngredientName, "drawable", getPackageName()));
                }
                else{
                    ingredient.setImgId(getResources().getIdentifier(ingredient.getName(), "drawable", getPackageName()));
                }
            }
        }
    }

    public void sendToSelected(View v) {
        int id = v.getId();
        if (id == btMealsMenu.getId()) {
            me.setClass(this, mealsMenu.class);
            startActivity(me);
        }
    }

    public void write(View v){
        if(me.hasExtra("meals")){
            try {
                fos = openFileOutput(todayDate, Context.MODE_PRIVATE);
                osw = new OutputStreamWriter(fos);
                bw = new BufferedWriter(osw);

                bw.write(todayDate + "\n");

                bw.write("Your breakfast: ");
                if(me.hasExtra("breakfast"))
                    bw.write(me.getStringExtra("breakfast"));

                bw.write("\n" + "Your lunch: ");
                if(me.hasExtra("lunch"))
                    bw.write(me.getStringExtra("lunch"));

                bw.write("\n" + "Your dinner: ");
                if(me.hasExtra("dinner"))
                    bw.write(me.getStringExtra("dinner"));

                bw.write("\n" + tvTotalCaloriesMain.getText().toString() + "\n");
                bw.write(tvCaloriesLeftMain.getText().toString());

                bw.close();

                Toast.makeText(this, todayDate + " wrote.", Toast.LENGTH_SHORT).show();
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            Toast.makeText(this, "You didn't choose any meal yet.", Toast.LENGTH_SHORT).show();
        }
    }

    public void showFileData(View v){
        String[] dataParts = getFileData(todayDate).split("\n");
        String savedBreakfast="", savedLunch="", savedDinner="", savedTotalCalories="", savedCaloriesLeft="";
        String[] meals = new String[3];

        if(dataParts[1].replaceAll(":", "").length() == dataParts[1].length() - 2)
            savedBreakfast = dataParts[1].split(": ")[1] + ": " + dataParts[1].split(": ")[2];
        if(!savedBreakfast.equals("") && !savedBreakfast.equals(" "))
            me.putExtra("breakfast", savedBreakfast);

        if(dataParts[2].replaceAll(":", "").length() == dataParts[2].length() - 2)
            savedLunch = dataParts[2].split(": ")[1] + ": " + dataParts[2].split(": ")[2];
        if(!savedLunch.equals("") && !savedLunch.equals(" "))
            me.putExtra("lunch", savedLunch);

        if(dataParts[3].replaceAll(":", "").length() == dataParts[3].length() - 2)
            savedDinner = dataParts[3].split(": ")[1] + ": " + dataParts[3].split(": ")[2];
        if(!savedDinner.equals("") && !savedDinner.equals(" "))
            me.putExtra("dinner", savedDinner);

        savedTotalCalories = dataParts[4].split(": ")[1];
        if(!savedTotalCalories.equals("") && !savedTotalCalories.equals(" "))
            me.putExtra("totalCalories", multiUsageFunctions.getCaloriesOrMinutesOutOfString(savedTotalCalories));
        savedCaloriesLeft = dataParts[5].split(": ")[1];
        if(!savedCaloriesLeft.equals("") && !savedCaloriesLeft.equals(" "))
            me.putExtra("caloriesLeft", multiUsageFunctions.getCaloriesOrMinutesOutOfString(savedCaloriesLeft));

        meals[0] = savedBreakfast.split(":")[0];
        meals[1] = savedLunch.split(":")[0];
        meals[2] = savedDinner.split(":")[0];
        me.putExtra("meals", meals);

        updateMealsIfNeeded();
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

            me.putExtra("playMusic", playMusic);
            me.putExtra("useVideos", useVideos);
            me.putExtra("useManuallySave", useManuallySave);
        }
    }

    public void initiateMediaPlayer(){
        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.my_song);
        mediaPlayer.setLooping(true);
        if(me.getBooleanExtra("playMusic", true)){
            mediaPlayer.start();
        }
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
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();
        if(itemID == R.id.musicController){
            if(mediaPlayer.isPlaying()){
                me.putExtra("playMusic", false);
                item.setIcon(R.drawable.ic_music_off_icon);
                mediaPlayer.pause();
            }
            else{
                me.putExtra("playMusic", true);
                item.setIcon(R.drawable.ic_music_on_icon);
                initiateMediaPlayer();
                mediaPlayer.start();
            }
        }

        if(itemID == R.id.sendToSettings){
            me.setClass(MainActivity.this, settingsSetter.class);
            me.putExtra("cameToSettingsFrom", getLocalClassName());
            startActivity(me);
        }
        return super.onOptionsItemSelected(item);
    }
}
