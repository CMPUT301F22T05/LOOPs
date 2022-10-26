package com.example.loops;

import java.util.ArrayList;

public class IngredientCollection {
    private ArrayList<Ingredient> ingredients;

    public IngredientCollection() {
        ingredients = new ArrayList<>();
    }

    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
    }

    public void deleteIngredient(int index) {
        ingredients.remove(index);
    }

    public void updateIngredient(int index, Ingredient ingredient) {
        ingredients.set(index, ingredient);
    }

    public void sort(IngredientSortOption option) {

    }
}
