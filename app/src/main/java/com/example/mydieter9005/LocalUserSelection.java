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
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class LocalUserSelection extends AppCompatActivity implements View.OnClickListener {

    private NetworkConnectionReceiver networkConnectionReceiver;

    private MediaPlayer mediaPlayer;
    private VideoView videoView;

    TextView tvGroupNumberOutOf, tvCodeState, tvTimeBeforeExpiration, tvLocalUserSelectionMainTextView;
    Button btUseOrGenerateCode, btUseCode, btGenerateCode, btCheckCode, btBack;
    Button btLoginAnotherUser, btRegisterAnotherUser;
    ImageButton ibtPreviousGroup, ibtNextGroup;
    ImageView ivUser1, ivUser2, ivUser3, ivUser4;
    TextView tvUser1, tvUser2, tvUser3, tvUser4;
    EditText etEnterCode;

    LinearLayout linearLayout, localUserSelectionLoadingLinearLayout, localUsersLinearLayout, useOrGenerateCodesLinearLayout;
    androidx.gridlayout.widget.GridLayout groupSelectorGridLayout;

    User[] presentedLocalUsers;
    ImageView[] ivUsers;
    TextView[] tvUsers;

    Song activeSong = Song.getSongs().get(0);
    ArrayList<User> localUsers;

    boolean internetConnection = true;
    boolean showUserMode = true;
    int groupsAmount = 0;
    int groupIndex = 1;

    FirebaseDatabase codesDb;
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
        setContentView(R.layout.activity_local_user_selection);

        me = getIntent();
        if(me.hasExtra("activeSong"))
            activeSong = (Song) me.getSerializableExtra("activeSong");

        my_db = new DBHelper(LocalUserSelection.this);

        localUserSelectionLoadingLinearLayout = (LinearLayout) findViewById(R.id.localUserSelectionLoadingLinearLayout);
        groupSelectorGridLayout = (androidx.gridlayout.widget.GridLayout) findViewById(R.id.groupSelectorGridLayout);
        useOrGenerateCodesLinearLayout = (LinearLayout) findViewById(R.id.useOrGenerateCodesLinearLayout);
        localUsersLinearLayout = (LinearLayout) findViewById(R.id.localUsersLinearLayout);
        linearLayout = (LinearLayout) findViewById(R.id.localUserSelectionLinearLayout);
        videoView = (VideoView) findViewById(R.id.localUserSelectionVideoView);

        ivUser1 = (ImageView) findViewById(R.id.ivUser1);
        ivUser2 = (ImageView) findViewById(R.id.ivUser2);
        ivUser3 = (ImageView) findViewById(R.id.ivUser3);
        ivUser4 = (ImageView) findViewById(R.id.ivUser4);
        ivUsers = new ImageView[]{ivUser1, ivUser2, ivUser3, ivUser4};

        tvGroupNumberOutOf = (TextView) findViewById(R.id.tvGroupNumberOutOf);
        tvUser1 = (TextView) findViewById(R.id.tvUser1);
        tvUser2 = (TextView) findViewById(R.id.tvUser2);
        tvUser3 = (TextView) findViewById(R.id.tvUser3);
        tvUser4 = (TextView) findViewById(R.id.tvUser4);
        tvUsers = new TextView[]{tvUser1, tvUser2, tvUser3, tvUser4};

        ibtPreviousGroup = (ImageButton) findViewById(R.id.ibtPreviousGroup);
        ibtPreviousGroup.setOnClickListener(this);
        ibtNextGroup = (ImageButton) findViewById(R.id.ibtNextGroup);
        ibtNextGroup.setOnClickListener(this);

        btRegisterAnotherUser = (Button) findViewById(R.id.btRegisterAnotherUser);
        btRegisterAnotherUser.setOnClickListener(this);
        btLoginAnotherUser = (Button) findViewById(R.id.btLoginAnotherUser);
        btLoginAnotherUser.setOnClickListener(this);
        btUseOrGenerateCode = (Button) findViewById(R.id.btUseOrGenerateCode);
        btUseOrGenerateCode.setOnClickListener(this);

        tvLocalUserSelectionMainTextView = (TextView) findViewById(R.id.tvLocalUserSelectionMainTextView);
        tvTimeBeforeExpiration = (TextView) findViewById(R.id.tvTimeBeforeExpiration);
        tvCodeState = (TextView) findViewById(R.id.tvCodeState);

        etEnterCode = (EditText) findViewById(R.id.etEnterCode);

        btGenerateCode = (Button) findViewById(R.id.btGenerateCode);
        btGenerateCode.setOnClickListener(this);
        btUseCode = (Button) findViewById(R.id.btUseCode);
        btUseCode.setOnClickListener(this);

        btCheckCode = (Button) findViewById(R.id.btCheckCode);
        btCheckCode.setOnClickListener(this);
        btBack = (Button) findViewById(R.id.btBack);
        btBack.setOnClickListener(this);

        setCustomNetworkConnectionReceiver();
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

            ivUsers[i].setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int viewId = v.getId();

                    if(viewId == ivUser1.getId())
                        showUserOptions(localUsers.get((groupIndex - 1) * 4));

                    if(viewId == ivUser2.getId())
                        showUserOptions(localUsers.get(1 + ((groupIndex - 1) * 4)));

                    if(viewId == ivUser3.getId())
                        showUserOptions(localUsers.get(2 + ((groupIndex - 1) * 4)));

                    if(viewId == ivUser4.getId())
                        showUserOptions(localUsers.get(3 + ((groupIndex - 1) * 4)));

                    return true;
                }
            });
        }

        tvGroupNumberOutOf.setText("   Group number " + groupIndex + " out of " + groupsAmount + "   ");

        if(localUsersSavedOnDatabaseAmount() > ivUsers.length)
            groupSelectorGridLayout.setVisibility(View.VISIBLE);
        else
            groupSelectorGridLayout.setVisibility(View.INVISIBLE);
    }

    private void initiateLocalUsersArrayList() {
        localUsers = new ArrayList<User>();

        if(localUsersSavedOnDatabaseAmount() != 0){
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

    private int localUsersSavedOnDatabaseAmount() {
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

    public void showUserOptions(User user){
        AlertDialog ad;
        AlertDialog.Builder adb;
        adb = new AlertDialog.Builder(this);
        adb.setTitle("Local user options:");
        adb.setIcon(R.drawable.ic_account_icon);
        adb.setMessage("Username: " + user.getUsername());

        final ImageView ivUserImage = new ImageView(LocalUserSelection.this);
        ivUserImage.setImageResource(user.getProfilePictureId());
        ivUserImage.setAdjustViewBounds(true);
        ivUserImage.setMaxHeight(1000);
        ivUserImage.setMaxWidth(1000);
        adb.setView(ivUserImage);

        adb.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                userChosen(user);
            }
        });

        adb.setNegativeButton("Remove", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sqdb = my_db.getWritableDatabase();
                sqdb.delete(DBHelper.TABLE_NAME, DBHelper.USERNAME + "=?", new String[]{user.getUsername()});
                sqdb.close();

                me.setClass(LocalUserSelection.this, LocalUserSelection.class);
                startActivity(me);

                Toast.makeText(LocalUserSelection.this, "User removed successfully.", Toast.LENGTH_SHORT).show();
            }
        });

        ad = adb.create();
        ad.show();
    }

    public void userChosen(User user){
        User.setCurrentUser(user);
        me.setClass(LocalUserSelection.this, MainActivity.class);
        me.putExtra("cameFromLogin", true);
        startActivity(me);
    }

    public void previousGroup(){
        if(0 < groupIndex && groupIndex <= groupsAmount){
            groupIndex--;

            for(int i = 0; i < ivUsers.length; i++){
                ivUsers[i].setVisibility(View.INVISIBLE);
                tvUsers[i].setVisibility(View.INVISIBLE);
            }

            initiateUsersSelectionViews();
            ibtNextGroup.setVisibility(View.VISIBLE);
            if(groupIndex == 1)
                ibtPreviousGroup.setVisibility(View.INVISIBLE);
        }
    }

    public void nextGroup(){
        if(0 <= groupIndex && groupIndex < groupsAmount){
            groupIndex++;

            for(int i = 0; i < ivUsers.length; i++){
                ivUsers[i].setVisibility(View.INVISIBLE);
                tvUsers[i].setVisibility(View.INVISIBLE);
            }

            initiateUsersSelectionViews();
            if(1 < groupIndex)
                ibtPreviousGroup.setVisibility(View.VISIBLE);
            if(groupIndex == groupsAmount)
                ibtNextGroup.setVisibility(View.INVISIBLE);
        }
    }

    public void useCodeMode(){
        btGenerateCode.setVisibility(View.GONE);
        btUseCode.setVisibility(View.GONE);

        tvCodeState.setText("Enter your code in here: ");
        etEnterCode.setVisibility(View.VISIBLE);
        btCheckCode.setVisibility(View.VISIBLE);
        btBack.setVisibility(View.VISIBLE);
    }

    public void useCode(){
        String code = etEnterCode.getText().toString();
        if(internetConnection){
            if(passCodeTests(code))
                getUsersFromFirebaseDatabaseByCode(code);
        }
        else
            Toast.makeText(this, "No internet connection, can't use codes.", Toast.LENGTH_SHORT).show();
    }

    public boolean passCodeTests(String code){
        boolean passTests = true;
        int normalCodeLength = 8;

        if(code.replaceAll(" ", "").equals("") && passTests){
            Toast.makeText(this, "Can't left the field 'code' empty.", Toast.LENGTH_SHORT).show();
            passTests = false;
        }

        if(code.length() != normalCodeLength && passTests){
            Toast.makeText(this, "Codes should be 8 characters long.", Toast.LENGTH_SHORT).show();
            passTests = false;
        }

        return passTests;
    }

    public void getUsersFromFirebaseDatabaseByCode(String code){
        linearLayout.setVisibility(View.GONE);
        localUserSelectionLoadingLinearLayout.setVisibility(View.VISIBLE);

        databaseReference = FirebaseDatabase.getInstance().getReference("codes");
        databaseReference.child(code).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        DataSnapshot dataSnapshot = task.getResult();

                        LocalDateTime expirationTime = LocalDateTime.parse(String.valueOf(dataSnapshot.child("expirationTime").getValue()).replaceAll("\\*", "\\."));

                        boolean isExpired = LocalDateTime.now(ZoneId.of("Asia/Jerusalem")).isAfter(expirationTime);

                        DataSnapshot users = dataSnapshot.child("users");

                        if(!isExpired){
                            localUsers.clear();

                            for(DataSnapshot user : users.getChildren()){
                                String username = String.valueOf(user.child("username").getValue());
                                String password = String.valueOf(user.child("password").getValue());
                                String email = String.valueOf(user.child("email").getValue());
                                double startingWeight = Double.parseDouble(String.valueOf(user.child("startingWeight").getValue()));

                                double targetCalories = Double.parseDouble(String.valueOf(user.child("currentPlan").child("targetCalories").getValue()));
                                double targetProteins = Double.parseDouble(String.valueOf(user.child("currentPlan").child("targetProteins").getValue()));
                                double targetFats = Double.parseDouble(String.valueOf(user.child("currentPlan").child("targetFats").getValue()));
                                Plan plan = new Plan(targetCalories, targetProteins, targetFats);

                                int profilePictureId = Integer.parseInt(String.valueOf(user.child("profilePictureId").getValue()));

                                if(!isUserAlreadyExists(username)) {
                                    User tmpUser = new User(username, password, email, startingWeight, plan, profilePictureId);
                                    addLoggedUserIntoLocalDatabase(tmpUser);
                                    localUsers.add(tmpUser);
                                }
                            }
                            Toast.makeText(LocalUserSelection.this, "Users successfully added.", Toast.LENGTH_SHORT).show();

                            me.setClass(LocalUserSelection.this, LocalUserSelection.class);
                            startActivity(me);
                        }
                        else
                            Toast.makeText(LocalUserSelection.this, "Code expired.", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(LocalUserSelection.this, "Code incorrect.", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(LocalUserSelection.this, "Code incorrect.", Toast.LENGTH_SHORT).show();

                localUserSelectionLoadingLinearLayout.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    public void backToChoosing(){
        btGenerateCode.setVisibility(View.VISIBLE);
        btUseCode.setVisibility(View.VISIBLE);

        tvCodeState.setText("What are you intending to do?");

        btCheckCode.setVisibility(View.GONE);

        tvTimeBeforeExpiration.setVisibility(View.GONE);
        etEnterCode.setVisibility(View.GONE);
        btCheckCode.setVisibility(View.GONE);

        btBack.setVisibility(View.GONE);
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


    private void addUserToDatabase(User user) {  // ZRabD8s7
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

    public void generateCodeAndExpirationDate() {
        if(internetConnection){
            final int randomCodeMaximumLength = 8;
            String allLettersAndDigits = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz0123456789";

            LocalDateTime expirationDate = LocalDateTime.now(ZoneId.of("Asia/Jerusalem")).plusMinutes(10);

            //   LocalDateTime.now(ZoneId.of("Asia/Jerusalem")).isAfter(expirationDate);

            String randomCode = "";
            for(int i = 0; i < randomCodeMaximumLength; i++)
                randomCode += allLettersAndDigits.charAt((int)(Math.random() * allLettersAndDigits.length()));

            btGenerateCode.setVisibility(View.GONE);
            btUseCode.setVisibility(View.GONE);

            tvCodeState.setText("Your code is:" + "\n" + randomCode);
            tvTimeBeforeExpiration.setVisibility(View.VISIBLE);
            btBack.setVisibility(View.VISIBLE);

            linearLayout.setVisibility(View.GONE);
            localUserSelectionLoadingLinearLayout.setVisibility(View.VISIBLE);

            Code code = new Code(randomCode, localUsers, expirationDate.toString());

            saveUsersCodeInFirebase(code);
        }
        else
            Toast.makeText(this, "No internet connection, can't generate code.", Toast.LENGTH_SHORT).show();
    }

    public void saveUsersCodeInFirebase(Code code){
        codesDb = FirebaseDatabase.getInstance();
        databaseReference = codesDb.getReference("codes");
        databaseReference.child(code.getCode()).setValue(code).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(LocalUserSelection.this, "Code successfully created.", Toast.LENGTH_SHORT).show();

                localUserSelectionLoadingLinearLayout.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    public void changeLocalUsersScreenMode(){
        showUserMode = !showUserMode;

        if(showUserMode) {
            tvLocalUserSelectionMainTextView.setVisibility(View.VISIBLE);

            if(groupsAmount > 1)
                groupSelectorGridLayout.setVisibility(View.VISIBLE);

            useOrGenerateCodesLinearLayout.setVisibility(View.GONE);
            localUsersLinearLayout.setVisibility(View.VISIBLE);
            btUseOrGenerateCode.setText("Use / Generate Code");
        }
        else{
            tvLocalUserSelectionMainTextView.setVisibility(View.INVISIBLE);
            groupSelectorGridLayout.setVisibility(View.INVISIBLE);

            localUsersLinearLayout.setVisibility(View.GONE);
            useOrGenerateCodesLinearLayout.setVisibility(View.VISIBLE);
            btUseOrGenerateCode.setText("Show local users");
        }
    }

    public void goToLogin(){
        me.setClass(LocalUserSelection.this, Login.class);
        startActivity(me);
    }

    public void goToRegister(){
        me.setClass(LocalUserSelection.this, Register.class);
        startActivity(me);
    }

    public void setCustomNetworkConnectionReceiver(){
        networkConnectionReceiver = new NetworkConnectionReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try{
                    internetConnection = isOnline(context);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
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
        mediaPlayer = MediaPlayer.create(LocalUserSelection.this, activeSong.getId());
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

        if(viewId == btLoginAnotherUser.getId())
            goToLogin();

        if(viewId == btRegisterAnotherUser.getId())
            goToRegister();

        if(viewId == ibtNextGroup.getId())
            nextGroup();

        if(viewId == ibtPreviousGroup.getId())
            previousGroup();

        if(viewId == btUseOrGenerateCode.getId())
            changeLocalUsersScreenMode();

        if(viewId == btGenerateCode.getId())
            generateCodeAndExpirationDate();

        if(viewId == btUseCode.getId())
            useCodeMode();

        if(viewId == btCheckCode.getId())
            useCode();

        if(viewId == btBack.getId())
            backToChoosing();
    }

    @Override
    public void onBackPressed(){

    }
}