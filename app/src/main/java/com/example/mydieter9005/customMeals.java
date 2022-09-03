package com.example.mydieter9005;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

@SuppressWarnings("SuspiciousRegexArgument")
public class customMeals extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private VideoView videoView;

    Button btFinishCustomize;
    TextView tvInstructions;
    EditText customMeal;
    String[] mealInfo;
    String cameFrom;
    Intent me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_meals);

        me = getIntent();
        cameFrom = me.getStringExtra("cameFrom");
        Toast.makeText(this, "selected meal: " + cameFrom, Toast.LENGTH_SHORT).show();

        btFinishCustomize = (Button) findViewById(R.id.btFinishCustomize);
        videoView = (VideoView) findViewById(R.id.customVideoView);
        tvInstructions = (TextView) findViewById(R.id.tvInstructions);
        customMeal = (EditText) findViewById(R.id.customMeal);

        writeTheInstructions();
        initiateVideoPlayer();

        mediaPlayer = MediaPlayer.create(customMeals.this, R.raw.my_song);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    public void writeTheInstructions(){
        tvInstructions.setText(
                "Make sure to use words like: 'with', 'and' and 'include' instead of using ','." + "\n" +
                "You need to use the special word 'flavored' to make something flavored, like this: " + "\n" +
                "chocolate flavored ice cream or chocolate flavored yogurt and so on..." + "\n" +
                "You can also use 'mini-meals' like: salad, toast and cereals in your text." + "\n" +
                "Not every ingredient have photo so it will just show you the name."
        );
    }

    public void setCustomFood(View v){
        String meal = customMeal.getText().toString();
        String testMeal;
        if(meal.contains(":") && meal.contains(",") && meal.contains(".") && meal.contains(" ")){
            testMeal = meal.replaceAll(":", "");
            if(meal.length() != testMeal.length() + 1){
                Toast.makeText(this, "There should be one ':' .", Toast.LENGTH_SHORT).show();
            }
            else{
                testMeal = meal.replaceAll(",", "");
                if(meal.length() != testMeal.length() + 1){
                    Toast.makeText(this, "There should be one ',' .", Toast.LENGTH_SHORT).show();
                }
                else{
                    testMeal = meal.replaceAll("\\.", "");
                    if(meal.length() != testMeal.length() + 1){
                        Toast.makeText(this, "There should be one '.' .", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if(meal.contains("_") || meal.contains("-")){
                            Toast.makeText(this, "Make sure to use only the format symbols.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            viewInfo();
                        }
                    }
                }
            }
        }
        else{
            Toast.makeText(this, "Make sure to follow the format instructions.", Toast.LENGTH_SHORT).show();
        }
    }

    public void viewInfo(){
        AlertDialog ad;
        AlertDialog.Builder adb;
        adb = new AlertDialog.Builder(this);
        mealInfo = organizeMeal(customMeal.getText().toString());
        adb.setTitle("Your meal is: ");
        adb.setMessage("Name: " + mealInfo[0] + "\n" + "Calories: " + mealInfo[1] + "\n" + "Minutes: " + mealInfo[2]);
        adb.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                customMeal.setText("");
            }
        });

        adb.setNeutralButton("Finish", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishCustomize();
            }
        });
        ad = adb.create();
        ad.show();
    }

    public String[] organizeMeal(String meal){
        String mealName = "", mealCalories = "", mealTime = "";
        int start = 0;
        for(int i = 0; i < meal.length(); i++){
            if(!Character.toString(meal.charAt(i)).equals(":")){
                mealName += Character.toString(meal.charAt(i));
            }
            else {
                start = i + 2;
                break;
            }
        }

        for(int i = start; i < meal.length(); i++){
            if(!Character.toString(meal.charAt(i)).equals(",")){
                mealCalories += Character.toString(meal.charAt(i));
            }
            else {
                start = i + 2;
                break;
            }
        }

        for(int i = start; i < meal.length(); i++){
            mealTime += Character.toString(meal.charAt(i));
        }

        return new String[] {mealName, mealCalories, mealTime};
    }

    public void finishCustomize(){
        me.setClass(customMeals.this, mealsMenu.class);
        me.putExtra(cameFrom, customMeal.getText().toString());
        startActivity(me);
    }

    public void initiateVideoPlayer(){
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.custom_background_video);
        videoView.setVideoURI(uri);
        videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
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

    @Override
    protected void onPostResume() {
        videoView.resume();
        super.onPostResume();
    }

    @Override
    protected void onRestart() {
        videoView.start();
        super.onRestart();
    }

    @Override
    protected void onResume() {
        mediaPlayer.start();
        super.onResume();
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