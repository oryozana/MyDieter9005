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
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.ArrayList;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private NetworkConnectionReceiver networkConnectionReceiver;

    private MediaPlayer mediaPlayer;
    private VideoView videoView;

    EditText etGetUsernameLoginInfo, etGetPasswordLoginInfo;
    LinearLayout linearLayout, loginLoadingLinearLayout;
    CheckBox cbSaveLoggedUserInLocalDatabase;
    Button btLogin, btGoToRegister;
    TextView tvForgotPassword;

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

        loginLoadingLinearLayout = (LinearLayout) findViewById(R.id.loginLoadingLinearLayout);
        linearLayout = (LinearLayout) findViewById(R.id.loginLinearLayout);
        videoView = (VideoView) findViewById(R.id.loginVideoView);

        etGetUsernameLoginInfo = (EditText) findViewById(R.id.etGetUsernameLoginInfo);
        etGetPasswordLoginInfo = (EditText) findViewById(R.id.etGetPasswordLoginInfo);

        tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
        tvForgotPassword.setOnClickListener(this);

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
        linearLayout.setVisibility(View.GONE);
        loginLoadingLinearLayout.setVisibility(View.VISIBLE);

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
                    else
                        Toast.makeText(Login.this, "Username or password incorrect.", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(Login.this, "Username or password incorrect.", Toast.LENGTH_SHORT).show();

                loginLoadingLinearLayout.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    public void checkUserInfoByFirebaseDatabase(String username, String enteredEmail){
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        DataSnapshot dataSnapshot = task.getResult();
                        String username = dataSnapshot.getKey();
                        String email = String.valueOf(dataSnapshot.child("email").getValue());

                        if(email.equals(enteredEmail)){
                            double targetCalories = Double.parseDouble(String.valueOf(dataSnapshot.child("currentPlan").child("targetCalories").getValue()));
                            double targetProteins = Double.parseDouble(String.valueOf(dataSnapshot.child("currentPlan").child("targetProteins").getValue()));
                            double targetFats = Double.parseDouble(String.valueOf(dataSnapshot.child("currentPlan").child("targetFats").getValue()));
                            Plan plan = new Plan(targetCalories, targetProteins, targetFats);

                            String password = String.valueOf(dataSnapshot.child("password").getValue());
                            double startingWeight = Double.parseDouble(String.valueOf(dataSnapshot.child("startingWeight").getValue()));
                            int profilePictureId = Integer.parseInt(String.valueOf(dataSnapshot.child("profilePictureId").getValue()));
                            User tmpUser = new User(username, password, email, startingWeight, plan, profilePictureId);

                            newPasswordAlertDialog(tmpUser);
                        }
                        else {
                            Toast.makeText(Login.this, "Username or email incorrect.", Toast.LENGTH_SHORT).show();
                            forgotPasswordAlertDialog();
                        }
                    }
                    else {
                        Toast.makeText(Login.this, "Username or email incorrect.", Toast.LENGTH_SHORT).show();
                        forgotPasswordAlertDialog();
                    }
                }
                else {
                    Toast.makeText(Login.this, "Username or email incorrect.", Toast.LENGTH_SHORT).show();
                    forgotPasswordAlertDialog();
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

    public void forgotPasswordAlertDialog(){
        AlertDialog ad;
        AlertDialog.Builder adb;
        adb = new AlertDialog.Builder(this);
        adb.setTitle("Enter your username and email:");
        adb.setIcon(R.drawable.ic_account_icon);
        adb.setCancelable(false);

        loginLoadingLinearLayout.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);

        LinearLayout layout = new LinearLayout(this);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);

        final EditText etUsername = new EditText(this);
        etUsername.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        etUsername.setHint("Username: ");
        layout.addView(etUsername);

        final EditText etEmail = new EditText(this);
        etEmail.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        etEmail.setHint("Email: ");
        layout.addView(etEmail);

        adb.setView(layout);

        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String username = etUsername.getText().toString();
                String email = etEmail.getText().toString();

                if(!username.replaceAll(" ", "").equals("") && !email.replaceAll(" ", "").equals("")) {
                    loginLoadingLinearLayout.setVisibility(View.VISIBLE);
                    checkUserInfoByFirebaseDatabase(username, email);
                }
                else {
                    Toast.makeText(Login.this, "One or more of the fields ware empty.", Toast.LENGTH_SHORT).show();
                    forgotPasswordAlertDialog();
                }
            }
        });

        adb.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                linearLayout.setVisibility(View.VISIBLE);
            }
        });

        ad = adb.create();
        ad.show();
    }

    public void newPasswordAlertDialog(User user){
        AlertDialog ad;
        AlertDialog.Builder adb;
        adb = new AlertDialog.Builder(this);
        adb.setTitle("Enter your new password:");
        adb.setIcon(R.drawable.ic_account_icon);
        adb.setCancelable(false);

        loginLoadingLinearLayout.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);

        LinearLayout layout = new LinearLayout(Login.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);

        EditText etNewPassword1 = new EditText(Login.this);
        etNewPassword1.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        etNewPassword1.setHint("Enter Password: ");
        layout.addView(etNewPassword1);

        EditText etNewPassword2 = new EditText(Login.this);
        etNewPassword2.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        etNewPassword2.setHint("Enter Password again: ");
        layout.addView(etNewPassword2);

        adb.setView(layout);

        adb.setPositiveButton("Change password", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String newPassword1 = etNewPassword1.getText().toString();
                String newPassword2 = etNewPassword2.getText().toString();

                if(!newPassword1.replaceAll(" ", "").equals("") && !newPassword2.replaceAll(" ", "").equals("")){
                    if(newPassword1.equals(newPassword2)) {
                        loginLoadingLinearLayout.setVisibility(View.VISIBLE);
                        changePassword(user, newPassword1);
                    }
                    else{
                        Toast.makeText(Login.this, "Passwords doesn't match.", Toast.LENGTH_SHORT).show();
                        newPasswordAlertDialog(user);
                    }
                }
                else{
                    Toast.makeText(Login.this, "One or more fields were empty.", Toast.LENGTH_SHORT).show();
                    newPasswordAlertDialog(user);
                }
            }
        });

        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                linearLayout.setVisibility(View.VISIBLE);
            }
        });

        ad = adb.create();
        ad.show();
    }

    public void changePassword(User user, String newPassword){
        boolean passTests = passChangePasswordTests(newPassword);

        if(passTests){
            user.setPassword(newPassword);
            updateUserPasswordInFirebaseAndInLocalDatabase(user, newPassword);
        }
        else{
            loginLoadingLinearLayout.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
        }
    }

    public boolean passChangePasswordTests(String newPassword){
        boolean passTests = true;

        if(newPassword.length() < 4 && passTests){
            Toast.makeText(this, "Password should be at least 4 characters wide!", Toast.LENGTH_SHORT).show();
            passTests = false;
        }

        return passTests;
    }

    public void updateUserPasswordInFirebaseAndInLocalDatabase(User user, String newPassword){
        usersDb = FirebaseDatabase.getInstance();
        databaseReference = usersDb.getReference("users");
        databaseReference.child(user.getUsername()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                User.setCurrentUser(user);

                sqdb=my_db.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(DBHelper.PASSWORD, newPassword);

                sqdb.update(DBHelper.TABLE_NAME, cv,DBHelper.USERNAME+"=?", new String[]{user.getUsername()});
                sqdb.close();

                Toast.makeText(Login.this, "Password successfully changed.", Toast.LENGTH_SHORT).show();

                me.setClass(Login.this, MainActivity.class);
                startActivity(me);
            }
        });
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

        if(viewId == tvForgotPassword.getId())
            forgotPasswordAlertDialog();
    }

    @Override
    public void onBackPressed() {
        me.setClass(Login.this, LocalUserSelection.class);
        startActivity(me);
    }
}
