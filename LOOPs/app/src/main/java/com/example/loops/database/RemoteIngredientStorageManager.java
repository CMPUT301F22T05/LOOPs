package com.example.loops.database;

import com.example.loops.modelCollections.IngredientStorage;
import com.example.loops.models.Ingredient;

import java.util.ArrayList;

public interface RemoteIngredientStorageManager {
    void getIngredientStorage(IngredientStorage ingredientStorage);

    void addIngredientToStorage(Ingredient ingredient);

    void removeIngredientFromStorage(Ingredient ingredient);

    void updateIngredientInStorage(Ingredient oldIngredient, Ingredient newIngredient);
}
