package com.example.loops;

import static org.junit.jupiter.api.Assertions.*;

import com.example.loops.modelCollections.IngredientCollection;
import com.example.loops.models.Ingredient;
import com.example.loops.sortOption.IngredientSortOption;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

/**
 * Test cases for the IngredientCollection class methods
 */
public class IngredientCollectionTest {
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private IngredientCollection storage;
    private Ingredient ingredient1;
    private Ingredient ingredient2;

    @BeforeEach
    void initialize() {
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
        storage.addIngredient(ingredient1);
        storage.addIngredient(ingredient2);
    }

    @Test
    void testAdd() {
        Ingredient ingredient = new Ingredient(
                "",
                LocalDate.now(),
                "",
                1,
                "",
                "");
        assertEquals(2, storage.size());
        storage.addIngredient(ingredient);
        assertEquals(3, storage.size());
    }

    @Test
    void testDelete() {
        assertFalse(storage.deleteIngredient(-1));
        assertEquals(2, storage.size());
        storage.deleteIngredient(1);
        assertEquals(1, storage.size());
        storage.deleteIngredient(0);
        assertEquals(0, storage.size());
    }

    @Test
    void testUpdate() {
        Ingredient ingredient = new Ingredient(
                "aa",
                LocalDate.now(),
                "",
                1,
                "",
                "");
        //assertFalse(storage.updateIngredient(3, ingredient));
        assertEquals("a", storage.get(0).getDescription());
        storage.updateIngredient(0, ingredient);
        assertEquals("aa", storage.get(0).getDescription());
    }

    @Test
    void testContains() {
        assertTrue( storage.contains(ingredient1) );
        assertTrue( storage.contains(ingredient2) );

        Ingredient shouldNotContain = new Ingredient(
        "xxx",
        "2022-10-28",
        "board",
        5,
        "",
        "");
        assertFalse( storage.contains(shouldNotContain) );
    }

    @Test
    void testContainsSameDescriptionAndCategory() {
        Ingredient shouldContain = new Ingredient(
                "a",
                "1965-10-11",
                "diff",
                5000,
                "diff",
                "x");
        Ingredient shouldNotContain1 = new Ingredient(
                "diff",
                "1965-10-11",
                "diff",
                5000,
                "diff",
                "x");
        Ingredient shouldNotContain2 = new Ingredient(
                "a",
                "1965-10-11",
                "diff",
                5000,
                "diff",
                "diff");

        assertTrue( storage.containsDescriptionAndCategory(shouldContain) );
        assertFalse( storage.containsDescriptionAndCategory(shouldNotContain1) );
        assertFalse( storage.containsDescriptionAndCategory(shouldNotContain2) );
    }

    @Test
    void testGetIngredient() {
        Ingredient actualIng1 = storage.get(0);
        Ingredient actualIng2 = storage.get(1);

        assertEquals(ingredient1, actualIng1);
        assertEquals(ingredient2, actualIng2);
    }

    @Test
    void testSortByDescription() {
        storage.sort(IngredientSortOption.BY_DESCRIPTION_DESCENDING);
        assertEquals("b", storage.get(0).getDescription());
        assertEquals("a", storage.get(1).getDescription());
        storage.sort(IngredientSortOption.BY_DESCRIPTION_ASCENDING);
        assertEquals("a", storage.get(0).getDescription());
        assertEquals("b", storage.get(1).getDescription());
    }

    @Test
    void testSortByBestBeforeDate() {
        storage.sort(IngredientSortOption.BY_BEST_BEFORE_DATE_DESCENDING);
        assertEquals("2022-10-29", storage.get(0).getBestBeforeDateString());
        assertEquals("2022-10-28", storage.get(1).getBestBeforeDateString());
        storage.sort(IngredientSortOption.BY_BEST_BEFORE_DATE_ASCENDING);
        assertEquals("2022-10-28", storage.get(0).getBestBeforeDateString());
        assertEquals("2022-10-29", storage.get(1).getBestBeforeDateString());
    }

    @Test
    void testSortByLocation() {
        storage.sort(IngredientSortOption.BY_LOCATION_DESCENDING);
        assertEquals("fridge", storage.get(0).getStoreLocation());
        assertEquals("cupboard", storage.get(1).getStoreLocation());
        storage.sort(IngredientSortOption.BY_LOCATION_ASCENDING);
        assertEquals("cupboard", storage.get(0).getStoreLocation());
        assertEquals("fridge", storage.get(1).getStoreLocation());
    }

    @Test
    void testSortByCategory() {
        storage.sort(IngredientSortOption.BY_CATEGORY_DESCENDING);
        assertEquals("y", storage.get(0).getCategory());
        assertEquals("x", storage.get(1).getCategory());
        storage.sort(IngredientSortOption.BY_CATEGORY_ASCENDING);
        assertEquals("x", storage.get(0).getCategory());
        assertEquals("y", storage.get(1).getCategory());
    }
}
