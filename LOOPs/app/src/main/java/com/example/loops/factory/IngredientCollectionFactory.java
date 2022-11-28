package com.example.loops.factory;

import android.app.Activity;
import android.widget.ArrayAdapter;

import com.example.loops.MainActivity;
import com.example.loops.R;
import com.example.loops.adapters.IngredientSelectionViewAdapter;
import com.example.loops.adapters.IngredientStorageViewAdapter;
import com.example.loops.adapters.ShoppingListViewAdapter;
import com.example.loops.modelCollections.IngredientCollection;
import com.example.loops.modelCollections.MealPlanCollection;
import com.example.loops.models.Ingredient;
import com.example.loops.shoppingListFragment.ShoppingListInitializer;


/**
 * A factory class to create ingredient collections. In addition, it can also return
 * proper related adapters for each ingredient collection type.
 */
public class IngredientCollectionFactory {
    private Activity context;

    /**
     * The type of the ingredient collections to create.
     *  EMPTY - creates an empty ingredient collection
     *  PRESET_FOR_EDIT - creates an ingredient collection preset with some ingredients
     *                      meant to be showed for editor fragments
     *  PRESET_FOR_VIEW - creates an ingredient collection preset with some ingredients
     *                        meant to be showed for selection fragments
     *  FROM_STORAGE_FOR_EDIT - retrieve ingredients from user's stored ingredients and
     *                          any changes to the collection are synced to the database
     *  FROM_STORAGE_FOR_VIEW - retrieve ingredients from user's stored ingredients
     *                          any changes to the collection are not synced to the database
     *  FROM_SHOPPING_LIST_FOR_EDIT - retrieve ingredients from the shopping list and
     *                                any changes to the collection are synced to the database
     */
    public enum CollectionType {
        EMPTY,
        PRESET_FOR_EDIT,
        PRESET_FOR_VIEW,
        FROM_STORAGE_FOR_EDIT,
        FROM_STORAGE_FOR_VIEW,
        FROM_SHOPPING_LIST_FOR_EDIT,
    }

    /**
     * Constructor. Creates an ingredient collection factory
     * @param activity activity of the fragment using this factory
     */
    public IngredientCollectionFactory(Activity activity) {
        this.context = activity;
    }

    /**
     * Create appropriate ingredient collection given the type
     * @param type type of the ingredient to create
     * @return ingredient collection
     */
    public IngredientCollection createIngredientCollection(CollectionType type) {
        IngredientCollection collection;
        switch (type) {
            case EMPTY:
                collection = new IngredientCollection();
                break;
            case PRESET_FOR_EDIT:
            case PRESET_FOR_VIEW:
                collection = new IngredientCollection();
                collection.addIngredient(new Ingredient(
                        "BBB",
                        "2022-10-28",
                        "fridge",
                        1,
                        "unit",
                        "XXX"));
                collection.addIngredient(new Ingredient(
                        "AAA",
                        "2022-10-29",
                        "cupboard",
                        1,
                        "unit",
                        "YYY"));
                break;
            case FROM_STORAGE_FOR_EDIT:
                collection = ((MainActivity) context).getIngredientStorage();
                break;
            case FROM_STORAGE_FOR_VIEW:
                IngredientCollection storedIngredients = ((MainActivity) context).getIngredientStorage();
                collection = new IngredientCollection();
                for (Ingredient ing : storedIngredients.getIngredients()) {
                    if ( ! ing.getPending() ) {
                        collection.addIngredient(new Ingredient(
                                ing.getDescription(),
                                ing.getAmount(),
                                ing.getUnit(),
                                ing.getCategory()
                        ));
                    }
                }
                break;
            case FROM_SHOPPING_LIST_FOR_EDIT:
                IngredientCollection ingredientStorage = ((MainActivity) context).getIngredientStorage();
                MealPlanCollection mealPlans = ((MainActivity) context).getMealPlans();
                collection = ShoppingListInitializer.getShoppingList(mealPlans, ingredientStorage);
                break;
            default:
                throw new Error("No collection is defined for this collection type");
        }
        return collection;
    }

    /**
     * Returns the sort option array adapter for a particular ingredient collection type
     * @param type type of the ingredient to create
     * @return array adapter storing the sort options
     */
    public ArrayAdapter<CharSequence> createSortOptionArrayAdapter(CollectionType type) {
        ArrayAdapter<CharSequence> sortOptionArrayAdapter;
        switch (type) {
            case EMPTY:
            case PRESET_FOR_EDIT:
            case FROM_STORAGE_FOR_EDIT:
                sortOptionArrayAdapter = ArrayAdapter.createFromResource(context,
                        R.array.ingredient_storage_sort_option, android.R.layout.simple_spinner_item);
                sortOptionArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                break;
            case PRESET_FOR_VIEW:
            case FROM_STORAGE_FOR_VIEW:
                sortOptionArrayAdapter = ArrayAdapter.createFromResource(context,
                        R.array.ingredient_selection_sort_option, android.R.layout.simple_spinner_item);
                sortOptionArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                break;
            case FROM_SHOPPING_LIST_FOR_EDIT:
                sortOptionArrayAdapter = ArrayAdapter.createFromResource(context,
                        R.array.shopping_list_sort_option, android.R.layout.simple_spinner_item);
                sortOptionArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                break;
            default:
                throw new Error("No sort option array is given for this collection type");
        }
        return sortOptionArrayAdapter;
    }

    /**
     * Returns the ingredient collection view adapter
     * @param type type of the ingredient collection
     * @param collection the ingredient collection to adapt
     * @return
     */
    public ArrayAdapter<Ingredient> createIngredientListAdapter(
            CollectionType type, IngredientCollection collection
    ) {
        ArrayAdapter<Ingredient> ingredientArrayAdapter;
        switch (type) {
            case EMPTY:
            case PRESET_FOR_EDIT:
            case FROM_STORAGE_FOR_EDIT:
                ingredientArrayAdapter = new IngredientStorageViewAdapter(context,
                        collection.getIngredients());
                break;
            case PRESET_FOR_VIEW:
            case FROM_STORAGE_FOR_VIEW:
                ingredientArrayAdapter = new IngredientSelectionViewAdapter(context,
                        collection.getIngredients());
                break;
            case FROM_SHOPPING_LIST_FOR_EDIT:
                ingredientArrayAdapter = new ShoppingListViewAdapter(context,
                        collection.getIngredients());
                break;
            default: throw new Error("No ingredient adapter is given for this collection type");
        }
        return ingredientArrayAdapter;
    }

}
