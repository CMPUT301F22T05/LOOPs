package com.example.loops.ingredientFragments.forms;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import android.view.View;

import com.example.loops.models.Ingredient;

/**
 * An ingredient form for adding ingredients.
 */
public class AddIngredientFormFragment extends IngredientFormFragment {
    public static final String RESULT_KEY = "ADD_INGREDIENT_FORM_FRAGMENT_RESULT_KEY";

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
     * Sends back the result through navcontroller's saved state handle with key RESULT_KEY
     * @param submittedIngredient ingredient submitted by the form
     */
    protected void sendResult(Ingredient submittedIngredient) {
        Navigation.findNavController(getView()).getPreviousBackStackEntry().getSavedStateHandle().set(
                RESULT_KEY,
                submittedIngredient
        );
        Navigation.findNavController(getView()).popBackStack();
    }
}