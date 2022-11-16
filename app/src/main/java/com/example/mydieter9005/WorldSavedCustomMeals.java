package com.example.mydieter9005;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.mydieter9005.databinding.ActivityMainBinding;
import com.example.mydieter9005.databinding.ActivityWorldSavedCustomMealsBinding;
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

public class WorldSavedCustomMeals extends AppCompatActivity implements View.OnClickListener {

    private NetworkConnectionReceiver networkConnectionReceiver;

    private MediaPlayer mediaPlayer;
    private VideoView videoView;

    Button btMultiDinnerSelect, btFinishWorldSavedCustomMeals;
    EditText etFilterWorldSavedCustomMeals;
    LinearLayout linearLayout;
    ListView listView;

    ArrayList<Meal> mealsList = new ArrayList<Meal>();
    ArrayAdapter<Meal> adapter;

    Song activeSong = Song.getSongs().get(0);

    boolean multiSelect = false;
    Meal chosenMultiSelectDinner = null;
    int multiSelectCounter = 0;

    DatabaseReference databaseReference;

    FileInputStream is;
    InputStreamReader isr;
    BufferedReader br;
    Intent me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world_saved_custom_meals);

        me = getIntent();
        if(me.hasExtra("activeSong"))
            activeSong = (Song) me.getSerializableExtra("activeSong");

        listView = (ListView) findViewById(R.id.lvWorldSavedCustomMeals);
        videoView = (VideoView) findViewById(R.id.worldSavedCustomMealsVideoView);
        linearLayout = (LinearLayout) findViewById(R.id.worldSavedCustomMealsLinearLayout);

        btFinishWorldSavedCustomMeals = (Button) findViewById(R.id.btFinishWorldSavedCustomMeals);
        btFinishWorldSavedCustomMeals.setOnClickListener(this);
        btMultiDinnerSelect = (Button) findViewById(R.id.btMultiWorldSavedCustomMealsSelect);
        btMultiDinnerSelect.setOnClickListener(this);

        etFilterWorldSavedCustomMeals = (EditText) findViewById(R.id.etFilterWorldSavedCustomMeals);
        etFilterWorldSavedCustomMeals.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                (WorldSavedCustomMeals.this).adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        setCustomNetworkConnectionReceiver();
        implementSettingsData();
        setListViewAdapter();
        initiateVideoPlayer();
        initiateMediaPlayer();
    }

    public void initiateListViewFields(){
        mealsList.clear();
        databaseReference = FirebaseDatabase.getInstance().getReference("recipes");
        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                ArrayList<Ingredient> ingredientsNeededInfo = new ArrayList<Ingredient>();
                Ingredient ingredient;
                String mealName, ingredientName;
                Double ingredientGrams;

                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        DataSnapshot dataSnapshot = task.getResult();
                        for(DataSnapshot customMeal : dataSnapshot.getChildren()){
                            ingredientsNeededInfo.clear();
                            mealName = customMeal.getKey();
                            for(int i = 0; i < customMeal.getChildrenCount(); i++) {
                                ingredientName = getName(customMeal.child(i + "").getValue().toString());
                                ingredientGrams = getGrams(customMeal.child(i + "").getValue().toString());

                                ingredient = new Ingredient(Ingredient.getIngredientByName(ingredientName), ingredientGrams);
                                ingredientsNeededInfo.add(ingredient);
                            }

                            mealsList.add(new Meal(mealName, ingredientsNeededInfo));
                            adapter.notifyDataSetChanged();
                        }

                        try{
                            unregisterReceiver(networkConnectionReceiver);
                        }
                        catch (IllegalArgumentException e){
                            e.getStackTrace();
                        }
                    }
                    else{
                        notEvenOneCustomMealAdded();
                    }
                }
                else{
                    recipesDatabaseNotFound();
                    Toast.makeText(WorldSavedCustomMeals.this, "Failed to load meals.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public String getName(String nameAndGrams){  // Needed when get ingredient from file.
        return nameAndGrams.split(": ")[0];
    }

    public double getGrams(String nameAndGrams){  // Needed when get ingredient from file.
        return Double.parseDouble((nameAndGrams.split(": ")[1]).split(" ")[0]);
    }

    public void recipesDatabaseNotFound(){
        AlertDialog ad;
        AlertDialog.Builder adb;
        adb = new AlertDialog.Builder(this);
        adb.setTitle("Custom meals not found!");
        adb.setMessage("We have trouble connecting our database right now, please come back later");
        adb.setIcon(R.drawable.ic_error_icon);
        adb.setCancelable(false);

        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                me.setClass(WorldSavedCustomMeals.this, customSelection.class);
                startActivity(me);
            }
        });

        ad = adb.create();
        ad.show();
    }

    public void notEvenOneCustomMealAdded(){
        AlertDialog ad;
        AlertDialog.Builder adb;
        adb = new AlertDialog.Builder(this);
        adb.setTitle("Custom meals not found!");
        adb.setMessage("It's seems like no one saved any custom meal so far, you can be the first!.");
        adb.setIcon(R.drawable.ic_food_icon);
        adb.setCancelable(false);

        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                me.setClass(WorldSavedCustomMeals.this, customSelection.class);
                startActivity(me);
            }
        });

        ad = adb.create();
        ad.show();
    }

    public void setListViewAdapter(){
        initiateListViewFields();

        adapter = new ArrayAdapter<Meal>(this, android.R.layout.simple_list_item_1, mealsList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Meal selectedItem = (Meal) adapterView.getItemAtPosition(i);

                if(!multiSelect){
                    me.setClass(WorldSavedCustomMeals.this, mealsMenu.class);
                    startActivity(me);
                }
                else {
                    chosenMultiSelectDinner = new Meal(chosenMultiSelectDinner, selectedItem);
                    Toast.makeText(WorldSavedCustomMeals.this, selectedItem.getName() + " has added.", Toast.LENGTH_SHORT).show();
                    multiSelectCounter++;
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Meal selectedItem = (Meal) parent.getItemAtPosition(position);

                showMealIngredientsInfo(selectedItem);
                return true;
            }
        });
    }

    public void showMealInfo(Meal meal){
        AlertDialog ad;
        AlertDialog.Builder adb;
        adb = new AlertDialog.Builder(this);
        adb.setTitle("Your meal nutrition: ");
        adb.setMessage(meal.getGrams() + " grams." + "\n" + meal.getProteins() + " proteins." + "\n" + meal.getFats() + " fats." + "\n" + meal.getCalories() + " calories.");
        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        adb.setNegativeButton("Ingredients", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showMealIngredientsInfo(meal);
            }
        });

        adb.setNeutralButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                me.setClass(WorldSavedCustomMeals.this, customMeals.class);
                me.putExtra("globalCustomMealToSave", meal);
                startActivity(me);
            }
        });

        ad = adb.create();
        ad.show();
    }

    public void showMealIngredientsInfo(Meal meal){
        AlertDialog ad;
        AlertDialog.Builder adb;
        adb = new AlertDialog.Builder(this);
        adb.setTitle("Your meal ingredients: ");

        String mealInfo = "";
        for(Ingredient ingredient : meal.getNeededIngredientsForMeal())
            mealInfo += ingredient.getName() + ": " + ingredient.getGrams() + " grams." + "\n";
        adb.setMessage(mealInfo);

        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        adb.setNegativeButton("Nutrition", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showMealInfo(meal);
            }
        });

        adb.setNeutralButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                me.setClass(WorldSavedCustomMeals.this, customMeals.class);
                me.putExtra("globalCustomMealToSave", meal);
                startActivity(me);
            }
        });

        ad = adb.create();
        ad.show();
    }

    public void multiOrSingleSelectUpdate(){
        if(!multiSelect){
            Toast.makeText(this, "Multi select has enabled.", Toast.LENGTH_SHORT).show();
            btMultiDinnerSelect.setText("Disable multi select");
            btFinishWorldSavedCustomMeals.setText("Finish choosing");
            multiSelectCounter = 0;
            multiSelect = true;
        }
        else{
            Toast.makeText(this, "Multi select has disabled.", Toast.LENGTH_SHORT).show();
            btMultiDinnerSelect.setText("Enable multi select");
            btFinishWorldSavedCustomMeals.setText("Finish");
            chosenMultiSelectDinner = null;
            multiSelectCounter = 0;
            multiSelect = false;
        }
    }

    public void finishMultiSelectOrExitWorldSavedCustomMeals(){
        if(multiSelect){
            if(multiSelectCounter == 0){
                Toast.makeText(this, "You didn't choose anything yet.", Toast.LENGTH_SHORT).show();
            }
            else{
                me.setClass(WorldSavedCustomMeals.this, mealsMenu.class);
                startActivity(me);
            }
        }
        else{
            me.setClass(WorldSavedCustomMeals.this, customSelection.class);
            startActivity(me);
        }
    }

    public void setCustomNetworkConnectionReceiver(){
        networkConnectionReceiver = new NetworkConnectionReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try{
                    if(!isOnline(context))
                        noInternetAccess(context);
                    else
                        Toast.makeText(context, "Network connection available.", Toast.LENGTH_SHORT).show();
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
        mediaPlayer = MediaPlayer.create(WorldSavedCustomMeals.this, activeSong.getId());
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
            me.setClass(WorldSavedCustomMeals.this, musicMaster.class);
            me.putExtra("cameToMusicMasterFrom", getLocalClassName());
            startActivity(me);
        }

        if(itemID == R.id.sendToSettings){
            me.setClass(WorldSavedCustomMeals.this, settingsSetter.class);
            me.putExtra("cameToSettingsFrom", getLocalClassName());
            startActivity(me);
        }

        if(itemID == R.id.sendToUserScreen){
            me.setClass(WorldSavedCustomMeals.this, UserInfoScreen.class);
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

        if(viewId == btMultiDinnerSelect.getId())
            multiOrSingleSelectUpdate();

        if(viewId == btFinishWorldSavedCustomMeals.getId())
            finishMultiSelectOrExitWorldSavedCustomMeals();
    }
}