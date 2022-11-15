package com.example.loops.models;

import com.example.loops.modelCollections.IngredientCollection;
import com.example.loops.modelCollections.RecipeCollection;

public class MealPlan {
    private String name;
    private IngredientCollection ingredients;
    private RecipeCollection recipes;

    public MealPlan(String name, IngredientCollection ingredients, RecipeCollection recipes) {
        this.name = name;
        this.ingredients = ingredients;
        this.recipes = recipes;
    }

    public MealPlan(String name) {
        this.name = name;
        ingredients = new IngredientCollection();
        recipes = new RecipeCollection();
    }

    public String getName() {
        return name;
    }
}
