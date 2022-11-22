package com.example.loops.models;

import com.example.loops.modelCollections.BaseRecipeCollection;
import com.example.loops.modelCollections.IngredientCollection;
import com.example.loops.modelCollections.RecipeCollection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The class models a meal, a meal can contains multiple recipes and ingredients
 */
public class MealPlan implements Serializable, ModelConstraints, Comparable<MealPlan>{
    private String name;
    private IngredientCollection ingredients;
    private BaseRecipeCollection recipes;

    /**
     * the constructor require all fields
     * @param name the meal's name
     * @param ingredients ingredients contained in the meal
     * @param recipes recipes contained in the meal
     */
    public MealPlan(String name, IngredientCollection ingredients, RecipeCollection recipes) {
        this.name = name;
        this.ingredients = ingredients;
        this.recipes = recipes;
    }

    /**
     * easy constructor
     * @param name the meal's name
     */
    public MealPlan(String name) {
        this.name = name;
        ingredients = new IngredientCollection();
        recipes = new RecipeCollection();
    }

    /**
     * get the meal's name
     * @return the meal's name
     */
    public String getName() {
        return name;
    }

    /**
     * get ingredient collection from meal plan
     * @return an ingredient collection
     */
    public IngredientCollection getIngredients() {
        return ingredients;
    }

    /**
     * set meal plan with a new ingredient collection
     * @param ingredients new ingredient collection
     */
    public void setIngredients(IngredientCollection ingredients) {
        this.ingredients = ingredients;
    }

    /**
     * get recipe collection from meal plan
     * @return a recipe collection
     */
    public BaseRecipeCollection getRecipes() {
        return recipes;
    }

    /**
     * set meal plan with a new recipe collection
     * @param recipes new recipe collection
     */
    public void setRecipes(RecipeCollection recipes) {
        this.recipes = recipes;
    }

    @Override
    public Map<String, Object> getMapData() {
        Map<String, Object> mapData = new HashMap<>();
        List<Map<String, Object>> ingredientList = new ArrayList<>();
        for (Ingredient ingredient : ingredients.getIngredients()) {
            ingredientList.add(ingredient.getMapData());
        }
        mapData.put("ingredients", ingredientList);
        List<Map<String, Object>> recipeList = new ArrayList<>();
        for (Recipe recipe : recipes.getAllRecipes()) {
            recipeList.add(recipe.getMapData());
        }
        mapData.put("recipes", recipeList);
        return mapData;
    }

    @Override
    public String getDocumentName() {
        return name;
    }

    @Override
    public int compareTo(MealPlan o) {
        return name.compareTo(o.getName());
    }
}
