package com.example.loops.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.loops.modelCollections.IngredientCollection;
import com.example.loops.sortOption.IngredientSortOption;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *  Model class. It is abstraction of recipe and it implements Serializable which make it
 *  transmissible between fragment
 *  Recipe class that encapsulate the data structure & methods of each recipe.
 */
public class Recipe implements Serializable, ModelConstraints {
    /**
     * A recipe includes:
     * title - name of the recipe
     * prepTime - time required to prepare for a recipe
     * numServing - number of people to serve
     * category - recipe category; should be user defined
     * photoBase64 - encoded base64 photo string
     * ingredients - collection of ingredients the recipe contains
     * comments - comments to recipe; contains the process of finishing a recipe
     */
    private String title;
    private Duration prepTime;
    private int numServing;
    private String category;
    private String photoBase64;
    private IngredientCollection ingredients;
    private String comments;

    /**
     * Constructor for a recipe; photo is Bitmap (used only when directly from photo source).
     * @param title name of the recipe
     * @param durationHour hour part of preparation time
     * @param durationMinute minute part of preparation time
     * @param numServing number of people to serve
     * @param category recipe category; should be user defined
     * @param photo original bitmap photo
     * @param ingredients ingredient collection the recipe contains
     * @param comments comments to recipe; contains the process of finishing a recipe
     */
    public Recipe(String title, long durationHour, long durationMinute, int numServing,
                  String category, Bitmap photo, IngredientCollection ingredients, String comments) {
        this.title = title;
        this.prepTime = Duration.ofHours(durationHour).plus(Duration.ofMinutes(durationMinute));
        this.numServing = numServing;
        this.category = category;
        this.photoBase64 = encodeBase64(photo);
        this.ingredients = ingredients;
        this.comments = comments;
    }

    /**
     * Constructor for a recipe; photo is base64 string (used only when read from database).
     * @param title name of the recipe
     * @param durationHour hour part of preparation time
     * @param durationMinute minute part of preparation time
     * @param numServing number of people to serve
     * @param category recipe category; should be user defined
     * @param photoBase64 original base64 photo
     * @param ingredients ingredient collection the recipe contains
     * @param comments comments to recipe; contains the process of finishing a recipe
     */
    public Recipe(String title, long durationHour, long durationMinute, int numServing,
                  String category, String photoBase64, IngredientCollection ingredients, String comments) {
        this.title = title;
        this.prepTime = Duration.ofHours(durationHour).plus(Duration.ofMinutes(durationMinute));
        this.numServing = numServing;
        this.category = category;
        this.photoBase64 = photoBase64;
        this.ingredients = ingredients;
        this.comments = comments;
    }

    /**
     * Copy constructor
     * @param that Recipe to copy
     */
    public Recipe(Recipe that) {
        this.title = that.getTitle();
        this.prepTime = that.getPrepTime();
        this.numServing = that.getNumServing();
        this.category = that.getCategory();
        this.photoBase64 = that.getPhotoBase64();
        this.comments = that.getComments();
        this.ingredients = new IngredientCollection();
        for (Ingredient ing : that.getIngredients().getIngredients()) {
            this.ingredients.addIngredient(new Ingredient(ing));
        }
    }

