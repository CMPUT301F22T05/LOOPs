package com.example.loops.modelCollections;

import com.example.loops.database.Database;
import com.example.loops.models.Ingredient;

/**
 * Controller class for ingredient stored in user's storage.
 * If given a database, the changes to this class are recorded in the database
 */
public class IngredientStorage extends IngredientCollection {
    private Database database;

    /**
     * Constructor with database access. It initializes the collection with ingredients
     * in database
     * @param manager database to interact with
     */
    public IngredientStorage(Database manager) {
        super();
        database = manager;
        getIngredientStorageFromDataBase();
    }

    /**
     * Retrieves the ingredient collection from database and populate this collection with it
     */
    private void getIngredientStorageFromDataBase() {
        database.retrieveCollection(Database.DB_INGREDIENT, this);
    }

    /**
     * Add an ingredient to this collection without interaction with database
     * @param ingredient
     */
    public void addIngredientLocal(Ingredient ingredient) {
        super.addIngredient(ingredient);
    }

    /**
     * Adds ingredient to collection and also the database
     * @param ingredient
     * @return
     */
    @Override
    public int addIngredient(Ingredient ingredient) {
        int ingIndex = super.addIngredient(ingredient);
        if (ingIndex == -1) {
            database.addDocument(ingredient);
        }
        else {
            database.addDocument(ingredients.get(ingIndex));
        }
        return ingIndex;
    }

    /**
     * Deletes ingredient in collection and also in the database
     * @param index
     * @return
     */
    @Override
    public boolean deleteIngredient(int index) {
        database.deleteDocument(ingredients.get(index));
        return super.deleteIngredient(index);
    }

    /**
     * Updates the ingredient in collection and also in the database
     * @param index (int)
     * @param ingredient (Ingredient)
     * @return
     */
    @Override
    public int updateIngredient(int index, Ingredient ingredient) {

        Ingredient oldIngredient = new Ingredient(ingredients.get(index));
        int dupInd = super.updateIngredient(index, ingredient);
        if (dupInd != index) {
            database.updateDocument(ingredients.get(dupInd), ingredients.get(dupInd));
            deleteIngredient(index);
        } else {
            database.updateDocument(oldIngredient, ingredient);

        }
        return dupInd;
    }
}
