package com.example.loops.modelCollections;

import com.example.loops.database.Database;
import com.example.loops.models.MealPlan;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * A collection class storing the meal plans.
 */
public class MealPlanCollection {
    public final static String dateTimeFormat = "yyyy-MM-dd";
    public final static int futureDays = 7;
    private ArrayList<MealPlan> mealPlans;
    private HashMap<String, MealPlan> mealPlanDict;
    private Database database;

    /**
     * Types of meal
     */
    private enum Meal {
        Breakfast,
        Lunch,
        Supper
    }

    /**
     * Constructor. Generates the schedule for this week's meal plan but with no meals
     */
    public MealPlanCollection() {
        generateMealPlans();
    }

    /**
     * Constructor with database interaction.
     * Initializes the collection to the meal plans stored in the database
     * @param database
     */
    public MealPlanCollection(Database database) {
        generateMealPlans();
        this.database = database;
        this.database.retrieveCollection(Database.DB_MEAL_PLAN, this);
    }

    /**
     * Generates the meal plan schedule for this week
     */
    private void generateMealPlans() {
        mealPlans = new ArrayList<>();
        mealPlanDict = new HashMap<>();
        LocalDate today = LocalDate.now();
        for (int i = 0; i < futureDays; i++) {
            String date = DateTimeFormatter.ofPattern(dateTimeFormat).format(today);
            makeMealPlan(date, Meal.Breakfast);
            makeMealPlan(date, Meal.Lunch);
            makeMealPlan(date, Meal.Supper);
            today = today.plusDays(1);
        }
    }

    /**
     * Adds a schedule for a meal on the given date in the meal plan schedule
     * @param date
     * @param meal
     */
    private void makeMealPlan(String date, Meal meal) {
        String mealPlanName = date + " " + meal.toString();
        MealPlan mealPlan = new MealPlan(mealPlanName);
        mealPlans.add(mealPlan);
        mealPlanDict.put(mealPlanName, mealPlan);
    }

    /**
     * Returns all the meal plans
     * @return
     */
    public ArrayList<MealPlan> getMealPlans() {
        return mealPlans;
    }

    /**
     * Gets all the meal plan names
     * @return
     */
    public Set<String> getMealPlanNames() {
        return mealPlanDict.keySet();
    }

    /**
     * Adds a meal plan
     * @param mealPlan
     */
    public void addMealPlan(MealPlan mealPlan) {
        mealPlans.add(mealPlan);
    }

    /**
     * Adds a meal plan locally
     * @param name
     * @param ingredients
     * @param recipes
     */
    public void addMealPlanLocally(String name, IngredientCollection ingredients, BaseRecipeCollection recipes) {
        MealPlan meal = mealPlanDict.get(name);
        if (meal != null) {
            meal.setIngredients(ingredients);
            meal.setRecipes(recipes);
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Updates a meal in the meal plan schedule
     * @param index
     * @param mealPlan
     */
    public void updateMealPlan(int index, MealPlan mealPlan) {
        mealPlans.set(index, mealPlan);
        if (database != null){
            database.addDocument(mealPlan);
        }

    }
}
