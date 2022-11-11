package com.example.loops.modelCollections;

import com.example.loops.models.Ingredient;
import com.example.loops.sortOption.IngredientSortOption;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Control class for ingredients
 * it can contain number of ingredients and keep track of them
 * ingredient can be removed or updated from the collection
 * new ingredients can also be added in
 * ingredients can also be sorted in different ways shown in IngredientSortOption enum
 * it also contains comparators for Ingredient
 */
public class IngredientCollection implements Serializable {
    protected ArrayList<Ingredient> ingredients;

    /**
     * Default constructor.
     */
    public IngredientCollection() {
        ingredients = new ArrayList<>();
    }

    /**
     * Getter for ingredient list.
     * @return ingredients
     */
    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    /**
     * Add an ingredient to the collection.
     * @param ingredient
     */
    public void addIngredient(Ingredient ingredient) {
        int ingInd = ingredients.indexOf(ingredient);
        if (ingInd != -1) {
            updateIngredient(ingInd, ingredient);
        } else {
            ingredients.add(ingredient);
        }
    }

    /**
     * Delete an ingredient from the collection.
     * @param index
     * @return true if deleted, false otherwise
     */
    public boolean deleteIngredient(int index) {
        try {
            ingredients.remove(index);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Update an ingredient's info based on its index.
     * @param index (int)
     * @param ingredient (Ingredient)
     * @return true if deleted, false otherwise
     */
    public boolean updateIngredient(int index, Ingredient ingredient) {
        try {
            ingredients.set(index, ingredient);
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * All sort options in a collection.
     * Include sort by description, best before date, location, category
     * @param option
     */
    public void sort(IngredientSortOption option) {
        if (option.equals(IngredientSortOption.BY_DESCRIPTION_ASCENDING)) {
            Collections.sort(ingredients, new DescriptionAscendingComparator());
        }
        else if (option.equals(IngredientSortOption.BY_BEST_BEFORE_DATE_ASCENDING)) {
            ingredients.sort(new BestBeforeDateAscendingComparator());
        }
        else if (option.equals(IngredientSortOption.BY_LOCATION_ASCENDING)) {
            Collections.sort(ingredients, new LocationAscendingComparator());
        }
        else if (option.equals(IngredientSortOption.BY_CATEGORY_ASCENDING)) {
            ingredients.sort(new CategoryAscendingComparator());
        }
        else if (option.equals(IngredientSortOption.BY_DESCRIPTION_DESCENDING)) {
            Collections.sort(ingredients, (new DescriptionAscendingComparator()).reversed());
        }
        else if (option.equals(IngredientSortOption.BY_BEST_BEFORE_DATE_DESCENDING)) {
            ingredients.sort((new BestBeforeDateAscendingComparator()).reversed());
        }
        else if (option.equals(IngredientSortOption.BY_LOCATION_DESCENDING)) {
            Collections.sort(ingredients, (new LocationAscendingComparator()).reversed());
        }
        else if (option.equals(IngredientSortOption.BY_CATEGORY_DESCENDING)) {
            ingredients.sort((new CategoryAscendingComparator()).reversed());
        }
    }

    /**
     * Sort by description.
     */
    static class DescriptionAscendingComparator implements Comparator<Ingredient> {

        @Override
        public int compare(Ingredient o1, Ingredient o2) {
            return o1.getDescription().compareTo(o2.getDescription());
        }
    }

    /**
     * Sort by best before date.
     */
     static class BestBeforeDateAscendingComparator implements Comparator<Ingredient> {
        @Override
        public int compare(Ingredient o1, Ingredient o2) {
            return o1.getBestBeforeDate().compareTo(o2.getBestBeforeDate());
        }
    }

    /**
     * Sort by category.
     */
    static class CategoryAscendingComparator implements Comparator<Ingredient> {
        @Override
        public int compare(Ingredient o1, Ingredient o2) {
            return o1.getCategory().compareTo(o2.getCategory());
        }
    }

    /**
     * Sort by location.
     */
    static class LocationAscendingComparator implements Comparator<Ingredient> {
        @Override
        public int compare(Ingredient o1, Ingredient o2) {
            return o1.getStoreLocation().compareTo(o2.getStoreLocation());
        }
    }

    /**
     * Equal method; used in recipe identical comparison; equal only when all ingredients are
     * equal with same order.
     * @param o ingredient collection to compare
     * @return whether two ingredient collections are identical
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof IngredientCollection))
            return false;
        IngredientCollection toCompare = (IngredientCollection) o;

        if (toCompare.getIngredients().size() != getIngredients().size())
            return false;
        int size = getIngredients().size();
        for (int i = 0; i < size; i++) {
            if (!toCompare.getIngredients().get(i).equals(getIngredients().get(i)))
                return false;
        }
        return true;
    }
}
