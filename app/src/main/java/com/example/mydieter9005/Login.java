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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private NetworkConnectionReceiver networkConnectionReceiver;

    private MediaPlayer mediaPlayer;
    private VideoView videoView;

    EditText etGetUsernameLoginInfo, etGetPasswordLoginInfo;
    LinearLayout linearLayout, loginLoadingLinearLayout;
    CheckBox cbRememberLoggedUserInLocalDatabase;
    Button btLogin, btGoToRegister;
    TextView tvForgotPassword;

    FileAndDatabaseHelper fileAndDatabaseHelper;
    Song activeSong = Song.getSongs().get(0);

    User forgotUser;

    FirebaseDatabase usersDb;
    DatabaseReference databaseReference;

    Intent me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        me = getIntent();
        if(me.hasExtra("activeSong"))
            activeSong = (Song) me.getSerializableExtra("activeSong");

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

        cbRememberLoggedUserInLocalDatabase = (CheckBox) findViewById(R.id.cbRememberLoggedUserInLocalDatabase);

        fileAndDatabaseHelper = new FileAndDatabaseHelper(this, me);
        activeSong = fileAndDatabaseHelper.implementSettingsData();

        setCustomNetworkConnectionReceiver();
        initiateVideoPlayer();
        initiateMediaPlayer();
    }

    public void getUserFromFirebaseDatabase(String username, String enteredPassword){
        linearLayout.setVisibility(View.GONE);
        loginLoadingLinearLayout.setVisibility(View.VISIBLE);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        DataSnapshot dataSnapshot = task.getResult();
                        String password = String.valueOf(dataSnapshot.child("password").getValue());

                        if(enteredPassword.equals(password)){
                            User.setCurrentUser(new User(dataSnapshot));
                            User.getCurrentUser().uploadUserDailyMenusIntoTemporaryFile(Login.this);

//                            DataSnapshot dailyMenus = dataSnapshot.child("dailyMenus");
//                            for(DataSnapshot dailyMenu : dailyMenus.getChildren()){
//                                String dailyMenuDate = dailyMenu.getKey();
//                                for(int i = 0; i < dailyMenu.getChildrenCount(); i++){
//
//                                }
//                                User.getCurrentUser().addDailyMeals(DailyMenu.generateDailyMenuObjectFromFile(dailyMenu.getValue()));
//                            }

                            if(cbRememberLoggedUserInLocalDatabase.isChecked())
                                fileAndDatabaseHelper.addLoggedUserIntoLocalDatabase(User.getCurrentUser());

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

    public void forgotPasswordAlertDialog() {
        AlertDialog ad;
        AlertDialog.Builder adb;
        adb = new AlertDialog.Builder(Login.this);

        View customAlertDialog = LayoutInflater.from(Login.this).inflate(R.layout.forgot_password_alert_dialog, null);

        LinearLayout forgotPasswordLinearLayout = (LinearLayout) customAlertDialog.findViewById(R.id.forgotPasswordLinearLayout);
        EditText etGetUsernameForgot = (EditText) customAlertDialog.findViewById(R.id.etGetUsernameForgot);
        EditText etGetEmailForgot = (EditText) customAlertDialog.findViewById(R.id.etGetEmailForgot);
        Button btCancelAndExitForgot = (Button) customAlertDialog.findViewById(R.id.btCancelAndExitForgot);
        Button btContinueForgot = (Button) customAlertDialog.findViewById(R.id.btContinueForgot);

        LinearLayout newPasswordLinearLayout = (LinearLayout) customAlertDialog.findViewById(R.id.newPasswordLinearLayout);
        EditText etNewPassword1 = (EditText) customAlertDialog.findViewById(R.id.etNewPassword1);
        EditText etNewPassword2 = (EditText) customAlertDialog.findViewById(R.id.etNewPassword2);
        Button btBackForgot = (Button) customAlertDialog.findViewById(R.id.btBackForgot);
        Button btChangeForgot = (Button) customAlertDialog.findViewById(R.id.btChangeForgot);

        LinearLayout loadingLinearLayout = (LinearLayout) customAlertDialog.findViewById(R.id.loadingLinearLayout);

        adb.setView(customAlertDialog);
        ad = adb.create();

        btCancelAndExitForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.cancel();
            }
        });

        btContinueForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredUsername = etGetUsernameForgot.getText().toString();
                String enteredEmail = etGetEmailForgot.getText().toString();

                if(!enteredUsername.replaceAll(" ", "").equals("") && !enteredEmail.replaceAll(" ", "").equals("")) {
                    forgotPasswordLinearLayout.setVisibility(View.GONE);
                    loadingLinearLayout.setVisibility(View.VISIBLE);

                    databaseReference = FirebaseDatabase.getInstance().getReference("users");
                    databaseReference.child(enteredUsername).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if(task.isSuccessful()){
                                if(task.getResult().exists()){
                                    DataSnapshot dataSnapshot = task.getResult();
                                    String username = dataSnapshot.getKey();
                                    String email = String.valueOf(dataSnapshot.child("email").getValue());

                                    String enteredEmail = etGetEmailForgot.getText().toString();

                                    if(email.equals(enteredEmail)){
                                        forgotUser = new User(dataSnapshot);
                                        forgotUser.uploadUserDailyMenusIntoTemporaryFile(Login.this);

                                        loadingLinearLayout.setVisibility(View.GONE);
                                        newPasswordLinearLayout.setVisibility(View.VISIBLE);
                                        Toast.makeText(Login.this, "User found successfully.", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(Login.this, "Username or email incorrect.", Toast.LENGTH_SHORT).show();
                                        loadingLinearLayout.setVisibility(View.GONE);
                                        forgotPasswordLinearLayout.setVisibility(View.VISIBLE);
                                    }
                                }
                                else {
                                    Toast.makeText(Login.this, "Username or email incorrect.", Toast.LENGTH_SHORT).show();
                                    loadingLinearLayout.setVisibility(View.GONE);
                                    forgotPasswordLinearLayout.setVisibility(View.VISIBLE);
                                }
                            }
                            else {
                                Toast.makeText(Login.this, "Username or email incorrect.", Toast.LENGTH_SHORT).show();
                                loadingLinearLayout.setVisibility(View.GONE);
                                forgotPasswordLinearLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(Login.this, "One or more of the fields were empty.", Toast.LENGTH_SHORT).show();
                    loadingLinearLayout.setVisibility(View.GONE);
                    forgotPasswordLinearLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        btBackForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotUser = null;

                newPasswordLinearLayout.setVisibility(View.GONE);
                forgotPasswordLinearLayout.setVisibility(View.VISIBLE);
            }
        });

        btChangeForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword1 = etNewPassword1.getText().toString();
                String newPassword2 = etNewPassword2.getText().toString();

                if(!newPassword1.replaceAll(" ", "").equals("") && !newPassword2.replaceAll(" ", "").equals("")){
                    if(newPassword1.equals(newPassword2)) {
                        newPasswordLinearLayout.setVisibility(View.GONE);
                        loadingLinearLayout.setVisibility(View.VISIBLE);
                        changePassword(newPassword1);
                    }
                    else
                        Toast.makeText(Login.this, "Passwords doesn't match.", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(Login.this, "One or more fields were empty.", Toast.LENGTH_SHORT).show();
            }
        });

        ad.show();
    }

    public void changePassword(String newPassword){
        boolean passTests = passChangePasswordTests(newPassword);

        if(passTests){
            forgotUser.setPassword(newPassword);
            updateUserPasswordInFirebaseAndInLocalDatabase(newPassword);
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

    public void updateUserPasswordInFirebaseAndInLocalDatabase(String newPassword){
        usersDb = FirebaseDatabase.getInstance();
        databaseReference = usersDb.getReference("users");
        databaseReference.child(forgotUser.getUsername()).setValue(forgotUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                User.setCurrentUser(forgotUser);

                fileAndDatabaseHelper.updateUserPasswordInLocalDatabase(forgotUser, newPassword);

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
