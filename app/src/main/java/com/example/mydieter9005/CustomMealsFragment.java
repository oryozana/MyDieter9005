package com.example.mydieter9005;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CustomMealsFragment extends Fragment implements View.OnClickListener {

    private NetworkConnectionReceiver networkConnectionReceiver;

    Button btSendToCustomSelection, btShowMealInfo, btSaveCustomMeal, btUseCodeAlertDialog;
    LinearLayout customMealsAdditionsLinearLayout;
    ListView lvCustomMealIngredients;
    ImageButton ibtAddIngredient;
    TextView tvCustomSelection;
    EditText etCustomMeal;

    IngredientListAdapter customMealIngredientsAdapter, codeMealIngredientsAdapter;
    Meal customMeal, codeMeal = null;

    ArrayList<Meal> customMealsList;
    MealListAdapter customMealsAdapter;

    String[] customMealsNames;

    boolean isCustomSelection = false;

    FileAndDatabaseHelper fileAndDatabaseHelper;

    FoodSelectionFragment foodSelectionFragment;
    MealOverviewFragment mealOverviewFragment;

    FirebaseDatabase codesDb;
    FirebaseDatabase recipesDb;
    DatabaseReference databaseReference;

    boolean internetConnection = true;

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
        btUseCodeAlertDialog = (Button) view.findViewById(R.id.btUseCodeAlertDialog);
        btUseCodeAlertDialog.setOnClickListener(this);
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

        setCustomNetworkConnectionReceiver();

        if(!customMeal.getName().equals(""))
            etCustomMeal.setText(customMeal.getName());

        if(isCustomSelection)
            switchBetweenCustomMealsAndCustomSelection();
        setIngredientsAdapters();
    }

    public void setIngredientsAdapters() {
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

        lvCustomMealIngredients.setOnItemLongClickListener(null);
    }

    public void switchBetweenCustomMealsAndCustomSelection(){
        isCustomSelection = !isCustomSelection;
        if(isCustomSelection){
            customMealsAdditionsLinearLayout.setVisibility(View.GONE);
            btSaveCustomMeal.setVisibility(View.GONE);
            btShowMealInfo.setVisibility(View.GONE);
            btSendToCustomSelection.setText("Create new custom meal");
            tvCustomSelection.setVisibility(View.VISIBLE);
            btUseCodeAlertDialog.setVisibility(View.VISIBLE);
            initiateCustomMealsList();
        }
        else{
            if(DailyMenu.hasCustomMeal())
                customMeal = DailyMenu.getCustomMeal();
            else
                customMeal = new Meal("");

            customMealsAdditionsLinearLayout.setVisibility(View.VISIBLE);
            btSaveCustomMeal.setVisibility(View.VISIBLE);
            btShowMealInfo.setVisibility(View.VISIBLE);
            btSendToCustomSelection.setText("Choose from saved");
            tvCustomSelection.setVisibility(View.GONE);
            btUseCodeAlertDialog.setVisibility(View.GONE);

            if(!customMeal.getName().equals(""))
                etCustomMeal.setText(customMeal.getName());

            setIngredientsAdapters();
        }
    }

    public void initiateCustomMealsList(){
        customMealsList = new ArrayList<Meal>();
        customMealsNames = getSavedCustomMealsNames();

        for(int i = 0; i < customMealsNames.length - 1; i++)  // - 1 to get rid of the empty last element in this array.
            customMealsList.add(new Meal(customMealsNames[i], getIngredientsFromFileByCustomMealName(customMealsNames[i])));

        setMealsListViewAdapter();
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

        adb.setNegativeButton("Use code", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                useCodeAlertDialog();
            }
        });

        ad = adb.create();
        ad.show();
    }

    public void setMealsListViewAdapter(){
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

        lvCustomMealIngredients.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Meal selectedItem = (Meal) parent.getItemAtPosition(position);

                customMealOptionsAlertDialog(selectedItem);
                return true;
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

    public void customMealOptionsAlertDialog(Meal meal){
        AlertDialog ad;
        AlertDialog.Builder adb;
        adb = new AlertDialog.Builder(getActivity());

        View customAlertDialog = LayoutInflater.from(getActivity()).inflate(R.layout.custom_meals_options_alert_dialog, null);

        LinearLayout customMealsOptionsLinearLayout = (LinearLayout) customAlertDialog.findViewById(R.id.customMealsOptionsLinearLayout);
        Button btPublishCustomMeal = (Button) customAlertDialog.findViewById(R.id.btPublishCustomMeal);
        Button btShareCustomMeal = (Button) customAlertDialog.findViewById(R.id.btShareCustomMeal);
        Button btUseCustomMealAsBase = (Button) customAlertDialog.findViewById(R.id.btUseCustomMealAsBase);
        Button btRemoveCustomMeal = (Button) customAlertDialog.findViewById(R.id.btRemoveCustomMeal);

        LinearLayout chooseCodeDurationLinearLayout = (LinearLayout) customAlertDialog.findViewById(R.id.chooseCodeDurationLinearLayout);
        RadioGroup rgChooseDuration = (RadioGroup) customAlertDialog.findViewById(R.id.rgChooseDuration);
        RadioButton rbOneHour = (RadioButton) customAlertDialog.findViewById(R.id.rbOneHour);
        RadioButton rbEightHours = (RadioButton) customAlertDialog.findViewById(R.id.rbEightHours);
        RadioButton rbOneDay = (RadioButton) customAlertDialog.findViewById(R.id.rbOneDay);
        RadioButton rbOneWeek = (RadioButton) customAlertDialog.findViewById(R.id.rbOneWeek);
        RadioButton rbUnlimited = (RadioButton) customAlertDialog.findViewById(R.id.rbUnlimited);
        Button btAcceptDuration = (Button) customAlertDialog.findViewById(R.id.btAcceptDuration);
        Button btBackFromDuration = (Button) customAlertDialog.findViewById(R.id.btBackFromDuration);

        LinearLayout showCodeLinearLayout = (LinearLayout) customAlertDialog.findViewById(R.id.showCodeLinearLayout);
        TextView tvShowGeneratedCode = (TextView) customAlertDialog.findViewById(R.id.tvShowGeneratedCode);
        ImageButton ibtShowCopyCodeOption = (ImageButton) customAlertDialog.findViewById(R.id.ibtShowCopyCodeOption);
        TextView tvShowExpirationDate = (TextView) customAlertDialog.findViewById(R.id.tvShowExpirationDate);
        Button btBackFromGenerated = (Button) customAlertDialog.findViewById(R.id.btBackFromGenerated);

        LinearLayout anotherCustomMealExistsLinearLayout = (LinearLayout) customAlertDialog.findViewById(R.id.anotherCustomMealExistsLinearLayout);
        Button btReplaceMeal = (Button) customAlertDialog.findViewById(R.id.btReplaceMeal);
        Button btCancel = (Button) customAlertDialog.findViewById(R.id.btCancel);

        LinearLayout loadingLinearLayout = (LinearLayout) customAlertDialog.findViewById(R.id.loadingLinearLayout);

        adb.setView(customAlertDialog);
        ad = adb.create();

        btPublishCustomMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customMealsOptionsLinearLayout.setVisibility(View.GONE);
                loadingLinearLayout.setVisibility(View.VISIBLE);

                if(internetConnection){
                    FirebaseMeal firebaseMeal = new FirebaseMeal(meal);

                    recipesDb = FirebaseDatabase.getInstance();
                    databaseReference = recipesDb.getReference("recipes");
                    databaseReference.child(firebaseMeal.getName()).setValue(firebaseMeal.getNeededIngredientsForMeal()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getActivity(), "Meal successfully uploaded.", Toast.LENGTH_SHORT).show();

                            loadingLinearLayout.setVisibility(View.GONE);
                            customMealsOptionsLinearLayout.setVisibility(View.VISIBLE);
                        }
                    });
                }
                else{
                    loadingLinearLayout.setVisibility(View.GONE);
                    customMealsOptionsLinearLayout.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Connect to the internet and try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btShareCustomMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customMealsOptionsLinearLayout.setVisibility(View.GONE);
                chooseCodeDurationLinearLayout.setVisibility(View.VISIBLE);
            }
        });

        btUseCustomMealAsBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customMealsOptionsLinearLayout.setVisibility(View.GONE);

                if(!DailyMenu.hasCustomMeal()) {
                    ad.cancel();
                    meal.setName(meal.getName() + " based");
                    DailyMenu.saveCustomMeal(meal);
                    switchBetweenCustomMealsAndCustomSelection();
                }
                else
                    anotherCustomMealExistsLinearLayout.setVisibility(View.VISIBLE);
            }
        });

        btRemoveCustomMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeCustomMealFile(meal.getName());
                removeSavedCustomMealName(meal.getName());
                initiateCustomMealsList();
                customMealIngredientsAdapter.notifyDataSetChanged();

                if(!checkIfAtLeastOneCustomMealAdded())
                    switchBetweenCustomMealsAndCustomSelection();
                ad.cancel();
            }
        });

        btAcceptDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(internetConnection){
                    LocalDateTime expirationDate = LocalDateTime.now(ZoneId.of("Asia/Jerusalem"));

                    int checkedRadioButtonId = rgChooseDuration.getCheckedRadioButtonId();
                    if(checkedRadioButtonId == rbOneHour.getId())
                        expirationDate = expirationDate.plusHours(1);

                    if(checkedRadioButtonId == rbEightHours.getId())
                        expirationDate = expirationDate.plusHours(8);

                    if(checkedRadioButtonId == rbOneDay.getId())
                        expirationDate = expirationDate.plusDays(1);

                    if(checkedRadioButtonId == rbOneWeek.getId())
                        expirationDate = expirationDate.plusWeeks(1);

                    if(checkedRadioButtonId == rbUnlimited.getId())  // A lot of time but not really unlimited.
                        expirationDate = expirationDate.plusYears(10);

                    RecipeCode recipeCode = new RecipeCode(new FirebaseMeal(meal), expirationDate.toString());
                    recipeCode.getMeal().setName(recipeCode.getMeal().getName().split(User.getCurrentUser().getUsername() + " - ")[1]);

                    String date = expirationDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    String time = expirationDate.format(DateTimeFormatter.ofPattern("HH:mm"));
                    tvShowExpirationDate.setText("You have until: " + date + " at " + time + " to use this code.");

                    chooseCodeDurationLinearLayout.setVisibility(View.GONE);
                    loadingLinearLayout.setVisibility(View.VISIBLE);

                    codesDb = FirebaseDatabase.getInstance();
                    databaseReference = codesDb.getReference("codes");
                    databaseReference.child(recipeCode.getCode()).setValue(recipeCode).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getActivity(), "Code successfully created.", Toast.LENGTH_SHORT).show();
                            tvShowGeneratedCode.setText(recipeCode.getCode());

                            loadingLinearLayout.setVisibility(View.GONE);
                            showCodeLinearLayout.setVisibility(View.VISIBLE);
                        }
                    });
                }
                else
                    Toast.makeText(getActivity(), "No internet connection, can't generate code.", Toast.LENGTH_SHORT).show();
            }
        });

        btBackFromDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgChooseDuration.clearCheck();
                rbOneHour.setChecked(true);
                chooseCodeDurationLinearLayout.setVisibility(View.GONE);
                customMealsOptionsLinearLayout.setVisibility(View.VISIBLE);
            }
        });

        ibtShowCopyCodeOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("TextView", tvShowGeneratedCode.getText().toString());
                clipboard.setPrimaryClip(clip);
                clip.getDescription();
                Toast.makeText(getActivity(), "Copied.", Toast.LENGTH_SHORT).show();
            }
        });

        btBackFromGenerated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCodeLinearLayout.setVisibility(View.GONE);
                chooseCodeDurationLinearLayout.setVisibility(View.VISIBLE);
            }
        });

        btReplaceMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.cancel();
                DailyMenu.saveCustomMeal(meal);
                switchBetweenCustomMealsAndCustomSelection();
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anotherCustomMealExistsLinearLayout.setVisibility(View.GONE);
                customMealsOptionsLinearLayout.setVisibility(View.VISIBLE);
            }
        });

        ad.show();
    }

    public void useCodeAlertDialog(){
        AlertDialog ad;
        AlertDialog.Builder adb;
        adb = new AlertDialog.Builder(getActivity());

        View customAlertDialog = LayoutInflater.from(getActivity()).inflate(R.layout.use_code_alert_dialog, null);

        LinearLayout enterCodeLinearLayout = (LinearLayout) customAlertDialog.findViewById(R.id.enterCodeLinearLayout);
        EditText etEnterCode = (EditText) customAlertDialog.findViewById(R.id.etEnterCode);
        Button btTestEnteredCode = (Button) customAlertDialog.findViewById(R.id.btTestEnteredCode);

        LinearLayout showCodeMealLinearLayout = (LinearLayout) customAlertDialog.findViewById(R.id.showCodeMealLinearLayout);
        TextView tvShowCodeMealName = (TextView) customAlertDialog.findViewById(R.id.tvShowCodeMealName);
        ListView lvCodeMealIngredients = (ListView) customAlertDialog.findViewById(R.id.lvCodeMealIngredients);
        Button btCancelMealCode = (Button) customAlertDialog.findViewById(R.id.btCancelMealCode);
        Button btContinueMealCode = (Button) customAlertDialog.findViewById(R.id.btContinueMealCode);

        LinearLayout whatToDoWithCodeMealLinearLayout = (LinearLayout) customAlertDialog.findViewById(R.id.whatToDoWithCodeMealLinearLayout);
        Button btSaveCodeMeal = (Button) customAlertDialog.findViewById(R.id.btSaveCodeMeal);
        Spinner sCodeMealSelectMealType = (Spinner) customAlertDialog.findViewById(R.id.sCodeMealSelectMealType);
        Button btAddIntoMealType = (Button) customAlertDialog.findViewById(R.id.btAddIntoMealType);

        String[] selectMealOptions = new String[]{"Breakfast", "Lunch", "Dinner"};
        ArrayAdapter<String> alertDialogSelectedMealAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, selectMealOptions);
        sCodeMealSelectMealType.setAdapter(alertDialogSelectedMealAdapter);

        LinearLayout anotherCustomMealExistsLinearLayout = (LinearLayout) customAlertDialog.findViewById(R.id.anotherCustomMealExistsLinearLayout);
        Button btReplaceMeal = (Button) customAlertDialog.findViewById(R.id.btReplaceMeal);
        Button btRenameMeal = (Button) customAlertDialog.findViewById(R.id.btRenameMeal);
        Button btCancel = (Button) customAlertDialog.findViewById(R.id.btCancel);

        LinearLayout renameCodeMealLinearLayout = (LinearLayout) customAlertDialog.findViewById(R.id.renameCodeMealLinearLayout);
        EditText etCodeMealRename = (EditText) customAlertDialog.findViewById(R.id.etCodeMealRename);
        TextView tvShowRenameAttempt = (TextView) customAlertDialog.findViewById(R.id.tvShowRenameAttempt);
        Button btRenameMealAttempt = (Button) customAlertDialog.findViewById(R.id.btRenameMealAttempt);
        Button btBackFromRename = (Button) customAlertDialog.findViewById(R.id.btBackFromRename);

        LinearLayout loadingLinearLayout = (LinearLayout) customAlertDialog.findViewById(R.id.loadingLinearLayout);

        adb.setView(customAlertDialog);
        ad = adb.create();

        btTestEnteredCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = etEnterCode.getText().toString();
                if(internetConnection){
                    if(passCodeTests(code)){
                        enterCodeLinearLayout.setVisibility(View.GONE);
                        loadingLinearLayout.setVisibility(View.VISIBLE);

                        databaseReference = FirebaseDatabase.getInstance().getReference("codes");
                        databaseReference.child(code).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (isAdded() && isVisible() && getUserVisibleHint()) {
                                    if (task.isSuccessful()) {
                                        if (task.getResult().exists()) {
                                            DataSnapshot dataSnapshot = task.getResult();

                                            LocalDateTime expirationTime = LocalDateTime.parse(String.valueOf(dataSnapshot.child("expirationTime").getValue()).replaceAll("\\*", "\\."));
                                            boolean isExpired = LocalDateTime.now(ZoneId.of("Asia/Jerusalem")).isAfter(expirationTime);

                                            codeMeal = new Meal("");

                                            if (!isExpired) {
                                                DataSnapshot customMealInfo = dataSnapshot.child("meal");
                                                codeMeal.setName(customMealInfo.child("name").getValue().toString());
                                                DataSnapshot customMealIngredients = customMealInfo.child("neededIngredientsForMeal");

                                                Ingredient tmpIngredient;
                                                String ingredientName;
                                                int ingredientGrams;

                                                for (int i = 0; i < customMealIngredients.getChildrenCount(); i++) {
                                                    ingredientName = (customMealIngredients.child(i + "").child("name").getValue().toString());
                                                    ingredientGrams = Integer.parseInt((customMealIngredients.child(i + "").child("grams").getValue().toString()));

                                                    tmpIngredient = new Ingredient(Ingredient.getIngredientByName(ingredientName), ingredientGrams);
                                                    codeMeal.addNeededIngredientForMeal(tmpIngredient);
                                                }

                                                showCodeMealLinearLayout.setVisibility(View.VISIBLE);
                                                tvShowCodeMealName.setText("Meal name: " + codeMeal.getName());
                                                codeMealIngredientsAdapter = new IngredientListAdapter(getActivity(), codeMeal.getNeededIngredientsForMeal());
                                                lvCodeMealIngredients.setAdapter(codeMealIngredientsAdapter);
                                            }
                                            else
                                                Toast.makeText(getActivity(), "Code expired.", Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                            Toast.makeText(getActivity(), "Code incorrect.", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                        Toast.makeText(getActivity(), "Code incorrect.", Toast.LENGTH_SHORT).show();

                                    loadingLinearLayout.setVisibility(View.GONE);
                                    if(showCodeMealLinearLayout.getVisibility() == View.GONE)
                                        enterCodeLinearLayout.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    }
                }
                else
                    Toast.makeText(getActivity(), "No internet connection, can't use codes.", Toast.LENGTH_SHORT).show();
            }
        });

        btCancelMealCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etEnterCode.setText("");

                showCodeMealLinearLayout.setVisibility(View.GONE);
                enterCodeLinearLayout.setVisibility(View.VISIBLE);
            }
        });

        btContinueMealCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCodeMealLinearLayout.setVisibility(View.GONE);
                whatToDoWithCodeMealLinearLayout.setVisibility(View.VISIBLE);
            }
        });

        btSaveCodeMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkIfCustomMealNameAlreadyExists(codeMeal.getName())){
                    whatToDoWithCodeMealLinearLayout.setVisibility(View.GONE);
                    anotherCustomMealExistsLinearLayout.setVisibility(View.VISIBLE);
                }
                else{
                    String[] savedCustomMealsNames = getSavedCustomMealsNames();
                    if(savedCustomMealsNames.length == 1)
                        switchBetweenCustomMealsAndCustomSelection();

                    saveCodeMealInAFile();
                    saveCodeMealNameInsideFile();
                    initiateCustomMealsList();
                    ad.cancel();
                }
            }
        });

        btAddIntoMealType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sCodeMealSelectMealType.getSelectedItem() != null) {
                    ad.cancel();
                    String selectedMeal = sCodeMealSelectMealType.getSelectedItem().toString();
                    DailyMenu.getTodayMenu().addMealByMealName(selectedMeal, codeMeal);
                    DailyMenu.saveDailyMenuIntoFile(DailyMenu.getTodayMenu(), getActivity());
                    Toast.makeText(getActivity(), "Meal successfully added.", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getActivity(), "Choose breakfast, lunch or dinner.", Toast.LENGTH_SHORT).show();
            }
        });

        btReplaceMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.cancel();
                saveCodeMealInAFile();
                saveCodeMealNameInsideFile();
                initiateCustomMealsList();
                Toast.makeText(getActivity(), "Meal replaced successfully", Toast.LENGTH_SHORT).show();
            }
        });

        btRenameMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvShowRenameAttempt.setText("The name: " + codeMeal.getName() + " already exists.");

                anotherCustomMealExistsLinearLayout.setVisibility(View.GONE);
                renameCodeMealLinearLayout.setVisibility(View.VISIBLE);
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.cancel();
            }
        });

        btRenameMealAttempt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmpMealName = etCodeMealRename.getText().toString();

                if(!tmpMealName.replaceAll(" ", "").equals("")){
                    if(checkIfCustomMealNameAlreadyExists(tmpMealName)){
                        Toast.makeText(getActivity(), "This name belong to another meal.", Toast.LENGTH_SHORT).show();
                        tvShowRenameAttempt.setText("The name: " + tmpMealName + " already exists.");
                    }
                    else{
                        Toast.makeText(getActivity(), "Rename went successfully.", Toast.LENGTH_SHORT).show();
                        codeMeal.setName(tmpMealName);
                        saveCodeMealInAFile();
                        saveCodeMealNameInsideFile();
                        initiateCustomMealsList();
                        ad.cancel();
                    }
                }
                else
                    Toast.makeText(getActivity(), "Enter something first.", Toast.LENGTH_SHORT).show();
            }
        });

        btBackFromRename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etCodeMealRename.setText("");
                renameCodeMealLinearLayout.setVisibility(View.GONE);
                anotherCustomMealExistsLinearLayout.setVisibility(View.VISIBLE);
            }
        });

        ad.show();
    }

    public boolean passCodeTests(String code){
        boolean passTests = true;
        int normalCodeLength = 8;

        if(code.replaceAll(" ", "").equals("")){
            Toast.makeText(getActivity(), "Can't left the field 'code' empty.", Toast.LENGTH_SHORT).show();
            passTests = false;
        }

        if(code.length() != normalCodeLength && passTests){
            Toast.makeText(getActivity(), "Codes should be 8 characters long.", Toast.LENGTH_SHORT).show();
            passTests = false;
        }
        return passTests;
    }

    public boolean checkIfCustomMealNameAlreadyExists(String mealName){
        for(int i = 0; customMealsNames != null && i < customMealsNames.length; i++){
            if(mealName.equals(customMealsNames[i]))
                return true;
        }
        return false;
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

    public void saveCodeMealInAFile(){
        try {
            fos = getActivity().openFileOutput("customMeal: " + codeMeal.getName(), Context.MODE_PRIVATE);
            osw = new OutputStreamWriter(fos);
            bw = new BufferedWriter(osw);

            bw.write(codeMeal.getName() + "\n");

            bw.write(codeMeal.getNeededIngredientsForMeal().get(0).toString());
            for(int i = 1; i < codeMeal.getNeededIngredientsForMeal().size(); i++)
                bw.write("\n" + codeMeal.getNeededIngredientsForMeal().get(i).toString());

            bw.close();

            boolean alreadyThere = false;
            for(int i = 0; i < getSavedCustomMealsNames().length; i++){
                if(codeMeal.getName().equals(getSavedCustomMealsNames()[i]))
                    alreadyThere = true;
            }
            if(!alreadyThere)
                saveCustomMealNameInsideFile();
            Toast.makeText(getActivity(), codeMeal.getName() + " added.", Toast.LENGTH_SHORT).show();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeCustomMealFile(String customMealName){
        boolean deleted = false;

        File file = new File(getActivity().getFilesDir(), "customMeal: " + customMealName);
        if(file.exists() && file.isFile())
            deleted = file.delete();

        if(deleted)
            Toast.makeText(getActivity(), "Custom meal deleted successfully.", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getActivity(), "Failed to delete meal...", Toast.LENGTH_SHORT).show();
    }

    public String[] getSavedCustomMealsNames(){
        String[] customMealsNames = fileAndDatabaseHelper.getFileData(fileName).split("\n");
        ArrayList<String> tmpCustomMealsNames = new ArrayList<String>();

        for(int i = 0; i < customMealsNames.length; i++){
            if(!customMealsNames[i].equals(""))
                tmpCustomMealsNames.add(customMealsNames[i]);
        }

        customMealsNames = new String[tmpCustomMealsNames.size()];
        for(int i = 0; i < customMealsNames.length; i++)
            customMealsNames[i] = tmpCustomMealsNames.get(i);

        for(int i = 1; i < customMealsNames.length; i++) // Get rid of first line.
            customMealsNames[i - 1] = customMealsNames[i];

        customMealsNames[customMealsNames.length - 1] = "";
        return customMealsNames;
    }

    public void removeSavedCustomMealName(String customMealName){
        String[] dataParts = getSavedCustomMealsNames();
        try {
            fos = getActivity().openFileOutput("customMealsNames", Context.MODE_PRIVATE);
            osw = new OutputStreamWriter(fos);
            bw = new BufferedWriter(osw);

            bw.write("Custom meals names: " + "\n");

            int skip = 0;
            if(customMealName.equals(dataParts[0])) {
                bw.write(dataParts[1]);
                skip++;
            }
            else
                bw.write(dataParts[0]);

            for(int i = 1 + skip; i < dataParts.length; i++) {
                if(!customMealName.equals(dataParts[i]))
                    bw.write("\n" + dataParts[i]);
            }

            bw.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveAndClearCustomMeal(){
        if(checkIfCustomMealIsOk()) {
            saveCustomMealInAFile();

            DailyMenu.saveCustomMeal(null);
            etCustomMeal.setText("");
            customMeal = new Meal("");
            setIngredientsAdapters();
        }
        else {
            if(DailyMenu.hasCustomMeal() && !customMeal.getName().replaceAll(" ", "").equals(""))
                customMeal.setName(DailyMenu.getCustomMeal().getName());
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

    public void saveCodeMealNameInsideFile(){
        try {
            fos = getActivity().openFileOutput(fileName, Context.MODE_APPEND);
            osw = new OutputStreamWriter(fos);
            bw = new BufferedWriter(osw);

            Toast.makeText(getActivity(), "here: " + codeMeal.getName(), Toast.LENGTH_SHORT).show();
            bw.write(codeMeal.getName() + "\n");

            bw.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setCustomNetworkConnectionReceiver(){
        networkConnectionReceiver = new NetworkConnectionReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try{
                    internetConnection = isOnline(context);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
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

        if(viewId == btUseCodeAlertDialog.getId())
            useCodeAlertDialog();
    }
}