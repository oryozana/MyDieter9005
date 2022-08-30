package com.example.mydieter9005;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.ArrayList;

public class dinnerSelection extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private VideoView videoView;

    Button btSendDinnerToCustomize, btClearDinnerSelection;
    ArrayList<String> mealsList;
    ArrayAdapter<String> adapter;
    ListView listView;
    Intent me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dinner_selection);

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

        listView = (ListView) findViewById(R.id.listViewDinner);
        videoView = (VideoView) findViewById(R.id.dinnerVideoView);

        btSendDinnerToCustomize = (Button) findViewById(R.id.btSendDinnerToCustomize);
        btClearDinnerSelection = (Button) findViewById(R.id.btClearDinnerSelection);

        setListViewFields();
        initiateVideoPlayer();

        mediaPlayer = MediaPlayer.create(dinnerSelection.this, R.raw.my_song);
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

                me.setClass(dinnerSelection.this, mealsMenu.class);
                me.putExtra("dinner", selectedItem);
                startActivity(me);
            }
        });
    }

    public void sendToCustomize(View v){
        me.setClass(dinnerSelection.this, customMeals.class);
        me.putExtra("cameFrom", "dinner");
        startActivity(me);
    }

    public void clearDinnerSelection(View v){
        if(me.hasExtra("dinner")){
            me.removeExtra("dinner");
            me.setClass(dinnerSelection.this, mealsMenu.class);
            startActivity(me);
        }
        else{
            Toast.makeText(this, "You didn't choose anything yet.", Toast.LENGTH_SHORT).show();
        }
    }

    public void initiateVideoPlayer(){
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.night_background_video);
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