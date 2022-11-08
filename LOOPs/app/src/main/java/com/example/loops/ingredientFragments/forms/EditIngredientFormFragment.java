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
    private Ingredient editIngredient;
    private int editIngredientInd;

    public EditIngredientFormFragment() { }

    /**
     * Set up event listeners and sets button text
     * @param formView view of the edit ingredient form
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View formView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(formView, savedInstanceState);
        submitButton.setText("Update");

        initializeFormWithIngredientAttributes();
    }

    /**
     * Get the ingredient to edit & set up the text views in edit text.
     */
    public void initializeFormWithIngredientAttributes() {
        editIngredient = EditIngredientFormFragmentArgs.fromBundle(getArguments())
                .getEditedIngredient();
        editIngredientInd = EditIngredientFormFragmentArgs.fromBundle(getArguments())
                .getEditIngredientIndex();

        // pre-fill all fields
        descriptionInput.setText(editIngredient.getDescription());
        bestBeforeDateInput.setText(editIngredient.getBestBeforeDateString());
        locationInput.setSelection(getSpinnerIndexByValue(editIngredient.getStoreLocation(), locationInput));
        amountInput.setText(Double.toString(editIngredient.getAmount()));
        unitInput.setSelection(getSpinnerIndexByValue(editIngredient.getUnit(), unitInput));
        categoryInput.setSelection(getSpinnerIndexByValue(editIngredient.getCategory(), categoryInput));
    }

    /**
     * Send the edited ingredient back to previous fragment.
     * @param submittedIngredient ingredient that is submitted by the form
     */
    void sendResult(Ingredient submittedIngredient) {
        Integer callerFragmentId = getCallerFragmentId();

        if ( callerFragmentId == null ) {
            throw new Error("No caller fragment");
        }
        else if ( callerFragmentId == R.id.ingredientFragment ) {
            NavDirections updateIngredientAction = EditIngredientFormFragmentDirections
                    .actionEditIngredientFormFragmentToIngredientFragment(
                            submittedIngredient, editIngredientInd, R.layout.fragment_ingredient_form);
            Navigation.findNavController(getView()).navigate(updateIngredientAction);
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