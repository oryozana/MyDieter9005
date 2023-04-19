package com.example.mydieter9005;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private static ArrayList<User> localUsers;
    private static User PrimaryUser;
    private static User currentUser;
    private String username;
    private String password;
    private String email;
    private final double startingWeight;
    private double weight;
    private Plan currentPlan;
    private String userDailyMenus;
    private int profilePictureId;

    public User(DataSnapshot dataSnapshot){
        this.username = dataSnapshot.getKey();
        this.password = String.valueOf(dataSnapshot.child("password").getValue());

        double targetCalories = Double.parseDouble(String.valueOf(dataSnapshot.child("currentPlan").child("targetCalories").getValue()));
        double targetProteins = Double.parseDouble(String.valueOf(dataSnapshot.child("currentPlan").child("targetProteins").getValue()));
        double targetFats = Double.parseDouble(String.valueOf(dataSnapshot.child("currentPlan").child("targetFats").getValue()));
        this.currentPlan = new Plan(targetCalories, targetProteins, targetFats);

        this.email = String.valueOf(dataSnapshot.child("email").getValue());
        this.startingWeight = Double.parseDouble(String.valueOf(dataSnapshot.child("startingWeight").getValue()));
        this.weight = startingWeight;

        this.profilePictureId = Integer.parseInt(String.valueOf(dataSnapshot.child("profilePictureId").getValue()));
        this.userDailyMenus = String.valueOf(dataSnapshot.child("dailyMenus").getValue());
    }

    public User(String username, String password, String email, double startingWeight, Plan currentPlan, int profilePictureId, String dailyMenus){
        this.username = username;
        this.password = password;
        this.email = email;
        this.startingWeight = startingWeight;
        this.weight = startingWeight;
        this.currentPlan = currentPlan;
        this.profilePictureId = profilePictureId;
        this.userDailyMenus = dailyMenus;
    }

    public User(String username, String password, String email, String startingWeight, String targetCalories, String targetProteins, String targetFats, String profilePictureId, String dailyMenus){
        this.username = username;
        this.password = password;
        this.email = email;
        this.startingWeight = Double.parseDouble(startingWeight);
        this.weight = Double.parseDouble(startingWeight);
        this.currentPlan = new Plan(targetCalories, targetProteins, targetFats);
        this.profilePictureId = Integer.parseInt(profilePictureId);
        this.userDailyMenus = dailyMenus;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getStartingWeight() {
        return startingWeight;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Plan getCurrentPlan() {
        return currentPlan;
    }

    public void setCurrentPlan(Plan currentPlan) {
        this.currentPlan = currentPlan;
    }

    public int getProfilePictureId() {
        return profilePictureId;
    }

    public void setProfilePictureId(int profilePictureId) {
        this.profilePictureId = profilePictureId;
    }

    public static ArrayList<User> obtainLocalUsers() {
        return localUsers;
    }

    public static void setLocalUsers(ArrayList<User> localUsers) {
        User.localUsers = localUsers;
    }

    public static User obtainPrimaryUser() {
        return PrimaryUser;
    }

    public static void setPrimaryUser(User primaryUser) {
        PrimaryUser = primaryUser;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        User.currentUser = currentUser;
    }

    public String getUserDailyMenus() {
        return userDailyMenus;
    }

    public void setUserDailyMenus(String userDailyMenus) {
        this.userDailyMenus = userDailyMenus;
    }

    public void downloadUserDailyMenusFromTemporaryFile(Context context){
        DailyMenu.fillMissingDailyMenusDates(context);
        this.userDailyMenus = DailyMenu.getTodayMenu().generateDailyMenuDescriptionForFiles();

        for(DailyMenu dailyMenu : DailyMenu.getDailyMenusFromFile(context)){
            if(!dailyMenu.getDate().equals(DailyMenu.getTodayMenu().getDate()))
                this.userDailyMenus += dailyMenu.generateDailyMenuDescriptionForFiles();
        }
    }

    public void uploadUserDailyMenusIntoTemporaryFile(Context context){
        if(this.userDailyMenus.equals("null")) {
            this.userDailyMenus = DailyMenu.getTodayMenu().generateEmptyDailyMenuDescriptionForFiles();
        }
        Toast.makeText(context, this.userDailyMenus + "      gegusbvusvo", Toast.LENGTH_SHORT).show();

        String[] dataParts = this.userDailyMenus.split("       ");  // Just upload it into the file
        DailyMenu.restartDailyMenusFile(context);

        String tmp = "";
        for(String data : dataParts)
            tmp += data;
        Toast.makeText(context, tmp, Toast.LENGTH_SHORT).show();

        for(int i = 0; i < dataParts.length; i++) {
            if(!dataParts[i].replaceAll(" ", "").equals(""))
                DailyMenu.saveDailyMenuIntoFile(generateDailyMenuFromDescription(dataParts[i]), context);
        }
    }

    public DailyMenu generateDailyMenuFromDescription(String data){
        String[] dataParts, breakfastDataParts, lunchDataParts, dinnerDataParts;
        String breakfastData, lunchData, dinnerData, date = "";

        ArrayList<Food> breakfastFood = new ArrayList<Food>();
        ArrayList<Food> lunchFood = new ArrayList<Food>();
        ArrayList<Food> dinnerFood = new ArrayList<Food>();

        dataParts = data.split("DailyMenu \\{");
        dataParts = dataParts[1].split(" \\}");
        data = dataParts[0];

        dataParts = data.split(" breakfast \\( ");
        dataParts = dataParts[1].split(" \\)");
        breakfastData = dataParts[0];

        dataParts = data.split(" lunch \\( ");
        dataParts = dataParts[1].split(" \\)");
        lunchData = dataParts[0];

        dataParts = data.split(" dinner \\( ");
        dataParts = dataParts[1].split(" \\)");
        dinnerData = dataParts[0];

        date = data.split(" date: ")[1];

        if(!breakfastData.equals("null")){
            breakfastDataParts = breakfastData.split("     ");
            for (String breakfastDataPart : breakfastDataParts) {
                if (breakfastDataPart.split(" \\[ ")[0].equals("Meal"))
                    breakfastFood.add(Meal.generateMealObjectFromFileDescription(breakfastDataPart + " ]"));
                if (breakfastDataPart.split(" \\[ ")[0].equals("Ingredient"))
                    breakfastFood.add(Ingredient.generateIngredientObjectFromFileDescription(breakfastDataPart + " ]"));
            }
        }

        if(!lunchData.equals("null")){
            lunchDataParts = lunchData.split("     ");
            for (String lunchDataPart : lunchDataParts) {
                if (lunchDataPart.split(" \\[ ")[0].equals("Meal"))
                    lunchFood.add(Meal.generateMealObjectFromFileDescription(lunchDataPart));
                if (lunchDataPart.split(" \\[ ")[0].equals("Ingredient"))
                    lunchFood.add(Ingredient.generateIngredientObjectFromFileDescription(lunchDataPart));
            }
        }

        if(!dinnerData.equals("null")){
            dinnerDataParts = dinnerData.split("     ");
            for (String dinnerDataPart : dinnerDataParts) {
                if (dinnerDataPart.split(" \\[ ")[0].equals("Meal"))
                    dinnerFood.add(Meal.generateMealObjectFromFileDescription(dinnerDataPart));
                if (dinnerDataPart.split(" \\[ ")[0].equals("Ingredient"))
                    dinnerFood.add(Ingredient.generateIngredientObjectFromFileDescription(dinnerDataPart));
            }
        }

        DailyMenu dailyMenu = new DailyMenu(date);
        for(int i = 0; i < breakfastFood.size(); i++)
            dailyMenu.addBreakfast(breakfastFood.get(i));

        for(int i = 0; i < lunchFood.size(); i++)
            dailyMenu.addLunch(lunchFood.get(i));

        for(int i = 0; i < dinnerFood.size(); i++)
            dailyMenu.addDinner(dinnerFood.get(i));

        return dailyMenu;
    }

//    public static void saveDailyMenuIntoUser(Context context) {
//        FileAndDatabaseHelper fileAndDatabaseHelper = new FileAndDatabaseHelper(context, null);
//        String[] dataParts = fileAndDatabaseHelper.getFileData("")
//    }

//    public ArrayList<DailyMenu> transferStringIntoDailyMenus(String dailyMenus){
//        DailyMenu.generateDailyMenuObjectFromFile(dailyMenus);
//    }
//
//    public static void saveDailyMenuIntoUser() {
//
//    }

//    public static void removeDailyMenuDuplicationsAndAddAnotherOne(DailyMenu dailyMenu){
//        ArrayList<DailyMenu> tmpDailyMenus = new ArrayList<DailyMenu>();
//        boolean found;
//
//        for(int i = 0; i < dailyMenus.size(); i++){
//            found = false;
//            for(int j = i + 1; j < dailyMenus.size(); j++){
//                if(dailyMenus.get(i).date.equals(dailyMenus.get(j).date) || dailyMenus.get(i).date.equals(dailyMenu.date))
//                    found = true;
//            }
//
//            if(!found)
//                tmpDailyMenus.add(dailyMenus.get(i));
//        }
//
//        tmpDailyMenus.add(dailyMenu);
//        dailyMenus = tmpDailyMenus;
//    }
//
//    public boolean hasTodayMenu(String currentDate){
//        for(int i = 0; i < this.userDailyMenus.size(); i++){
//            if(this.userDailyMenus.get(i).getDate().equals(currentDate))
//                return true;
//        }
//        return false;
//    }
//
//    public DailyMenu getTodayMenu(String currentDate){
//        if(hasTodayMenu(currentDate)){
//            for(int i = 0; i < this.userDailyMenus.size(); i++){
//                if(this.userDailyMenus.get(i).getDate().equals(currentDate))
//                    return this.userDailyMenus.get(i);
//            }
//        }
//        return null;
//    }
//
//    public void addDailyMenu(DailyMenu dailyMenu, Context context) {
//        boolean found = false;
//
//        for(int i = 0; i < this.userDailyMenus.size(); i++){
//            if(this.userDailyMenus.get(i).getDate().equals(dailyMenu.getDate()) && !found){
//                this.userDailyMenus.remove(i);
//                this.userDailyMenus.add(dailyMenu);
//                found = true;
//            }
//        }
//
//        if(this.userDailyMenus.size() == 0)
//            this.userDailyMenus.add(dailyMenu);
//
//        String dailyMenus = "";
//        for(int i = 0; i < this.userDailyMenus.size(); i++)
//            dailyMenus += this.userDailyMenus.get(i).generateDailyMenuDescriptionForFiles();
//
//        FileAndDatabaseHelper fileAndDatabaseHelper = new FileAndDatabaseHelper(context, null);
//        fileAndDatabaseHelper.updateUserDailyMenusInLocalDatabase(dailyMenus);
//    }

    @Override
    public String toString() {
        return this.username + " , " + this.password + " , " + this.email + " , " + this.startingWeight + " , " + this.weight + " , " + this.currentPlan.toString();
    }
}
