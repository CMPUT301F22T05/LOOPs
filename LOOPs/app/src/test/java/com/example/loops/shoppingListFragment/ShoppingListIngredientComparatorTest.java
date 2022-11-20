package com.example.loops.shoppingListFragment;

import static org.junit.jupiter.api.Assertions.*;

import com.example.loops.modelCollections.IngredientCollection;
import com.example.loops.models.Ingredient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShoppingListIngredientComparatorTest {
    private ShoppingListIngredientComparator shoppingList;
    private IngredientCollection storage;
    private IngredientCollection mealPlan;
    private Ingredient ingredient1;
    private Ingredient ingredient2;
    private Ingredient ingredient3;

    @BeforeEach
    void initialize() {
        shoppingList = new ShoppingListIngredientComparator();
        storage = new IngredientCollection();
        mealPlan = new IngredientCollection();
        ingredient1 = new Ingredient(
                "Butter",
                "2022-10-28",
                "fridge",
                1,
                "kg",
                "x");
        ingredient2 = new Ingredient(
                "Salt",
                "2022-10-29",
                "fridge",
                1,
                "g",
                "y");
        ingredient3 = new Ingredient(
                "Butter",
                "2022-10-28",
                "fridge",
                5,
                "kg",
                "x");
        mealPlan.addIngredient(ingredient1);
        storage.addIngredient(ingredient2);
        storage.addIngredient(ingredient3);
    }

    @Test
    void testComparator(){
        assertEquals(Double.MAX_VALUE, ShoppingListIngredientComparator.getAmountDiff(ingredient1, ingredient2));
        double amountNeeded = ShoppingListIngredientComparator.getAmountDiff(ingredient1, ingredient3);
        assertEquals(-4, amountNeeded);
    }

}