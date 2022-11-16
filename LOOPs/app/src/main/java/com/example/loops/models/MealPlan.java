package com.example.loops.models;

import com.example.loops.modelCollections.IngredientCollection;
import com.example.loops.modelCollections.RecipeCollection;

/**
 * The class models a meal, a meal can contains multiple recipes and ingredients
 */
public class MealPlan {
    private String name;
    private IngredientCollection ingredients;
    private RecipeCollection recipes;

    /**
     * the constructor require all fields
     * @param name the meal's name
     * @param ingredients ingredients contained in the meal
     * @param recipes recipes contained in the meal
     */
    public MealPlan(String name, IngredientCollection ingredients, RecipeCollection recipes) {
        this.name = name;
        this.ingredients = ingredients;
        this.recipes = recipes;
    }

    /**
     * easy constructor
     * @param name the meal's name
     */
    public MealPlan(String name) {
        this.name = name;
        ingredients = new IngredientCollection();
        recipes = new RecipeCollection();
    }

    /**
     * get the meal's name
     * @return the meal's name
     */
    public String getName() {
        return name;
    }
}
