package com.example.mydieter9005;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.ListView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.ArrayList;

public class lunchSelection extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private VideoView videoView;

    Button btSendLunchToCustomize, btClearLunchSelection, btMultiLunchSelect;
    ArrayList<String> mealsList;
    ArrayAdapter<String> adapter;
    boolean multiSelect = false;
    String chosenLunchName = "";
    int chosenLunchCalories = 0, chosenLunchMinutes = 0, multiSelectCounter = 0;
    ListView listView;
    Intent me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch_selection);

        me = getIntent();

        mealsList = new ArrayList<>();
        mealsList.add("Toast");
        mealsList.add("400");
        mealsList.add("15");

        mealsList.add("Toast with tomato and cucumber");
        mealsList.add("435");
        mealsList.add("20");

        mealsList.add("Toast with tomato");
        mealsList.add("425");
        mealsList.add("18");

        mealsList.add("Toast with cucumber");
        mealsList.add("410");
        mealsList.add("18");

        mealsList.add("Toast with olive and corn");
        mealsList.add("500");
        mealsList.add("20");

        mealsList.add("Toast with olive");
        mealsList.add("440");
        mealsList.add("18");

        mealsList.add("Toast with corn");
        mealsList.add("460");
        mealsList.add("18");

        listView = (ListView) findViewById(R.id.listViewLunch);
        videoView = (VideoView) findViewById(R.id.lunchVideoView);

        btSendLunchToCustomize = (Button) findViewById(R.id.btSendLunchToCustomize);
        btClearLunchSelection = (Button) findViewById(R.id.btClearLunchSelection);
        btMultiLunchSelect = (Button) findViewById(R.id.btMultiLunchSelect);

        setListViewFields();
        initiateVideoPlayer();
        initiateMediaPlayer();
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
                    me.setClass(lunchSelection.this, mealsMenu.class);
                    me.putExtra("lunch", selectedItem);
                    startActivity(me);
                }
                else{
                    String[] mealInfo = multiUsageFunctions.organizeMeal(selectedItem);
                    Toast.makeText(lunchSelection.this, mealInfo[0] + " has added.", Toast.LENGTH_SHORT).show();
                    chosenLunchName += mealInfo[0].toLowerCase() + " and ";
                    chosenLunchCalories += multiUsageFunctions.getCaloriesOrMinutesOutOfString(mealInfo[1]);
                    chosenLunchMinutes += multiUsageFunctions.getCaloriesOrMinutesOutOfString(mealInfo[2]);
                    multiSelectCounter += 1;
                }
            }
        });
    }

    public void multiOrSingleSelectUpdate(View v){
        if(!multiSelect){
            Toast.makeText(this, "Multi select has enabled.", Toast.LENGTH_SHORT).show();
            btMultiLunchSelect.setText("Disable multi select");
            btClearLunchSelection.setText("Finish choosing");
            multiSelectCounter = 0;
            multiSelect = true;
        }
        else{
            Toast.makeText(this, "Multi select has disabled.", Toast.LENGTH_SHORT).show();
            btMultiLunchSelect.setText("Enable multi select");
            btClearLunchSelection.setText("Clear selection");
            multiSelectCounter = 0;
            multiSelect = false;
        }
    }

    public void sendToCustomize(View v){
        me.setClass(lunchSelection.this, customMeals.class);
        me.putExtra("cameFrom", "lunch");
        startActivity(me);
    }

    public void clearLunchSelection(View v){
        if(multiSelect){
            if(multiSelectCounter == 0){
                Toast.makeText(this, "You didn't choose anything yet.", Toast.LENGTH_SHORT).show();
            }
            else{
                chosenLunchName = chosenLunchName.substring(0, chosenLunchName.length() - 5);  // 5 = " and "
                String chosenLunch = chosenLunchName + ": " + chosenLunchCalories + " calories, " + chosenLunchMinutes + " minutes.";
                me.setClass(lunchSelection.this, mealsMenu.class);
                me.putExtra("lunch", chosenLunch);
                startActivity(me);
            }
        }
        else{
            if(me.hasExtra("lunch")){
                me.removeExtra("lunch");
                me.setClass(lunchSelection.this, mealsMenu.class);
                startActivity(me);
            }
            else{
                Toast.makeText(this, "You didn't choose anything yet.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void initiateVideoPlayer(){
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.evening_background_video);
        videoView.setVideoURI(uri);
        videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
    }

    public void initiateMediaPlayer(){
        mediaPlayer = MediaPlayer.create(lunchSelection.this, R.raw.my_song);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
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