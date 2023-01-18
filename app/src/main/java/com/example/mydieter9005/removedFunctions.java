package com.example.mydieter9005;

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
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class removedFunctions {

    // From: ingredientPickup:

//    public int lookForSpecialWords(String[] mealParts, int i){
//        String previousIngredient, middleIngredient, nextIngredient;
//        int combo = 1;
//
//        if(mealParts[i].equals("oil") || mealParts[i].equals("powder")){
//            nextIngredient = mealParts[i - 1] + " " + mealParts[i];
//            removeIfNeeded(mealParts[i - 1], 1);
//            addIfNeeded(nextIngredient);
//            i += combo;
//            return i;
//        }
//
//        if(mealParts[i].equals("ice") || foodCompanies.contains(mealParts[i])){
//            previousIngredient = mealParts[i] + " " + mealParts[i + 1];
//            addIfNeeded(previousIngredient);
//            i += combo;
//            return i;
//        }
//
//        if (mealParts[i].equals("flavored")){
//            middleIngredient = mealParts[i - 1] + " " + mealParts[i] + " " + mealParts[i + 1];
//            if(mealParts[i + 1].equals("ice") || foodCompanies.contains(mealParts[i])){
//                middleIngredient += " " + mealParts[i + 2];
//                combo++;
//            }
//            addIfNeeded(middleIngredient);
//            removeIfNeeded(mealParts[i - 1], 1);
//            i += combo;
//            return i;
//        }
//        return i;
//    }

//    public void removeIfNeeded(String ingredient, int amountToRemove){
//        if(amount[ingredients.indexOf(ingredient)] == amountToRemove){
//            finalIngredients.remove(ingredient);
//            ingredient_amount -= 1;
//        }
//        else{
//            amount[ingredients.indexOf(ingredient)] -= amountToRemove;
//        }
//    }

        // From: selections activities.

//    public void setListViewFields(){
//        String[] fields = new String[mealsList.size()];
//        for(int i = 0; i < mealsList.size(); i += 3){
//            String field = mealsList.get(i) + ": " + mealsList.get(i + 1) + " calories, " + mealsList.get(i + 2) + " minutes.";
//            fields[i / 3] = field;
//        }
//        setListViewAdapter(fields);
//    }



//    public void setSavedModifiedMealsInfo(){
//        String data = getFileData(modifiedMealsFileName);
//        data = data.substring(data.indexOf('\n') + 1);
//
//        String[] dataParts = data.split("\n");
//        String[] mealPart;
//        String name;
//        double grams, proteins, fats, calories;
//        for(int i = 0; i < dataParts.length; i++){
//            mealPart = dataParts[i].split(" , ");
//
//            name = mealPart[0];
//            grams = Double.parseDouble(mealPart[1]);
//            proteins = Double.parseDouble(mealPart[2]);
//            fats = Double.parseDouble(mealPart[3]);
//            calories = Double.parseDouble(mealPart[4]);
//
//            Meal modifiedMeal = new Meal(name, grams, proteins, fats, calories);
//            if(getMealIndexInMealsList(modifiedMeal) != -1)  // Check if exist inside mealsList.
//                mealsList.set(getMealIndexInMealsList(modifiedMeal), modifiedMeal);
//        }
//    }


        // From: finishMeals:

//    public void initiateMealsRecipes(){
//        for(int mealIndex = 0; mealIndex < meals.length; mealIndex++) {
//            String meal = meals[mealIndex];
//            if (meal != null) {
//                mealParts = meal.split(" ", 100);
//                mealParts[0] = mealParts[0].toLowerCase();
//                for (int i = 0; i < mealParts.length; i++) {
//                    mealParts[i] = mealParts[i].replaceAll(" ", "");
//
//                    if (ingredients.contains(Ingredient.getIngredientByName(mealParts[i]))) {
//                        addIfNeeded(mealParts[i], mealIndex);
//                        if (mealParts[i].equals("olive")) {
//                            addExtra("olive", 7, mealIndex);
//                        }
//                    }
//
//                    i = lookForSpecialWords(mealParts, i, mealIndex);
//                    addIfMiniMealInside(mealParts[i], mealIndex);
//                }
//                updateMealName(meal, mealIndex);
//            }
//        }
//    }
//
//    public int lookForSpecialWords(String[] mealParts, int i, int mealIndex){
//        String previousIngredient, middleIngredient, nextIngredient;
//        int combo = 1;
//
//        if(mealParts[i].equals("oil") || mealParts[i].equals("powder")){
//            nextIngredient = mealParts[i - 1] + " " + mealParts[i];
//            removeIfNeeded(mealParts[i - 1], 1, mealIndex);
//            addIfNeeded(nextIngredient, mealIndex);
//            i += combo;
//            return i;
//        }
//
//        if(mealParts[i].equals("ice") || foodCompanies.contains(mealParts[i])){
//            previousIngredient = mealParts[i] + " " + mealParts[i + 1];
//            addIfNeeded(previousIngredient, mealIndex);
//            i += combo;
//            return i;
//        }
//
//        if (mealParts[i].equals("flavored")){
//            middleIngredient = mealParts[i - 1] + " " + mealParts[i] + " " + mealParts[i + 1];
//            if(mealParts[i + 1].equals("ice") || foodCompanies.contains(mealParts[i])){
//                middleIngredient += " " + mealParts[i + 2];
//                combo++;
//            }
//            addIfNeeded(middleIngredient, mealIndex);
//            removeIfNeeded(mealParts[i - 1], 1, mealIndex);
//            i += combo;
//            return i;
//        }
//        return i;
//    }
//
//    public void addIfMiniMealInside(String mealPart, int mealIndex){
//        if (mealPart.equals("toast")) {
//            addIfNeeded("bread", mealIndex);
//            addIfNeeded("yellow cheese", mealIndex);
//            addIfNeeded("ketchup", mealIndex);
//            addIfNeeded("thousand island dressing", mealIndex);
//        }
//        if (mealPart.equals("salad")) {
//            addIfNeeded("tomato", mealIndex);
//            addIfNeeded("cucumber", mealIndex);
//            addIfNeeded("lettuce", mealIndex);
//        }
//    }
//
//    public void addExtra(String ingredient, int amount, int mealIndex){
//        if(mealIndex == 0){
//            int index = breakfastIngredientsList.indexOf(ingredient);
//            breakfastIngredientsAmount.add(index, breakfastIngredientsAmount.get(index) + amount);
//        }
//
//        if(mealIndex == 1){
//            int index = lunchIngredientsList.indexOf(ingredient);
//            lunchIngredientsAmount.add(index, lunchIngredientsAmount.get(index) + amount);
//        }
//
//        if(mealIndex == 2){
//            int index = dinnerIngredientsList.indexOf(ingredient);
//            dinnerIngredientsAmount.add(index, dinnerIngredientsAmount.get(index) + amount);
//        }
//    }
//
//    public void addIfNeeded(String selectedIngredient, int mealIndex){
//        Ingredient ingredient = Ingredient.getIngredientByName(selectedIngredient);
//        if(mealIndex == 0){
//            if(!breakfastIngredientsList.contains(ingredient)){
//                breakfastIngredientsList.add(ingredient);
//                breakfastIngredientsAmount.add(1);
//            }
//            else{
//                int index = breakfastIngredientsList.indexOf(ingredient);
//                breakfastIngredientsAmount.add(index, breakfastIngredientsAmount.get(index) + 1);
//            }
//        }
//
//        if(mealIndex == 1){
//            if(!lunchIngredientsList.contains(ingredient)){
//                lunchIngredientsList.add(ingredient);
//                lunchIngredientsAmount.add(1);
//            }
//            else{
//                int index = lunchIngredientsList.indexOf(ingredient);
//                lunchIngredientsAmount.add(index, lunchIngredientsAmount.get(index) + 1);
//            }
//        }
//
//        if(mealIndex == 2){
//            if(!dinnerIngredientsList.contains(ingredient)){
//                dinnerIngredientsList.add(ingredient);
//                dinnerIngredientsAmount.add(1);
//            }
//            else{
//                int index = dinnerIngredientsList.indexOf(ingredient);
//                dinnerIngredientsAmount.add(index, dinnerIngredientsAmount.get(index) + 1);
//            }
//        }
//    }
//
//    public void removeIfNeeded(String ingredient, int amountToRemove, int mealIndex){
//        if(mealIndex == 0){
//            int index = breakfastIngredientsList.indexOf(ingredient);
//            if(breakfastIngredientsAmount.get(index) == amountToRemove){
//                breakfastIngredientsList.remove(index);
//                breakfastIngredientsAmount.remove(index);
//            }
//            else{
//                breakfastIngredientsAmount.add(index, breakfastIngredientsAmount.get(index) - amountToRemove);
//            }
//        }
//
//        if(mealIndex == 1){
//            int index = lunchIngredientsList.indexOf(ingredient);
//            if(lunchIngredientsAmount.get(index) == amountToRemove){
//                lunchIngredientsList.remove(index);
//                lunchIngredientsAmount.remove(index);
//            }
//            else{
//                lunchIngredientsAmount.add(index, lunchIngredientsAmount.get(index) - amountToRemove);
//            }
//        }
//
//        if(mealIndex == 2){
//            int index = dinnerIngredientsList.indexOf(ingredient);
//            if(dinnerIngredientsAmount.get(index) == amountToRemove){
//                dinnerIngredientsList.remove(index);
//                dinnerIngredientsAmount.remove(index);
//            }
//            else{
//                dinnerIngredientsAmount.add(index, dinnerIngredientsAmount.get(index) - amountToRemove);
//            }
//        }
//    }


        // From Meal:

//    public void addIfMiniMealInside(String mealPart){
//        if (mealPart.equals("toast")) {
//            addIfNeeded(Ingredient.getIngredientByName("bread"));
//            addIfNeeded(Ingredient.getIngredientByName("yellow cheese"));
//            addIfNeeded(Ingredient.getIngredientByName("ketchup"));
//            addIfNeeded(Ingredient.getIngredientByName("thousand island dressing"));
//        }
//        if (mealPart.equals("salad")) {
//            addIfNeeded(Ingredient.getIngredientByName("tomato"));
//            addIfNeeded(Ingredient.getIngredientByName("cucumber"));
//            addIfNeeded(Ingredient.getIngredientByName("lettuce"));
//        }
//    }


        // From app-wild:

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int itemID = item.getItemId();
//        if(itemID == R.id.musicController){
//            if(mediaPlayer.isPlaying()){
//                me.putExtra("playMusic", false);
//                item.setIcon(R.drawable.ic_music_off_icon);
//                mediaPlayer.pause();
//            }
//            else{
//                me.putExtra("playMusic", true);
//                item.setIcon(R.drawable.ic_music_on_icon);
//                initiateMediaPlayer();
//                mediaPlayer.start();
//            }
//        }
//
//        if(itemID == R.id.sendToSettings){
//            me.setClass(MainActivity.this, settingsSetter.class);
//            me.putExtra("cameToSettingsFrom", getLocalClassName());
//            startActivity(me);
//        }
//        return super.onOptionsItemSelected(item);
//    }


        // From: ingredientsSelection:

    //    public void initiateListViewFields(){
//        ingredientsListViewInfo = new ArrayList<String>();
//
//        for(int i = 0; i < ingredients.size(); i++){
//            Ingredient ingredient = ingredients.get(i);
//            ingredientsListViewInfo.add(ingredient.getName());  //  + ": " + ingredient.getProteins() * 100.0 + " proteins, " + ingredient.getFats() * 100.0 + " fats and " + ingredient.getCalories() + " calories."
//        }
//    }

        // From: customMeals:


//    public void writeTheInstructions(){
//        tvInstructions.setText(
//                "Make sure to use words like: 'with', 'and' and 'include' instead of using ','." + "\n" +
//                "You need to use the special word 'flavored' to make something flavored, like this: " + "\n" +
//                "chocolate flavored ice cream or chocolate flavored yogurt and so on..." + "\n" +
//                "You can also use 'mini-meals' like: salad, toast and cereals in your text." + "\n" +
//                "Not every ingredient have photo so it will just show you the name."
//        );
//    }

//    public void setCustomFood(){
//        String meal = customMeal.getText().toString();
//        String testMeal;
//        if(meal.contains(":") && meal.contains(",") && meal.contains(".") && meal.contains(" ")){
//            testMeal = meal.replaceAll(":", "");
//            if(meal.length() != testMeal.length() + 1){
//                Toast.makeText(this, "There should be one ':' .", Toast.LENGTH_SHORT).show();
//            }
//            else{
//                testMeal = meal.replaceAll(",", "");
//                if(meal.length() != testMeal.length() + 1){
//                    Toast.makeText(this, "There should be one ',' .", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    testMeal = meal.replaceAll("\\.", "");
//                    if(meal.length() != testMeal.length() + 1){
//                        Toast.makeText(this, "There should be one '.' .", Toast.LENGTH_SHORT).show();
//                    }
//                    else{
//                        if(meal.contains("_") || meal.contains("-")){
//                            Toast.makeText(this, "Make sure to use only the format symbols.", Toast.LENGTH_SHORT).show();
//                        }
//                        else{
//                            viewInfo();
//                        }
//                    }
//                }
//            }
//        }
//        else{
//            Toast.makeText(this, "Make sure to follow the format instructions.", Toast.LENGTH_SHORT).show();
//        }
//    }


        // From: Video:

//    package com.example.mydieter9005;
//
//import java.util.ArrayList;
//
//    public class Video {
//        private static ArrayList<com.example.mydieter9005.Video> videos = new ArrayList<com.example.mydieter9005.Video>();
//        private final String videoName;
//        private String videoPath;
//
//        public Video(String videoName){
//            this.videoName = videoName;
//            this.videoPath = "";
//            videos.add(this);
//        }
//
//        public Video(String videoName, String videoPath){
//            this.videoName = videoName;
//            this.videoPath = videoPath;
//            videos.add(this);
//        }
//
//        public static ArrayList<com.example.mydieter9005.Video> getVideos() {
//            return videos;
//        }
//
//        public String getVideoName() {
//            return videoName;
//        }
//
//        public String getVideoPath() {
//            return videoPath;
//        }
//
//        public void setVideoPath(String videoPath) {
//            this.videoPath = videoPath;
//        }
//
//        public static com.example.mydieter9005.Video getVideoByName(String name){
//            name = name.replaceAll(" ", "_");
//            for(int i = 0; i < videos.size(); i++){
//                if(videos.get(i).videoName.equals(name))
//                    return videos.get(i);
//            }
//            return null;
//        }
//
//        public static void initiateVideos(String fileData){
//            videos.clear();
//            String[] savedVideos = fileData.split("\n");
//            for(int i = 1; i < savedVideos.length; i++){
//                String videoName = savedVideos[i].split(" path: ")[0];
//                String videoPath = savedVideos[i].split(" path: ")[1];
//
//                new com.example.mydieter9005.Video(videoName, videoPath);
//            }
//        }
//
//        public static void firstInitiateVideos(){
//            new com.example.mydieter9005.Video("main_activity_morning_background_video");
//            new com.example.mydieter9005.Video("main_activity_noon_background_video");
//            new com.example.mydieter9005.Video("main_activity_night_background_video");
//            new com.example.mydieter9005.Video("breakfast_selection_background_video");
//            new com.example.mydieter9005.Video("lunch_selection_background_video");
//            new com.example.mydieter9005.Video("dinner_selection_background_video");
//            new com.example.mydieter9005.Video("ingredients_selection_background_video");
//            new com.example.mydieter9005.Video("meal_modifier_background_video");
//            new com.example.mydieter9005.Video("custom_background_video");
//            new com.example.mydieter9005.Video("custom_selection_background_video");
//            new com.example.mydieter9005.Video("music_master_video_background");
//            new com.example.mydieter9005.Video("settings_setter_video_background");
//        }
//
//        @Override
//        public String toString() {
//            return videoName + " path: " + videoPath;
//        }
//    }


        // From MainActivity:

//    ActivityMainBinding binding;
//    StorageReference storageReference;
//    ProgressDialog progressDialog;

    // onClick:
//    binding = ActivityMainBinding.inflate(getLayoutInflater());
//    setContentView(binding.getRoot());

    // createTheFirstIntent:
//    Video.initiateVideos(getFileData("videos"));

    // idk:
//    saveVideosPaths(Video.getVideos());

//    public void saveVideosPaths(ArrayList<Video> videos){
//        try {
//            fos = openFileOutput("videos", Context.MODE_PRIVATE);
//            osw = new OutputStreamWriter(fos);
//            bw = new BufferedWriter(osw);
//
//            bw.write("Background videos from firebase cloud." + "\n");
//
//            bw.write(videos.get(0).toString());
//            for(int i = 1; i < videos.size(); i++)
//                bw.write("\n" + videos.get(i).toString());
//
//            bw.close();
//
//            Toast.makeText(this,  "All videos saved.", Toast.LENGTH_SHORT).show();
//        }
//        catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void firstSetAllVideosPathsFromFirebase() {
//        Video.firstInitiateVideos();
//        ArrayList<Video> videos = Video.getVideos();
//
//        getAndSetBackgroundVideoPathFromFirebase(videos.get(1));
////        for(int i = 0; i < videos.size(); i++) {
////        }
//
//    }
//
//    public void getAndSetBackgroundVideoPathFromFirebase(Video video){
//        progressDialog = new ProgressDialog(MainActivity.this);
//        progressDialog.setMessage("Fetching videos...");
//        progressDialog.setCancelable(false);
//        progressDialog.show();
//
//        StorageReference storageReference = FirebaseStorage.getInstance().getReference("background_videos/" + video.getVideoName() + ".mp4");
//
//        try {
//            File localFile = File.createTempFile(video.getVideoName() + " temp_file", ".mp4");
//            storageReference.getFile(localFile)
//                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//
//
//                            if(progressDialog.isShowing())
//                                progressDialog.dismiss();
//
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(MainActivity.this, "Failed to retrieve.", Toast.LENGTH_SHORT).show();
//
//                            if(progressDialog.isShowing())
//                                progressDialog.dismiss();
//                        }
//                    });
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    // getFileData:
//                if(fileName.equals("videos")) {
//        firstSetAllVideosPathsFromFirebase();
//        //  Video.initiateVideos(getFileData("videos"));
//    }


        // From customMeals:


//    private void choosePicture(){
//        Intent intent = new Intent();
//        intent.setType("image/");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent, 1);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
//            Uri imageUri = data.getData();
//        }
//    }
//
//    private void uploadPicture(Uri imageUri){
//
//        final String randomKey = UUID.randomUUID().toString();
//        StorageReference mountainsRef = storageReference.child("mountains.jpg");
//
//        storageReference.putFile(imageUri)
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        Snackbar.make(findViewById(android.R.id.content), "Image uploaded.", Snackbar.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(customMeals.this, "Failed to upload.", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }


        // From customSelection:
    //    public void showFileData(){
//        customMealsList = new ArrayList<String>();
//        String[] dataParts = getFileData(fileName).split("\n"), organizedMeal;
//
//        for(String dataPart : dataParts){
//            organizedMeal = multiUsageFunctions.organizeMeal(dataPart);
//
//            customMealsList.add(multiUsageFunctions.separateInfo(organizedMeal[0]));
//            customMealsList.add(multiUsageFunctions.separateInfo(organizedMeal[1]));
//            customMealsList.add(multiUsageFunctions.separateInfo(organizedMeal[2]));
//        }
//    }

    //      From: Login

    //    public void notEvenOneUserAdded(){
//        AlertDialog ad;
//        AlertDialog.Builder adb;
//        adb = new AlertDialog.Builder(this);
//        adb.setTitle("Users not found!");
//        adb.setMessage("It's seems like no one sing up for now, you can be the first!.");
//        adb.setIcon(R.drawable.ic_users_not_found_icon);
//        adb.setCancelable(false);
//
//        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                me.setClass(Login.this, Register.class);
//                startActivity(me);
//            }
//        });
//
//        ad = adb.create();
//        ad.show();
//    }

            // From: breakfastSelection, lunchSelection, dinnerSelection, customSelection and WorldSavedCustomMeals
//    public void updateIfMealModified(){
//        if(me.hasExtra("modifiedMeal")){
//            Meal modifiedMeal = (Meal) me.getSerializableExtra("modifiedMeal");
//            if(getMealIndexInMealsList(modifiedMeal) != -1)  // Check if exist inside mealsList.
//                mealsList.set(getMealIndexInMealsList(modifiedMeal), modifiedMeal);
//        }
//    }
//
//    public int getMealIndexInMealsList(Meal meal){
//        for(int i = 0; i < mealsList.size(); i++){
//            if(mealsList.get(i).getName().equals(meal.getName()))
//                return i;
//        }
//        return -1;
//    }
//
//    public void multiOrSingleSelectUpdate(){
//        if(!multiSelect){
//            Toast.makeText(this, "Multi select has enabled.", Toast.LENGTH_SHORT).show();
//            btMultiBreakfastSelect.setText("Disable multi select");
//            btClearBreakfastSelection.setText("Finish choosing");
//            multiSelectCounter = 0;
//            multiSelect = true;
//        }
//        else{
//            Toast.makeText(this, "Multi select has disabled.", Toast.LENGTH_SHORT).show();
//            btMultiBreakfastSelect.setText("Enable multi select");
//            btClearBreakfastSelection.setText("Clear selection");
//            chosenMultiSelectBreakfast = null;
//            multiSelectCounter = 0;
//            multiSelect = false;
//        }
//    }
//

//    Meal chosenMultiSelectBreakfast = null;
//    boolean multiSelect = false;
//    int multiSelectCounter = 0;

//    public void clearBreakfastSelectionOrFinishMultiSelect(){
//        if(multiSelect){
//            if(multiSelectCounter == 0){
//                Toast.makeText(this, "You didn't choose anything yet.", Toast.LENGTH_SHORT).show();
//            }
//            else{
//                me.setClass(breakfastSelection.this, mealsMenu.class);
//                me.putExtra("breakfast", chosenMultiSelectBreakfast);
//                startActivity(me);
//            }
//        }
//        else{
//            if(me.hasExtra("breakfast")){
//                me.removeExtra("breakfast");
//                me.setClass(breakfastSelection.this, mealsMenu.class);
//                startActivity(me);
//            }
//            else{
//                Toast.makeText(this, "You didn't choose anything yet.", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

            // From: mealMenu
//            public void updateMeals(){
//                btBreakfast.setText("Select");
//                btLunch.setText("Select");
//                btDinner.setText("Select");
//
//                if(me.hasExtra("breakfast")){
//                    breakfast = (Meal) me.getSerializableExtra("breakfast");
//                    tvBreakfast.setText("Your breakfast is: " + breakfast.getName() + ".");
//                    btBreakfast.setText("Reselect");
//                    totalProteins += breakfast.getProteins();
//                    totalFats += breakfast.getFats();
//                    totalCalories += breakfast.getCalories();
//                    selectedMeals[0] = breakfast;
//                }
//
//                if(me.hasExtra("lunch")){
//                    lunch = (Meal) me.getSerializableExtra("lunch");
//                    tvLunch.setText("Your lunch is: " + lunch.getName() + ".");
//                    btLunch.setText("Reselect");
//                    totalProteins += lunch.getProteins();
//                    totalFats += lunch.getFats();
//                    totalCalories += lunch.getCalories();
//                    selectedMeals[1] = lunch;
//                }
//
//                if(me.hasExtra("dinner")){
//                    dinner = (Meal) me.getSerializableExtra("dinner");
//                    tvDinner.setText("Your dinner is: " + dinner.getName() + ".");
//                    btDinner.setText("Reselect");
//                    totalProteins += dinner.getProteins();
//                    totalFats += dinner.getFats();
//                    totalCalories += dinner.getCalories();
//                    selectedMeals[2] = dinner;
//                }
//
//                tvTotalProteins.setText("Total proteins: " + Math.round(totalProteins * 1000.0) / 1000.0 + " .");
//                tvTotalFats.setText("Total fats: " + Math.round(totalFats * 1000.0) / 1000.0 + " .");
//                tvTotalCalories.setText("Total calories: " + Math.round(totalCalories * 1000.0) / 1000.0 + " .");
//            }

//            if(viewId == btFinish.getId()) {
//        if(me.hasExtra("breakfast") || me.hasExtra("lunch") || me.hasExtra("dinner")){
//            me.setClass(mealsMenu.this, finishMeals.class);
//
//            if(selectedMeals[0] != null)
//                me.putExtra("selectedBreakfast", selectedMeals[0]);
//            if(selectedMeals[1] != null)
//                me.putExtra("selectedLunch", selectedMeals[1]);
//            if(selectedMeals[2] != null)
//                me.putExtra("selectedDinner", selectedMeals[2]);
//
//            me.putExtra("totalProteins", totalProteins);
//            me.putExtra("totalFats", totalFats);
//            me.putExtra("totalCalories", totalCalories);
//            startActivity(me);
//        }
//        else{
//            Toast.makeText(this, "Please pick at least one meal !", Toast.LENGTH_LONG).show();
//        }
//    }

            // From: finishMeals
//            public void setAdapters(){
//                initiateIngredientListsAndSetMealsNames();
//                initiateListViewsFields();
//
//                if(selectedMeals[0] != null){  // Breakfast.
//                    breakfastIngredientsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, breakfastFields);
//                    lvBreakfastIngredients.setAdapter(breakfastIngredientsAdapter);
//                    lvBreakfastIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            Ingredient selectedItem = breakfastIngredientsList.get(position);
//
//                            showIngredientImage(selectedItem);
//                        }
//                    });
//                }
//
//                if(selectedMeals[1] != null){  // Lunch.
//                    lunchIngredientsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lunchFields);
//                    lvLunchIngredients.setAdapter(lunchIngredientsAdapter);
//                    lvLunchIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            Ingredient selectedItem = lunchIngredientsList.get(position);
//
//                            showIngredientImage(selectedItem);
//                        }
//                    });
//                }
//
//                if(selectedMeals[2] != null){  // Dinner.
//                    dinnerIngredientsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dinnerFields);
//                    lvDinnerIngredients.setAdapter(dinnerIngredientsAdapter);
//                    lvDinnerIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            Ingredient selectedItem = dinnerIngredientsList.get(position);
//
//                            showIngredientImage(selectedItem);
//                        }
//                    });
//                }
//            }

//    public void initiateIngredientListsAndSetMealsNames(){
//        breakfastIngredientsList = new ArrayList<Ingredient>();
//        if(selectedMeals[0] != null) {
//            tvBreakfastInfo.setText("Breakfast: " + selectedMeals[0].getName());
//            for (int i = 0; i < selectedMeals[0].getNeededIngredientsForMeal().size(); i++)
//                breakfastIngredientsList.add(new Ingredient(selectedMeals[0].getNeededIngredientsForMeal().get(i)));
//        }
//
//        lunchIngredientsList = new ArrayList<Ingredient>();
//        if(selectedMeals[1] != null){
//            tvLunchInfo.setText("Lunch: " + selectedMeals[1].getName());
//            for(int i = 0; i < selectedMeals[1].getNeededIngredientsForMeal().size(); i++)
//                lunchIngredientsList.add(new Ingredient(selectedMeals[1].getNeededIngredientsForMeal().get(i)));
//        }
//
//        dinnerIngredientsList = new ArrayList<Ingredient>();
//        if(selectedMeals[2] != null){
//            tvDinnerInfo.setText("Dinner: " + selectedMeals[2].getName());
//            for(int i = 0; i < selectedMeals[2].getNeededIngredientsForMeal().size(); i++)
//                dinnerIngredientsList.add(new Ingredient(selectedMeals[2].getNeededIngredientsForMeal().get(i)));
//        }
//    }

            // From: ingredientsPickup
//            public void initiateIngredientsToShow(){
//                ingredientsToShow = new ArrayList<Ingredient>();
//                for(Meal meal : selectedMeals) {
//                    if(meal != null){
//                        for(int i = 0; i < meal.getNeededIngredientsForMeal().size(); i++) {
//                            Ingredient ingredient = new Ingredient(meal.getNeededIngredientsForMeal().get(i));
//
//                            if(getIngredientIndexInArrayList(ingredient, ingredientsToShow) == -1)
//                                ingredientsToShow.add(new Ingredient(ingredient));
//                            else
//                                ingredientsToShow.get(getIngredientIndexInArrayList(ingredient, ingredientsToShow)).addGrams(ingredient.getGrams());
//                        }
//                    }
//                }
//                ingredientsAmount = ingredientsToShow.size();
//
//                tvCounterShow.setText("Item: " + ingredientsCounter + " out of " + ingredientsAmount);
//                if(ingredientsAmount == 0)
//                    ibtNext.setVisibility(View.INVISIBLE);
//            }


            // Activity: ProfilePictureSelection:
//    package com.example.mydieter9005;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.database.sqlite.SQLiteDatabase;
//import android.media.MediaPlayer;
//import android.net.ConnectivityManager;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//import android.widget.VideoView;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import java.io.BufferedReader;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStreamReader;

//    public class ProfilePictureSelection extends AppCompatActivity implements View.OnClickListener {
//
//        private NetworkConnectionReceiver networkConnectionReceiver;
//
//        private MediaPlayer mediaPlayer;
//        private VideoView videoView;
//
//        Button btChoseProfilePicture, btCancelProfilePictureSelection;
//        ImageButton ibtPreviousPicture, ibtNextPicture;
//        ImageView ivProfilePictureSelector;
//        TextView tvPictureNumberOutOf;
//        LinearLayout linearLayout;
//
//        Song activeSong = Song.getSongs().get(0);
//        int currentIndex = 0, maxAmount = 10;
//
//        FirebaseDatabase usersDb;
//        DatabaseReference databaseReference;
//
//        SQLiteDatabase sqdb;
//        DBHelper my_db;
//
//        FileInputStream is;
//        InputStreamReader isr;
//        BufferedReader br;
//        Intent me;
//
//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_profile_picture_selection);
//
//            me = getIntent();
//            if(me.hasExtra("activeSong"))
//                activeSong = (Song) me.getSerializableExtra("activeSong");
//
//            my_db = new DBHelper(com.example.mydieter9005.ProfilePictureSelection.this);
//
//            linearLayout = (LinearLayout) findViewById(R.id.profilePictureSelectionLinearLayout);
//            videoView = (VideoView) findViewById(R.id.profilePictureSelectionVideoView);
//
//            ivProfilePictureSelector = (ImageView) findViewById(R.id.ivProfilePictureSelector);
//            tvPictureNumberOutOf = (TextView) findViewById(R.id.tvPictureNumberOutOf);
//
//            btCancelProfilePictureSelection = (Button) findViewById(R.id.btCancelProfilePictureSelection);
//            btCancelProfilePictureSelection.setOnClickListener(this);
//            btChoseProfilePicture = (Button) findViewById(R.id.btChoseProfilePicture);
//            btChoseProfilePicture.setOnClickListener(this);
//
//            ibtPreviousPicture = (ImageButton) findViewById(R.id.ibtPreviousPicture);
//            ibtPreviousPicture.setOnClickListener(this);
//            ibtNextPicture = (ImageButton) findViewById(R.id.ibtNextPicture);
//            ibtNextPicture.setOnClickListener(this);
//
//            setCustomNetworkConnectionReceiver();
//            nextPicture();
//            implementSettingsData();
//            initiateVideoPlayer();
//            initiateMediaPlayer();
//        }
//
//        public void setImageBasedOnIndex(){
//            ivProfilePictureSelector.setImageResource(getResources().getIdentifier("user_picture_" + (currentIndex), "drawable", getPackageName()));
//        }
//
//        public void previousPicture(){
//            if(0 < currentIndex && currentIndex <= maxAmount){
//                currentIndex--;
//                tvPictureNumberOutOf.setText("  Picture number " + currentIndex + " out of " + maxAmount + "  ");
//                setImageBasedOnIndex();
//
//                ibtNextPicture.setVisibility(View.VISIBLE);
//                if(currentIndex == 1)
//                    ibtPreviousPicture.setVisibility(View.INVISIBLE);
//            }
//        }
//
//        public void nextPicture(){
//            if(0 <= currentIndex && currentIndex < maxAmount){
//                currentIndex++;
//                tvPictureNumberOutOf.setText("   Picture number " + currentIndex + " out of " + maxAmount + "   ");
//                setImageBasedOnIndex();
//
//                if(1 < currentIndex)
//                    ibtPreviousPicture.setVisibility(View.VISIBLE);
//                if(currentIndex == maxAmount)
//                    ibtNextPicture.setVisibility(View.INVISIBLE);
//            }
//        }
//
//        public void profilePictureSelected(){
//            User.getCurrentUser().setProfilePictureId(getResources().getIdentifier("user_picture_" + (currentIndex), "drawable", getPackageName()));
//            updateUserProfilePictureIdInFirebaseAndDatabase(User.getCurrentUser());
//        }
//
//        public void updateUserProfilePictureIdInFirebaseAndDatabase(User user){
//            usersDb = FirebaseDatabase.getInstance();
//            databaseReference = usersDb.getReference("users");
//            databaseReference.child(user.getUsername()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    sqdb=my_db.getWritableDatabase();
//                    ContentValues cv = new ContentValues();
//                    cv.put(DBHelper.PROFILE_PICTURE_ID, user.getProfilePictureId());
//
//                    sqdb.update(DBHelper.TABLE_NAME, cv,DBHelper.USERNAME+"=?", new String[]{User.getCurrentUser().getUsername()});
//                    sqdb.close();
//
//                    Toast.makeText(com.example.mydieter9005.ProfilePictureSelection.this, "User profile picture successfully changed.", Toast.LENGTH_SHORT).show();
//
//                    me.setClass(com.example.mydieter9005.ProfilePictureSelection.this, UserInfoScreen.class);
//                    startActivity(me);
//                }
//            });
//        }
//
//        public void setCustomNetworkConnectionReceiver(){
//            networkConnectionReceiver = new NetworkConnectionReceiver() {
//                @Override
//                public void onReceive(Context context, Intent intent) {
//                    try{
//                        if(!isOnline(context))
//                            noInternetAccess(context);
//                        else
//                            btChoseProfilePicture.setVisibility(View.VISIBLE);
//                    }
//                    catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void noInternetAccess(Context context){
//                    AlertDialog ad;
//                    AlertDialog.Builder adb;
//                    adb = new AlertDialog.Builder(context);
//                    adb.setTitle("Internet connection not found!");
//                    adb.setMessage("Connect to the internet and try again.");
//                    adb.setIcon(R.drawable.ic_network_not_found);
//                    adb.setCancelable(false);
//
//                    adb.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            if(!isOnline(context))
//                                noInternetAccess(context);
//                            else
//                                Toast.makeText(context, "Network connection available.", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//                    adb.setNeutralButton("Just look", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            btChoseProfilePicture.setVisibility(View.INVISIBLE);
//                        }
//                    });
//
//                    adb.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            finish();
//                        }
//                    });
//
//                    ad = adb.create();
//                    ad.show();
//                }
//            };
//        }
//
//        public String getFileData(String fileName){
//            String currentLine = "", allData = "";
//            try{
//                is = openFileInput(fileName);
//                isr = new InputStreamReader(is);
//                br = new BufferedReader(isr);
//
//                currentLine = br.readLine();
//                while(currentLine != null){
//                    allData += currentLine + "\n";
//                    currentLine = br.readLine();
//                }
//                br.close();
//            }
//            catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            catch (IOException e) {
//                e.printStackTrace();
//            }
//            return allData;
//        }
//
//        public void implementSettingsData(){
//            if(getFileData("settings") != null){
//                String[] settingsParts = getFileData("settings").split("\n");
//                Boolean playMusic, useVideos, useManuallySave;
//
//                playMusic = Boolean.parseBoolean(settingsParts[0].split(": ")[1]);
//                useVideos = Boolean.parseBoolean(settingsParts[1].split(": ")[1]);
//                useManuallySave = Boolean.parseBoolean(settingsParts[2].split(": ")[1]);
//                activeSong = Song.getSongByName(settingsParts[3].split(": ")[1]);
//
//                me.putExtra("playMusic", playMusic);
//                me.putExtra("useVideos", useVideos);
//                me.putExtra("useManuallySave", useManuallySave);
//                me.putExtra("activeSong", activeSong);
//            }
//        }
//
//        public void initiateVideoPlayer(){
//            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.world_saved_custom_meals_background_video);
//            videoView.setVideoURI(uri);
//
//            if(me.getBooleanExtra("useVideos", true))
//                videoView.start();
//
//            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mp) {
//                    mp.setLooping(true);
//                }
//            });
//        }
//
//        public void initiateMediaPlayer(){
//            mediaPlayer = MediaPlayer.create(com.example.mydieter9005.ProfilePictureSelection.this, activeSong.getId());
//            mediaPlayer.setLooping(true);
//            if(me.getBooleanExtra("playMusic", true)){
//                mediaPlayer.start();
//            }
//        }
//
//        @Override
//        public boolean onCreateOptionsMenu(@NonNull Menu menu) {
//            MenuInflater inflater = getMenuInflater();
//            inflater.inflate(R.menu.main_menu, menu);
//            return true;
//        }
//
//        @Override
//        public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//            int itemID = item.getItemId();
//            if(itemID == R.id.sendToMusicMaster){
//                me.setClass(com.example.mydieter9005.ProfilePictureSelection.this, musicMaster.class);
//                me.putExtra("cameToMusicMasterFrom", getLocalClassName());
//                startActivity(me);
//            }
//
//            if(itemID == R.id.sendToSettings){
//                me.setClass(com.example.mydieter9005.ProfilePictureSelection.this, settingsSetter.class);
//                me.putExtra("cameToSettingsFrom", getLocalClassName());
//                startActivity(me);
//            }
//
//            if(itemID == R.id.sendToUserScreen){
//                me.setClass(com.example.mydieter9005.ProfilePictureSelection.this, UserInfoScreen.class);
//                me.putExtra("cameToUserScreenFrom", getLocalClassName());
//                startActivity(me);
//            }
//            return super.onOptionsItemSelected(item);
//        }
//
//        @Override
//        protected void onPostResume() {
//            super.onPostResume();
//            videoView.resume();
//            if(!me.getBooleanExtra("useVideos", true)){
//                linearLayout.setBackground(getDrawable(R.drawable.world_saved_custom_meals_background));
//                videoView.stopPlayback();
//            }
//            else
//                videoView.start();
//        }
//
//        @Override
//        protected void onRestart() {
//            videoView.start();
//            super.onRestart();
//        }
//
//        @Override
//        protected void onResume() {
//            super.onResume();
//
//            IntentFilter networkConnectionFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
//                registerReceiver(networkConnectionReceiver, networkConnectionFilter);
//            }
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//                registerReceiver(networkConnectionReceiver, networkConnectionFilter);
//            }
//
//            mediaPlayer.start();
//            if(!me.getBooleanExtra("playMusic", true)){
//                mediaPlayer.stop();
//            }
//        }
//
//        @Override
//        protected void onPause() {
//
//            try{
//                unregisterReceiver(networkConnectionReceiver);
//            }
//            catch (IllegalArgumentException e){
//                e.getStackTrace();
//            }
//
//            videoView.suspend();
//            mediaPlayer.pause();
//            super.onPause();
//        }
//
//        @Override
//        protected void onDestroy() {
//            videoView.stopPlayback();
//            mediaPlayer.stop();
//            mediaPlayer.release();
//            super.onDestroy();
//        }
//
//        @Override
//        public void onClick(View v) {
//            int viewId = v.getId();
//
//            if(viewId == ibtNextPicture.getId())
//                nextPicture();
//
//            if(viewId == ibtPreviousPicture.getId())
//                previousPicture();
//
//            if(viewId == btChoseProfilePicture.getId())
//                profilePictureSelected();
//
//            if(viewId == btCancelProfilePictureSelection.getId())
//                finish();
//        }
//    }

            // His XML file:
//    <?xml version="1.0" encoding="utf-8"?>
//<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
//    xmlns:app="http://schemas.android.com/apk/res-auto"
//    xmlns:tools="http://schemas.android.com/tools"
//    android:layout_width="match_parent"
//    android:layout_height="match_parent"
//    android:orientation="vertical"
//    tools:context=".ProfilePictureSelection">
//
//    <VideoView
//    android:id="@+id/profilePictureSelectionVideoView"
//    android:layout_width="match_parent"
//    android:layout_height="match_parent"
//    android:layout_alignParentLeft="true"
//    android:layout_alignParentTop="true"
//    android:layout_alignParentRight="true"
//    android:layout_alignParentBottom="true" />
//
//    <LinearLayout
//    android:id="@+id/profilePictureSelectionLinearLayout"
//    android:layout_width="match_parent"
//    android:layout_height="match_parent"
//    android:orientation="vertical"
//    android:gravity="center">
//
//        <TextView
//    android:layout_width="150pt"
//    android:layout_height="wrap_content"
//    android:textSize="10pt"
//    android:text="You can choose a profile picture from the selection below: " />
//
//        <ImageView
//    android:id="@+id/ivProfilePictureSelector"
//    android:layout_width="300dp"
//    android:layout_height="300dp"
//    android:layout_marginTop="20pt"
//    android:layout_marginBottom="20pt"/>
//
//        <androidx.gridlayout.widget.GridLayout
//    android:layout_width="160pt"
//    android:layout_height="wrap_content"
//    app:orientation="horizontal"
//    android:layoutDirection="ltr">
//
//            <ImageButton
//    android:id="@+id/ibtPreviousPicture"
//    android:layout_width="25pt"
//    android:layout_height="wrap_content"
//    app:srcCompat="@drawable/ic_previous_arrow_icon"
//    android:visibility="invisible"
//    android:layoutDirection="ltr"/>
//
//            <TextView
//    android:id="@+id/tvPictureNumberOutOf"
//    android:layout_width="wrap_content"
//    android:layout_height="match_parent"
//    android:textSize="8pt"
//    android:gravity="center|bottom" />
//
//            <ImageButton
//    android:id="@+id/ibtNextPicture"
//    android:layout_width="25pt"
//    android:layout_height="wrap_content"
//    app:srcCompat="@drawable/ic_next_arrow_icon"
//    android:layoutDirection="ltr"/>
//
//        </androidx.gridlayout.widget.GridLayout>
//
//        <Button
//    android:id="@+id/btChoseProfilePicture"
//    android:layout_width="125pt"
//    android:layout_height="wrap_content"
//    android:layout_marginTop="15pt"
//    android:text="Choose this one" />
//
//        <Button
//    android:id="@+id/btCancelProfilePictureSelection"
//    android:layout_width="125pt"
//    android:layout_height="wrap_content"
//    android:text="Cancel" />
//    </LinearLayout>
//
//</RelativeLayout>

            // From: multiUsageFunctions:
//    package com.example.mydieter9005;
//
//    public class multiUsageFunctions {
//
//        public static String[] organizeMeal(String meal){  // Get a full meal name and separate it into: name, calories and minutes.
//            String mealName = "", mealCalories = "", mealTime = "";
//            int start = 0;
//            for(int i = 0; i < meal.length(); i++){
//                if(meal.charAt(i) != ':'){
//                    mealName += meal.charAt(i);
//                }
//                else {
//                    start = i + 2;
//                    break;
//                }
//            }
//            for(int i = start; i < meal.length(); i++){
//                if(meal.charAt(i) != ','){
//                    mealCalories += meal.charAt(i);
//                }
//                else {
//                    start = i + 2;
//                    break;
//                }
//            }
//            for(int i = start; i < meal.length(); i++){
//                mealTime += meal.charAt(i);
//            }
//            return new String[] {mealName, mealCalories, mealTime};
//        }
//
//        public static int getCaloriesOrMinutesOutOfString(String caloriesOrMinutes){  // Get a string who contain one number somewhere and return the number.
//            char currentChar;
//            String amount = "";
//            for(int i = 0; i < caloriesOrMinutes.length(); i++){
//                currentChar = caloriesOrMinutes.charAt(i);
//                if(Character.isDigit(currentChar)) {
//                    amount += currentChar;
//                }
//            }
//            return Integer.parseInt(amount);
//        }
//
//        public static String separateInfo(String listPart){
//            String info = "";
//            for(int i = 0; i < listPart.length(); i++){
//                if(!Character.toString(listPart.charAt(i)).equals(",") && !Character.toString(listPart.charAt(i)).equals(" ") && !Character.toString(listPart.charAt(i)).equals(":")){
//                    info += Character.toString(listPart.charAt(i));
//                }
//                else {
//                    return info;
//                }
//            }
//            return info;
//        }
//    }


            // BreakfastSelection, LunchSelection and DinnerSelection:
    //package com.example.mydieter9005;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.media.MediaPlayer;
//import android.net.Uri;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.VideoView;
//
//import java.io.BufferedReader;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//
//public class breakfastSelection extends AppCompatActivity implements View.OnClickListener {
//
//    private MediaPlayer mediaPlayer;
//    private VideoView videoView;
//
//    Button btSendBreakfastToCustomize, btClearBreakfastSelection, btBackFromBreakfastSelect;
//    EditText etFilterBreakfast;
//    ListView listView;
//
//    DailyMenu todayMenu = DailyMenu.getTodayMenu();
//    FileAndDatabaseHelper fileAndDatabaseHelper;
//    Song activeSong = Song.getSongs().get(0);
//    ArrayList<Meal> mealsList;
//    MealListAdapter adapter;
//
//    Intent me;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_breakfast_selection);
//
//        me = getIntent();
//        if(me.hasExtra("activeSong"))
//            activeSong = (Song) me.getSerializableExtra("activeSong");
//
//        mealsList = new ArrayList<Meal>();
//
//        ArrayList<Ingredient> ingredientsNeeded = new ArrayList<Ingredient>();  // For multi-ingredients meals.
//
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("nestle cereals"), 30));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("milk"), 200));
//        mealsList.add(new Meal("Nestle cereals with milk", ingredientsNeeded));
//        ingredientsNeeded.clear();
//
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("Chocolate flavored nestle cereals"), 30));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("milk"), 200));
//        mealsList.add(new Meal("Chocolate flavored nestle cereals", ingredientsNeeded));
//        ingredientsNeeded.clear();
//
//        mealsList.add(new Meal("Yogurt", 100));
//        mealsList.add(new Meal("Chocolate flavored yogurt", 100));
//        mealsList.add(new Meal("Chocolate flavored ice cream", 250));
//
//        listView = (ListView) findViewById(R.id.listViewBreakfast);
//        videoView = (VideoView) findViewById(R.id.breakfastVideoView);
//
//        btSendBreakfastToCustomize = (Button) findViewById(R.id.btSendBreakfastToCustomize);
//        btSendBreakfastToCustomize.setOnClickListener(this);
//        btClearBreakfastSelection = (Button) findViewById(R.id.btClearBreakfastSelection);
//        btClearBreakfastSelection.setOnClickListener(this);
//        btBackFromBreakfastSelect = (Button) findViewById(R.id.btBackFromBreakfastSelect);
//        btBackFromBreakfastSelect.setOnClickListener(this);
//
//        etFilterBreakfast = (EditText) findViewById(R.id.etFilterBreakfast);
//        etFilterBreakfast.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                (breakfastSelection.this).adapter.getFilter().filter(s);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {}
//        });
//
//        fileAndDatabaseHelper = new FileAndDatabaseHelper(this, me);
//        activeSong = fileAndDatabaseHelper.implementSettingsData();
//
//        setListViewAdapter();
//        initiateVideoPlayer();
//        initiateMediaPlayer();
//    }
//
//    public void setListViewAdapter(){
//        adapter = new MealListAdapter(this, mealsList);
//        listView.setAdapter(adapter);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Meal selectedItem = (Meal) adapterView.getItemAtPosition(i);
//
//                todayMenu.addBreakfast(selectedItem);
//
//                me.setClass(breakfastSelection.this, mealsMenu.class);
//                startActivity(me);
//            }
//        });
//
//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Meal selectedItem = (Meal) parent.getItemAtPosition(position);
//
//                showMealIngredientsInfo(selectedItem);
//                return true;
//            }
//        });
//    }
//
//    public void showMealInfo(Meal meal){
//        AlertDialog ad;
//        AlertDialog.Builder adb;
//        adb = new AlertDialog.Builder(this);
//        adb.setTitle("Your meal nutrition: ");
//        adb.setMessage(meal.getGrams() + " grams." + "\n" + meal.getProteins() + " proteins." + "\n" + meal.getFats() + " fats." + "\n" + meal.getCalories() + " calories.");
//        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//
//        adb.setNegativeButton("Ingredients", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                showMealIngredientsInfo(meal);
//            }
//        });
//
//        ad = adb.create();
//        ad.show();
//    }
//
//    public void showMealIngredientsInfo(Meal meal){
//        AlertDialog ad;
//        AlertDialog.Builder adb;
//        adb = new AlertDialog.Builder(this);
//        adb.setTitle("Your meal ingredients: ");
//        String mealInfo = "";
//        for(Ingredient ingredient : meal.getNeededIngredientsForMeal())
//            mealInfo += ingredient.getName() + ": " + ingredient.getGrams() + " grams." + "\n";
//        adb.setMessage(mealInfo);
//        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//
//        adb.setNegativeButton("Nutrition", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                showMealInfo(meal);
//            }
//        });
//
//        ad = adb.create();
//        ad.show();
//    }
//
//    public void sendToCustomize(){
//        me.setClass(breakfastSelection.this, customMeals.class);
//        me.putExtra("cameFrom", "breakfast");
//        startActivity(me);
//    }
//
//    public void backToMealsMenu(){
//        me.setClass(breakfastSelection.this, mealsMenu.class);
//        startActivity(me);
//    }
//
//    public void initiateVideoPlayer(){
//        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.breakfast_selection_background_video);
//        videoView.setVideoURI(uri);
//
//        if(me.getBooleanExtra("useVideos", true))
//            videoView.start();
//
//        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                mp.setLooping(true);
//            }
//        });
//    }
//
//    public void initiateMediaPlayer(){
//        mediaPlayer = MediaPlayer.create(breakfastSelection.this, activeSong.getId());
//        mediaPlayer.setLooping(true);
//        if(me.getBooleanExtra("playMusic", true)){
//            mediaPlayer.start();
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int itemID = item.getItemId();
//        if(itemID == R.id.sendToMusicMaster){
//            me.setClass(breakfastSelection.this, musicMaster.class);
//            me.putExtra("cameToMusicMasterFrom", getLocalClassName());
//            startActivity(me);
//        }
//
//        if(itemID == R.id.sendToSettings){
//            me.setClass(breakfastSelection.this, settingsSetter.class);
//            me.putExtra("cameToSettingsFrom", getLocalClassName());
//            startActivity(me);
//        }
//
//        if(itemID == R.id.sendToUserScreen){
//            me.setClass(breakfastSelection.this, UserInfoScreen.class);
//            me.putExtra("cameToUserScreenFrom", getLocalClassName());
//            startActivity(me);
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    protected void onPostResume() {
//        super.onPostResume();
//        videoView.resume();
//        if(!me.getBooleanExtra("useVideos", true)){
//            findViewById(R.id.breakfastSelectionLinearLayout).setBackground(getDrawable(R.drawable.breakfast_selection_background));
//            videoView.stopPlayback();
//        }
//        else
//            videoView.start();
//    }
//
//    @Override
//    protected void onRestart() {
//        videoView.start();
//        super.onRestart();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mediaPlayer.start();
//        if(!me.getBooleanExtra("playMusic", true)){
//            mediaPlayer.stop();
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        videoView.suspend();
//        mediaPlayer.pause();
//        super.onPause();
//    }
//
//    @Override
//    protected void onDestroy() {
//        videoView.stopPlayback();
//        mediaPlayer.stop();
//        mediaPlayer.release();
//        super.onDestroy();
//    }
//
//    @Override
//    public void onClick(View v) {
//        int viewId = v.getId();
//
//        if(viewId == btSendBreakfastToCustomize.getId())
//            sendToCustomize();
//
//        if(viewId == btClearBreakfastSelection.getId())
//            todayMenu.clearBreakfast(breakfastSelection.this);
//
//        if(viewId == btBackFromBreakfastSelect.getId())
//            backToMealsMenu();
//    }
//}
//
//    <?xml version="1.0" encoding="utf-8"?>
//<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
//    xmlns:app="http://schemas.android.com/apk/res-auto"
//    xmlns:tools="http://schemas.android.com/tools"
//    android:layout_width="match_parent"
//    android:layout_height="match_parent"
//    android:gravity="center"
//    android:orientation="vertical"
//    tools:context=".breakfastSelection">
//
//    <VideoView
//    android:id="@+id/breakfastVideoView"
//    android:layout_width="match_parent"
//    android:layout_height="match_parent"
//    android:layout_alignParentTop="true"
//    android:layout_alignParentBottom="true"
//    android:layout_alignParentLeft="true"
//    android:layout_alignParentRight="true" />
//
//    <LinearLayout
//    android:id="@+id/breakfastSelectionLinearLayout"
//    android:layout_width="match_parent"
//    android:layout_height="match_parent"
//    android:orientation="vertical"
//    android:gravity="center">
//
//        <TextView
//    android:layout_width="match_parent"
//    android:layout_height="100dp"
//    android:padding="10pt"
//    android:text="Choose your breakfast, from the options below:"
//    android:textSize="10pt" />
//
//        <EditText
//    android:id="@+id/etFilterBreakfast"
//    android:layout_width="150pt"
//    android:layout_height="wrap_content"
//    android:ems="10"
//    android:hint="You can search here: "
//    android:inputType="textPersonName" />
//
//        <ListView
//    android:id="@+id/listViewBreakfast"
//    android:layout_width="match_parent"
//    android:layout_height="375dp"
//    android:layout_marginTop="2pt">
//        </ListView>
//
//        <Button
//    android:id="@+id/btSendBreakfastToCustomize"
//    android:layout_width="125pt"
//    android:layout_height="wrap_content"
//    android:layout_marginLeft="15pt"
//    android:layout_marginTop="10pt"
//    android:layout_marginRight="15pt"
//    android:layout_marginBottom="2pt"
//    android:text="Customize meal" />
//
//        <Button
//    android:id="@+id/btClearBreakfastSelection"
//    android:layout_width="125pt"
//    android:layout_height="wrap_content"
//    android:layout_marginLeft="15pt"
//    android:layout_marginRight="15pt"
//    android:layout_marginBottom="2pt"
//    android:text="Clear selection" />
//
//        <Button
//    android:id="@+id/btBackFromBreakfastSelect"
//    android:layout_width="125pt"
//    android:layout_height="wrap_content"
//    android:layout_marginLeft="15pt"
//    android:layout_marginRight="15pt"
//    android:text="Back" />
//    </LinearLayout>
//
//</RelativeLayout>

    //package com.example.mydieter9005;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.media.MediaPlayer;
//import android.net.Uri;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.Toast;
//import android.widget.VideoView;
//
//import java.io.BufferedReader;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//
//public class lunchSelection extends AppCompatActivity implements View.OnClickListener {
//
//    private MediaPlayer mediaPlayer;
//    private VideoView videoView;
//
//    Button btSendLunchToCustomize, btClearLunchSelection, btBackFromLunchSelect;
//    EditText etFilterLunch;
//    ListView listView;
//
//    DailyMenu todayMenu = DailyMenu.getTodayMenu();
//    FileAndDatabaseHelper fileAndDatabaseHelper;
//    Song activeSong = Song.getSongs().get(0);
//    MealListAdapter adapter;
//    ArrayList<Meal> mealsList;
//
//    FileInputStream is;
//    InputStreamReader isr;
//    BufferedReader br;
//    Intent me;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_lunch_selection);
//
//        me = getIntent();
//        if(me.hasExtra("activeSong"))
//            activeSong = (Song) me.getSerializableExtra("activeSong");
//
//        mealsList = new ArrayList<>();
//
//        ArrayList<Ingredient> ingredientsNeeded = new ArrayList<Ingredient>();  // For multi-ingredients meals.
//
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("bread"), 150));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("yellow cheese"), 75));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("thousand island dressing"), 25));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("ketchup"), 25));
//        mealsList.add(new Meal("Toast", ingredientsNeeded));
//        ingredientsNeeded.clear();
//
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("bread"), 150));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("yellow cheese"), 75));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("thousand island dressing"), 25));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("ketchup"), 25));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("tomato"), 100));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("cucumber"), 100));
//        mealsList.add(new Meal("Toast with tomato and cucumber", ingredientsNeeded));
//        ingredientsNeeded.clear();
//
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("bread"), 150));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("yellow cheese"), 75));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("thousand island dressing"), 25));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("ketchup"), 25));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("tomato"), 100));
//        mealsList.add(new Meal("Toast with tomato", ingredientsNeeded));
//        ingredientsNeeded.clear();
//
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("bread"), 150));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("yellow cheese"), 75));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("thousand island dressing"), 25));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("ketchup"), 25));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("cucumber"), 100));
//        mealsList.add(new Meal("Toast with cucumber", ingredientsNeeded));
//        ingredientsNeeded.clear();
//
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("bread"), 150));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("yellow cheese"), 75));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("thousand island dressing"), 25));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("ketchup"), 25));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("olive"), 100));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("corn"), 100));
//        mealsList.add(new Meal("Toast with olive and corn", ingredientsNeeded));
//        ingredientsNeeded.clear();
//
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("bread"), 150));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("yellow cheese"), 75));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("thousand island dressing"), 25));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("ketchup"), 25));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("olive"), 100));
//        mealsList.add(new Meal("Toast with olive", ingredientsNeeded));
//        ingredientsNeeded.clear();
//
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("bread"), 150));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("yellow cheese"), 75));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("thousand island dressing"), 25));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("ketchup"), 25));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("corn"), 100));
//        mealsList.add(new Meal("Toast with corn", ingredientsNeeded));
//        ingredientsNeeded.clear();
//
//        listView = (ListView) findViewById(R.id.listViewLunch);
//        videoView = (VideoView) findViewById(R.id.lunchVideoView);
//
//        btSendLunchToCustomize = (Button) findViewById(R.id.btSendLunchToCustomize);
//        btSendLunchToCustomize.setOnClickListener(this);
//        btClearLunchSelection = (Button) findViewById(R.id.btClearLunchSelection);
//        btClearLunchSelection.setOnClickListener(this);
//        btBackFromLunchSelect = (Button) findViewById(R.id.btBackFromLunchSelect);
//        btBackFromLunchSelect.setOnClickListener(this);
//
//        etFilterLunch = (EditText) findViewById(R.id.etFilterLunch);
//        etFilterLunch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                (lunchSelection.this).adapter.getFilter().filter(s);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {}
//        });
//
//        fileAndDatabaseHelper = new FileAndDatabaseHelper(this, me);
//        activeSong = fileAndDatabaseHelper.implementSettingsData();
//
//        setListViewAdapter();
//        initiateVideoPlayer();
//        initiateMediaPlayer();
//    }
//
//    public void setListViewAdapter(){
//        adapter = new MealListAdapter(this, mealsList);
//        listView.setAdapter(adapter);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Meal selectedItem = (Meal) adapterView.getItemAtPosition(i);
//
//                todayMenu.addLunch(selectedItem);
//
//                me.setClass(lunchSelection.this, mealsMenu.class);
//                startActivity(me);
//            }
//        });
//
//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Meal selectedItem = (Meal) parent.getItemAtPosition(position);
//
//                showMealIngredientsInfo(selectedItem);
//                return true;
//            }
//        });
//    }
//
//    public void showMealInfo(Meal meal){
//        AlertDialog ad;
//        AlertDialog.Builder adb;
//        adb = new AlertDialog.Builder(this);
//        adb.setTitle("Your meal nutrition: ");
//        adb.setMessage(meal.getGrams() + " grams." + "\n" + meal.getProteins() + " proteins." + "\n" + meal.getFats() + " fats." + "\n" + meal.getCalories() + " calories.");
//        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//
//        adb.setNegativeButton("Ingredients", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                showMealIngredientsInfo(meal);
//            }
//        });
//
//        ad = adb.create();
//        ad.show();
//    }
//
//    public void showMealIngredientsInfo(Meal meal){
//        AlertDialog ad;
//        AlertDialog.Builder adb;
//        adb = new AlertDialog.Builder(this);
//        adb.setTitle("Your meal ingredients: ");
//        String mealInfo = "";
//        for(Ingredient ingredient : meal.getNeededIngredientsForMeal())
//            mealInfo += ingredient.getName() + ": " + ingredient.getGrams() + " grams." + "\n";
//        adb.setMessage(mealInfo);
//        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//
//        adb.setNegativeButton("Nutrition", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                showMealInfo(meal);
//            }
//        });
//
//        ad = adb.create();
//        ad.show();
//    }
//
//    public void sendToCustomize(){
//        me.setClass(lunchSelection.this, customMeals.class);
//        me.putExtra("cameFrom", "lunch");
//        startActivity(me);
//    }
//
//    public void backToMealsMenu(){
//        me.setClass(lunchSelection.this, mealsMenu.class);
//        startActivity(me);
//    }
//
//    public void initiateVideoPlayer(){
//        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.lunch_selection_background_video);
//        videoView.setVideoURI(uri);
//
//        if(me.getBooleanExtra("useVideos", true))
//            videoView.start();
//
//        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                mp.setLooping(true);
//            }
//        });
//    }
//
//    public void initiateMediaPlayer(){
//        mediaPlayer = MediaPlayer.create(lunchSelection.this, activeSong.getId());
//        mediaPlayer.setLooping(true);
//        if(me.getBooleanExtra("playMusic", true)){
//            mediaPlayer.start();
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int itemID = item.getItemId();
//        if(itemID == R.id.sendToMusicMaster){
//            me.setClass(lunchSelection.this, musicMaster.class);
//            me.putExtra("cameToMusicMasterFrom", getLocalClassName());
//            startActivity(me);
//        }
//
//        if(itemID == R.id.sendToSettings){
//            me.setClass(lunchSelection.this, settingsSetter.class);
//            me.putExtra("cameToSettingsFrom", getLocalClassName());
//            startActivity(me);
//        }
//
//        if(itemID == R.id.sendToUserScreen){
//            me.setClass(lunchSelection.this, UserInfoScreen.class);
//            me.putExtra("cameToUserScreenFrom", getLocalClassName());
//            startActivity(me);
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    protected void onPostResume() {
//        super.onPostResume();
//        videoView.resume();
//        if(!me.getBooleanExtra("useVideos", true)){
//            findViewById(R.id.lunchSelectionLinearLayout).setBackground(getDrawable(R.drawable.lunch_selection_background));
//            videoView.stopPlayback();
//        }
//        else
//            videoView.start();
//    }
//
//    @Override
//    protected void onRestart() {
//        videoView.start();
//        super.onRestart();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mediaPlayer.start();
//        if(!me.getBooleanExtra("playMusic", true)){
//            mediaPlayer.stop();
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        videoView.suspend();
//        mediaPlayer.pause();
//        super.onPause();
//    }
//
//    @Override
//    protected void onDestroy() {
//        videoView.stopPlayback();
//        mediaPlayer.stop();
//        mediaPlayer.release();
//        super.onDestroy();
//    }
//
//    @Override
//    public void onClick(View v) {
//        int viewId = v.getId();
//
//        if(viewId == btSendLunchToCustomize.getId())
//            sendToCustomize();
//
//        if(viewId == btClearLunchSelection.getId())
//            todayMenu.clearLunch(lunchSelection.this);
//
//        if(viewId == btBackFromLunchSelect.getId())
//            backToMealsMenu();
//    }
//}
//
//    <?xml version="1.0" encoding="utf-8"?>
//<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
//    xmlns:app="http://schemas.android.com/apk/res-auto"
//    xmlns:tools="http://schemas.android.com/tools"
//    android:layout_width="match_parent"
//    android:layout_height="match_parent"
//    android:gravity="center"
//    android:orientation="vertical"
//    tools:context=".lunchSelection">
//
//    <VideoView
//    android:id="@+id/lunchVideoView"
//    android:layout_width="match_parent"
//    android:layout_height="match_parent"
//    android:layout_alignParentTop="true"
//    android:layout_alignParentBottom="true"
//    android:layout_alignParentLeft="true"
//    android:layout_alignParentRight="true" />
//
//    <LinearLayout
//    android:id="@+id/lunchSelectionLinearLayout"
//    android:layout_width="match_parent"
//    android:layout_height="match_parent"
//    android:orientation="vertical"
//    android:gravity="center">
//
//        <TextView
//    android:id="@+id/textView"
//    android:layout_width="match_parent"
//    android:layout_height="100dp"
//    android:padding="10pt"
//    android:text="Choose your lunch, from the options below:"
//    android:textSize="10pt" />
//
//        <EditText
//    android:id="@+id/etFilterLunch"
//    android:layout_width="150pt"
//    android:layout_height="wrap_content"
//    android:ems="10"
//    android:hint="You can search here: "
//    android:inputType="textPersonName" />
//
//        <ListView
//    android:id="@+id/listViewLunch"
//    android:layout_width="match_parent"
//    android:layout_height="375dp"
//    android:layout_marginTop="2pt" >
//        </ListView>
//
//        <Button
//    android:id="@+id/btSendLunchToCustomize"
//    android:layout_width="125pt"
//    android:layout_height="wrap_content"
//    android:layout_marginLeft="15pt"
//    android:layout_marginTop="10pt"
//    android:layout_marginRight="15pt"
//    android:layout_marginBottom="2pt"
//    android:text="Customize meal" />
//
//        <Button
//    android:id="@+id/btClearLunchSelection"
//    android:layout_width="125pt"
//    android:layout_height="wrap_content"
//    android:layout_marginLeft="15pt"
//    android:layout_marginRight="15pt"
//    android:layout_marginBottom="2pt"
//    android:text="Clear selection" />
//
//        <Button
//    android:id="@+id/btBackFromLunchSelect"
//    android:layout_width="125pt"
//    android:layout_height="wrap_content"
//    android:layout_marginLeft="15pt"
//    android:layout_marginRight="15pt"
//    android:text="Back" />
//    </LinearLayout>
//
//</RelativeLayout>

//    package com.example.mydieter9005;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.media.MediaPlayer;
//import android.net.Uri;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.VideoView;
//
//import java.io.BufferedReader;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//
//public class dinnerSelection extends AppCompatActivity implements View.OnClickListener {
//
//    private MediaPlayer mediaPlayer;
//    private VideoView videoView;
//
//    Button btSendDinnerToCustomize, btClearDinnerSelection, btBackFromDinnerSelect;
//    EditText etFilterDinner;
//    ListView listView;
//
//    DailyMenu todayMenu = DailyMenu.getTodayMenu();
//    FileAndDatabaseHelper fileAndDatabaseHelper;
//    Song activeSong = Song.getSongs().get(0);
//    MealListAdapter adapter;
//    ArrayList<Meal> mealsList;
//
//    Intent me;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_dinner_selection);
//
//        me = getIntent();
//        if(me.hasExtra("activeSong"))
//            activeSong = (Song) me.getSerializableExtra("activeSong");
//
//        mealsList = new ArrayList<>();
//        ArrayList<Ingredient> ingredientsNeeded = new ArrayList<Ingredient>();  // For multi-ingredients meals.
//
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("bread"), 150));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("yellow cheese"), 75));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("thousand island dressing"), 25));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("ketchup"), 25));
//        mealsList.add(new Meal("Toast", ingredientsNeeded));
//        ingredientsNeeded.clear();
//
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("bread"), 150));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("yellow cheese"), 75));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("thousand island dressing"), 25));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("ketchup"), 25));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("tomato"), 100));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("cucumber"), 100));
//        mealsList.add(new Meal("Toast with tomato and cucumber", ingredientsNeeded));
//        ingredientsNeeded.clear();
//
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("bread"), 150));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("yellow cheese"), 75));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("thousand island dressing"), 25));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("ketchup"), 25));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("tomato"), 100));
//        mealsList.add(new Meal("Toast with tomato", ingredientsNeeded));
//        ingredientsNeeded.clear();
//
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("bread"), 150));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("yellow cheese"), 75));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("thousand island dressing"), 25));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("ketchup"), 25));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("cucumber"), 100));
//        mealsList.add(new Meal("Toast with cucumber", ingredientsNeeded));
//        ingredientsNeeded.clear();
//
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("bread"), 150));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("yellow cheese"), 75));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("thousand island dressing"), 25));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("ketchup"), 25));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("olive"), 100));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("corn"), 100));
//        mealsList.add(new Meal("Toast with olive and corn", ingredientsNeeded));
//        ingredientsNeeded.clear();
//
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("bread"), 150));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("yellow cheese"), 75));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("thousand island dressing"), 25));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("ketchup"), 25));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("olive"), 100));
//        mealsList.add(new Meal("Toast with olive", ingredientsNeeded));
//        ingredientsNeeded.clear();
//
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("bread"), 150));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("yellow cheese"), 75));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("thousand island dressing"), 25));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("ketchup"), 25));
//        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("corn"), 100));
//        mealsList.add(new Meal("Toast with corn", ingredientsNeeded));
//        ingredientsNeeded.clear();
//
//        listView = (ListView) findViewById(R.id.listViewDinner);
//        videoView = (VideoView) findViewById(R.id.dinnerVideoView);
//
//        btSendDinnerToCustomize = (Button) findViewById(R.id.btSendDinnerToCustomize);
//        btSendDinnerToCustomize.setOnClickListener(this);
//        btClearDinnerSelection = (Button) findViewById(R.id.btClearDinnerSelection);
//        btClearDinnerSelection.setOnClickListener(this);
//        btBackFromDinnerSelect = (Button) findViewById(R.id.btBackFromDinnerSelect);
//        btBackFromDinnerSelect.setOnClickListener(this);
//
//        etFilterDinner = (EditText) findViewById(R.id.etFilterDinner);
//        etFilterDinner.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                (dinnerSelection.this).adapter.getFilter().filter(s);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {}
//        });
//
//        fileAndDatabaseHelper = new FileAndDatabaseHelper(this, me);
//        activeSong = fileAndDatabaseHelper.implementSettingsData();
//
//        setListViewAdapter();
//        initiateVideoPlayer();
//        initiateMediaPlayer();
//    }
//
//    public void setListViewAdapter(){
//        adapter = new MealListAdapter(this, mealsList);
//        listView.setAdapter(adapter);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Meal selectedItem = (Meal) adapterView.getItemAtPosition(i);
//
//                todayMenu.addDinner(selectedItem);
//
//                me.setClass(dinnerSelection.this, mealsMenu.class);
//                startActivity(me);
//            }
//        });
//
//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Meal selectedItem = (Meal) parent.getItemAtPosition(position);
//
//                showMealIngredientsInfo(selectedItem);
//                return true;
//            }
//        });
//    }
//
//    public void showMealInfo(Meal meal){
//        AlertDialog ad;
//        AlertDialog.Builder adb;
//        adb = new AlertDialog.Builder(this);
//        adb.setTitle("Your meal nutrition: ");
//        adb.setMessage(meal.getGrams() + " grams." + "\n" + meal.getProteins() + " proteins." + "\n" + meal.getFats() + " fats." + "\n" + meal.getCalories() + " calories.");
//        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//
//        adb.setNegativeButton("Ingredients", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                showMealIngredientsInfo(meal);
//            }
//        });
//
//        ad = adb.create();
//        ad.show();
//    }
//
//    public void showMealIngredientsInfo(Meal meal){
//        AlertDialog ad;
//        AlertDialog.Builder adb;
//        adb = new AlertDialog.Builder(this);
//        adb.setTitle("Your meal ingredients: ");
//        String mealInfo = "";
//        for(Ingredient ingredient : meal.getNeededIngredientsForMeal())
//            mealInfo += ingredient.getName() + ": " + ingredient.getGrams() + " grams." + "\n";
//        adb.setMessage(mealInfo);
//        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//
//        adb.setNegativeButton("Nutrition", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                showMealInfo(meal);
//            }
//        });
//
//        ad = adb.create();
//        ad.show();
//    }
//
//    public void sendToCustomize(){
//        me.setClass(dinnerSelection.this, customMeals.class);
//        me.putExtra("cameFrom", "dinner");
//        startActivity(me);
//    }
//
//    public void backToMealsMenu(){
//        me.setClass(dinnerSelection.this, mealsMenu.class);
//        startActivity(me);
//    }
//
//    public void initiateVideoPlayer(){
//        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.dinner_selection_background_video);
//        videoView.setVideoURI(uri);
//
//        if(me.getBooleanExtra("useVideos", true))
//            videoView.start();
//
//        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                mp.setLooping(true);
//            }
//        });
//    }
//
//    public void initiateMediaPlayer(){
//        mediaPlayer = MediaPlayer.create(dinnerSelection.this, activeSong.getId());
//        mediaPlayer.setLooping(true);
//        if(me.getBooleanExtra("playMusic", true)){
//            mediaPlayer.start();
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int itemID = item.getItemId();
//        if(itemID == R.id.sendToMusicMaster){
//            me.setClass(dinnerSelection.this, musicMaster.class);
//            me.putExtra("cameToMusicMasterFrom", getLocalClassName());
//            startActivity(me);
//        }
//
//        if(itemID == R.id.sendToSettings){
//            me.setClass(dinnerSelection.this, settingsSetter.class);
//            me.putExtra("cameToSettingsFrom", getLocalClassName());
//            startActivity(me);
//        }
//
//        if(itemID == R.id.sendToUserScreen){
//            me.setClass(dinnerSelection.this, UserInfoScreen.class);
//            me.putExtra("cameToUserScreenFrom", getLocalClassName());
//            startActivity(me);
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    protected void onPostResume() {
//        super.onPostResume();
//        videoView.resume();
//        if(!me.getBooleanExtra("useVideos", true)){
//            findViewById(R.id.dinnerSelectionLinearLayout).setBackground(getDrawable(R.drawable.dinner_selection_background));
//            videoView.stopPlayback();
//        }
//        else
//            videoView.start();
//    }
//
//    @Override
//    protected void onRestart() {
//        videoView.start();
//        super.onRestart();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mediaPlayer.start();
//        if(!me.getBooleanExtra("playMusic", true)){
//            mediaPlayer.stop();
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        videoView.suspend();
//        mediaPlayer.pause();
//        super.onPause();
//    }
//
//    @Override
//    protected void onDestroy() {
//        videoView.stopPlayback();
//        mediaPlayer.stop();
//        mediaPlayer.release();
//        super.onDestroy();
//    }
//
//    @Override
//    public void onClick(View v) {
//        int viewId = v.getId();
//
//        if(viewId == btSendDinnerToCustomize.getId())
//            sendToCustomize();
//
//        if(viewId == btClearDinnerSelection.getId())
//            todayMenu.clearDinner(dinnerSelection.this);
//
//        if(viewId == btBackFromDinnerSelect.getId())
//            backToMealsMenu();
//    }
//}
//
//    <?xml version="1.0" encoding="utf-8"?>
//<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
//    xmlns:app="http://schemas.android.com/apk/res-auto"
//    xmlns:tools="http://schemas.android.com/tools"
//    android:layout_width="match_parent"
//    android:layout_height="match_parent"
//    android:gravity="center"
//    android:orientation="vertical"
//    tools:context=".dinnerSelection">
//
//    <VideoView
//    android:id="@+id/dinnerVideoView"
//    android:layout_width="match_parent"
//    android:layout_height="match_parent"
//    android:layout_alignParentTop="true"
//    android:layout_alignParentBottom="true"
//    android:layout_alignParentLeft="true"
//    android:layout_alignParentRight="true" />
//
//    <LinearLayout
//    android:id="@+id/dinnerSelectionLinearLayout"
//    android:layout_width="match_parent"
//    android:layout_height="match_parent"
//    android:orientation="vertical"
//    android:gravity="center">
//
//        <TextView
//    android:layout_width="match_parent"
//    android:layout_height="100dp"
//    android:padding="10pt"
//    android:text="Choose your dinner, from the options below:"
//    android:textSize="10pt" />
//
//        <EditText
//    android:id="@+id/etFilterDinner"
//    android:layout_width="150pt"
//    android:layout_height="wrap_content"
//    android:ems="10"
//    android:hint="You can search here: "
//    android:inputType="textPersonName" />
//
//        <ListView
//    android:id="@+id/listViewDinner"
//    android:layout_width="match_parent"
//    android:layout_height="375dp"
//    android:layout_marginTop="2pt" >
//        </ListView>
//
//        <Button
//    android:id="@+id/btSendDinnerToCustomize"
//    android:layout_width="125pt"
//    android:layout_height="wrap_content"
//    android:layout_marginLeft="15pt"
//    android:layout_marginTop="10pt"
//    android:layout_marginRight="15pt"
//    android:layout_marginBottom="2pt"
//    android:text="Customize meal" />
//
//        <Button
//    android:id="@+id/btClearDinnerSelection"
//    android:layout_width="125pt"
//    android:layout_height="wrap_content"
//    android:layout_marginLeft="15pt"
//    android:layout_marginRight="15pt"
//    android:layout_marginBottom="2pt"
//    android:text="Clear selection" />
//
//        <Button
//    android:id="@+id/btBackFromDinnerSelect"
//    android:layout_width="125pt"
//    android:layout_height="wrap_content"
//    android:layout_marginLeft="15pt"
//    android:layout_marginRight="15pt"
//    android:text="Back" />
//    </LinearLayout>
//
//</RelativeLayout>

//            WorldSavedCustomMeals
//package com.example.mydieter9005;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.media.MediaPlayer;
//import android.net.ConnectivityManager;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.Toast;
//import android.widget.VideoView;
//
//import com.example.mydieter9005.databinding.ActivityMainBinding;
//import com.example.mydieter9005.databinding.ActivityWorldSavedCustomMealsBinding;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import java.io.BufferedReader;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//
//public class WorldSavedCustomMeals extends AppCompatActivity implements View.OnClickListener {
//
//    private NetworkConnectionReceiver networkConnectionReceiver;
//
//    private MediaPlayer mediaPlayer;
//    private VideoView videoView;
//
//    Button btFinishWorldSavedCustomMeals;
//    EditText etFilterWorldSavedCustomMeals;
//    LinearLayout linearLayout;
//    ListView listView;
//
//    ArrayList<Meal> mealsList = new ArrayList<Meal>();
//    MealListAdapter adapter;
//    String cameFrom;
//
//    DailyMenu todayMenu = DailyMenu.getTodayMenu();
//    FileAndDatabaseHelper fileAndDatabaseHelper;
//    Song activeSong = Song.getSongs().get(0);
//
//    DatabaseReference databaseReference;
//
//    Intent me;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_world_saved_custom_meals);
//
//        me = getIntent();
//        if(me.hasExtra("activeSong"))
//            activeSong = (Song) me.getSerializableExtra("activeSong");
//        cameFrom = me.getStringExtra("cameFrom");
//
//        listView = (ListView) findViewById(R.id.lvWorldSavedCustomMeals);
//        videoView = (VideoView) findViewById(R.id.worldSavedCustomMealsVideoView);
//        linearLayout = (LinearLayout) findViewById(R.id.worldSavedCustomMealsLinearLayout);
//
//        btFinishWorldSavedCustomMeals = (Button) findViewById(R.id.btFinishWorldSavedCustomMeals);
//        btFinishWorldSavedCustomMeals.setOnClickListener(this);
//
//        etFilterWorldSavedCustomMeals = (EditText) findViewById(R.id.etFilterWorldSavedCustomMeals);
//        etFilterWorldSavedCustomMeals.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                (WorldSavedCustomMeals.this).adapter.getFilter().filter(s);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {}
//        });
//
//        fileAndDatabaseHelper = new FileAndDatabaseHelper(this, me);
//        activeSong = fileAndDatabaseHelper.implementSettingsData();
//
//        setCustomNetworkConnectionReceiver();
//        setListViewAdapter();
//        initiateVideoPlayer();
//        initiateMediaPlayer();
//    }
//
//    public void initiateListViewFields(){
//        mealsList.clear();
//        databaseReference = FirebaseDatabase.getInstance().getReference("recipes");
//        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                ArrayList<Ingredient> ingredientsNeededInfo = new ArrayList<Ingredient>();
//                Ingredient ingredient;
//                String mealName, ingredientName;
//                Double ingredientGrams;
//
//                if(task.isSuccessful()){
//                    if(task.getResult().exists()){
//                        DataSnapshot dataSnapshot = task.getResult();
//                        for(DataSnapshot customMeal : dataSnapshot.getChildren()){
//                            ingredientsNeededInfo.clear();
//                            mealName = customMeal.getKey();
//                            for(int i = 0; i < customMeal.getChildrenCount(); i++) {
//                                ingredientName = getName(customMeal.child(i + "").getValue().toString());
//                                ingredientGrams = getGrams(customMeal.child(i + "").getValue().toString());
//
//                                ingredient = new Ingredient(Ingredient.getIngredientByName(ingredientName), ingredientGrams);
//                                ingredientsNeededInfo.add(ingredient);
//                            }
//
//                            mealsList.add(new Meal(mealName, ingredientsNeededInfo));
//                            adapter.notifyDataSetChanged();
//                        }
//
//                        try{
//                            unregisterReceiver(networkConnectionReceiver);
//                        }
//                        catch (IllegalArgumentException e){
//                            e.getStackTrace();
//                        }
//                    }
//                    else{
//                        notEvenOneCustomMealAdded();
//                    }
//                }
//                else{
//                    recipesDatabaseNotFound();
//                    Toast.makeText(WorldSavedCustomMeals.this, "Failed to load meals.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//
//    public String getName(String nameAndGrams){  // Needed when get ingredient from file.
//        return nameAndGrams.split(": ")[0];
//    }
//
//    public double getGrams(String nameAndGrams){  // Needed when get ingredient from file.
//        return Double.parseDouble((nameAndGrams.split(": ")[1]).split(" ")[0]);
//    }
//
//    public void recipesDatabaseNotFound(){
//        AlertDialog ad;
//        AlertDialog.Builder adb;
//        adb = new AlertDialog.Builder(this);
//        adb.setTitle("Custom meals not found!");
//        adb.setMessage("We have trouble connecting our database right now, please come back later");
//        adb.setIcon(R.drawable.ic_error_icon);
//        adb.setCancelable(false);
//
//        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                me.setClass(WorldSavedCustomMeals.this, customSelection.class);
//                startActivity(me);
//            }
//        });
//
//        ad = adb.create();
//        ad.show();
//    }
//
//    public void notEvenOneCustomMealAdded(){
//        AlertDialog ad;
//        AlertDialog.Builder adb;
//        adb = new AlertDialog.Builder(this);
//        adb.setTitle("Custom meals not found!");
//        adb.setMessage("It's seems like no one saved any custom meal so far, you can be the first!.");
//        adb.setIcon(R.drawable.ic_food_icon);
//        adb.setCancelable(false);
//
//        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                me.setClass(WorldSavedCustomMeals.this, customSelection.class);
//                startActivity(me);
//            }
//        });
//
//        ad = adb.create();
//        ad.show();
//    }
//
//    public void setListViewAdapter(){
//        initiateListViewFields();
//
//        adapter = new MealListAdapter(this, mealsList);
//        listView.setAdapter(adapter);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Meal selectedItem = (Meal) adapterView.getItemAtPosition(i);
//                selectedItem.setName(selectedItem.getName().split(" - ")[1]);
//
//                if(cameFrom.equals("breakfast"))
//                    todayMenu.addBreakfast(selectedItem);
//
//                if(cameFrom.equals("lunch"))
//                    todayMenu.addLunch(selectedItem);
//
//                if(cameFrom.equals("dinner"))
//                    todayMenu.addDinner(selectedItem);
//
//                me.setClass(WorldSavedCustomMeals.this, mealsMenu.class);
//                startActivity(me);
//            }
//        });
//
//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Meal selectedItem = (Meal) parent.getItemAtPosition(position);
//
//                showMealIngredientsInfo(selectedItem);
//                return true;
//            }
//        });
//    }
//
//    public void showMealInfo(Meal meal){
//        AlertDialog ad;
//        AlertDialog.Builder adb;
//        adb = new AlertDialog.Builder(this);
//        adb.setTitle("Your meal nutrition: ");
//        adb.setMessage(meal.getGrams() + " grams." + "\n" + meal.getProteins() + " proteins." + "\n" + meal.getFats() + " fats." + "\n" + meal.getCalories() + " calories.");
//        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//
//        adb.setNegativeButton("Ingredients", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                showMealIngredientsInfo(meal);
//            }
//        });
//
//        adb.setNeutralButton("Save", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                me.setClass(WorldSavedCustomMeals.this, customMeals.class);
//                me.putExtra("globalCustomMealToSave", meal);
//                startActivity(me);
//            }
//        });
//
//        ad = adb.create();
//        ad.show();
//    }
//
//    public void showMealIngredientsInfo(Meal meal){
//        AlertDialog ad;
//        AlertDialog.Builder adb;
//        adb = new AlertDialog.Builder(this);
//        adb.setTitle("Your meal ingredients: ");
//
//        String mealInfo = "";
//        for(Ingredient ingredient : meal.getNeededIngredientsForMeal())
//            mealInfo += ingredient.getName() + ": " + ingredient.getGrams() + " grams." + "\n";
//        adb.setMessage(mealInfo);
//
//        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//
//        adb.setNegativeButton("Nutrition", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                showMealInfo(meal);
//            }
//        });
//
//        adb.setNeutralButton("Save", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                me.setClass(WorldSavedCustomMeals.this, customMeals.class);
//                me.putExtra("globalCustomMealToSave", meal);
//                startActivity(me);
//            }
//        });
//
//        ad = adb.create();
//        ad.show();
//    }
//
//    public void exitWorldSavedCustomMeals(){
//        me.setClass(WorldSavedCustomMeals.this, customSelection.class);
//        startActivity(me);
//    }
//
//    public void setCustomNetworkConnectionReceiver(){
//        networkConnectionReceiver = new NetworkConnectionReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                try{
//                    if(!isOnline(context))
//                        noInternetAccess(context);
//                }
//                catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void noInternetAccess(Context context){
//                AlertDialog ad;
//                AlertDialog.Builder adb;
//                adb = new AlertDialog.Builder(context);
//                adb.setTitle("Internet connection not found!");
//                adb.setMessage("Connect to the internet and try again.");
//                adb.setIcon(R.drawable.ic_network_not_found);
//                adb.setCancelable(false);
//
//                adb.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        if(!isOnline(context))
//                            noInternetAccess(context);
//                        else
//                            Toast.makeText(context, "Network connection available.", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//                adb.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        finish();
//                    }
//                });
//
//                ad = adb.create();
//                ad.show();
//            }
//        };
//    }
//
//    public void initiateVideoPlayer(){
//        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.world_saved_custom_meals_background_video);
//        videoView.setVideoURI(uri);
//
//        if(me.getBooleanExtra("useVideos", true))
//            videoView.start();
//
//        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                mp.setLooping(true);
//            }
//        });
//    }
//
//    public void initiateMediaPlayer(){
//        mediaPlayer = MediaPlayer.create(WorldSavedCustomMeals.this, activeSong.getId());
//        mediaPlayer.setLooping(true);
//        if(me.getBooleanExtra("playMusic", true)){
//            mediaPlayer.start();
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int itemID = item.getItemId();
//        if(itemID == R.id.sendToMusicMaster){
//            me.setClass(WorldSavedCustomMeals.this, musicMaster.class);
//            me.putExtra("cameToMusicMasterFrom", getLocalClassName());
//            startActivity(me);
//        }
//
//        if(itemID == R.id.sendToSettings){
//            me.setClass(WorldSavedCustomMeals.this, settingsSetter.class);
//            me.putExtra("cameToSettingsFrom", getLocalClassName());
//            startActivity(me);
//        }
//
//        if(itemID == R.id.sendToUserScreen){
//            me.setClass(WorldSavedCustomMeals.this, UserInfoScreen.class);
//            me.putExtra("cameToUserScreenFrom", getLocalClassName());
//            startActivity(me);
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    protected void onPostResume() {
//        super.onPostResume();
//        videoView.resume();
//        if(!me.getBooleanExtra("useVideos", true)){
//            linearLayout.setBackground(getDrawable(R.drawable.world_saved_custom_meals_background));
//            videoView.stopPlayback();
//        }
//        else
//            videoView.start();
//    }
//
//    @Override
//    protected void onRestart() {
//        videoView.start();
//        super.onRestart();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        IntentFilter networkConnectionFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
//            registerReceiver(networkConnectionReceiver, networkConnectionFilter);
//        }
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            registerReceiver(networkConnectionReceiver, networkConnectionFilter);
//        }
//
//        mediaPlayer.start();
//        if(!me.getBooleanExtra("playMusic", true)){
//            mediaPlayer.stop();
//        }
//    }
//
//    @Override
//    protected void onPause() {
//
//        try{
//            unregisterReceiver(networkConnectionReceiver);
//        }
//        catch (IllegalArgumentException e){
//            e.getStackTrace();
//        }
//
//        videoView.suspend();
//        mediaPlayer.pause();
//        super.onPause();
//    }
//
//    @Override
//    protected void onDestroy() {
//        videoView.stopPlayback();
//        mediaPlayer.stop();
//        mediaPlayer.release();
//        super.onDestroy();
//    }
//
//    @Override
//    public void onClick(View v) {
//        int viewId = v.getId();
//
//        if(viewId == btFinishWorldSavedCustomMeals.getId())
//            exitWorldSavedCustomMeals();
//    }
//}
//
//    <?xml version="1.0" encoding="utf-8"?>
//<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
//    xmlns:app="http://schemas.android.com/apk/res-auto"
//    xmlns:tools="http://schemas.android.com/tools"
//    android:layout_width="match_parent"
//    android:layout_height="match_parent"
//    android:orientation="vertical"
//    tools:context=".WorldSavedCustomMeals">
//
//    <VideoView
//    android:id="@+id/worldSavedCustomMealsVideoView"
//    android:layout_width="match_parent"
//    android:layout_height="match_parent"
//    android:layout_alignParentLeft="true"
//    android:layout_alignParentTop="true"
//    android:layout_alignParentRight="true"
//    android:layout_alignParentBottom="true" />
//
//    <LinearLayout
//    android:id="@+id/worldSavedCustomMealsLinearLayout"
//    android:layout_width="match_parent"
//    android:layout_height="match_parent"
//    android:gravity="center"
//    android:orientation="vertical">
//
//        <TextView
//    android:layout_width="match_parent"
//    android:layout_height="100dp"
//    android:padding="10pt"
//    android:text="Here are world published recipes, chose from the options below:"
//    android:textSize="10pt" />
//
//        <EditText
//    android:id="@+id/etFilterWorldSavedCustomMeals"
//    android:layout_width="150pt"
//    android:layout_height="wrap_content"
//    android:ems="10"
//    android:hint="You can search here: "
//    android:inputType="textPersonName" />
//
//        <ListView
//    android:id="@+id/lvWorldSavedCustomMeals"
//    android:layout_width="match_parent"
//    android:layout_height="525dp"
//    android:layout_marginTop="2pt" />
//
//        <Button
//    android:id="@+id/btFinishWorldSavedCustomMeals"
//    android:layout_width="125pt"
//    android:layout_height="wrap_content"
//    android:layout_marginLeft="15pt"
//    android:layout_marginRight="15pt"
//    android:layout_marginBottom="2pt"
//    android:text="Finish" />
//    </LinearLayout>
//</RelativeLayout>

            // mealsMenu
    //package com.example.mydieter9005;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.media.MediaPlayer;
//import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import java.io.BufferedReader;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStreamReader;
//
//public class mealsMenu extends AppCompatActivity implements View.OnClickListener {
//
//    private MediaPlayer mediaPlayer;
//    TextView tvBreakfast, tvLunch, tvDinner;
//    TextView tvTotalProteins, tvTotalFats, tvTotalCalories;
//    Button btBreakfast, btLunch, btDinner, btFinish;
//
//    DailyMenu todayMenu = DailyMenu.getTodayMenu();
//    FileAndDatabaseHelper fileAndDatabaseHelper;
//    Song activeSong = Song.getSongs().get(0);
//
//    FileInputStream is;
//    InputStreamReader isr;
//    BufferedReader br;
//    Intent me;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_meals_menu);
//
//        me = getIntent();
//        if(me.hasExtra("activeSong"))
//            activeSong = (Song) me.getSerializableExtra("activeSong");
//
//        tvBreakfast = (TextView) findViewById(R.id.tvBreakfast);
//        tvLunch = (TextView) findViewById(R.id.tvLunch);
//        tvDinner = (TextView) findViewById(R.id.tvDinner);
//
//        btBreakfast = (Button) findViewById(R.id.btBreakfast);
//        btBreakfast.setOnClickListener(this);
//        btLunch = (Button) findViewById(R.id.btLunch);
//        btLunch.setOnClickListener(this);
//        btDinner = (Button) findViewById(R.id.btDinner);
//        btDinner.setOnClickListener(this);
//        btFinish = (Button) findViewById(R.id.btFinish);
//        btFinish.setOnClickListener(this);
//
//        tvTotalProteins = (TextView) findViewById(R.id.tvTotalProteins);
//        tvTotalFats = (TextView) findViewById(R.id.tvTotalFats);
//        tvTotalCalories = (TextView) findViewById(R.id.tvTotalCalories);
//
//        DailyMenu.saveDailyMenuIntoFile(DailyMenu.getTodayMenu(), mealsMenu.this);
//
//        fileAndDatabaseHelper = new FileAndDatabaseHelper(this, me);
//        activeSong = fileAndDatabaseHelper.implementSettingsData();
//
//        initiateMediaPlayer();
//        updateMeals();
//    }
//
//    public void chooseMealType(){
//        AlertDialog ad;
//        AlertDialog.Builder adb;
//        adb = new AlertDialog.Builder(this);
//        adb.setTitle("Choose meal type!");
//        adb.setIcon(R.drawable.ic_food_icon);
//
//        LinearLayout alertDialogLinearLayout = new LinearLayout(getApplicationContext());
//        alertDialogLinearLayout.setOrientation(LinearLayout.VERTICAL);
//
//        Button chooseBreakfast = new Button(getApplicationContext());
//        chooseBreakfast.setOnClickListener(this);
//        alertDialogLinearLayout.addView(chooseBreakfast);
//
//        Button chooseLunch = new Button(getApplicationContext());
//        chooseLunch.setOnClickListener(this);
//        alertDialogLinearLayout.addView(chooseLunch);
//
//        Button chooseDinner = new Button(getApplicationContext());
//        chooseDinner.setOnClickListener(this);
//        alertDialogLinearLayout.addView(chooseDinner);
//
//        Button chooseCustom = new Button(getApplicationContext());
//        chooseCustom.setOnClickListener(this);
//        alertDialogLinearLayout.addView(chooseCustom);
//
//        adb.setView(alertDialogLinearLayout);
//
//        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//
//        ad = adb.create();
//        ad.show();
//    }
//
//    public void updateMeals(){
//        btBreakfast.setText("Select");
//        btLunch.setText("Select");
//        btDinner.setText("Select");
//
//        if(todayMenu.hasBreakfast()){
//            tvBreakfast.setText("Your breakfast is: " + todayMenu.getUnitedBreakfastName() + ".");
//            btBreakfast.setText("Reselect");
//        }
//
//        if(todayMenu.hasLunch()){
//            tvLunch.setText("Your lunch is: " + todayMenu.getUnitedLunchName() + ".");
//            btLunch.setText("Reselect");
//        }
//
//        if(todayMenu.hasDinner()){
//            tvDinner.setText("Your dinner is: " + todayMenu.getUnitedDinnerName() + ".");
//            btDinner.setText("Reselect");
//        }
//
//        tvTotalProteins.setText("Total proteins: " + todayMenu.getTotalProteins() + " .");
//        tvTotalFats.setText("Total fats: " + todayMenu.getTotalFats() + " .");
//        tvTotalCalories.setText("Total calories: " + todayMenu.getTotalCalories() + " .");
//    }
//
//    public void initiateMediaPlayer(){
//        mediaPlayer = MediaPlayer.create(mealsMenu.this, activeSong.getId());
//        mediaPlayer.setLooping(true);
//        if(me.getBooleanExtra("playMusic", true)){
//            mediaPlayer.start();
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mediaPlayer.start();
//        if(!me.getBooleanExtra("playMusic", true)){
//            mediaPlayer.stop();
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        mediaPlayer.pause();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mediaPlayer.stop();
//        mediaPlayer.release();
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int itemID = item.getItemId();
//        if(itemID == R.id.sendToMusicMaster){
//            me.setClass(mealsMenu.this, musicMaster.class);
//            me.putExtra("cameToMusicMasterFrom", getLocalClassName());
//            startActivity(me);
//        }
//
//        if(itemID == R.id.sendToSettings){
//            me.setClass(mealsMenu.this, settingsSetter.class);
//            me.putExtra("cameToSettingsFrom", getLocalClassName());
//            startActivity(me);
//        }
//
//        if(itemID == R.id.sendToUserScreen){
//            me.setClass(mealsMenu.this, UserInfoScreen.class);
//            me.putExtra("cameToUserScreenFrom", getLocalClassName());
//            startActivity(me);
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public void onClick(View v) {
//        int viewId = v.getId();
//
////        if(viewId == btBreakfast.getId()) {
////            me.setClass(this, breakfastSelection.class);
////            startActivity(me);
////        }
//
//        if(viewId == btLunch.getId()) {
//            me.setClass(this, lunchSelection.class);
//            startActivity(me);
//        }
//
//        if(viewId == btDinner.getId()) {
//            me.setClass(this, dinnerSelection.class);
//            startActivity(me);
//        }
//
//        if(viewId == btFinish.getId()) {
//            if(todayMenu.isThereAtLeastOneThing()){
//                me.setClass(mealsMenu.this, finishMeals.class);
//                startActivity(me);
//            }
//            else
//                Toast.makeText(this, "Please pick at least one meal !", Toast.LENGTH_LONG).show();
//        }
//    }
//
//    @Override
//    public void onBackPressed(){
//        me.setClass(mealsMenu.this, MainActivity.class);
//        startActivity(me);
//    }
//}

//    <?xml version="1.0" encoding="utf-8"?>
//<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
//    xmlns:app="http://schemas.android.com/apk/res-auto"
//    xmlns:tools="http://schemas.android.com/tools"
//    android:layout_width="match_parent"
//    android:layout_height="match_parent"
//    android:orientation="vertical"
//    android:gravity="center"
//    android:background="@drawable/meals_menu_background"
//    tools:context=".mealsMenu">
//
//    <TextView
//    android:id="@+id/tvBreakfast"
//    android:layout_width="100pt"
//    android:layout_height="wrap_content"
//    android:text="Set Breakfast: "
//    android:textColor="@color/black"/>
//
//    <Button
//    android:id="@+id/btBreakfast"
//    android:layout_width="100pt"
//    android:layout_height="wrap_content"
//    android:text="Select" />
//
//    <TextView
//    android:id="@+id/tvLunch"
//    android:layout_width="100pt"
//    android:layout_height="wrap_content"
//    android:text="Set Lunch: "
//    android:textColor="@color/black"/>
//
//    <Button
//    android:id="@+id/btLunch"
//    android:layout_width="100pt"
//    android:layout_height="wrap_content"
//    android:text="Select" />
//
//    <TextView
//    android:id="@+id/tvDinner"
//    android:layout_width="100pt"
//    android:layout_height="wrap_content"
//    android:text="Set Dinner: "
//    android:textColor="@color/black"/>
//
//    <Button
//    android:id="@+id/btDinner"
//    android:layout_width="100pt"
//    android:layout_height="wrap_content"
//    android:text="Select" />
//
//    <TextView
//    android:id="@+id/tvTotalProteins"
//    android:layout_width="100pt"
//    android:layout_height="wrap_content"
//    android:layout_marginTop="7pt"
//    android:textSize="8pt"
//    android:text="Total proteins: 0."
//    android:textColor="@color/black"/>
//
//    <TextView
//    android:id="@+id/tvTotalFats"
//    android:layout_width="100pt"
//    android:layout_height="wrap_content"
//    android:textSize="8pt"
//    android:text="Total fats: 0."
//    android:textColor="@color/black"/>
//
//    <TextView
//    android:id="@+id/tvTotalCalories"
//    android:layout_width="100pt"
//    android:layout_height="wrap_content"
//    android:layout_marginBottom="7pt"
//    android:textSize="8pt"
//    android:text="Total Calories: 0."
//    android:textColor="@color/black"/>
//
//    <Button
//    android:id="@+id/btFinish"
//    android:layout_width="100pt"
//    android:layout_height="wrap_content"
//    android:text="Finish" />
//
//</LinearLayout>
//
//      From: HomeFragment
    //    public void sendToSelected(View v) {
//        int id = v.getId();
//        if (id == btMealsMenu.getId()) {
//            me.setClass(this, mealsMenu.class);
//            startActivity(me);
//        }
//    }

//    public void write(){
//        if(me.hasExtra("meals")){
//            try {
//                fos = openFileOutput(todayDate, Context.MODE_PRIVATE);
//                osw = new OutputStreamWriter(fos);
//                bw = new BufferedWriter(osw);
//
//                bw.write(todayDate + "\n");
//
//                bw.write("Your breakfast: ");
//                if(me.hasExtra("breakfast"))
//                    bw.write(me.getStringExtra("breakfast"));
//
//                bw.write("\n" + "Your lunch: ");
//                if(me.hasExtra("lunch"))
//                    bw.write(me.getStringExtra("lunch"));
//
//                bw.write("\n" + "Your dinner: ");
//                if(me.hasExtra("dinner"))
//                    bw.write(me.getStringExtra("dinner"));
//
//                bw.write("\n" + tvTotalCaloriesMain.getText().toString() + "\n");
//                //bw.write(tvCaloriesLeftMain.getText().toString());
//
//                bw.close();
//
//                Toast.makeText(this, todayDate + " wrote.", Toast.LENGTH_SHORT).show();
//            }
//            catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        else{
//            Toast.makeText(this, "You didn't choose any meal yet.", Toast.LENGTH_SHORT).show();
//        }
//    }

//    public void showFileData(){
//        String[] dataParts = getFileData(todayDate).split("\n");
//        String savedBreakfast="", savedLunch="", savedDinner="", savedTotalCalories="", savedCaloriesLeft="";
//        String[] meals = new String[3];
//
//        if(dataParts[1].replaceAll(":", "").length() == dataParts[1].length() - 2)
//            savedBreakfast = dataParts[1].split(": ")[1] + ": " + dataParts[1].split(": ")[2];
//        if(!savedBreakfast.equals("") && !savedBreakfast.equals(" "))
//            me.putExtra("breakfast", savedBreakfast);
//
//        if(dataParts[2].replaceAll(":", "").length() == dataParts[2].length() - 2)
//            savedLunch = dataParts[2].split(": ")[1] + ": " + dataParts[2].split(": ")[2];
//        if(!savedLunch.equals("") && !savedLunch.equals(" "))
//            me.putExtra("lunch", savedLunch);
//
//        if(dataParts[3].replaceAll(":", "").length() == dataParts[3].length() - 2)
//            savedDinner = dataParts[3].split(": ")[1] + ": " + dataParts[3].split(": ")[2];
//        if(!savedDinner.equals("") && !savedDinner.equals(" "))
//            me.putExtra("dinner", savedDinner);
//
//        savedTotalCalories = dataParts[4].split(": ")[1];
//        if(!savedTotalCalories.equals("") && !savedTotalCalories.equals(" "))
//            me.putExtra("totalCalories", multiUsageFunctions.getCaloriesOrMinutesOutOfString(savedTotalCalories));
//        savedCaloriesLeft = dataParts[5].split(": ")[1];
//        if(!savedCaloriesLeft.equals("") && !savedCaloriesLeft.equals(" "))
//            me.putExtra("caloriesLeft", multiUsageFunctions.getCaloriesOrMinutesOutOfString(savedCaloriesLeft));
//
//        meals[0] = savedBreakfast.split(":")[0];
//        meals[1] = savedLunch.split(":")[0];
//        meals[2] = savedDinner.split(":")[0];
//        me.putExtra("meals", meals);
//
//        updateMealsIfNeeded();
//    }
    //        if(viewId == btMealsMenu.getId())
//            sendToSelected(v);
//
//        if(viewId == btWriteMealsToExternalFile.getId())
//            write();
//
//        if(viewId == btReadMealsFromExternalFile.getId())
//            showFileData();

    //      From: ingredientsSelection:
//    package com.example.mydieter9005;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.media.MediaPlayer;
//import android.net.Uri;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.InputType;
//import android.text.TextWatcher;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.Toast;
//import android.widget.VideoView;
//
//import java.io.BufferedReader;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//
//    public class ingredientsSelection extends AppCompatActivity implements View.OnClickListener {
//
//        private VideoView videoView;
//        private MediaPlayer mediaPlayer;
//
//        Button btMultiIngredientsSelect, btFinishIngredientsSelection;
//        LinearLayout ingredientsSelectionLinearLayout;
//
//        FileAndDatabaseHelper fileAndDatabaseHelper;
//        Song activeSong = Song.getSongs().get(0);
//
//        ArrayList<Ingredient> ingredients;
//        IngredientListAdapter adapter;
//        Ingredient ingredientSelected;
//        EditText etFilterIngredients;
//        ListView lvIngredients;
//
//        Intent me;
//
//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_ingredients_selection);
//
//            me = getIntent();
//            if(me.hasExtra("activeSong"))
//                activeSong = (Song) me.getSerializableExtra("activeSong");
//
//            ingredientsSelectionLinearLayout = (LinearLayout) findViewById(R.id.ingredientsSelectionLinearLayout);
//            videoView = (VideoView) findViewById(R.id.ingredientsSelectionVideoView);
//
//            lvIngredients = (ListView) findViewById(R.id.lvIngredients);
//
//            btFinishIngredientsSelection = (Button) findViewById(R.id.btFinishIngredientsSelection);
//            btFinishIngredientsSelection.setOnClickListener(this);
//            btMultiIngredientsSelect = (Button) findViewById(R.id.btMultiIngredientsSelect);
//            btMultiIngredientsSelect.setOnClickListener(this);
//
//            etFilterIngredients = (EditText) findViewById(R.id.etFilterIngredients);
//            etFilterIngredients.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    (ingredientsSelection.this).adapter.getFilter().filter(s);
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {}
//            });
//
//            fileAndDatabaseHelper = new FileAndDatabaseHelper(this, me);
//            activeSong = fileAndDatabaseHelper.implementSettingsData();
//
//            initiateMediaPlayer();
//            initiateVideoPlayer();
//            setAdapters();
//        }
//
//        public void initiateIngredientsList(){
//            ArrayList<Ingredient> tempIngredients = Ingredient.getIngredientsList();
//            ingredients = new ArrayList<Ingredient>();
//
//            for(int i = 0; i < tempIngredients.size(); i++)
//                ingredients.add(new Ingredient(tempIngredients.get(i), 100));
//        }
//
//        public void setAdapters() {
//            initiateIngredientsList();
//
//            adapter = new IngredientListAdapter(this, ingredients);
//            lvIngredients.setAdapter(adapter);
//            lvIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Ingredient selectedItem = (Ingredient) parent.getItemAtPosition(position);
//
//                    showIngredientInfo(selectedItem);
//                }
//            });
//        }
//
//        public void showIngredientInfo(Ingredient ingredient){
//            AlertDialog ad;
//            AlertDialog.Builder adb;
//            adb = new AlertDialog.Builder(this);
//            adb.setTitle("Your ingredient: ");
//            adb.setMessage(ingredient.getIngredientInfo());
//            adb.setIcon(ingredient.getImgId());
//
//            adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    adapter = new IngredientListAdapter(ingredientsSelection.this, ingredients);
//                    lvIngredients.setAdapter(adapter);
//
//                    String filter = etFilterIngredients.getText().toString();
//                    if(!filter.equals(""))
//                        adapter.getFilter().filter(filter);
//                }
//            });
//
//            adb.setNegativeButton("Change grams", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    changeIngredientInfo(ingredient);
//                }
//            });
//
//            adb.setNeutralButton("Add", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    ingredientSelected = new Ingredient(ingredient);
//                    finishIngredientsSelection();
//                }
//            });
//
//            ad = adb.create();
//            ad.show();
//        }
//
//        public void finishIngredientsSelection(){
//            String cameFrom = me.getStringExtra("cameToIngredientsSelectionFrom");
//            me.putExtra("addedIngredient", ingredientSelected);
//
//            if(cameFrom.equals("customMeals"))
//                me.setClass(ingredientsSelection.this, customMeals.class);
//
//            startActivity(me);
//        }
//
//        public void changeIngredientInfo(Ingredient ingredient){
//            AlertDialog ad;
//            AlertDialog.Builder adb;
//            adb = new AlertDialog.Builder(this);
//            adb.setTitle("Set ingredient new gram value: ");
//            adb.setMessage(ingredient.toString());
//
//            final EditText etGetGrams = new EditText(this);
//            etGetGrams.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
//            etGetGrams.setText(ingredient.getGrams() + "");
//            etGetGrams.setHint("Put new grams here: ");
//            adb.setView(etGetGrams);
//
//            adb.setNeutralButton("Change grams", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    double newGrams = Double.parseDouble(etGetGrams.getText().toString());
//                    ingredient.setGrams(newGrams);
//                    showIngredientInfo(ingredient);
//                }
//            });
//
//            ad = adb.create();
//            ad.show();
//        }
//
//        public void initiateVideoPlayer(){
//            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.ingredients_selection_background_video);
//            videoView.setVideoURI(uri);
//
//            if(me.getBooleanExtra("useVideos", true))
//                videoView.start();
//
//            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mp) {
//                    mp.setLooping(true);
//                }
//            });
//        }
//
//        public void initiateMediaPlayer(){
//            mediaPlayer = MediaPlayer.create(ingredientsSelection.this, activeSong.getId());
//            mediaPlayer.setLooping(true);
//            if(me.getBooleanExtra("playMusic", true)){
//                mediaPlayer.start();
//            }
//        }
//
//        @Override
//        public boolean onCreateOptionsMenu(@NonNull Menu menu) {
//            MenuInflater inflater = getMenuInflater();
//            inflater.inflate(R.menu.main_menu, menu);
//            return true;
//        }
//
//        @Override
//        public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//            int itemID = item.getItemId();
//            if(itemID == R.id.sendToMusicMaster){
//                me.setClass(ingredientsSelection.this, musicMaster.class);
//                me.putExtra("cameToMusicMasterFrom", getLocalClassName());
//                startActivity(me);
//            }
//
//            if(itemID == R.id.sendToSettings){
//                me.setClass(ingredientsSelection.this, settingsSetter.class);
//                me.putExtra("cameToSettingsFrom", getLocalClassName());
//                startActivity(me);
//            }
//
//            if(itemID == R.id.sendToUserScreen){
//                me.setClass(ingredientsSelection.this, UserInfoScreen.class);
//                me.putExtra("cameToUserScreenFrom", getLocalClassName());
//                startActivity(me);
//            }
//            return super.onOptionsItemSelected(item);
//        }
//
//        @Override
//        protected void onPostResume() {
//            super.onPostResume();
//            videoView.resume();
//            if(!me.getBooleanExtra("useVideos", true)){
//                ingredientsSelectionLinearLayout.setBackground(getDrawable(R.drawable.ingredients_selection_background));
//                videoView.stopPlayback();
//            }
//            else
//                videoView.start();
//        }
//
//        @Override
//        protected void onRestart() {
//            videoView.start();
//            super.onRestart();
//        }
//
//        @Override
//        protected void onResume() {
//            super.onResume();
//            mediaPlayer.start();
//            if(!me.getBooleanExtra("playMusic", true)){
//                mediaPlayer.stop();
//            }
//        }
//
//        @Override
//        protected void onPause() {
//            videoView.suspend();
//            mediaPlayer.pause();
//            super.onPause();
//        }
//
//        @Override
//        protected void onDestroy() {
//            videoView.stopPlayback();
//            mediaPlayer.stop();
//            mediaPlayer.release();
//            super.onDestroy();
//        }
//
//        @Override
//        public void onClick(View v) {
//            int viewId = v.getId();
//
//            if(viewId == btFinishIngredientsSelection.getId())
//                finishIngredientsSelection();
//        }
//    }
//<?xml version="1.0" encoding="utf-8"?>
//<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
//    xmlns:app="http://schemas.android.com/apk/res-auto"
//    xmlns:tools="http://schemas.android.com/tools"
//    android:layout_width="match_parent"
//    android:layout_height="match_parent"
//    android:orientation="vertical"
//    tools:context=".ingredientsSelection">
//
//    <VideoView
//    android:id="@+id/ingredientsSelectionVideoView"
//    android:layout_width="match_parent"
//    android:layout_height="match_parent"
//    android:layout_alignParentTop="true"
//    android:layout_alignParentBottom="true"
//    android:layout_alignParentLeft="true"
//    android:layout_alignParentRight="true" />
//
//    <LinearLayout
//    android:id="@+id/ingredientsSelectionLinearLayout"
//    android:layout_width="match_parent"
//    android:layout_height="match_parent"
//    android:gravity="center"
//    android:orientation="vertical">
//
//        <TextView
//    android:layout_width="match_parent"
//    android:layout_height="wrap_content"
//    android:padding="10pt"
//    android:text="Choose your ingredient, from the option below: "
//    android:textSize="10pt" />
//
//        <EditText
//    android:id="@+id/etFilterIngredients"
//    android:layout_width="150pt"
//    android:layout_height="wrap_content"
//    android:ems="10"
//    android:hint="You can search here: "
//    android:inputType="textPersonName" />
//
//        <ListView
//    android:id="@+id/lvIngredients"
//    android:layout_width="match_parent"
//    android:layout_height="425dp"
//    android:layout_marginTop="2pt" />
//
//        <Button
//    android:id="@+id/btMultiIngredientsSelect"
//    android:layout_width="125pt"
//    android:layout_height="wrap_content"
//    android:layout_marginLeft="15pt"
//    android:layout_marginTop="10pt"
//    android:layout_marginRight="15pt"
//    android:layout_marginBottom="2pt"
//    android:text="Enable multi select" />
//
//        <Button
//    android:id="@+id/btFinishIngredientsSelection"
//    android:layout_width="125pt"
//    android:layout_height="wrap_content"
//    android:text="Finish" />
//
//    </LinearLayout>
//</RelativeLayout>

    //      From: CustomSelection:
    //package com.example.mydieter9005;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.media.MediaPlayer;
//import android.net.Uri;
//import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.Toast;
//import android.widget.VideoView;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import java.io.BufferedReader;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//
//public class customSelection extends AppCompatActivity implements View.OnClickListener {
//
//    private MediaPlayer mediaPlayer;
//    private VideoView videoView;
//
//    Button btSentToWorldSavedCustomMeals, btFinishCustomSelection;
//    ListView listView;
//
//    ArrayList<Meal> customMealsList;
//    MealListAdapter adapter;
//
//    String[] customMealsNames;
//    String cameFrom;
//
//    DailyMenu todayMenu = DailyMenu.getTodayMenu();
//    FileAndDatabaseHelper fileAndDatabaseHelper;
//    Song activeSong = Song.getSongs().get(0);
//
//    FirebaseDatabase recipesDb;
//    DatabaseReference databaseReference;
//    String userName = User.getCurrentUser().getUsername();
//
//    String fileName = "customMealsNames";
//    Intent me;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_custom_selection);
//
//        me = getIntent();
//        if(me.hasExtra("activeSong"))
//            activeSong = (Song) me.getSerializableExtra("activeSong");
//        cameFrom = me.getStringExtra("cameFrom");
//
//        listView = (ListView) findViewById(R.id.listViewCustom);
//        videoView = (VideoView) findViewById(R.id.customSelectionVideoView);
//
//        btSentToWorldSavedCustomMeals = (Button) findViewById(R.id.btSentToWorldSavedCustomMeals);
//        btSentToWorldSavedCustomMeals.setOnClickListener(this);
//        btFinishCustomSelection = (Button) findViewById(R.id.btFinishCustomSelection);
//        btFinishCustomSelection.setOnClickListener(this);
//
//        fileAndDatabaseHelper = new FileAndDatabaseHelper(this, me);
//        activeSong = fileAndDatabaseHelper.implementSettingsData();
//
//        initiateCustomMealsList();
//        initiateVideoPlayer();
//        initiateMediaPlayer();
//        checkIfAtLeastOneCustomMealAdded();
//    }
//
//    public void initiateCustomMealsList(){
//        customMealsList = new ArrayList<Meal>();
//        customMealsNames = getSavedCustomMealsNames();
//
//        for(int i = 0; i < customMealsNames.length - 1; i++)  // - 1 to get rid of the empty last element in this array.
//            customMealsList.add(new Meal(customMealsNames[i], getIngredientsFromFileByCustomMealName(customMealsNames[i])));
//
//        setListViewAdapter();
//    }
//
//    public String[] getSavedCustomMealsNames(){
//        String[] customMealsNames = fileAndDatabaseHelper.getFileData(fileName).split("\n");
//
//        for(int i = 1; i < customMealsNames.length; i++)  // Get rid of first line.
//            customMealsNames[i - 1] = customMealsNames[i];
//
//        customMealsNames[customMealsNames.length - 1] = "";
//        return customMealsNames;
//    }
//
//    public ArrayList<Ingredient> getIngredientsFromFileByCustomMealName(String mealName){
//        String[] mealIngredients = fileAndDatabaseHelper.getFileData("customMeal: " + mealName).split("\n");
//        ArrayList<Ingredient> ingredientsFound = new ArrayList<Ingredient>();
//
//        for(int i = 1; i < mealIngredients.length; i++){  // i = 1 to skip the custom meal name line.
//            String name = getName(mealIngredients[i]);
//            double grams = getGrams(mealIngredients[i]);
//
//            Ingredient ingredient = Ingredient.getIngredientByName(name);
//            ingredientsFound.add(new Ingredient(ingredient, grams));
//        }
//
//        return ingredientsFound;
//    }
//
//    public String getName(String nameAndGrams){  // Needed when get ingredient from file.
//        return nameAndGrams.split(": ")[0];
//    }
//
//    public double getGrams(String nameAndGrams){  // Needed when get ingredient from file.
//        return Double.parseDouble((nameAndGrams.split(": ")[1]).split(" ")[0]);
//    }
//
//    public void checkIfAtLeastOneCustomMealAdded(){
//        String[] savedCustomMealsNames = getSavedCustomMealsNames();
//
//        if(savedCustomMealsNames.length == 1){
//            notEvenOneCustomMealAdded();
//        }
//    }
//
//    public void notEvenOneCustomMealAdded(){
//        AlertDialog ad;
//        AlertDialog.Builder adb;
//        adb = new AlertDialog.Builder(this);
//        adb.setTitle("Custom meals not found!");
//        adb.setMessage("It's seems like you didn't saved any custom meal so far, give it a try and come back again.");
//        adb.setIcon(R.drawable.ic_food_icon);
//        adb.setCancelable(false);
//
//        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                me.setClass(customSelection.this, customMeals.class);
//                startActivity(me);
//            }
//        });
//
////        adb.setNegativeButton("World saved", new DialogInterface.OnClickListener() {
////            @Override
////            public void onClick(DialogInterface dialog, int which) {
////                sendToWorldSavedCustomSelection();
////            }
////        });
//
//        ad = adb.create();
//        ad.show();
//    }
//
//    public void setListViewAdapter(){
//        adapter = new MealListAdapter(this, customMealsList);
//        listView.setAdapter(adapter);
//
////        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////            @Override
////            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
////                Meal selectedItem = (Meal) adapterView.getItemAtPosition(i);
////
////                if(cameFrom.equals("breakfast"))
////                    todayMenu.addBreakfast(selectedItem);
////
////                if(cameFrom.equals("lunch"))
////                    todayMenu.addLunch(selectedItem);
////
////                if(cameFrom.equals("dinner"))
////                    todayMenu.addDinner(selectedItem);
////
////                me.setClass(customSelection.this, mealsMenu.class);
////                startActivity(me);
////            }
////        });
//
//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Meal selectedItem = (Meal) parent.getItemAtPosition(position);
//
//                showMealIngredientsInfo(selectedItem);
//                return true;
//            }
//        });
//    }
//
//    public void showMealInfo(Meal meal){
//        AlertDialog ad;
//        AlertDialog.Builder adb;
//        adb = new AlertDialog.Builder(this);
//        adb.setTitle("Your meal nutrition: ");
//        adb.setMessage(meal.getMealInfo());
//        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//
//        adb.setNegativeButton("Ingredients", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                showMealIngredientsInfo(meal);
//            }
//        });
//
//        adb.setNegativeButton("Publish", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                ArrayList<String> mealIngredientsInfo = new ArrayList<String>();
//                for(int i = 0; i < meal.getNeededIngredientsForMeal().size(); i++)
//                    mealIngredientsInfo.add(meal.getNeededIngredientsForMeal().get(i).toString());
//
//                recipesDb = FirebaseDatabase.getInstance();
//                databaseReference = recipesDb.getReference("recipes");
//                databaseReference.child(userName + " - " + meal.getName()).setValue(mealIngredientsInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        Toast.makeText(customSelection.this, "Successfully published.", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
//
//        ad = adb.create();
//        ad.show();
//    }
//
//    public void showMealIngredientsInfo(Meal meal){
//        AlertDialog ad;
//        AlertDialog.Builder adb;
//        adb = new AlertDialog.Builder(this);
//        adb.setTitle("Your meal ingredients: ");
//        String mealInfo = "";
//        for(Ingredient ingredient : meal.getNeededIngredientsForMeal())
//            mealInfo += ingredient.getName() + ": " + ingredient.getGrams() + " grams." + "\n";
//        adb.setMessage(mealInfo);
//        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//
//        adb.setNegativeButton("Nutrition", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                showMealInfo(meal);
//            }
//        });
//
//        adb.setNegativeButton("Publish", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                ArrayList<String> mealIngredientsInfo = new ArrayList<String>();
//                for(int i = 0; i < meal.getNeededIngredientsForMeal().size(); i++)
//                    mealIngredientsInfo.add(meal.getNeededIngredientsForMeal().get(i).toString());
//
//                recipesDb = FirebaseDatabase.getInstance();
//                databaseReference = recipesDb.getReference("recipes");
//                databaseReference.child(userName + " - " + meal.getName()).setValue(mealIngredientsInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        Toast.makeText(customSelection.this, "Successfully published.", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
//
//        ad = adb.create();
//        ad.show();
//    }
//
//    public void returnToCustomize(){
//        me.setClass(customSelection.this, customMeals.class);
//        startActivity(me);
//    }
//
//    public void initiateVideoPlayer(){
//        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.custom_selection_background_video);
//        videoView.setVideoURI(uri);
//
//        if(me.getBooleanExtra("useVideos", true))
//            videoView.start();
//
//        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                mp.setLooping(true);
//            }
//        });
//    }
//
//    public void initiateMediaPlayer(){
//        mediaPlayer = MediaPlayer.create(customSelection.this, activeSong.getId());
//        mediaPlayer.setLooping(true);
//        if(me.getBooleanExtra("playMusic", true)){
//            mediaPlayer.start();
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int itemID = item.getItemId();
//        if(itemID == R.id.sendToMusicMaster){
//            me.setClass(customSelection.this, musicMaster.class);
//            me.putExtra("cameToMusicMasterFrom", getLocalClassName());
//            startActivity(me);
//        }
//
//        if(itemID == R.id.sendToSettings){
//            me.setClass(customSelection.this, settingsSetter.class);
//            me.putExtra("cameToSettingsFrom", getLocalClassName());
//            startActivity(me);
//        }
//
//        if(itemID == R.id.sendToUserScreen){
//            me.setClass(customSelection.this, UserInfoScreen.class);
//            me.putExtra("cameToUserScreenFrom", getLocalClassName());
//            startActivity(me);
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    protected void onPostResume() {
//        super.onPostResume();
//        videoView.resume();
//        if(!me.getBooleanExtra("useVideos", true)){
//            findViewById(R.id.customSelectionLinearLayout).setBackground(getDrawable(R.drawable.custom_selection_background));
//            videoView.stopPlayback();
//        }
//        else
//            videoView.start();
//    }
//
//    @Override
//    protected void onRestart() {
//        videoView.start();
//        super.onRestart();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mediaPlayer.start();
//        if(!me.getBooleanExtra("playMusic", true)){
//            mediaPlayer.stop();
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        videoView.suspend();
//        mediaPlayer.pause();
//        super.onPause();
//    }
//
//    @Override
//    protected void onDestroy() {
//        videoView.stopPlayback();
//        mediaPlayer.stop();
//        mediaPlayer.release();
//        super.onDestroy();
//    }
//
//    @Override
//    public void onClick(View v) {
//        int viewId = v.getId();
//
//        if(viewId == btFinishCustomSelection.getId())
//            returnToCustomize();
//    }
//}
//    <?xml version="1.0" encoding="utf-8"?>
//<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
//    xmlns:app="http://schemas.android.com/apk/res-auto"
//    xmlns:tools="http://schemas.android.com/tools"
//    android:layout_width="match_parent"
//    android:layout_height="match_parent"
//    android:background="@drawable/breakfast_selection_background"
//    android:gravity="center"
//    android:orientation="vertical"
//    tools:context=".customSelection">
//
//    <VideoView
//    android:id="@+id/customSelectionVideoView"
//    android:layout_width="match_parent"
//    android:layout_height="match_parent"
//    android:layout_alignParentTop="true"
//    android:layout_alignParentBottom="true"
//    android:layout_alignParentLeft="true"
//    android:layout_alignParentRight="true" />
//
//    <LinearLayout
//    android:id="@+id/customSelectionLinearLayout"
//    android:layout_width="match_parent"
//    android:layout_height="match_parent"
//    android:orientation="vertical"
//    android:gravity="center">
//
//
//        <TextView
//    android:id="@+id/textView"
//    android:layout_width="match_parent"
//    android:layout_height="100dp"
//    android:padding="10pt"
//    android:text="Choose your custom meal, from the options below:"
//    android:textSize="10pt" />
//
//        <ListView
//    android:id="@+id/listViewCustom"
//    android:layout_width="match_parent"
//    android:layout_height="475dp" >
//        </ListView>
//
//        <Button
//    android:id="@+id/btSentToWorldSavedCustomMeals"
//    android:layout_width="125pt"
//    android:layout_height="wrap_content"
//    android:layout_marginTop="10pt"
//    android:layout_marginLeft="15pt"
//    android:layout_marginRight="15pt"
//    android:layout_marginBottom="2pt"
//    android:text="World saved recipes" />
//
//        <Button
//    android:id="@+id/btFinishCustomSelection"
//    android:layout_width="125pt"
//    android:layout_height="wrap_content"
//    android:layout_marginLeft="15pt"
//    android:layout_marginRight="15pt"
//    android:layout_marginBottom="2pt"
//    android:text="Return to customize" />
//    </LinearLayout>
//
//</RelativeLayout>


    //      From: CustomMeals:
//    package com.example.mydieter9005;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.media.MediaPlayer;
//import android.net.Uri;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.Toast;
//import android.widget.VideoView;
//
//import java.io.BufferedWriter;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.OutputStreamWriter;
//import java.util.ArrayList;
//
//public class customMeals extends AppCompatActivity implements View.OnClickListener {
//
//    private MediaPlayer mediaPlayer;
//    private VideoView videoView;
//
//    Button btSendToCustomSelection, btShowMealInfo, btFinishCustomize;
//    ListView lvCustomMealIngredients;
//    EditText etCustomMeal;
//
//    IngredientListAdapter adapter;
//    Ingredient addedIngredient;
//    Meal customMeal;
//    String cameFrom;
//
//    FileAndDatabaseHelper fileAndDatabaseHelper;
//    Song activeSong = Song.getSongs().get(0);
//
//    FileOutputStream fos;
//    OutputStreamWriter osw;
//    BufferedWriter bw;
//    String fileName = "customMealsNames";
//
//    Intent me;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_custom_meals);
//
//        me = getIntent();
//        if(me.hasExtra("activeSong"))
//            activeSong = (Song) me.getSerializableExtra("activeSong");
//
//        etCustomMeal = (EditText) findViewById(R.id.etCustomMeal);
//
//        if(me.hasExtra("addedIngredient")){
//            addedIngredient = (Ingredient) me.getSerializableExtra("addedIngredient");
//            me.removeExtra("addedIngredient");
//        }
//
//        if(me.hasExtra("savedMeal")) {
//            customMeal = (Meal) me.getSerializableExtra("savedMeal");
//            ArrayList<Ingredient> customMealSavedIngredients = new ArrayList<Ingredient>();
//            for(Ingredient ingredient : customMeal.getNeededIngredientsForMeal())
//                customMealSavedIngredients.add(new Ingredient(ingredient));
//            customMeal.setNeededIngredientsForMeal(customMealSavedIngredients, addedIngredient);
//            etCustomMeal.setText(customMeal.getName());
//        }
//        else
//            customMeal = new Meal("");
//
//        if(me.hasExtra("globalCustomMealToSave")){
//            customMeal = (Meal) me.getSerializableExtra("globalCustomMealToSave");
//            customMeal.setName(customMeal.getName().split(" - ")[1]);
//            ArrayList<Ingredient> customMealSavedIngredients = new ArrayList<Ingredient>();
//            for(Ingredient ingredient : customMeal.getNeededIngredientsForMeal())
//                customMealSavedIngredients.add(new Ingredient(ingredient));
//            etCustomMeal.setText(customMeal.getName());
//            me.removeExtra("globalCustomMealToSave");
//        }
//
//        cameFrom = me.getStringExtra("cameFrom");
//
//        videoView = (VideoView) findViewById(R.id.customVideoView);
//
//        btSendToCustomSelection = (Button) findViewById(R.id.btSendToCustomSelection);
//        btSendToCustomSelection.setOnClickListener(this);
//        btShowMealInfo = (Button) findViewById(R.id.btShowMealInfo);
//        btShowMealInfo.setOnClickListener(this);
//        btFinishCustomize = (Button) findViewById(R.id.btFinishCustomize);
//        btFinishCustomize.setOnClickListener(this);
//
//        lvCustomMealIngredients = (ListView) findViewById(R.id.lvCustomMealIngredients);
//
//        etCustomMeal.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                customMeal.setName(s.toString());
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {}
//        });
//
//        fileAndDatabaseHelper = new FileAndDatabaseHelper(this, me);
//        activeSong = fileAndDatabaseHelper.implementSettingsData();
//
//        setAdapters();
//        initiateVideoPlayer();
//        initiateMediaPlayer();
//    }
//
//    public void setAdapters() {
//        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
//        if(customMeal != null)
//            ingredients = customMeal.getNeededIngredientsForMeal();
//
//        adapter = new IngredientListAdapter(this, ingredients);
//        lvCustomMealIngredients.setAdapter(adapter);
//        lvCustomMealIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Ingredient selectedItem = (Ingredient) parent.getItemAtPosition(position);
//
//                showIngredientInfo(selectedItem);
//            }
//        });
//    }
//
//    public void showIngredientInfo(Ingredient ingredient){
//        AlertDialog ad;
//        AlertDialog.Builder adb;
//        adb = new AlertDialog.Builder(this);
//        adb.setTitle("Your ingredient: ");
//        adb.setMessage(ingredient.getIngredientInfo());
//        adb.setIcon(ingredient.getImgId());
//
//        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//
//        ad = adb.create();
//        ad.show();
//    }
//
//
//    public void showMealInfo(){
//        AlertDialog ad;
//        AlertDialog.Builder adb;
//        adb = new AlertDialog.Builder(this);
//        adb.setTitle("Your meal nutrition: ");
//        adb.setMessage(customMeal.getGrams() + " grams." + "\n" + customMeal.getProteins() + " proteins." + "\n" + customMeal.getFats() + " fats." + "\n" + customMeal.getCalories() + " calories.");
//        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//
//        adb.setNegativeButton("Ingredients", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                showMealIngredientsInfo();
//            }
//        });
//
//        ad = adb.create();
//        ad.show();
//    }
//
//    public void showMealIngredientsInfo(){
//        AlertDialog ad;
//        AlertDialog.Builder adb;
//        adb = new AlertDialog.Builder(this);
//        adb.setTitle("Your meal ingredients: ");
//        String mealInfo = "";
//        for(Ingredient ingredient : customMeal.getNeededIngredientsForMeal())
//            mealInfo += ingredient.getName() + ": " + ingredient.getGrams() + " grams." + "\n";
//        adb.setMessage(mealInfo);
//        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//
//        adb.setNegativeButton("Nutrition", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                showMealInfo();
//            }
//        });
//        ad = adb.create();
//        ad.show();
//    }
//
//    public void saveCustomMealInAFile(){
//        try {
//            fos = openFileOutput("customMeal: " + customMeal.getName(), Context.MODE_PRIVATE);
//            osw = new OutputStreamWriter(fos);
//            bw = new BufferedWriter(osw);
//
//            bw.write(customMeal.getName() + "\n");
//
//            bw.write(customMeal.getNeededIngredientsForMeal().get(0).toString());
//            for(int i = 1; i < customMeal.getNeededIngredientsForMeal().size(); i++)
//                bw.write("\n" + customMeal.getNeededIngredientsForMeal().get(i).toString());
//
//            bw.close();
//
//            boolean alreadyThere = false;
//            for(int i = 0; i < getSavedCustomMealsNames().length; i++){
//                if(customMeal.getName().equals(getSavedCustomMealsNames()[i]))
//                    alreadyThere = true;
//            }
//            if(!alreadyThere)
//                saveCustomMealNameInsideFile();
//            Toast.makeText(this, customMeal.getName() + " added.", Toast.LENGTH_SHORT).show();
//        }
//        catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public String[] getSavedCustomMealsNames(){
//        String[] customMealsNames = fileAndDatabaseHelper.getFileData(fileName).split("\n");
//
//        for(int i = 1; i < customMealsNames.length; i++)  // Get rid of first line.
//            customMealsNames[i - 1] = customMealsNames[i];
//
//        customMealsNames[customMealsNames.length - 1] = "";
//        return customMealsNames;
//    }
//
//    public boolean checkIfCustomMealIsOk(){
//        if(customMeal == null){
//            Toast.makeText(this, "You didn't create any meal yet.", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        else{
//            if(customMeal.getName().replaceAll(" ", "").equals("")){
//                Toast.makeText(this, "You didn't give your meal a name.", Toast.LENGTH_SHORT).show();
//                return false;
//            }
//            else{
//                if(customMeal.getNeededIngredientsForMeal().size() == 0){
//                    Toast.makeText(this, "You need to add at least one ingredient.", Toast.LENGTH_SHORT).show();
//                    return false;
//                }
//            }
//        }
//        return true;
//    }
//
//    public void saveCustomMealNameInsideFile(){
//        try {
//            fos = openFileOutput(fileName, Context.MODE_APPEND);
//            osw = new OutputStreamWriter(fos);
//            bw = new BufferedWriter(osw);
//
//            bw.write(customMeal.getName() + "\n");
//
//            bw.close();
//        }
//        catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void viewFinalMeal(){
//        AlertDialog ad;
//        AlertDialog.Builder adb;
//        adb = new AlertDialog.Builder(this);
//        adb.setTitle("Your meal is: ");
//        adb.setMessage(customMeal.getMealInfo());
//        adb.setNegativeButton("Change", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//
////        adb.setNeutralButton("Finish", new DialogInterface.OnClickListener() {
////            @Override
////            public void onClick(DialogInterface dialogInterface, int i) {
////                finishCustomize();
////            }
////        });
//
//        adb.setPositiveButton("Save", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                saveCustomMealInAFile();
//            }
//        });
//
//        ad = adb.create();
//        ad.show();
//    }
//
//    public void sendToCustomSelection(){
//        me.setClass(customMeals.this, customSelection.class);
//        startActivity(me);
//    }
//
//    public void initiateVideoPlayer(){
//        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.custom_background_video);
//        videoView.setVideoURI(uri);
//
//        if(me.getBooleanExtra("useVideos", true))
//            videoView.start();
//
//        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                mp.setLooping(true);
//            }
//        });
//    }
//
//    public void initiateMediaPlayer(){
//        mediaPlayer = MediaPlayer.create(customMeals.this, activeSong.getId());
//        mediaPlayer.setLooping(true);
//        if(me.getBooleanExtra("playMusic", true)){
//            mediaPlayer.start();
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.modify_and_custom_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int itemID = item.getItemId();
//        if(itemID == R.id.sendToIngredientsSelection){
//            me.setClass(customMeals.this, ingredientsSelection.class);
//            me.putExtra("cameToIngredientsSelectionFrom", getLocalClassName());
//            me.putExtra("savedMeal", customMeal);
//            startActivity(me);
//        }
//
//        if(itemID == R.id.sendToMusicMaster){
//            me.setClass(customMeals.this, musicMaster.class);
//            me.putExtra("cameToMusicMasterFrom", getLocalClassName());
//            startActivity(me);
//        }
//
//        if(itemID == R.id.sendToSettings){
//            me.setClass(customMeals.this, settingsSetter.class);
//            me.putExtra("cameToSettingsFrom", getLocalClassName());
//            startActivity(me);
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    protected void onPostResume() {
//        super.onPostResume();
//        videoView.resume();
//        if(!me.getBooleanExtra("useVideos", true)){
//            findViewById(R.id.customMealsLinearLayout).setBackground(getDrawable(R.drawable.custom_meals_background));
//            videoView.stopPlayback();
//        }
//        else
//            videoView.start();
//    }
//
//    @Override
//    protected void onRestart() {
//        videoView.start();
//        super.onRestart();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mediaPlayer.start();
//        if(!me.getBooleanExtra("playMusic", true)){
//            mediaPlayer.stop();
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        videoView.suspend();
//        mediaPlayer.pause();
//        super.onPause();
//    }
//
//    @Override
//    protected void onDestroy() {
//        videoView.stopPlayback();
//        mediaPlayer.stop();
//        mediaPlayer.release();
//        super.onDestroy();
//    }
//
//    @Override
//    public void onClick(View v) {
//        int viewId = v.getId();
//
//        if(viewId == btSendToCustomSelection.getId())
//            sendToCustomSelection();
//
//        if(viewId == btShowMealInfo.getId()) {
//            if(checkIfCustomMealIsOk())
//                showMealInfo();
//        }
//
//        if(viewId == btFinishCustomize.getId()) {
//            if(checkIfCustomMealIsOk())
//                viewFinalMeal();
//        }
//    }
//}
//    <?xml version="1.0" encoding="utf-8"?>
//<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
//    xmlns:app="http://schemas.android.com/apk/res-auto"
//    xmlns:tools="http://schemas.android.com/tools"
//    android:layout_width="match_parent"
//    android:layout_height="match_parent"
//    android:gravity="center|top"
//    android:orientation="vertical"
//    tools:context=".customMeals">
//
//    <VideoView
//    android:id="@+id/customVideoView"
//    android:layout_width="match_parent"
//    android:layout_height="match_parent"
//    android:layout_alignParentTop="true"
//    android:layout_alignParentBottom="true"
//    android:layout_alignParentLeft="true"
//    android:layout_alignParentRight="true" />
//
//    <LinearLayout
//    android:id="@+id/customMealsLinearLayout"
//    android:layout_width="match_parent"
//    android:layout_height="match_parent"
//    android:orientation="vertical"
//    android:gravity="center|top">
//
//        <EditText
//    android:id="@+id/etCustomMeal"
//    android:layout_width="match_parent"
//    android:layout_height="wrap_content"
//    android:layout_marginLeft="10pt"
//    android:layout_marginTop="10pt"
//    android:layout_marginRight="10pt"
//    android:hint="Enter custom meal name: "
//    android:inputType="textPersonName" />
//
//        <ListView
//    android:id="@+id/lvCustomMealIngredients"
//    android:layout_width="match_parent"
//    android:layout_height="425dp"
//    android:layout_marginTop="10pt"
//    android:layout_marginBottom="10pt"/>
//
//        <Button
//    android:id="@+id/btSendToCustomSelection"
//    android:layout_width="125pt"
//    android:layout_height="wrap_content"
//    android:text="Choose from saved" />
//
//        <Button
//    android:id="@+id/btShowMealInfo"
//    android:layout_width="125pt"
//    android:layout_height="wrap_content"
//    android:text="Show meal info" />
//
//        <Button
//    android:id="@+id/btFinishCustomize"
//    android:layout_width="125pt"
//    android:layout_height="wrap_content"
//    android:text="Finish" />
//    </LinearLayout>
//
//
//</RelativeLayout>


    //      From: finishMeals:
 //   package com.example.mydieter9005;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.media.MediaPlayer;
//import android.os.Bundle;
//import android.text.method.ScrollingMovementMethod;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import java.io.BufferedReader;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//
//public class finishMeals extends AppCompatActivity implements View.OnClickListener {
//
//    private MediaPlayer mediaPlayer;
//
//    Button btSendToIngredientsPickup, btFinishMeals;
//    TextView tvBreakfastInfo, tvLunchInfo, tvDinnerInfo;
//    ListView lvBreakfastIngredients, lvLunchIngredients, lvDinnerIngredients;
//
//    DailyMenu todayMenu = DailyMenu.getTodayMenu();
//    FileAndDatabaseHelper fileAndDatabaseHelper;
//    Song activeSong = Song.getSongs().get(0);
//
//    ArrayList<Ingredient> breakfastIngredientsList, lunchIngredientsList, dinnerIngredientsList;
//    ArrayList<String> breakfastFields, lunchFields, dinnerFields;
//    ArrayAdapter<String> breakfastIngredientsAdapter, lunchIngredientsAdapter, dinnerIngredientsAdapter;
//
//    Intent me;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_finish_meals);
//
//        me = getIntent();
//        if(me.hasExtra("activeSong"))
//            activeSong = (Song) me.getSerializableExtra("activeSong");
//
//        tvBreakfastInfo = (TextView) findViewById(R.id.tvBreakfastInfo);
//        tvBreakfastInfo.setMovementMethod(new ScrollingMovementMethod());
//        tvLunchInfo = (TextView) findViewById(R.id.tvLunchInfo);
//        tvLunchInfo.setMovementMethod(new ScrollingMovementMethod());
//        tvDinnerInfo = (TextView) findViewById(R.id.tvDinnerInfo);
//        tvDinnerInfo.setMovementMethod(new ScrollingMovementMethod());
//
//        lvBreakfastIngredients = (ListView) findViewById(R.id.lvBreakfastIngredients);
//        lvLunchIngredients = (ListView) findViewById(R.id.lvLunchIngredients);
//        lvDinnerIngredients = (ListView) findViewById(R.id.lvDinnerIngredients);
//
//        btSendToIngredientsPickup = (Button) findViewById(R.id.btSendToIngredientsPickup);
//        btSendToIngredientsPickup.setOnClickListener(this);
//        btFinishMeals = (Button) findViewById(R.id.btFinishMeals);
//        btFinishMeals.setOnClickListener(this);
//
//        fileAndDatabaseHelper = new FileAndDatabaseHelper(this, me);
//        activeSong = fileAndDatabaseHelper.implementSettingsData();
//
//        DailyMenu.saveDailyMenuIntoFile(DailyMenu.getTodayMenu(), finishMeals.this);
//        initiateMediaPlayer();
//        setAdapters();
//    }
//
//    public void setAdapters(){
//        initiateIngredientListsAndSetMealsNames();  // Must come before initiateListViewsFields()
//        initiateListViewsFields();
//
//        if(todayMenu.hasBreakfast()){
//            breakfastIngredientsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, breakfastFields);
//            lvBreakfastIngredients.setAdapter(breakfastIngredientsAdapter);
//            lvBreakfastIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Ingredient selectedItem = breakfastIngredientsList.get(position);
//
//                    showIngredientImage(selectedItem);
//                }
//            });
//        }
//
//        if(todayMenu.hasLunch()){
//            lunchIngredientsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lunchFields);
//            lvLunchIngredients.setAdapter(lunchIngredientsAdapter);
//            lvLunchIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Ingredient selectedItem = lunchIngredientsList.get(position);
//
//                    showIngredientImage(selectedItem);
//                }
//            });
//        }
//
//        if(todayMenu.hasDinner()){
//            dinnerIngredientsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dinnerFields);
//            lvDinnerIngredients.setAdapter(dinnerIngredientsAdapter);
//            lvDinnerIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Ingredient selectedItem = dinnerIngredientsList.get(position);
//
//                    showIngredientImage(selectedItem);
//                }
//            });
//        }
//    }
//
//    public void showIngredientImage(Ingredient ingredient){
//        AlertDialog ad;
//        AlertDialog.Builder adb;
//        adb = new AlertDialog.Builder(this);
//        adb.setTitle("Your ingredient: " + ingredient.getName());
//        adb.setIcon(R.drawable.ic_food_icon);
//
//        final ImageView ivIngredientImage = new ImageView(finishMeals.this);
//        ivIngredientImage.setImageResource(ingredient.getImgId());
//        adb.setView(ivIngredientImage);
//
//        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//
//        ad = adb.create();
//        ad.show();
//    }
//
//    public void initiateListViewsFields(){
//        Ingredient ingredient;
//        breakfastFields = new ArrayList<String>();
//        for(int i = 0; i < breakfastIngredientsList.size(); i++){
//            ingredient = breakfastIngredientsList.get(i);
//            breakfastFields.add(ingredient.getName() + ": " + ingredient.getGrams() + " grams.");
//        }
//
//        lunchFields = new ArrayList<String>();
//        for(int i = 0; i < lunchIngredientsList.size(); i++){
//            ingredient = lunchIngredientsList.get(i);
//            lunchFields.add(ingredient.getName() + ": " + ingredient.getGrams() + " grams.");
//        }
//
//        dinnerFields = new ArrayList<String>();
//        for(int i = 0; i < dinnerIngredientsList.size(); i++){
//            ingredient = dinnerIngredientsList.get(i);
//            dinnerFields.add(ingredient.getName() + ": " + ingredient.getGrams() + " grams.");
//        }
//    }
//
//    public void initiateIngredientListsAndSetMealsNames(){
////        breakfastIngredientsList = new ArrayList<Ingredient>();
////        if(todayMenu.hasBreakfast()) {
////            tvBreakfastInfo.setText("Breakfast: " + todayMenu.getUnitedBreakfastName() + " .");
////            breakfastIngredientsList = todayMenu.generateBreakfastIngredientsArray();
////        }
////
////        lunchIngredientsList = new ArrayList<Ingredient>();
////        if(todayMenu.hasLunch()) {
////            tvLunchInfo.setText("Lunch: " + todayMenu.getUnitedLunchName() + " .");
////            lunchIngredientsList = todayMenu.generateLunchIngredientsArray();
////        }
////
////        dinnerIngredientsList = new ArrayList<Ingredient>();
////        if(todayMenu.hasDinner()) {
////            tvDinnerInfo.setText("Dinner: " + todayMenu.getUnitedDinnerName() + " .");
////            dinnerIngredientsList = todayMenu.generateDinnerIngredientsArray();
////        }
//    }
//
//    public void sendToSeeAllIngredients(){
//        me.setClass(this, ingredientsPickup.class);
//        startActivity(me);
//    }
//
//    public void finishFinishMeals(){
//        me.setClass(this, MainActivity.class);
//        startActivity(me);
//    }
//
//    public void initiateMediaPlayer(){
//        mediaPlayer = MediaPlayer.create(finishMeals.this, activeSong.getId());
//        mediaPlayer.setLooping(true);
//        if(me.getBooleanExtra("playMusic", true)){
//            mediaPlayer.start();
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int itemID = item.getItemId();
//        if(itemID == R.id.sendToMusicMaster){
//            me.setClass(finishMeals.this, musicMaster.class);
//            me.putExtra("cameToMusicMasterFrom", getLocalClassName());
//            startActivity(me);
//        }
//
//        if(itemID == R.id.sendToSettings){
//            me.setClass(finishMeals.this, settingsSetter.class);
//            me.putExtra("cameToSettingsFrom", getLocalClassName());
//            startActivity(me);
//        }
//
//        if(itemID == R.id.sendToUserScreen){
//            me.setClass(finishMeals.this, UserInfoScreen.class);
//            me.putExtra("cameToUserScreenFrom", getLocalClassName());
//            startActivity(me);
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mediaPlayer.start();
//        if(!me.getBooleanExtra("playMusic", true)){
//            mediaPlayer.stop();
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        mediaPlayer.pause();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mediaPlayer.stop();
//        mediaPlayer.release();
//    }
//
//    @Override
//    public void onClick(View v) {
//        int viewId = v.getId();
//
//        if(viewId == btSendToIngredientsPickup.getId())
//            sendToSeeAllIngredients();
//
//        if(viewId == btFinishMeals.getId())
//            finishFinishMeals();
//    }
//}
//
//    <?xml version="1.0" encoding="utf-8"?>
//<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
//    xmlns:app="http://schemas.android.com/apk/res-auto"
//    xmlns:tools="http://schemas.android.com/tools"
//    android:layout_width="match_parent"
//    android:layout_height="match_parent"
//    android:orientation="vertical"
//    android:gravity="center"
//    tools:context=".finishMeals"
//    android:background="@drawable/finish_meals_background">
//
//
//    <androidx.gridlayout.widget.GridLayout
//    android:layout_width="125pt"
//    android:layout_height="80pt"
//    app:orientation="vertical"
//    android:layout_gravity="center"
//    android:layout_margin="10pt">
//
//        <HorizontalScrollView
//    android:layout_width="125pt"
//    android:layout_height="wrap_content"
//    app:layout_gravity="center">
//
//            <TextView
//    android:id="@+id/tvBreakfastInfo"
//    android:layout_width="wrap_content"
//    android:layout_height="10pt"
//    android:singleLine="true"
//    android:scrollHorizontally="true"
//    android:gravity="center|start"
//    android:text="Breakfast: None" />
//        </HorizontalScrollView>
//
//        <ListView
//    android:id="@+id/lvBreakfastIngredients"
//    android:layout_width="match_parent"
//    android:layout_height="70pt" />
//    </androidx.gridlayout.widget.GridLayout>
//
//
//    <androidx.gridlayout.widget.GridLayout
//    android:layout_width="125pt"
//    android:layout_height="80pt"
//    app:orientation="vertical"
//    android:layout_gravity="center"
//    android:layout_margin="10pt">
//
//        <HorizontalScrollView
//    android:layout_width="125pt"
//    android:layout_height="wrap_content"
//    app:layout_gravity="center">
//
//            <TextView
//    android:id="@+id/tvLunchInfo"
//    android:layout_width="wrap_content"
//    android:layout_height="15pt"
//    android:singleLine="true"
//    android:scrollHorizontally="true"
//    android:gravity="center|start"
//    android:text="Lunch: None" />
//        </HorizontalScrollView>
//
//        <ListView
//    android:id="@+id/lvLunchIngredients"
//    android:layout_width="match_parent"
//    android:layout_height="70pt" />
//    </androidx.gridlayout.widget.GridLayout>
//
//
//    <androidx.gridlayout.widget.GridLayout
//    android:layout_width="125pt"
//    android:layout_height="80pt"
//    app:orientation="vertical"
//    android:layout_gravity="center"
//    android:layout_margin="10pt">
//
//        <HorizontalScrollView
//    android:layout_width="125pt"
//    android:layout_height="wrap_content"
//    app:layout_gravity="center">
//
//            <TextView
//    android:id="@+id/tvDinnerInfo"
//    android:layout_width="wrap_content"
//    android:layout_height="15pt"
//    android:singleLine="true"
//    android:scrollHorizontally="true"
//    android:gravity="center|start"
//    android:text="Dinner: None" />
//        </HorizontalScrollView>
//
//        <ListView
//    android:id="@+id/lvDinnerIngredients"
//    android:layout_width="match_parent"
//    android:layout_height="70pt" />
//    </androidx.gridlayout.widget.GridLayout>
//
//    <Button
//    android:id="@+id/btSendToIngredientsPickup"
//    android:layout_width="100pt"
//    android:layout_height="wrap_content"
//    android:text="See all ingredients" />
//
//    <Button
//    android:id="@+id/btFinishMeals"
//    android:layout_width="100pt"
//    android:layout_height="wrap_content"
//    android:text="Finish" />
//
//</LinearLayout>
}