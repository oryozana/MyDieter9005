package com.example.mydieter9005;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
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

    Button btSendBreakfastToCustomize, btClearBreakfastSelection;
    ArrayList<String> mealsList;
    ArrayAdapter<String> adapter;
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

        mealsList.add("Yogurt (small)");
        mealsList.add("100");
        mealsList.add("1");

        mealsList.add("Chocolate flavored yogurt");
        mealsList.add("200");
        mealsList.add("1");

        listView = (ListView) findViewById(R.id.listViewBreakfast);
        videoView = (VideoView) findViewById(R.id.breakfastVideoView);

        btSendBreakfastToCustomize = (Button) findViewById(R.id.btSendBreakfastToCustomize);
        btClearBreakfastSelection = (Button) findViewById(R.id.btClearBreakfastSelection);

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

                me.setClass(breakfastSelection.this, mealsMenu.class);
                me.putExtra("breakfast", selectedItem);
                startActivity(me);
            }
        });
    }

    public void sendToCustomize(View v){
        me.setClass(breakfastSelection.this, customMeals.class);
        me.putExtra("cameFrom", "breakfast");
        startActivity(me);
    }

    public void clearBreakfastSelection(View v){
        if(me.hasExtra("breakfast")){
            me.removeExtra("breakfast");
            me.setClass(breakfastSelection.this, mealsMenu.class);
            startActivity(me);
        }
        else{
            Toast.makeText(this, "You didn't choose anything yet.", Toast.LENGTH_SHORT).show();
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