package com.example.loops;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import android.graphics.Bitmap;

import com.example.loops.modelCollections.IngredientCollection;
import com.example.loops.models.Ingredient;
import com.example.loops.models.Recipe;

import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

import java.time.Duration;

/**
 * Test Cases for the Recipe class methods
 */
public class RecipeTest extends TestCase {
    Recipe testRecipe;
    IngredientCollection ingredients;
    Ingredient carrot;

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
    }

    void initialize() {
        ingredients = new IngredientCollection();
        carrot = new Ingredient(
                "Carrot",
                "10/24/22",
                "Fridge",
                10,
                "units",
                "snack");
        ingredients.addIngredient(carrot);

        testRecipe = new Recipe();
        Duration x = Duration.ofHours(2);
        Duration y = Duration.ofMinutes(15);
        x.plus(y);
        testRecipe.setTitle("Baked carrots");
        testRecipe.setPrepTime(x);
        testRecipe.setNumServing(3);
        testRecipe.setCategory("Vegetables");
        testRecipe.setIngredients(ingredients);
        testRecipe.setComments("Bake in oven at 350F");
    }

    @Test
    void testGetters() {
        initialize();

        assertEquals("Baked carrots", testRecipe.getTitle());

        Duration x = Duration.ofHours(2);
        Duration y = Duration.ofMinutes(15);
        x.plus(y);
        assertEquals(x, testRecipe.getPrepTime());

        assertEquals(3, testRecipe.getNumServing());

        assertEquals("Vegetables", testRecipe.getCategory());

        assertEquals(ingredients, testRecipe.getIngredients());

        assertEquals("Bake in oven at 350F",testRecipe.getComments());
    }

    @Test
    void testSetters() {
        initialize();
        testRecipe.setTitle("apple");
        assertEquals("apple", testRecipe.getTitle());

        Duration x = Duration.ofHours(3);
        Duration y = Duration.ofMinutes(5);
        x.plus(y);
        testRecipe.setPrepTime(x);
        assertEquals(x, testRecipe.getPrepTime());

        testRecipe.setNumServing(5);
        assertEquals(5, testRecipe.getNumServing());

        testRecipe.setCategory("fruit");
        assertEquals("fruit", testRecipe.getCategory());

        testRecipe.setComments("good apples");
        assertEquals("good apples", testRecipe.getComments());

        ingredients = new IngredientCollection();
        Ingredient meat;
        meat = new Ingredient(
                "beef",
                "9/20/22",
                "Fridge",
                8,
                "units",
                "meat");
        ingredients.addIngredient(meat);
        testRecipe.setIngredients(ingredients);
        assertEquals(ingredients, testRecipe.getIngredients());

    }


    @Test
    void testEquals() {
        initialize();
        IngredientCollection comparedIngredientCollection  = new IngredientCollection();
        Ingredient comparedIngredient = new Ingredient(
                "Carrot",
                "10/24/22",
                "Fridge",
                10,
                "units",
                "snack");
        comparedIngredientCollection.addIngredient(comparedIngredient);

        Recipe comparedRecipe = new Recipe();
        Duration x = Duration.ofHours(2);
        Duration y = Duration.ofMinutes(15);
        x.plus(y);
        comparedRecipe.setTitle("Baked carrots");
        comparedRecipe.setPrepTime(x);
        comparedRecipe.setNumServing(3);
        comparedRecipe.setCategory("Vegetables");
        comparedRecipe.setIngredients(ingredients);
        comparedRecipe.setComments("Bake in oven at 350F");

        assertTrue(testRecipe.equals(comparedRecipe));
    }

}