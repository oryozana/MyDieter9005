package com.example.mydieter9005;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
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

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    TextView tvBreakfastMain, tvLunchMain, tvDinnerMain;
    TextView tvTotalCaloriesMain, tvCaloriesLeftMain;
    Button btMealsMenu;
    int totalCalories, caloriesLeft;
    String[] meals;
    Intent me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        me = getIntent();
        me = createTheFirstIntent(me);
        initiateMediaPlayer();

        tvBreakfastMain = (TextView) findViewById(R.id.tvBreakfastMain);
        tvBreakfastMain.setMovementMethod(new ScrollingMovementMethod());
        tvLunchMain = (TextView) findViewById(R.id.tvLunchMain);
        tvLunchMain.setMovementMethod(new ScrollingMovementMethod());
        tvDinnerMain = (TextView) findViewById(R.id.tvDinnerMain);
        tvDinnerMain.setMovementMethod(new ScrollingMovementMethod());

        tvTotalCaloriesMain = (TextView) findViewById(R.id.tvTotalCaloriesMain);
        tvCaloriesLeftMain = (TextView) findViewById(R.id.tvCaloriesLeftMain);

        btMealsMenu = (Button) findViewById(R.id.btMealsMenu);

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
        if(!me.hasExtra("meals")){
            me = new Intent(this, mealsMenu.class);
        }
        me.putExtra("isPlaying", true);
        return me;
    }

    public void sendToSelected(View v) {
        int id = v.getId();
        if (id == btMealsMenu.getId()) {
            me.setClass(this, mealsMenu.class);
            startActivity(me);
        }
    }

    public void initiateMediaPlayer(){
        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.my_song);
        mediaPlayer.setLooping(true);
        if(me.getBooleanExtra("isPlaying", true)){
            mediaPlayer.start();
        }
        else{
            mediaPlayer.pause();
        }
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
            if(me.getBooleanExtra("isPlaying", true)){
                mediaPlayer.pause();
                me.putExtra("isPlaying", false);
                item.setIcon(R.drawable.ic_music_off_icon);
            }
            else{
                mediaPlayer.start();
                me.putExtra("isPlaying", true);
                item.setIcon(R.drawable.ic_music_on_icon);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
