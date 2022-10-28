package com.example.loops;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import static com.example.loops.AddIngredientFormFragmentDirections.*;

import android.view.View;

/**
 * An ingredient form for adding ingredients.
 */
public class AddIngredientFormFragment extends IngredientFormFragment {

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
        // FIXME: Make this more flexible by sending the result back to the fragment that opened
        // this form, not hardcoded to be the ingredient collection view.
        SubmitIngredientToCollection toSubmitAction =
                AddIngredientFormFragmentDirections
                .submitIngredientToCollection();
        toSubmitAction.setAddedIngredient(submittedIngredient);
        Navigation.findNavController(getView()).navigate(toSubmitAction);
    }
}