package com.example.loops;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.example.loops.modelCollections.RecipeCollection;
import com.example.loops.models.Recipe;

import junit.framework.TestCase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

/**
 *  JUNIT test for recipeCollection
 */
public class RecipeCollectionTest extends TestCase {

    private RecipeCollection recipeBook;
    private Recipe recipe1;
    private Recipe recipe2;

    @BeforeEach
    void initialize() {
        recipeBook = new RecipeCollection();
        recipe1 = new Recipe(
                "Pizza",
                Duration.ofHours(2),
                "Supper",
                4,
                "Just like in Italy"
        );
        recipe2 = new Recipe(
                "Grilled Cheese",
                Duration.ofMinutes(30),
                "Lunch",
                1,
                "Classic"
        );
        recipeBook.addRecipe(recipe1);
        recipeBook.addRecipe(recipe2);
    }

    @Test
    void testAddRecipe(){
        Recipe recipe3 = new Recipe(
                "Burger",
                Duration.ofMinutes(45),
                "Lunch",
                2,
                "Better than McDonalds"
        );
        assertEquals(2, recipeBook.getAllRecipes().size());
        recipeBook.addRecipe(recipe3);
        assertEquals(3, recipeBook.getAllRecipes().size());
    }

    @Test
    void testDelete(){
        assertFalse(recipeBook.deleteRecipe(-1));
        assertEquals(2, recipeBook.getAllRecipes().size());
        recipeBook.deleteRecipe(1);
        assertEquals(1, recipeBook.getAllRecipes().size());
        recipeBook.deleteRecipe(0);
        assertEquals(0, recipeBook.getAllRecipes().size());
    }

    @Test
    public void testGetRecipe() {
        Recipe recipe3;
        recipe3 = recipeBook.getRecipe(1);
        assertEquals("Grilled Cheese", recipe3.getTitle());
    }

    @Test
    public void testUpdateRecipe() {
        Recipe recipe3 = new Recipe(
                "Burger",
                Duration.ofMinutes(45),
                "Lunch",
                2,
                "Better than McDonalds"
        );
        assertFalse(recipeBook.updateRecipe(3, recipe3));
        assertEquals("Pizza", recipeBook.getAllRecipes().get(0).getTitle());
        recipeBook.updateRecipe(0, recipe3);
        assertEquals("Burger", recipeBook.getAllRecipes().get(0).getTitle());
    }

}