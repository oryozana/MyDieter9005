package com.example.mydieter9005;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AnalogClock;
import android.widget.TextClock;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

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
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AnalogClock analogClock;
    private TextClock textClock;

    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    FoodSelectionFragment foodSelectionFragment = new FoodSelectionFragment();

    private VideoView videoView;
    private MediaPlayer mediaPlayer;

    DailyMenu todayMenu;
    Song activeSong;  // In this activity he get a initial value at "createTheFirstIntent".

    FileOutputStream fos;
    OutputStreamWriter osw;
    BufferedWriter bw;
    String currentDate;
    int currentHour;

    SQLiteDatabase sqdb;
    DBHelper my_db;

    FileInputStream is;
    InputStreamReader isr;
    BufferedReader br;
    Intent me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        me = getIntent();
        if(me.hasExtra("cameFromLogin")){
            Toast.makeText(this, "Welcome back " + User.getCurrentUser().getUsername() + "!.", Toast.LENGTH_SHORT).show();
            me.removeExtra("cameFromLogin");
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd_MM_yyyy");
        LocalDateTime today = LocalDateTime.now();
        currentDate = dtf.format(today);

        Calendar calendar = Calendar.getInstance();
        currentHour = calendar.get(Calendar.HOUR_OF_DAY);

        analogClock = (AnalogClock) findViewById(R.id.mainActivityAnalogClock);
        textClock = (TextClock) findViewById(R.id.mainActivityTextClock);

        videoView = (VideoView) findViewById(R.id.mainActivityVideoView);

        bottomNavigationView = findViewById(R.id.bnvMain);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int itemId = item.getItemId();

                analogClock.setVisibility(View.INVISIBLE);
                textClock.setVisibility(View.INVISIBLE);

                if(itemId == R.id.sendToHome) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityFrameLayout, homeFragment).commit();
                    analogClock.setVisibility(View.VISIBLE);
                    textClock.setVisibility(View.VISIBLE);
                    implementSettingsData();
                    return true;
                }

                if(itemId == R.id.sendToFoodSelection) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityFrameLayout, foodSelectionFragment).commit();
                    return true;
                }

                return false;
            }
        });

        me = createTheFirstIntent(me);
        if(me.hasExtra("activeSong"))
            activeSong = (Song) me.getSerializableExtra("activeSong");

        implementSettingsData();
        initiateMediaPlayer();
        initiateVideoPlayer();
    }

    public Intent createTheFirstIntent(Intent me){
        if(!me.hasExtra("exists")){
            me = new Intent(this, mealsMenu.class);
            me.putExtra("exists", true);

            me.putExtra("playMusic", true);
            me.putExtra("useVideos", true);
            me.putExtra("useManuallySave", true);
            me.putExtra("currentDate", currentDate);

            Song.initiateSongs();
            activeSong = Song.getActiveSong();
            me.putExtra("activeSong", activeSong);

            Ingredient.initiateIngredientsList();
            initiateIngredientsPictures();

            DailyMenu.setDailyMenus(MainActivity.this);
            if(DailyMenu.hasTodayMenuInsideAllDailyMenus(currentDate))
                todayMenu = DailyMenu.getTodayMenuFromAllDailyMenus(currentDate);
            else
                todayMenu = new DailyMenu(currentDate);
            DailyMenu.setTodayMenu(todayMenu);

            me.setClass(this, LocalUserSelection.class);
            startActivity(me);
        }
        else {
//            Toast.makeText(this, currentDate +"", Toast.LENGTH_SHORT).show();
//            if(User.getCurrentUser().hasTodayMenu(currentDate)) {
//                todayMenu = User.getCurrentUser().getTodayMenu(currentDate);
//            }
//            else {
//                todayMenu = DailyMenu.getTodayMenu();
//                User.getCurrentUser().addDailyMeals(todayMenu);
//            }

            todayMenu = DailyMenu.getTodayMenu();
            DailyMenu.saveDailyMenuIntoFile(DailyMenu.getTodayMenu(), MainActivity.this);
            getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityFrameLayout, homeFragment).commit();
        }
        return me;
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
            if(fileName.equals("settings")) {  // If setting don't exist it's the first time opening the app.
                firstInitiateSettingsFile();
                firstInitiateCustomMealsNamesFile();
                firstInitiateLocalUsersDatabase();
                firstInitiateDailyMenusFile();
                implementSettingsData();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return allData;
    }

    public void implementSettingsData(){
        if(getFileData("settings") != null){
            String[] settingsParts = getFileData("settings").split("\n");
            Boolean playMusic, useVideos, useManuallySave, useDigitalClock;

            playMusic = Boolean.parseBoolean(settingsParts[0].split(": ")[1]);
            useVideos = Boolean.parseBoolean(settingsParts[1].split(": ")[1]);
            useManuallySave = Boolean.parseBoolean(settingsParts[2].split(": ")[1]);
            activeSong = Song.getSongByName(settingsParts[3].split(": ")[1]);
            useDigitalClock = Boolean.parseBoolean(settingsParts[4].split(": ")[1]);

            if(useDigitalClock)
                analogClock.setVisibility(View.INVISIBLE);
            else
                textClock.setVisibility(View.INVISIBLE);

            me.putExtra("playMusic", playMusic);
            me.putExtra("useVideos", useVideos);
            me.putExtra("useManuallySave", useManuallySave);
            me.putExtra("activeSong", activeSong);
        }
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

    public void initiateVideoPlayer(){
        if(6 <= currentHour && currentHour < 12)
            videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.main_activity_morning_background_video));
        if(12 <= currentHour && currentHour < 18)
            videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.main_activity_noon_background_video));
        if((18 <= currentHour && currentHour <= 23) || (0 <= currentHour && currentHour < 6))
            videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.main_activity_night_background_video));

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
        mediaPlayer = MediaPlayer.create(MainActivity.this, activeSong.getId());
        mediaPlayer.setLooping(true);
        if(me.getBooleanExtra("playMusic", true)){
            mediaPlayer.start();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        videoView.resume();
        if(!me.getBooleanExtra("useVideos", true)) { // videoView background cause of the clock.
            videoView.stopPlayback();
            videoView.setBackground(getDrawable(R.drawable.main_activity_morning_background));
            if(12 <= currentHour && currentHour < 18)
                videoView.setBackground(getDrawable(R.drawable.main_activity_noon_background));
            if((18 <= currentHour && currentHour <= 23) || (0 <= currentHour && currentHour < 6))
                videoView.setBackground(getDrawable(R.drawable.main_activity_night_background));
        }
        else
            videoView.start();
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
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();
        if(itemID == R.id.sendToMusicMaster){
            me.setClass(MainActivity.this, musicMaster.class);
            me.putExtra("cameToMusicMasterFrom", getLocalClassName());
            startActivity(me);
        }

        if(itemID == R.id.sendToSettings){
            me.setClass(MainActivity.this, settingsSetter.class);
            me.putExtra("cameToSettingsFrom", getLocalClassName());
            startActivity(me);
        }

        if(itemID == R.id.sendToUserScreen){
            me.setClass(MainActivity.this, UserInfoScreen.class);
            me.putExtra("cameToUserScreenFrom", getLocalClassName());
            startActivity(me);
        }
        return super.onOptionsItemSelected(item);
    }

    public void firstInitiateSettingsFile(){
        try {
            fos = openFileOutput("settings", Context.MODE_PRIVATE);
            osw = new OutputStreamWriter(fos);
            bw = new BufferedWriter(osw);

            bw.write("Play music ?: " + true + "\n");
            bw.write("Use Videos ?: " + true + "\n");
            bw.write("Use manually Save ?: " + true + "\n");
            bw.write("Active song name: " + activeSong.getName() + "\n");
            bw.write("Use digital clock ?: " + true);

            bw.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void firstInitiateCustomMealsNamesFile(){
        try {
            fos = openFileOutput("customMealsNames", Context.MODE_PRIVATE);
            osw = new OutputStreamWriter(fos);
            bw = new BufferedWriter(osw);

            bw.write("Custom meals names: " + "\n");

            bw.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void firstInitiateDailyMenusFile(){
        try {
            fos = openFileOutput("dailyMenusFile", Context.MODE_PRIVATE);
            osw = new OutputStreamWriter(fos);
            bw = new BufferedWriter(osw);

            bw.write("Daily menus: " + "\n");

            bw.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void firstInitiateLocalUsersDatabase(){
        my_db = new DBHelper(this);
        sqdb = my_db.getWritableDatabase();
        sqdb.close();
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

//        if(viewId == btMealsMenu.getId())
//            sendToSelected(v);

//        if(viewId == btWriteMealsToExternalFile.getId())
//            write();
//
//        if(viewId == btReadMealsFromExternalFile.getId())
//            showFileData();
    }

    @Override
    public void onBackPressed() {

    }
}
