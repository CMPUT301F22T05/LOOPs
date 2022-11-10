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
        assertEquals(2, storage.getIngredients().size());
        storage.addIngredient(ingredient);
        assertEquals(3, storage.getIngredients().size());
    }

    @Test
    void testDelete() {
        assertFalse(storage.deleteIngredient(-1));
        assertEquals(2, storage.getIngredients().size());
        storage.deleteIngredient(1);
        assertEquals(1, storage.getIngredients().size());
        storage.deleteIngredient(0);
        assertEquals(0, storage.getIngredients().size());
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
        assertFalse(storage.updateIngredient(3, ingredient));
        assertEquals("a", storage.getIngredients().get(0).getDescription());
        storage.updateIngredient(0, ingredient);
        assertEquals("aa", storage.getIngredients().get(0).getDescription());
    }

    @Test
    void testSortByDescription() {
        storage.sort(IngredientSortOption.BY_DESCRIPTION_DESCENDING);
        assertEquals("b", storage.getIngredients().get(0).getDescription());
        assertEquals("a", storage.getIngredients().get(1).getDescription());
        storage.sort(IngredientSortOption.BY_DESCRIPTION_ASCENDING);
        assertEquals("a", storage.getIngredients().get(0).getDescription());
        assertEquals("b", storage.getIngredients().get(1).getDescription());
    }

    @Test
    void testSortByBestBeforeDate() {
        storage.sort(IngredientSortOption.BY_BEST_BEFORE_DATE_DESCENDING);
        assertEquals("2022-10-29", storage.getIngredients().get(0).getBestBeforeDateString());
        assertEquals("2022-10-28", storage.getIngredients().get(1).getBestBeforeDateString());
        storage.sort(IngredientSortOption.BY_BEST_BEFORE_DATE_ASCENDING);
        assertEquals("2022-10-28", storage.getIngredients().get(0).getBestBeforeDateString());
        assertEquals("2022-10-29", storage.getIngredients().get(1).getBestBeforeDateString());
    }

    @Test
    void testSortByLocation() {
        storage.sort(IngredientSortOption.BY_LOCATION_DESCENDING);
        assertEquals("fridge", storage.getIngredients().get(0).getStoreLocation());
        assertEquals("cupboard", storage.getIngredients().get(1).getStoreLocation());
        storage.sort(IngredientSortOption.BY_LOCATION_ASCENDING);
        assertEquals("cupboard", storage.getIngredients().get(0).getStoreLocation());
        assertEquals("fridge", storage.getIngredients().get(1).getStoreLocation());
    }

    @Test
    void testSortByCategory() {
        storage.sort(IngredientSortOption.BY_CATEGORY_DESCENDING);
        assertEquals("y", storage.getIngredients().get(0).getCategory());
        assertEquals("x", storage.getIngredients().get(1).getCategory());
        storage.sort(IngredientSortOption.BY_CATEGORY_ASCENDING);
        assertEquals("x", storage.getIngredients().get(0).getCategory());
        assertEquals("y", storage.getIngredients().get(1).getCategory());
    }
}
