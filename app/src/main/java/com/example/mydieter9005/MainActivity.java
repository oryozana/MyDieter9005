package com.example.mydieter9005;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
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

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.Clock;
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
    Intent me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        me = getIntent();
        me = createTheFirstIntent(me);
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
        }
        return me;
    }

    public void sendToSelected(View v) {
        int id = v.getId();
        if (id == btMealsMenu.getId()) {
            me.setClass(this, mealsMenu.class);
            startActivity(me);
        }
    }

    public void write(View v){
        try {
            fos = openFileOutput(todayDate, Context.MODE_PRIVATE);
            Toast.makeText(this, todayDate, Toast.LENGTH_SHORT).show();
            osw = new OutputStreamWriter(fos);
            bw = new BufferedWriter(osw);

            bw.write(todayDate + "\n");
            bw.write(tvBreakfastMain.getText().toString() + "\n");
            bw.write(tvLunchMain.getText().toString() + "\n");
            bw.write(tvDinnerMain.getText().toString() + "\n");

            bw.write(tvTotalCaloriesMain.getText().toString() + "\n");
            bw.write(tvCaloriesLeftMain.getText().toString());

            bw.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initiateMediaPlayer(){
        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.my_song);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
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
        inflater.inflate(R.menu.music_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();
        if(itemID == R.id.musicController){
            if(mediaPlayer.isPlaying()){
                mediaPlayer.pause();
                item.setIcon(R.drawable.ic_music_off_icon);
            }
            else{
                mediaPlayer.start();
                item.setIcon(R.drawable.ic_music_on_icon);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
