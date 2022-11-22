package com.example.loops.modelCollections;

import com.example.loops.database.Database;
import com.example.loops.models.Recipe;

/**
 *  Control class for recipes
 *  it can contain number of recipes and keep track of them
 *  recipe can be removed or updated from the collection
 *  new recipe can also be added in
 *  recipes can also be sorted in different ways shown in RecipeSortOption enum
 *  it also contains comparators for Recipe
 */
public class RecipeCollection extends BaseRecipeCollection {
    /**
     * Attributes of the RecipeCollection
     */
    private Database database;

    /**
     * Constructor with no database interaction
     */
    public RecipeCollection() {
        database = null;
    }

    /**
     * Constructor with database data pre-loaded
     * @param database database singleton
     */
    public RecipeCollection(Database database) {
        this.database = database;
        updateAllRecipes();
    }

    /**
     * Update database data to recipe collection.
     */
    public void updateAllRecipes(){
        database.retrieveCollection(Database.DB_RECIPE, this);
    }

    /**
     * Method to add a recipe to the collection of recipes
     * @param recipe (Recipe)
     */
    public void addRecipe(Recipe recipe){
        super.addRecipe(recipe);
        if (database != null)
            database.addDocument(recipe);
    }

    /**
     * Method to add a recipe locally without adding to the database
     * @param recipe (Recipe)
     */
    public void addRecipeLocally(Recipe recipe){
        super.addRecipe(recipe);
    }

    /**
     * Method to remove a recipe from the collection of recipes
     * @param indexToDelete (int)
     * @return true if deleted, false otherwise
     */
    public boolean deleteRecipe(int indexToDelete){
        if (database != null)
            database.deleteDocument(allRecipes.get(indexToDelete));
        return super.deleteRecipe(indexToDelete);
    }

    /**
     * Method to update (edit) a recipe by overwriting it in the collection
     * @param recipeInd (int)
     * @param newRecipe (Recipe)
     * @return true if deleted, false otherwise
     */
    public boolean updateRecipe(int recipeInd, Recipe newRecipe){
        if (database != null)
            database.updateDocument(allRecipes.get(recipeInd), newRecipe);
        return super.updateRecipe(recipeInd, newRecipe);
    }
}
