package com.example.mydieter9005;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class CustomMealsFragment extends Fragment implements View.OnClickListener {

    Button btSendToCustomSelection, btShowMealInfo, btSaveCustomMeal;
    LinearLayout customMealsAdditionsLinearLayout;
    ListView lvCustomMealIngredients;
    ImageButton ibtAddIngredient;
    TextView tvCustomSelection;
    EditText etCustomMeal;

    IngredientListAdapter customMealIngredientsAdapter;
    Meal customMeal;

    ArrayList<Meal> customMealsList;
    MealListAdapter customMealsAdapter;

    String[] customMealsNames;

    boolean isCustomSelection = false;

    FileAndDatabaseHelper fileAndDatabaseHelper;

    FoodSelectionFragment foodSelectionFragment;
    MealOverviewFragment mealOverviewFragment;

    FileOutputStream fos;
    OutputStreamWriter osw;
    BufferedWriter bw;
    String fileName = "customMealsNames";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_custom_meals, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etCustomMeal = (EditText) view.findViewById(R.id.etCustomMeal);

        if(DailyMenu.hasCustomMeal())
            customMeal = DailyMenu.getCustomMeal();
        else
            customMeal = new Meal("");

        btSendToCustomSelection = (Button) view.findViewById(R.id.btSendToCustomSelection);
        btSendToCustomSelection.setOnClickListener(this);
        btShowMealInfo = (Button) view.findViewById(R.id.btShowMealInfo);
        btShowMealInfo.setOnClickListener(this);
        btSaveCustomMeal = (Button) view.findViewById(R.id.btSaveCustomMeal);
        btSaveCustomMeal.setOnClickListener(this);

        ibtAddIngredient = (ImageButton) view.findViewById(R.id.ibtAddIngredient);
        ibtAddIngredient.setOnClickListener(this);

        tvCustomSelection = (TextView) view.findViewById(R.id.tvCustomSelection);

        customMealsAdditionsLinearLayout = (LinearLayout) view.findViewById(R.id.customMealsAdditionsLinearLayout);

        lvCustomMealIngredients = (ListView) view.findViewById(R.id.lvCustomMealIngredients);

        etCustomMeal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                customMeal.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        fileAndDatabaseHelper = new FileAndDatabaseHelper(getActivity(), null);

        if(!customMeal.getName().equals(""))
            etCustomMeal.setText(customMeal.getName());

        if(isCustomSelection)
            switchBetweenCustomMealsAndCustomSelection();
        setAdapters();
    }

    public void setAdapters() {
        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        if(customMeal != null)
            ingredients = customMeal.getNeededIngredientsForMeal();

        customMealIngredientsAdapter = new IngredientListAdapter(getActivity(), ingredients);
        lvCustomMealIngredients.setAdapter(customMealIngredientsAdapter);
        lvCustomMealIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Ingredient selectedItem = (Ingredient) parent.getItemAtPosition(position);

                ingredientOverviewAlertDialog(selectedItem);
            }
        });
    }

    public void switchBetweenCustomMealsAndCustomSelection(){
        isCustomSelection = !isCustomSelection;
        if(isCustomSelection){
            customMealsAdditionsLinearLayout.setVisibility(View.GONE);
            btSaveCustomMeal.setVisibility(View.GONE);
            btShowMealInfo.setVisibility(View.GONE);
            btSendToCustomSelection.setText("Create new custom meal");
            tvCustomSelection.setVisibility(View.VISIBLE);
            initiateCustomMealsList();
        }
        else{
            customMealsAdditionsLinearLayout.setVisibility(View.VISIBLE);
            btSaveCustomMeal.setVisibility(View.VISIBLE);
            btShowMealInfo.setVisibility(View.VISIBLE);
            btSendToCustomSelection.setText("Choose from saved");
            tvCustomSelection.setVisibility(View.GONE);
            setAdapters();
        }
    }

    public void initiateCustomMealsList(){
        customMealsList = new ArrayList<Meal>();
        customMealsNames = getSavedCustomMealsNames();

        for(int i = 0; i < customMealsNames.length - 1; i++)  // - 1 to get rid of the empty last element in this array.
            customMealsList.add(new Meal(customMealsNames[i], getIngredientsFromFileByCustomMealName(customMealsNames[i])));

        setListViewAdapter();
    }

    public ArrayList<Ingredient> getIngredientsFromFileByCustomMealName(String mealName){
        String[] mealIngredients = fileAndDatabaseHelper.getFileData("customMeal: " + mealName).split("\n");
        ArrayList<Ingredient> ingredientsFound = new ArrayList<Ingredient>();

        for(int i = 1; i < mealIngredients.length; i++){  // i = 1 to skip the custom meal name line.
            String name = getName(mealIngredients[i]);
            double grams = getGrams(mealIngredients[i]);

            Ingredient ingredient = Ingredient.getIngredientByName(name);
            ingredientsFound.add(new Ingredient(ingredient, grams));
        }

        return ingredientsFound;
    }

    public String getName(String nameAndGrams){  // Needed when get ingredient from file.
        return nameAndGrams.split(": ")[0];
    }

    public double getGrams(String nameAndGrams){  // Needed when get ingredient from file.
        return Double.parseDouble((nameAndGrams.split(": ")[1]).split(" ")[0]);
    }

    public boolean checkIfAtLeastOneCustomMealAdded(){
        String[] savedCustomMealsNames = getSavedCustomMealsNames();

        if(savedCustomMealsNames.length == 1){
            notEvenOneCustomMealAdded();
            return false;
        }
        return true;
    }

    public void notEvenOneCustomMealAdded(){
        AlertDialog ad;
        AlertDialog.Builder adb;
        adb = new AlertDialog.Builder(getActivity());
        adb.setTitle("Custom meals not found!");
        adb.setMessage("It's seems like you didn't saved any custom meal so far, give it a try and come back again.");
        adb.setIcon(R.drawable.ic_food_icon);
        adb.setCancelable(false);

        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        ad = adb.create();
        ad.show();
    }

    public void setListViewAdapter(){
        customMealsAdapter = new MealListAdapter(getActivity(), customMealsList);
        lvCustomMealIngredients.setAdapter(customMealsAdapter);

        lvCustomMealIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Meal selectedItem = (Meal) parent.getItemAtPosition(position);

                mealOverviewFragment = new MealOverviewFragment(selectedItem);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityFrameLayout, mealOverviewFragment).commit();
            }
        });
    }

    public void ingredientOverviewAlertDialog(Ingredient ingredient){
        AlertDialog ad;
        AlertDialog.Builder adb;
        adb = new AlertDialog.Builder(getActivity());

        Ingredient tmpIngredient = new Ingredient(ingredient);

        View customAlertDialog = LayoutInflater.from(getActivity()).inflate(R.layout.ingredient_overview_alert_dialog, null);
        TextView tvAlertDialogIngredientName = (TextView) customAlertDialog.findViewById(R.id.tvAlertDialogIngredientName);
        tvAlertDialogIngredientName.setText("Name: " + ingredient.getName());
        TextView tvAlertDialogIngredientCalories = (TextView) customAlertDialog.findViewById(R.id.tvAlertDialogIngredientCalories);
        tvAlertDialogIngredientCalories.setText("Calories: " + ingredient.getCalories());
        TextView tvAlertDialogIngredientProteins = (TextView) customAlertDialog.findViewById(R.id.tvAlertDialogIngredientProteins);
        tvAlertDialogIngredientProteins.setText("Proteins: " + ingredient.getProteins());
        TextView tvAlertDialogIngredientFats = (TextView) customAlertDialog.findViewById(R.id.tvAlertDialogIngredientFats);
        tvAlertDialogIngredientFats.setText("Fats: " + ingredient.getFats());

        ImageView ivAlertDialogIngredientImage = (ImageView) customAlertDialog.findViewById(R.id.ivAlertDialogIngredientImage);
        ivAlertDialogIngredientImage.setImageResource(ingredient.getImgId());

        LinearLayout alertDialogMealSelectionLinearLayout = (LinearLayout) customAlertDialog.findViewById(R.id.alertDialogMealSelectionLinearLayout);
        alertDialogMealSelectionLinearLayout.setVisibility(View.INVISIBLE);

        EditText etAlertDialogIngredientGrams = customAlertDialog.findViewById(R.id.etAlertDialogIngredientGrams);
        etAlertDialogIngredientGrams.setText((int) ingredient.getGrams() + "");
        etAlertDialogIngredientGrams.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!etAlertDialogIngredientGrams.getText().toString().equals("")){
                    tmpIngredient.setGrams(Integer.parseInt(etAlertDialogIngredientGrams.getText().toString()));
                    tvAlertDialogIngredientCalories.setText("Calories: " + tmpIngredient.getCalories());
                    tvAlertDialogIngredientProteins.setText("Proteins: " + tmpIngredient.getProteins());
                    tvAlertDialogIngredientFats.setText("Fats: " + tmpIngredient.getFats());
                }
                else{
                    tvAlertDialogIngredientCalories.setText("Calories: 0");
                    tvAlertDialogIngredientProteins.setText("Proteins: 0");
                    tvAlertDialogIngredientFats.setText("Fats: 0");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        adb.setView(customAlertDialog);
        ad = adb.create();

        Button btAlertDialogConfirmIngredient = (Button) customAlertDialog.findViewById(R.id.btAlertDialogConfirmIngredient);
        btAlertDialogConfirmIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean passTests = true;
                if(etAlertDialogIngredientGrams.getText().toString().replaceAll(" ", "").equals("")){
                    Toast.makeText(getActivity(), "Must enter grams value first.", Toast.LENGTH_SHORT).show();
                    passTests = false;
                }

                if(etAlertDialogIngredientGrams.getText().toString().equals("0") && passTests) {
                    Toast.makeText(getActivity(), "Must be more than 0 grams.", Toast.LENGTH_SHORT).show();
                    passTests = false;
                }

                if(passTests){
                    Toast.makeText(getActivity(), "Ingredient successfully changed.", Toast.LENGTH_SHORT).show();

                    int grams = Integer.parseInt(etAlertDialogIngredientGrams.getText().toString());
                    Ingredient tmpIngredient2 = new Ingredient(Ingredient.getIngredientByName(ingredient.getName()), grams);
                    DailyMenu.getCustomMeal().removeNeededIngredientForMeal(ingredient);
                    DailyMenu.getCustomMeal().addNeededIngredientForMeal(tmpIngredient2);

//                    tvMealOverviewMealGrams.setText("Grams: " + meal.getGrams());
//                    tvMealOverviewMealCalories.setText("Calories: " + meal.getCalories());
//                    tvMealOverviewMealProteins.setText("Proteins: " + meal.getProteins());
//                    tvMealOverviewMealFats.setText("Fats: " + meal.getFats());
                    customMealIngredientsAdapter.notifyDataSetChanged();

                    ad.cancel();
                }
            }
        });

        Button btAlertDialogCancelIngredient = (Button) customAlertDialog.findViewById(R.id.btAlertDialogCancelIngredient);
        btAlertDialogCancelIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.cancel();
            }
        });

        ad.show();
    }

    public void saveCustomMealInAFile(){
        try {
            fos = getActivity().openFileOutput("customMeal: " + customMeal.getName(), Context.MODE_PRIVATE);
            osw = new OutputStreamWriter(fos);
            bw = new BufferedWriter(osw);

            bw.write(customMeal.getName() + "\n");

            bw.write(customMeal.getNeededIngredientsForMeal().get(0).toString());
            for(int i = 1; i < customMeal.getNeededIngredientsForMeal().size(); i++)
                bw.write("\n" + customMeal.getNeededIngredientsForMeal().get(i).toString());

            bw.close();

            boolean alreadyThere = false;
            for(int i = 0; i < getSavedCustomMealsNames().length; i++){
                if(customMeal.getName().equals(getSavedCustomMealsNames()[i]))
                    alreadyThere = true;
            }
            if(!alreadyThere)
                saveCustomMealNameInsideFile();
            Toast.makeText(getActivity(), customMeal.getName() + " added.", Toast.LENGTH_SHORT).show();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] getSavedCustomMealsNames(){
        String[] customMealsNames = fileAndDatabaseHelper.getFileData(fileName).split("\n");

        for(int i = 1; i < customMealsNames.length; i++)  // Get rid of first line.
            customMealsNames[i - 1] = customMealsNames[i];

        customMealsNames[customMealsNames.length - 1] = "";
        return customMealsNames;
    }

    public void saveAndClearCustomMeal(){
        if(checkIfCustomMealIsOk()) {
            saveCustomMealInAFile();

            DailyMenu.saveCustomMeal(null);
            customMeal = new Meal("");
            etCustomMeal.setText("");
            setAdapters();
        }
    }

    public boolean checkIfCustomMealIsOk(){
        if(customMeal == null){
            Toast.makeText(getActivity(), "You didn't create any meal yet.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            if(customMeal.getName().replaceAll(" ", "").equals("")){
                Toast.makeText(getActivity(), "You didn't give your meal a name.", Toast.LENGTH_SHORT).show();
                return false;
            }
            else{
                if(customMeal.getNeededIngredientsForMeal().size() == 0){
                    Toast.makeText(getActivity(), "You need to add at least one ingredient.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        return true;
    }

    public void saveCustomMealNameInsideFile(){
        try {
            fos = getActivity().openFileOutput(fileName, Context.MODE_APPEND);
            osw = new OutputStreamWriter(fos);
            bw = new BufferedWriter(osw);

            bw.write(customMeal.getName() + "\n");

            bw.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        if(viewId == btShowMealInfo.getId()) {
            if(checkIfCustomMealIsOk()) {
                mealOverviewFragment = new MealOverviewFragment("CustomMealsFragment", DailyMenu.getCustomMeal());
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityFrameLayout, mealOverviewFragment).commit();
            }
        }

        if(viewId == btSendToCustomSelection.getId()){
            if(checkIfAtLeastOneCustomMealAdded()) {
                DailyMenu.saveCustomMeal(customMeal);
                switchBetweenCustomMealsAndCustomSelection();
            }
        }

        if(viewId == ibtAddIngredient.getId()){
            DailyMenu.saveCustomMeal(customMeal);
            foodSelectionFragment = new FoodSelectionFragment("CustomMealsFragment");
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityFrameLayout, foodSelectionFragment).commit();
        }

        if(viewId == btSaveCustomMeal.getId())
            saveAndClearCustomMeal();
    }
}