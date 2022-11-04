package com.example.loops;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

/**
 * An abstract fragment displaying an ingredient collection
 */
public abstract class IngredientCollectionFragment extends GenericCollectionLayout {
    protected IngredientCollection ingredientCollection;
    protected IngredientStorageViewAdapter collectionViewAdapter;

    /**
     * The type of ingredient collections the fragment accepts. Accepted values are:
     *  FROM_STORAGE - retrieve ingredients from user's stored ingredients
     *  FROM_RECIPE_INGREDIENTS - retrieves ingredients from a given recipe
     *  FROM_TESTING - FIXME: Temporary value for debugging.
     */
    public enum CollectionType {
        FROM_STORAGE,
        FROM_RECIPE_INGREDIENTS,
        FROM_TESTING
    }

    public IngredientCollectionFragment() {
        // Required empty public constructor
    }

    /**
     * Subclasses must implement the behavior when the add button is clicked
     * @param clickedView
     */
    abstract void onClickAddButton(View clickedView);

    /**
     * Subclasses must implement the behavior when ingredient items in the list are clicked
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    abstract void onClickIngredient(AdapterView<?> parent, View view, int position, long id);

    /**
     * Subclasses must parse arguments to at minimum handle the type of the ingredient collection
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
        View view = inflater.inflate(R.layout.fragment_ingredient_collection, container, false);
        bindComponents(view);

        parseArguments();
        populateSortSpinnerOptions();
        setListeners();

        return view;
    }

    /**
     * Sets the type of ingredient collection the fragment must display
     * @param type the type of the ingredient collection to display
     */
    protected void setIngredientCollectionToDisplay(CollectionType type) {
        if (type == CollectionType.FROM_STORAGE) {
            ingredientCollection = ((MainActivity)getActivity()).allIngredients;
            //((MainActivity)getActivity()).retrieveIngredientFromDatabase();
        }
        else if (type == CollectionType.FROM_RECIPE_INGREDIENTS) {
            // TODO: implement this whoever is handling recipe fragment
            throw new IllegalArgumentException("NOT IMPLEMENTED");
        }
//        FIXME: Here for debugging purposes. Remove later
        else if (type == CollectionType.FROM_TESTING) {
            ingredientCollection = new IngredientCollection();
            ingredientCollection.addIngredient(new Ingredient(
                    "test 1",
                    new Date(0),
                    "Test Location",
                    69,
                    "Test Unit",
                    "Test Category"
            ));
            ingredientCollection.addIngredient(new Ingredient(
                    "test 2",
                    new Date(0),
                    "Test Location",
                    69,
                    "Test Unit",
                    "Test Category"
            ));
            ingredientCollection.addIngredient(new Ingredient(
                    "test 3",
                    new Date(0),
                    "Test Location",
                    69,
                    "Test Unit",
                    "Test Category"
            ));
        }
        else {
            throw new IllegalArgumentException("Unknown given collection type");
        }
        adaptIngredientCollection(ingredientCollection);
    }

    /**
     * Sets the listeners for the UIs
     */
    private void setListeners() {
        sortOptionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortIngredientCollection(parent, position);
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
     * @param position
     */
    private void sortIngredientCollection(AdapterView<?> parent, int position) {
        if (parent.getItemAtPosition(position).equals(getString(R.string.empty_sort_option))) {
            return;
        }
        else if (parent.getItemAtPosition(position).equals(getString(R.string.sort_by_description))) {
            ingredientCollection.sort(IngredientSortOption.BY_DESCRIPTION_ASCENDING);
        }
        else if (parent.getItemAtPosition(position).equals(getString(R.string.sort_by_best_before_date))) {
            ingredientCollection.sort(IngredientSortOption.BY_BEST_BEFORE_DATE_ASCENDING);
        }
        else if (parent.getItemAtPosition(position).equals(getString(R.string.sort_by_Location))) {
            ingredientCollection.sort(IngredientSortOption.BY_LOCATION_ASCENDING);
        }
        else if (parent.getItemAtPosition(position).equals(getString(R.string.sort_by_Category))) {
            ingredientCollection.sort(IngredientSortOption.BY_CATEGORY_ASCENDING);
        }
        collectionViewAdapter.notifyDataSetChanged();
    }

    /**
     * Binds the ingredient collection to the UI
     * @param ingredientCollection the collection of ingredient to bind to UI
     */
    private void adaptIngredientCollection(IngredientCollection ingredientCollection) {
        collectionViewAdapter = new IngredientStorageViewAdapter(getActivity(),
                    ingredientCollection.getIngredients());
        collectionView.setAdapter(collectionViewAdapter);
    }

    /**
     * Populates the spinners in the fragment with options
     */
    private void populateSortSpinnerOptions() {
        ArrayAdapter<CharSequence> sortOptionSpinnerAdapter =
                ArrayAdapter.createFromResource(getActivity(),
                        R.array.ingredient_collection_sort_option, android.R.layout.simple_spinner_item);
        sortOptionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortOptionSpinner.setAdapter(sortOptionSpinnerAdapter);
    }
}