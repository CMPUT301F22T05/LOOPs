package com.example.loops;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.View;

/**
 * An ingredient form for adding ingredients.
 */
public class AddIngredientFormFragment extends IngredientFormFragment {

    public AddIngredientFormFragment() { }

    /**
     * Set up event listeners and changes button text
     * @param formView view of the add ingredient form
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View formView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(formView, savedInstanceState);
        submitButton.setText("Add");
    }

    /**
     * Sends ingredient to IngredientCollectionFragment through navigation graph action
     * @param submittedIngredient ingredient submitted by the form
     */
    void sendResult(Ingredient submittedIngredient) {
        Integer callerFragmentId = getCallerFragmentId();

        if ( callerFragmentId == null ) {
            throw new Error("No caller fragment");
        }
        else if ( callerFragmentId == R.id.ingredientCollectionEditorFragment ) {
            AddIngredientFormFragmentDirections.SubmitIngredientToCollection toSubmitAction =
                AddIngredientFormFragmentDirections.submitIngredientToCollection();
            toSubmitAction.setAddedIngredient(submittedIngredient);
            Navigation.findNavController(getView()).navigate((NavDirections) toSubmitAction);
        }
//        else if (callerFragmentId == R.id.addRecipeFormFragment){
//
//        }
        else {
            throw new Error("Navigation action not defined");
        }

    }

    /**
     * Returns the id of the previous fragment
     * @return id of previous fragment. If no such fragment then null
     */
    private Integer getCallerFragmentId() {
        NavController navController = Navigation.findNavController(getView());
        NavBackStackEntry previousFragment = navController.getPreviousBackStackEntry();
        if (previousFragment == null)
            return null;
        return previousFragment.getDestination().getId();
    }

}