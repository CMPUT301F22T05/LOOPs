package com.example.loops;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
                "10/28/2022",
                "cupboard",
                1,
                "",
                "x");
        ingredient2 = new Ingredient(
                "b",
                "10/29/2022",
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
                new Date(0),
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
                new Date(0),
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
        assertEquals("2022-10-29",
                dateFormatter.format(storage.getIngredients().get(0).getBestBeforeDate()));
        assertEquals("2022-10-28",
                dateFormatter.format(storage.getIngredients().get(1).getBestBeforeDate()));
        storage.sort(IngredientSortOption.BY_BEST_BEFORE_DATE_ASCENDING);
        assertEquals("2022-10-28",
                dateFormatter.format(storage.getIngredients().get(0).getBestBeforeDate()));
        assertEquals("2022-10-29",
                dateFormatter.format(storage.getIngredients().get(1).getBestBeforeDate()));
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
