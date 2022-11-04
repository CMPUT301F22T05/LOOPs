package com.example.loops;

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
public class IngredientCollection {
    private ArrayList<Ingredient> ingredients;

    public IngredientCollection() {
        ingredients = new ArrayList<>();
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
    }

    public boolean deleteIngredient(int index) {
        try {
            ingredients.remove(index);
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

    public boolean updateIngredient(int index, Ingredient ingredient) {
        try {
            ingredients.set(index, ingredient);
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }

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

    class DescriptionAscendingComparator implements Comparator<Ingredient> {

        @Override
        public int compare(Ingredient o1, Ingredient o2) {
            return o1.getDescription().compareTo(o2.getDescription());
        }
    }

    class BestBeforeDateAscendingComparator implements Comparator<Ingredient> {
        @Override
        public int compare(Ingredient o1, Ingredient o2) {
            return o1.getBestBeforeDate().compareTo(o2.getBestBeforeDate());
        }
    }

    class CategoryAscendingComparator implements Comparator<Ingredient> {
        @Override
        public int compare(Ingredient o1, Ingredient o2) {
            return o1.getCategory().compareTo(o2.getCategory());
        }
    }

    class LocationAscendingComparator implements Comparator<Ingredient> {
        @Override
        public int compare(Ingredient o1, Ingredient o2) {
            return o1.getStoreLocation().compareTo(o2.getStoreLocation());
        }
    }
}
