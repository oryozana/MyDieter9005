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
import android.inputmethodservice.ExtractEditText;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
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
    LinearLayout linearLayout, registerLoadingLinearLayout;
    Button btRegister, btGoToLogin, btGetHelpCreatingAPlan;
    CheckBox cbRememberRegisteredUserInLocalDatabase;

    FileAndDatabaseHelper fileAndDatabaseHelper;
    Song activeSong = Song.getSongs().get(0);

    Plan currentGeneratedPlan, maintainWeightPlan, loseWeightPlan, gainWeightPlan;
    int currentPlanIndex = 1, maxPlansAmount = 3;

    ArrayList<String> usernamesList = new ArrayList<String>();
    int userPicturesAmount = 10;

    FirebaseDatabase usersDb;
    DatabaseReference databaseReference;

    Intent exitAppService;
    Intent me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        me = getIntent();
        if(me.hasExtra("activeSong"))
            activeSong = (Song) me.getSerializableExtra("activeSong");

        registerLoadingLinearLayout = (LinearLayout) findViewById(R.id.registerLoadingLinearLayout);
        linearLayout = (LinearLayout) findViewById(R.id.registerLinearLayout);
        videoView = (VideoView) findViewById(R.id.registerVideoView);

        etGetUsername = (EditText) findViewById(R.id.etGetUsername);
        etGetPassword = (EditText) findViewById(R.id.etGetPassword);
        etGetEmail = (EditText) findViewById(R.id.etGetEmail);
        etGetStartingWeight = (EditText) findViewById(R.id.etGetStartingWeight);

        etGetTargetCalories = (EditText) findViewById(R.id.etGetTargetCalories);
        etGetTargetProteins = (EditText) findViewById(R.id.etGetTargetProteins);
        etGetTargetFats = (EditText) findViewById(R.id.etGetTargetFats);

        btGetHelpCreatingAPlan = (Button) findViewById(R.id.btGetHelpCreatingAPlan);
        btGetHelpCreatingAPlan.setOnClickListener(this);
        btGoToLogin = (Button) findViewById(R.id.btGoToLogin);
        btGoToLogin.setOnClickListener(this);
        btRegister = (Button) findViewById(R.id.btRegister);
        btRegister.setOnClickListener(this);

        cbRememberRegisteredUserInLocalDatabase = (CheckBox) findViewById(R.id.cbRememberRegisteredUserInLocalDatabase);

        fileAndDatabaseHelper = new FileAndDatabaseHelper(this, me);
        activeSong = fileAndDatabaseHelper.implementSettingsData();

        setCustomNetworkConnectionReceiver();
        getAllExistingUsernames();
        initiateVideoPlayer();
        initiateMediaPlayer();
    }

    public void createUserAndUserPlan(){
        if(passUserInfoTests()){  // Not to be included in the User(dataSnapshot) builder.
            double targetCalories = Double.parseDouble(etGetTargetCalories.getText().toString());
            double targetProteins = Double.parseDouble(etGetTargetProteins.getText().toString());
            double targetFats = Double.parseDouble(etGetTargetFats.getText().toString());
            Plan userPlan = new Plan(targetCalories, targetProteins, targetFats);

            String username = etGetUsername.getText().toString();
            String password = etGetPassword.getText().toString();
            String email = etGetEmail.getText().toString();
            double startingWeight = Double.parseDouble(etGetStartingWeight.getText().toString());

            int profilePictureId = getResources().getIdentifier("user_picture_" + (((int)(Math.random() * userPicturesAmount)) + 1), "drawable", getPackageName());
            User user = new User(username, password, email, startingWeight, userPlan, profilePictureId, DailyMenu.getTodayMenu().generateEmptyDailyMenuDescriptionForFiles());

            user.uploadUserDailyMenusIntoTemporaryFile(Register.this);

            linearLayout.setVisibility(View.GONE);
            registerLoadingLinearLayout.setVisibility(View.VISIBLE);

            saveUserInFirebaseAndInLocalDatabase(user);
        }
    }

    public void saveUserInFirebaseAndInLocalDatabase(User user){
        usersDb = FirebaseDatabase.getInstance();
        databaseReference = usersDb.getReference("users");
        databaseReference.child(user.getUsername()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(cbRememberRegisteredUserInLocalDatabase.isChecked())
                    fileAndDatabaseHelper.addRegisteredUserToDatabase(user);

                User.setCurrentUser(user);
                Toast.makeText(Register.this, "User successfully created.", Toast.LENGTH_SHORT).show();

                me.setClass(Register.this, MainActivity.class);
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

        if(passTests){
            if(etGetStartingWeight.getText().toString().replaceAll(" ", "").equals("")) {
                Toast.makeText(this, "One or more of the fields is empty.", Toast.LENGTH_SHORT).show();
                passTests = false;
            }
            else{
                if(!(0 < Double.parseDouble(etGetStartingWeight.getText().toString()) && Double.parseDouble(etGetStartingWeight.getText().toString()) < 500)){
                    Toast.makeText(this, "Starting weight should be between 0 to 500 kg.", Toast.LENGTH_SHORT).show();
                    passTests = false;
                }
            }
        }

        if(passTests){
            if(etGetTargetCalories.getText().toString().replaceAll(" ", "").equals("")){
                Toast.makeText(this, "One or more of the fields is empty.", Toast.LENGTH_SHORT).show();
                passTests = false;
            }
            else{
                if(!(0 < Double.parseDouble(etGetTargetCalories.getText().toString()) && Double.parseDouble(etGetTargetCalories.getText().toString()) < 5000)){
                    Toast.makeText(this, "Target calories should be between 0 to 5000.", Toast.LENGTH_SHORT).show();
                    passTests = false;
                }
            }
        }

        if(passTests){
            if(etGetTargetProteins.getText().toString().replaceAll(" ", "").equals("")){
                Toast.makeText(this, "One or more of the fields is empty.", Toast.LENGTH_SHORT).show();
                passTests = false;
            }
            else{
                if(!(0 < Double.parseDouble(etGetTargetProteins.getText().toString()) && Double.parseDouble(etGetTargetProteins.getText().toString()) < 1000)){
                    Toast.makeText(this, "Target proteins should be between 0 to 1000.", Toast.LENGTH_SHORT).show();
                    passTests = false;
                }
            }
        }

        if(passTests){
            if(etGetTargetFats.getText().toString().replaceAll(" ", "").equals("")){
                Toast.makeText(this, "One or more of the fields is empty.", Toast.LENGTH_SHORT).show();
                passTests = false;
            }
            else{
                if(!(0 < Double.parseDouble(etGetTargetFats.getText().toString()) && Double.parseDouble(etGetTargetFats.getText().toString()) < 1000)){
                    Toast.makeText(this, "Target fats should be between 0 to 1000.", Toast.LENGTH_SHORT).show();
                    passTests = false;
                }
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

    public void generatePlanAlertDialog(){
        AlertDialog ad;
        AlertDialog.Builder adb;
        adb = new AlertDialog.Builder(Register.this);

        View customAlertDialog = LayoutInflater.from(Register.this).inflate(R.layout.generate_plan_alert_dialog, null);

        LinearLayout generatePlansLinearLayout = (LinearLayout) customAlertDialog.findViewById(R.id.generatePlansLinearLayout);
        EditText etGetAge = (EditText) customAlertDialog.findViewById(R.id.etGetAge);
        EditText etGetHeight = (EditText) customAlertDialog.findViewById(R.id.etGetHeight);
        EditText etGetWeight = (EditText) customAlertDialog.findViewById(R.id.etGetWeight);
        Spinner sHowActiveAreYou = (Spinner) customAlertDialog.findViewById(R.id.sHowActiveAreYou);
        RadioGroup rgChooseGender = (RadioGroup) customAlertDialog.findViewById(R.id.rgChooseGender);
        Button btGeneratePlans = (Button) customAlertDialog.findViewById(R.id.btGeneratePlans);

        ArrayAdapter<String> alertDialogAdapter = new ArrayAdapter<String>(Register.this, android.R.layout.simple_spinner_dropdown_item, Plan.getActiveLevelOptions());
        sHowActiveAreYou.setAdapter(alertDialogAdapter);

        LinearLayout showPlansLinearLayout = (LinearLayout) customAlertDialog.findViewById(R.id.showPlansLinearLayout);
        TextView tvPlanName = (TextView) customAlertDialog.findViewById(R.id.tvPlanName);
        TextView tvPlanCalories = (TextView) customAlertDialog.findViewById(R.id.tvPlanCalories);
        TextView tvPlanProteins = (TextView) customAlertDialog.findViewById(R.id.tvPlanProteins);
        TextView tvPlanFats = (TextView) customAlertDialog.findViewById(R.id.tvPlanFats);
        ImageButton ibtPreviousPlan = (ImageButton) customAlertDialog.findViewById(R.id.ibtPreviousPlan);
        TextView tvPlanNumberOutOf = (TextView) customAlertDialog.findViewById(R.id.tvPlanNumberOutOf);
        ImageButton ibtNextPlan = (ImageButton) customAlertDialog.findViewById(R.id.ibtNextPlan);
        Button btChoosePlan = (Button) customAlertDialog.findViewById(R.id.btChoosePlan);
        Button btCancelPlan = (Button) customAlertDialog.findViewById(R.id.btCancelPlan);

        adb.setView(customAlertDialog);
        ad = adb.create();

        btGeneratePlans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String testWeight = etGetWeight.getText().toString();
                String testHeight = etGetHeight.getText().toString();
                String testAge = etGetAge.getText().toString();
                String activeLevel = sHowActiveAreYou.getSelectedItem().toString();

                if(passAlertDialogInfoTests(testWeight, testHeight, testAge, activeLevel)) {
                    double weight = Double.parseDouble(testWeight);
                    double height = Double.parseDouble(testHeight);
                    int age = Integer.parseInt(testAge);

                    String gender;
                    if(rgChooseGender.getCheckedRadioButtonId() == R.id.rbChooseMale)
                        gender = "Male";
                    else
                        gender = "Female";

                    loseWeightPlan = new Plan("Lose weight", gender, weight, height, age, activeLevel);
                    maintainWeightPlan = new Plan("Maintain weight", gender, weight, height, age, activeLevel);
                    gainWeightPlan = new Plan("Gain weight", gender, weight, height, age, activeLevel);

                    generatePlansLinearLayout.setVisibility(View.GONE);
                    showPlansLinearLayout.setVisibility(View.VISIBLE);

                    tvPlanName.setText("Name: Lose weight");
                    tvPlanCalories.setText("Calories: " + loseWeightPlan.getTargetCalories());
                    tvPlanProteins.setText("Proteins: " + loseWeightPlan.getTargetProteins());
                    tvPlanFats.setText("Fats: " + loseWeightPlan.getTargetFats());

                    tvPlanNumberOutOf.setText("Plan number 1 out of " + maxPlansAmount);
                    ibtPreviousPlan.setVisibility(View.INVISIBLE);
                    ibtNextPlan.setVisibility(View.VISIBLE);

                    currentPlanIndex = 1;
                }
            }
        });

        ibtPreviousPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(0 < currentPlanIndex && currentPlanIndex <= maxPlansAmount){
                    currentPlanIndex--;

                    if(currentPlanIndex == 1)
                        currentGeneratedPlan = loseWeightPlan;
                    if(currentPlanIndex == 2)
                        currentGeneratedPlan = maintainWeightPlan;
                    if(currentPlanIndex == 3)
                        currentGeneratedPlan = gainWeightPlan;

                    tvPlanName.setText("Name: " + currentGeneratedPlan.getGoal());
                    tvPlanCalories.setText("Calories: " + currentGeneratedPlan.getTargetCalories());
                    tvPlanProteins.setText("Proteins: " + currentGeneratedPlan.getTargetProteins());
                    tvPlanFats.setText("Fats: " + currentGeneratedPlan.getTargetFats());

                    tvPlanNumberOutOf.setText("Plan number " + currentPlanIndex + " out of " + maxPlansAmount);

                    ibtNextPlan.setVisibility(View.VISIBLE);
                    if(currentPlanIndex == 1)
                        ibtPreviousPlan.setVisibility(View.INVISIBLE);
                }
            }
        });

        ibtNextPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(0 <= currentPlanIndex && currentPlanIndex < maxPlansAmount) {
                    currentPlanIndex++;

                    if(currentPlanIndex == 1)
                        currentGeneratedPlan = loseWeightPlan;
                    if(currentPlanIndex == 2)
                        currentGeneratedPlan = maintainWeightPlan;
                    if(currentPlanIndex == 3)
                        currentGeneratedPlan = gainWeightPlan;

                    tvPlanName.setText("Name: " + currentGeneratedPlan.getGoal());
                    tvPlanCalories.setText("Calories: " + currentGeneratedPlan.getTargetCalories());
                    tvPlanProteins.setText("Proteins: " + currentGeneratedPlan.getTargetProteins());
                    tvPlanFats.setText("Fats: " + currentGeneratedPlan.getTargetFats());

                    tvPlanNumberOutOf.setText("Plan number " + currentPlanIndex + " out of " + maxPlansAmount);

                    if(1 < currentPlanIndex)
                        ibtPreviousPlan.setVisibility(View.VISIBLE);
                    if(currentPlanIndex == maxPlansAmount)
                        ibtNextPlan.setVisibility(View.INVISIBLE);
                }
            }
        });

        btChoosePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Register.this, "The plan to " + currentGeneratedPlan.getGoal() + " chosen successfully !", Toast.LENGTH_SHORT).show();
                etGetTargetCalories.setText(currentGeneratedPlan.getTargetCalories() + "");
                etGetTargetProteins.setText(currentGeneratedPlan.getTargetProteins() + "");
                etGetTargetFats.setText(currentGeneratedPlan.getTargetFats() + "");
                ad.cancel();
            }
        });

        btCancelPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.cancel();
            }
        });

        ad.show();
    }

    public boolean passAlertDialogInfoTests(String weight, String height, String age, String activeLevel) {
        boolean passTests = true;

        if(weight.replaceAll(" ", "").equals("")){
            Toast.makeText(this, "One or more of the fields is empty.", Toast.LENGTH_SHORT).show();
            passTests = false;
        }
        else{
            if(!(0 < Double.parseDouble(weight) && Double.parseDouble(weight) < 500)){
                Toast.makeText(this, "Starting weight should be between 0 to 500 kg.", Toast.LENGTH_SHORT).show();
                passTests = false;
            }
        }

        if(passTests){
            if(height.replaceAll(" ", "").equals("")){
                Toast.makeText(this, "One or more of the fields is empty.", Toast.LENGTH_SHORT).show();
                passTests = false;
            }
            else{
                if((!(50 < Double.parseDouble(height) && Double.parseDouble(height) < 250))){
                    Toast.makeText(this, "Height should be between 50 to 250 cm.", Toast.LENGTH_SHORT).show();
                    passTests = false;
                }
            }
        }

        if(passTests){
            if(age.replaceAll(" ", "").equals("")){
                Toast.makeText(this, "One or more of the fields is empty.", Toast.LENGTH_SHORT).show();
                passTests = false;
            }
            else{
                if((!(0 < Integer.parseInt(age) && Integer.parseInt(age) < 120))){
                    Toast.makeText(this, "Age should be between 0 to 120 years.", Toast.LENGTH_SHORT).show();
                    passTests = false;
                }
            }
        }

        if(activeLevel == null && passTests){
            Toast.makeText(this, "Choose an active level first.", Toast.LENGTH_SHORT).show();
            passTests = false;
        }

        return passTests;
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
        startService(exitAppService);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        if(viewId == btGetHelpCreatingAPlan.getId())
            generatePlanAlertDialog();

        if(viewId == btRegister.getId())
            createUserAndUserPlan();

        if(viewId == btGoToLogin.getId())
            goToLogin();
    }

    @Override
    public void onBackPressed() {
        me.setClass(Register.this, LocalUserSelection.class);
        startActivity(me);
    }
}
