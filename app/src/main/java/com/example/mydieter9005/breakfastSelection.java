package com.example.mydieter9005;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.ArrayList;

public class breakfastSelection extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private VideoView videoView;

    Button btSendBreakfastToCustomize, btClearBreakfastSelection, btMultiBreakfastSelect;
    ArrayList<String> mealsList;
    ArrayAdapter<String> adapter;
    boolean multiSelect = false;
    String chosenBreakfastName = "";
    int chosenBreakfastCalories = 0, chosenBreakfastMinutes = 0, multiSelectCounter = 0;
    ListView listView;
    Intent me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breakfast_selection);

        me = getIntent();

        mealsList = new ArrayList<>();
        mealsList.add("Nestle cereals");
        mealsList.add("185");
        mealsList.add("1");

        mealsList.add("Chocolate flavored nestle cereals");
        mealsList.add("230");
        mealsList.add("1");

        mealsList.add("Yogurt");
        mealsList.add("100");
        mealsList.add("1");

        mealsList.add("Chocolate flavored yogurt");
        mealsList.add("200");
        mealsList.add("1");

        mealsList.add("Chocolate flavored ice cream");
        mealsList.add("300");
        mealsList.add("1");

        listView = (ListView) findViewById(R.id.listViewBreakfast);
        videoView = (VideoView) findViewById(R.id.breakfastVideoView);

        btSendBreakfastToCustomize = (Button) findViewById(R.id.btSendBreakfastToCustomize);
        btClearBreakfastSelection = (Button) findViewById(R.id.btClearBreakfastSelection);
        btMultiBreakfastSelect = (Button) findViewById(R.id.btMultiBreakfastSelect);

        setListViewFields();
        initiateVideoPlayer();

        mediaPlayer = MediaPlayer.create(breakfastSelection.this, R.raw.my_song);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    public void setListViewFields(){
        String[] fields = new String[mealsList.size() / 3];
        for(int i = 0; i < mealsList.size(); i += 3){
            String field = mealsList.get(i) + ": " + mealsList.get(i + 1) + " calories, " + mealsList.get(i + 2) + " minutes.";
            fields[i / 3] = field;
        }
        setListViewAdapter(fields);
    }

    public void setListViewAdapter(String[] fields){
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fields);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = (String) adapterView.getItemAtPosition(i);

                if(!multiSelect){
                    me.setClass(breakfastSelection.this, mealsMenu.class);
                    me.putExtra("breakfast", selectedItem);
                    startActivity(me);
                }
                else{
                    String[] mealInfo = organizeMeal(selectedItem);
                    Toast.makeText(breakfastSelection.this, mealInfo[0] + " has added.", Toast.LENGTH_SHORT).show();
                    chosenBreakfastName += mealInfo[0].toLowerCase() + " and ";
                    chosenBreakfastCalories += getCaloriesOrMinutesOutOfString(mealInfo[1]);
                    chosenBreakfastMinutes += getCaloriesOrMinutesOutOfString(mealInfo[2]);
                    multiSelectCounter += 1;
                }
            }
        });
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

    public int getCaloriesOrMinutesOutOfString(String caloriesOrMinutes){
        char currentChar;
        String amount = "";
        for(int i = 0; i < caloriesOrMinutes.length(); i++){
            currentChar = caloriesOrMinutes.charAt(i);
            if(Character.isDigit(currentChar)) {
                amount += currentChar;
            }
        }
        return Integer.parseInt(amount);
    }

    public void sendToCustomize(View v){
        me.setClass(breakfastSelection.this, customMeals.class);
        me.putExtra("cameFrom", "breakfast");
        startActivity(me);
    }

    public void multiOrSingleSelectUpdate(View v){
        if(!multiSelect){
            Toast.makeText(this, "Multi select has enabled.", Toast.LENGTH_SHORT).show();
            btMultiBreakfastSelect.setText("Disable multi select");
            btClearBreakfastSelection.setText("Finish choosing");
            multiSelectCounter = 0;
            multiSelect = true;
        }
        else{
            Toast.makeText(this, "Multi select has disabled.", Toast.LENGTH_SHORT).show();
            btMultiBreakfastSelect.setText("Enable multi select");
            btClearBreakfastSelection.setText("Clear selection");
            multiSelectCounter = 0;
            multiSelect = false;
        }
    }

    public void clearBreakfastSelection(View v){
        if(multiSelect){
            if(multiSelectCounter == 0){
                Toast.makeText(this, "You didn't choose anything yet.", Toast.LENGTH_SHORT).show();
            }
            else{
                chosenBreakfastName = chosenBreakfastName.substring(0, chosenBreakfastName.length() - 5);  // 5 = " and "
                String chosenBreakfast = chosenBreakfastName + ": " + chosenBreakfastCalories + " calories, " + chosenBreakfastMinutes + " minutes.";
                me.setClass(breakfastSelection.this, mealsMenu.class);
                me.putExtra("breakfast", chosenBreakfast);
                startActivity(me);
            }
        }
        else{
            if(me.hasExtra("breakfast")){
                me.removeExtra("breakfast");
                me.setClass(breakfastSelection.this, mealsMenu.class);
                startActivity(me);
            }
            else{
                Toast.makeText(this, "You didn't choose anything yet.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void initiateVideoPlayer(){
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.morning_background_video);
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