package com.example.mydieter9005;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.CheckBox;
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

public class Login extends AppCompatActivity implements View.OnClickListener {

    private NetworkConnectionReceiver networkConnectionReceiver;

    private MediaPlayer mediaPlayer;
    private VideoView videoView;

    EditText etGetUsernameLoginInfo, etGetPasswordLoginInfo;
    CheckBox cbSaveLoggedUserInLocalDatabase;
    Button btLogin, btGoToRegister;
    LinearLayout linearLayout;

    Song activeSong = Song.getSongs().get(0);

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
        setContentView(R.layout.activity_login);

        me = getIntent();
        if(me.hasExtra("activeSong"))
            activeSong = (Song) me.getSerializableExtra("activeSong");

        my_db = new DBHelper(Login.this);

        linearLayout = (LinearLayout) findViewById(R.id.loginLinearLayout);
        videoView = (VideoView) findViewById(R.id.loginVideoView);

        etGetUsernameLoginInfo = (EditText) findViewById(R.id.etGetUsernameLoginInfo);
        etGetPasswordLoginInfo = (EditText) findViewById(R.id.etGetPasswordLoginInfo);

        btGoToRegister = (Button) findViewById(R.id.btGoToRegister);
        btGoToRegister.setOnClickListener(this);
        btLogin = (Button) findViewById(R.id.btLogin);
        btLogin.setOnClickListener(this);

        cbSaveLoggedUserInLocalDatabase = (CheckBox) findViewById(R.id.cbSaveLoggedUserInLocalDatabase);

        setCustomNetworkConnectionReceiver();
        implementSettingsData();
        initiateVideoPlayer();
        initiateMediaPlayer();
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

                            if(cbSaveLoggedUserInLocalDatabase.isChecked())
                                addLoggedUserIntoLocalDatabase(User.getCurrentUser());

                            me.setClass(Login.this, MainActivity.class);
                            me.putExtra("cameFromLogin", 0);
                            startActivity(me);
                        }
                        else
                            Toast.makeText(Login.this, "Username or password incorrect.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(Login.this, "Username or password incorrect.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void addLoggedUserIntoLocalDatabase(User user){
        boolean added = false;

        if(isDatabaseEmpty()) {
            addUserToDatabase(user);
            added = true;
        }

        if(!isUserAlreadyExists(user.getUsername()))
            if(!added)
                addUserToDatabase(user);
    }


    private void addUserToDatabase(User user) {
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

    private boolean isUserAlreadyExists(String username) {
        boolean flag = false;
        sqdb = my_db.getWritableDatabase();

        Cursor c = sqdb.query(DBHelper.TABLE_NAME,null, null, null, null, null, null);

        int col1 = c.getColumnIndex(DBHelper.USERNAME);

        c.moveToFirst();

        while(!c.isAfterLast()) {
            String t1 = c.getString(col1);

            if(username.equals(t1))
                flag = true;

            c.moveToNext();
        }

        c.close();
        sqdb.close();
        return flag;
    }

    private boolean isDatabaseEmpty() {
        sqdb = my_db.getWritableDatabase();
        boolean flag = true;

        Cursor c = sqdb.query(DBHelper.TABLE_NAME,null, null, null, null, null, null);
        c.moveToFirst();

        if(!c.isAfterLast())
            flag = false;

        c.close();
        sqdb.close();

        return flag;
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
                        me.setClass(Login.this, LocalUserSelection.class);
                        startActivity(me);
                    }
                });

                ad = adb.create();
                ad.show();
            }
        };
    }

    public void goToRegister(){
        me.setClass(this, Register.class);
        startActivity(me);
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
        mediaPlayer = MediaPlayer.create(Login.this, activeSong.getId());
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

        if(viewId == btLogin.getId())
            getUserFromFirebaseDatabase(etGetUsernameLoginInfo.getText().toString(), etGetPasswordLoginInfo.getText().toString());

        if(viewId == btGoToRegister.getId())
            goToRegister();
    }
}
