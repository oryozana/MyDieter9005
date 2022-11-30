package com.example.mydieter9005;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Annotation;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AnalogClock;
import android.widget.Button;
import android.widget.DigitalClock;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.mydieter9005.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private VideoView videoView;
    private MediaPlayer mediaPlayer;
    private AnalogClock analogClock;
    private TextClock textClock;

    TextView tvBreakfastMain, tvLunchMain, tvDinnerMain;
    TextView tvTotalProteinsMain, tvTotalFatsMain, tvTotalCaloriesMain;
    Button btMealsMenu, btWriteMealsToExternalFile, btReadMealsFromExternalFile;
    double totalProteins, totalFats, totalCalories;
    LinearLayout mainActivityLinearLayout;
    Meal[] selectedMeals = new Meal[3];
    Song activeSong;  // In this activity he get a initial value at "createTheFirstIntent".

    FileOutputStream fos;
    OutputStreamWriter osw;
    BufferedWriter bw;
    String todayDate;
    int currentHour;

    SQLiteDatabase sqdb; //הכרזה על עצם ממחלקה זו שהיא מחלקה פנימית של גאווה המכילה את כל הדרוש לגישה ועבודה עם בסיסי נתונים
    DBHelper my_db; //הכרזה על עצם ממחלקה זו שהיא מחלקת עזר לעבודה עם בסיס נתונים אותה בנינו

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
        todayDate = dtf.format(today) + " saved file.";

        Calendar calendar = Calendar.getInstance();
        currentHour = calendar.get(Calendar.HOUR_OF_DAY);

        videoView = (VideoView) findViewById(R.id.mainActivityVideoView);
        analogClock = (AnalogClock) findViewById(R.id.mainActivityAnalogClock);
        textClock = (TextClock) findViewById(R.id.mainActivityTextClock);

        tvBreakfastMain = (TextView) findViewById(R.id.tvBreakfastMain);
        tvBreakfastMain.setMovementMethod(new ScrollingMovementMethod());
        tvLunchMain = (TextView) findViewById(R.id.tvLunchMain);
        tvLunchMain.setMovementMethod(new ScrollingMovementMethod());
        tvDinnerMain = (TextView) findViewById(R.id.tvDinnerMain);
        tvDinnerMain.setMovementMethod(new ScrollingMovementMethod());

        tvTotalCaloriesMain = (TextView) findViewById(R.id.tvTotalCaloriesMain);
        tvTotalProteinsMain = (TextView) findViewById(R.id.tvTotalProteinsMain);
        tvTotalFatsMain = (TextView) findViewById(R.id.tvTotalFatsMain);

        btReadMealsFromExternalFile = (Button) findViewById(R.id.btReadMealsFromExternalFile);
        btReadMealsFromExternalFile.setOnClickListener(this);
        btWriteMealsToExternalFile = (Button) findViewById(R.id.btWriteMealsToExternalFile);
        btWriteMealsToExternalFile.setOnClickListener(this);
        btMealsMenu = (Button) findViewById(R.id.btMealsMenu);
        btMealsMenu.setOnClickListener(this);

        mainActivityLinearLayout = (LinearLayout) findViewById(R.id.mainActivityLinearLayout);

        me = createTheFirstIntent(me);
        if(me.hasExtra("activeSong"))
            activeSong = (Song) me.getSerializableExtra("activeSong");

        implementSettingsData();
        initiateMediaPlayer();
        initiateVideoPlayer();
        updateMealsIfNeeded();
    }

    public void updateMealsIfNeeded(){
        if(me.hasExtra("selectedBreakfast") || me.hasExtra("selectedLunch") || me.hasExtra("selectedDinner")) {
            selectedMeals[0] = (Meal) me.getSerializableExtra("selectedBreakfast");
            selectedMeals[1] = (Meal) me.getSerializableExtra("selectedLunch");
            selectedMeals[2] = (Meal) me.getSerializableExtra("selectedDinner");

            if(selectedMeals[0] != null)
                tvBreakfastMain.setText("Your breakfast: " + selectedMeals[0].getName());
            if(selectedMeals[1] != null)
                tvLunchMain.setText("Your lunch: " + selectedMeals[1].getName());
            if(selectedMeals[2] != null)
                tvDinnerMain.setText("Your dinner: " + selectedMeals[2].getName());

            totalProteins = me.getDoubleExtra("totalProteins", 0);
            tvTotalProteinsMain.setText("Total Proteins: " + Math.round(totalProteins * 1000.0) / 1000.0 + " .");

            totalFats = me.getDoubleExtra("totalFats", 0);
            tvTotalFatsMain.setText("Total Fats: " + Math.round(totalFats * 1000.0) / 1000.0 + " .");

            totalCalories = me.getDoubleExtra("totalCalories", 0);
            tvTotalCaloriesMain.setText("Total calories: " + Math.round(totalCalories * 1000.0) / 1000.0 + " .");
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

            Song.initiateSongs();
            activeSong = Song.getActiveSong();
            me.putExtra("activeSong", activeSong);

            Ingredient.initiateIngredientsList();
            initiateIngredientsPictures();

            me.setClass(this, LocalUserSelection.class);
            startActivity(me);
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

    public void write(){
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
                //bw.write(tvCaloriesLeftMain.getText().toString());

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

    public void showFileData(){
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
            if(fileName.equals("settings")) {  // If setting don't exist it's the first time opening the app.
                firstInitiateSettingsFile();
                firstInitiateCustomMealsNamesFile();
                firstInitiateLocalUsersDatabase();
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

    public void firstInitiateLocalUsersDatabase(){
        my_db = new DBHelper(this); //הקמת העצם. העצם מכיר את הטבלה  את השדות ואת הקובץ בו נמצא בסיס הנתונים. הוא גם מוכן לביצוע פעולות.
        sqdb = my_db.getWritableDatabase();//הכרזה כי בסיס הנתונים מאפשר כתיבת נתונים בטבלה שלו
        sqdb.close();//סגירת ערוץ הגישה לבסיס הנתונים
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        if(viewId == btMealsMenu.getId())
            sendToSelected(v);

        if(viewId == btWriteMealsToExternalFile.getId())
            write();

        if(viewId == btReadMealsFromExternalFile.getId())
            showFileData();
    }
}
