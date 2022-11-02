package com.example.loops;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import java.util.Date;

/**
 * Ingredient collection fragment for showing the ingredients in storage
 */
public class IngredientCollectionEditorFragment extends IngredientCollectionFragment {
    public static final int FROM_STORAGE = 0;
    public static final int FROM_TESTING = 1;   // FIXME: temporary. Just to show it works

    public IngredientCollectionEditorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = super.onCreateView(inflater, container, savedInstanceState);
        collectionTitle.setText(R.string.ingredientCollection);
        return fragmentView;
    }

    void parseActionArguments() {
        getIngredientCollectionToDisplay();

        IngredientCollectionEditorFragmentArgs args = IngredientCollectionEditorFragmentArgs.fromBundle(getArguments());
        if (args.getAddedIngredient() != null) {
            ingredientCollection.addIngredient(args.getAddedIngredient());
        }
        else if (args.getEditedIngredient() != null) {
            if (args.getDeleteFlag() == false) { //update ingredient
                ingredientCollection.updateIngredient(args.getEditedIngredientIndex(), args.getEditedIngredient());
            }
            else {
                ingredientCollection.deleteIngredient(args.getEditedIngredientIndex());
            }
        }
        getArguments().clear();
    }

    void onClickAddButton(View clickedView) {
        Navigation.findNavController(getView()).navigate(R.id.addIngredientFromCollection);
    }

    void onClickIngredient(AdapterView<?> parent, View view, int position, long id) {
        Ingredient selectedIngredient = ingredientCollection.getIngredients().get(position);
        NavDirections viewIngredientDetailsAction =
                (NavDirections)IngredientCollectionEditorFragmentDirections.actionViewIngredientDetails(
                        selectedIngredient, position, R.layout.fragment_ingredient_collection);
        Navigation.findNavController(view).navigate(viewIngredientDetailsAction);
    }

    void getIngredientCollectionToDisplay() {
        Bundle argsBundle = getArguments();
        int collectionTypeId = IngredientCollectionEditorFragmentArgs.fromBundle(argsBundle).getCollectionType();
        switch(collectionTypeId) {
            case -1:    // FIXME: hardcoded to this for now
                Log.e("TESTING", "-1 in getIngredientCollectionToDisplay in edition");
            case FROM_STORAGE:
                ingredientCollection = ((MainActivity)getActivity()).allIngredients;
                break;
            case FROM_TESTING:
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
                break;
            case -5:
                break;
            default:
                throw new RuntimeException("Invalid collection type id for ingredient selection fragment");
        }
    }
}
