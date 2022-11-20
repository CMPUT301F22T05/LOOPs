package com.example.loops.shoppingListFragment;

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
            for (Ingredient storageIngredient : currentIngredients.getIngredients()) {

                // for ingredient that is pending to finish detail, shopping list will ignore them
                // if there is a match in description & category of pending ingredient,
                // we don't generate shopping list
                if (storageIngredient.getPending()) {
                    if (ShoppingListIngredientComparator.hasPending(mpIngredient, storageIngredient)) {
                        break;
                    }
                    continue;
                }

                // generate shopping list for matching result
                Double amountDiff = ShoppingListIngredientComparator.getAmountDiff(mpIngredient, storageIngredient);
                if (amountDiff < 0) {
                    Ingredient shoppingIngredient = new Ingredient(mpIngredient);
                    shoppingIngredient.setAmount(-amountDiff);
                    shoppingList.addIngredient(shoppingIngredient);
                }
            }
        }

        return shoppingList;
    }
}
