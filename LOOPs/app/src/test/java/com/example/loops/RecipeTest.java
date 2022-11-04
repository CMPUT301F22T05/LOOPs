package com.example.loops;

import junit.framework.TestCase;

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
        amount = 10;
        carrot = new Ingredient("Carrot", "10/24/22", "Fridge",amount , "units","snack");
        ingredients.addIngredient(carrot);
        testRecipe.setTitle("Baked carrots");
        testRecipe.setCategory("Vegetables");
        testRecipe.setComments("Bake in oven at 350F");
        testRecipe.setIngredients(ingredients);
    }

    public void testGetTitle() {
        initialize();
        assertEquals("Baked carrots", testRecipe.getTitle());
        Recipe pizzaRecipe = new Recipe("Pizza", 2);
        assertEquals("Pizza", pizzaRecipe.getTitle());
    }

    public void testSetTitle() {
        Recipe pizzaRecipe = new Recipe();
        pizzaRecipe.setTitle("Pizza");
        assertEquals("Pizza", pizzaRecipe.getTitle());
        testRecipe.setTitle("Oven Baked Carrots");
        assertEquals("Oven Baked Carrots", testRecipe.getTitle());
    }

    public void testGetPrepTime() {
    }

    public void testSetPrepTime() {
    }

    public void testGetNumServing() {
    }

    public void testSetNumServing() {
    }

    public void testGetCategory() {
    }

    public void testSetCategory() {
    }

    public void testGetPhoto() {
    }

    public void testSetPhoto() {
    }

    public void testGetPicUri() {
    }

    public void testSetPicUri() {
    }
}