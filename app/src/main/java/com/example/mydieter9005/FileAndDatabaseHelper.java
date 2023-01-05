package com.example.mydieter9005;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileAndDatabaseHelper {
    private static SQLiteDatabase sqdb;
    private static DBHelper my_db;
    private Context context;
    private Intent me;

    private FileInputStream is;
    private InputStreamReader isr;
    private BufferedReader br;

    public FileAndDatabaseHelper(Context context, Intent me){
        FileAndDatabaseHelper.my_db = new DBHelper(context);
        this.context = context;
        this.me = me;
    }

    public String getFileData(String fileName){
        String currentLine = "", allData = "";
        try{
            is = context.openFileInput(fileName);
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

    public Song implementSettingsData(){
        Song activeSong = Song.getActiveSong();
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
            return activeSong;
        }
        return activeSong;
    }

    public void updateUserPasswordInLocalDatabase(User user, String newPassword){
        User.setCurrentUser(user);

        sqdb=my_db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.PASSWORD, newPassword);

        sqdb.update(DBHelper.TABLE_NAME, cv,DBHelper.USERNAME+"=?", new String[]{user.getUsername()});
        sqdb.close();

        Toast.makeText(context, "Password successfully changed.", Toast.LENGTH_SHORT).show();
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

    public void addUserToDatabase(User user) {
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

    public boolean isUserAlreadyExists(String username) {
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

    public boolean isDatabaseEmpty() {
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
}
