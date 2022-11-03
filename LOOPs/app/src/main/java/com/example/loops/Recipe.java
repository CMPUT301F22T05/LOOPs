package com.example.loops;

import android.graphics.Bitmap;
import android.net.Uri;

import java.time.LocalTime;

/**
 *  model class of a Recipe
 */
public class Recipe {
    private String title;
    private LocalTime prepTime;
    private int numServing;
    private String category;
    private Bitmap photo;
    private Uri picUri;
    private IngredientCollection ingredients;

    /**
     * Empty constructor
     */
    public Recipe(){this.ingredients = new IngredientCollection();};

    /**
     * Constructor w/ minimum required parameters
     * @param title (String)
     * @param numServing (int)
     */
    public Recipe(String title, int numServing) {
        this.title = title;
        this.numServing = numServing;
        this.ingredients = new IngredientCollection();
    }

    /**
     * Method for adding an ingredient to a recipe
     * @param ingredient (Ingredient)
     */
    public void addIngredient(Ingredient ingredient){
        ingredients.addIngredient(ingredient);
    }

    /**
     * Method for removing an ingredient from a recipe, index based
     * @param indexToDelete (int)
     */
    public void removeIngredient(int indexToDelete){
        ingredients.deleteIngredient(indexToDelete);
    }


    /**
     * Method to get the title of the recipe
     * @return String representing the title of the recipe
     */
    public String getTitle() {
        return title;
    }

    /**
     * Method for setting the title of the recipe
     * @param title (String)
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Method to get the preparation time of the recipe
     * @return LocalTime representing the preparation time
     */
    public LocalTime getPrepTime() {
        return prepTime;
    }

    /**
     * Method for setting the preparation time of the recipe
     * @param prepTime (LocalTime)
     */
    public void setPrepTime(LocalTime prepTime) {
        this.prepTime = prepTime;
    }

    /**
     * Method for getting the number of servings of the recipe
     * @return int representing the number of servings
     */
    public int getNumServing() {
        return numServing;
    }

    /**
     * Method for setting the number of servings for the recipe
     * @param numServing (int)
     */
    public void setNumServing(int numServing) {
        this.numServing = numServing;
    }

    /**
     * Method to get the category of the recipe
     * @return String representing the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Method for setting the category for the recipe
     * @param category (String)
     */
    public void setCategory(String category) {
        this.category = category;
    }

    //TODO: Update with javadocs the methods that are required below

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public Uri getPicUri() {
        return picUri;
    }

    public void setPicUri(Uri picUri) {
        this.picUri = picUri;
    }

}
