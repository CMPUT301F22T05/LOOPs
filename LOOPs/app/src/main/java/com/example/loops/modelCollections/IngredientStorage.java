package com.example.loops.modelCollections;

import com.example.loops.database.Database;
import com.example.loops.models.Ingredient;

public class IngredientStorage extends IngredientCollection {
    private Database database;

    public IngredientStorage(Database manager) {
        super();
        database = manager;
        getIngredientStorageFromDataBase();
    }

    private void getIngredientStorageFromDataBase() {
        database.retrieveCollection(Database.DB_INGREDIENT, this);
    }

    public void addIngredientLocal(Ingredient ingredient) {
        super.addIngredient(ingredient);
    }

    @Override
    public void addIngredient(Ingredient ingredient) {
        database.addDocument(ingredient);
        super.addIngredient(ingredient);
    }

    @Override
    public boolean deleteIngredient(int index) {
        database.deleteDocument(ingredients.get(index));
        return super.deleteIngredient(index);
    }

    @Override
    public boolean updateIngredient(int index, Ingredient ingredient) {
        database.updateDocument(ingredients.get(index), ingredient);
        return super.updateIngredient(index, ingredient);
    }
}
