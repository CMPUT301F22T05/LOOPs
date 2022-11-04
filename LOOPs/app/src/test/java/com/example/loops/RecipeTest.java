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
        assertEquals(2, testRecipe.getPrepTimeHour());
    }

    @Test
    void testSetPrepTimeHour() {
        initialize();
        Recipe pizzaRecipe = new Recipe("Pizza", 2);
        pizzaRecipe.setPrepTimeHour(3);
        assertEquals(3, pizzaRecipe.getPrepTimeHour());
    }

    @Test
    void testGetPrepTimeMinute() {
        initialize();
        assertEquals(15, testRecipe.getPrepTimeMinute());
    }

    @Test
    void testSetPrepTimeMinute() {
        initialize();
        Recipe pizzaRecipe = new Recipe("Pizza", 2);
        pizzaRecipe.setPrepTimeMinute(30);
        assertEquals(30, pizzaRecipe.getPrepTimeMinute());
    }

    @Test
    void testGetPrepTime() {
        initialize();
        Recipe pizzaRecipe = new Recipe("Pizza", 2);
        pizzaRecipe.setPrepTimeHour(2);
        pizzaRecipe.setPrepTimeMinute(30);
        assertEquals(150, pizzaRecipe.getPrepTime());
    }

    @Test
    void testGetNumServing() {
        initialize();
        Recipe pizzaRecipe = new Recipe("Pizza", 2);
        assertEquals(3, testRecipe.getNumServing());
        assertEquals(2, pizzaRecipe.getNumServing());
    }

    @Test
    void testSetNumServing() {
        initialize();
        Recipe pizzaRecipe = new Recipe("Pizza", 2);
        pizzaRecipe.setNumServing(4);
        assertEquals(4, pizzaRecipe.getNumServing());
    }

    @Test
    void testGetCategory() {
        initialize();
        Recipe pizzaRecipe = new Recipe("Pizza", 2);
        pizzaRecipe.setCategory("Supper");
        assertEquals("Supper", pizzaRecipe.getCategory());
    }

    @Test
    void testSetCategory() {
        initialize();
        testRecipe.setCategory("Snack");
        Recipe pizzaRecipe = new Recipe("Pizza", 2);
        pizzaRecipe.setCategory("Lunch");
        assertEquals("Snack", testRecipe.getCategory());
        assertEquals("Lunch", pizzaRecipe.getCategory());
    }

}