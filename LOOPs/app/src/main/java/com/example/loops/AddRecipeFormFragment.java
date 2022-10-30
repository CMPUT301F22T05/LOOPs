package com.example.loops;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

/**
 * An ingredient form for adding ingredients.
 */
public class AddRecipeFormFragment extends RecipeFormFragment {

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
     * Sends ingredient to IngredientCollectionFragment through navigation graph action
     * @param submittedRecipe
     */
    void sendResult(Recipe submittedRecipe) {
        // FIXME: Make this more flexible by sending the result back to the fragment that opened
        // this form, not hardcoded to be the ingredient collection view.
        /*
        AddRecipeFormFragmentDirections.SubmitIngredientToCollection toSubmitAction =
                AddRecipeFormFragmentDirections
                .submitRecipeToCollection();
        toSubmitAction.setAddedRecipe(submittedRecipe);
        Navigation.findNavController(getView()).navigate(toSubmitAction);
        */
    }
}