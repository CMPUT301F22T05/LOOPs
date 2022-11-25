package com.example.loops.factory;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ArrayAdapter;

import com.example.loops.MainActivity;
import com.example.loops.R;
import com.example.loops.adapters.RecipeCollectionViewAdapter;
import com.example.loops.adapters.RecipeSelectionViewAdapter;
import com.example.loops.modelCollections.IngredientCollection;
import com.example.loops.modelCollections.BaseRecipeCollection;
import com.example.loops.models.Recipe;

/**
 * A factory object that creates recipes from sources specified by CollectionType.
 * It also returns sort option adapters and list adapters
 */
public class RecipeCollectionFactory {
    private Activity context;

    /**
     * The type of the recipe collections to create.
     *  EMPTY - creates an empty recipe collection
     *  PRESET_FOR_EDIT - creates an recipe collection preset with some recipes
     *                      meant to be shown in editor fragments
     *  PRESET_FOR_VIEW - creates an recipe collection preset with some recipes
     *                      meant to be shown in selection fragments
     *  FROM_STORAGE_FOR_EDIT - retrieve recipes from user's stored recipes and
     *                          any changes to the collection are synced to the database
     *  FROM_STORAGE_FOR_VIEW - retrieve recipes from user's stored recipes
     *                          any changes to the collection are not synced to the database
     */
    public enum CollectionType {
        EMPTY,
        PRESET_FOR_EDIT,
        PRESET_FOR_VIEW,
        FROM_STORAGE_FOR_EDIT,
        FROM_STORAGE_FOR_VIEW
    }

    /**
     * Constructor. Creates a recipe collection factory
     * @param activity activity of the fragment using this factory
     */
    public RecipeCollectionFactory(Activity activity) {
        this.context = activity;
    }

    /**
     * Create appropriate recipe collection given the type
     * @param type type of the recipe to create
     * @return recipe collection
     */
    public BaseRecipeCollection createRecipeCollection(CollectionType type) {
        BaseRecipeCollection collection;
        switch (type) {
            case EMPTY:
                collection = new BaseRecipeCollection();
                break;
            case PRESET_FOR_VIEW:
            case PRESET_FOR_EDIT:
                collection = new BaseRecipeCollection();
                IngredientCollection grilledCheeseIngredients = new IngredientCollection();
                Bitmap grilledCheese = BitmapFactory.decodeResource(context.getResources(), R.drawable.grilled_cheese_test_image);
                Recipe recipe1 = new Recipe("Grilled Cheese",
                        0,
                        15,
                        1,
                        "Breakfast",
                        grilledCheese,
                        grilledCheeseIngredients,
                        "Classic");
                IngredientCollection pizzaIngredients = new IngredientCollection();
                Bitmap pizza = BitmapFactory.decodeResource(context.getResources(),R.drawable.pizza_test_image);
                Recipe recipe2 = new Recipe("Pizza",
                        0,
                        45,
                        4,
                        "Dinner",
                        pizza,
                        pizzaIngredients,
                        "Italian Pizza");
                collection.addRecipe(recipe1);
                collection.addRecipe(recipe2);
                break;
            case FROM_STORAGE_FOR_EDIT:
                collection = ((MainActivity) context).getAllRecipes();
                break;
            case FROM_STORAGE_FOR_VIEW:
                BaseRecipeCollection storedRecipes = ((MainActivity) context).getAllRecipes();
                collection = new BaseRecipeCollection();
                for (Recipe recipe : storedRecipes.getAllRecipes()) {
                    collection.addRecipe(new Recipe(recipe));
                }
                break;
            default:
                throw new Error("No collection is defined for this collection type");
        }
        return collection;
    }

    /**
     * Returns the sort option array adapter for a particular recipe collection type
     * @param type type of the recipe to create
     * @return array adapter storing the sort options
     */
    public ArrayAdapter<CharSequence> createSortOptionArrayAdapter(CollectionType type) {
        ArrayAdapter<CharSequence> sortOptionArrayAdapter;
        switch (type) {
            case EMPTY:
            case PRESET_FOR_VIEW:
            case PRESET_FOR_EDIT:
            case FROM_STORAGE_FOR_VIEW:
            case FROM_STORAGE_FOR_EDIT:
                sortOptionArrayAdapter =
                        ArrayAdapter.createFromResource(context,
                                R.array.recipe_collection_sort_option, android.R.layout.simple_spinner_item);
                sortOptionArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                break;
            default:
                throw new Error("No sort option array is given for this collection type");
        }
        return sortOptionArrayAdapter;
    }

    /**
     * Returns the recipe collection view adapter
     * @param type type of the recipe collection
     * @param collection the recipe collection to adapt
     * @return
     */
    public ArrayAdapter<Recipe> createRecipeListAdapter(
            CollectionType type, BaseRecipeCollection collection
    ) {
        ArrayAdapter<Recipe> recipeArrayAdapter;
        switch (type) {
            case EMPTY:
            case PRESET_FOR_EDIT:
            case FROM_STORAGE_FOR_EDIT:
                recipeArrayAdapter = new RecipeCollectionViewAdapter(context,
                        collection.getAllRecipes());
                break;
            case PRESET_FOR_VIEW:
            case FROM_STORAGE_FOR_VIEW:
                recipeArrayAdapter = new RecipeSelectionViewAdapter(context,
                        collection.getAllRecipes());
                break;
            default: throw new Error("No recipe adapter is given for this collection type");
        }
        return recipeArrayAdapter;
    }
}
