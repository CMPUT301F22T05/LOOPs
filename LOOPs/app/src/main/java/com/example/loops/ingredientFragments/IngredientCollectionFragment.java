package com.example.loops.ingredientFragments;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.loops.GenericCollectionLayout;
import com.example.loops.adapters.ShoppingListViewAdapter;
import com.example.loops.factory.IngredientCollectionFactory;
import com.example.loops.factory.IngredientCollectionFactory.CollectionType;
import com.example.loops.modelCollections.MealPlanCollection;
import com.example.loops.models.Ingredient;
import com.example.loops.modelCollections.IngredientCollection;
import com.example.loops.adapters.IngredientStorageViewAdapter;
import com.example.loops.MainActivity;
import com.example.loops.R;
import com.example.loops.shoppingListFragment.ShoppingListInitializer;
import com.example.loops.sortOption.IngredientSortOption;

/**
 * An abstract fragment displaying an ingredient collection
 */
public abstract class IngredientCollectionFragment extends GenericCollectionLayout {
    private CollectionType collectionType = null;
    protected IngredientCollection ingredientCollection;
    protected ArrayAdapter<Ingredient> collectionViewAdapter;
    protected ArrayAdapter<CharSequence> sortOptionSpinnerAdapter;

    /**
     * Sets the UI layout for the view
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
     * Returns the layout id of the UI layout of this fragment
     * @return id of the UI layout
     */
    protected int getUIViewId() {
        return R.layout.fragment_ingredient_collection;
    }

    /**
     * Reads in any arguments of the fragment and set up listeners for the UI
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (collectionType == null)
            collectionType = getCollectionType();
        IngredientCollectionFactory ingredientCollectionFactory = new IngredientCollectionFactory(getActivity());
        ingredientCollection = ingredientCollectionFactory.createIngredientCollection(collectionType);
        populateSortSpinnerOptions(ingredientCollectionFactory, collectionType);
        setIngredientCollectionToDisplay(ingredientCollectionFactory, collectionType, ingredientCollection);
        parseArguments();
        setListeners();
    }

    /**
     * Subclasses must implement this. When subclasses parse arguments, collection type can be parsed
     * there
     */
    abstract protected CollectionType getCollectionType();

    /**
     * Subclasses must implement the behavior when ingredient items in the list are clicked
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    abstract protected void onClickIngredient(AdapterView<?> parent, View view, int position, long id);

    /**
     * Subclasses must parse arguments to at minimum handle the type of the ingredient collection
     */
    abstract protected void parseArguments();

    /**
     * Populate the sort spinner with sort options
     * @param factory factory used to create the sort options
     * @param type type of the ingredient collection
     */
    private void populateSortSpinnerOptions(IngredientCollectionFactory factory, CollectionType type) {
        sortOptionSpinnerAdapter = factory.createSortOptionArrayAdapter(type);
        sortOptionSpinner.setAdapter(sortOptionSpinnerAdapter);
    }

    /**
     * Sets the ingredient collection to display in the UI list view
     * @param factory factory used to create the view adapter for the list view
     * @param type type of the ingredient collection
     * @param collection ingredient collection to display
     */
    private void setIngredientCollectionToDisplay(
            IngredientCollectionFactory factory, CollectionType type, IngredientCollection collection
    ) {
        collectionViewAdapter = factory.createIngredientListAdapter(type, collection);
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
                onClickIngredient(parent, view, position, id);
            }
        });
    }

    /**
     * Sorts the ingredient collection and display it
     * @param parent
     */
    @Override
    protected void sortCollection(AdapterView<?> parent) {
        if (parent.getSelectedItem().toString().equals(getString(R.string.empty_sort_option))) {
            ingredientCollection.sort(IngredientSortOption.BY_PENDING);
        }
        if (isAscendingOrder) {
            if (parent.getSelectedItem().toString().equals(getString(R.string.sort_by_description))) {
                ingredientCollection.sort(IngredientSortOption.BY_DESCRIPTION_ASCENDING);
            }
            else if (parent.getSelectedItem().toString().equals(getString(R.string.sort_by_best_before_date))) {
                ingredientCollection.sort(IngredientSortOption.BY_BEST_BEFORE_DATE_ASCENDING);
            }
            else if (parent.getSelectedItem().toString().equals(getString(R.string.sort_by_Location))) {
                ingredientCollection.sort(IngredientSortOption.BY_LOCATION_ASCENDING);
            }
            else if (parent.getSelectedItem().toString().equals(getString(R.string.sort_by_Category))) {
                ingredientCollection.sort(IngredientSortOption.BY_CATEGORY_ASCENDING);
            }
        }
        else {
            if (parent.getSelectedItem().toString().equals(getString(R.string.sort_by_description))) {
                ingredientCollection.sort(IngredientSortOption.BY_DESCRIPTION_DESCENDING);
            }
            else if (parent.getSelectedItem().toString().equals(getString(R.string.sort_by_best_before_date))) {
                ingredientCollection.sort(IngredientSortOption.BY_BEST_BEFORE_DATE_DESCENDING);
            }
            else if (parent.getSelectedItem().toString().equals(getString(R.string.sort_by_Location))) {
                ingredientCollection.sort(IngredientSortOption.BY_LOCATION_DESCENDING);
            }
            else if (parent.getSelectedItem().toString().equals(getString(R.string.sort_by_Category))) {
                ingredientCollection.sort(IngredientSortOption.BY_CATEGORY_DESCENDING);
            }
        }

        collectionViewAdapter.notifyDataSetChanged();
    }
}