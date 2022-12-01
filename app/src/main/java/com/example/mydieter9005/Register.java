package com.example.mydieter9005;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Register extends AppCompatActivity implements View.OnClickListener {

    private NetworkConnectionReceiver networkConnectionReceiver;

    private MediaPlayer mediaPlayer;
    private VideoView videoView;

    EditText etGetUsername, etGetPassword, etGetEmail, etGetStartingWeight;
    EditText etGetTargetCalories, etGetTargetProteins, etGetTargetFats;
    Button btRegister, btGoToLogin;
    LinearLayout linearLayout;

    Song activeSong = Song.getSongs().get(0);
    ArrayList<String> usernamesList = new ArrayList<String>();
    int userPicturesAmount = 10;

    FirebaseDatabase usersDb;
    DatabaseReference databaseReference;

    SQLiteDatabase sqdb;
    DBHelper my_db;

    FileInputStream is;
    InputStreamReader isr;
    BufferedReader br;
    Intent me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        me = getIntent();
        if(me.hasExtra("activeSong"))
            activeSong = (Song) me.getSerializableExtra("activeSong");

        my_db = new DBHelper(Register.this);

        linearLayout = (LinearLayout) findViewById(R.id.registerLinearLayout);
        videoView = (VideoView) findViewById(R.id.registerVideoView);

        etGetUsername = (EditText) findViewById(R.id.etGetUsername);
        etGetPassword = (EditText) findViewById(R.id.etGetPassword);
        etGetEmail = (EditText) findViewById(R.id.etGetEmail);
        etGetStartingWeight = (EditText) findViewById(R.id.etGetStartingWeight);

        etGetTargetCalories = (EditText) findViewById(R.id.etGetTargetCalories);
        etGetTargetProteins = (EditText) findViewById(R.id.etGetTargetProteins);
        etGetTargetFats = (EditText) findViewById(R.id.etGetTargetFats);

        btGoToLogin = (Button) findViewById(R.id.btGoToLogin);
        btGoToLogin.setOnClickListener(this);
        btRegister = (Button) findViewById(R.id.btRegister);
        btRegister.setOnClickListener(this);

        setCustomNetworkConnectionReceiver();
        getAllExistingUsernames();
        implementSettingsData();
        initiateVideoPlayer();
        initiateMediaPlayer();
    }

    public void createUserAndUserPlan(){
        if(passUserInfoTests()){
            double targetCalories = Double.parseDouble(etGetTargetCalories.getText().toString());
            double targetProteins = Double.parseDouble(etGetTargetProteins.getText().toString());
            double targetFats = Double.parseDouble(etGetTargetFats.getText().toString());
            Plan userPlan = new Plan(targetCalories, targetProteins, targetFats);

            String username = etGetUsername.getText().toString();
            String password = etGetPassword.getText().toString();
            String email = etGetEmail.getText().toString();
            double startingWeight = Double.parseDouble(etGetStartingWeight.getText().toString());

            int profilePictureId = getResources().getIdentifier("user_picture_" + (((int)(Math.random() * userPicturesAmount)) + 1), "drawable", getPackageName());
            User user = new User(username, password, email, startingWeight, userPlan, profilePictureId);

            saveUserInFirebaseAndInDatabase(user);
        }
    }

    public void saveUserInFirebaseAndInDatabase(User user){
        usersDb = FirebaseDatabase.getInstance();
        databaseReference = usersDb.getReference("users");
        databaseReference.child(user.getUsername()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(Register.this, "User successfully created.", Toast.LENGTH_SHORT).show();
                me.setClass(Register.this, MainActivity.class);
                addRegisteredUserToDatabase(user);
                User.setCurrentUser(user);
                startActivity(me);
            }
        });
    }

    public boolean passUserInfoTests(){
        boolean passTests = true;

        if(etGetUsername.getText().toString().replaceAll(" ", "").equals("") && passTests){
            Toast.makeText(this, "One or more of the fields is empty.", Toast.LENGTH_SHORT).show();
            passTests = false;
        }
        else{
            if(isUserAlreadyExistsInFirebase(etGetUsername.getText().toString()) && passTests){
                Toast.makeText(this, "Username already exists.", Toast.LENGTH_SHORT).show();
                passTests = false;
            }
        }

        if(etGetPassword.getText().toString().replaceAll(" ", "").equals("") && passTests) {
            Toast.makeText(this, "One or more of the fields is empty.", Toast.LENGTH_SHORT).show();
            passTests = false;
        }
        else{
            if(etGetPassword.getText().toString().length() < 4 && passTests){
                Toast.makeText(this, "Password should be at least 4 characters wide.", Toast.LENGTH_SHORT).show();
                passTests = false;
            }
        }

        if(etGetEmail.getText().toString().replaceAll(" ", "").equals("") && passTests) {
            Toast.makeText(this, "One or more of the fields is empty.", Toast.LENGTH_SHORT).show();
            passTests = false;
        }
        else{
            if(!etGetEmail.getText().toString().contains("@") && passTests){
                Toast.makeText(this, "Email address should have @ in them.", Toast.LENGTH_SHORT).show();
                passTests = false;
            }
        }

        if(etGetStartingWeight.getText().toString().replaceAll(" ", "").equals("") && passTests) {
            Toast.makeText(this, "One or more of the fields is empty.", Toast.LENGTH_SHORT).show();
            passTests = false;
        }
        else{
            if(!(0 < Double.parseDouble(etGetStartingWeight.getText().toString()) && Double.parseDouble(etGetStartingWeight.getText().toString()) < 500) && passTests){
                Toast.makeText(this, "Starting weight should be between 0 to 500 kg.", Toast.LENGTH_SHORT).show();
                passTests = false;
            }
        }

        if(etGetTargetCalories.getText().toString().replaceAll(" ", "").equals("") && passTests){
            Toast.makeText(this, "One or more of the fields is empty.", Toast.LENGTH_SHORT).show();
            passTests = false;
        }
        else{
            if(!(0 < Double.parseDouble(etGetTargetCalories.getText().toString()) && Double.parseDouble(etGetTargetCalories.getText().toString()) < 5000) && passTests){
                Toast.makeText(this, "Target calories should be between 0 to 5000.", Toast.LENGTH_SHORT).show();
                passTests = false;
            }
        }

        if(etGetTargetProteins.getText().toString().replaceAll(" ", "").equals("") && passTests){
            Toast.makeText(this, "One or more of the fields is empty.", Toast.LENGTH_SHORT).show();
            passTests = false;
        }
        else{
            if(!(0 < Double.parseDouble(etGetTargetProteins.getText().toString()) && Double.parseDouble(etGetTargetProteins.getText().toString()) < 1000) && passTests){
                Toast.makeText(this, "Target proteins should be between 0 to 1000.", Toast.LENGTH_SHORT).show();
                passTests = false;
            }
        }

        if(etGetTargetFats.getText().toString().replaceAll(" ", "").equals("") && passTests){
            Toast.makeText(this, "One or more of the fields is empty.", Toast.LENGTH_SHORT).show();
            passTests = false;
        }
        else{
            if(!(0 < Double.parseDouble(etGetTargetFats.getText().toString()) && Double.parseDouble(etGetTargetFats.getText().toString()) < 1000) && passTests){
                Toast.makeText(this, "Target fats should be between 0 to 1000.", Toast.LENGTH_SHORT).show();
                passTests = false;
            }
        }

        return passTests;
    }

    public boolean isUserAlreadyExistsInFirebase(String username){
        for(int i = 0; i < usernamesList.size(); i++){
            if(usernamesList.get(i).equals(username))
                return true;
        }
        return false;
    }

    public void getAllExistingUsernames(){
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        DataSnapshot dataSnapshot = task.getResult();
                        for(DataSnapshot user : dataSnapshot.getChildren())
                            usernamesList.add(user.getKey());
                    }
                }
                else{
                    Toast.makeText(Register.this, "Failed to load users.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void goToLogin(){
        me.setClass(this, Login.class);
        startActivity(me);
    }

    public void setCustomNetworkConnectionReceiver(){
        networkConnectionReceiver = new NetworkConnectionReceiver() {
            @Override
            public void noInternetAccess(Context context){
                AlertDialog ad;
                AlertDialog.Builder adb;
                adb = new AlertDialog.Builder(context);
                adb.setTitle("Internet connection not found!");
                adb.setMessage("Connect to the internet and try again.");
                adb.setIcon(R.drawable.ic_network_not_found);
                adb.setCancelable(false);

                adb.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(!isOnline(context))
                            noInternetAccess(context);
                        else
                            Toast.makeText(context, "Network connection available.", Toast.LENGTH_SHORT).show();
                    }
                });

                adb.setNegativeButton("Offline user select", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        me.setClass(Register.this, LocalUserSelection.class);
                        startActivity(me);
                    }
                });

                ad = adb.create();
                ad.show();
            }
        };
    }

    private void addRegisteredUserToDatabase(User user) {
        ContentValues cv = new ContentValues();

        cv.put(my_db.USERNAME, user.getUsername());
        cv.put(my_db.PASSWORD, user.getPassword());
        cv.put(my_db.EMAIL, user.getEmail());
        cv.put(my_db.STARTING_WEIGHT, user.getStartingWeight());
        cv.put(my_db.WEIGHT, user.getWeight());
        cv.put(my_db.TARGET_CALORIES, user.getCurrentPlan().getTargetCalories());
        cv.put(my_db.TARGET_PROTEIN, user.getCurrentPlan().getTargetProteins());
        cv.put(my_db.TARGET_FATS, user.getCurrentPlan().getTargetFats());
        cv.put(my_db.PROFILE_PICTURE_ID, user.getProfilePictureId());

        sqdb = my_db.getWritableDatabase();
        sqdb.insert(my_db.TABLE_NAME, null, cv);
        sqdb.close();
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
            activeSong = Song.getSongByName(settingsParts[3].split(": ")[1]);

            me.putExtra("playMusic", playMusic);
            me.putExtra("useVideos", useVideos);
            me.putExtra("useManuallySave", useManuallySave);
            me.putExtra("activeSong", activeSong);
        }
    }

    public void initiateVideoPlayer(){
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.world_saved_custom_meals_background_video);
        videoView.setVideoURI(uri);

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
        mediaPlayer = MediaPlayer.create(Register.this, activeSong.getId());
        mediaPlayer.setLooping(true);
        if(me.getBooleanExtra("playMusic", true)){
            mediaPlayer.start();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        videoView.resume();
        if(!me.getBooleanExtra("useVideos", true)){
            linearLayout.setBackground(getDrawable(R.drawable.world_saved_custom_meals_background));
            videoView.stopPlayback();
        }
        else
            videoView.start();
    }

    @Override
    protected void onRestart() {
        videoView.start();
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter networkConnectionFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            registerReceiver(networkConnectionReceiver, networkConnectionFilter);
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            registerReceiver(networkConnectionReceiver, networkConnectionFilter);
        }

        mediaPlayer.start();
        if(!me.getBooleanExtra("playMusic", true)){
            mediaPlayer.stop();
        }
    }

    @Override
    protected void onPause() {

        try{
            unregisterReceiver(networkConnectionReceiver);
        }
        catch (IllegalArgumentException e){
            e.getStackTrace();
        }

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
    public void onClick(View v) {
        int viewId = v.getId();

        if(viewId == btRegister.getId())
            createUserAndUserPlan();

        if(viewId == btGoToLogin.getId())
            goToLogin();
    }
}