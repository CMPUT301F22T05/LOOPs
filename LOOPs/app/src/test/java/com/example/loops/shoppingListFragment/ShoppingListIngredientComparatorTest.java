package com.example.loops.shoppingListFragment;

import static org.junit.jupiter.api.Assertions.*;

import com.example.loops.modelCollections.IngredientCollection;
import com.example.loops.models.Ingredient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShoppingListIngredientComparatorTest {
    private ShoppingListIngredientComparator shoppingList;
    private IngredientCollection storage;
    private Ingredient ingredient1;
    private Ingredient ingredient2;
    private Ingredient ingredient3;

    @BeforeEach
    void initialize() {
        shoppingList = new ShoppingListIngredientComparator();
        storage = new IngredientCollection();
        ingredient1 = new Ingredient(
                "a",
                "2022-10-28",
                "cupboard",
                1,
                "",
                "x");
        ingredient2 = new Ingredient(
                "b",
                "2022-10-29",
                "fridge",
                1,
                "",
                "y");
        ingredient3 = new Ingredient(
                "a",
                "2022-10-28",
                "cupboard",
                5,
                "",
                "x");
        storage.addIngredient(ingredient1);
        storage.addIngredient(ingredient2);
        storage.addIngredient(ingredient3);
    }

    @Test
    void testComparator(){
        assertEquals(Double.MAX_VALUE, shoppingList.);
    }

}