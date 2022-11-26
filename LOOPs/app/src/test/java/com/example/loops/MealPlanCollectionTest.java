package com.example.loops;

import com.example.loops.modelCollections.IngredientCollection;
import com.example.loops.modelCollections.MealPlanCollection;
import com.example.loops.modelCollections.RecipeCollection;
import com.example.loops.models.Ingredient;
import com.example.loops.models.MealPlan;
import com.example.loops.models.Recipe;

import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Set;

public class MealPlanCollectionTest extends  TestCase{
    MealPlanCollection mockMealPlanCollection;
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
        mockMealPlanCollection = new MealPlanCollection();
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

    /**
     * Testing getters
     */
    @Test
    void testGetMealPlan(){
        initialize();
        Integer size = mockMealPlanCollection.getMealPlans().size();
        assertEquals((Integer) 21,size);
    }

    /**
     * Test adding a meal plan into the meal plan collection
     */
    @Test
    void testAddMealPlan(){
        initialize();
        Integer size = mockMealPlanCollection.getMealPlans().size();
        assertEquals((Integer) 21,size);
        mockMealPlanCollection.addMealPlan(mockMealPlan);
        size = mockMealPlanCollection.getMealPlans().size();
        assertEquals((Integer) 22,size);
    }


    /**
     * Test update meal plan
     */
    @Test
    void testUpdateMealPlan(){
        initialize();
        mockMealPlanCollection.updateMealPlan(0,mockMealPlan2);
        Integer size = mockMealPlanCollection.getMealPlans().size();
        assertEquals((Integer) 21,size);
        assertEquals(mockMealPlanCollection.getMealPlans().get(0),mockMealPlan2);
    }
}
