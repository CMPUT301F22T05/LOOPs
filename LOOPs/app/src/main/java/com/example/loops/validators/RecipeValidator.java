package com.example.loops.validators;

import com.example.loops.R;
import com.example.loops.models.Recipe;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Check if attributes of recipe are valid or not based on the context it lies in
 * The context of the ingredient is specified by {@link}
 */
public class RecipeValidator {
    private final Set<Integer> errorStringIds;

    /**
     * The various context the ingredient lies on
     * STORED - Recipe that is stored in ingredient storage
     * SHOPPING - Recipe that is in the shopping list
     */
    public enum RECIPE_TYPE {
        STORED,
        SHOPPING
    }

    /**
     * Constructor
     */
    public RecipeValidator() {
        errorStringIds = new HashSet<>();
    }

    /**
     * Returns the buffer that stores error message ids.
     * The error messages are specified in validatorErrorMessages.xml.
     * The error buffer is emptied when this is called.
     * @return ArrayList of error string ids.
     */
    public ArrayList<Integer> getErrorStringIds() {
        ArrayList<Integer> toReturn = new ArrayList<>(errorStringIds);
        errorStringIds.clear();
        return toReturn;
    }

    /**
     * A nice function that checks for all attributes of the Recipe by calling
     * other checker methods
     * @param recipe ingredient to check
     * @return true if valid. False otherwise
     */
    public boolean checkRecipe(Recipe recipe, RECIPE_TYPE type) {
        boolean success = recipe != null;
        if (recipe != null) {
            success &= checkTitle(recipe.getTitle(), type);
            success &= checkDuration(recipe.getPrepTime(), type);
            success &= checkCategory(recipe.getCategory(), type);
            success &= checkNumServ(recipe.getNumServing(), type);
            success &= checkComment(recipe.getComments(), type);
        }
        return success;
    }

    /**
     * Returns true on a valid title of Recipe. Otherwise false.
     * Appends any validation failures to buffer.
     * @param title title of the Recipe
     * @param type the type of the Recipe, i.e. the context the Recipe lies on
     * @return true if valid. False otherwise.
     */
    public boolean checkTitle(String title, RECIPE_TYPE type) {
        if (title == null || title.length() <= 0) {
            errorStringIds.add(R.string.recipe_no_title);
            return false;
        }
        return true;
    }

    /**
     * Returns true on a valid duration of Recipe. Otherwise false.
     * Appends any validation failures to buffer.
     * @param duration Duration of the Recipe
     * @param type the type of the Recipe, i.e. the context the Recipe lies on
     * @return true if valid. False otherwise.
     */
    public boolean checkDuration(Duration duration, RECIPE_TYPE type) {
        if (duration == null || duration.isNegative()) {
            errorStringIds.add(R.string.recipe_no_duration);
            return false;
        }
        return true;
    }

    /**
     * Returns true on a valid Category of Recipe. Otherwise false.
     * Appends any validation failures to buffer.
     * @param category Category of the Recipe
     * @param type the type of the Recipe, i.e. the context the Recipe lies on
     * @return true if valid. False otherwise.
     */
    public boolean checkCategory(String category, RECIPE_TYPE type) {
        if (category == null || category.length() <= 0) {
            errorStringIds.add(R.string.recipe_no_category);
            return false;
        }
        return true;
    }

    /**
     * Returns true on a valid numServ of Recipe. Otherwise false.
     * Appends any validation failures to buffer.
     * @param numServ numServ of the Recipe
     * @param type the type of the Recipe, i.e. the context the Recipe lies on
     * @return true if valid. False otherwise.
     */
    public boolean checkNumServ(int numServ, RECIPE_TYPE type) {
        if (numServ < 0) {
            errorStringIds.add(R.string.recipe_no_numServ);
            return false;
        }
        return true;
    }

    /**
     * Returns true on a valid comment of Recipe. Otherwise false.
     * Appends any validation failures to buffer.
     * @param comment comment of the Recipe
     * @param type the type of the Recipe, i.e. the context the Recipe lies on
     * @return true if valid. False otherwise.
     */
    public boolean checkComment(String comment, RECIPE_TYPE type) {
        if (comment == null || comment.length() <= 0) {
            errorStringIds.add(R.string.recipe_no_Comment);
            return false;
        }
        return true;
    }
}