package com.example.loops;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * An ingredient collection includes all data access methods.
 */
public class IngredientCollection {
    private ArrayList<Ingredient> ingredients;

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
        ingredients.add(ingredient);
    }

    /**
     * Delete an ingredient from the collection.
     * @param index
     */
    public void deleteIngredient(int index) {
        ingredients.remove(index);
    }

    /**
     * Update an ingredient's info based on its index.
     * @param index
     * @param ingredient
     */
    public void updateIngredient(int index, Ingredient ingredient) {
        ingredients.set(index, ingredient);
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
    class DescriptionAscendingComparator implements Comparator<Ingredient> {

        @Override
        public int compare(Ingredient o1, Ingredient o2) {
            return o1.getDescription().compareTo(o2.getDescription());
        }
    }

    /**
     * Sort by best before date.
     */
    class BestBeforeDateAscendingComparator implements Comparator<Ingredient> {
        @Override
        public int compare(Ingredient o1, Ingredient o2) {
            return o1.getBestBeforeDate().compareTo(o2.getBestBeforeDate());
        }
    }

    /**
     * Sort by category.
     */
    class CategoryAscendingComparator implements Comparator<Ingredient> {
        @Override
        public int compare(Ingredient o1, Ingredient o2) {
            return o1.getCategory().compareTo(o2.getCategory());
        }
    }

    /**
     * Sort by location.
     */
    class LocationAscendingComparator implements Comparator<Ingredient> {
        @Override
        public int compare(Ingredient o1, Ingredient o2) {
            return o1.getStoreLocation().compareTo(o2.getStoreLocation());
        }
    }
}
