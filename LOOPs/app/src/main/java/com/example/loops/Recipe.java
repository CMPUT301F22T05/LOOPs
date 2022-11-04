package com.example.loops;

import android.graphics.Bitmap;
import android.net.Uri;




import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;


/**
 *  model class of a Recipe
 */
public class Recipe implements Serializable, Comparable<Recipe> {
    private String title;
    private int prepTimeHour;
    private int prepTimeMinute;
    private int prepTime;
    private int numServing;
    private String category;
    private Bitmap photo;
    private Uri picUri;
    private IngredientCollection ingredients;

    private String comments;

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }


    public IngredientCollection getIngredients() {
        return ingredients;
    }

    public void setIngredients(IngredientCollection ingredients) {
        this.ingredients = ingredients;
    }


    /**
     * Empty constructor
     */
    public Recipe(){
        this.ingredients = new IngredientCollection();
        this.prepTimeMinute = 0;
        this.prepTimeHour = 0;
        this.numServing = 0;
        this.prepTime = 0;
    };


    /**
     * Constructor w/ minimum required parameters
     * @param title (String)
     * @param numServing (int)
     */
    public Recipe(String title, int numServing) {
        this.title = title;
        this.numServing = numServing;
        this.ingredients = new IngredientCollection();
        this.prepTimeMinute = 0;
        this.prepTimeHour = 0;
        this.prepTime = 0;
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


    /**
     * Method to get the hours of prep time of the recipe
     * @return int representing the hours
     */
    public int getPrepTimeHour() {
        return prepTimeHour;
    }


    /**
     * Method to set the hours of prep time of the recipe
     * @param prepTimeHour (int)
     */
    public void setPrepTimeHour(int prepTimeHour) {
        this.prepTimeHour = prepTimeHour;
        updateTotalPrepTime(prepTimeHour * 60);
    }


    /**
     * Method to get the minutes of prep time of the recipe
     * @return int representing the minutes
     */
    public int getPrepTimeMinute() {
        return prepTimeMinute;
    }


    /**
     * Method to set the minutes of prep time of the recipe
     * @param prepTimeMinute (int)
     */
    public void setPrepTimeMinute(int prepTimeMinute) {
        this.prepTimeMinute = prepTimeMinute;
        updateTotalPrepTime(prepTimeMinute);
    }

    /**
     * Method to set the minutes of prep time of the recipe
     * @return the total prep time in minutes (int)
     */
    public int getPrepTime(){
        return this.prepTime;
    }

    private void updateTotalPrepTime(int minutes){
        this.prepTime += minutes;
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

    @Override
    public int compareTo(Recipe o)
    {
        if (this.prepTime > o.prepTime) {
            // if current object is greater,then return 1
            return 1;
        }
        else if (this.prepTime < o.prepTime) {
            // if current object is greater,then return -1
            return -1;
        }
        else {
            // if current object is equal to o,then return 0
            return 0;
        }
    }
}