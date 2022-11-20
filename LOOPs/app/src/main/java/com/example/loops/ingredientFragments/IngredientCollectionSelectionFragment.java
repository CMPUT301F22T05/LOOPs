package com.example.loops.ingredientFragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.loops.adapters.IngredientStorageViewAdapter;
import com.example.loops.modelCollections.IngredientCollection;
import com.example.loops.factory.IngredientCollectionFactory.CollectionType;
import com.example.loops.models.Ingredient;
import com.example.loops.R;

/**
 * Ingredient collection fragment for selecting an ingredient
 */
public class IngredientCollectionSelectionFragment extends IngredientCollectionFragment {
    public static final String RESULT_KEY = "INGREDIENT_COLLECTION_SELECTION_FRAGMENT_RESULT_KEY";
    private Button saveButton;
    private IngredientCollection chosenIngredients;

    public IngredientCollectionSelectionFragment() {
        // Required empty public constructor
    }

    /**
     * Sets the UI layout of the view it creates
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = super.onCreateView(inflater, container, savedInstanceState);
        saveButton = fragmentView.findViewById(R.id.select_button);
        chosenIngredients = new IngredientCollection();
        setSaveButtonListener();
        return fragmentView;
    }

    /**
     * Returns the layout id of the UI layout of this fragment
     * @return id of the UI layout
     */
    @Override
    protected int getUIViewId() {
        return R.layout.fragment_ingredient_collection_selection;
    }

    /**
     * Returns the ingredient collection type that is being displayed
     * @return
     */
    @Override
    public CollectionType getCollectionType() {
        if (getArguments() == null)
            throw new IllegalArgumentException("Arguments not supplied to the fragment");
        IngredientCollectionSelectionFragmentArgs argsBundle
                = IngredientCollectionSelectionFragmentArgs.fromBundle(getArguments());
        CollectionType collectionType = argsBundle.getCollectionType();
        return collectionType;
    }

    /**
     * Parses the arguments specified by navigation graph actions.
     * Sets the ingredient collection from the arguments and passes ingredients from forms back
     * to caller
     */
    protected void parseArguments() {
        if (getArguments() == null)
            throw new IllegalArgumentException("Arguments not supplied to the fragment");
        IngredientCollectionSelectionFragmentArgs argsBundle
                = IngredientCollectionSelectionFragmentArgs.fromBundle(getArguments());
        // Get ingredients to filter
        IngredientCollection filterIngredients = argsBundle.getIngredientsToFilter();
        if (filterIngredients != null) {
            for (Ingredient ing : filterIngredients.getIngredients()) {
                int index = ingredientCollection.getIngredients().indexOf(ing);
                if (index != -1) {
                    ingredientCollection.deleteIngredient(index);
                }
            }
        }
        getArguments().clear();
    }

    private void setSaveButtonListener() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendIngredientsToCallerFragment();
            }
        });
    }

    /**
     * Selects the clicked ingredient and sends it to caller fragment
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    protected void onClickIngredient(AdapterView<?> parent, View view, int position, long id) {
        ((IngredientStorageViewAdapter) collectionViewAdapter).selectItem(position);
        Ingredient selectedIngredient = collectionViewAdapter.getItem(position);

        // If already selected, unselect it
        if (chosenIngredients.getIngredients().contains(selectedIngredient)) {
            chosenIngredients.getIngredients().remove(selectedIngredient);
        }
        // If not, add it to selection
        else {
            chosenIngredients.addIngredient(selectedIngredient);
        }
    }

    /**
     * Sends the ingredient to the caller fragment
     */
    void sendIngredientsToCallerFragment() {
        Navigation.findNavController(getView()).getPreviousBackStackEntry().getSavedStateHandle().set(
                RESULT_KEY,
                chosenIngredients
        );
        Navigation.findNavController(getView()).popBackStack();
    }
}