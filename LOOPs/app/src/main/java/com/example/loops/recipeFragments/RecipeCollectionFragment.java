package com.example.loops.recipeFragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.loops.GenericCollectionLayout;
import com.example.loops.factory.RecipeCollectionFactory;
import com.example.loops.factory.RecipeCollectionFactory.CollectionType;
import com.example.loops.modelCollections.BaseRecipeCollection;
import com.example.loops.modelCollections.IngredientCollection;
import com.example.loops.models.Ingredient;
import com.example.loops.MainActivity;
import com.example.loops.R;
import com.example.loops.models.Recipe;
import com.example.loops.modelCollections.RecipeCollection;
import com.example.loops.adapters.RecipeCollectionViewAdapter;
import com.example.loops.sortOption.RecipeSortOption;

import java.lang.reflect.Array;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Date;

/**
 * An abstract fragment displaying a recipe collection
 */
public abstract class RecipeCollectionFragment extends GenericCollectionLayout {
    private CollectionType collectionType = null;
    protected BaseRecipeCollection recipeCollection;
    protected ArrayAdapter<Recipe> collectionViewAdapter;

//    public RecipeCollectionFragment() {
//        // Required empty public constructor
//    }

    /**
     * Subclasses must implement the behavior when recipe items in the list are clicked
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    abstract protected void onClickRecipe(AdapterView<?> parent, View view, int position, long id);

    /**
     * Subclasses must parse arguments to at minimum, handle the type of the recipe collection
     */
    abstract protected void parseArguments();

    /**
     * Subclasses must implement this. When subclasses parse arguments, collection type can be parsed
     * there
     */
    abstract protected CollectionType getCollectionType();

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
        View view = inflater.inflate(getUIViewId(), container, false);
        bindComponents(view);
        return view;
    }

    /**
     * Reads in any arguments of the fragment and set up listeners for the UI
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (collectionType == null)
            collectionType = getCollectionType();
        RecipeCollectionFactory recipeCollectionFactory = new RecipeCollectionFactory(getActivity());
        recipeCollection = recipeCollectionFactory.createRecipeCollection(collectionType);
        populateSortSpinnerOptions(recipeCollectionFactory, collectionType);
        setRecipeCollectionToDisplay(recipeCollectionFactory, collectionType, recipeCollection);
        parseArguments();
        setListeners();
    }

    /**
     * Returns the layout id of the UI layout of this fragment
     * @return id of the UI layout
     */
    protected int getUIViewId() {
        return R.layout.fragment_recipe_collection;
    }

    /**
     * Sets the recipe collection to display in the UI list view
     * @param factory factory used to create the view adapter for the list view
     * @param type type of the recipe collection
     * @param collection recipe collection to display
     */
    protected void setRecipeCollectionToDisplay(
            RecipeCollectionFactory factory,  CollectionType type, BaseRecipeCollection collection) {
        collectionViewAdapter = factory.createRecipeListAdapter(type, collection);
        collectionView.setAdapter(collectionViewAdapter);
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
     * Populates the spinners in the fragment with options
     * @param factory factory used to create the sort options
     * @param type type of the recipe collection to sort
     */
    private void populateSortSpinnerOptions(RecipeCollectionFactory factory,  CollectionType type) {
        ArrayAdapter<CharSequence> sortOptionSpinnerAdapter = factory.createSortOptionArrayAdapter(type);
        sortOptionSpinner.setAdapter(sortOptionSpinnerAdapter);
    }
}