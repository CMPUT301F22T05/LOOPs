package com.example.loops.recipeFragments;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.loops.GenericCollectionLayout;
import com.example.loops.models.Ingredient;
import com.example.loops.MainActivity;
import com.example.loops.R;
import com.example.loops.models.Recipe;
import com.example.loops.modelCollections.RecipeCollection;
import com.example.loops.adapters.RecipeCollectionViewAdapter;
import com.example.loops.sortOption.RecipeSortOption;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Date;

/**
 * An abstract fragment displaying a recipe collection
 */
public abstract class RecipeCollectionFragment extends GenericCollectionLayout {
    protected RecipeCollection recipeCollection;
    protected RecipeCollectionViewAdapter collectionViewAdapter;

    /**
     * The type of recipe collections the fragment accepts. Accepted values are:
     * FROM_STORAGE - the user defined recipes
     * FROM_TESTING - FIXME: Temporary value for debugging
     */
    public enum CollectionType {
        FROM_STORAGE,
        FROM_TESTING
    }

    public RecipeCollectionFragment() {
        // Required empty public constructor
    }

    /**
     * Subclasses must implement the behavior when the add button is clicked
     * @param clickedView
     */
    abstract void onClickAddButton(View clickedView);

    /**
     * Subclasses must implement the behavior when recipe items in the list are clicked
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    abstract void onClickRecipe(AdapterView<?> parent, View view, int position, long id);

    /**
     * Subclasses must parse arguments to at minimum, handle the type of the recipe collection
     */
    abstract void parseArguments();

    /**
     * Sets the UI layout for the view it creates and any listeners
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_collection, container, false);
        bindComponents(view);
        populateSortSpinnerOptions();
        parseArguments();
        setListeners();
        return view;
    }

    /**
     * Sets the type of recipe collection the fragment must display
     * @param type the type of the recipe collection to display
     */
    protected void setRecipeCollectionToDisplay(CollectionType type) {
        if (type == CollectionType.FROM_STORAGE) {
            recipeCollection = ((MainActivity)getActivity()).getAllRecipes();
        }
        /*
//        FIXME: Here for debugging purposes. Remove later
        else if (type == CollectionType.FROM_TESTING) {
            recipeCollection = new RecipeCollection();
            recipeCollection.addRecipe(new Recipe("test 1", 5));
            recipeCollection.getRecipe(0).setPrepTime(Duration.ofMinutes(5));
            recipeCollection.getRecipe(0).setCategory("test 3");
            recipeCollection.getRecipe(0).setComments("Blah blah");
            recipeCollection.getRecipe(0).addIngredient(new Ingredient(
                    "desc",
                    LocalDate.now(),
                    "loc",
                    4,
                    "unit",
                    "cate"
            ));
            recipeCollection.addRecipe(new Recipe("test 2", 5));
            recipeCollection.getRecipe(1).setPrepTime(Duration.ofMinutes(3));
            recipeCollection.getRecipe(1).setCategory("test 2");
            recipeCollection.getRecipe(1).setComments("alah blah");
            recipeCollection.getRecipe(1).addIngredient(new Ingredient(
                    "desc",
                    LocalDate.now(),
                    "loc",
                    4,
                    "unit",
                    "cate"
            ));
            recipeCollection.addRecipe(new Recipe("test 3", 5));
            recipeCollection.getRecipe(2).setPrepTime(Duration.ofMinutes(2));
            recipeCollection.getRecipe(2).setNumServing(14);
            recipeCollection.getRecipe(2).setCategory("test 1");
            recipeCollection.getRecipe(2).setComments("Klah blah");
            recipeCollection.getRecipe(2).addIngredient(new Ingredient(
                    "desc",
                    LocalDate.now(),
                    "loc",
                    4,
                    "unit",
                    "cate"
            ));
        }
        else {
            throw new IllegalArgumentException("Unknown given collection type");
        }
         */
        adaptRecipeCollection(recipeCollection);
    }

    /**
     * Sets the listeners for the UIs
     */
    private void setListeners() {
        sortOptionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortCollection(parent);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //useless?
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickAddButton(v);
            }
        });

        collectionView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onClickRecipe(parent, view, position, id);
            }
        });
    }

    /**
     * Sorts the recipe collection and display it
     * @param parent
     */
    @Override
    protected void sortCollection(AdapterView<?> parent) {
        if (parent.getSelectedItem().toString().equals(getString(R.string.empty_sort_option))) {
            return;
        }
        if (isAscendingOrder) {
            if (parent.getSelectedItem().toString().equals(getString(R.string.sort_by_title))) {
                recipeCollection.sort(RecipeSortOption.BY_TITLE_ASCENDING);
            }
            else if (parent.getSelectedItem().toString().equals(getString(R.string.sort_by_preptime))) {
                recipeCollection.sort(RecipeSortOption.BY_PREP_TIME_ASCENDING);
            }
            else if (parent.getSelectedItem().toString().equals(getString(R.string.sort_by_category))) {
                recipeCollection.sort(RecipeSortOption.BY_CATEGORY_ASCENDING);
            }
        }
        else {
             if (parent.getSelectedItem().toString().equals(getString(R.string.sort_by_title))) {
                recipeCollection.sort(RecipeSortOption.BY_TITLE__DESCENDING);
            }
            else if (parent.getSelectedItem().toString().equals(getString(R.string.sort_by_preptime))) {
                recipeCollection.sort(RecipeSortOption.BY_PREP_TIME_DESCENDING);
            }
            else if (parent.getSelectedItem().toString().equals(getString(R.string.sort_by_category))) {
                recipeCollection.sort(RecipeSortOption.BY_CATEGORY_DESCENDING);
            }
        }

        collectionViewAdapter.notifyDataSetChanged();
    }

    /**
     * Binds the recipe collection to the UI
     * @param recipeCollection the collection of recipes to bind to UI
     */
    private void adaptRecipeCollection(RecipeCollection recipeCollection) {
        collectionViewAdapter = new RecipeCollectionViewAdapter(getActivity(),
                recipeCollection.getAllRecipes());
        collectionView.setAdapter(collectionViewAdapter);
    }

    /**
     * Populates the spinners in the fragment with options
     */
    private void populateSortSpinnerOptions() {
        ArrayAdapter<CharSequence> sortOptionSpinnerAdapter =
                ArrayAdapter.createFromResource(getActivity(),
                        R.array.recipe_collection_sort_option, android.R.layout.simple_spinner_item);
        sortOptionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortOptionSpinner.setAdapter(sortOptionSpinnerAdapter);
    }
}