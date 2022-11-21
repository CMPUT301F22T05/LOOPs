package com.example.loops.shoppingListFragment;

import androidx.annotation.NonNull;

import com.example.loops.modelCollections.IngredientCollection;
import com.example.loops.models.Ingredient;

public class ShoppingListIngredientComparator {

    /**
     *  Compares two ingredients, when they are the same, returns the amount needed to buy
     * @param ingredient1 - Ingredient from the meal plan recipes
     * @param ingredient2 - Ingredient from the ingredient storage
     * @return - Double representing the amount of an ingredient needed to buy
     */
    public static double getAmountDiff(@NonNull Ingredient ingredient1, @NonNull Ingredient ingredient2) {
        // Check if ingredients have the same description
        if(ingredient1.getDescription().equalsIgnoreCase(ingredient2.getDescription())){
            // Check if ingredients have the same category
            if(ingredient1.getCategory().equalsIgnoreCase(ingredient2.getCategory())){
                // Check if ingredients have the same unit
                if(ingredient1.getUnit().equalsIgnoreCase(ingredient2.getUnit())){
                    // Ingredients match, return the difference
                    // a negative number represents the amount to buy
                    // a positive number means we have enough
                    return ( ingredient1.getAmount() - ingredient2.getAmount());
                }
            }
        }
        // return a max value positive indicating ingredients are not the same
        return Double.MAX_VALUE;
    }

    public static double getStoredAmount(Ingredient mealPlanIngredient, Ingredient storageIngredient) {
        boolean condition = mealPlanIngredient.getDescription().equalsIgnoreCase(storageIngredient.getDescription())
                && mealPlanIngredient.getCategory().equalsIgnoreCase(storageIngredient.getCategory())
                && mealPlanIngredient.getUnit().equalsIgnoreCase(storageIngredient.getUnit());
        if (condition) {
            return storageIngredient.getAmount();
        }
        return 0;
    }

    /**
     * Compare & see if meal plan ingredient has an identical ingredient storage that is pending
     * @param ingredient1 meal plan ingredient
     * @param ingredient2 storage ingredient
     * @return if storage ingredient is pending and matched meal plan ingredient
     */
    public static boolean hasPending(@NonNull Ingredient ingredient1, @NonNull Ingredient ingredient2) {
        return ingredient2.getPending() &&
                ingredient1.getDescription().equalsIgnoreCase(ingredient2.getDescription()) &&
                ingredient1.getCategory().equalsIgnoreCase(ingredient2.getCategory());
    }

}
