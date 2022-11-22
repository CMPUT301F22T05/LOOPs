package com.example.loops.shoppingListFragment;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.example.loops.MainActivity;
import com.example.loops.modelCollections.IngredientCollection;
import com.example.loops.modelCollections.MealPlanCollection;
import com.example.loops.models.Ingredient;
import com.example.loops.models.MealPlan;
import com.example.loops.models.Recipe;

public class ShoppingListInitializer {

    /**
     * Auto generate shopping list mandatory for user to buy.
     * @param currentMealPlans current user meal plans
     * @param currentIngredients current ingredient storage
     * @return generated shopping list
     */
    public static IngredientCollection getShoppingList(
            MealPlanCollection currentMealPlans, IngredientCollection currentIngredients) {

        IngredientCollection shoppingList = new IngredientCollection();

        // store an ingredient collection for all meal plan ingredients
        IngredientCollection mealPlanIngredients = new IngredientCollection();
        for (MealPlan mealPlan : currentMealPlans.getMealPlans()) {
            // get all ingredient from meal plan recipe
            for (Recipe recipe : mealPlan.getRecipes().getAllRecipes()) {
                for (Ingredient ingredient : recipe.getIngredients().getIngredients()) {
                    Ingredient mpIngredient = new Ingredient(ingredient);
                    mpIngredient.setBestBeforeDate("2022-01-01");
                    mpIngredient.setStoreLocation("Pantry");
                    mealPlanIngredients.addIngredient(mpIngredient);
                }
            }
            // get all ingredient from meal plan ingredient
            for (Ingredient ingredient : mealPlan.getIngredients().getIngredients()) {
                Ingredient mpIngredient = new Ingredient(ingredient);
                mpIngredient.setBestBeforeDate("2022-01-01");
                mpIngredient.setStoreLocation("Pantry");
                mealPlanIngredients.addIngredient(mpIngredient);
            }
        }

        // compare one by one the ingredient
        for (Ingredient mpIngredient : mealPlanIngredients.getIngredients()) {
            Log.e(TAG, mpIngredient.getDescription());
            double difference = mpIngredient.getAmount();
            for (Ingredient storageIngredient : currentIngredients.getIngredients()) {

                // for ingredient that is pending to finish detail, shopping list will ignore them
                // if there is a match in description & category of pending ingredient,
                // we don't generate shopping list
                if (storageIngredient.getPending()) {
                    if (ShoppingListIngredientComparator.hasPending(mpIngredient, storageIngredient)) {
                        difference = -1;
                        break;
                    }
                    continue;
                }

                // minus the amount of matched storage ingredient
                difference -= ShoppingListIngredientComparator.getStoredAmount(mpIngredient, storageIngredient);
            }

            // if difference still more than 0, append to shopping list
            if (difference > 0) {
                Ingredient shoppingItem = new Ingredient(mpIngredient);
                shoppingItem.setAmount(difference);
                shoppingList.addIngredient(shoppingItem);
            }
        }

        return shoppingList;
    }
}
