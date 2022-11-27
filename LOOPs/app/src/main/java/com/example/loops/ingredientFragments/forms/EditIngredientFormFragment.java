package com.example.loops.ingredientFragments.forms;

import static java.lang.String.valueOf;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.View;

import com.example.loops.R;
import com.example.loops.models.Ingredient;

/**
 * An ingredient form for editing ingredients. Supply the ingredient to edit through action args
 */
public class EditIngredientFormFragment extends IngredientFormFragment {
    public static final String RESULT_KEY = "EDIT_INGREDIENT_FORM_FRAGMENT_RESULT_KEY";
    private Ingredient editIngredient;

//    public EditIngredientFormFragment() { }

    /**
     * Set up event listeners and sets button text
     * @param formView view of the edit ingredient form
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View formView, @Nullable Bundle savedInstanceState) {
        initializeFormWithIngredientAttributes();
        super.onViewCreated(formView, savedInstanceState);
        submitButton.setText("Update");
    }

    /**
     * Returns the default category to show in the category spinner by default
     * @return the default category option
     */
    @Override
    protected String getDefaultCategory() {
        return editIngredient.getCategory();
    }

    /**
     * Returns the default location to show in the location spinner by default
     * @return the default location option
     */
    @Override
    protected String getDefaultLocation() {
        return editIngredient.getStoreLocation();
    }

    /**
     * Get the ingredient to edit & set up the text views in edit text.
     */
    public void initializeFormWithIngredientAttributes() {
        editIngredient = EditIngredientFormFragmentArgs.fromBundle(getArguments())
                .getEditedIngredient();

        // pre-fill all fields
        descriptionInput.setText(editIngredient.getDescription());
        bestBeforeDateInput.setText(editIngredient.getBestBeforeDateString());
        locationInput.setSelection(getSpinnerIndexByValue(editIngredient.getStoreLocation(), locationInput));
        amountInput.setText(Double.toString(editIngredient.getAmount()));
        unitInput.setSelection(getSpinnerIndexByValue(editIngredient.getUnit(), unitInput));
        categoryInput.setSelection(getSpinnerIndexByValue(editIngredient.getCategory(), categoryInput));

        // if this ingredient is pending, leave best before date, location, amount, & unit blank
        if (editIngredient.getPending()) {
            bestBeforeDateInput.setText("");
            locationInput.setSelection(0);
            amountInput.setText("");
            unitInput.setSelection(0);
        }
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