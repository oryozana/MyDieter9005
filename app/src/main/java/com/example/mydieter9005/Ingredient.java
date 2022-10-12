package com.example.mydieter9005;

import java.util.ArrayList;

public class Ingredient extends Food {
    private static ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>(); // Contain every ingredient basic info.
    private int imgId = 0;

    public Ingredient(String name, double grams, double proteins, double fats, double calories) {  // Full info ingredient, not for new ones.
        super(name, grams, proteins, fats, calories);
    }

    public Ingredient(Ingredient ingredient, double grams){  // Copy other ingredient info and adjust by grams.
        super(ingredient.name, grams, ingredient.proteins, ingredient.fats, ingredient.calories);
        this.proteins *= grams;
        this.fats *= grams;
        this.calories *= grams;
        this.imgId = ingredient.imgId;
        roundValues();
    }

    public Ingredient(Ingredient ingredient){  // Copy other ingredient info.
        super(ingredient.name, ingredient.grams, ingredient.proteins, ingredient.fats, ingredient.calories);
        this.imgId = ingredient.imgId;
        roundValues();
    }

    public Ingredient(String name, double proteins, double fats, double calories) {  // To initiate ingredients for the ingredients list.
        super(name, proteins, fats, calories);
        ingredients.add(this);
        roundValues();
    }

    public static Ingredient getIngredientByName(String name){
        for(Ingredient ingredient : Ingredient.ingredients){
            if(ingredient.name.equals(name))
                return ingredient;
        }
        return new Ingredient(name, -1, -1, -1);
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public static ArrayList<Ingredient> getIngredientsList(){
        return Ingredient.ingredients;
    }

    public void addGrams(double grams){
        this.grams += grams;
    }

    public String getIngredientInfo(){
        roundValues();
        return this.name + ": " + this.grams + " grams, " + this.proteins + " proteins, " + this.fats + " fats and " + this.calories + " calories.";
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public static ArrayList<Ingredient> initiateIngredientsList(){
        // Vegetables, fruits and mushrooms:
        new Ingredient("tomato", 0.887, 0.2, 18);
        new Ingredient("cucumber",  0.65, 0.11, 15);
        new Ingredient("parsley",  2.97, 0.79, 36);
        new Ingredient("lettuce",  1.23, 0.3, 17);
        new Ingredient("olive", 1.2, 18, 179);
        new Ingredient("corn", 3.1, 1.1, 117);
        new Ingredient("potato", 1.71, 0.1, 86);
        new Ingredient("lemon",  1.2, 0.3, 20);
        new Ingredient("mushroom", 3.09, 0.34, 22);
        new Ingredient("cauliflower", 1.98, 0.1, 25);
        new Ingredient("dwarf corn",  1.5, 0.3, 28);
        new Ingredient("strawberry", 0.67, 0.3, 32);
        new Ingredient("avocado", 2, 14.66, 160);
        new Ingredient("garlic", 6.36, 0.5, 149);
        new Ingredient("purple onion", 0.94, 0.1, 44);
        new Ingredient("carrot", 0.93, 0.24,1);
        new Ingredient("basil", 3.15, 0.64, 23);
        new Ingredient("red bell pepper", 0.99, 0.3, 31);
        new Ingredient("green bell pepper", 0.86, 0.17, 20);
        new Ingredient("scallion", 1.83, 0.19, 32);
        new Ingredient("onion", 1.1, 0.1, 40);
        new Ingredient("celery", 0.69, 0.17, 16);
        new Ingredient("red chili pepper", 1.87, 0.44, 40);
        new Ingredient("green chili pepper", 2, 0.2, 40);
        new Ingredient("jalapeno", 0.91, 0.37, 29);
        new Ingredient("summer zucchini", 1.21, 0.18, 16);
        new Ingredient("winter zucchini", 0.95, 0.13, 34);
        new Ingredient("cherry tomato", 0.88, 0.2, 18);
        new Ingredient("sweat potato", 1.57, 0.05, 86);
        new Ingredient("broccoli", 2.82, 0.37, 34);
        new Ingredient("pumpkin", 1, 0.1, 26);
        new Ingredient("thyme", 5.56, 1.68, 101);
        new Ingredient("spinach", 2.86, 0.39, 23);

        // Milky ingredients:
        new Ingredient("milk", 3.3, 3, 60);
        new Ingredient("yogurt", 4.8, 3, 65);
        new Ingredient("chocolate flavored yogurt", 7.7, 2, 100);
        new Ingredient("cheese", 9, 5, 98);
        new Ingredient("yellow cheese", 30, 9, 202);
        new Ingredient("chocolate", 9.5, 30, 532);
        new Ingredient("chocolate flavored ice cream", 4, 13.3, 233);
        new Ingredient("butter", 0.5, 82, 742);
        new Ingredient("cheddar", 26.5, 34, 413);
        new Ingredient("parmesan", 30, 23, 328);

        // Parve ingredients:
        new Ingredient("egg", 12.56, 9.51, 143);
        new Ingredient("patit", 8.5, 4.2, 378);
        new Ingredient("bread", 8.1, 2, 248);
        new Ingredient("nestle cereals", 7, 0.9, 375);
        new Ingredient("honey", 0.3, 0, 304);
        new Ingredient("rice", 8.7, 0.7, 351);
        new Ingredient("pasta", 11, 0.11, 353);
        new Ingredient("breadcrumbs", 12.6, 3.1, 392);
        new Ingredient("flour", 10.7, 1.8, 359);
        new Ingredient("sugar", 0, 0, 387);
        new Ingredient("brown sugar", 0.12, 0, 380);
        new Ingredient("spaghetti", 12.8, 2, 359);
        new Ingredient("peanut butter", 18.3, 29.8, 529);
        new Ingredient("cumin", 17.81, 22.27, 375);
        new Ingredient("italian seasoning", 13.4, 5.3, 263);
        new Ingredient("canned tomato", 3.4, 0.4, 98);

        // Fleshy ingredients:
        new Ingredient("chicken breast", 33.44, 4.71, 187);

        // Powders:
        new Ingredient("baking soda powder", 0, 0, 0);
        new Ingredient("cocoa powder", 19.6, 13.7, 228);
        new Ingredient("cafe powder", 12.2, 0.5, 241);
        new Ingredient("sweet paprika powder", 14.76, 12.96, 289);
        new Ingredient("garlic powder", 16.8, 0.76, 332);
        new Ingredient("cinnamon powder", 3.99, 1.24, 247);
        new Ingredient("baking powder",0.1, 0, 51);
        new Ingredient("oregano powder", 11, 10.25, 306);
        new Ingredient("paprika powder", 14.76, 12.95, 289);
        new Ingredient("vanilla powder", 0, 0, 344);
        new Ingredient("chili powder", 12.26, 16.76, 314);
        new Ingredient("onion powder", 10.12, 1.05, 347);

        // Sauces:
        new Ingredient("ketchup", 0.9, 0.1, 103);
        new Ingredient("tehina", 24, 60, 689);
        new Ingredient("mayonnaise", 0.7, 65, 600);
        new Ingredient("thousand island dressing", 0.9, 31, 336);
        new Ingredient("soy sauce", 10, 0, 187);
        new Ingredient("mustard sauce", 4.37, 4.01, 67);

        // Oils:
        new Ingredient("olive oil", 0, 92, 828);
        new Ingredient("canola oil", 0, 92, 828);

        return Ingredient.ingredients;
    }
}
