package com.example.loops;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Check if attributes of ingredient are valid or not based on the context it lies in
 * The context of the ingredient is specified by {@link INGREDIENT_TYPE}
 */
// FIXME: THe name isn't the best. It might be too verbose and long. Feel free to rename them.
/* FIXME: Furthermore, some functions aren't probably needed. For now, I added them but if it's
    safe to do so, you can remove them.
 */
// FIXME: So far, I only handled null description... Check for other null attributes.
public class IngredientValidator {
    private final Set<Integer> errorStringIds;

    /**
     * The various context the ingredient lies on
     *      STORED - ingredient that is stored in ingredient storage
     *      SHOPPING - ingredient that is in the shopping list
     */
    public enum INGREDIENT_TYPE {
        STORED,
        SHOPPING
    }


    public IngredientValidator() {
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
     * A nice function that checks for all attributes of the ingredient by calling
     * other checker methods
     * @param ingredient ingredient to check
     * @return true if valid. False otherwise
     */
    public boolean checkIngredient(Ingredient ingredient, INGREDIENT_TYPE type) {
        return ingredient != null
                && checkDescription(ingredient.getDescription(), type)
                && checkBestBeforeDate(ingredient.getBestBeforeDate(), type)
                && checkLocation(ingredient.getStoreLocation(), type)
                && checkAmount(ingredient.getAmount(), type)
                && checkUnit(ingredient.getUnit(), type)
                && checkCategory(ingredient.getCategory(), type);
    }

    /**
     * Returns true on a valid description of ingredient. Otherwise false.
     * Appends any validation failures to buffer.
     * @param description description of the ingredient
     * @param type the type of the ingredient, i.e. the context the ingredient lies on
     * @return true if valid. False otherwise.
     */
    public boolean checkDescription(String description, INGREDIENT_TYPE type) {
        if (description == null || description.length() <= 0) {
            errorStringIds.add(R.string.ingredient_no_description);
            return false;
        }
        return true;
    }

    /**
     * Returns true on a valid best before date of ingredient. Otherwise false.
     * Appends any validation failures to buffer.
     * @param date best before date of the ingredient
     * @param type the type of the ingredient, i.e. the context the ingredient lies on
     * @return true if valid. False otherwise.
     */
    public boolean checkBestBeforeDate(Date date, INGREDIENT_TYPE type) {
        if (date == null) {
            errorStringIds.add(R.string.ingredient_no_bestbeforedate);
            return false;
        }
        return true;
    }

    /**
     * Returns true on a valid location of ingredient. Otherwise false.
     * Appends any validation failures to buffer.
     * @param location location of the ingredient
     * @param type the type of the ingredient, i.e. the context the ingredient lies on
     * @return true if valid. False otherwise.
     */
    public boolean checkLocation(String location, INGREDIENT_TYPE type) {
        if (location == null || location.length() <= 0) {
            errorStringIds.add(R.string.ingredient_no_location);
            return false;
        }
        return true;
    }

    /**
     * Returns true on a valid amount of ingredient. Otherwise false.
     * Appends any validation failures to buffer.
     * @param amount amount of the ingredient
     * @param type the type of the ingredient, i.e. the context the ingredient lies on
     * @return true if valid. False otherwise.
     */
    public boolean checkAmount(Integer amount, INGREDIENT_TYPE type) {
        if (amount == null) {
            errorStringIds.add(R.string.ingredient_no_amount);
            return false;
        }
        if (amount < 0) {
            errorStringIds.add(R.string.ingredient_negative_amount);
            return false;
        }
        return true;
    }

    /**
     * Returns true on a valid unit of ingredient. Otherwise false.
     * Appends any validation failures to buffer.
     * @param unit unit of the ingredient
     * @param type the type of the ingredient, i.e. the context the ingredient lies on
     * @return true if valid. False otherwise.
     */
    public boolean checkUnit(String unit, INGREDIENT_TYPE type) {
        if (unit == null || unit.length() <= 0) {
            errorStringIds.add(R.string.ingredient_no_unit);
            return false;
        }
        return true;
    }

    /**
     * Returns true on a valid category of ingredient. Otherwise false.
     * Appends any validation failures to buffer.
     * @param category best before date of the ingredient
     * @param type the type of the ingredient, i.e. the context the ingredient lies on
     * @return true if valid. False otherwise.
     */
    public boolean checkCategory(String category, INGREDIENT_TYPE type) {
        if (category == null || category.length() <= 0) {
            errorStringIds.add(R.string.ingredient_no_category);
            return false;
        }
        return true;
    }
}
