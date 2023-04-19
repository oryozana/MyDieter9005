package com.example.mydieter9005;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "local_users.db";//הכרזה על בסיס נתונים
    private static final int DATABASE_VERSION = 1;//גרסה אצלנו תמיד 1 כי זו גרסה ראשונה של האפליקצייה

    //הצהרה על שדות הטבלה
    public static final String TABLE_NAME = "all_users";
    public static final String USERNAME = "Username";
    public static final String PASSWORD = "Password";
    public static final String EMAIL = "Email";
    public static final String STARTING_WEIGHT = "Starting_weight";
    public static final String WEIGHT = "Weight";
    public static final String TARGET_CALORIES = "Target_calories";
    public static final String TARGET_PROTEIN = "Target_protein";
    public static final String TARGET_FATS = "Target_fats";
    public static final String PROFILE_PICTURE_ID = "Profile_picture_id";
    public static final String DAILY_MENUS = "Daily_menus";

    //מחרוזות שבאמצעותן נריץ שאילתות SQL ליצירת טבלה או עדכון ומחיקת טבלה
    String SQL_Create="";
    String SQL_Delete="";

    //בנאי של המחלקה DBHelper
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //בפעולה און קריאייט יוצרים את הטבלה
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        SQL_Create="CREATE TABLE "+TABLE_NAME+" (";
        SQL_Create+=USERNAME+" TEXT, ";
        SQL_Create+=PASSWORD+" TEXT, ";
        SQL_Create+=EMAIL+" TEXT, ";
        SQL_Create+=STARTING_WEIGHT+" TEXT, ";
        SQL_Create+=WEIGHT+" TEXT, ";
        SQL_Create+=TARGET_CALORIES+" INTEGER, ";
        SQL_Create+=TARGET_PROTEIN+" INTEGER, ";
        SQL_Create+=TARGET_FATS+" INTEGER, ";
        SQL_Create+=PROFILE_PICTURE_ID+" INTEGER, ";
        SQL_Create+=DAILY_MENUS+" TEXT);";
        sqLiteDatabase.execSQL(SQL_Create);
    }

    //כותבים אבל לא נשתמש שאילתא שתאפשר העברת נתונים הקיימים באפליקציה כשיש עדכון גרסה
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        SQL_Delete = "DROP TABLE IF EXISTS "+ TABLE_NAME;
        sqLiteDatabase.execSQL(SQL_Delete);
        onCreate(sqLiteDatabase);
    }
}
