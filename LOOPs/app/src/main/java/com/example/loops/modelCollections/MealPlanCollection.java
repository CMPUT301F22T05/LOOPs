package com.example.loops.modelCollections;

import com.example.loops.database.Database;
import com.example.loops.models.MealPlan;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MealPlanCollection {
    private String dateTimeFormat = "yyyy-MM-dd";
    private final int futureDays = 7;
    private ArrayList<MealPlan> mealPlans;
    private Database database;

    public MealPlanCollection() {
        generateMealPlan();
    }

    public MealPlanCollection(Database database) {
        generateMealPlan();
    }

    private void generateMealPlan() {
        mealPlans = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = 0; i < futureDays; i++) {
            String date = DateTimeFormatter.ofPattern(dateTimeFormat).format(today);
            mealPlans.add(new MealPlan(date + " Breakfast"));
            mealPlans.add(new MealPlan(date + " Lunch"));
            mealPlans.add(new MealPlan(date + " Supper"));
            today = today.plusDays(1);
        }
    }

    public ArrayList<MealPlan> getMealPlans() {
        return mealPlans;
    }
}
