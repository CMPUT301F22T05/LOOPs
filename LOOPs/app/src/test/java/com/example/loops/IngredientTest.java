package com.example.loops;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.Date;

/**
 * Test cases for the Ingredient class methods
 */
public class IngredientTest {
    private Ingredient ingredient;
    private Date date;

    @BeforeEach
    void initialize() {
        ingredient = new Ingredient(
                "apple",
                "11/04/2022",
                "pantry",
                1,
                "kg",
                "fruit");
        date = new Date(0);
    }

    @Test
    void testGetters() {
        assertEquals("apple", ingredient.getDescription());
        assertEquals("11/04/2022", ingredient.getBestBeforeDateString());
        assertEquals("pantry", ingredient.getStoreLocation());
        assertEquals(1.0, ingredient.getAmount());
        assertEquals("kg", ingredient.getUnit());
        assertEquals("fruit", ingredient.getCategory());
    }

    @Test
    void testSetters() {
        ingredient.setDescription("pork");
        assertEquals("pork", ingredient.getDescription());
        ingredient.setBestBeforeDate(date);
        assertEquals(date, ingredient.getBestBeforeDate());
        ingredient.setBestBeforeDate("11/10/2022");
        assertEquals("11/10/2022", ingredient.getBestBeforeDateString());
        ingredient.setStoreLocation("fridge");
        assertEquals("fridge", ingredient.getStoreLocation());
        ingredient.setAmount(3);
        assertEquals(3.0, ingredient.getAmount());
        ingredient.setUnit("pond");
        assertEquals("pond", ingredient.getUnit());
        ingredient.setCategory("meat");
        assertEquals("meat", ingredient.getCategory());
    }

    @Test
    void testEquals() {
        Ingredient comparedIngredient = new Ingredient(
                "apple",
                "11/04/2022",
                "pantry",
                1,
                "kg",
                "fruit");

        assertTrue(ingredient.equals(comparedIngredient));
    }
}
