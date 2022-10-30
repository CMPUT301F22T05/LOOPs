package com.example.loops;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.Serializable;
import java.time.LocalTime;

/**
 *  model class of a Recipe
 */
public class Recipe implements Serializable {
    private String title;
    private LocalTime prepTime;
    private int numServing;
    private String category;
    private Bitmap photo;
    private Uri picUri;
    private IngredientCollection ingredients;

    public Recipe(String title, int numServing) {
        this.title = title;
        this.numServing = numServing;
    }

    public void addIngredient(Ingredient ingredient){
        ingredients.addIngredient(ingredient);
    }

    //Accepts into to delete but should take ingredient?
    public void removeIngredient(Ingredient ingredient){
        ingredients.deleteIngredient(0);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalTime getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(LocalTime prepTime) {
        this.prepTime = prepTime;
    }

    public int getNumServing() {
        return numServing;
    }

    public void setNumServing(int numServing) {
        this.numServing = numServing;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

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