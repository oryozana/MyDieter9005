package com.example.mydieter9005;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProfilePictureSelection extends AppCompatActivity implements View.OnClickListener {

    private NetworkConnectionReceiver networkConnectionReceiver;

    private MediaPlayer mediaPlayer;
    private VideoView videoView;

    Button btChoseProfilePicture, btCancelProfilePictureSelection;
    ImageButton ibtPreviousPicture, ibtNextPicture;
    ImageView ivProfilePictureSelector;
    TextView tvPictureNumberOutOf;
    LinearLayout linearLayout;

    Song activeSong = Song.getSongs().get(0);
    int currentIndex = 0, maxAmount = 10;

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
        setContentView(R.layout.activity_profile_picture_selection);

        me = getIntent();
        if(me.hasExtra("activeSong"))
            activeSong = (Song) me.getSerializableExtra("activeSong");

        my_db = new DBHelper(ProfilePictureSelection.this);

        linearLayout = (LinearLayout) findViewById(R.id.profilePictureSelectionLinearLayout);
        videoView = (VideoView) findViewById(R.id.profilePictureSelectionVideoView);

        ivProfilePictureSelector = (ImageView) findViewById(R.id.ivProfilePictureSelector);
        tvPictureNumberOutOf = (TextView) findViewById(R.id.tvPictureNumberOutOf);

        btCancelProfilePictureSelection = (Button) findViewById(R.id.btCancelProfilePictureSelection);
        btCancelProfilePictureSelection.setOnClickListener(this);
        btChoseProfilePicture = (Button) findViewById(R.id.btChoseProfilePicture);
        btChoseProfilePicture.setOnClickListener(this);

        ibtPreviousPicture = (ImageButton) findViewById(R.id.ibtPreviousPicture);
        ibtPreviousPicture.setOnClickListener(this);
        ibtNextPicture = (ImageButton) findViewById(R.id.ibtNextPicture);
        ibtNextPicture.setOnClickListener(this);

        setCustomNetworkConnectionReceiver();
        nextPicture();
        implementSettingsData();
        initiateVideoPlayer();
        initiateMediaPlayer();
    }

    public void setImageBasedOnIndex(){
        ivProfilePictureSelector.setImageResource(getResources().getIdentifier("user_picture_" + (currentIndex), "drawable", getPackageName()));
    }

    public void previousPicture(){
        if(0 < currentIndex && currentIndex <= maxAmount){
            currentIndex--;
            tvPictureNumberOutOf.setText("  Picture number " + currentIndex + " out of " + maxAmount + "  ");
            setImageBasedOnIndex();

            ibtNextPicture.setVisibility(View.VISIBLE);
            if(currentIndex == 1)
                ibtPreviousPicture.setVisibility(View.INVISIBLE);
        }
    }

    public void nextPicture(){
        if(0 <= currentIndex && currentIndex < maxAmount){
            currentIndex++;
            tvPictureNumberOutOf.setText("   Picture number " + currentIndex + " out of " + maxAmount + "   ");
            setImageBasedOnIndex();

            if(1 < currentIndex)
                ibtPreviousPicture.setVisibility(View.VISIBLE);
            if(currentIndex == maxAmount)
                ibtNextPicture.setVisibility(View.INVISIBLE);
        }
    }

    public void profilePictureSelected(){
        User.getCurrentUser().setProfilePictureId(getResources().getIdentifier("user_picture_" + (currentIndex), "drawable", getPackageName()));
        updateUserProfilePictureIdInFirebaseAndDatabase(User.getCurrentUser());
    }

    public void updateUserProfilePictureIdInFirebaseAndDatabase(User user){
        usersDb = FirebaseDatabase.getInstance();
        databaseReference = usersDb.getReference("users");
        databaseReference.child(user.getUsername()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                sqdb=my_db.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(DBHelper.PROFILE_PICTURE_ID, user.getProfilePictureId());

                sqdb.update(DBHelper.TABLE_NAME, cv,DBHelper.USERNAME+"=?", new String[]{User.getCurrentUser().getUsername()});
                sqdb.close();

                Toast.makeText(ProfilePictureSelection.this, "User profile picture successfully changed.", Toast.LENGTH_SHORT).show();

                me.setClass(ProfilePictureSelection.this, UserInfoScreen.class);
                startActivity(me);
            }
        });
    }

    public void setCustomNetworkConnectionReceiver(){
        networkConnectionReceiver = new NetworkConnectionReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try{
                    if(!isOnline(context))
                        noInternetAccess(context);
                    else
                        btChoseProfilePicture.setVisibility(View.VISIBLE);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

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

                adb.setNeutralButton("Just look", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        btChoseProfilePicture.setVisibility(View.INVISIBLE);
                    }
                });

                adb.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                ad = adb.create();
                ad.show();
            }
        };
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
        mediaPlayer = MediaPlayer.create(ProfilePictureSelection.this, activeSong.getId());
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
            me.setClass(ProfilePictureSelection.this, musicMaster.class);
            me.putExtra("cameToMusicMasterFrom", getLocalClassName());
            startActivity(me);
        }

        if(itemID == R.id.sendToSettings){
            me.setClass(ProfilePictureSelection.this, settingsSetter.class);
            me.putExtra("cameToSettingsFrom", getLocalClassName());
            startActivity(me);
        }

        if(itemID == R.id.sendToUserScreen){
            me.setClass(ProfilePictureSelection.this, UserInfoScreen.class);
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

        if(viewId == ibtNextPicture.getId())
            nextPicture();

        if(viewId == ibtPreviousPicture.getId())
            previousPicture();

        if(viewId == btChoseProfilePicture.getId())
            profilePictureSelected();

        if(viewId == btCancelProfilePictureSelection.getId())
            finish();
    }
}