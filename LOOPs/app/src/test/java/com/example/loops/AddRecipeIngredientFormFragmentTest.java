package com.example.loops;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.loops.modelCollections.IngredientCollection;
import com.example.loops.models.Ingredient;

import junit.framework.TestCase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AddRecipeIngredientFormFragmentTest extends TestCase {

    private IngredientCollection storage;
    private Ingredient ingredient1;
    private Ingredient ingredient2;

    @BeforeEach
    void initialize() {
        storage = new IngredientCollection();
        ingredient1 = new Ingredient(
                "a",
                "2022-01-01",
                "cupboard",
                1,
                "kg",
                "x");
        ingredient2 = new Ingredient(
                "b",
                "2022-01-01",
                "fridge",
                2,
                "g",
                "y");
        storage.addIngredient(ingredient1);
        storage.addIngredient(ingredient2);
    }

    @Test
    public void testSendResult(){
        assertEquals("a", storage.getIngredients().get(0).getDescription());
        assertEquals("b", storage.getIngredients().get(1).getDescription());

        assertEquals("2022-01-01", storage.getIngredients().get(0).getBestBeforeDateString());
        assertEquals("2022-01-01", storage.getIngredients().get(1).getBestBeforeDateString());

        assertEquals("cupboard", storage.getIngredients().get(0).getStoreLocation());
        assertEquals("fridge", storage.getIngredients().get(1).getStoreLocation());

        assertEquals(1.0, storage.getIngredients().get(0).getAmount());
        assertEquals(2.0, storage.getIngredients().get(1).getAmount());

        assertEquals("kg", storage.getIngredients().get(0).getUnit());
        assertEquals("g", storage.getIngredients().get(1).getUnit());

        assertEquals("x", storage.getIngredients().get(0).getCategory());
        assertEquals("y", storage.getIngredients().get(1).getCategory());

    }

}
