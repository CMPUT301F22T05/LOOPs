package com.example.loops;

import junit.framework.TestCase;

import org.junit.jupiter.api.Test;

import java.time.Duration;

/**
 * JUNIT test for Recipe class
 */
public class RecipeTest extends TestCase {
    Recipe testRecipe;
    Ingredient carrot;
    IngredientCollection ingredients;
    float amount;



    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
    }

    void initialize() {
        testRecipe = new Recipe();
        ingredients = new IngredientCollection();

        carrot = new Ingredient("Carrot", "10/24/22", "Fridge",amount , "units","snack");
        ingredients.addIngredient(carrot);

        amount = 10;

        testRecipe.setTitle("Baked carrots");
        testRecipe.setCategory("Vegetables");
        testRecipe.setComments("Bake in oven at 350F");
        testRecipe.setIngredients(ingredients);
        testRecipe.setNumServing(3);
        testRecipe.setPrepTimeHour(2);
        testRecipe.setPrepTimeMinute(15);

    }

    @Test
    void testGetTitle() {
        initialize();
        assertEquals("Baked carrots", testRecipe.getTitle());
        Recipe pizzaRecipe = new Recipe("Pizza", 2);
        assertEquals("Pizza", pizzaRecipe.getTitle());
    }

    @Test
    void testSetTitle() {
        initialize();
        Recipe pizzaRecipe = new Recipe();
        pizzaRecipe.setTitle("Pizza");
        assertEquals("Pizza", pizzaRecipe.getTitle());
        testRecipe.setTitle("Oven Baked Carrots");
        assertEquals("Oven Baked Carrots", testRecipe.getTitle());
    }

    @Test
    void testGetPrepTimeHour() {
        initialize();
        Duration testDuration = Duration.ofHours(2);
        assertEquals(testDuration, testRecipe.getPrepTimeHour());
    }

    @Test
    void testSetPrepTimeHour() {
        initialize();
        Recipe pizzaRecipe = new Recipe("Pizza", 2);
        pizzaRecipe.setPrepTimeHour(3);
        assertEquals(3, testRecipe.getPrepTimeHour());
    }

    public void testGetNumServing() {
        initialize();
        assertEquals(3, testRecipe.getNumServing());
    }

    public void testSetNumServing() {
        initialize();
    }

    public void testGetCategory() {
        initialize();
    }

    public void testSetCategory() {
        initialize();
    }

}