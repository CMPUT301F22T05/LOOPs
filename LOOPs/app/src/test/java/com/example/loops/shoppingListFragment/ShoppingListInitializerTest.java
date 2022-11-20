package com.example.loops.shoppingListFragment;

import com.example.loops.modelCollections.IngredientCollection;
import com.example.loops.modelCollections.MealPlanCollection;
import com.example.loops.modelCollections.RecipeCollection;
import com.example.loops.models.Ingredient;
import com.example.loops.models.MealPlan;
import com.example.loops.models.Recipe;

import org.junit.jupiter.api.BeforeEach;

public class ShoppingListInitializerTest {
    private MealPlanCollection testMealPlans;
    private IngredientCollection testIngredients;

    @BeforeEach
    void init() {
        RecipeCollection mealPlanRecipes = new RecipeCollection();
        IngredientCollection recipeIngredient = new IngredientCollection();
        recipeIngredient.addIngredient(new Ingredient(
                "Apple",
                "2022-01-01",
                "Pantry",
                3.5,
                "kg",
                "Fruit"
        ));
        mealPlanRecipes.addRecipe(new Recipe(
                "Apple Pie",
                1,
                35,
                8,
                "Dessert",
                "",
                recipeIngredient,
                "Mice apple pie!"
        ));

        IngredientCollection mealPlanIngredients = new IngredientCollection();
        mealPlanIngredients.addIngredient(new Ingredient(
                "Banana",
                "2022-01-01",
                "Pantry",
                2,
                "g",
                "Fruit"
        ));

        MealPlan mp = new MealPlan("", mealPlanIngredients, mealPlanRecipes);
        testMealPlans = new MealPlanCollection();
    }
}