    /**
     * Encode Bitmap photo to base64 photo; use non-lossy PNG format to compress.
     * @param photo Bitmap photo to encode
     * @return encoded base64 photo string
     */
    private String encodeBase64(Bitmap photo) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, out);
        return Base64.getEncoder().encodeToString(out.toByteArray());
    }

    /**
     * Decode base64 photo string to Bitmap photo.
     * @param photoBase64 base64 string to decode
     * @return decoded Bitmap photo
     */
    private Bitmap decodeBase64(String photoBase64) {
        byte[] decoded = Base64.getDecoder().decode(photoBase64);
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
        return decodedBitmap;
    }

    /**
     * Two recipes are equal if their title, prepTime, numServing, category, ingredients, & comments
     * are all equal.
     * @param o recipe to compare
     * @return whether they are equal
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Recipe))
            return false;
        Recipe toCompare = (Recipe) o;
        return toCompare.getTitle().equals(getTitle())
                && toCompare.getPrepTime().equals(getPrepTime())
                && toCompare.getNumServing() == getNumServing()
                && toCompare.getCategory().equals(getCategory())
                && toCompare.getIngredients().equals(getIngredients())
                && toCompare.getComments().equals(getComments());
    }

    /**
     * Returns whether two recipes are equal ignoring quantity (num servings)
     * @param o object to compare
     * @return True if equal. Otherwise false
     */
    public boolean equalsIgnoringQuantity(Object o) {
        if (!(o instanceof Recipe))
            return false;
        Recipe toCompare = (Recipe) o;
        return toCompare.getTitle().equals(getTitle())
                && toCompare.getPrepTime().equals(getPrepTime())
                && toCompare.getCategory().equals(getCategory())
                && toCompare.getIngredients().equals(getIngredients())
                && toCompare.getComments().equals(getComments());
    }

    /**
     * Generate unique hashcode for each different recipes.
     * @return documentName for recipe
     */
    @Override
    public String getDocumentName() {
        String hashStr = getTitle();
        hashStr += getPrepTime().toString();
        hashStr += Integer.toString(getNumServing());
        hashStr += getCategory();
        ingredients.sort(IngredientSortOption.BY_DESCRIPTION_ASCENDING);
        for (Ingredient ing : getIngredients().getIngredients())
            hashStr += ing.getDocumentName();
        hashStr += getComments();
        return Integer.toString(hashStr.hashCode());
    }

    /**
     * Generate map data for database storage.
     * @return map data for database storage
     */
    public Map<String, Object> getMapData() {
        Map<String, Object> mapData = new HashMap<>();
        mapData.put("category", getCategory());
        mapData.put("comments", getComments());
        mapData.put("durationHour", getPrepTime().toHours());
        mapData.put("durationMinute", getPrepTime().toMinutes() - getPrepTime().toHours()*60);
        List<Map<String, Object>> ingredientList = new ArrayList<>();
        for (Ingredient ingredient : ingredients.getIngredients()) {
            ingredientList.add(ingredient.getMapData());
        }
        mapData.put("ingredients", ingredientList);
        mapData.put("numServing", getNumServing());
        mapData.put("title", getTitle());
        mapData.put("photoBase64", getPhotoBase64());
        return mapData;
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
    public Duration getPrepTime() {
        return prepTime;
    }

    /**
     * Method for setting the preparation time of the recipe
     * @param prepTime (LocalTime)
     */
    public void setPrepTime(Duration prepTime) {
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


    /**
     * Method for getting the Bitmap photo for the recipe
     * @return Bitmap photo
     */
    public Bitmap getPhoto() {
        return decodeBase64(photoBase64);
    }

    /**
     * Method for setting the Bitmap photo for the recipe
     * @param photo (Bitmap)
     */
    public void setPhoto(Bitmap photo) {
        this.photoBase64 = encodeBase64(photo);
    }

    /**
     * Method for getting the base64 photo string for the recipe
     * @return base64 photo string
     */
    public String getPhotoBase64() {
        return photoBase64;
    }

    /**
     * Method for getting the base64 photo string for the recipe
     * @param photoBase64 (String)
     */
    public void setPhotoBase64(String photoBase64) {
        this.photoBase64 = photoBase64;
    }

    /**
     * Returns the comments of the recipe
     * @return comments (String)
     */
    public String getComments() {
        return comments;
    }

    /**
     * Sets the comments of the recipe
     * @param comments (String)
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * Returns the ingredients of the recipe
     * @return ingredients (IngredientCollection)
     */
    public IngredientCollection getIngredients() {
        return ingredients;
    }

    /**
     * Sets the ingredients for the recipe
     * @param ingredients (IngredientCollection)
     */
    public void setIngredients(IngredientCollection ingredients) {
        this.ingredients = ingredients;
    }


    /**
     * Used in unit testing since bitmaps and encoding does not work in unit testing
     * @param title
     * @param duration
     * @param category
     * @param numServ
     * @param comment
     */
    public Recipe(String title, Duration duration, String category, int numServ, String comment) {
        this.title = title;
        this.prepTime = duration;
        this.numServing = numServ;
        this.category = category;
        this.comments = comment;
        this.ingredients = new IngredientCollection();
    }

    /**
     * Used for unit testing since bitmaps and encoding does not work in unit testing
     */
    public Recipe(){this.ingredients = new IngredientCollection();}

    @Deprecated
    public Recipe(String title, int numServing) {
        this.title = title;
        this.numServing = numServing;
        this.ingredients = new IngredientCollection();
    }
}
