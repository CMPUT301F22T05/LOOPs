package com.example.loops.modelCollections;

import com.example.loops.database.Database;
import com.example.loops.models.MealPlan;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class MealPlanCollection {
    public final static String dateTimeFormat = "yyyy-MM-dd";
    public final static int futureDays = 7;
    private ArrayList<MealPlan> mealPlans;
    private HashMap<String, MealPlan> mealPlanDict;
    private Database database;

    private enum Meal {
        Breakfast,
        Lunch,
        Supper
    }

    public MealPlanCollection() {
        generateMealPlans();
    }

    public MealPlanCollection(Database database) {
        generateMealPlans();
        this.database = database;
        this.database.retrieveCollection(Database.DB_MEAL_PLAN, this);
    }

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

    private void makeMealPlan(String date, Meal meal) {
        String mealPlanName = date + " " + meal.toString();
        MealPlan mealPlan = new MealPlan(mealPlanName);
        mealPlans.add(mealPlan);
        mealPlanDict.put(mealPlanName, mealPlan);
    }

    public ArrayList<MealPlan> getMealPlans() {
        return mealPlans;
    }

    public Set<String> getMealPlanNames() {
        return mealPlanDict.keySet();
    }

    public void addMealPlan(MealPlan mealPlan) {
        mealPlans.add(mealPlan);
    }

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

    public void updateMealPlan(int index, MealPlan mealPlan) {
        mealPlans.set(index, mealPlan);
        if (database != null){
            database.addDocument(mealPlan);
        }

    }
}
