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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class UserInfoScreen extends AppCompatActivity implements View.OnClickListener {

    private MediaPlayer mediaPlayer;
    private VideoView videoView;

    Button btSendToProfilePictureSelection, btChangePassword;
    EditText etGetOldPassword, etGetNewPassword;
    ImageView ivProfilePicture;
    LinearLayout linearLayout;

    Song activeSong = Song.getSongs().get(0);
    User user = User.getCurrentUser();

    FirebaseDatabase usersDb;
    DatabaseReference databaseReference;

    FileInputStream is;
    InputStreamReader isr;
    BufferedReader br;
    Intent me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_screen);

        me = getIntent();
        if(me.hasExtra("activeSong"))
            activeSong = (Song) me.getSerializableExtra("activeSong");

        videoView = (VideoView) findViewById(R.id.userInfoScreenVideoView);
        linearLayout = (LinearLayout) findViewById(R.id.userInfoScreenLinearLayout);

        ivProfilePicture = (ImageView) findViewById(R.id.ivProfilePicture);
        ivProfilePicture.setImageResource(user.getProfilePictureId());

        btSendToProfilePictureSelection = (Button) findViewById(R.id.btSendToProfilePictureSelection);
        btSendToProfilePictureSelection.setOnClickListener(this);
        btChangePassword = (Button) findViewById(R.id.btChangePassword);
        btChangePassword.setOnClickListener(this);

        etGetOldPassword = (EditText) findViewById(R.id.etGetOldPassword);
        etGetNewPassword = (EditText) findViewById(R.id.etGetNewPassword);

        implementSettingsData();
        initiateVideoPlayer();
        initiateMediaPlayer();
    }

    public void sendToProfilePictureSelection(){
        me.setClass(UserInfoScreen.this, ProfilePictureSelection.class);
        startActivity(me);
    }

    public void changePassword(){
        boolean passTests = passChangePasswordTests();

        if(passTests){
            String userPassword = User.getCurrentUser().getPassword();
            if(userPassword.equals(etGetOldPassword.getText().toString())){
                User.getCurrentUser().setPassword(etGetNewPassword.getText().toString());
                saveUserInFirebase(User.getCurrentUser());
                Toast.makeText(this, "Password successfully changed.", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this, "Wrong password.", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean passChangePasswordTests(){
        boolean passTests = true;

        if(etGetOldPassword.getText().toString().replaceAll(" ", "").equals("") && passTests){
            Toast.makeText(this, "Enter your old password!", Toast.LENGTH_SHORT).show();
            passTests = false;
        }

        if(etGetNewPassword.getText().toString().replaceAll(" ", "").equals("") && passTests){
            Toast.makeText(this, "Enter new password!", Toast.LENGTH_SHORT).show();
            passTests = false;
        }
        else{
            if(etGetNewPassword.getText().toString().length() < 4 && passTests){
                Toast.makeText(this, "Password should be at least 4 characters wide!", Toast.LENGTH_SHORT).show();
                passTests = false;
            }
        }

        return passTests;
    }

    public void saveUserInFirebase(User user){
        usersDb = FirebaseDatabase.getInstance();
        databaseReference = usersDb.getReference("users");
        databaseReference.child(user.getUsername()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                User.setCurrentUser(user);
            }
        });
    }

    public void getUserFromFirebaseDatabase(String username, String entered_password){
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        DataSnapshot dataSnapshot = task.getResult();
                        String username = dataSnapshot.getKey();
                        String password = String.valueOf(dataSnapshot.child("password").getValue());

                        if(entered_password.equals(password)){
                            double targetCalories = Double.parseDouble(String.valueOf(dataSnapshot.child("currentPlan").child("targetCalories").getValue()));
                            double targetProteins = Double.parseDouble(String.valueOf(dataSnapshot.child("currentPlan").child("targetProteins").getValue()));
                            double targetFats = Double.parseDouble(String.valueOf(dataSnapshot.child("currentPlan").child("targetFats").getValue()));
                            Plan plan = new Plan(targetCalories, targetProteins, targetFats);

                            String email = String.valueOf(dataSnapshot.child("email").getValue());
                            double startingWeight = Double.parseDouble(String.valueOf(dataSnapshot.child("startingWeight").getValue()));
                            int profilePictureId = Integer.parseInt(String.valueOf(dataSnapshot.child("profilePictureId").getValue()));
                            User.setCurrentUser(new User(username, password, email, startingWeight, plan, profilePictureId));
                        }
                    }
                    else
                        Toast.makeText(UserInfoScreen.this, "Not the correct password.", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.dinner_selection_background_video);
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
        mediaPlayer = MediaPlayer.create(UserInfoScreen.this, activeSong.getId());
        mediaPlayer.setLooping(true);
        if(me.getBooleanExtra("playMusic", true)){
            mediaPlayer.start();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();
        if(itemID == R.id.sendToMusicMaster){
            me.setClass(UserInfoScreen.this, musicMaster.class);
            me.putExtra("cameToMusicMasterFrom", getLocalClassName());
            startActivity(me);
        }

        if(itemID == R.id.sendToSettings){
            me.setClass(UserInfoScreen.this, settingsSetter.class);
            me.putExtra("cameToSettingsFrom", getLocalClassName());
            startActivity(me);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        videoView.resume();
        if(!me.getBooleanExtra("useVideos", true)){
            findViewById(R.id.dinnerSelectionLinearLayout).setBackground(getDrawable(R.drawable.dinner_selection_background));
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
    public void onClick(View v) {
        int viewId = v.getId();

        if(viewId == btSendToProfilePictureSelection.getId())
            sendToProfilePictureSelection();

        if(viewId == btChangePassword.getId())
            changePassword();
    }

    @Override
    public void onBackPressed(){
        me.setClass(UserInfoScreen.this, MainActivity.class);
        startActivity(me);
    }
}