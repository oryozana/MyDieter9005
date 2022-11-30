package com.example.mydieter9005;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LocalUserSelection extends AppCompatActivity implements View.OnClickListener {

    private MediaPlayer mediaPlayer;
    private VideoView videoView;

    Button btLoginAnotherUser, btRegisterAnotherUser;
    ImageView ivUser1, ivUser2, ivUser3, ivUser4;
    TextView tvUser1, tvUser2, tvUser3, tvUser4;
    LinearLayout linearLayout;

    User[] presentedLocalUsers;
    ImageView[] ivUsers;
    TextView[] tvUsers;

    Song activeSong = Song.getSongs().get(0);
    ArrayList<User> localUsers;

    int groupsAmount = 0;
    int groupIndex = 1;

    SQLiteDatabase sqdb;
    DBHelper my_db;

    FileInputStream is;
    InputStreamReader isr;
    BufferedReader br;
    Intent me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_user_selection);

        me = getIntent();
        if(me.hasExtra("activeSong"))
            activeSong = (Song) me.getSerializableExtra("activeSong");

        my_db = new DBHelper(LocalUserSelection.this);

        linearLayout = (LinearLayout) findViewById(R.id.localUserSelectionLinearLayout);
        videoView = (VideoView) findViewById(R.id.localUserSelectionVideoView);

        ivUser1 = (ImageView) findViewById(R.id.ivUser1);
        ivUser2 = (ImageView) findViewById(R.id.ivUser2);
        ivUser3 = (ImageView) findViewById(R.id.ivUser3);
        ivUser4 = (ImageView) findViewById(R.id.ivUser4);
        ivUsers = new ImageView[]{ivUser1, ivUser2, ivUser3, ivUser4};

        tvUser1 = (TextView) findViewById(R.id.tvUser1);
        tvUser2 = (TextView) findViewById(R.id.tvUser2);
        tvUser3 = (TextView) findViewById(R.id.tvUser3);
        tvUser4 = (TextView) findViewById(R.id.tvUser4);
        tvUsers = new TextView[]{tvUser1, tvUser2, tvUser3, tvUser4};

        btRegisterAnotherUser = (Button) findViewById(R.id.btRegisterAnotherUser);
        btRegisterAnotherUser.setOnClickListener(this);
        btLoginAnotherUser = (Button) findViewById(R.id.btLoginAnotherUser);
        btLoginAnotherUser.setOnClickListener(this);

        initiateLocalUsersArrayList();
        initiateUsersSelectionViews();
        implementSettingsData();
        initiateVideoPlayer();
        initiateMediaPlayer();
    }

    public void initiateUsersSelectionViews(){
        presentedLocalUsers = new User[ivUsers.length];
        for(int i = 0; i < ivUsers.length && i < localUsers.size() - (groupIndex - 1) * ivUsers.length; i++){
            presentedLocalUsers[i] = localUsers.get(((groupIndex - 1) * ivUsers.length) + i);

            ivUsers[i].setImageResource(localUsers.get(((groupIndex - 1) * ivUsers.length) + i).getProfilePictureId());
            ivUsers[i].setVisibility(View.VISIBLE);

            tvUsers[i].setText(localUsers.get(((groupIndex - 1) * ivUsers.length) + i).getUsername());
            tvUsers[i].setVisibility(View.VISIBLE);

            ivUsers[i].setClickable(true);
            ivUsers[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int viewId = v.getId();

                    if(viewId == ivUser1.getId())
                        userChosen(localUsers.get((groupIndex - 1) * 4));

                    if(viewId == ivUser2.getId())
                        userChosen(localUsers.get(1 + ((groupIndex - 1) * 4)));

                    if(viewId == ivUser3.getId())
                        userChosen(localUsers.get(2 + ((groupIndex - 1) * 4)));

                    if(viewId == ivUser4.getId())
                        userChosen(localUsers.get(3 + ((groupIndex - 1) * 4)));
                }
            });
        }
    }

    private void initiateLocalUsersArrayList() {
        localUsers = new ArrayList<User>();

        if(howManyLocalUsersSavedOnDatabase() != 0){
            sqdb = my_db.getWritableDatabase();

            Cursor c = sqdb.query(DBHelper.TABLE_NAME,null, null, null, null, null, null);

            int col1 = c.getColumnIndex(DBHelper.USERNAME);
            int col2 = c.getColumnIndex(DBHelper.PASSWORD);
            int col3 = c.getColumnIndex(DBHelper.EMAIL);
            int col4 = c.getColumnIndex(DBHelper.STARTING_WEIGHT);
            int col5 = c.getColumnIndex(DBHelper.WEIGHT);
            int col6 = c.getColumnIndex(DBHelper.TARGET_CALORIES);
            int col7 = c.getColumnIndex(DBHelper.TARGET_PROTEIN);
            int col8 = c.getColumnIndex(DBHelper.TARGET_FATS);
            int col9 = c.getColumnIndex(DBHelper.PROFILE_PICTURE_ID);

            c.moveToFirst();

            while(!c.isAfterLast()) {
                String t1 = c.getString(col1);
                String t2 = c.getString(col2);
                String t3 = c.getString(col3);
                String t4 = c.getString(col4);
                String t5 = c.getString(col5);
                String t6 = c.getString(col6);
                String t7 = c.getString(col7);
                String t8 = c.getString(col8);
                String t9 = c.getString(col9);

                localUsers.add(new User(t1, t2, t3, t4, t6, t7, t8, t9));
                c.moveToNext();
            }

            c.close();
            sqdb.close();
        }
        else
            goToLogin();

        groupsAmount = (int) Math.round(Math.floor(localUsers.size() / ivUsers.length));
        if(localUsers.size() % ivUsers.length != 0)
            groupsAmount++;
    }

    private int howManyLocalUsersSavedOnDatabase() {
        sqdb = my_db.getWritableDatabase();
        int counter = 0;

        Cursor c = sqdb.query(DBHelper.TABLE_NAME,null, null, null, null, null, null);
        c.moveToFirst();

        while(!c.isAfterLast()) {
            counter++;
            c.moveToNext();
        }

        c.close();
        sqdb.close();

        return counter;
    }

    public void userChosen(User user){
        User.setCurrentUser(user);
        me.setClass(LocalUserSelection.this, MainActivity.class);
        me.putExtra("cameFromLogin", true);
        startActivity(me);
    }

    public void goToLogin(){
        me.setClass(LocalUserSelection.this, Login.class);
        startActivity(me);
    }

    public void goToRegister(){
        me.setClass(LocalUserSelection.this, Register.class);
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
        mediaPlayer = MediaPlayer.create(LocalUserSelection.this, activeSong.getId());
        mediaPlayer.setLooping(true);
        if(me.getBooleanExtra("playMusic", true)){
            mediaPlayer.start();
        }
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
            me.setClass(LocalUserSelection.this, musicMaster.class);
            me.putExtra("cameToMusicMasterFrom", getLocalClassName());
            startActivity(me);
        }

        if(itemID == R.id.sendToSettings){
            me.setClass(LocalUserSelection.this, settingsSetter.class);
            me.putExtra("cameToSettingsFrom", getLocalClassName());
            startActivity(me);
        }

        if(itemID == R.id.sendToUserScreen){
            me.setClass(LocalUserSelection.this, UserInfoScreen.class);
            me.putExtra("cameToUserScreenFrom", getLocalClassName());
            startActivity(me);
        }
        return super.onOptionsItemSelected(item);
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

        if(viewId == btLoginAnotherUser.getId())
            goToLogin();

        if(viewId == btRegisterAnotherUser.getId())
            goToRegister();
    }

    @Override
    public void onBackPressed(){

    }
}