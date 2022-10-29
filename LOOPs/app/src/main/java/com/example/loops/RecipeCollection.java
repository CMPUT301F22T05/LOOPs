package com.example.loops;

import java.util.ArrayList;

/**
 *  Control class for Recipes
 */
public class RecipeCollection {

    private ArrayList<Recipe> allRecipes = new ArrayList<>();

    public void updateAllRecipes(){
        //function for using db data to populate list of recipes
    }

    public void addRecipe(Recipe recipe){
        if (!allRecipes.contains(recipe)){
            allRecipes.add(recipe);
        }
    }

    public Recipe getRecipe(int recipeInd){
        return allRecipes.get(recipeInd);
    }

    public void updateRecipe(int recipeInd, Recipe newRecipe){
        allRecipes.set(recipeInd, newRecipe);
    }


}
