package com.example.loops.modelCollections;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.loops.models.Recipe;
import com.example.loops.sortOption.RecipeSortOption;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *  A container storing model recipe classes
 *  it can contain number of recipes and keep track of them
 *  recipe can be removed or updated from the collection
 *  new recipe can also be added in
 *  recipes can also be sorted in different ways shown in RecipeSortOption enum
 *  it also contains comparators for Recipe
 */
public class BaseRecipeCollection implements Serializable {
    /* FIXME: I think it is better to rename this to RecipeCollection and the old RecipeCollection
           to RecipeStorage.
           However, I am not renaming it right now because it might conflict with other's work
   */
    protected ArrayList<Recipe> allRecipes;

    /**
     * Constructor
     */
    public BaseRecipeCollection() {
        allRecipes = new ArrayList<>();
    }

    /**
     * Method to add a recipe to the collection of recipes
     * @param recipe (Recipe)
     */
    public void addRecipe(Recipe recipe){
        if (!allRecipes.contains(recipe)){
            allRecipes.add(recipe);
        }
    }

    /**
     * Method to remove a recipe from the collection of recipes
     * @param indexToDelete (int)
     * @return true if deleted, false otherwise
     */
    public boolean deleteRecipe(int indexToDelete){
        try {
            allRecipes.remove(indexToDelete);
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
        return true;
    }

    /**
     * Method to get a recipe from the collection of recipes
     * @param recipeInd (int)
     * @return Recipe
     */
    public Recipe getRecipe(int recipeInd){
        return allRecipes.get(recipeInd);
    }

    /**
     * Method to get all recipes in the collection as an array list
     * @return ArrayList of recipes
     */
    public ArrayList<Recipe> getAllRecipes(){
        return allRecipes;
    }

    /**
     * Method to update (edit) a recipe by overwriting it in the collection
     * @param recipeInd (int)
     * @param newRecipe (Recipe)
     * @return true if deleted, false otherwise
     */
    public boolean updateRecipe(int recipeInd, Recipe newRecipe){
        try {
            allRecipes.set(recipeInd, newRecipe);
        }
        catch (IndexOutOfBoundsException e) {
            return false;
        }
        return true;
    }

    /**
     * Returns true if the collection contains a recipe with same attributes except num servings
     * @param toCheck the recipe to check for containment
     * @return true if it does. Otherwise false
     */
    public boolean containsSameRecipeIgnoringQuantity(Recipe toCheck) {
        return allRecipes.stream().anyMatch(
            (recipe) -> { return
                    recipe.equalsIgnoringQuantity(toCheck);
            });
    }

    /**
     * Returns the number of recipes in the collection
     * @return
     */
    public int size() {
        return allRecipes.size();
    }

    /**
     * Method that calls the appropriate class based on the user sorting selection
     * @param option (RecipeSortOption)
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void sort(RecipeSortOption option) {
        if (option.equals(RecipeSortOption.BY_TITLE_ASCENDING)) {
            Collections.sort(allRecipes, new BaseRecipeCollection.TitleAscendingComparator());
        }
        else if (option.equals(RecipeSortOption.BY_PREP_TIME_ASCENDING)) {
            allRecipes.sort(new BaseRecipeCollection.PrepTimeAscendingComparator());
        }
        else if (option.equals(RecipeSortOption.BY_CATEGORY_ASCENDING)) {
            allRecipes.sort(new BaseRecipeCollection.CategoryAscendingComparator());
        }
        else if (option.equals(RecipeSortOption.BY_TITLE__DESCENDING)) {
            Collections.sort(allRecipes, (new BaseRecipeCollection.TitleAscendingComparator().reversed()));
        }
        else if (option.equals(RecipeSortOption.BY_PREP_TIME_DESCENDING)) {
            allRecipes.sort(new BaseRecipeCollection.PrepTimeAscendingComparator().reversed());
        }
        else if (option.equals(RecipeSortOption.BY_CATEGORY_DESCENDING)) {
            allRecipes.sort(new BaseRecipeCollection.CategoryAscendingComparator().reversed());
        }
    }

    /**
     * Class for comparing recipes by title
     */
    class TitleAscendingComparator implements Comparator<Recipe> {
        @Override
        public int compare(Recipe o1, Recipe o2) {
            return o1.getTitle().compareTo(o2.getTitle());
        }
    }

    /**
     * Class for comparing recipes by preparation time
     */
    class PrepTimeAscendingComparator implements Comparator<Recipe> {
        @Override
        public int compare(Recipe o1, Recipe o2) {
            return o1.getPrepTime().compareTo(o2.getPrepTime());
        }
    }

    /**
     * Class for comparing recipes by categories
     */
    class CategoryAscendingComparator implements Comparator<Recipe> {
        @Override
        public int compare(Recipe o1, Recipe o2) {
            return o1.getCategory().compareTo(o2.getCategory());
        }
    }
}
