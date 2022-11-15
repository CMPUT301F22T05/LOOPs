package com.example.loops.models;

import com.example.loops.modelCollections.IngredientCollection;
import com.example.loops.modelCollections.RecipeCollection;

public class Meal {
    private String name;
    private IngredientCollection ingredients;
    private RecipeCollection recipes;

    public Meal(String name, IngredientCollection ingredients, RecipeCollection recipes) {
        this.name = name;
        this.ingredients = ingredients;
        this.recipes = recipes;
    }

    public Meal(String name) {
        this.name = name;
        ingredients = new IngredientCollection();
        recipes = new RecipeCollection();
    }
}
