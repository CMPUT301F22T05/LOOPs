package com.example.loops;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *  Control class for Recipe's
 */
public class RecipeCollection {
    /**
     * Attributes of the RecipeCollection
     */
    private ArrayList<Recipe> allRecipes;

    /**
     * Constructor
     */
    public RecipeCollection() {
        allRecipes = new ArrayList<>();
    }


    /**
     * Unused for now, no function
     */
    public void updateAllRecipes(){
        //function for using db data to populate list of recipes
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
     */
    public void deleteRecipe(int indexToDelete){
            allRecipes.remove(indexToDelete);
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
     */
    public void updateRecipe(int recipeInd, Recipe newRecipe){
        allRecipes.set(recipeInd, newRecipe);
    }

    /**
     * Method that calls the appropriate class based on the user sorting selection
     * @param option (RecipeSortOption)
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void sort(RecipeSortOption option) {
        if (option.equals(RecipeSortOption.BY_TITLE_ASCENDING)) {
            Collections.sort(allRecipes, new RecipeCollection.TitleAscendingComparator());
        }
        else if (option.equals(RecipeSortOption.BY_PREP_TIME_ASCENDING)) {
            allRecipes.sort(new RecipeCollection.PrepTimeAscendingComparator());
        }
        else if (option.equals(RecipeSortOption.BY_CATEGORY_ASCENDING)) {
            allRecipes.sort(new RecipeCollection.CategoryAscendingComparator());
        }
        else if (option.equals(RecipeSortOption.BY_TITLE__DESCENDING)) {
            Collections.sort(allRecipes, (new TitleAscendingComparator().reversed()));
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
