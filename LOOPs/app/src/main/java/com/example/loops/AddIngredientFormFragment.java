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
    public static final String RESULT_KEY = "ADD_INGREDIENT_FORM_FRAGMENT_RESULT_KEY";
    public static final String INGREDIENT_KEY = "ADD_INGREDIENT_FORM_FRAGMENT_RESULT_KEY_INGREDIENT";

    public AddIngredientFormFragment() { }

    /**
     * Set up event listeners and changes button text
     * @param formView
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View formView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(formView, savedInstanceState);
        submitButton.setText("Add");
    }

    /**
     * Sends ingredient to IngredientCollectionFragment through navigation graph action
     * @param submittedIngredient
     */
    void sendResult(Ingredient submittedIngredient) {
        Integer callerFragmentId = getCallerFragmentId();

        if ( callerFragmentId == null ) {
            Bundle resultBundle = new Bundle();
            resultBundle.putSerializable(INGREDIENT_KEY, submittedIngredient);
            getParentFragmentManager().setFragmentResult(RESULT_KEY, resultBundle);
        }
        else if ( callerFragmentId == R.id.ingredientCollectionEditorFragment ) {
            AddIngredientFormFragmentDirections.SubmitIngredientToCollection toSubmitAction =
                AddIngredientFormFragmentDirections.submitIngredientToCollection();
            toSubmitAction.setAddedIngredient(submittedIngredient);
            Navigation.findNavController(getView()).navigate((NavDirections) toSubmitAction);
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