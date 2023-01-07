package com.example.mydieter9005;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.InputType;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FoodSelectionFragment extends Fragment implements View.OnClickListener {

    private NetworkConnectionReceiver networkConnectionReceiver;

    MealOverviewFragment mealOverviewFragment;

    LinearLayout linearLayout, loadingWorldSavedCustomMealsLinearLayout;
    Button btSwitchBetweenMealAndIngredients, btSwitchBetweenLocalAndGlobalFood;
    EditText etFilterFood;
    ListView listView;

    DailyMenu todayMenu = DailyMenu.getTodayMenu();
    ArrayList<Meal> mealsList;
    ArrayList<Meal> internetMealsList = new ArrayList<Meal>();
    MealListAdapter mealsAdapter;

    ArrayList<Ingredient> ingredients;
    IngredientListAdapter ingredientsAdapter;

    boolean isOnMealsMode = false;
    boolean isOnLocalMode = true;

    DatabaseReference databaseReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_food_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mealsList = new ArrayList<Meal>();
        ArrayList<Ingredient> ingredientsNeeded = new ArrayList<Ingredient>();  // For multi-ingredients meals.

        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("nestle cereals"), 30));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("milk"), 200));
        mealsList.add(new Meal("Nestle cereals with milk", ingredientsNeeded));
        ingredientsNeeded.clear();

        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("Chocolate flavored nestle cereals"), 30));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("milk"), 200));
        mealsList.add(new Meal("Chocolate flavored nestle cereals", ingredientsNeeded));
        ingredientsNeeded.clear();

        mealsList.add(new Meal("Yogurt", 100));
        mealsList.add(new Meal("Chocolate flavored yogurt", 100));
        mealsList.add(new Meal("Chocolate flavored ice cream", 250));

        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("bread"), 150));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("yellow cheese"), 75));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("thousand island dressing"), 25));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("ketchup"), 25));
        mealsList.add(new Meal("Toast", ingredientsNeeded));
        ingredientsNeeded.clear();

        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("bread"), 150));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("yellow cheese"), 75));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("thousand island dressing"), 25));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("ketchup"), 25));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("tomato"), 100));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("cucumber"), 100));
        mealsList.add(new Meal("Toast with tomato and cucumber", ingredientsNeeded));
        ingredientsNeeded.clear();

        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("bread"), 150));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("yellow cheese"), 75));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("thousand island dressing"), 25));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("ketchup"), 25));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("tomato"), 100));
        mealsList.add(new Meal("Toast with tomato", ingredientsNeeded));
        ingredientsNeeded.clear();

        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("bread"), 150));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("yellow cheese"), 75));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("thousand island dressing"), 25));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("ketchup"), 25));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("cucumber"), 100));
        mealsList.add(new Meal("Toast with cucumber", ingredientsNeeded));
        ingredientsNeeded.clear();

        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("bread"), 150));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("yellow cheese"), 75));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("thousand island dressing"), 25));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("ketchup"), 25));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("olive"), 100));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("corn"), 100));
        mealsList.add(new Meal("Toast with olive and corn", ingredientsNeeded));
        ingredientsNeeded.clear();

        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("bread"), 150));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("yellow cheese"), 75));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("thousand island dressing"), 25));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("ketchup"), 25));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("olive"), 100));
        mealsList.add(new Meal("Toast with olive", ingredientsNeeded));
        ingredientsNeeded.clear();

        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("bread"), 150));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("yellow cheese"), 75));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("thousand island dressing"), 25));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("ketchup"), 25));
        ingredientsNeeded.add(new Ingredient(Ingredient.getIngredientByName("corn"), 100));
        mealsList.add(new Meal("Toast with corn", ingredientsNeeded));
        ingredientsNeeded.clear();

        listView = (ListView) view.findViewById(R.id.listViewFood);

        loadingWorldSavedCustomMealsLinearLayout = (LinearLayout) view.findViewById(R.id.loadingWorldSavedCustomMealsLinearLayout);
        loadingWorldSavedCustomMealsLinearLayout.setVisibility(View.GONE);
        linearLayout = (LinearLayout) view.findViewById(R.id.foodSelectionLinearLayout);
        linearLayout.setVisibility(View.VISIBLE);

        btSwitchBetweenLocalAndGlobalFood = (Button) view.findViewById(R.id.btSwitchBetweenLocalAndGlobalFood);
        btSwitchBetweenLocalAndGlobalFood.setOnClickListener(this);
        btSwitchBetweenMealAndIngredients = (Button) view.findViewById(R.id.btSwitchBetweenMealAndIngredients);
        btSwitchBetweenMealAndIngredients.setOnClickListener(this);

        etFilterFood = (EditText) view.findViewById(R.id.etFilterFood);
        etFilterFood.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(isOnLocalMode){
                    if(isOnMealsMode)
                        mealsAdapter.getFilter().filter(s);
                    else
                        ingredientsAdapter.getFilter().filter(s);
                }
                else
                    mealsAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        isOnMealsMode = false;
        isOnLocalMode = true;

        setIngredientListViewAdapters();
    }

    public void setIngredientListViewAdapters() {
        initiateIngredientsList();

        ingredientsAdapter = new IngredientListAdapter(getActivity(), ingredients);
        listView.setAdapter(ingredientsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Ingredient selectedItem = (Ingredient) parent.getItemAtPosition(position);

                ingredientOverviewAlertDialog(selectedItem);
            }
        });
    }

    public void setMealListViewAdapter(){
        mealsAdapter = new MealListAdapter(getActivity(), mealsList);
        listView.setAdapter(mealsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Meal selectedItem = (Meal) adapterView.getItemAtPosition(i);

                mealOverviewFragment = new MealOverviewFragment(selectedItem);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityFrameLayout, mealOverviewFragment).commit();
            }
        });
    }

    public void setInternetListViewAdapter(){
        initiateInternetListViewFields();

        mealsAdapter = new MealListAdapter(getActivity(), internetMealsList);
        listView.setAdapter(mealsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Meal selectedItem = (Meal) adapterView.getItemAtPosition(i);
                selectedItem.setName(selectedItem.getName().split(" - ")[1]);

                mealOverviewFragment = new MealOverviewFragment(selectedItem);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityFrameLayout, mealOverviewFragment).commit();
            }
        });
    }

    public void initiateIngredientsList(){
        ArrayList<Ingredient> tempIngredients = Ingredient.getIngredientsList();
        ingredients = new ArrayList<Ingredient>();

        for(int i = 0; i < tempIngredients.size(); i++)
            ingredients.add(new Ingredient(tempIngredients.get(i), 100));
    }

    public void initiateInternetListViewFields(){
        loadingWorldSavedCustomMealsLinearLayout.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.GONE);

        internetMealsList.clear();
        databaseReference = FirebaseDatabase.getInstance().getReference("recipes");
        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                ArrayList<Ingredient> ingredientsNeededInfo = new ArrayList<Ingredient>();
                Ingredient ingredient;
                String mealName, ingredientName;
                Double ingredientGrams;

                if(isAdded() && isVisible() && getUserVisibleHint()) {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            DataSnapshot dataSnapshot = task.getResult();
                            for (DataSnapshot customMeal : dataSnapshot.getChildren()) {
                                ingredientsNeededInfo.clear();
                                mealName = customMeal.getKey();
                                for (int i = 0; i < customMeal.getChildrenCount(); i++) {
                                    ingredientName = (customMeal.child(i + "").getValue().toString()).split(": ")[0];;
                                    ingredientGrams = Double.parseDouble((customMeal.child(i + "").getValue().toString().split(": "))[1].split(" ")[0]);

                                    ingredient = new Ingredient(Ingredient.getIngredientByName(ingredientName), ingredientGrams);
                                    ingredientsNeededInfo.add(ingredient);
                                }
                                internetMealsList.add(new Meal(mealName, ingredientsNeededInfo));
                                mealsAdapter.notifyDataSetChanged();
                            }

                            try {
                                getActivity().unregisterReceiver(networkConnectionReceiver);
                            }
                            catch (IllegalArgumentException e) {
                                e.getStackTrace();
                            }
                        }
                        else {
                            notEvenOneCustomMealAdded();
                        }
                    }
                    else {
                        recipesDatabaseNotFound();
                        Toast.makeText(getActivity(), "Failed to load meals.", Toast.LENGTH_SHORT).show();
                    }
                }

                loadingWorldSavedCustomMealsLinearLayout.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    public void recipesDatabaseNotFound(){
        AlertDialog ad;
        AlertDialog.Builder adb;
        adb = new AlertDialog.Builder(getActivity());
        adb.setTitle("Custom meals not found!");
        adb.setMessage("We have trouble connecting our database right now, please come back later");
        adb.setIcon(R.drawable.ic_error_icon);
        adb.setCancelable(false);

        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switchBetweenLocalAndGlobalFood();
            }
        });

        ad = adb.create();
        ad.show();
    }

    public void notEvenOneCustomMealAdded(){
        AlertDialog ad;
        AlertDialog.Builder adb;
        adb = new AlertDialog.Builder(getActivity());
        adb.setTitle("Custom meals not found!");
        adb.setMessage("It's seems like no one saved any custom meal so far, you can be the first!.");
        adb.setIcon(R.drawable.ic_food_icon);
        adb.setCancelable(false);

        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switchBetweenLocalAndGlobalFood();
            }
        });

        ad = adb.create();
        ad.show();
    }

    public void ingredientOverviewAlertDialog(Ingredient ingredient){
        AlertDialog ad;
        AlertDialog.Builder adb;
        adb = new AlertDialog.Builder(getActivity());

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

        Spinner sAlertDialogSelectMeal = (Spinner) customAlertDialog.findViewById(R.id.sAlertDialogSelectMeal);
        String[] selectMealOptions = new String[]{"Breakfast", "Lunch", "Dinner"};
        ArrayAdapter<String> alertDialogSelectedMealAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, selectMealOptions);
        sAlertDialogSelectMeal.setAdapter(alertDialogSelectedMealAdapter);

        EditText etAlertDialogIngredientGrams = customAlertDialog.findViewById(R.id.etAlertDialogIngredientGrams);
        etAlertDialogIngredientGrams.setText("100");
        etAlertDialogIngredientGrams.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!etAlertDialogIngredientGrams.getText().toString().equals("")){
                    ingredient.setGrams(Integer.parseInt(etAlertDialogIngredientGrams.getText().toString()));
                    tvAlertDialogIngredientCalories.setText("Calories: " + ingredient.getCalories());
                    tvAlertDialogIngredientProteins.setText("Proteins: " + ingredient.getProteins());
                    tvAlertDialogIngredientFats.setText("Fats: " + ingredient.getFats());
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

                if(sAlertDialogSelectMeal.getSelectedItem() == null && passTests) {
                    Toast.makeText(getActivity(), "Choose breakfast, lunch or dinner.", Toast.LENGTH_SHORT).show();
                    passTests = false;
                }

                if(passTests){
                    Toast.makeText(getActivity(), "Ingredient successfully added.", Toast.LENGTH_SHORT).show();

                    String selectedMeal = sAlertDialogSelectMeal.getSelectedItem().toString();
                    int grams = Integer.parseInt(etAlertDialogIngredientGrams.getText().toString());
                    todayMenu.addIngredientByMealName(selectedMeal, ingredient, grams);
                    DailyMenu.saveDailyMenuIntoFile(todayMenu, getActivity());

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

    public void switchBetweenMealAndIngredients(){
        isOnMealsMode = !isOnMealsMode;

        if(!etFilterFood.getText().toString().equals(""))
            etFilterFood.setText("");

        if(isOnMealsMode) {
            btSwitchBetweenMealAndIngredients.setText("Choose from Ingredients");
            setMealListViewAdapter();
        }
        else {
            btSwitchBetweenMealAndIngredients.setText("Choose from Meals");
            setIngredientListViewAdapters();
        }
    }

    public void switchBetweenLocalAndGlobalFood(){
        isOnLocalMode = !isOnLocalMode;

        if(!etFilterFood.getText().toString().equals(""))
            etFilterFood.setText("");

        if(isOnLocalMode){
            btSwitchBetweenLocalAndGlobalFood.setText("Choose from internet");
            btSwitchBetweenMealAndIngredients.setVisibility(View.VISIBLE);

            if(isOnMealsMode)
                setMealListViewAdapter();
            else
                setIngredientListViewAdapters();

            try{
                getActivity().unregisterReceiver(networkConnectionReceiver);
            }
            catch (IllegalArgumentException e){
                e.getStackTrace();
            }
        }
        else{
            btSwitchBetweenLocalAndGlobalFood.setText("Choose from local");
            btSwitchBetweenMealAndIngredients.setVisibility(View.GONE);
            setCustomNetworkConnectionReceiver();
            setInternetListViewAdapter();
        }
    }

    public void setCustomNetworkConnectionReceiver(){
        networkConnectionReceiver = new NetworkConnectionReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try{
                    if(!isOnline(getActivity()))
                        noInternetAccess(context);
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

                adb.setNegativeButton("Go back to local", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switchBetweenLocalAndGlobalFood();
                    }
                });

                ad = adb.create();
                ad.show();
            }
        };

        IntentFilter networkConnectionFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            getActivity().registerReceiver(networkConnectionReceiver, networkConnectionFilter);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            getActivity().registerReceiver(networkConnectionReceiver, networkConnectionFilter);

        if(!networkConnectionReceiver.isOnline(getActivity())) {
            networkConnectionReceiver.noInternetAccess(getActivity());
            getActivity().unregisterReceiver(networkConnectionReceiver);
        }
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        if(viewId == btSwitchBetweenMealAndIngredients.getId())
            switchBetweenMealAndIngredients();

        if(viewId == btSwitchBetweenLocalAndGlobalFood.getId())
            switchBetweenLocalAndGlobalFood();
    }
}