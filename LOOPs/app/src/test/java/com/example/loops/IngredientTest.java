package com.example.loops;

import static org.junit.jupiter.api.Assertions.*;

import com.example.loops.models.Ingredient;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.util.Date;

/**
 * Test cases for the Ingredient class methods
 */
public class IngredientTest {
    private Ingredient ingredient;
    private LocalDate date;

    @BeforeEach
    void initialize() {
        ingredient = new Ingredient(
                "apple",
                "2022-11-04",
                "pantry",
                1,
                "kg",
                "fruit");
        date = LocalDate.now();
    }

    @Test
    void testGetters() {
        assertEquals("apple", ingredient.getDescription());
        assertEquals("2022-11-04", ingredient.getBestBeforeDateString());
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
        ingredient.setBestBeforeDate("2022-11-10");
        assertEquals("2022-11-10", ingredient.getBestBeforeDateString());
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
                "2022-11-04",
                "pantry",
                1,
                "kg",
                "fruit");

        assertTrue(ingredient.equals(comparedIngredient));
    }
}
