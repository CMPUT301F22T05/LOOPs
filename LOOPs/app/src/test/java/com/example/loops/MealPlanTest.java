package com.example.loops;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import com.example.loops.modelCollections.BaseRecipeCollection;
import com.example.loops.modelCollections.IngredientCollection;
import com.example.loops.modelCollections.RecipeCollection;
import com.example.loops.models.Ingredient;
import com.example.loops.models.MealPlan;
import com.example.loops.models.Recipe;

import junit.framework.TestCase;

import org.junit.jupiter.api.Test;


import java.time.Duration;

/**
 * Testing the model class MealPlan
 */
public class MealPlanTest extends TestCase{
    MealPlan mockMealPlan;
    Recipe mockRecipe;
    RecipeCollection mockRecipeCollection;
    Ingredient mockIngredient;
    IngredientCollection mockIngredientCollection;
    Ingredient recipeIngredient;
    MealPlan mockMealPlan2;

    public void setUp() throws Exception{
        super.setUp();
    }
    public void tearDown() throws Exception{

    }
    /**
     * Sets the variables up before each test
     */
    void initialize(){
        mockRecipeCollection = new RecipeCollection();
        mockRecipe = new Recipe();
        recipeIngredient = new Ingredient(
                "Carrot",
                "10/24/22",
                "Fridge",
                10,
                "#",
                "snack");
        mockRecipe.addIngredient(recipeIngredient);
        Duration x = Duration.ofHours(2);
        Duration y = Duration.ofMinutes(15);
        x.plus(y);
        mockRecipe.setTitle("Baked carrots");
        mockRecipe.setPrepTime(x);
        mockRecipe.setNumServing(3);
        mockRecipe.setCategory("Vegetables");
        mockRecipe.setComments("Bake in oven at 350F");
        mockRecipeCollection.addRecipe(mockRecipe);
        mockIngredient = new Ingredient(
                "Apple",
                "10/24/22",
                "Fridge",
                10,
                "#",
                "snack"

        );
        mockIngredientCollection = new IngredientCollection();
        mockIngredientCollection.addIngredient(mockIngredient);
        mockMealPlan = new MealPlan("mockMealPlan");
        mockMealPlan2 = new MealPlan("mockMealPlan2",mockIngredientCollection,mockRecipeCollection);
    }
    /*
     * Testing setters of MealPlan
     */
    @Test
    void testSetters(){
        initialize();
        mockMealPlan.setIngredients(mockIngredientCollection);
        assertEquals(mockMealPlan.getIngredients(),mockIngredientCollection);
        mockMealPlan.setRecipes(mockRecipeCollection);
        assertEquals(mockMealPlan.getRecipes(),mockRecipeCollection);

    }

    /**
     * Testing getters
     */
    @Test
    void testGetters(){
        initialize();
        assertEquals(mockMealPlan2.getRecipes(),mockRecipeCollection);
        assertEquals(mockMealPlan2.getIngredients(),mockIngredientCollection);
    }

    /**
     * Test the compareTo method in MealPlan class
     */
    @Test
    void testCompareTo(){
        initialize();
        MealPlan mealPlan = new MealPlan("mockMealPlan");
        assertEquals(-1,mockMealPlan.compareTo(mockMealPlan2));
        assertEquals(1,mockMealPlan2.compareTo(mockMealPlan));
        assertEquals(0,mealPlan.compareTo(mockMealPlan));
    }

}
