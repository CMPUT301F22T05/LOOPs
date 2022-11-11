package com.example.loops.ingredientFragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.loops.GenericCollectionLayout;
import com.example.loops.adapters.ShoppingListViewAdapter;
import com.example.loops.models.Ingredient;
import com.example.loops.modelCollections.IngredientCollection;
import com.example.loops.adapters.IngredientStorageViewAdapter;
import com.example.loops.MainActivity;
import com.example.loops.R;
import com.example.loops.sortOption.IngredientSortOption;

/**
 * An abstract fragment displaying an ingredient collection
 */
public abstract class IngredientCollectionFragment extends GenericCollectionLayout {
    protected IngredientCollection ingredientCollection;
    protected ArrayAdapter<Ingredient> collectionViewAdapter;
    protected ArrayAdapter<CharSequence> sortOptionSpinnerAdapter;
    enum LayoutType {
        IngredientStorage,
        ShoppingList
    }

    /**
     * The type of ingredient collections the fragment accepts. Accepted values are:
     *  FROM_STORAGE - retrieve ingredients from user's stored ingredients
     *  FROM_RECIPE_INGREDIENTS - retrieves ingredients from a given recipe
     *  FROM_TESTING - FIXME: Temporary value for debugging.
     */
    public enum CollectionType {
        FROM_STORAGE,
        FROM_RECIPE_INGREDIENTS,
        FOR_TEST_INGREDIENT_COLLECTION_EDITOR_FRAGMENT,
        FROM_SHOPPING_LIST
    }

    public IngredientCollectionFragment() {
        // Required empty public constructor
    }

    /**
     * Subclasses must implement the behavior when the add button is clicked
     * @param clickedView
     */
    abstract protected void onClickAddButton(View clickedView);

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
        View view = inflater.inflate(R.layout.generic_collection_layout, container, false);
        bindComponents(view);
        return view;
    }

    /**
     * Reads in any arguments of the fragment and set up listeners for the UI
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        parseArguments();
        setListeners();
    }

    /**
     * Sets the type of ingredient collection the fragment must display
     * @param type the type of the ingredient collection to display
     */
    protected void setIngredientCollectionToDisplay(CollectionType type) {
        if (type == CollectionType.FROM_STORAGE) {
            ingredientCollection = ((MainActivity)getActivity()).getIngredientStorage();
            //((MainActivity)getActivity()).retrieveIngredientFromDatabase();
        }
        else if (type == CollectionType.FROM_RECIPE_INGREDIENTS) {
            // TODO: implement this whoever is handling recipe fragment
            throw new IllegalArgumentException("NOT IMPLEMENTED");
        }
        //This is used for intent test and it should not be removed
        else if (type == CollectionType.FOR_TEST_INGREDIENT_COLLECTION_EDITOR_FRAGMENT) {
            ingredientCollection = new IngredientCollection();
            ingredientCollection.addIngredient(new Ingredient(
                    "BBB",
                    "2022-10-28",
                    "fridge",
                    1,
                    "unit",
                    "XXX"));
            ingredientCollection.addIngredient(new Ingredient(
                    "AAA",
                    "2022-10-29",
                    "cupboard",
                    1,
                    "unit",
                    "YYY"));
        }
        else if (type == CollectionType.FROM_SHOPPING_LIST) {
            ingredientCollection = new IngredientCollection();
            ingredientCollection.addIngredient(new Ingredient(
                    "BBB",
                    "2022-10-28",
                    "fridge",
                    1,
                    "g",
                    "XXX"));
            ingredientCollection.addIngredient(new Ingredient(
                    "AAA",
                    "2022-10-29",
                    "cupboard",
                    1,
                    "kg",
                    "YYY"));
        }
        else {
            throw new IllegalArgumentException("Unknown given collection type");
        }
        populateSortSpinnerOptions(type);
        adaptIngredientCollection(ingredientCollection, type);
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
            return;
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

    /**
     * Binds the ingredient collection to the UI
     * @param ingredientCollection the collection of ingredient to bind to UI
     */
    private void adaptIngredientCollection(IngredientCollection ingredientCollection, CollectionType type) {
        if (type == CollectionType.FROM_STORAGE) {
            collectionViewAdapter = new IngredientStorageViewAdapter(getActivity(),
                    ingredientCollection.getIngredients());
        }
        else {
            collectionViewAdapter = new ShoppingListViewAdapter(getActivity(),
                    ingredientCollection.getIngredients());
        }

        collectionView.setAdapter(collectionViewAdapter);
    }

    /**
     * Populates the spinners in the fragment with options
     */
    private void populateSortSpinnerOptions(CollectionType type) {
        if (type == CollectionType.FROM_STORAGE) {
            sortOptionSpinnerAdapter = ArrayAdapter.createFromResource(getActivity(),
                    R.array.ingredient_storage_sort_option, android.R.layout.simple_spinner_item);
            sortOptionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        }
        else {
            sortOptionSpinnerAdapter = ArrayAdapter.createFromResource(getActivity(),
                    R.array.shopping_list_sort_option, android.R.layout.simple_spinner_item);
            sortOptionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        }

        sortOptionSpinner.setAdapter(sortOptionSpinnerAdapter);
    }
}