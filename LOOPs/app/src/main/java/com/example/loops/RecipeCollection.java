package com.example.loops;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *  Control class for Recipes
 */
public class RecipeCollection {

    private ArrayList<Recipe> allRecipes;

    public RecipeCollection() {
        allRecipes = new ArrayList<>();
    }

    public void updateAllRecipes(){
        //function for using db data to populate list of recipes
    }

    public void addRecipe(Recipe recipe){
        if (!allRecipes.contains(recipe)){
            allRecipes.add(recipe);
        }
    }

    public void deleteRecipe(Recipe recipe){
        if (allRecipes.contains(recipe)){
            allRecipes.remove(recipe);
        }
    }

    public void deleteRecipe(int index){
            allRecipes.remove(index);
    }

    public Recipe getRecipe(int recipeInd){
        return allRecipes.get(recipeInd);
    }

    public void updateRecipe(int recipeInd, Recipe newRecipe){
        allRecipes.set(recipeInd, newRecipe);
    }

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

    class TitleAscendingComparator implements Comparator<Recipe> {
        @Override
        public int compare(Recipe o1, Recipe o2) {
            return o1.getTitle().compareTo(o2.getTitle());
        }
    }

    class PrepTimeAscendingComparator implements Comparator<Recipe> {
        @Override
        public int compare(Recipe o1, Recipe o2) {
            return o1.getPrepTime().compareTo(o2.getPrepTime());
        }
    }

    class CategoryAscendingComparator implements Comparator<Recipe> {
        @Override
        public int compare(Recipe o1, Recipe o2) {
            return o1.getCategory().compareTo(o2.getCategory());
        }
    }

}
