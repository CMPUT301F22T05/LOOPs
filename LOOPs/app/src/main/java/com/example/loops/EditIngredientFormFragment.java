package com.example.loops;

import static java.lang.String.valueOf;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.View;

/**
 * An ingredient form for editing ingredients. Supply the ingredient to edit through action args
 */
public class EditIngredientFormFragment extends IngredientFormFragment {

    private Ingredient editIngredient;

    public EditIngredientFormFragment() { }

    /**
     * Set up event listeners and sets button text
     * @param formView
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View formView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(formView, savedInstanceState);
        submitButton.setText("update");

        initializeFormWithIngredientAttributes();
    }

    // TODO: Implement this
    public void initializeFormWithIngredientAttributes() {
        editIngredient = EditIngredientFormFragmentArgs.fromBundle(getArguments())
                .getEditedIngredient();

        // pre-fill all fields
        descriptionInput.setText(editIngredient.getDescription());
        bestBeforeDateInput.setText(editIngredient.getBestBeforeDateString());
        locationInput.setSelection(getSpinnerIndexByValue(editIngredient.getStoreLocation(), locationInput));
        amountInput.setText(valueOf(editIngredient.getAmount()));
        unitInput.setSelection(getSpinnerIndexByValue(editIngredient.getUnit(), unitInput));
        categoryInput.setSelection(getSpinnerIndexByValue(editIngredient.getCategory(), categoryInput));
    }

    // TODO: Implement this
    void sendResult(Ingredient submittedIngredient) {
        NavDirections updateIngredientAction = EditIngredientFormFragmentDirections
                .actionEditIngredientFormFragmentToIngredientFragment(submittedIngredient, 0);
        Navigation.findNavController(getView()).navigate(updateIngredientAction);
    }
}