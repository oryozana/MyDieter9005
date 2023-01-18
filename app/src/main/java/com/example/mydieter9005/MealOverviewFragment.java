package com.example.mydieter9005;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MealOverviewFragment extends Fragment implements View.OnClickListener {

    private String fromWhere;
    private Meal meal, tmpMeal;

    LinearLayout mealOverviewMealSelectorLinearLayout;

    HomeFragment homeFragment = new HomeFragment();
    FoodSelectionFragment foodSelectionFragment = new FoodSelectionFragment();
    CustomMealsFragment customMealsFragment = new CustomMealsFragment();

    TextView tvMealOverviewMealName, tvMealOverviewMealGrams;
    TextView tvMealOverviewMealCalories, tvMealOverviewMealProteins, tvMealOverviewMealFats;

    Button btMealOverviewConfirmMeal, btMealOverviewCancelMeal;

    ListView lvMealOverviewIngredients;
    IngredientListAdapter ingredientsAdapter;

    Spinner sMealOverviewSelectMeal;

    String mealType;
    boolean actNormal = true;

    public MealOverviewFragment(String fromWhere, Meal meal){
        this.fromWhere = fromWhere;
        this.tmpMeal = new Meal(meal.getName(), meal.getNeededIngredientsForMeal2());
        this.meal = meal;
    }

    public MealOverviewFragment(Meal meal, String mealType){
        fromWhere = "HomeFragment";
        this.mealType = mealType;
        this.tmpMeal = new Meal(meal.getName(), meal.getNeededIngredientsForMeal2());
        this.meal = meal;
    }

    public MealOverviewFragment(Meal customMeal){
        fromWhere = "CustomMealsFragment";
        this.tmpMeal = new Meal(customMeal.getName(), customMeal.getNeededIngredientsForMeal2());
        this.meal = customMeal;
        actNormal = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meal_overview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvMealOverviewMealName = (TextView) view.findViewById(R.id.tvMealOverviewMealName);
        tvMealOverviewMealName.setText("Name: " + tmpMeal.getName());

        tvMealOverviewMealGrams = (TextView) view.findViewById(R.id.tvMealOverviewMealGrams);
        tvMealOverviewMealGrams.setText("Grams: " + tmpMeal.getGrams());

        tvMealOverviewMealCalories = (TextView) view.findViewById(R.id.tvMealOverviewMealCalories);
        tvMealOverviewMealCalories.setText("Calories: " + tmpMeal.getCalories());

        tvMealOverviewMealProteins = (TextView) view.findViewById(R.id.tvMealOverviewMealProteins);
        tvMealOverviewMealProteins.setText("Proteins: " + tmpMeal.getProteins());

        tvMealOverviewMealFats = (TextView) view.findViewById(R.id.tvMealOverviewMealFats);
        tvMealOverviewMealFats.setText("Fats: " + tmpMeal.getFats());

        btMealOverviewConfirmMeal = (Button) view.findViewById(R.id.btMealOverviewConfirmMeal);
        btMealOverviewConfirmMeal.setOnClickListener(this);
        btMealOverviewCancelMeal = (Button) view.findViewById(R.id.btMealOverviewCancelMeal);
        btMealOverviewCancelMeal.setOnClickListener(this);

        lvMealOverviewIngredients = (ListView) view.findViewById(R.id.lvMealOverviewIngredients);

        mealOverviewMealSelectorLinearLayout = (LinearLayout) view.findViewById(R.id.mealOverviewMealSelectorLinearLayout);
        if(fromWhere.equals("CustomMealsFragment") && actNormal)
            mealOverviewMealSelectorLinearLayout.setVisibility(View.INVISIBLE);
        if(fromWhere.equals("HomeFragment"))
            mealOverviewMealSelectorLinearLayout.setVisibility(View.INVISIBLE);

        sMealOverviewSelectMeal = (Spinner) view.findViewById(R.id.sMealOverviewSelectMeal);
        String[] selectMealOptions = new String[]{"Breakfast", "Lunch", "Dinner"};
        ArrayAdapter<String> alertDialogSelectedMealAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, selectMealOptions);
        sMealOverviewSelectMeal.setAdapter(alertDialogSelectedMealAdapter);

        setIngredientListViewAdapters();
    }

    public void setIngredientListViewAdapters() {
//        ArrayList<Ingredient> neededIngredients = new ArrayList<Ingredient>();
//        for(int i = 0; i < meal.getNeededIngredientsForMeal().size(); i++)
//            neededIngredients.add(new Ingredient(meal.getNeededIngredientsForMeal().get(i)));

        ingredientsAdapter = new IngredientListAdapter(getActivity(), tmpMeal.getNeededIngredientsForMeal());
        lvMealOverviewIngredients.setAdapter(ingredientsAdapter);
        lvMealOverviewIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Ingredient selectedItem = (Ingredient) parent.getItemAtPosition(position);

                ingredientOverviewAlertDialog(selectedItem);
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

                    ingredient.setGrams(tmpIngredient.getGrams());
                    int grams = Integer.parseInt(etAlertDialogIngredientGrams.getText().toString());
                    Ingredient tmpIngredient = new Ingredient(Ingredient.getIngredientByName(ingredient.getName()), grams);
                    tmpMeal.removeNeededIngredientForMeal(ingredient);
                    tmpMeal.addNeededIngredientForMeal(tmpIngredient);

                    tvMealOverviewMealGrams.setText("Grams: " + tmpMeal.getGrams());
                    tvMealOverviewMealCalories.setText("Calories: " + tmpMeal.getCalories());
                    tvMealOverviewMealProteins.setText("Proteins: " + tmpMeal.getProteins());
                    tvMealOverviewMealFats.setText("Fats: " + tmpMeal.getFats());
                    ingredientsAdapter.notifyDataSetChanged();

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

    public void addMealIntoTodayMenu(){
        boolean passTests = true;

        if(sMealOverviewSelectMeal.getSelectedItem() == null) {
            Toast.makeText(getActivity(), "Choose breakfast, lunch or dinner.", Toast.LENGTH_SHORT).show();
            passTests = false;
        }

        if(passTests){
            Toast.makeText(getActivity(), "Meal successfully added.", Toast.LENGTH_SHORT).show();
            String selectedMeal = sMealOverviewSelectMeal.getSelectedItem().toString();
            DailyMenu.getTodayMenu().addMealByMealName(selectedMeal, tmpMeal);
            DailyMenu.saveDailyMenuIntoFile(DailyMenu.getTodayMenu(), getActivity());
        }
    }

    public void updateMeal(){
        Toast.makeText(getActivity(), "Meal successfully changed.", Toast.LENGTH_SHORT).show();
        DailyMenu.getTodayMenu().replaceMeal(tmpMeal, mealType);
        DailyMenu.saveDailyMenuIntoFile(DailyMenu.getTodayMenu(), getActivity());
    }

    public void updateCustomMeal(){
        Toast.makeText(getActivity(), "Meal successfully changed.", Toast.LENGTH_SHORT).show();
        DailyMenu.saveCustomMeal(tmpMeal);
        DailyMenu.saveDailyMenuIntoFile(DailyMenu.getTodayMenu(), getActivity());
    }

    public void exitMealOverview(){
        if(fromWhere.equals("HomeFragment"))
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityFrameLayout, homeFragment).commit();
        if(fromWhere.equals("FoodSelectionFragment"))
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityFrameLayout, foodSelectionFragment).commit();
        if(fromWhere.equals("CustomMealsFragment"))
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityFrameLayout, customMealsFragment).commit();
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        if(viewId == btMealOverviewCancelMeal.getId())
            exitMealOverview();

        if(viewId == btMealOverviewConfirmMeal.getId()){
            if(fromWhere.equals("HomeFragment"))
                updateMeal();
            if(fromWhere.equals("FoodSelectionFragment"))
                addMealIntoTodayMenu();

            if(fromWhere.equals("CustomMealsFragment")){
                if(actNormal)
                    updateCustomMeal();
                else
                    addMealIntoTodayMenu();
            }
            exitMealOverview();
        }
    }
}