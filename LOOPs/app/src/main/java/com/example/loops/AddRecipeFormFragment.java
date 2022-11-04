package com.example.loops;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

/**
 * A recipe form for adding recipes.
 */
public class AddRecipeFormFragment extends RecipeFormFragment {

    // Empty constructor
    public AddRecipeFormFragment() { }

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
     * Sends recipe to RecipeCollectionFragment through navigation graph action
     * @param submittedRecipe
     */
    void sendResult(Recipe submittedRecipe) {
        // FIXME: Make this more flexible by sending the result back to the fragment that opened
        // this form, not hardcoded to be the recipe collection view.
        /*
        AddRecipeFormFragmentDirections.SubmitIngredientToCollection toSubmitAction =
                AddRecipeFormFragmentDirections
                .submitRecipeToCollection();
        toSubmitAction.setAddedRecipe(submittedRecipe);
        Navigation.findNavController(getView()).navigate(toSubmitAction);
        Integer callerFragmentId = getCallerFragmentId();
        */
        Integer callerFragmentId = getCallerFragmentId();

        if ( callerFragmentId == null ) {
            throw new Error("No caller fragment");
        }
        else if ( callerFragmentId == R.id.recipeCollectionEditorFragment ) {
            AddRecipeFormFragmentDirections.SubmitRecipeToCollection toSubmitAction =
                    AddRecipeFormFragmentDirections.submitRecipeToCollection();
            toSubmitAction.setAddedRecipe(submittedRecipe);
            Navigation.findNavController(getView()).navigate((NavDirections) toSubmitAction);
        }
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